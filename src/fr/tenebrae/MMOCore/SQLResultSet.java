package fr.tenebrae.MMOCore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLResultSet {
	
	private ResultSet set;
	private Connection c;
	
	public SQLResultSet(Connection c, ResultSet set) {
		this.set = set;
		this.c = c;
	}
	
	public ResultSet getResultSet() {
		return this.set;
	}
	
	public Connection getConnection() {
		return this.c;
	}
	
	public void close() throws SQLException {
		this.set.close();
		this.c.close();
	}
}