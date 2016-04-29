package fr.tenebrae.MMOCore.Utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.SQLResultSet;

public class SQLHelper {
	
	public static void updateSQL(String request) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = Main.db.getmysql();
			stmt = conn.createStatement();
			stmt.executeUpdate(request);
		} catch (SQLException e) {
			throw e;
		} finally {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}
	}
	
	public static SQLResultSet querySQL(String request) throws SQLException {
		Connection conn = Main.db.getmysql();
		Statement stmt = conn.createStatement();
		return new SQLResultSet(conn, stmt, stmt.executeQuery(request));
	}
	
	
	public static void insertEntry(String dbname, String tableName, String[] columns, Object[] values) throws SQLException, ClassNotFoundException {
		String request = "INSERT INTO `"+dbname+"`.`"+tableName+"` (";
		int marker = 0;
		for (String s : columns) {
			marker++;
			request = request+s+(marker == columns.length ? ") " : ", ");
		}
		request = request+"VALUES (";
		marker = 0;
		for (Object o : values) {
			marker++;
			request = request+"'"+String.valueOf(o)+"'"+(marker == values.length ? ") " : ", ");
		}
		request = request+";";
		updateSQL(request);
	}
	
	public static SQLResultSet getSortedEntrys(String dbname, String tableName, String column, Object value) throws SQLException, ClassNotFoundException {
		String request = "SELECT * FROM `"+dbname+"`.`"+tableName+"` WHERE `"+column+"` = '"+value+"';";
		return querySQL(request);
	}
	
	public static SQLResultSet getAllEntrys(String dbname, String tableName) throws SQLException, ClassNotFoundException {
		String request = "SELECT * FROM `"+dbname+"`.`"+tableName+"`";
		return querySQL(request);
	}
	
	public static void clearEntries(String dbname, String tableName) throws SQLException, ClassNotFoundException {
		String request = "DELETE * FROM `"+dbname+"`.`"+tableName+"`";
		updateSQL(request);
	}
	
	public static void updateEntry(String dbname, String tableName, String columnToSeek, Object valueToSeek, String[] columnsToUpdate, Object[] valuesToUpdate) throws SQLException, ClassNotFoundException {
		String request = "UPDATE `"+dbname+"`.`"+tableName+"` SET ";
		int marker = 0;
		for (String s : columnsToUpdate) {
			marker++;
			request = request+"`"+s+"` = '"+valuesToUpdate[marker-1]+"'"+(marker == columnsToUpdate.length ? " WHERE " : " , ");
		}
		request = request+"`"+columnToSeek+"` = '"+valueToSeek+"'";
		updateSQL(request);
	}
}