package fr.tenebrae.MMOCore.Utils;

import net.minecraft.server.v1_9_R1.DataWatcher;
import net.minecraft.server.v1_9_R1.DataWatcherObject;
import net.minecraft.server.v1_9_R1.DataWatcherRegistry;
import net.minecraft.server.v1_9_R1.EntityEnderman;
import net.minecraft.server.v1_9_R1.EntityGhast;
import net.minecraft.server.v1_9_R1.EntityGuardian;
import net.minecraft.server.v1_9_R1.EntityHorse;
import net.minecraft.server.v1_9_R1.EntityZombie;
import net.minecraft.server.v1_9_R1.PacketPlayOutAnimation;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEnderman;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftGhast;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftGuardian;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftHorse;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftWolf;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftZombie;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tenebrae.MMOCore.Main;

public class AnimationAPI {

	public static boolean sendAnimation(net.minecraft.server.v1_9_R1.Entity target, final Animation anim, Player... players) {
		return sendAnimation((Entity)((CraftEntity.getEntity(target.getWorld().getServer(), target))), anim, (players == null ? null : players));
	}
	
	public static boolean sendAnimation(Entity target, final Animation anim, Player... players) {
		if (anim.type == 0) {
			try {
				PacketPlayOutAnimation packet = new PacketPlayOutAnimation(((CraftEntity)target).getHandle(), anim.id);
				if (players != null && players.length > 0) for (Player p : players) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
				else for (Player p : target.getWorld().getPlayers()) ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
				
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else if (anim.type == 1) {
			try {
				if (target instanceof Horse) {
					final DataWatcher datawatcher = (((CraftHorse)target).getHandle()).getDataWatcher();
					final DataWatcherObject<Byte> bE = DataWatcher.a(EntityHorse.class, DataWatcherRegistry.a);
					byte b0 = ((Byte)datawatcher.get(bE)).byteValue();
					datawatcher.set(bE, Byte.valueOf((byte) (b0 | anim.dataWatcherValue)));
					new BukkitRunnable() {
						@Override
						public void run() {
							byte b0 = ((Byte)datawatcher.get(bE)).byteValue();
							datawatcher.set(bE, Byte.valueOf((byte) (b0 & (anim.dataWatcherValue ^ 0xFFFFFFFF))));
						}
					}.runTaskLater(Main.plugin, (anim.dataWatcherValue == 64 || anim.dataWatcherValue == 128 ? 20 : 30));
					return true;
				} else throw new IllegalArgumentException("The target entity cannot receive this animation");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else if (anim.type == 2) {
			try {
				if (target instanceof Guardian) {
					final DataWatcher datawatcher = (((CraftGuardian)target).getHandle()).getDataWatcher();
					DataWatcherObject<Byte> a = DataWatcher.a(EntityGuardian.class, DataWatcherRegistry.a);
					int i = ((Byte)datawatcher.get(a).byteValue());
					if (anim == Animation.GUARDIAN_RETRACT_SPIKES) datawatcher.set(a, Byte.valueOf((byte) (i | anim.dataWatcherValue)));
					if (anim == Animation.GUARDIAN_EXTEND_SPIKES) datawatcher.set(a, Byte.valueOf((byte) (i & (anim.dataWatcherValue ^ 0xFFFFFFFF))));//~anim.dataWatcherValue
					return true;
				} else throw new IllegalArgumentException("The target entity cannot receive this animation");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else if (anim.type == 3) {
			try {
				if (target instanceof Wolf) {
					(((CraftWolf)target).getHandle()).s(anim.boolValue);
					return true;
				} else throw new IllegalArgumentException("The target entity cannot receive this animation");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else if (anim.type == 4) {
			try {
				if (target instanceof Enderman) {
					final DataWatcher datawatcher = (((CraftEnderman)target).getHandle()).getDataWatcher();
					datawatcher.set(DataWatcher.a(EntityEnderman.class, DataWatcherRegistry.h), anim.boolValue);
					return true;
				} else throw new IllegalArgumentException("The target entity cannot receive this animation");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else if (anim.type == 5) {
			try {
				if (target instanceof Zombie) {
					final DataWatcher datawatcher = (((CraftZombie)target).getHandle()).getDataWatcher();
					datawatcher.set(DataWatcher.a(EntityZombie.class, DataWatcherRegistry.h), anim.boolValue);
					return true;
				} else throw new IllegalArgumentException("The target entity cannot receive this animation");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else if (anim.type == 7) {
			try {
				if (target instanceof Ghast) {
					final DataWatcher datawatcher = (((CraftGhast)target).getHandle()).getDataWatcher();
					datawatcher.set(DataWatcher.a(EntityGhast.class, DataWatcherRegistry.h), anim.boolValue);
					return true;
				} else throw new IllegalArgumentException("The target entity cannot receive this animation");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public enum Animation {
		SWING_ARM(0, 0),
		TAKE_DAMAGE(1, 0),
		LEAVE_BED(2, 0),
		EAT_FOOD(3, 0),
		CRITICAL_EFFECT(4, 0),
		MAGICAL_CRITICAL_EFFECT(5, 0),
		
		HORSE_REARING(16, 1, 64),
		HORSE_EATING(16, 1, 32),
		HORSE_MOUSE_OPEN(16, 1, 128),

		GUARDIAN_RETRACT_SPIKES(16, 2, 4),
		GUARDIAN_EXTEND_SPIKES(16, 2, 4),

		WOLF_BEGGING_START(19, 3, true),
		WOLF_BEGGING_END(19, 3, false),

		ENDERMAN_SCREAM_START(18, 4, true),
		ENDERMAN_SCREAM_END(18, 4, false),

		ZOMBIE_SHAKE_START(14, 5, true),
		ZOMBIE_SHAKE_END(14, 5, false),
		
		GHAST_READY_START(16, 7, true),
		GHAST_READY_END(16, 7, false);
		
		public int id;
		public int type;
		public int dataWatcherValue;
		public boolean boolValue;
		private Animation(int id, int type) { this.id = id; this.type = type; }
		private Animation(int id, int type, int dataWatcherValue) { this.id = id; this.type = type; this.dataWatcherValue = dataWatcherValue; }
		private Animation(int id, int type, boolean boolValue) { this.id = id; this.type = type; this.boolValue = boolValue; }
	}
}
