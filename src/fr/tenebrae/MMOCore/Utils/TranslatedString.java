package fr.tenebrae.MMOCore.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.SQLResultSet;
import fr.tenebrae.PlayerLanguage.LanguageAPI;

public class TranslatedString {

	public static String getString(int id, String language) {
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
	
	public static String getString(int id) {
		return getString(id, "english");
	}
	
	public static String getString(int id, Player p) {
		return getString(id, LanguageAPI.getStringLanguage(p));
	}
}
