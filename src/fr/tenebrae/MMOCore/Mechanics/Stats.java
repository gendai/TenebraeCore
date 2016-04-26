package fr.tenebrae.MMOCore.Mechanics;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.SQLResultSet;
import fr.tenebrae.MMOCore.Utils.SQLHelper;


public enum Stats {
	
	HEALTH(50007),
	MANA(50008),
	STRENGTH(50002),
	STAMINA(50005),
	AGILITY(50006),
	INTELLIGENCE(50003),
	SPIRIT(50004),
	ARMOR(50000),
	MAGICAL_ARMOR(50009),
	POWER(50010),
	MAGICAL_POWER(50011),
	CRITICAL_CHANCE(50012),
	DODGE_CHANCE(50013),
	HIT_CHANCE(50014),
	BLOCK_CHANCE(50015),
	ATTACK_SPEED(50001),
	XP_BONUS(50016);

	private int id = 0;
	
	private Stats(int id) {
		this.id = id;
	}
	
	public String getString(String language) {
		String returned = "null";
		try {
			SQLResultSet sqlRS = SQLHelper.getSortedEntrys(Main.DB_DATABASE, Main.DB_STRING_TEMPLATE, "entry", id);
			ResultSet nameRow = sqlRS.getResultSet();
			if (!nameRow.next()) throw new SQLException("String template did not contained the requested entry ("+id+")");
			if (nameRow.isAfterLast()) throw new SQLException("String template did not contained the requested entry ("+id+")");
			returned = nameRow.getString(language);
			sqlRS.close();
		} catch (Exception e) { e.printStackTrace(); }
		if (returned == null) returned = "null";
		return returned;
	}
	
	
}
