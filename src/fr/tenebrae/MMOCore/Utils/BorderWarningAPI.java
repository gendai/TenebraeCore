package fr.tenebrae.MMOCore.Utils;

import net.minecraft.server.v1_9_R1.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_9_R1.WorldBorder;

import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BorderWarningAPI {

	public static void send(Player p, int percentage) {
		if (percentage > 100) percentage = 100;
		percentage = 100 - percentage;
		percentage = Math.round((float)(percentage/1));
		int dist = -146 * percentage + 20000;
		
		//WorldBorder border = (((CraftWorld)p.getWorld()).getHandle()).getWorldBorder();
		WorldBorder fake = new WorldBorder();
		fake.setCenter(p.getLocation().getX(), p.getLocation().getZ());
		//fake.setSize(border.getSize());
		fake.setWarningDistance(dist);
		fake.setWarningTime(15);
		fake.transitionSizeBetween(10000, 10000, 0L);
		
		PacketPlayOutWorldBorder packet = new PacketPlayOutWorldBorder(fake, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
	}

	public static void send(Player p) { send(p, 100); }
	
	public static void remove(Player p) {
		WorldBorder border = (((CraftWorld)p.getWorld()).getHandle()).getWorldBorder();
		
		PacketPlayOutWorldBorder packet = new PacketPlayOutWorldBorder(border, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
	}
}
