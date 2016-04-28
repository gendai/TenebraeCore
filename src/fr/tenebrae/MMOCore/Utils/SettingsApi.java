package fr.tenebrae.MMOCore.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.bukkit.entity.Player;
import org.json.JSONObject;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Exception.UnsetDataException;
import redis.clients.jedis.Jedis;

public class SettingsApi {

	public static void setSettings(Player player, JSONObject json) throws Exception {
		String uuid = player.getUniqueId().toString();
		Connection conn = null;
		PreparedStatement pst = null;
		Jedis rconn = null;
		try {
			conn = Main.db.getmysql();
			pst = conn.prepareStatement("UPDATE settings, user SET settings.data WHERE user.uuid=? AND user.uid=settings.uid");
			
			pst.setString(1, json.toString());
			pst.setString(2, uuid);
			
			pst.executeUpdate();
			
			rconn = Main.db.getredis();
			rconn.hset("player:"+uuid, "settings", json.toString());
		} catch (Exception e){
			throw e;
		} finally {
			if (pst != null)
				pst.close();
			if (conn != null)
				conn.close();
			if (rconn != null)
				rconn.close();
		}
	}
	
	public static void setSetting(Player player, String name, boolean value) throws Exception {
		JSONObject settings = getSettings(player);
		settings.put(name, value);
		setSettings(player, settings);
	}
	
	public static JSONObject getSettings(Player player) throws Exception {
		String uuid = player.getUniqueId().toString();
		Jedis rconn = null;
		try {
			rconn = Main.db.getredis();
			String data = rconn.hget("player:"+uuid, "settings");
			if (data != null) throw new UnsetDataException();
			JSONObject json = new JSONObject(data);
			return json;
		} catch(Exception e) {
			throw e;
		} finally {
			if (rconn != null)
				rconn.close();
		}
	}
	
	public static boolean getSetting(Player player, String name) throws Exception {
		JSONObject settings = getSettings(player);
		
		if (settings.has(name)) return (boolean) settings.get(name);
		return true;
	}
}
