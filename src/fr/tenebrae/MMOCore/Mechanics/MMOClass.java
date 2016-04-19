package fr.tenebrae.MMOCore.Mechanics;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Utils.SQLHelper;

public enum MMOClass {

	WARRIOR(60000),
	ARCHER(60001),
	ASSASSIN(60002),
	MAGE(60003);
	
	private int id = 0;
	
	private MMOClass(int id) {
		this.id = id;
	}
	
	public String getString(String language) {
		String returned = "null";
		try {
			ResultSet nameRow = SQLHelper.getSortedEntrys(Main.DB_DATABASE, Main.DB_STRING_TEMPLATE, "entry", id);
			if (!nameRow.next()) throw new SQLException("String template did not contained the requested entry ("+id+")");
			if (nameRow.isAfterLast()) throw new SQLException("String template did not contained the requested entry ("+id+")");
			returned = nameRow.getString(language);
		} catch (Exception e) { e.printStackTrace(); }
		if (returned == null) returned = "null";
		return returned;
	}
}
