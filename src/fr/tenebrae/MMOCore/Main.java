package fr.tenebrae.MMOCore;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.minecraft.server.v1_9_R1.Packet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.comphenix.packetwrapper.WrapperPlayServerBoss;
import com.comphenix.packetwrapper.WrapperPlayServerBoss.Action;
import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

import fr.tenebrae.MMOCore.Characters.Character;
import fr.tenebrae.MMOCore.Chat.ChatManager;
import fr.tenebrae.MMOCore.Entities.CEntityTypes;
import fr.tenebrae.MMOCore.Items.ItemRegistry;
import fr.tenebrae.MMOCore.Skin.CacheHandler;
import fr.tenebrae.MMOCore.Utils.NamePlatesAPI;
import fr.tenebrae.MMOCore.Utils.TranslatedString;
import fr.tenebrae.TenebraeDB.DbManager;

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

	public static DbManager db;
	public BungeeMessageReceiver bmr;
	public ProtocolManager protocolManager;

	public static Logger log;

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		config = this.getConfig();
		chatConfig = config.getConfigurationSection("general.chat.channel");
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

		Main.db = fr.tenebrae.TenebraeDB.Main.getApi();
		plugin = this;
		log = this.getLogger();
		protocolManager = ProtocolLibrary.getProtocolManager();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Listeners(this), this);

		bmr = new BungeeMessageReceiver(this);
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", bmr);


		NamePlatesAPI.init();
		CEntityTypes.registerEntities();
		ItemRegistry.registerItems();
		initEntitiesTranslator();

		new ChatManager().init(chatConfig);
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					CacheHandler.create();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(this);

		new BukkitRunnable() {
			@Override
			public void run() {
				Main.START_LOCATION = new Location(Bukkit.getWorld("MMOErdrae"), 352.5, 74.25, -1521.5);
			}
		}.runTaskLater(this, 30L);

	}

	@Override
	public void onDisable() {

	}

	public void initEntitiesTranslator() {
		protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_METADATA, PacketType.Play.Server.BOSS) {
			@SuppressWarnings("unchecked")
			@Override
			public void onPacketSending(PacketEvent evt) {
				if (evt.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
					WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(evt.getPacket().deepClone());
					try {
						if (packet.getEntity(evt.getPlayer().getWorld()).getType() != EntityType.ARMOR_STAND) return;
					} catch (NullPointerException npe) { return; }
					List<WrappedWatchableObject> meta = packet.getMetadata();
					if (meta.get(2) == null) return;
					WrappedWatchableObject oname = meta.get(2);
					String serializedName = (String) oname.getValue();
					if (!serializedName.contains("@")) {
						try {
							Integer.valueOf(serializedName);
						} catch (NumberFormatException ee) { return; }
					}
					if (serializedName.contains("@")) {
						String[] splittedName = serializedName.split("@");
						String name = TranslatedString.getString(Integer.valueOf(splittedName[0]), evt.getPlayer());
						int level = Integer.valueOf(splittedName[1]);
						name += " §7[§6Lv. "+(level < 10 ? "0"+level : level)+"§7]";
						oname.setValue(name);
					} else {
						String name = TranslatedString.getString(Integer.valueOf(serializedName), evt.getPlayer());
						oname.setValue(name);
					}
					meta.set(2, oname);
					packet.setMetadata(meta);
					evt.setCancelled(true);
					((CraftPlayer)evt.getPlayer()).getHandle().playerConnection.sendPacket((Packet<?>) packet.getHandle().getHandle());
				} else if (evt.getPacketType() == PacketType.Play.Server.BOSS) {
					WrapperPlayServerBoss packet = new WrapperPlayServerBoss(evt.getPacket().deepClone());
					if (packet.getAction() == Action.ADD || packet.getAction() == Action.UPDATE_NAME) {
						WrappedChatComponent chatComp = packet.getTitle();
						JSONParser parser = new JSONParser();
						JSONObject json = null;
						try {
							json = (JSONObject) parser.parse(chatComp.getJson());
						} catch (ParseException e) {
							e.printStackTrace();
							return;
						}
						
						JSONArray extra = (JSONArray) json.get("extra");
						JSONObject jsonText = (JSONObject) extra.get(0);
						String serializedName = (String) jsonText.get("text");
						
						String translatedName = "";
						if (!serializedName.contains("@")) {
							try {
								Integer.valueOf(serializedName);
							} catch (NumberFormatException ee) { return; }
						}
						if (serializedName.contains("@")) {
							String[] splittedName = serializedName.split("@");
							translatedName = TranslatedString.getString(Integer.valueOf(splittedName[0]), evt.getPlayer());
							int level = Integer.valueOf(splittedName[1]);
							translatedName += " §7[§6Lv. "+(level < 10 ? "0"+level : level)+"§7]";
						} else {
							translatedName = TranslatedString.getString(Integer.valueOf(serializedName), evt.getPlayer());
						}
						jsonText.put("text", translatedName);
						extra.set(0, jsonText);
						json.put("extra", extra);
						chatComp.setJson(json.toJSONString());
						packet.setTitle(chatComp);
						evt.setCancelled(true);
						((CraftPlayer)evt.getPlayer()).getHandle().playerConnection.sendPacket((Packet<?>) packet.getHandle().getHandle());
					}
				}
			}
		});
	}

	public Object getPrivateField(Class<?> clazz, String fieldName) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(null);
		} catch (Exception e) { 
			e.printStackTrace();
			return null;
		}
	}
}
