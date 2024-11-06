package com.rumisystem.rumi_java_lib;

import com.rumisystem.rumi_java_lib.LOG_PRINT.LOG_TYPE;

import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.rumisystem.rumi_java_lib.LOG_PRINT.Main.LOG;

public class SQL {
	public static Connection CONNECT = null;
	public static PreparedStatement STMT = null;

	public static void CONNECT(String IP, String PORT, String DB, String USER, String PASS){
		//接続文字列
		String URL = "jdbc:mariadb://" + IP + ":" + PORT + "/" + DB;

		try {
			//MariaDBへ接続
			CONNECT = (Connection) DriverManager.getConnection(URL, USER, PASS);

			//自動コミットOFF
			CONNECT.setAutoCommit(false);

			LOG(LOG_TYPE.OK, "SQL Connected");

			ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

			//1秒ごとに実行されるタスク
			Runnable TASK = () -> {
				LOG(LOG_TYPE.INFO, "SQL PING");

				RUN("SHOW TABLES;", new Object[]{});
			};

			SCHEDULER.scheduleAtFixedRate(TASK, 0, 5, TimeUnit.HOURS);
		} catch (SQLException E) {
			//エラー
			LOG(LOG_TYPE.FAILED, "SQL Connection Error");
			E.printStackTrace();
		}
	}

	public static ArrayNode RUN(String SQL_SCRIPT, Object[] PARAMS) {
		//SQLの実行結果を入れる変数
		ResultSet SQL_RESULT = null;

		try{
			//SELECT文の実行
			STMT = CONNECT.prepareStatement(SQL_SCRIPT);

			for(int I = 0; I < PARAMS.length; I++){
				Object PARAM = PARAMS[I];

				//型に寄って動作をかえる
				if(PARAM instanceof String){
					//Stringなら
					STMT.setString(I + 1, PARAM.toString());
				} else if(PARAM instanceof Integer){
					//Intなら
					STMT.setInt(I + 1, Integer.parseInt(PARAM.toString()));
				} else if (PARAM instanceof Long) {
					STMT.setLong(I + 1, Long.parseLong(PARAM.toString()));
				} else if (PARAM instanceof  Boolean) {
					STMT.setBoolean(I + 1, Boolean.parseBoolean(PARAM.toString()));
				}
			}

			SQL_RESULT = STMT.executeQuery();

			//SQLの結果をArrayNode化する
			ArrayNode ARRAY_NODE = new ArrayNode();

			ResultSetMetaData META_DATA = SQL_RESULT.getMetaData();
			int COLUM_COUNT = META_DATA.getColumnCount();
			int ARRAY_NODE_I = 0;

			while (SQL_RESULT.next()) {
				ArrayNode ROW = new ArrayNode();

				for (int I = 1; I <= COLUM_COUNT; I++) {
					String COLUM_NAME = META_DATA.getColumnName(I);
					Object VAL = SQL_RESULT.getObject(I);

					ROW.setDATA(COLUM_NAME, VAL);
				}

				//ArrayNodeに追加
				ARRAY_NODE.setDATA(ARRAY_NODE_I, ROW);

				//AI
				ARRAY_NODE_I++;
			}

			return ARRAY_NODE;
		} catch (SQLException e) {
			//エラー
			LOG(LOG_TYPE.FAILED, "SQL Error [" + SQL_SCRIPT + "]");
			e.printStackTrace();
			return null;
		}
	}

	public static void UP_RUN(String SQL_SCRIPT, Object[] PARAMS) throws SQLException {
		//SELECT文の実行
		STMT = CONNECT.prepareStatement(SQL_SCRIPT);

		for(int I = 0; I < PARAMS.length; I++){
			Object PARAM = PARAMS[I];
			//型に寄って動作をかえる
			if(PARAM instanceof String){//Stringなら
				STMT.setString(I + 1, PARAM.toString());
			}

			if(PARAM instanceof Integer){//Intなら
				STMT.setInt(I + 1, Integer.parseInt(PARAM.toString()));
			}
		}

		// 実行
		int rowsAffected = STMT.executeUpdate();

		CONNECT.commit();
	}
}
