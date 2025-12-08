package su.rumishistem.rumi_java_lib;

import java.sql.*;

public class SQL {
	private static String sql_url;
	private static String sql_user;
	private static String sql_password;

	public static void connect(String host, String port, String db, String user, String password){
		sql_url = "jdbc:mariadb://"+host+":"+port+"/"+db+"?useServerPrepStmts=false&cachePrepStmts=false";
		sql_user = user;
		sql_password = password;
	}

	public static SQLC new_connection() throws SQLException {
		Connection connection = (Connection) DriverManager.getConnection(sql_url, sql_user, sql_password);
		//未コミットのトランザクションは見えなくなるよ
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		connection.setAutoCommit(false);

		SQLC sqlc = new SQLC(connection, false);
		return sqlc;
	}

	public static SQLC new_auto_commit_connection() throws SQLException {
		Connection connection = (Connection) DriverManager.getConnection(sql_url, sql_user, sql_password);
		//未コミットのトランザクションは見えなくなるよ
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

		SQLC sqlc = new SQLC(connection, true);
		return sqlc;
	}
}
