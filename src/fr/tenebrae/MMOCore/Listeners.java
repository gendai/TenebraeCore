package fr.tenebrae.MMOCore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftZombie;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import fr.tenebrae.MMOCore.Entities.ICreature;
import fr.tenebrae.MMOCore.Quests.KillCounter;
import fr.tenebrae.MMOCore.Quests.Quest;
import fr.tenebrae.MMOCore.Quests.QuestCondition;
import fr.tenebrae.MMOCore.Quests.QuestCondition.ConditionType;
import fr.tenebrae.MMOCore.Quests.QuestObjective;
import fr.tenebrae.MMOCore.Quests.QuestObjective.ObjectiveType;
import fr.tenebrae.MMOCore.Quests.QuestReward;
import fr.tenebrae.MMOCore.Quests.QuestReward.RewardType;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.NBTTagList;
import net.minecraft.server.v1_9_R1.NBTTagString;

public class Listeners implements Listener {

	public Main plugin;
	private Quest q;
	private Quest q2;
	private KillCounter kc;
	

	public Listeners(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityDamgeByEntityEvent(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player && event.getEntity() instanceof CraftZombie){
			CraftZombie dead = (CraftZombie) event.getEntity();
			if(dead.getHealth() < 1){
				kc.add(1);
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.GRASS){
			e.setCancelled(true);
			ItemStack book = new ItemStack(Material.BOOK);
			ItemMeta meta = book.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA+"Journal de quêtes");
			book.setItemMeta(meta);
			
			ItemStack trash = new ItemStack(Material.CAULDRON_ITEM);
			ItemMeta metac = trash.getItemMeta();
			metac.setDisplayName(ChatColor.AQUA+"Trash");
			trash.setItemMeta(metac);

			Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Quests");
			inv.setItem(13,book);
			inv.setItem(0, trash);

			e.getPlayer().openInventory(inv);
		}else if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CAULDRON){
			QuestCondition condition = new QuestCondition(ConditionType.LEVEL, (int)10, e.getPlayer(), null, null);
			kc = new KillCounter();
			QuestObjective obj = new QuestObjective(ObjectiveType.KILL, net.minecraft.server.v1_9_R1.EntityZombie.class, (int)1, kc, null);
			QuestReward rew = new QuestReward(RewardType.XP, (int)30, e.getPlayer(), null, null);
			ArrayList<QuestCondition> conditionArr = new ArrayList<>();
			conditionArr.add(condition);
			ArrayList<QuestObjective> objArr = new ArrayList<>();
			objArr.add(obj);
			ArrayList<QuestReward> rewardArr = new ArrayList<>();
			rewardArr.add(rew);
			q = new Quest("Quêtes test", "1", 0, 0, objArr, conditionArr, rewardArr);
			
			condition = new QuestCondition(ConditionType.LEVEL, (int)5, e.getPlayer(), null, null);
			obj = new QuestObjective(ObjectiveType.DISCOVER, "Test Land", null, null, null);
			rew = new QuestReward(RewardType.MONEY, (int)20, null, null, null);
			ArrayList<QuestCondition> conditionArr2 = new ArrayList<>();
			conditionArr2.add(condition);
			ArrayList<QuestObjective> objArr2 = new ArrayList<>();
			objArr2.add(obj);
			ArrayList<QuestReward> rewardArr2 = new ArrayList<>();
			rewardArr2.add(rew);
			q2 = new Quest("Quête secondaire", "1", 1, 1, objArr2, conditionArr2, rewardArr2);
		}else if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.BARRIER){
			if(q.isDone()){
				e.getPlayer().sendMessage(ChatColor.RED+"You have completed the quest");
			}else{
				e.getPlayer().sendMessage(ChatColor.BLACK+"Quest not completed "+kc.getCount());
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(InventoryClickEvent e){
		if(e.getInventory().getTitle().equals(ChatColor.GOLD + "Quests")){

			if(e.getSlot() == 13){
				e.setCancelled(true);
				if(q == null){
					e.getWhoClicked().sendMessage(ChatColor.DARK_AQUA+"You have no quest");
				}else{
					/*ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
					net.minecraft.server.v1_9_R1.ItemStack nmsis = CraftItemStack.asNMSCopy(writtenBook);
					List<String> pages = new ArrayList<String>();
					pages.add("Conditions: ");
					pages.add("Objectives: Kill 1 zombie");
					pages.add("Rewards: 50 XP");
					NBTTagCompound bd = new NBTTagCompound();
					bd.setString("title", q.getDescription());
					NBTTagList bp = new NBTTagList();
					for(String text : pages) {
						bp.add(new NBTTagString(text));
					}
					bd.set("pages", bp);
					nmsis.setTag(bd);
					writtenBook = CraftItemStack.asBukkitCopy(nmsis);*/
					Inventory invBooksQuest = Bukkit.createInventory(null, 9*5, ChatColor.GOLD + "Quests List");
					invBooksQuest.addItem(q.getWrittenBook());
					invBooksQuest.addItem(q2.getWrittenBook());
					e.getWhoClicked().openInventory(invBooksQuest);
				}
			}else if(e.getSlot() == 0){
				e.setCancelled(true);
				CraftPlayer cp = (CraftPlayer)e.getWhoClicked();
				cp.getInventory().removeItem(cp.getInventory().getItemInHand());
			}else{
				return;
			}
		}else if(e.getInventory().getTitle().equals(ChatColor.GOLD+"Quests List")){
			e.setCancelled(true);
			String chatQuest[] = new String[6];
			BookMeta qmeta = (BookMeta)e.getCurrentItem().getItemMeta();
			chatQuest[0] = ChatColor.BLACK+"###################";
			chatQuest[1] = ChatColor.LIGHT_PURPLE+"Quête: "+qmeta.getTitle();
			chatQuest[2] = ChatColor.LIGHT_PURPLE+qmeta.getPage(1);
			chatQuest[3] = ChatColor.LIGHT_PURPLE+qmeta.getPage(2);
			chatQuest[4] = ChatColor.LIGHT_PURPLE+qmeta.getPage(3);
			chatQuest[5] = ChatColor.BLACK+"###################";
			e.getWhoClicked().sendMessage(chatQuest);
		}
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
