package com.rumisystem.rumi_java_lib;

import com.rumisystem.rumi_java_lib.LOG_PRINT.LOG_TYPE;
import com.rumisystem.rumi_java_lib.Misskey.API.I;

import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.rumisystem.rumi_java_lib.LOG_PRINT.Main.LOG;

public class SQL {
	private static Connection CONNECT = null;
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
		CONNECT = (Connection) DriverManager.getConnection(URL, SQL_USER, SQL_PASS);

		//自動コミットOFF
		CONNECT.setAutoCommit(false);

		//分離レベルをCOMMITTEDに
		CONNECT.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

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
				if (CONNECT == null || CONNECT.isClosed() || !CONNECT.isValid(2)) {
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

	public static synchronized ArrayNode RUN(String SQL_SCRIPT, Object[] PARAMS) {
		//接続チェック
		GetConnect();

		try{
			//コミット(最新の状態に)
			CONNECT.commit();

			//SELECT文の実行
			PreparedStatement STMT = CONNECT.prepareStatement(SQL_SCRIPT);

			for(int I = 0; I < PARAMS.length; I++){
				Object PARAM = PARAMS[I];

				//型に寄って動作をかえる
				if(PARAM instanceof String){
					//Stringなら
					STMT.setString(I + 1, (String)PARAM);
				} else if(PARAM instanceof Integer){
					//Intなら
					STMT.setInt(I + 1, (int)PARAM);
				} else if (PARAM instanceof Long) {
					STMT.setLong(I + 1, (long)PARAM);
				} else if (PARAM instanceof  Boolean) {
					STMT.setBoolean(I + 1, (boolean)PARAM);
				} else if (PARAM instanceof byte[]) {
					STMT.setBytes(I + 1, (byte[])PARAM);
				} else if (PARAM instanceof Byte) {
					STMT.setByte(I + 1, (byte) PARAM);
				}
			}

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

					ROW.setDATA(COLUM_NAME, VAL);
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
		} catch (SQLException e) {
			//エラー
			LOG(LOG_TYPE.FAILED, "SQL Error [" + SQL_SCRIPT + "]");
			e.printStackTrace();
			return null;
		}
	}

	public static synchronized void UP_RUN(String SQL_SCRIPT, Object[] PARAMS) throws SQLException {
		//接続チェック
		GetConnect();

		//SELECT文の実行
		PreparedStatement STMT = CONNECT.prepareStatement(SQL_SCRIPT);

		for(int I = 0; I < PARAMS.length; I++){
			Object PARAM = PARAMS[I];
			//型に寄って動作をかえる
			if(PARAM instanceof String){//Stringなら
				STMT.setString(I + 1, PARAM.toString());
			} else if(PARAM instanceof Integer){//Intなら
				STMT.setInt(I + 1, (int)PARAM);
			} else if (PARAM instanceof Long) {
				STMT.setLong(I + 1, (long)PARAM);
			} else if (PARAM instanceof  Boolean) {
				STMT.setBoolean(I + 1, (boolean)PARAM);
			}
		}

		// 実行
		int rowsAffected = STMT.executeUpdate();

		CONNECT.commit();
	}
}
