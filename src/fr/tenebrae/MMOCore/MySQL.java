package fr.tenebrae.MMOCore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.plugin.Plugin;

public class MySQL extends Database {
   public String user;
   public String database;
   public String password;
   public String port;
   public String hostname;

   public MySQL(Plugin plugin, String hostname, String port, String database, String username, String password) {
      super(plugin);
      this.hostname = hostname;
      this.port = port;
      this.database = database;
      this.user = username;
      this.password = password;
   }
   
   public MySQL(Plugin plugin, String hostname, int port, String database, String username, String password) {
	      super(plugin);
	      this.hostname = hostname;
	      this.port = String.valueOf(port);
	      this.database = database;
	      this.user = username;
	      this.password = password;
	   }

   public Connection openConnection() throws SQLException, ClassNotFoundException {
      if(this.checkConnection()) {
         return this.connection;
      } else {
         this.connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.user, this.password);
         return this.connection;
      }
   }
}
