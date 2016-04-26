package fr.tenebrae.MMOCore;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.tenebrae.MMOCore.Characters.Character;
import fr.tenebrae.MMOCore.Entities.IClickable;
import fr.tenebrae.MMOCore.Entities.ICreature;
import fr.tenebrae.MMOCore.Entities.QuestNpc;
import fr.tenebrae.MMOCore.Quests.DiscoverCoord;
import fr.tenebrae.MMOCore.Quests.KillCounter;
import fr.tenebrae.MMOCore.Quests.Quest;
import fr.tenebrae.MMOCore.Quests.QuestCondition;
import fr.tenebrae.MMOCore.Quests.QuestCondition.ConditionType;
import fr.tenebrae.MMOCore.Quests.QuestObjective;
import fr.tenebrae.MMOCore.Quests.QuestObjective.ObjectiveType;
import fr.tenebrae.MMOCore.Quests.QuestReward;
import fr.tenebrae.MMOCore.Quests.QuestReward.RewardType;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.Tuple;

public class Listeners implements Listener {

	public Main plugin;
	private Quest q;
	private Quest q2;
	public static ArrayList<Quest> quests = new ArrayList<>();


	public Listeners(Main plugin) {
		this.plugin = plugin;
	}

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
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && (e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN)){
			e.setCancelled(true);
			ItemStack book = new ItemStack(Material.BOOK);
			ItemMeta meta = book.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA+"Journal de quêtes");
			book.setItemMeta(meta);

			Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Quests");
			inv.setItem(13,book);

			e.getPlayer().openInventory(inv);
		}else if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CAULDRON){
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
			q = new Quest("Quêtes test","Ceci est une description pour la quête principale", "1", 0, 0, objArr, conditionArr, rewardArr);

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
			q2 = new Quest("Quêtes Secondaire","Ceci est une description pour la quête secondaire", "1", 1, 1, objArr2, conditionArr2, rewardArr2);
			QuestNpc npc = new QuestNpc(e.getPlayer().getLocation());
			npc.spawn();
			npc.addQuestNpc(q);
			npc.addQuestNpc(q2);
			Bukkit.getServer().getPluginManager().registerEvents(npc, this.plugin);
		}
	}

	@EventHandler
	public void onEntityClick(PlayerInteractEntityEvent e){
		Player player = e.getPlayer();
		Entity entity = ((CraftEntity)e.getRightClicked()).getHandle();
		if(entity instanceof IClickable){
			QuestNpc npc = (QuestNpc)entity;
			Character ch = new Character(player.getDisplayName());
			ch.setAccount(player);
			npc.onInteract(ch, (net.minecraft.server.v1_9_R1.Item)null, 0.0D);
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
	public void onDamage(EntityDamageByEntityEvent evt) {
		Entity nmsEntity = ((CraftEntity)evt.getEntity()).getHandle();
		if (nmsEntity instanceof ICreature) {
			((ICreature)nmsEntity).damage(((CraftEntity)evt.getDamager()).getHandle(), (int) evt.getOriginalDamage(DamageModifier.BASE), 1);
			evt.setCancelled(true);
		}
	}

	@EventHandler
	public void onTarget(EntityTargetEvent evt) {
		Entity nmsEntity = ((CraftEntity)evt.getEntity()).getHandle();
		if (nmsEntity instanceof ICreature) evt.setCancelled(true);
	}
}
