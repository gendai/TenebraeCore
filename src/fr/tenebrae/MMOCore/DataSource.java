package fr.tenebrae.MMOCore;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp2.BasicDataSource;

public class DataSource {

	private static DataSource     datasource;
	private BasicDataSource ds;

	private DataSource() throws IOException, SQLException, PropertyVetoException {
		ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUsername(Main.DB_USER);
		ds.setPassword(Main.DB_PASSWORD);
		ds.setUrl("jdbc:mysql://"+Main.DB_HOST+":"+Main.DB_PORT+"/"+Main.DB_DATABASE);

		ds.setMaxTotal(50);
		ds.setMinIdle(5);
		ds.setMaxIdle(20);
		ds.setMaxOpenPreparedStatements(50);
	}

	public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
		if (datasource == null) {
			datasource = new DataSource();
			return datasource;
		} else {
			return datasource;
		}
	}

	public Connection getConnection() throws SQLException {
		return this.ds.getConnection();
	}

	public SQLResultSet querySQL(String query) throws SQLException, ClassNotFoundException {
		final Connection c = this.getConnection();

		Statement statement = c.createStatement();
		ResultSet result = statement.executeQuery(query);

		return new SQLResultSet(c, result);
	}

	public int updateSQL(String query) throws SQLException, ClassNotFoundException {
		Connection c = this.getConnection();

		Statement statement = c.createStatement();
		int result = statement.executeUpdate(query);
		
		c.close();
		return result;
	}

}
