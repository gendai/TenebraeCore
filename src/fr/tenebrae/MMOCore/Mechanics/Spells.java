package fr.tenebrae.MMOCore.Mechanics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.SQLResultSet;
import fr.tenebrae.MMOCore.Utils.SQLHelper;



public enum Spells {
	
	HOLY_NOVA(100000, 200000);
	
	private int nameId = 0;
	private int descriptionId = 0;
	
	private Spells(int nameId, int descriptionId) {
		this.nameId = nameId;
		this.descriptionId = descriptionId;
	}
	
	public String getName(String language) {
		return getString(nameId, language);
	}
	
	public List<String> getDescription(String language) {
		return Arrays.asList(getString(descriptionId, language).split("%n"));
	}
	
	private String getString(int id, String language) {
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
