package fr.tenebrae.MMOCore.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.PacketPlayOutBlockBreakAnimation;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BlockBreakAPI {
	
	public static List<Integer> usedIds = new ArrayList<Integer>();
	
	public static BlockBreakAnimation createBreakAnimation(Location loc, int stage) {
		int id = -1;
		while (id == -1) {
			int rnd = new Random().nextInt(Integer.MAX_VALUE);
			if (!usedIds.contains(rnd)) {
				id = rnd;
				break;
			}
		}
		
		return new BlockBreakAnimation(id, loc, stage);
	}
	
	public static BlockBreakAnimation createBreakAnimation(Location loc) { 
		return createBreakAnimation(loc, 0);
	}
	
	
}


class BlockBreakAnimation {
	
	private int id;
	private Location loc;
	private BlockPosition pos;
	private int stage;
	private List<Player> players = new ArrayList<Player>();
	
	public BlockBreakAnimation(int id, Location loc, int stage) {
		this.id = id;
		this.loc = loc;
		this.stage = stage;
		this.pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	
	public BlockBreakAnimation(int id, Location loc, int stage, Player... players) {
		this.id = id;
		this.loc = loc;
		this.stage = stage;
		this.players = Arrays.asList(players);
		this.pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	
	public int getId() { return id; }
	public Location getLocation() { return loc; }
	public int getStage() { return stage; }
	
	public BlockBreakAnimation changeStage(int newStage) {
		this.stage = newStage;
		this.display();
		return this;
	}
	
	public BlockBreakAnimation display() {
		PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(id, pos, stage);
		if (players.isEmpty()) for (Player p : loc.getWorld().getPlayers()) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
		else for (Player p : players) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
		return this;
	}
	
	public boolean remove() {
		try {
			PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(id, pos, -1);
			if (players.isEmpty()) for (Player p : loc.getWorld().getPlayers()) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			else for (Player p : players) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}