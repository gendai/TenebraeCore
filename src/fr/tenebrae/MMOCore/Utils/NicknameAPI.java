package fr.tenebrae.MMOCore.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.server.v1_9_R1.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NicknameAPI {
	public static void applyNick(Player player, String nick) throws Exception {
		player.setPlayerListName(nick);
		EntityPlayer entity = ((CraftPlayer) player).getHandle();
		Field name = entity.getProfile().getClass().getDeclaredField("name");
		name.setAccessible(true);
		name.set(entity.getProfile(), nick);
		
		ArrayList<Player> ignored = new ArrayList<Player>();
		ignored.add(player);
		
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (!online.canSee(player)) ignored.add(online);
			if (ignored.contains(online)) continue;
			online.hidePlayer(player);
		}
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (ignored.contains(online)) continue;
			online.showPlayer(player);
		}
	}
}
