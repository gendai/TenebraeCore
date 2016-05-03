package fr.tenebrae.MMOCore.Characters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.server.v1_9_R1.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.SQLResultSet;
import fr.tenebrae.MMOCore.Bags.Bag;
import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.Items.ItemUtils;
import fr.tenebrae.MMOCore.LoginScreen.LoginScreen;
import fr.tenebrae.MMOCore.Mechanics.MMOClass;
import fr.tenebrae.MMOCore.Mechanics.Stats;
import fr.tenebrae.MMOCore.Quests.DiscoverCoord;
import fr.tenebrae.MMOCore.Quests.KillCounter;
import fr.tenebrae.MMOCore.Quests.Quest;
import fr.tenebrae.MMOCore.Quests.QuestObjective;
import fr.tenebrae.MMOCore.Quests.QuestRegistry;
import fr.tenebrae.MMOCore.Utils.ActionBarAPI;
import fr.tenebrae.MMOCore.Utils.ItemStackBuilder;
import fr.tenebrae.MMOCore.Utils.SQLHelper;
import fr.tenebrae.MMOCore.Utils.TranslatedString;
import fr.tenebrae.MMOCore.Utils.Serializers.InventorySerializer;
import fr.tenebrae.MMOCore.Utils.Serializers.LocationSerializer;
import fr.tenebrae.PlayerLanguage.LanguageAPI;
import fr.tenebrae.PlayerLanguage.Languages;

public class Character {

	private String characterName = "";
	private Player accountPlayer;
	private int level = 1;
	private int xp = 0;
	private MMOClass mmoClass = null;
	public int hp = 0;
	public Map<Stats,Double> stats = new HashMap<Stats,Double>();
	public Map<Stats,Double> statsBonus = new HashMap<Stats,Double>();
	public List<Bag> bags = new ArrayList<Bag>();
	public List<Quest> activeQuests = new ArrayList<Quest>();
	public net.minecraft.server.v1_9_R1.Entity lastDamager = null;
	public CharacterEquipment equipment;
	public Date lastAttackDate;
	public Date lastDamagedDate;
	public boolean dualWielding = false;
	public int money = 0;
	public boolean inFight = false;


	public Character(String characterName) {
		try {
			SQLResultSet sqlRS = SQLHelper.getSortedEntrys(Main.DB_DATABASE, Main.DB_PLAYERS_TABLE, "name", characterName);
			ResultSet charRow = sqlRS.getResultSet();
			if (!charRow.next()) throw new SQLException("Characters table didn't contained the character "+characterName);
			if (!Bukkit.getPlayer(UUID.fromString(charRow.getString("owner_uuid"))).isOnline()) throw new IllegalStateException("The owner of "+characterName+" isn't online.");

			this.characterName = characterName;
			this.lastAttackDate = new Date();
			this.lastDamagedDate = new Date(new Date().getTime()-150000);
			this.accountPlayer = Bukkit.getPlayer(UUID.fromString(charRow.getString("owner_uuid")));
			this.accountPlayer.getInventory().setContents(InventorySerializer.fromBase64(charRow.getString("inventory")).getContents());
			this.level = charRow.getInt("level");
			this.xp = charRow.getInt("xp");
			this.mmoClass = MMOClass.valueOf(charRow.getString("class"));
			this.hp = charRow.getInt("hp");
			this.money = charRow.getInt("money");

			String serializedStats = charRow.getString("stats");
			String[] sStats = serializedStats.split("@");
			for (String s : sStats) {
				String[] separate = s.split(":");
				stats.put(Stats.valueOf(separate[0]), Double.valueOf(separate[1]));
			}
			for (Stats s : Stats.values()) statsBonus.put(s, 0.0D);

			if (!charRow.getString("activeQuests").equals("none")) {
				if (!charRow.getString("activeQuests").contains("#@#")) {
					String[] qRaw = charRow.getString("activeQuests").split(";");
					Quest quest = (Quest)QuestRegistry.getQuest(Integer.parseInt(qRaw[0])).deepClone();
					int index = 0;
					for (String sObj : qRaw[1].split("/")) {
						String[] params = sObj.split(",");
						switch(params[0]){
							case "KILL":
								KillCounter kc = (KillCounter)quest.getObjectives().get(index).getData2();
								kc.setCount(Integer.parseInt(params[1]));
								quest.getObjectives().get(index).setData2(kc);
								break;
							case "DISCOVER":
								DiscoverCoord dc = (DiscoverCoord)quest.getObjectives().get(index).getData1();
								dc.setIsArrived(params[1].equals("true") ? true : false);
								quest.getObjectives().get(index).setData1(dc);
								break;
							default:
								break;
						}
						index++;
					}
					activeQuests.add(quest);
				} else {
					for (String sQuest : charRow.getString("activeQuests").split("#@#")) {
						String[] qRaw = sQuest.split(";");
						Quest quest = (Quest)QuestRegistry.getQuest(Integer.parseInt(qRaw[0])).deepClone();
						int index = 0;
						for (String sObj : qRaw[1].split("/")) {
							String[] params = sObj.split(",");
							switch(params[0]){
								case "KILL":
									KillCounter kc = (KillCounter)quest.getObjectives().get(index).getData2();
									kc.setCount(Integer.parseInt(params[1]));
									quest.getObjectives().get(index).setData2(kc);
									break;
								case "DISCOVER":
									DiscoverCoord dc = (DiscoverCoord)quest.getObjectives().get(index).getData1();
									dc.setIsArrived(params[1].equals("true") ? true : false);
									quest.getObjectives().get(index).setData1(dc);
									break;
								default:
									break;
							}
							index++;
						}
						activeQuests.add(quest);
					}
				}
			}

			if (!charRow.getString("bags").equals("none")) {
				if (!charRow.getString("bags").contains("#@#")) {
					bags.add(new Bag(charRow.getString("bags")));
				} else {
					for (String sBag : charRow.getString("bags").split("#@#")) {
						bags.add(new Bag(sBag));
					}
				}
			}

			this.equipment = new CharacterEquipment(this, charRow.getString("equipment"));

			this.accountPlayer.teleport(LocationSerializer.stringToLocation(charRow.getString("location")));
			this.accountPlayer.setExp(1F * ((1F * this.xp) / (1F * (8+(level*112)))));
			this.accountPlayer.setLevel(level);

			sqlRS.close();

			new BukkitRunnable() {
				@Override
				public void run() {
					updateInventory();
				}
			}.runTaskLaterAsynchronously(Main.plugin, 20L);
			new BukkitRunnable() {
				@Override
				public void run() {
					updateStats();
				}
			}.runTaskLaterAsynchronously(Main.plugin, 30L);
		} catch (Exception e) { e.printStackTrace(); }

		new BukkitRunnable() {
			@Override
			public void run() {
				if (!accountPlayer.isOnline()) {
					this.cancel();
					return;
				}
				if (inFight) {
					if (new Date().getTime() - lastDamagedDate.getTime() > 6000) {
						setInFight(false);
					}
				}
				int maxHp = getMaxHp();
				if (!inFight && hp < maxHp) {
					hp += 1+(level*2);
					if (hp > maxHp) hp = maxHp;
					accountPlayer.setHealth((hp*20.0)/maxHp);
				}
				if (hp > maxHp) hp = maxHp;
				ActionBarAPI.sendActionBar(accountPlayer, "§cHealth: §7"+hp+"§6/§f"+maxHp);
			}
		}.runTaskTimerAsynchronously(Main.plugin, 20L, 20L);
	}

	public Player getAccount() {
		return accountPlayer;
	}

	public EntityPlayer getNMSAccount() {
		return ((CraftPlayer)accountPlayer).getHandle();
	}

	public void setAccount(Player account) {
		this.accountPlayer = account;
	}

	public String getCharacterName() {
		return characterName;
	}

	public String getLanguage() {
		return LanguageAPI.getStringLanguage(accountPlayer);
	}

	public void setLanguage(Languages language) {
		LanguageAPI.setLanguage(accountPlayer, language);
		updateInventory();
	}

	public int getLevel() {
		return level;
	}

	public int getXp() {
		return xp;
	}

	public void addXp(int xp) {
		//Main.log.info("Adding "+xp+" xp");
		//Main.log.info("Old xp was "+this.xp);
		this.xp += xp;
		//Main.log.info("New xp is "+this.xp);

		int max = 8+(level*112);
		//Main.log.info("Xp cap for level "+level+" is "+max);
		if (this.xp >= max) {
			this.xp = this.xp - max;
			this.levelUp();
		}

		this.accountPlayer.setExp(1F * ((1F * this.xp) / (1F * (8+(level*112)))));
	}

	public void addStat(Stats stat, double amount) {
		this.stats.put(stat, this.stats.get(stat)+amount);
	}

	public void levelUp() {
		this.level++;
		switch(mmoClass) {
		case ARCHER:
			this.addStat(Stats.AGILITY, 1.5);
			this.addStat(Stats.CRITICAL_CHANCE, 0.22);
			this.addStat(Stats.HIT_CHANCE, 0.22);
			this.addStat(Stats.STAMINA, 0.75);
			break;
		case ASSASSIN:
			this.addStat(Stats.AGILITY, 1.5);
			this.addStat(Stats.STRENGTH, 1);
			this.addStat(Stats.STAMINA, 0.75);
			this.addStat(Stats.CRITICAL_CHANCE, 0.22);
			this.addStat(Stats.DODGE_CHANCE, 0.22);
			break;
		case MAGE:
			this.addStat(Stats.INTELLIGENCE, 1.5);
			this.addStat(Stats.SPIRIT, 1);
			this.addStat(Stats.MAGICAL_ARMOR, 1);
			this.addStat(Stats.STAMINA, 0.57);
			break;
		case WARRIOR:
			this.addStat(Stats.STRENGTH, 1.5);
			this.addStat(Stats.AGILITY, 1);
			this.addStat(Stats.STAMINA, 1);
			this.addStat(Stats.CRITICAL_CHANCE, 0.18);
			this.addStat(Stats.HIT_CHANCE, 0.18);
			this.addStat(Stats.BLOCK_CHANCE, 0.18);
			break;
		default:
			break;

		}
		this.hp = getMaxHp();
		this.accountPlayer.setHealth(20.0D);
		this.accountPlayer.setLevel(level);
	}

	public void updateStats() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Stats s : Stats.values()) statsBonus.put(s, 0.0D);
				for (Item i : equipment.asList()) {
					if (i != null) {
						if (i.getStats() != null) {
							if (!i.getStats().isEmpty()) {
								for (Entry<Stats,Double> stat : i.getStats().entrySet()) {
									statsBonus.put(stat.getKey(), statsBonus.get(stat.getKey())+stat.getValue());
								}
							}
						}
					}
				}
			}
		}.runTaskLaterAsynchronously(Main.plugin, 1L);
	}

	public double getStat(Stats stat) {
		double returned = 0;
		if (stat == Stats.ARMOR) {
			double armor = (stats.get(stat)+statsBonus.get(stat));
			returned = armor / (0.25D * this.getLevel());
			new DecimalFormat("#.##").format(returned);
		} else if (stat == Stats.MAGICAL_ARMOR) {
			double armor = (stats.get(stat)+statsBonus.get(stat));
			returned = armor / (0.35D * this.getLevel());
			new DecimalFormat("#.##").format(returned);
		} else if (stat == Stats.ATTACK_SPEED) {
			returned = (int) (stats.get(stat)+statsBonus.get(stat));
		} else if (stat == Stats.HEALTH) {
			returned = (int) (stats.get(stat)+statsBonus.get(stat));
			returned += (int) ((stats.get(Stats.STAMINA)*5)+(statsBonus.get(Stats.STAMINA)*5));
		} else {
			returned = stats.get(stat)+statsBonus.get(stat);
		}
		return returned;
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

	public void removeItem(ItemStack is) {
		if (bags.isEmpty()) return;
		for (Bag b : bags) {
			if (b.getInventory().contains(is)) {
				b.getInventory().setItem(b.getInventory().first(is), null);
				return;
			}
		}
	}

	public void addItem(final ItemStack is) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!ItemUtils.isMMOItem(is)) return;
				final Item i = new Item(is).setLocale(getLanguage());
				if (bags.isEmpty()) return;
				for (final Bag b : bags) {
					if (!b.isFull()) {
						new BukkitRunnable() {
							@Override
							public void run() {
								b.getInventory().addItem(i.getItemStack());
							}
						}.runTaskLater(Main.plugin, 2L);
						return;
					}
				}
			}
		}.runTaskAsynchronously(Main.plugin);
	}

	public boolean canDualWield() {
		return this.dualWielding;
	}

	public boolean canAttack() {
		if (new Date().getTime()-this.lastAttackDate.getTime() >= this.getStat(Stats.ATTACK_SPEED)) {
			return true;
		} else return false;
	}

	public void damage(net.minecraft.server.v1_9_R1.Entity damager, int amount) {
		this.hp -= amount;
		this.lastDamager = damager;
		this.lastDamagedDate = new Date();
		this.accountPlayer.damage(0.0D);
		if (hp <= 0) {
			die();
			return;
		}
		this.accountPlayer.setHealth((this.hp*20.0)/getMaxHp());
		if (!this.inFight) {
			setInFight(true);
			new BukkitRunnable() {
				@Override
				public void run() {
					if (new Date().getTime()-lastDamagedDate.getTime() >= 6000) {
						setInFight(false);
						this.cancel();
						return;
					}
				}
			}.runTaskTimerAsynchronously(Main.plugin, 20L, 20L);
		}
	}

	public void setInFight(boolean fight) {
		if (fight && !this.inFight) accountPlayer.sendMessage("§cYou are now in fight.");
		else if (!fight && this.inFight) accountPlayer.sendMessage("§aYou are no longer in fight.");
		this.inFight = fight;
	}

	public void damage(Entity damager, int amount) {
		damage(((CraftEntity)damager).getHandle(), amount);
	}

	public int getMaxHp() {
		return (int)this.getStat(Stats.HEALTH);
	}

	public MMOClass getMmoClass() {
		return mmoClass;
	}

	public void setMmoClass(MMOClass mmoClass) {
		this.mmoClass = mmoClass;
	}

	public void disconnect() {
		save();
		accountPlayer.sendMessage("§aSuccessfully disconnected.");
		Main.connectedCharacters.remove(accountPlayer);
		new LoginScreen(accountPlayer);
	}

	public void die() {

	}

	public void save() {
		try {

			String serializedQuests = "";
			String serializedStats = "";
			String serializedEquipment = "";
			String serializedBags = "";

			if (!activeQuests.isEmpty()) {
				for (Quest quest : activeQuests) {
					serializedQuests += QuestRegistry.getId(quest.getClass())+";";
					for(QuestObjective obj : quest.getObjectives()){
						switch (obj.getType()){
						case KILL:
							KillCounter kc = (KillCounter)obj.getData2();
							serializedQuests += "KILL,"+kc.getCount()+"/";
							break;
						case DISCOVER:
							DiscoverCoord dc = (DiscoverCoord)obj.getData1();
							serializedQuests += "DISCOVER,"+dc.getIsArrived()+"/";
							break;
						default:
							break;
						}
					}
					serializedQuests = serializedQuests.substring(0, serializedQuests.length()-1);
					serializedQuests += "#@#";
				}
				serializedQuests = serializedQuests.substring(0, serializedQuests.length()-3);
			} else {
				serializedQuests = "none";
			}

			for (Entry<Stats,Double> entry : stats.entrySet()) {
				serializedStats += entry.getKey().toString()+":"+entry.getValue()+"@";
			}
			serializedStats = serializedStats.substring(0, serializedStats.length()-1);

			for (Item item : this.equipment.asList()) {
				if (item != null) {
					serializedEquipment += item.toString()+"#@#";
				} else {
					serializedEquipment += "null"+"#@#";
				}
			}
			serializedEquipment = serializedEquipment.substring(0, serializedEquipment.length()-3);

			if (bags.isEmpty()) {
				serializedBags = "none";
			} else {
				for (Bag bag : bags) {
					serializedBags += bag.toString()+"#@#";
				}
				serializedBags = serializedBags.substring(0, serializedBags.length()-3);
			}

			String[] columns = {"name","owner_uuid","level","xp","class","hp","activeQuests","stats","equipment","location","bags","money"};
			Object[] values = {this.characterName, this.accountPlayer.getUniqueId().toString(), this.level, this.xp, this.mmoClass.toString(), this.hp, serializedQuests, serializedStats, serializedEquipment, LocationSerializer.locationToString(this.accountPlayer.getLocation()), serializedBags, this.money};

			SQLHelper.updateEntry(Main.DB_DATABASE, Main.DB_PLAYERS_TABLE, "name", this.characterName, columns, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateInventory() {
		String language = this.getLanguage();
		this.equipment.update();
		for (Bag b : this.bags) {
			for (ItemStack is : b.getInventory().getContents()) {
				if (is != null) {
					if (ItemUtils.isMMOItem(is)) {
						Item i = new Item(is);
						i.setLocale(language);
					}
				}
			}
		}

		Inventory returned = this.accountPlayer.getInventory();
		returned.setItem(9, new ItemStackBuilder()
				.withMaterial(Material.PAPER)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70000, language))
				.build());
		returned.setItem(11, new ItemStackBuilder()
				.withMaterial(Material.CHORUS_FRUIT)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70001, language))
				.build());
		returned.setItem(12, new ItemStackBuilder()
				.withMaterial(Material.CHORUS_FRUIT_POPPED)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70002, language))
				.build());
		returned.setItem(14, new ItemStackBuilder()
				.withMaterial(Material.MELON_SEEDS)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70003, language))
				.build());
		returned.setItem(15, new ItemStackBuilder()
				.withMaterial(Material.PUMPKIN_SEEDS)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70004, language))
				.build());
		returned.setItem(17, new ItemStackBuilder()
				.withMaterial(Material.BARRIER)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70005, language))
				.build());
		for (int k = 18; k < 24; k++) {
			returned.setItem(k, new ItemStackBuilder()
					.withMaterial(Material.BEETROOT_SEEDS)
					.withAmount(1)
					.withDisplayName(TranslatedString.getString(70006, language))
					.build());
		}
		returned.setItem(26, new ItemStackBuilder()
				.withMaterial(Material.BOOK)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70007, language))
				.build());
		for (int k = 27; k < 31; k++) {
			returned.setItem(k, new ItemStackBuilder()
					.withMaterial(Material.RABBIT_HIDE)
					.withAmount(1)
					.withDisplayName(TranslatedString.getString(70008, language))
					.build());
		}
		returned.setItem(32, new ItemStackBuilder()
				.withMaterial(Material.FEATHER)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70009, language))
				.build());
		returned.setItem(33, new ItemStackBuilder()
				.withMaterial(Material.FEATHER)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70009, language))
				.build());
		returned.setItem(35, new ItemStackBuilder()
				.withMaterial(Material.BOOK_AND_QUILL)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70010, language))
				.build());
	}

	public static Inventory setupInventory(Item startWeapon, String language) {
		Inventory returned = Bukkit.createInventory(null, InventoryType.PLAYER);

		returned.setItem(0, startWeapon.getItemStack());
		returned.setItem(9, new ItemStackBuilder()
				.withMaterial(Material.PAPER)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70000, language))
				.build());
		returned.setItem(11, new ItemStackBuilder()
				.withMaterial(Material.CHORUS_FRUIT)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70001, language))
				.build());
		returned.setItem(12, new ItemStackBuilder()
				.withMaterial(Material.CHORUS_FRUIT_POPPED)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70002, language))
				.build());
		returned.setItem(14, new ItemStackBuilder()
				.withMaterial(Material.MELON_SEEDS)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70003, language))
				.build());
		returned.setItem(15, new ItemStackBuilder()
				.withMaterial(Material.PUMPKIN_SEEDS)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70004, language))
				.build());
		returned.setItem(17, new ItemStackBuilder()
				.withMaterial(Material.BARRIER)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70005, language))
				.build());
		for (int k = 18; k < 24; k++) {
			returned.setItem(k, new ItemStackBuilder()
					.withMaterial(Material.BEETROOT_SEEDS)
					.withAmount(1)
					.withDisplayName(TranslatedString.getString(70006, language))
					.build());
		}
		returned.setItem(26, new ItemStackBuilder()
				.withMaterial(Material.BOOK)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70007, language))
				.build());
		for (int k = 27; k < 31; k++) {
			returned.setItem(k, new ItemStackBuilder()
					.withMaterial(Material.RABBIT_HIDE)
					.withAmount(1)
					.withDisplayName(TranslatedString.getString(70008, language))
					.build());
		}
		returned.setItem(32, new ItemStackBuilder()
				.withMaterial(Material.FEATHER)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70009, language))
				.build());
		returned.setItem(33, new ItemStackBuilder()
				.withMaterial(Material.FEATHER)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70009, language))
				.build());
		returned.setItem(35, new ItemStackBuilder()
				.withMaterial(Material.BOOK_AND_QUILL)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70010, language))
				.build());


		return returned;
	}

	private String getFormattedMoney() {
		String returned = "§r";
		if (money > 10000) {
			String price = String.valueOf(money);
			returned += price.substring(0, price.length()-4)+"§f郂§r";
			returned += " "+price.substring(price.length()-4, price.length()-2)+"§f郁§r";
			returned += " "+price.substring(price.length()-2, price.length())+"§f郀§r";
		} else if (money > 100) {
			String price = String.valueOf(money);
			returned += price.substring(0, price.length()-2)+"§f郁§r";
			returned += " "+price.substring(price.length()-2, price.length())+"§f郀§r";
		} else {
			returned += String.valueOf(money)+"§f郀§r";
		}
		return returned;
	}

	public void openSheet() {
		accountPlayer.closeInventory();
		Inventory opened = Bukkit.createInventory(accountPlayer, 7*9, getFormattedMoney());

		this.equipment.display(opened);
		for (int k = 0; k < 7; k++) {
			opened.setItem(4+(k*9), new ItemStackBuilder()
					.withMaterial(Material.IRON_FENCE)
					.withDisplayName("§r")
					.build());
		}
		int k;
		for (k = 0; k < bags.size(); k++) {
			opened.setItem(59+k, new ItemStackBuilder()
					.withMaterial(Material.LEATHER)
					.withDisplayName(TranslatedString.getString(70102, accountPlayer)+(k+1))
					.build());
		}
		while (k < 4) {
			opened.setItem(59+k, new ItemStackBuilder()
					.withMaterial(Material.RABBIT_HIDE)
					.withDisplayName(TranslatedString.getString(70008, accountPlayer))
					.build());
			k++;
		}

		opened.setItem(54, new ItemStackBuilder()
				.withMaterial(Material.BOOK)
				.withDisplayName("§6"+TranslatedString.getString(70104, accountPlayer))
				.withLore(this.getDisplayedPrimaryStats())
				.build());
		opened.setItem(55, new ItemStackBuilder()
				.withMaterial(Material.BOOK)
				.withDisplayName("§6"+TranslatedString.getString(70105, accountPlayer))
				.withLore(this.getDisplayedSecondaryStats())
				.build());
		opened.setItem(56, new ItemStackBuilder()
				.withMaterial(Material.BOOK)
				.withDisplayName("§a"+TranslatedString.getString(70106, accountPlayer))
				.withLore(this.getDisplayedGeneralInfos())
				.build());
		opened.setItem(57, new ItemStackBuilder()
				.withMaterial(Material.BOOK)
				.withDisplayName("§b"+TranslatedString.getString(70107, accountPlayer))
				.withLore(Arrays.asList("§cWork In Progress"))
				.build());

		if (!bags.isEmpty()) {
			bags.get(0).display(opened);
			ItemStack is = opened.getItem(59);
			ItemMeta meta = is.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
			is.setItemMeta(meta);
			opened.setItem(59, is);
		}

		accountPlayer.openInventory(opened);
	}

	public void openBag(int slot) {
		if (this.bags.isEmpty()) return;
		if (this.bags.size() < slot) return;
		accountPlayer.closeInventory();
		Inventory opened = Bukkit.createInventory(accountPlayer, this.bags.get(slot-1).getInventory().getSize()+9, TranslatedString.getString(70102, accountPlayer)+slot);

		opened.setContents(this.bags.get(slot-1).getInventory().getContents());
		opened.setItem(opened.getSize()-9, new ItemStackBuilder()
				.withMaterial(Material.RECORD_5)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70101, accountPlayer))
				.build());
		for (int k = opened.getSize()-8; k < opened.getSize()-1; k++) {
			opened.setItem(k, new ItemStackBuilder()
					.withMaterial(Material.STAINED_GLASS_PANE)
					.withDurability(7)
					.withDisplayName("§r")
					.build());
		}
		opened.setItem(opened.getSize()-1, new ItemStackBuilder()
				.withMaterial(Material.RECORD_6)
				.withAmount(1)
				.withDisplayName(TranslatedString.getString(70100, accountPlayer))
				.build());
		accountPlayer.openInventory(opened);
	}

	public List<String> getDisplayedPrimaryStats() {
		List<String> returned = new ArrayList<String>();

		returned.add("§7"+Stats.STRENGTH.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§2"+(int)this.getStat(Stats.STRENGTH));
		returned.add("§7"+Stats.AGILITY.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§2"+(int)this.getStat(Stats.AGILITY));
		returned.add("§7"+Stats.STAMINA.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§2"+(int)this.getStat(Stats.STAMINA));
		returned.add("§7"+Stats.INTELLIGENCE.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§2"+(int)this.getStat(Stats.INTELLIGENCE));
		returned.add("§7"+Stats.SPIRIT.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§2"+(int)this.getStat(Stats.SPIRIT));
		returned.add("§7"+Stats.HEALTH.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§c"+(int)this.getStat(Stats.HEALTH));
		returned.add("§7"+Stats.MANA.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§3"+(int)this.getStat(Stats.MANA));

		return returned;
	}

	public List<String> getDisplayedSecondaryStats() {
		List<String> returned = new ArrayList<String>();

		returned.add("§7"+Stats.ATTACK_SPEED.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§6"+(int)this.getStat(Stats.ATTACK_SPEED));
		returned.add("§7"+Stats.ARMOR.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§6"+new DecimalFormat("#.##").format(this.getStat(Stats.ARMOR))+"%");
		returned.add("§7"+Stats.MAGICAL_ARMOR.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§6"+new DecimalFormat("#.##").format(this.getStat(Stats.MAGICAL_ARMOR))+"%");
		returned.add("§7"+Stats.POWER.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§6"+(int)this.getStat(Stats.POWER));
		returned.add("§7"+Stats.MAGICAL_POWER.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§6"+(int)this.getStat(Stats.MAGICAL_POWER));
		returned.add("§7"+Stats.CRITICAL_CHANCE.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§6"+new DecimalFormat("#.##").format(this.getStat(Stats.CRITICAL_CHANCE))+"%");
		returned.add("§7"+Stats.DODGE_CHANCE.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§6"+new DecimalFormat("#.##").format(this.getStat(Stats.DODGE_CHANCE))+"%");
		returned.add("§7"+Stats.HIT_CHANCE.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§6"+new DecimalFormat("#.##").format(this.getStat(Stats.HIT_CHANCE))+"%");
		returned.add("§7"+Stats.BLOCK_CHANCE.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§6"+new DecimalFormat("#.##").format(this.getStat(Stats.BLOCK_CHANCE))+"%");
		returned.add("§7"+Stats.XP_BONUS.getString(LanguageAPI.getStringLanguage(accountPlayer))+"§6"+new DecimalFormat("#.##").format(this.getStat(Stats.XP_BONUS))+"%");

		return returned;
	}

	public List<String> getDisplayedGeneralInfos() {
		List<String> returned = new ArrayList<String>();

		returned.add("§7"+TranslatedString.getString(70108, accountPlayer)+"§e"+this.characterName);
		returned.add("§7"+TranslatedString.getString(70110, accountPlayer)+"§e"+this.mmoClass.getString(LanguageAPI.getStringLanguage(accountPlayer)));
		returned.add("§7"+TranslatedString.getString(70109, accountPlayer)+"§e"+"TODO Save gender information");

		return returned;
	}
}
