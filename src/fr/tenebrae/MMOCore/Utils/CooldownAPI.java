package fr.tenebrae.MMOCore.Utils;

import net.minecraft.server.v1_9_R1.Item;
import net.minecraft.server.v1_9_R1.PacketPlayOutSetCooldown;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CooldownAPI {

	public static void setCooldown(Player p, ItemStack item, int cd) {
		setCooldown(p, CraftItemStack.asNMSCopy(item).getItem(), cd);
	}
	
	public static void setCooldown(Player p, Item item, int cd) {
		PacketPlayOutSetCooldown packet = new PacketPlayOutSetCooldown(item, cd);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
	}
}
