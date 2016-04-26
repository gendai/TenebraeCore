package fr.tenebrae.MMOCore.LoginScreen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import net.minecraft.server.v1_9_R1.CancelledPacketHandleException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.SQLResultSet;
import fr.tenebrae.MMOCore.Bags.Bag;
import fr.tenebrae.MMOCore.Characters.Character;
import fr.tenebrae.MMOCore.Mechanics.MMOClass;
import fr.tenebrae.MMOCore.Mechanics.Stats;
import fr.tenebrae.MMOCore.Utils.ItemStackBuilder;
import fr.tenebrae.MMOCore.Utils.SQLHelper;
import fr.tenebrae.MMOCore.Utils.TitleAPI;
import fr.tenebrae.MMOCore.Utils.TranslatedString;
import fr.tenebrae.MMOCore.Utils.Serializers.InventorySerializer;
import fr.tenebrae.MMOCore.Utils.Serializers.ItemStackSerializer;
import fr.tenebrae.MMOCore.Utils.Serializers.LocationSerializer;
import fr.tenebrae.PlayerLanguage.LanguageAPI;

public class CharacterCreator implements Listener {

	public static Map<Player,CharacterCreator> creatingPlayers = new HashMap<Player,CharacterCreator>();

	private Player p;
	private Inventory mainInv;
	private Inventory selectClass;
	private Inventory selectGender;
	private String language = "english";

	private Gender gender = Gender.MALE;
	private MMOClass mmoClass = MMOClass.WARRIOR;
	private String charName = "Unnamed";

	private boolean isChoosingName = false;

	public CharacterCreator(Player p) {
		this.p = p;
		this.language = LanguageAPI.getStringLanguage(p);
		creatingPlayers.put(p, this);

		this.mainInv = Bukkit.createInventory(p, InventoryType.HOPPER, TranslatedString.getString(20000, this.language));
		this.mainInv.setItem(0, new ItemStackBuilder()
		.withMaterial(Material.SKULL_ITEM)
		.withDurability(3)
		.withOwner(p.getName())
		.withAmount(1)
		.withDisplayName("§r")
		.withLore(Arrays.asList(TranslatedString.getString(20001, this.language).replace("%gender%", "/").replace("%class%", "/").split(" %n% ")))
		.build());
		this.mainInv.setItem(2, new ItemStackBuilder()
		.withMaterial(Material.GOLD_SWORD)
		.withDisplayName("§6"+TranslatedString.getString(20002, this.language))
		.withAmount(1)
		.build());
		this.mainInv.setItem(3, new ItemStackBuilder()
		.withMaterial(Material.SKULL_ITEM)
		.withDurability(3)
		.withDisplayName("§6"+TranslatedString.getString(20003, this.language))
		.withAmount(1)
		.build());
		this.mainInv.setItem(4, new ItemStackBuilder()
		.withMaterial(Material.SEEDS)
		.withDisplayName("§a"+TranslatedString.getString(20005, this.language))
		.withAmount(1)
		.build());

		this.selectClass = Bukkit.createInventory(p, InventoryType.HOPPER, TranslatedString.getString(20002, this.language));
		this.selectClass.setItem(0, new ItemStackBuilder()
		.withMaterial(Material.IRON_SWORD)
		.withDisplayName("§r"+MMOClass.WARRIOR.getString(this.language))
		.withLore(Arrays.asList(TranslatedString.getString(21000, this.language).split(" %n% ")))
		.withAmount(1)
		.build());
		this.selectClass.setItem(1, new ItemStackBuilder()
		.withMaterial(Material.IRON_SPADE)
		.withDisplayName("§r"+MMOClass.ARCHER.getString(this.language))
		.withLore(Arrays.asList(TranslatedString.getString(21001, this.language).split(" %n% ")))
		.withAmount(1)
		.build());
		this.selectClass.setItem(2, new ItemStackBuilder()
		.withMaterial(Material.IRON_PICKAXE)
		.withDisplayName("§r"+MMOClass.ASSASSIN.getString(this.language))
		.withLore(Arrays.asList(TranslatedString.getString(21002, this.language).split(" %n% ")))
		.withAmount(1)
		.build());
		this.selectClass.setItem(3, new ItemStackBuilder()
		.withMaterial(Material.IRON_HOE)
		.withDisplayName("§r"+MMOClass.MAGE.getString(this.language))
		.withLore(Arrays.asList(TranslatedString.getString(21003, this.language).split(" %n% ")))
		.withAmount(1)
		.build());

		this.selectGender = Bukkit.createInventory(p, InventoryType.HOPPER, TranslatedString.getString(20003, this.language));
		this.selectGender.setItem(1, new ItemStackBuilder()
		.withMaterial(Material.SKULL_ITEM)
		.withDurability(3)
		.withDisplayName("§r"+Gender.MALE.getString(this.language))
		.withOwner("MHF_Steve")
		.withAmount(1)
		.build());
		this.selectGender.setItem(3, new ItemStackBuilder()
		.withMaterial(Material.SKULL_ITEM)
		.withDurability(3)
		.withDisplayName("§r"+Gender.FEMALE.getString(this.language))
		.withOwner("MHF_Alex")
		.withAmount(1)
		.build());

		Main.plugin.getServer().getPluginManager().registerEvents(this, Main.plugin);
		chooseName();

	}

	public void chooseName() {
		TitleAPI.sendTitle(p, 5, 32767, 5, " ", TranslatedString.getString(20004, p));
		this.isChoosingName = true;
		p.closeInventory();
	}
	
	public void chooseClass() {
		p.closeInventory();
		p.openInventory(this.selectClass);
	}
	
	public void chooseGender() {
		p.closeInventory();
		p.openInventory(this.selectGender);
	}
	
	public void done() {
		if (charName.equals("Unnamed")) {
			p.sendMessage("§cUnnamed character.");
			return;
		}
		
		String serializedQuests = "none";
		String serializedStats = "";
		String serializedInventory = InventorySerializer.toBase64(Character.setupInventory(this.mmoClass.getStartWeapon(), this.language));
		String serializedEquipment = "null#@#null#@#"+ItemStackSerializer.toBase64(this.mmoClass.getStartArmor().getItemStack())+"#@#null#@#null#@#null#@#null#@#null#@#"+ItemStackSerializer.toBase64(this.mmoClass.getStartWeapon().getItemStack())+"#@#null";
		String serializedBags = new Bag(27).toString();
		String location = LocationSerializer.locationToString(Main.START_LOCATION);
		
		for (Entry<Stats,Double> entry : this.mmoClass.getBaseStats().entrySet()) {
			serializedStats += entry.getKey().toString()+":"+entry.getValue()+"@";
		}
		serializedStats = serializedStats.substring(0, serializedStats.length()-1);
		
		String[] columns = {"name","owner_uuid","level","xp","class","hp","activeQuests","stats","inventory","equipment","location","bags"};
		Object[] values = {this.charName, this.p.getUniqueId().toString(), 1, 0, this.mmoClass, this.mmoClass.getBaseStats().get(Stats.HEALTH), serializedQuests, serializedStats, serializedInventory, serializedEquipment, location, serializedBags};
		
		try {
			SQLHelper.insertEntry(Main.DB_DATABASE, Main.DB_PLAYERS_TABLE, columns, values);
		} catch (ClassNotFoundException | SQLException e) {
			this.p.sendMessage("§cAn error occured while creating the character. Aborting.");
			e.printStackTrace();
		}

		HandlerList.unregisterAll(this);
		CharacterCreator.creatingPlayers.remove(p);
		new LoginScreen(p);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent evt) {
		if (evt.getPlayer() != this.p) return;
		evt.setCancelled(true);
		if (!this.isChoosingName) return;
		String name = evt.getMessage().split(" ")[0];
		if (name.length() > 16) {
			p.sendMessage(TranslatedString.getString(10097, p));
			return;
		}
		if (Pattern.compile("([0-9])").matcher(name).find()) {
			p.sendMessage(TranslatedString.getString(10098, p));
			return;
		}
		try {
			SQLResultSet sqlRS = SQLHelper.getSortedEntrys(Main.DB_DATABASE, Main.DB_PLAYERS_TABLE, "name", name);
			ResultSet set = sqlRS.getResultSet();
			if (!set.next()) {
				this.charName = name;
				this.mainInv.setItem(0, new ItemStackBuilder()
				.withMaterial(Material.SKULL_ITEM)
				.withDurability(3)
				.withOwner(p.getName())
				.withAmount(1)
				.withDisplayName("§r"+name)
				.withLore(Arrays.asList(TranslatedString.getString(20001, this.language).replace("%gender%", this.gender.getString(this.language)).replace("%class%", this.mmoClass.getString(this.language)).split(" %n% ")))
				.build());
				this.p.openInventory(this.mainInv);
				this.isChoosingName = false;
				TitleAPI.clearTitle(p);
				sqlRS.close();
				return;
			} else {
				p.sendMessage(TranslatedString.getString(10099, p));
				sqlRS.close();
				return;
			}
		} catch (SQLException | ClassNotFoundException e) {
			p.sendMessage(TranslatedString.getString(10099, p));
			e.printStackTrace();
			return;
		} catch (CancelledPacketHandleException nmsE) {}
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent evt) {
		if (!(evt.getWhoClicked() instanceof Player)) return;
		Player p = (Player)evt.getWhoClicked();
		if (!p.getName().equals(this.p.getName())) return;
		evt.setCancelled(true);
		Inventory inv = evt.getClickedInventory();
		int slot = evt.getRawSlot();
		if (inv.getName().equals(this.mainInv.getName())) {
			if (slot == 2) {
				chooseClass();
				return;
			} else if (slot == 3) {
				chooseGender();
				return;
			} else if (slot == 4) {
				done();
				return;
			}
		} else if (inv.getName().equals(this.selectClass.getName())) {
			switch(slot) {
			case 0:
				this.mmoClass = MMOClass.WARRIOR;
				this.mainInv.setItem(0, new ItemStackBuilder()
				.withMaterial(Material.SKULL_ITEM)
				.withDurability(3)
				.withOwner(p.getName())
				.withAmount(1)
				.withDisplayName("§r"+charName)
				.withLore(Arrays.asList(TranslatedString.getString(20001, this.language).replace("%gender%", this.gender.getString(this.language)).replace("%class%", this.mmoClass.getString(this.language)).split(" %n% ")))
				.build());
				this.p.openInventory(this.mainInv);
				return;
			case 1:
				this.mmoClass = MMOClass.ARCHER;
				this.mainInv.setItem(0, new ItemStackBuilder()
				.withMaterial(Material.SKULL_ITEM)
				.withDurability(3)
				.withOwner(p.getName())
				.withAmount(1)
				.withDisplayName("§r"+charName)
				.withLore(Arrays.asList(TranslatedString.getString(20001, this.language).replace("%gender%", this.gender.getString(this.language)).replace("%class%", this.mmoClass.getString(this.language)).split(" %n% ")))
				.build());
				this.p.openInventory(this.mainInv);
				return;
			case 2:
				this.mmoClass = MMOClass.ASSASSIN;
				this.mainInv.setItem(0, new ItemStackBuilder()
				.withMaterial(Material.SKULL_ITEM)
				.withDurability(3)
				.withOwner(p.getName())
				.withAmount(1)
				.withDisplayName("§r"+charName)
				.withLore(Arrays.asList(TranslatedString.getString(20001, this.language).replace("%gender%", this.gender.getString(this.language)).replace("%class%", this.mmoClass.getString(this.language)).split(" %n% ")))
				.build());
				this.p.openInventory(this.mainInv);
				return;
			case 3:
				this.mmoClass = MMOClass.MAGE;
				this.mainInv.setItem(0, new ItemStackBuilder()
				.withMaterial(Material.SKULL_ITEM)
				.withDurability(3)
				.withOwner(p.getName())
				.withAmount(1)
				.withDisplayName("§r"+charName)
				.withLore(Arrays.asList(TranslatedString.getString(20001, this.language).replace("%gender%", this.gender.getString(this.language)).replace("%class%", this.mmoClass.getString(this.language)).split(" %n% ")))
				.build());
				this.p.openInventory(this.mainInv);
				return;
			default:
				return;
			}
		} else if (inv.getName().equals(this.selectGender.getName())) {
			if (slot == 1) {
				this.gender = Gender.MALE;
				this.mainInv.setItem(0, new ItemStackBuilder()
				.withMaterial(Material.SKULL_ITEM)
				.withDurability(3)
				.withOwner(p.getName())
				.withAmount(1)
				.withDisplayName("§r"+charName)
				.withLore(Arrays.asList(TranslatedString.getString(20001, this.language).replace("%gender%", this.gender.getString(this.language)).replace("%class%", this.mmoClass.getString(this.language)).split(" %n% ")))
				.build());
				this.p.openInventory(this.mainInv);
				return;
			} else if (slot == 3) {
				this.gender = Gender.FEMALE;
				this.mainInv.setItem(0, new ItemStackBuilder()
				.withMaterial(Material.SKULL_ITEM)
				.withDurability(3)
				.withOwner(p.getName())
				.withAmount(1)
				.withDisplayName("§r"+charName)
				.withLore(Arrays.asList(TranslatedString.getString(20001, this.language).replace("%gender%", this.gender.getString(this.language)).replace("%class%", this.mmoClass.getString(this.language)).split(" %n% ")))
				.build());
				this.p.openInventory(this.mainInv);
				return;
			}
		}
	}
}

enum Gender {

	MALE(21010),
	FEMALE(21011);

	private int id;

	private Gender(int id) {
		this.id = id;
	}

	public String getString(String language) {
		String returned = "null";
		try {
			SQLResultSet sqlRS = SQLHelper.getSortedEntrys(Main.DB_DATABASE, Main.DB_STRING_TEMPLATE, "entry", id);
			ResultSet nameRow = sqlRS.getResultSet();
			if (!nameRow.next()) throw new SQLException("String template did not contained the requested entry ("+id+")");
			if (nameRow.isAfterLast()) throw new SQLException("String template did not contained the requested entry ("+id+")");
			returned = nameRow.getString(language);
			sqlRS.close();
		} catch (Exception e) { e.printStackTrace(); }
		if (returned == null) returned = "null";
		return returned;
	}
}