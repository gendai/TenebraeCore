package fr.tenebrae.MMOCore.Entities;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;

import net.minecraft.server.v1_9_R1.Entity;

import org.bukkit.Location;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Mechanics.Character;

public class Utils {

	public static Location getEntityLocation(Entity entity) { return new Location(entity.world.getWorld(), entity.locX, entity.locY, entity.locZ); }
	public static int getRandomDamage(int min, int max) { return new Random().nextInt((int) Math.abs(max-min))+min; }
	public static Character getCharacter(Player p) { return (Main.connectedCharacters.containsKey(p) ? Main.connectedCharacters.get(p) : null); }

	public static Object getPrivateField(String fieldName, Class<?> clazz, Object object) {
		Field field;
		Object o = null;

		try
		{
			field = clazz.getDeclaredField(fieldName);

			field.setAccessible(true);

			o = field.get(object);
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return o;
	}
	
	public static BarStyle getBarStyleByHP(int hp) {
		if (hp < 100) return BarStyle.SOLID;
		if (hp < 6000) return BarStyle.SEGMENTED_6;
		if (hp < 10000) return BarStyle.SEGMENTED_10;
		if (hp < 12000) return BarStyle.SEGMENTED_12;
		if (hp >= 12000) return BarStyle.SEGMENTED_20;
		return BarStyle.SOLID;
	}

	public static Object getKeyFromValue(Map<?, ?> hm, Object value) {
		for (Object o : hm.keySet()) {
			if (hm.get(o).equals(value)) {
				return o;
			}
		}
		return null;
	}
}
