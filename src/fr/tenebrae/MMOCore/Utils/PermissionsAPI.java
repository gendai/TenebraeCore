package fr.tenebrae.MMOCore.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.json.JSONObject;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Exception.UnsetDataException;
import redis.clients.jedis.Jedis;


public class PermissionsAPI {
	
	public JSONObject getRawDataOf(Player player) throws Exception {
		String uuid = player.getUniqueId().toString();
		Jedis rconn = null;
		try {
			rconn = Main.db.getredis();
			String data = rconn.hget("player:"+uuid, "permissions");
			if (data == null) throw new UnsetDataException();
			JSONObject json = new JSONObject(data);
			return json;
		} catch(Exception e) {
			throw e;
		} finally {
			if (rconn != null)
				rconn.close();
		}
	}
	
	public HashMap<Integer,String> getRawData() throws Exception {
		Jedis rconn = null;
		try {
			rconn = Main.db.getredis();
			HashMap<String,String> data = (HashMap<String, String>) rconn.hgetAll("grouplist");
			if (data == null) throw new UnsetDataException();
			HashMap<Integer,String> newdata = new HashMap<Integer,String>();
			Iterator<Entry<String, String>> it = data.entrySet().iterator();
			
			while(it.hasNext()) {
				Entry<String, String> pair = it.next();
				newdata.put(Integer.parseInt(pair.getKey()), pair.getValue());
			}
			return newdata;
		} catch(Exception e) {
			throw e;
		} finally {
			if (rconn != null)
				rconn.close();
		}
	}
	
	public String getPrefix(int gid) throws Exception {
		HashMap<Integer,String> data = getRawData();
		if (data.containsKey(gid))
			return new JSONObject(data.get(gid)).getString("prefix");
		throw new UnsetDataException();
	}
	
	public String getPrefix(String name) throws Exception {
		return getPrefix(getQid(name));
	}
	public int getPermLevel(int gid) throws Exception {
		HashMap<Integer,String> data = getRawData();
		if (data.containsKey(gid))
			return new JSONObject(data.get(gid)).getInt("permlevel");
		throw new UnsetDataException();
	}
	
	public int getPermLevel(String name) throws Exception {
		return getPermLevel(getQid(name));
	}
	
	
	public String getGroupName(int gid) throws Exception {
		HashMap<Integer,String> data = getRawData();
		if (data.containsKey(gid))
			return new JSONObject(data.get(gid)).getString("name");
		throw new UnsetDataException();
	}
	
	public int getGroupOf(Player player) throws Exception {
		return getRawDataOf(player).getInt("gid");
	}
	
	public String getPrefixOf(Player player) throws Exception {
		return getPrefix(getGroupOf(player));
	}
	
	public int getPermLevelOf(Player player) throws Exception {
		return getPermLevel(getGroupOf(player));
	}
	
	public String getGroupNameOf(Player player) throws Exception {
		return getGroupName(getGroupOf(player));
	}
	
	public ArrayList<String> getGroupsName() throws Exception {
		
		ArrayList<String> groups = new ArrayList<String>();
		HashMap<Integer,String> data = getRawData();
		
		Iterator<Entry<Integer, String>> it = data.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<Integer, String> pair = it.next();
			groups.add(new JSONObject(pair.getValue()).getString("name"));
		}
		return groups;
	}
	
	public HashMap<String,Integer> getGroups() throws Exception {
		
		HashMap<String,Integer> groups = new HashMap<String,Integer>();
		HashMap<Integer,String> data = getRawData();
		
		Iterator<Entry<Integer, String>> it = data.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<Integer, String> pair = it.next();
			groups.put(new JSONObject(pair.getValue()).getString("name"), pair.getKey());
		}
		return groups;
	}
	
	public int getQid(String name) throws Exception {
		return getGroups().get(name);
	}
	
	public void setGroupOf(Player player, int gid) throws Exception {
		JSONObject json = getRawDataOf(player);
		json.remove("gid");
		json.put("gid",gid);
		setRawDataOf(player, json);
	}
	
	public void setGroupOf(Player player, String name) throws Exception {
		setGroupOf(player, getQid(name));
	}
	
	public void setPermLevelOf(Player player, int permlevel) throws Exception {
		JSONObject json = getRawDataOf(player);
		json.remove("permlevel");
		json.put("permlevel",permlevel);
		setRawDataOf(player, json);
	}
	
	public void setRawDataOf(Player player, JSONObject json) throws Exception {
		String uuid = player.getUniqueId().toString();
		Connection conn = null;
		PreparedStatement pst = null;
		Jedis rconn = null;
		try {
			conn = Main.db.getmysql();
			pst = conn.prepareStatement("UPDATE permissions, user SET permisisons.permlevel = ?, permisisons.gid = ? WHERE user.uuid=? AND permisisons.uid=user.uid");
			
			pst.setInt(1, json.getInt("permlevel"));
			pst.setInt(2, json.getInt("gid"));
			pst.setString(3, uuid);
			
			pst.executeUpdate();
			
			rconn = Main.db.getredis();
			rconn.hset("player:"+uuid, "permission", json.toString());
			
		} catch (SQLException e) {
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
	
	public boolean isGroup(String name) throws Exception {
		return getGroupsName().contains(name);
	}
	public boolean isGroup(Integer qid) throws Exception {
		return getGroups().containsValue(qid);
	}
	
}