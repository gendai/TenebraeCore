package fr.tenebrae.MMOCore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLResultSet {
	
	private ResultSet rs = null;
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	
	public SQLResultSet(Connection conn, Statement stmt, ResultSet rs) {
		this.rs = rs;
		this.stmt = stmt;
		this.conn = conn;
	}
	
	public SQLResultSet(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		this.rs = rs;
		this.pstmt = pstmt;
		this.conn = conn;
	}
	
	public ResultSet getResultSet() {
		return this.rs;
	}
	
	public Connection getConnection() {
		return this.conn;
	}
	
	public void close() throws SQLException {
		if (this.rs != null)
			this.rs.close();
		if (this.stmt != null)
			this.stmt.close();
		if (this.pstmt != null)
			this.pstmt.close();
		if (this.conn != null)
			this.conn.close();
	}
}