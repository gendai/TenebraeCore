package fr.tenebrae.MMOCore.ItemsComponents;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Utils.SQLHelper;

public enum ItemType {
	WEAPON(-1),
	OFFHAND(30013),
	HELMET(30000),
	CHESTPLATE(30001),
	LEGGINGS(30002),
	BOOTS(30003),
	BAG(30014),
	CONSOMMABLE(30015),
	QUEST_ITEM(30016),
	NECKLACE(30004),
	RING(30005),
	OTHER(30017);
	
	private int id = 0;
	
	private ItemType(int stringId) {
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
