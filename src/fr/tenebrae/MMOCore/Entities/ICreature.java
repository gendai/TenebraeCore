package fr.tenebrae.MMOCore.Entities;

import java.util.Map;

import net.minecraft.server.v1_9_R1.Entity;

import org.bukkit.Location;
import org.bukkit.boss.BossBar;

import fr.tenebrae.MMOCore.Items.Item;

public interface ICreature {

	public void onDeath();
	public boolean isAttackReady();
	public void setTarget(Entity target);
	public void lookAt(Entity target);
	public void lookAt(Location target);
	public void moveTo(Location loc);
	public void moveTo(Location loc, double speed);
	public void moveTo(double x, double y, double z);
	public void moveTo(double x, double y, double z, double speed);
	public Map<Item, Integer> getDrops();
	public void setDrops(Map<Item,Integer> drops);
	public void addDrop(Item drop, int percent);
	public void removeDrop(Item drop);
	public boolean hasDrop(Item item);
	public boolean isMoving();
	public void setup();
	public boolean damage(Entity damager, int amount, double aggroMultiplier);
	public ICreature spawn();
	public BossBar getHealthBar();
	public Location getLocation();
	public org.bukkit.entity.Entity getBEntity();
	public void reset();
	public boolean isResetting();
	public void setResetting(boolean resetting);
	public Location getSpawn();
	public void setPlayMusic(boolean playMusic);
	public boolean doesPlayMusic();
	public void setDarkenSky(boolean darkenSky);
	public boolean doesDarkenSky();
	public void setCreateFog(boolean createFog);
	public boolean doesCreateFog();
	
}