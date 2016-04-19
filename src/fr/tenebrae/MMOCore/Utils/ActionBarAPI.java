package fr.tenebrae.MMOCore.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Characters.Character;

/**
 * <b>ActionBar API</b>
 * <p>
 * This API was created by @ConnorLinfoot and allows you to use simple methods to send messages just above the action bar.
 * While the original just allow non-timed messages (so ~40 ticks), I decided to improve this, and add a function to choose how long do you want it.
 * <p>
 * Original version can be found here: https://www.spigotmc.org/resources/actionbarapi.1315/
 * 
 * @author Arektor
 * @version 1.0
 */

public class ActionBarAPI {

	public static boolean works = true;
	public static String nmsver;

	/**
	 * It send to @player the message @msg just above his action bar, for at least 40 ticks, else for @ticks.
	 * 
	 * @param p The targeted player, the one who shall receive the message.
	 * @param msg The desired message. And, yes, it support colors.
	 * @param ticks It is how much time - in ticks - shall the message appear. If less than 40, it's adjusted to 40.
	 */
	public static void sendTimedMsg(Player p, String msg, int ticks) {
		ActionBarScheduler abs = new ActionBarScheduler(p, msg, ticks);
		abs.runTaskTimer(Main.plugin, 0, 1);
	}
	
	/**
	 * It send to @player the message @msg just above his action bar, for at least 40 ticks, else for @ticks.
	 * 
	 * @param players The targeted players, those who shall receive the message.
	 * @param msg The desired message. And, yes, it support colors.
	 * @param ticks It is how much time - in ticks - shall the message appear. If less than 40, it's adjusted to 40.
	 */
	public static void sendTimedMsg(List<Player> players, String msg, int ticks) {
		ActionBarScheduler abs = new ActionBarScheduler(players, msg, ticks);
		abs.runTaskTimer(Main.plugin, 0, 1);
	}
	
	/**
	 * It send to @player the message @msg just above his action bar, for at least 40 ticks, else for @ticks.
	 * 
	 * @param p The targeted player, the one who shall receive the message.
	 * @param msg The desired message. And, yes, it support colors.
	 * @param ticks It is how much time - in ticks - shall the message appear. If less than 40, it's adjusted to 40.
	 */
	public static void sendTimedMsg(Character c, String msg, int ticks) {
		ActionBarScheduler abs = new ActionBarScheduler(c.getAccount(), msg, ticks);
		abs.runTaskTimer(Main.plugin, 0, 1);
	}
	
	/**
	 * It send to @player the message @msg just above his action bar, for at least 40 ticks, else for @ticks.
	 * 
	 * @param players The targeted players, those who shall receive the message.
	 * @param msg The desired message. And, yes, it support colors.
	 * @param ticks It is how much time - in ticks - shall the message appear. If less than 40, it's adjusted to 40.
	 */
	public static void sendTimedMsg(ArrayList<Character> characters, String msg, int ticks) {
		ActionBarScheduler abs = new ActionBarScheduler(characters, msg, ticks);
		abs.runTaskTimer(Main.plugin, 0, 1);
	}
	
	/**
	 * <b>sendActionBar(Player, String)</b>
	 * <p>
	 * This is the original method of ActionBarAPI, and the only one.
	 * Original version can be found here: https://www.spigotmc.org/resources/actionbarapi.1315/
	 * 
	 * @param player The targeted player, the one who shall receive the message.
	 * @param message The desired message. And, yes, it support colors.
	 */
    public static void sendActionBar(Player player, String message){
    	try {
    		Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
    		Object p = c1.cast(player);
    		Object ppoc = null;
    		Class<?> c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
    		Class<?> c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
    		if (nmsver.equalsIgnoreCase("v1_8_R1") || !nmsver.startsWith("v1_8_")) {
        		Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
        		Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
        		Method m3 = c2.getDeclaredMethod("a", new Class<?>[] {String.class});
        		Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
        		ppoc = c4.getConstructor(new Class<?>[] {c3, byte.class}).newInstance(new Object[] {cbc, (byte) 2});
    		} else {
    			Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
        		Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
    			Object o = c2.getConstructor(new Class<?>[] {String.class}).newInstance(new Object[] {message});
        		ppoc = c4.getConstructor(new Class<?>[] {c3, byte.class}).newInstance(new Object[] {o, (byte) 2});
    		}
    		Method m1 = c1.getDeclaredMethod("getHandle", new Class<?>[] {});
    		Object h = m1.invoke(p);
    		Field f1 = h.getClass().getDeclaredField("playerConnection");
    		Object pc = f1.get(h);
    		Method m5 = pc.getClass().getDeclaredMethod("sendPacket",new Class<?>[] {c5});
    		m5.invoke(pc, ppoc);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		works = false;
    	}
    }
	
	/**
	 * <b>sendActionBar(Player, String)</b>
	 * <p>
	 * This is the original method of ActionBarAPI, and the only one.
	 * Original version can be found here: https://www.spigotmc.org/resources/actionbarapi.1315/
	 * 
	 * @param player The targeted player, the one who shall receive the message.
	 * @param message The desired message. And, yes, it support colors.
	 */
    public static void sendActionBar(Character c, String message){
    	Player player = c.getAccount();
    	try {
    		Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
    		Object p = c1.cast(player);
    		Object ppoc = null;
    		Class<?> c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
    		Class<?> c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
    		if (nmsver.equalsIgnoreCase("v1_8_R1") || !nmsver.startsWith("v1_8_")) {
        		Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
        		Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
        		Method m3 = c2.getDeclaredMethod("a", new Class<?>[] {String.class});
        		Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
        		ppoc = c4.getConstructor(new Class<?>[] {c3, byte.class}).newInstance(new Object[] {cbc, (byte) 2});
    		} else {
    			Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
        		Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
    			Object o = c2.getConstructor(new Class<?>[] {String.class}).newInstance(new Object[] {message});
        		ppoc = c4.getConstructor(new Class<?>[] {c3, byte.class}).newInstance(new Object[] {o, (byte) 2});
    		}
    		Method m1 = c1.getDeclaredMethod("getHandle", new Class<?>[] {});
    		Object h = m1.invoke(p);
    		Field f1 = h.getClass().getDeclaredField("playerConnection");
    		Object pc = f1.get(h);
    		Method m5 = pc.getClass().getDeclaredMethod("sendPacket",new Class<?>[] {c5});
    		m5.invoke(pc, ppoc);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		works = false;
    	}
    }
}
