package fr.tenebrae.MMOCore.Characters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_9_R1.EntityPlayer;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Mechanics.MMOClass;
import fr.tenebrae.MMOCore.Mechanics.Stats;
import fr.tenebrae.MMOCore.Characters.Character;
import fr.tenebrae.PlayerLanguage.LanguageAPI;
import fr.tenebrae.PlayerLanguage.Languages;

public class Character {
	
	private String accountName = "";
	private String characterName = "";
	private Player accountPlayer;
	private int level = 1;
	private int xp = 0;
	private MMOClass mmoClass = null;
	public int hp = 0;
	public int maxHp = 0;
	public Map<Stats,Integer> stats = new HashMap<Stats,Integer>();
	
	public Character(String characterName) {
		
	}

	public Player getAccount() {
		return accountPlayer;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public EntityPlayer getNMSAccount() {
		return ((CraftPlayer)accountPlayer).getHandle();
	}

	public void setAccount(Player account) {
		this.accountPlayer = account;
		this.accountName = accountPlayer.getName();
	}

	public String getCharacterName() {
		return characterName;
	}

	public String getLanguage() {
		return LanguageAPI.getLanguage(accountPlayer).toString().toLowerCase();
	}

	public void setLanguage(Languages language) {
		LanguageAPI.setLanguage(accountPlayer, language);
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getXp() {
		return xp;
	}
	
	public List<Player> getNearbyPlayers(double x, double y, double z) {
		List<Player> returned = new ArrayList<Player>();
		List<Entity> entities = getAccount().getNearbyEntities(x, y, z);
		for (Entity e : entities) {
			if (e instanceof Player) returned.add((Player)e);
		}
		return returned;
	}
	
	public List<Character> getNearbyCharacters(double x, double y, double z) {
		List<Character> returned = new ArrayList<Character>();
		List<Entity> entities = getAccount().getNearbyEntities(x, y, z);
		for (Entity e : entities) {
			if (e instanceof Player) {
				Player p = (Player) e;
				returned.add(Main.connectedCharacters.get(p));
			}
		}
		return returned;
	}
	
	public void damage(net.minecraft.server.v1_9_R1.Entity damager, int amount) {
		
	}
	
	public void damage(Entity damager, int amount) {
		damage(((CraftEntity)damager).getHandle(), amount);
	}

	public MMOClass getMmoClass() {
		return mmoClass;
	}

	public void setMmoClass(MMOClass mmoClass) {
		this.mmoClass = mmoClass;
	}
}
