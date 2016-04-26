package fr.tenebrae.MMOCore;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeMessageReceiver implements PluginMessageListener {

	Main plugin;

	Map<String, Integer> ip = new HashMap<String, Integer>();
	Map<String, Integer> playercount = new HashMap<String, Integer>();
	Map<String, String[]> playerlist = new HashMap<String, String[]>();
	String[] serverlist;
	String servername;
	String UUID;
	
	String serverIP_name;
	String serverIP_ip;
	int serverIP_port = 25565;
	
	public BungeeMessageReceiver(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("IP")) {
			ip.clear();
			ip.put(in.readUTF(), in.readInt());
		}
		if (subchannel.equals("PlayerCount")) {
			playercount.clear();
			playercount.put(in.readUTF(), in.readInt());
		}
		if (subchannel.equals("PlayerList")) {
			playerlist.clear();
			playerlist.put(in.readUTF(), in.readUTF().split(", "));
		}
		if (subchannel.equals("GetServers")) {
			serverlist = in.readUTF().split(", ");
		}
		if (subchannel.equals("GetServer")) {
			servername = in.readUTF();
		}
		if (subchannel.equals("UUID")) {
			UUID = in.readUTF();
		}
		if (subchannel.equals("UUIDOther")) {
			UUID = in.readUTF();
		}
		if (subchannel.equals("ServerIP")) {
			serverIP_name = in.readUTF();
			serverIP_ip = in.readUTF();
			serverIP_port = in.readShort();
		}
	}
	
	public void kickPlayer(Player p) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("KickPlayer");
		out.writeUTF(p.getName());
		out.writeUTF("Vous avez été éjecté du réseau pour une raison non précisée.");
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	public void kickPlayer(Player p, String reason) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("KickPlayer");
		out.writeUTF(p.getName());
		out.writeUTF(reason);
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	public void getServerIP(String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ServerIP");
		out.writeUTF(serverName);
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	public void getUUIDOther(Player p) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("UUIDOther");
		out.writeUTF(p.getName());
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	public void getUUID(Player p) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("UUID");

		p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	public void getServerName(Player p) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServer");
	
		p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	public void sendMessage(Player p, String msg) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Message");
		out.writeUTF(p.getName());
		out.writeUTF(msg);
		
		p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	public void getServers() {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServers");
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	public void getPlayerList(String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerList");
		out.writeUTF(serverName);
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	public void getPlayerCount(String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF(serverName);
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	public void getPlayerIP(Player p) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("IP");

		p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	public void connect(Player p, String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(serverName);
			
		p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	public void connectOther(Player p, String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(p.getName());
		out.writeUTF(serverName);
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		
		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}

}