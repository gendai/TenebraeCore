package fr.tenebrae.MMOCore.Utils;

import net.minecraft.server.v1_9_R1.Block;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.PacketPlayOutBlockAction;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BlockAnimationAPI {

	private static boolean openChest(Location loc, Player... players) {
		try {
			BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			Block block = Block.getByName((loc.getBlock().getType() == Material.CHEST ? "chest" : (loc.getBlock().getType() == Material.TRAPPED_CHEST ? "trapped_chest" : "ender_chest")));
			
			PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, block, 1, 1);
			for (Player p : players) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean openChest(Location loc) {
		try {
			BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			Block block = Block.getByName((loc.getBlock().getType() == Material.CHEST ? "chest" : (loc.getBlock().getType() == Material.TRAPPED_CHEST ? "trapped_chest" : "ender_chest")));
			
			PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, block, 1, 1);
			for (Player p : loc.getWorld().getPlayers()) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean closeChest(Location loc, Player... players) {
		try {
			BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			Block block = Block.getByName((loc.getBlock().getType() == Material.CHEST ? "chest" : (loc.getBlock().getType() == Material.TRAPPED_CHEST ? "trapped_chest" : "ender_chest")));
			
			PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, block, 1, 0);
			for (Player p : players) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean closeChest(Location loc) {
		try {
			BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			Block block = Block.getByName((loc.getBlock().getType() == Material.CHEST ? "chest" : (loc.getBlock().getType() == Material.TRAPPED_CHEST ? "trapped_chest" : "ender_chest")));
			
			PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, block, 1, 0);
			for (Player p : loc.getWorld().getPlayers()) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean extendPiston(Location loc, int directionId, Player... players) {
		try {
			BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			Block block = Block.getByName((loc.getBlock().getType() == Material.PISTON_BASE ? "piston" : "sticky_piston"));
			
			PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, block, 0, directionId);
			for (Player p : players) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean extendPiston(Location loc, int directionId) {
		try {
			BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			Block block = Block.getByName((loc.getBlock().getType() == Material.PISTON_BASE ? "piston" : "sticky_piston"));
			
			PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, block, 0, directionId);
			for (Player p : loc.getWorld().getPlayers()) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean retractPiston(Location loc, int directionId, Player... players) {
		try {
			BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			Block block = Block.getByName((loc.getBlock().getType() == Material.PISTON_BASE ? "piston" : "sticky_piston"));
			
			PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, block, 1, directionId);
			for (Player p : players) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean retractPiston(Location loc, int directionId) {
		try {
			BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			Block block = Block.getByName((loc.getBlock().getType() == Material.PISTON_BASE ? "piston" : "sticky_piston"));
			
			PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, block, 1, directionId);
			for (Player p : loc.getWorld().getPlayers()) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean playNote(Location loc, int instrumentId, int notePitch, Player... players) {
		try {
			BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			Block block = Block.getByName("noteblock");
			
			PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, block, instrumentId, notePitch);
			for (Player p : players) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean playNote(Location loc, int instrumentId, int notePitch) {
		try {
			BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			Block block = Block.getByName("noteblock");
			
			PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, block, instrumentId, notePitch);
			for (Player p : loc.getWorld().getPlayers()) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean noteblock(Location loc, NoteInstrument instrument, int notePitch, Player... players) {
		if (loc.getBlock().getType() != Material.NOTE_BLOCK) throw new IllegalArgumentException("Block at given location isn't an instance of a noteblock.");
		return playNote(loc, instrument.id, notePitch, players);
	}
	
	public static boolean noteblock(Location loc, NoteInstrument instrument, int notePitch) {
		if (loc.getBlock().getType() != Material.NOTE_BLOCK) throw new IllegalArgumentException("Block at given location isn't an instance of a noteblock.");
		return playNote(loc, instrument.id, notePitch);
	}
	
	public static boolean piston(Location loc, PistonAnimation anim, PistonDirection direction, Player... players) {
		if (loc.getBlock().getType() != Material.PISTON_BASE && loc.getBlock().getType() != Material.PISTON_STICKY_BASE) throw new IllegalArgumentException("Block at given location isn't an instance of a piston.");
		switch(anim) {
		case EXTEND:
			return extendPiston(loc, direction.id, players);
		case RETRACT:
			return retractPiston(loc, direction.id, players);
		default:
			return false;
		}
	}
	
	public static boolean piston(Location loc, PistonAnimation anim, PistonDirection direction) {
		if (loc.getBlock().getType() != Material.PISTON_BASE && loc.getBlock().getType() != Material.PISTON_STICKY_BASE) throw new IllegalArgumentException("Block at given location isn't an instance of a piston.");
		switch(anim) {
		case EXTEND:
			return extendPiston(loc, direction.id);
		case RETRACT:
			return retractPiston(loc, direction.id);
		default:
			return false;
		}
	}
	
	public static boolean chest(Location loc, ChestAnimation anim, Player... players) {
		if (loc.getBlock().getType() != Material.CHEST && loc.getBlock().getType() != Material.ENDER_CHEST && loc.getBlock().getType() != Material.TRAPPED_CHEST) throw new IllegalArgumentException("Block at given location isn't an instance of a chest.");
		switch(anim) {
		case OPEN:
			return openChest(loc, players);
		case CLOSE:
			return closeChest(loc, players);
		default:
			return false;
		}
	}
	
	public static boolean chest(Location loc, ChestAnimation anim) {
		if (loc.getBlock().getType() != Material.CHEST && loc.getBlock().getType() != Material.ENDER_CHEST && loc.getBlock().getType() != Material.TRAPPED_CHEST) throw new IllegalArgumentException("Block at given location isn't an instance of a chest.");
		switch(anim) {
		case OPEN:
			return openChest(loc);
		case CLOSE:
			return closeChest(loc);
		default:
			return false;
		}
	}
	
	public enum NoteInstrument {
		HARP(0),
		DOUBLE_BASS(1),
		SNARE_DRUM(2),
		CLICK(3),
		BASS_DRUM(4);
		
		int id;
		private NoteInstrument(int id) { this.id = id; }
	}
	
	public enum PistonDirection {
		DOWN(0),
		UP(1),
		SOUTH(2),
		WEST(3),
		NORTH(4),
		EAST(5);
		
		public int id;
		private PistonDirection(int id) { this.id = id; }
	}
	
	public enum PistonAnimation {
		EXTEND,
		RETRACT;
	}
	
	public enum ChestAnimation {
		OPEN,
		CLOSE;
	}
}
