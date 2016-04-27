package fr.tenebrae.MMOCore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.Tuple;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import fr.tenebrae.MMOCore.Bags.Bag;
import fr.tenebrae.MMOCore.Characters.Character;
import fr.tenebrae.MMOCore.Entities.IClickable;
import fr.tenebrae.MMOCore.Entities.ICreature;
import fr.tenebrae.MMOCore.Entities.L01MineSpider;
import fr.tenebrae.MMOCore.Entities.QuestNpc;
import fr.tenebrae.MMOCore.Items.IClickableItem;
import fr.tenebrae.MMOCore.Items.IUsableItem;
import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.Items.ItemRegistry;
import fr.tenebrae.MMOCore.Items.ItemUtils;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.LoginScreen.CharacterCreator;
import fr.tenebrae.MMOCore.LoginScreen.LoginScreen;
import fr.tenebrae.MMOCore.Mechanics.Damage;
import fr.tenebrae.MMOCore.Quests.DiscoverCoord;
import fr.tenebrae.MMOCore.Quests.KillCounter;
import fr.tenebrae.MMOCore.Quests.Quest;
import fr.tenebrae.MMOCore.Quests.QuestCondition;
import fr.tenebrae.MMOCore.Quests.QuestCondition.ConditionType;
import fr.tenebrae.MMOCore.Quests.QuestObjective;
import fr.tenebrae.MMOCore.Quests.QuestObjective.ObjectiveType;
import fr.tenebrae.MMOCore.Quests.QuestReward;
import fr.tenebrae.MMOCore.Quests.QuestReward.RewardType;
import fr.tenebrae.MMOCore.Utils.ItemStackBuilder;
import fr.tenebrae.MMOCore.Utils.TitleAPI;
import fr.tenebrae.MMOCore.Utils.TranslatedString;

public class Listeners implements Listener {

	public Main plugin;
	private List<Integer> internalSl01 = new ArrayList<Integer>(Arrays.asList(5,6,7,8,17,16,15,14,26,25,24,23,35,34,33,32,44,43,42,41,53,52,51,50));
	public static ArrayList<Quest> quests = new ArrayList<>();

	public Listeners(Main plugin) {
		this.plugin = plugin;
	}



	// INDEV PART TEMP COMMANDS ----- START

	@EventHandler
	public void command(final PlayerCommandPreprocessEvent evt) {
		String[] args = evt.getMessage().split(" ");
		if (args[0].equalsIgnoreCase("/spidy")) {
			new L01MineSpider(evt.getPlayer().getLocation()).spawn();
			return;
		} else if (args[0].equalsIgnoreCase("/getitem")) {
			if (args.length == 1) return;
			try {
				Main.connectedCharacters.get(evt.getPlayer()).addItem(ItemRegistry.getItem(Integer.valueOf(args[1])).getItemStack());
			} catch (NumberFormatException e) { return; }
		}
	}

	// INDEV PART TEMP COMMANDS ----- END

	
	// GENDAI QUEST EVENTS ---- START
	
	private Tuple<ArrayList<Quest>,ArrayList<QuestObjective>> entityInQuestKill(org.bukkit.entity.Entity entity){
		ArrayList<Quest> questsList = new ArrayList<>();
		ArrayList<QuestObjective> objList = new ArrayList<>();
		for(Quest q : quests){
			if(!q.completed){
				for(QuestObjective obj : q.getObjectives()){
					if(obj.getType().equals(ObjectiveType.KILL) && obj.getData0().equals(entity.getClass())){
						questsList.add(q);
						objList.add(obj);
					}
				}
			}
		}
		return new Tuple<ArrayList<Quest>, ArrayList<QuestObjective>>(questsList, objList);
	}
	
	@EventHandler
	public void onKillEntity(EntityDeathEvent event){
		if(event.getEntity().getKiller() instanceof Player){
			Tuple<ArrayList<Quest>, ArrayList<QuestObjective>> tupl = entityInQuestKill(event.getEntity());
			if(tupl.a().size() > 0){
				CraftEntity dead = (CraftEntity) event.getEntity();
				if(dead.isDead()){
					for(int i = 0; i < tupl.a().size(); i++){
						KillCounter killc = (KillCounter)tupl.b().get(i).getData2();
						killc.add(1);
						tupl.b().get(i).setData2(killc);
						tupl.a().get(i).informUpdate(event.getEntity().getKiller());
					}
				}
			}
		}
	}

	private Tuple<ArrayList<Quest>,ArrayList<QuestObjective>> hasReachQuest(){
		ArrayList<Quest> questsList = new ArrayList<>();
		ArrayList<QuestObjective> objList = new ArrayList<>();
		for(Quest q : quests){
			if(!q.completed){
				for(QuestObjective obj : q.getObjectives()){
					if(obj.getType().equals(ObjectiveType.DISCOVER)){
						questsList.add(q);
						objList.add(obj);
					}
				}
			}
		}
		return new Tuple<ArrayList<Quest>, ArrayList<QuestObjective>>(questsList, objList);
	}


	@EventHandler
	public void onPlayerMove(PlayerMoveEvent evt){
		Location  loc = evt.getTo();
		Tuple<ArrayList<Quest>, ArrayList<QuestObjective>> tupl = hasReachQuest();
		if(tupl.a().size() > 0){
			for(int i = 0; i < tupl.a().size(); i++){
				DiscoverCoord dc  = (DiscoverCoord)tupl.b().get(i).getData1();
				if(dc.hasReached(loc) && !dc.getIsArrived()){
					dc.setIsArrived(true);
					tupl.a().get(i).informUpdate(evt.getPlayer());
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		/*if(e.getAction() == Action.RIGHT_CLICK_BLOCK && (e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN)){
			e.setCancelled(true);
			ItemStack book = new ItemStack(Material.BOOK);
			ItemMeta meta = book.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA+"Journal de quêtes");
			book.setItemMeta(meta);

			Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Quests");
			inv.setItem(13,book);

			e.getPlayer().openInventory(inv);
		}else */if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CAULDRON){
			QuestCondition condition = new QuestCondition(ConditionType.LEVEL, (int)1, e.getPlayer(), null, null);
			KillCounter kc = new KillCounter();
			QuestObjective obj = new QuestObjective(ObjectiveType.KILL, org.bukkit.craftbukkit.v1_9_R1.entity.CraftZombie.class, (int)1, kc, null);
			QuestReward rew = new QuestReward(RewardType.XP, (int)30, e.getPlayer(), null, null);
			ArrayList<QuestCondition> conditionArr = new ArrayList<>();
			conditionArr.add(condition);
			ArrayList<QuestObjective> objArr = new ArrayList<>();
			objArr.add(obj);
			ArrayList<QuestReward> rewardArr = new ArrayList<>();
			rewardArr.add(rew);
			Quest q = new Quest("Quêtes test","Ceci est une description pour la quête principale", "1", 0, 0, objArr, conditionArr, rewardArr);

			condition = new QuestCondition(ConditionType.LEVEL, (int)5, e.getPlayer(), null, null);
			obj = new QuestObjective(ObjectiveType.DISCOVER, "Test Land", new DiscoverCoord(-198.7, 95, 100.451, 5, 3), null, null);
			KillCounter killc = new KillCounter();
			QuestObjective obj2 = new QuestObjective(ObjectiveType.KILL, CraftZombie.class, 3, killc, null);
			rew = new QuestReward(RewardType.MONEY, (int)20, null, null, null);
			ArrayList<QuestCondition> conditionArr2 = new ArrayList<>();
			conditionArr2.add(condition);
			ArrayList<QuestObjective> objArr2 = new ArrayList<>();
			objArr2.add(obj);
			objArr2.add(obj2);
			ArrayList<QuestReward> rewardArr2 = new ArrayList<>();
			rewardArr2.add(rew);
			Quest q2 = new Quest("Quêtes Secondaire","Ceci est une description pour la quête secondaire", "1", 1, 1, objArr2, conditionArr2, rewardArr2);
			QuestNpc npc = new QuestNpc(e.getPlayer().getLocation());
			npc.spawn();
			npc.addQuestNpc(q);
			npc.addQuestNpc(q2);
			Bukkit.getServer().getPluginManager().registerEvents(npc, this.plugin);
		}
	}
	
	private boolean hasPendingQuests(){
		for(Quest q : quests){
			if(!q.getFinished()){
				return true;
			}
		}
		return false;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e){
		if(e.getInventory().getTitle().equals(ChatColor.GOLD + "Quests")){
			if(e.getSlot() == 13){
				e.setCancelled(true);
				if(!hasPendingQuests()){
					e.getWhoClicked().sendMessage(ChatColor.DARK_AQUA+"You have no quest");
				}else{
					Inventory invBooksQuest = Bukkit.createInventory(null, 9*5, ChatColor.GOLD + "Quests List");
					if(quests.size() > 0){
						for(Quest q : quests){
							if(!q.getFinished()){
								invBooksQuest.addItem(q.getWrittenBook());
							}
						}
					}
					e.getWhoClicked().openInventory(invBooksQuest);
				}
			}
		}else if(e.getInventory().getTitle().equals(ChatColor.GOLD+"Quests List")){
			e.setCancelled(true);
			Quest quest = questFromBookMeta(e.getCurrentItem().getItemMeta());
			quest.informUpdate((Player)e.getWhoClicked());
		}
	}
	
	public Quest questFromBookMeta(ItemMeta bookMeta){
		for(Quest q : quests){
			ItemMeta qmeta = q.getWrittenBook().getItemMeta();
			if(qmeta.getDisplayName().equals(bookMeta.getDisplayName())){
				return q;
			}
		}
		return null;
	}

	private void openQuestDiary(Player player){
		if(!hasPendingQuests()){
			player.sendMessage(ChatColor.DARK_AQUA+"You have no quest");
		}else{
			Inventory invBooksQuest = Bukkit.createInventory(null, 9*5, ChatColor.GOLD + "Quests List");
			if(quests.size() > 0){
				for(Quest q : quests){
					if(!q.getFinished()){
						invBooksQuest.addItem(q.getWrittenBook());
					}
				}
			}
			player.openInventory(invBooksQuest);
		}
	}


	// GENDAI QUEST EVENTS ---- END
	
	@EventHandler
	public void onUnloadChunk(ChunkUnloadEvent evt) {
		evt.setCancelled(true);
	}

	@EventHandler
	public void entityDamage(EntityDamageEvent evt) {
		if (evt.getCause() == DamageCause.FALL) {
			evt.setDamage(evt.getDamage()/7.5);
		} else {
			if (evt.getEntity() instanceof Player) {

			}
		}
	}

	@EventHandler
	public void food(FoodLevelChangeEvent evt) {
		evt.setCancelled(true);
	}

	@EventHandler
	public void drop(PlayerDropItemEvent evt) {
		evt.setCancelled(true);
	}

	@EventHandler
	public void whenPlayerDamageEntity(EntityDamageByEntityEvent evt) {
		if (!(evt.getDamager() instanceof Player)) return;
		evt.setCancelled(true);
		Character damager = Main.connectedCharacters.get((Player)evt.getDamager());
		if (!damager.canAttack()) return;
		Item weapon = damager.equipment.getWeapon();
		int minDmg = 0;
		int maxDmg = 0;
		((Player)evt.getDamager()).getInventory().setHeldItemSlot(0);
		if (weapon != null) {
			minDmg = weapon.getMinDmg();
			maxDmg = weapon.getMaxDmg();
		}
		int baseDmg = minDmg+(new Random().nextInt(maxDmg-minDmg));

		Damage damage = new Damage(damager, ((CraftEntity)evt.getEntity()).getHandle(), baseDmg);

		if (((CraftEntity)evt.getEntity()).getHandle() instanceof ICreature) {
			damager.lastAttackDate = new Date();
			damage.apply();
		} else if (evt.getEntity() instanceof Player) {
			((Player)evt.getDamager()).sendMessage("§4* §cLe système de PK est en cours de développement.");
		}
	}

	@EventHandler
	public void onTarget(EntityTargetEvent evt) {
		evt.setCancelled(true);
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent evt) {
		if (evt.getSpawnReason() != SpawnReason.CUSTOM) evt.setCancelled(true);
	}

	@EventHandler
	public void move(PlayerMoveEvent evt) {
		if ((evt.getFrom().getX() == evt.getTo().getX()) && (evt.getFrom().getY() == evt.getTo().getY()) && (evt.getFrom().getZ() == evt.getTo().getZ())) return;

		//if (LoginScreen.loggingPlayers.contains(evt.getPlayer())) evt.setCancelled(true);
		//else if (CharacterCreator.creatingPlayers.containsKey(evt.getPlayer())) evt.setCancelled(true);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent evt) {
		evt.setJoinMessage("");
		new LoginScreen(evt.getPlayer());
	}

	@EventHandler
	public void onLeft(PlayerQuitEvent evt) {
		Player p = evt.getPlayer();
		TitleAPI.clearTitle(p);

		if (Main.connectedCharacters.containsKey(p)) {
			Main.connectedCharacters.get(p).save();
			Main.connectedCharacters.remove(p);
		}
		p.getInventory().clear();
		p.setExp(0F);
		p.setLevel(0);
		p.setFoodLevel(20);
		p.setMaxHealth(20.0D);
		p.setHealth(20.0D);

		if (CharacterCreator.creatingPlayers.containsKey(p)) {
			HandlerList.unregisterAll(CharacterCreator.creatingPlayers.get(p));
			CharacterCreator.creatingPlayers.remove(p);
		}
		if (LoginScreen.loggingPlayers.containsKey(p)) {
			LoginScreen ls = LoginScreen.loggingPlayers.get(p);

			for (Hologram hol : ls.chars) hol.delete();
			for (Hologram hol : ls.misc) hol.delete();
			ls.playButton.delete();
			LoginScreen.loggingPlayers.remove(p);
		}
	}

	@EventHandler
	public void invClick(InventoryClickEvent evt) {
		if (evt.isShiftClick()) evt.setCancelled(true);
		if (evt.getCurrentItem() == null) return;
		if (!(evt.getWhoClicked() instanceof Player)) return;
		if (evt.getClickedInventory().getType() != InventoryType.PLAYER) return;
		evt.setCancelled(true);
		Player p = (Player)evt.getWhoClicked();
		int slot = evt.getRawSlot();
		switch(slot) {
		case 9:
			Main.connectedCharacters.get(p).openSheet();
			break;
		case 17:
			Main.connectedCharacters.get(p).disconnect();
			break;
		case 27:
			Main.connectedCharacters.get(p).openBag(1);
			break;
		case 28:
			Main.connectedCharacters.get(p).openBag(2);
			break;
		case 29:
			Main.connectedCharacters.get(p).openBag(3);
			break;
		case 30:
			Main.connectedCharacters.get(p).openBag(4);
			break;
		case 57:
			openQuestDiary(p);
			break;
		default:
			break;
		}
	}

	@EventHandler
	public void bagClose(InventoryCloseEvent evt) {
		if (evt.getView().getTopInventory() == null) return;
		if (!(evt.getPlayer() instanceof Player)) return;
		Player p = (Player) evt.getPlayer();
		if (!evt.getView().getTopInventory().getName().contains(TranslatedString.getString(70102, p))) return;
		Inventory inv = evt.getView().getTopInventory();
		int openedBagNumber = Integer.valueOf(inv.getName().substring(inv.getName().length()-1));

		ItemStack[] newBagContent = new ItemStack[inv.getSize()-9];
		for (int k = 0; k < inv.getSize()-9; k++) {
			newBagContent[k] = inv.getItem(k);
		}
		Main.connectedCharacters.get(p).bags.get(openedBagNumber-1).setItems(newBagContent);
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void bagClick(InventoryClickEvent evt) {
		if (evt.getClickedInventory() == null) return;
		if (!(evt.getWhoClicked() instanceof Player)) return;
		Player p = (Player) evt.getWhoClicked();
		if (!evt.getClickedInventory().getName().contains(TranslatedString.getString(70102, p))) return;
		evt.setCancelled(true);
		int slot = evt.getRawSlot();
		Inventory inv = evt.getClickedInventory();
		if (inv.getItem(slot) == null) return;
		ItemStack is = inv.getItem(slot);
		if (is.getType() == Material.STAINED_GLASS_PANE && is.getDurability() == 7) {
			evt.setCancelled(true);
			return;
		}
		ItemMeta meta = is.getItemMeta();
		if (!meta.hasDisplayName()) return;
		int openedBagNumber = Integer.valueOf(inv.getName().substring(inv.getName().length()-1));
		if (meta.getDisplayName().equalsIgnoreCase(TranslatedString.getString(70101, p))) {
			evt.setCancelled(true);
			if (openedBagNumber <= 1) return;
			Main.connectedCharacters.get(p).openBag(openedBagNumber-1);
			return;
		} else if (meta.getDisplayName().equalsIgnoreCase(TranslatedString.getString(70100, p))) {
			evt.setCancelled(true);
			if (openedBagNumber >= 4) return;
			Main.connectedCharacters.get(p).openBag(openedBagNumber+1);
			return;
		}
		evt.setCancelled(false);
	}

	@EventHandler
	public void sheetClick(InventoryClickEvent evt) {
		if (!(evt.getWhoClicked() instanceof Player)) return;
		Player p = (Player)evt.getWhoClicked();
		if (evt.getClickedInventory() == null) return;
		if (evt.getRawSlot() == -999 || evt.getRawSlot() == 999) return;
		if (!evt.getClickedInventory().getName().contains("郀")) return;
		evt.setCancelled(true);
		if (evt.getClickedInventory().getItem(evt.getRawSlot()) == null) return;

		Character c = Main.connectedCharacters.get(p);
		Inventory inv = evt.getClickedInventory();
		int slot = evt.getRawSlot();

		if (internalSl01.contains(slot)) {
			if (ItemUtils.isMMOItem(ItemUtils.asNMS(inv.getItem(slot)))) {
				Item item = new Item(inv.getItem(slot));
				if (ItemUtils.isEquipableItem(item)) {
					Item oldItem;
					switch(item.getType()) {
					case BOOTS:
						oldItem = c.equipment.getBoots();
						c.removeItem(inv.getItem(slot));
						inv.setItem(slot, oldItem == null ? null : oldItem.getItemStack());
						c.equipment.setBoots(item);
						break;
					case CHESTPLATE:
						oldItem = c.equipment.getChestplate();
						c.removeItem(inv.getItem(slot));
						inv.setItem(slot, oldItem == null ? null : oldItem.getItemStack());
						c.equipment.setChestplate(item);
						break;
					case GLOVES:
						oldItem = c.equipment.getGloves();
						c.removeItem(inv.getItem(slot));
						inv.setItem(slot, oldItem == null ? null : oldItem.getItemStack());
						c.equipment.setGloves(item);
						break;
					case HELMET:
						oldItem = c.equipment.getHelmet();
						c.removeItem(inv.getItem(slot));
						inv.setItem(slot, oldItem == null ? null : oldItem.getItemStack());
						c.equipment.setHelmet(item);
						break;
					case LEGGINGS:
						oldItem = c.equipment.getLeggings();
						c.removeItem(inv.getItem(slot));
						inv.setItem(slot, oldItem == null ? null : oldItem.getItemStack());
						c.equipment.setLeggings(item);
						break;
					case NECKLACE:
						oldItem = c.equipment.getNecklace();
						c.removeItem(inv.getItem(slot));
						inv.setItem(slot, oldItem == null ? null : oldItem.getItemStack());
						c.equipment.setNecklace(item);
						break;
					case RING:
						if (evt.isLeftClick()) {
							oldItem = c.equipment.getFirstRing();
							c.removeItem(inv.getItem(slot));
							inv.setItem(slot, oldItem == null ? null : oldItem.getItemStack());
							c.equipment.setFirstRing(item);
						} else if (evt.isRightClick()) {
							oldItem = c.equipment.getSecondRing();
							c.removeItem(inv.getItem(slot));
							inv.setItem(slot, oldItem == null ? null : oldItem.getItemStack());
							c.equipment.setSecondRing(item);
						}
						break;
					default:
						return;
					}
					c.equipment.display(inv);
				} else if (item.getType() == ItemType.WEAPON) {
					if (evt.isLeftClick()) {
						Item oldItem = c.equipment.getWeapon();
						c.removeItem(inv.getItem(slot));
						inv.setItem(slot, oldItem == null ? null : oldItem.getItemStack());
						c.equipment.setWeapon(item);
					} else if (evt.isRightClick()) {
						if (!c.canDualWield()) {
							Item oldItem = c.equipment.getWeapon();
							c.removeItem(inv.getItem(slot));
							inv.setItem(slot, oldItem == null ? null : oldItem.getItemStack());
							c.equipment.setWeapon(item);
						}
						Item oldItem = c.equipment.getOffhand();
						c.removeItem(inv.getItem(slot));
						inv.setItem(slot, oldItem == null ? null : oldItem.getItemStack());
						c.equipment.setOffhand(item);
					}
				} else if (ItemUtils.isOffhandItem(item)) {
					Item oldItem = c.equipment.getOffhand();
					c.removeItem(inv.getItem(slot));
					inv.setItem(slot, oldItem == null ? null : oldItem.getItemStack());
					c.equipment.setOffhand(item);
				}
			}
		}

		if (evt.isRightClick()) return;

		switch(slot) {
		case 0:
			if (inv.getItem(slot).getType() != Material.BEETROOT) {
				if (ItemUtils.isMMOItem(ItemUtils.asNMS(inv.getItem(slot)))) {
					ItemStack is = inv.getItem(slot);
					for (Bag b : c.bags) {
						if (!b.isFull()) {
							b.getInventory().addItem(is);
							c.equipment.setHelmet(null);
							inv.setItem(0, new ItemStackBuilder()
							.withMaterial(Material.BEETROOT)
							.withDisplayName("§f"+TranslatedString.getString(30000, p))
							.build());
							return;
						}
					}
					p.sendMessage("§cInventory full.");
					return;
				}
			}
			break;
		case 9:
			if (inv.getItem(slot).getType() != Material.RABBIT_FOOT) {
				if (ItemUtils.isMMOItem(ItemUtils.asNMS(inv.getItem(slot)))) {
					ItemStack is = inv.getItem(slot);
					for (Bag b : c.bags) {
						if (!b.isFull()) {
							b.getInventory().addItem(is);
							c.equipment.setNecklace(null);
							inv.setItem(9, new ItemStackBuilder()
							.withMaterial(Material.RABBIT_FOOT)
							.withDisplayName("§f"+TranslatedString.getString(30004, p))
							.build());
							return;
						}
					}
					p.sendMessage("§cInventory full.");
					return;
				}
			}
			break;
		case 18:
			if (inv.getItem(slot).getType() != Material.RABBIT_STEW) {
				if (ItemUtils.isMMOItem(ItemUtils.asNMS(inv.getItem(slot)))) {
					ItemStack is = inv.getItem(slot);
					for (Bag b : c.bags) {
						if (!b.isFull()) {
							b.getInventory().addItem(is);
							c.equipment.setChestplate(null);
							inv.setItem(18, new ItemStackBuilder()
							.withMaterial(Material.RABBIT_STEW)
							.withDisplayName("§f"+TranslatedString.getString(30001, p))
							.build());
							return;
						}
					}
					p.sendMessage("§cInventory full.");
					return;
				}
			}
			break;
		case 19:
			if (inv.getItem(slot).getType() != Material.BLAZE_POWDER) {
				if (ItemUtils.isMMOItem(ItemUtils.asNMS(inv.getItem(slot)))) {
					ItemStack is = inv.getItem(slot);
					for (Bag b : c.bags) {
						if (!b.isFull()) {
							b.getInventory().addItem(is);
							c.equipment.setFirstRing(null);
							inv.setItem(19, new ItemStackBuilder()
							.withMaterial(Material.BLAZE_POWDER)
							.withDisplayName("§f"+TranslatedString.getString(30005, p))
							.build());
							return;
						}
					}
					p.sendMessage("§cInventory full.");
					return;
				}
			}
			break;
		case 27:
			if (inv.getItem(slot).getType() != Material.GOLDEN_CARROT) {
				if (ItemUtils.isMMOItem(ItemUtils.asNMS(inv.getItem(slot)))) {
					ItemStack is = inv.getItem(slot);
					for (Bag b : c.bags) {
						if (!b.isFull()) {
							b.getInventory().addItem(is);
							c.equipment.setGloves(null);
							inv.setItem(27, new ItemStackBuilder()
							.withMaterial(Material.GOLDEN_CARROT)
							.withDisplayName("§f"+TranslatedString.getString(30018, p))
							.build());
							return;
						}
					}
					p.sendMessage("§cInventory full.");
					return;
				}
			}
			break;
		case 28:
			if (inv.getItem(slot).getType() != Material.BLAZE_POWDER) {
				if (ItemUtils.isMMOItem(ItemUtils.asNMS(inv.getItem(slot)))) {
					ItemStack is = inv.getItem(slot);
					for (Bag b : c.bags) {
						if (!b.isFull()) {
							b.getInventory().addItem(is);
							c.equipment.setSecondRing(null);
							inv.setItem(28, new ItemStackBuilder()
							.withMaterial(Material.BLAZE_POWDER)
							.withDisplayName("§f"+TranslatedString.getString(30005, p))
							.build());
							return;
						}
					}
					p.sendMessage("§cInventory full.");
					return;
				}
			}
			break;
		case 36:
			if (inv.getItem(slot).getType() != Material.GHAST_TEAR) {
				if (ItemUtils.isMMOItem(ItemUtils.asNMS(inv.getItem(slot)))) {
					ItemStack is = inv.getItem(slot);
					for (Bag b : c.bags) {
						if (!b.isFull()) {
							b.getInventory().addItem(is);
							c.equipment.setLeggings(null);
							inv.setItem(36, new ItemStackBuilder()
							.withMaterial(Material.GHAST_TEAR)
							.withDisplayName("§f"+TranslatedString.getString(30002, p))
							.build());
							return;
						}
					}
					p.sendMessage("§cInventory full.");
					return;
				}
			}
			break;
		case 45:
			if (inv.getItem(slot).getType() != Material.NETHER_STALK) {
				if (ItemUtils.isMMOItem(ItemUtils.asNMS(inv.getItem(slot)))) {
					ItemStack is = inv.getItem(slot);
					for (Bag b : c.bags) {
						if (!b.isFull()) {
							b.getInventory().addItem(is);
							c.equipment.setBoots(null);
							inv.setItem(45, new ItemStackBuilder()
							.withMaterial(Material.NETHER_STALK)
							.withDisplayName("§f"+TranslatedString.getString(30003, p))
							.build());
							return;
						}
					}
					p.sendMessage("§cInventory full.");
					return;
				}
			}
			break;
		case 59:
			if (inv.getItem(slot).getType() == Material.LEATHER) {
				c.bags.get(0).display(inv);
			}
			break;
		case 60:
			if (inv.getItem(slot).getType() == Material.LEATHER) {
				c.bags.get(1).display(inv);
			}
			break;
		case 61:
			if (inv.getItem(slot).getType() == Material.LEATHER) {
				c.bags.get(2).display(inv);
			}
			break;
		case 62:
			if (inv.getItem(slot).getType() == Material.LEATHER) {
				c.bags.get(3).display(inv);
			}
			break;
		default:
			break;
		}
	}

	@EventHandler
	public void pickup(PlayerPickupItemEvent evt) {
		evt.setCancelled(true);
		if (!ItemUtils.isMMOItem(evt.getItem().getItemStack())) {
			evt.getItem().remove();
			return;
		} else if (ItemUtils.isCoinItem(new Item(evt.getItem().getItemStack()))) {
			Character c = Main.connectedCharacters.get(evt.getPlayer());
			Item item = new Item(evt.getItem().getItemStack());
			if (item.getId() == 997) {
				c.money += item.getAmount();
			} else if (item.getId() == 998) {
				c.money += item.getAmount()*100;
			} else if (item.getId() == 999) {
				c.money += item.getAmount()*10000;
			}
			evt.getItem().remove();
		} else {
			Character c = Main.connectedCharacters.get(evt.getPlayer());
			for (Bag b : c.bags) {
				if (!b.isFull()) {
					b.getInventory().addItem(evt.getItem().getItemStack());
					evt.getItem().remove();
					break;
				}
			}
		}
	}


	// ITEM EFFECTS LAUNCH

	@EventHandler
	public void clickableItems(InventoryClickEvent evt) {
		if (evt.getCurrentItem() == null) return;
		if (!(evt.getWhoClicked() instanceof Player)) return;
		if (evt.isRightClick()) return;
		try {
			if (ItemUtils.isMMOItem(evt.getCurrentItem())) {
				Item item = new Item(evt.getCurrentItem());
				if (ItemUtils.isClickableItem(item)) {
					((IClickableItem)ItemRegistry.getItem(item.getId())).onClick(((Player)evt.getWhoClicked()), evt.getRawSlot());
					evt.setCancelled(true);
				}
			}
		} catch (NullPointerException npe) { return; }
	}

	@EventHandler
	public void usableItems(PlayerInteractEvent evt) {
		if (evt.getItem() == null) return;
		if (evt.getAction() != Action.RIGHT_CLICK_AIR && evt.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (ItemUtils.isMMOItem(evt.getItem())) {
			Item item = new Item(evt.getItem());
			if (ItemUtils.isUsableItem(item)) {
				((IUsableItem)ItemRegistry.getItem(item.getId())).onUse(evt.getPlayer());
				evt.setCancelled(true);
			}
		}
	}
	
	
	
	// ENTITY EFFECTS LAUNCH
	
	@EventHandler
	public void onEntityClick(PlayerInteractEntityEvent e){
		Player player = e.getPlayer();
		Entity entity = ((CraftEntity)e.getRightClicked()).getHandle();
		if(entity instanceof IClickable){
			QuestNpc npc = (QuestNpc)entity;
			Character ch = Main.connectedCharacters.get(player);
			npc.onInteract(ch, (net.minecraft.server.v1_9_R1.Item)null, 0.0D);
		}
	}
}
