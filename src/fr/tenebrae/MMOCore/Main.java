package fr.tenebrae.MMOCore;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tenebrae.MMOCore.Utils.NamePlatesAPI;
import fr.tenebrae.MMOCore.Characters.Character;
import fr.tenebrae.MMOCore.Commands.SpawnSpider;
import fr.tenebrae.MMOCore.Entities.CEntityTypes;

public class Main extends JavaPlugin {
	
	public FileConfiguration messages = null;
	private File messagesFile = null;
	
	public FileConfiguration characters = null;
	private File charactersFile = null;
	
	public FileConfiguration npcs = null;
	private File npcsFile = null;
	
	public FileConfiguration items = null;
	private File itemsFile = null;
	
	public FileConfiguration quests = null;
	private File questsFile = null;
	
	public static Main plugin;
	public FileConfiguration config;
	
	public static Map<Player, Character> connectedCharacters = new HashMap<Player, Character>();
	
	public double emoterange;
	public double sayrange;
	public double yellrange;

	public static String DB_HOST;
	public static int DB_PORT;
	public static String DB_DATABASE;
	public static String DB_USER;
	public static String DB_PASSWORD;
	public static String DB_PLAYERS_TABLE;
	public static String DB_QUEST_TEMPLATE;
	public static String DB_CREATURE_TEMPLATE;
	public static String DB_ITEM_TEMPLATE;
	public static String DB_LOOT_TEMPLATE;
	public static String DB_STRING_TEMPLATE;
	public static String DB_XP_TEMPLATE;

	public MySQL database;
	
	public static Logger log; //= plugin.getLogger();
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		//this.saveDefaultCharactersFile();
		//this.saveDefaultMessagesFile();
		config = this.getConfig();
		emoterange = config.getDouble("general.chat.emoterange");
		sayrange = config.getDouble("general.chat.sayrange");
		yellrange = config.getDouble("general.chat.yellrange");

		DB_HOST = config.getString("sql.host");
		DB_PORT = config.getInt("sql.port");
		DB_DATABASE = config.getString("sql.database");
		DB_USER = config.getString("sql.user");
		DB_PASSWORD = config.getString("sql.password");
		DB_PLAYERS_TABLE = config.getString("sql.players_table");
		DB_QUEST_TEMPLATE = config.getString("sql.quest_template");
		DB_ITEM_TEMPLATE = config.getString("sql.item_template");
		DB_CREATURE_TEMPLATE = config.getString("sql.creature_template");
		DB_LOOT_TEMPLATE = config.getString("sql.loot_template");
		DB_STRING_TEMPLATE = config.getString("sql.string_template");
		DB_XP_TEMPLATE = config.getString("sql.xp_template");
		
		database = new MySQL(this,
				DB_HOST,
				DB_PORT,
				DB_DATABASE,
				DB_USER,
				DB_PASSWORD);
		plugin = this;
		NamePlatesAPI.init();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Listeners(this), this);
		log = plugin.getLogger();
		this.getCommand("spw").setExecutor(new SpawnSpider());
		CEntityTypes.registerEntities();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public void registerItems() {
	}
	
	//---------------------- CHARACTERS.YML PART ----------------------\\
	
	public void reloadCharactersFile() {
	    if (charactersFile == null) {
	    	charactersFile = new File(getDataFolder(), "characters.yml");
	    }
	    characters = YamlConfiguration.loadConfiguration(charactersFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream = null;
		try {
			defConfigStream = new InputStreamReader(this.getResource("characters.yml"), "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        characters.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getCharactersFile() {
	    if (characters == null) {
	    	reloadCharactersFile();
	    }
	    return characters;
	}
	
	public void saveCharactersFile() {
	    if (characters == null || charactersFile == null) {
	        return;
	    }
	    try {
	    	getCharactersFile().save(charactersFile);
	    } catch (IOException ex) {
	        getLogger().log(Level.SEVERE, "Could not save config to " + charactersFile, ex);
	        getLogger().log(Level.SEVERE, "Maybe there's not enough space?", ex);
	    }
	}
	
	public void saveDefaultCharactersFile() {
	    if (charactersFile == null) {
	    	charactersFile = new File(getDataFolder(), "characters.yml");
	    }
	    if (!charactersFile.exists()) {            
	         plugin.saveResource("characters.yml", false);
	     }
	}
	
	//---------------------- CHARACTERS.YML PART ----------------------\\
	
	//------------------------- NPCS.YML PART -------------------------\\
	
	public void reloadNpcsFile() {
	    if (npcsFile == null) {
	    	npcsFile = new File(getDataFolder(), "npcs.yml");
	    }
	    npcs = YamlConfiguration.loadConfiguration(npcsFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream = null;
		try {
			defConfigStream = new InputStreamReader(this.getResource("npcs.yml"), "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        npcs.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getNpcsFile() {
	    if (npcs == null) {
	    	reloadCharactersFile();
	    }
	    return npcs;
	}
	
	public void saveNpcsFile() {
	    if (npcs == null || npcsFile == null) {
	        return;
	    }
	    try {
	    	getCharactersFile().save(npcsFile);
	    } catch (IOException ex) {
	        getLogger().log(Level.SEVERE, "Could not save config to " + npcsFile, ex);
	        getLogger().log(Level.SEVERE, "Maybe there's not enough space?", ex);
	    }
	}
	
	public void saveDefaultNpcsFile() {
	    if (npcsFile == null) {
	    	npcsFile = new File(getDataFolder(), "npcs.yml");
	    }
	    if (!npcsFile.exists()) {            
	         plugin.saveResource("npcs.yml", false);
	     }
	}	
	
	//------------------------- NPCS.YML PART -------------------------\\
	
	//----------------------- ITEMLIST.YML PART -----------------------\\
	
	public void reloadItemsFile() {
	    if (itemsFile == null) {
	    	itemsFile = new File(getDataFolder(), "items.yml");
	    }
	    items = YamlConfiguration.loadConfiguration(itemsFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream = null;
		try {
			defConfigStream = new InputStreamReader(this.getResource("items.yml"), "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        items.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getItemsFile() {
	    if (items == null) {
	    	reloadCharactersFile();
	    }
	    return items;
	}
	
	public void saveItemsFile() {
	    if (items == null || itemsFile == null) {
	        return;
	    }
	    try {
	    	getCharactersFile().save(itemsFile);
	    } catch (IOException ex) {
	        getLogger().log(Level.SEVERE, "Could not save config to " + itemsFile, ex);
	        getLogger().log(Level.SEVERE, "Maybe there's not enough space?", ex);
	    }
	}
	
	public void saveDefaultItemsFile() {
	    if (itemsFile == null) {
	    	itemsFile = new File(getDataFolder(), "items.yml");
	    }
	    if (!itemsFile.exists()) {            
	         plugin.saveResource("items.yml", false);
	     }
	}
	
	//----------------------- ITEMLIST.YML PART -----------------------\\
	
	//------------------------ QUESTS.YML PART ------------------------\\
	
	public void reloadQuestsFile() {
	    if (questsFile == null) {
	    	questsFile = new File(getDataFolder(), "quests.yml");
	    }
	    quests = YamlConfiguration.loadConfiguration(questsFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream = null;
		try {
			defConfigStream = new InputStreamReader(this.getResource("quests.yml"), "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        quests.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getQuestsFile() {
	    if (quests == null) {
	    	reloadCharactersFile();
	    }
	    return quests;
	}
	
	public void saveQuestsFile() {
	    if (quests == null || questsFile == null) {
	        return;
	    }
	    try {
	    	getCharactersFile().save(questsFile);
	    } catch (IOException ex) {
	        getLogger().log(Level.SEVERE, "Could not save config to " + questsFile, ex);
	        getLogger().log(Level.SEVERE, "Maybe there's not enough space?", ex);
	    }
	}
	
	public void saveDefaultQuestsFile() {
	    if (questsFile == null) {
	    	questsFile = new File(getDataFolder(), "quests.yml");
	    }
	    if (!questsFile.exists()) {            
	         plugin.saveResource("quests.yml", false);
	     }
	}
	
	//------------------------ QUESTS.YML PART ------------------------\\
	
	
	public void reloadMessagesFile() {
		if (messagesFile == null) {
			messagesFile = new File(getDataFolder(), "messages.yml");
		}
		messages = YamlConfiguration.loadConfiguration(messagesFile);
		    
		Reader defConfigStream = null;
		try {
			defConfigStream = new InputStreamReader(this.getResource("messages.yml"), "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			messages.setDefaults(defConfig);
		}
	}
		
	public FileConfiguration getMessagesFile() {
		if (messages == null) {
			reloadMessagesFile();
		}
		return messages;
	}
		
	public void saveMessagesFile() {
		if (messages == null || messagesFile == null) {
			return;
		}
		try {
			getMessagesFile().save(messagesFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + messagesFile, ex);
			getLogger().log(Level.SEVERE, "Maybe there's not enough space?", ex);
		}
	}
		
	public void saveDefaultMessagesFile() {
		if (messagesFile == null) {
			messagesFile = new File(getDataFolder(), "messages.yml");
		}
		if (!messagesFile.exists()) {            
			saveResource("messages.yml", false);
		}
	}
}
