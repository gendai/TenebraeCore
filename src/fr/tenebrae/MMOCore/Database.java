package fr.tenebrae.MMOCore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.plugin.Plugin;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

public abstract class Database {
	protected Connection connection;
	protected Plugin plugin;

	protected Database(Plugin plugin) {
		this.plugin = plugin;
		this.connection = null;
	}

	public abstract Connection openConnection() throws SQLException, ClassNotFoundException;

	public boolean checkConnection() throws SQLException {
		return this.connection != null && !this.connection.isClosed();
	}

	public Connection getConnection() {
		return this.connection;
	}

	public boolean closeConnection() throws SQLException {
		if(this.connection == null) {
			return false;
		} else {
			this.connection.close();
			return true;
		}
	}

	public ResultSet querySQL(String query) throws SQLException, ClassNotFoundException {
		try {
			this.openConnection();

			Statement statement = this.connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			return result;
		} catch (CommunicationsException e) {
			try {
				this.openConnection();

				Statement statement = this.connection.createStatement();
				ResultSet result = statement.executeQuery(query);
				return result;
			} catch (CommunicationsException e1) {
				this.openConnection();

				Statement statement = this.connection.createStatement();
				ResultSet result = statement.executeQuery(query);
				return result;
			}
		}
	}

	public int updateSQL(String query) throws SQLException, ClassNotFoundException {
		try {
			this.openConnection();

			Statement statement = this.connection.createStatement();
			int result = statement.executeUpdate(query);
			return result;
		} catch (CommunicationsException e) {
			try {
				this.openConnection();

				Statement statement = this.connection.createStatement();
				int result = statement.executeUpdate(query);
				return result;
			} catch (CommunicationsException e1) {
				this.openConnection();

				Statement statement = this.connection.createStatement();
				int result = statement.executeUpdate(query);
				return result;
			}
		}
	}
}
