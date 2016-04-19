package fr.tenebrae.MMOCore.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.tenebrae.MMOCore.Main;

public class SQLHelper {
	
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
		Main.plugin.database.updateSQL(request);
	}
	
	public static ResultSet getSortedEntrys(String dbname, String tableName, String column, Object value) throws SQLException, ClassNotFoundException {
		String request = "SELECT * FROM `"+dbname+"`.`"+tableName+"` WHERE `"+column+"` = '"+value+"';";
		return Main.plugin.database.querySQL(request);
	}
	
	public static ResultSet getAllEntrys(String dbname, String tableName) throws SQLException, ClassNotFoundException {
		String request = "SELECT * FROM `"+dbname+"`.`"+tableName+"`";
		return Main.plugin.database.querySQL(request);
	}
	
	public static void clearEntries(String dbname, String tableName) throws SQLException, ClassNotFoundException {
		String request = "DELETE * FROM `"+dbname+"`.`"+tableName+"`";
		Main.plugin.database.updateSQL(request);
	}
	
	public static void updateEntry(String dbname, String tableName, String columnToSeek, Object valueToSeek, String[] columnsToUpdate, Object[] valuesToUpdate) throws SQLException, ClassNotFoundException {
		String request = "UPDATE `"+dbname+"`.`"+tableName+"` SET ";
		int marker = 0;
		for (String s : columnsToUpdate) {
			marker++;
			request = request+"`"+s+"` = '"+valuesToUpdate[marker-1]+"'"+(marker == columnsToUpdate.length ? " WHERE " : " , ");
		}
		request = request+"`"+columnToSeek+"` = '"+valueToSeek+"'";
		Main.plugin.database.updateSQL(request);
	}
}