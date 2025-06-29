package su.rumishistem.rumi_java_lib;

import su.rumishistem.rumi_java_lib.LOG_PRINT.LOG_TYPE;
import java.sql.*;
import static su.rumishistem.rumi_java_lib.LOG_PRINT.Main.LOG;

public class SQL {
	private static Connection SQLConnet = null;
	private static String SQL_IP;
	private static String SQL_PORT;
	private static String SQL_DB;
	private static String SQL_USER;
	private static String SQL_PASS;

	private static final String SQL_PARAM = "?useServerPrepStmts=false&cachePrepStmts=false";

	public static void CONNECT(String IP, String PORT, String DB, String USER, String PASS){
		try {
			//接続情報をSQLに
			SQL_IP = IP;
			SQL_PORT = PORT;
			SQL_DB = DB;
			SQL_USER = USER;
			SQL_PASS = PASS;

			//接続
			CONNECT();
		} catch (SQLException E) {
			//エラー
			LOG(LOG_TYPE.FAILED, "SQL Connection Error");
			E.printStackTrace();
		}
	}

	private static void CONNECT() throws SQLException {
		//接続文字列
		String URL = "jdbc:mariadb://" + SQL_IP + ":" + SQL_PORT + "/" + SQL_DB + SQL_PARAM;

		//MariaDBへ接続
		SQLConnet = (Connection) DriverManager.getConnection(URL, SQL_USER, SQL_PASS);

		//自動コミットOFF
		SQLConnet.setAutoCommit(false);

		//分離レベルをCOMMITTEDに
		SQLConnet.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

		LOG(LOG_TYPE.OK, "SQL Connected");
	}

	private static void ReCONNECT() throws SQLException {
		//MariaDBへ接続
		CONNECT();

		LOG(LOG_TYPE.OK, "SQL ReConnected");
	}

	private static synchronized boolean GetConnect() {
		int RETRY = 0;
		int MAX_RETRY = 5;
		long DELAY = 5000; // 5秒

		while (RETRY < MAX_RETRY) {
			try {
				if (SQLConnet == null || SQLConnet.isClosed() || !SQLConnet.isValid(2)) {
					ReCONNECT();
					return true;
				} else {
					return true;
				}
			} catch (Exception ex) {
				RETRY++;
				LOG(LOG_TYPE.FAILED, "Retrying SQL connection... (" + MAX_RETRY + "/" + RETRY + ")");
				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}

		LOG(LOG_TYPE.FAILED, "Failed to reconnect to SQL after " + MAX_RETRY + " attempts.");
		return false;
	}

	private static synchronized void STMTSetParam(PreparedStatement STMT, Object[] ParamList) throws SQLException {
		for (int I = 0; I < ParamList.length; I++) {
			Object Param = ParamList[I];
			//型に寄って動作をかえる
			if(Param instanceof String){
				//Stringなら
				STMT.setString(I + 1, (String)Param);
			} else if(Param instanceof Integer){
				//Intなら
				STMT.setInt(I + 1, (int)Param);
			} else if (Param instanceof Long) {
				STMT.setLong(I + 1, (long)Param);
			} else if (Param instanceof  Boolean) {
				STMT.setBoolean(I + 1, (boolean)Param);
			} else if (Param instanceof byte[]) {
				STMT.setBytes(I + 1, (byte[])Param);
			} else if (Param instanceof Byte) {
				STMT.setByte(I + 1, (byte) Param);
			} else if (Param == null) {
				STMT.setNull(I + 1, Types.NULL);
			} else {
				throw new Error(Param.getClass().getSimpleName() + "という型は非対応です");
			}
		}
	}

	public static synchronized ArrayNode RUN(String SQLScript, Object[] ParamList) throws SQLException {
		//接続チェック
		GetConnect();

		//コミット(最新の状態に)
		SQLConnet.commit();

		//実行の準備ー
		PreparedStatement STMT = SQLConnet.prepareStatement(SQLScript);

		//パラメーター
		STMTSetParam(STMT, ParamList);

		//SQLを実行し、結果を入れる
		ResultSet SQL_RESULT = STMT.executeQuery();

		//SQLの結果をArrayNode化する
		ArrayNode ARRAY_NODE = new ArrayNode();

		ResultSetMetaData META_DATA = SQL_RESULT.getMetaData();
		int COLUM_COUNT = META_DATA.getColumnCount();
		int ARRAY_NODE_I = 0;

		while (SQL_RESULT.next()) {
			ArrayNode ROW = new ArrayNode();

			for (int I = 1; I <= COLUM_COUNT; I++) {
				String COLUM_NAME = META_DATA.getColumnLabel(I);
				Object VAL = SQL_RESULT.getObject(I);

				ROW.setDATA(COLUM_NAME, new ArrayData(VAL));
			}

			//ArrayNodeに追加
			ARRAY_NODE.setDATA(ARRAY_NODE_I, ROW);

			//AI
			ARRAY_NODE_I++;
		}

		//色々閉じる
		STMT.close();
		SQL_RESULT.close();

		return ARRAY_NODE;
	}

	public static synchronized void UP_RUN(String SQLScript, Object[] ParamList) throws SQLException {
		//接続チェック
		GetConnect();

		//実行準備！
		PreparedStatement STMT = SQLConnet.prepareStatement(SQLScript);

		//パラメーター
		STMTSetParam(STMT, ParamList);

		//実行
		STMT.executeUpdate();

		SQLConnet.commit();
	}
}
