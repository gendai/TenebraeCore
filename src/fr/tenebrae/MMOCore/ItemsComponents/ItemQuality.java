package fr.tenebrae.MMOCore.ItemsComponents;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.md_5.bungee.api.ChatColor;
import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Utils.SQLHelper;

public enum ItemQuality {
	MAUVAIS(30050,ChatColor.GRAY),
	COMMUN(30051,ChatColor.WHITE),
	INHABITUEL(30052,ChatColor.GREEN),
	RARE(30053,ChatColor.BLUE),
	EPIQUE(30054,ChatColor.DARK_PURPLE),
	LEGENDAIRE(30055,ChatColor.GOLD),
	ARTEFACT(30056,ChatColor.YELLOW),
	HERITAGE(30057,ChatColor.YELLOW);
	
	private int id;
	private ChatColor color;
	
	private ItemQuality(int id, ChatColor color) {
		this.id = id;
		this.color = color;
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
	
	public ChatColor getColor() {
		return color;
	}
}
