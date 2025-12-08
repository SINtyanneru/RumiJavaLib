package su.rumishistem.rumi_java_lib;

import java.sql.*;
import java.util.Map;
import java.util.function.BiConsumer;

public class SQLC {
	private Connection connection;
	private boolean auto_commit = false;

	public SQLC(Connection connection, boolean auto_commit) {
		this.connection = connection;
		this.auto_commit = auto_commit;
	}

	public void update_execute(String script, Object[] param_list) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(script);
		stmt_set_param(stmt, param_list);
		stmt.executeUpdate();

		if (auto_commit) {
			connection.commit();
			connection.close();
		}
	}

	public ArrayNode select_execute(String script, Object[] param_list) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(script);
		stmt_set_param(stmt, param_list);

		ResultSet result = stmt.executeQuery();
		ResultSetMetaData meta = result.getMetaData();
		final int col_count = meta.getColumnCount();

		int data_i = 0;
		ArrayNode data = new ArrayNode();

		while (result.next()) {
			ArrayNode row = new ArrayNode();
			for (int i = 0; i < col_count; i++) {
				String name = meta.getColumnName(i);
				Object value = result.getObject(i);
				row.setDATA(name, new ArrayData(value));
			}

			data.setDATA(data_i, row);
			data_i += 1;
		}

		return data;
	}

	public void begin() throws SQLException {
		//無いと違和感しか無いので置いてあるだけの関数
	}

	public void commit() throws SQLException {
		connection.commit();
		connection.close();
	}

	public void rollback() throws SQLException {
		connection.rollback();
		connection.close();
	}

	private void stmt_set_param(PreparedStatement stmt, Object[] param_list) throws SQLException {
		for (int i = 0; i < param_list.length; i++) {
			Object param = param_list[i];
			Class<?> c = param.getClass();

			if (param == null) {
				stmt.setNull(i+1, Types.NULL);
				continue;
			}

			if (c == String.class) {
				stmt.setString(i+1, (String) param);
			} else if (c == Integer.class) {
				stmt.setInt(i+1, (Integer) param);
			} else if (c == Long.class) {
				stmt.setLong(i+1, (Long) param);
			} else if (c == Boolean.class) {
				stmt.setBoolean(i+1, (Boolean) param);
			} else if (c == byte[].class) {
				stmt.setBytes(i+1, (byte[]) param);
			} else if (c == Byte.class) {
				stmt.setByte(i+1, (Byte) param);
			} else {
				throw new RuntimeException(c.getSimpleName() + "という型は未対応です");
			}
		}
	}
}
