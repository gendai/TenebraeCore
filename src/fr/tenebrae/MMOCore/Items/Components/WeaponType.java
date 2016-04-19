package fr.tenebrae.MMOCore.Items.Components;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Utils.SQLHelper;

public enum WeaponType {

	SWORD_1H(30006),
	SWORD_2H(30007),
	AXE_1H(30008),
	AXE_2H(30009),
	STAFF(30012),
	BOW(30011),
	DAGGER(30010);
	
	private int id = 0;
	
	private WeaponType(int stringId) {
		this.id = stringId;
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
