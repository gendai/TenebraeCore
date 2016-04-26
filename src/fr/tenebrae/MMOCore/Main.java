package fr.tenebrae.MMOCore;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tenebrae.MMOCore.Characters.Character;
import fr.tenebrae.MMOCore.Chat.ChatManager;
import fr.tenebrae.MMOCore.Commands.SpawnSpider;
import fr.tenebrae.MMOCore.Entities.CEntityTypes;
import fr.tenebrae.MMOCore.Items.ItemRegistry;
import fr.tenebrae.MMOCore.Utils.NamePlatesAPI;

public class Main extends JavaPlugin {

	public static Main plugin;
	public FileConfiguration config;

	public static Map<Player, Character> connectedCharacters = new HashMap<Player, Character>();

	public ConfigurationSection chatConfig;

	public static Location START_LOCATION;

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

	public DataSource ds;
	public BungeeMessageReceiver bmr;

	public static Logger log;

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		config = this.getConfig();

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

		try {
			ds = DataSource.getInstance();
		} catch (IOException | SQLException | PropertyVetoException e) {
			e.printStackTrace();
		}
		plugin = this;
		log = this.getLogger();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Listeners(this), this);

		bmr = new BungeeMessageReceiver(this);
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", bmr);


		NamePlatesAPI.init();
		CEntityTypes.registerEntities();
		ItemRegistry.registerItems();

		new ChatManager().init(chatConfig);
		this.getCommand("spw").setExecutor(new SpawnSpider());

		Main.START_LOCATION = new Location(Bukkit.getWorld("MMOErdrae"), 352.5, 74.25, -1521.5);

	}

	@Override
	public void onDisable() {

	}
}
