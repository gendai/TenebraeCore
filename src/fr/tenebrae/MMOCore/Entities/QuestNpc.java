package fr.tenebrae.MMOCore.Entities;

import java.util.Date;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftVillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.Quests.Quest;
import fr.tenebrae.MMOCore.Quests.QuestCondition;
import fr.tenebrae.MMOCore.Quests.QuestObjective;
import fr.tenebrae.MMOCore.Quests.QuestReward;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EntityVillager;
import net.minecraft.server.v1_9_R1.EnumHand;
import net.minecraft.server.v1_9_R1.World;
import fr.tenebrae.MMOCore.Listeners;
import fr.tenebrae.MMOCore.Characters.Character;

public class QuestNpc extends EntityVillager implements ICreature, IQuester, IClickable, Listener{

	public double health = 16;
	public double maxHealth = 16;
	public double attackSpeed = 1000;
	public Date lastAttackDate = null;
	public int atkMin = 1;
	public int atkMax = 3;
	public int level = 1;
	public Entity target = null;
	public Entity lastDamager = null;
	public int givenXp = 4;
	public double attackRange = 1;
	public String name = "Quest NPC";
	public Location spawn;
	public boolean resetting = false;
	public boolean isDead = false;

	public double walkSpeed = 0D;
	public double sprintSpeed = 0D;
	public double resetSpeed = 0D;

	public String hurtSound = "mob.villager.hurt";
	public String idleSound = "mob.villager.idle";
	public String stepSound = "mob.villager.step";
	public String deathSound = "mob.villager.death";

	@Override
	public Inventory openFinishedQuestGui(){
		Inventory inv = Bukkit.createInventory(null, 9*5, ChatColor.GOLD + "Fnished Quests");
		for(Quest q : quests){
			if(q.getFinished()){
				inv.addItem(q.getWrittenBook());
			}
		}
		ItemStack panback = new ItemStack(Material.SIGN);
		ItemMeta metaback = panback.getItemMeta();
		metaback.setDisplayName(ChatColor.AQUA+"Previous");
		panback.setItemMeta(metaback);

		ItemStack panforw = new ItemStack(Material.SIGN);
		ItemMeta metaforw = panforw.getItemMeta();
		metaforw.setDisplayName(ChatColor.AQUA+"Next");
		panforw.setItemMeta(metaforw);

		inv.setItem(36, panback);
		inv.setItem(44, panforw);
		return inv;
	}

	@Override
	public Inventory openPendingQuestGui(){
		Inventory inv = Bukkit.createInventory(null, 9*5, ChatColor.GOLD + "Pending Quests");
		for(Quest q : Listeners.quests/*Will be player.quests*/){
			if(!q.getFinished()){
				inv.addItem(q.getWrittenBook());
			}
		}
		ItemStack panback = new ItemStack(Material.SIGN);
		ItemMeta metaback = panback.getItemMeta();
		metaback.setDisplayName(ChatColor.AQUA+"Previous");
		panback.setItemMeta(metaback);

		ItemStack panforw = new ItemStack(Material.SIGN);
		ItemMeta metaforw = panforw.getItemMeta();
		metaforw.setDisplayName(ChatColor.AQUA+"Next");
		panforw.setItemMeta(metaforw);

		inv.setItem(36, panback);
		inv.setItem(44, panforw);
		return inv;
	}

	@Override
	public Inventory openAvailableQuestGui(Player player){
		Inventory inv = Bukkit.createInventory(null, 9*5, ChatColor.GOLD + "Available Quests");
		for(Quest q : quests){
			if(q.canHaveQuest(player) && !Listeners.quests.contains(q)){
				inv.addItem(q.getWrittenBook());
			}
		}
		ItemStack panback = new ItemStack(Material.SIGN);
		ItemMeta metaback = panback.getItemMeta();
		metaback.setDisplayName(ChatColor.AQUA+"Previous");
		panback.setItemMeta(metaback);

		ItemStack panforw = new ItemStack(Material.SIGN);
		ItemMeta metaforw = panforw.getItemMeta();
		metaforw.setDisplayName(ChatColor.AQUA+"Next");
		panforw.setItemMeta(metaforw);

		inv.setItem(36, panback);
		inv.setItem(44, panforw);
		return inv;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e){
		if(e.getInventory().getTitle().equals(ChatColor.GOLD + "Available Quests")){
			e.setCancelled(true);
			if(e.getSlot() == 36){
				e.getWhoClicked().openInventory(openFinishedQuestGui());
			}else if(e.getSlot() == 44){
				e.getWhoClicked().openInventory(openPendingQuestGui());
			}else if(e.getCurrentItem().getType().equals(Material.BOOK)){
				ItemMeta qmeta = e.getCurrentItem().getItemMeta();
				if(e.getClick().equals(ClickType.RIGHT)){
				}else if(e.getClick().equals(ClickType.LEFT)){
					e.getWhoClicked().sendMessage("NPC say: "+questFromBookMeta(qmeta).getDescription());
					Quest quest = questFromBookMeta(qmeta);
					giveQuest(quest, (Player)e.getWhoClicked());
					e.getInventory().setItem(e.getSlot(), null);
				}
			}
		}else if(e.getInventory().getTitle().equals(ChatColor.GOLD + "Pending Quests")){
			e.setCancelled(true);
			if(e.getSlot() == 36){
				e.getWhoClicked().openInventory(openAvailableQuestGui((Player)e.getWhoClicked()));
			}else if(e.getSlot() == 44){
				e.getWhoClicked().openInventory(openFinishedQuestGui());
			}else if(e.getCurrentItem().getType().equals(Material.BOOK)){
				ItemMeta qmeta = e.getCurrentItem().getItemMeta();
				if(e.getClick().equals(ClickType.RIGHT)){
				}else if(e.getClick().equals(ClickType.LEFT)){
					Quest quest = questFromBookMeta(qmeta);
					if(quest.isDone((Player)e.getWhoClicked())){
						e.getWhoClicked().sendMessage(ChatColor.GREEN+"You have completed the Quest: "+quest.getTitle());
						e.getInventory().setItem(e.getSlot(), null);
					}else{
						e.getWhoClicked().sendMessage(ChatColor.RED+"You have not finish this quest yet");
					}
				}
			}
		}else if(e.getInventory().getTitle().equals(ChatColor.GOLD + "Fnished Quests")){
			e.setCancelled(true);
			if(e.getSlot() == 36){
				e.getWhoClicked().openInventory(openPendingQuestGui());
			}else if(e.getSlot() == 44){
				e.getWhoClicked().openInventory(openAvailableQuestGui((Player)e.getWhoClicked()));
			}
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

	@Override
	public void onInteract(Character clicker, net.minecraft.server.v1_9_R1.Item nomItem, double distance) {
		Player player = clicker.getAccount();
		Inventory invquest = openAvailableQuestGui(player);
		player.openInventory(invquest);
	}

	@Override
	public boolean hasAlreadyQuest(Quest quest, Player player){
		if(Listeners.quests.contains(quest)){
			return true;
		}
		return false;
	}

	@Override
	public void addQuestNpc(Quest quest){
		quests.add(quest);
	}

	@Override
	public boolean isQuestAviable(Quest quest, Player player) {
		for(QuestCondition qc : quest.getConditions()){
			if(!qc.isAuthorize(player)){
				return false;
			}
		}
		return true;
	}

	@Override
	public void giveQuest(Quest quest, Player player) {
		//Todo add quest to player quest list.
		Listeners.quests.add(quest);
	}

	@Override
	public boolean hasFinishedQuest(Quest quest, Player player) {
		for(QuestObjective obj : quest.getObjectives()){
			if(!obj.isCompleted()){
				return false;
			}
		}
		return true;
	}

	@Override
	public void giveReward(Quest quest, Player player) {
		for(QuestReward qr : quest.getRewards()){
			qr.giveReward(player);
		}
	}

	public QuestNpc(World world){
		super(world);
	}

	public QuestNpc(org.bukkit.World world) {
		super(((CraftWorld)world).getHandle());
	}

	public QuestNpc(Location loc) {
		super(((CraftWorld)loc.getWorld()).getHandle());
		this.spawn = loc;
	}

	protected void initAttributes(){
		super.initAttributes();
	}

	@Override
	public QuestNpc spawn() {
		try {
			this.setPosition(spawn.getX(), spawn.getY(), spawn.getZ());
			(((CraftWorld) spawn.getWorld()).getHandle()).addEntity(this, SpawnReason.CUSTOM);
			this.setup();
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean a(EntityHuman entity, EnumHand enumhand, net.minecraft.server.v1_9_R1.ItemStack itemstack)
	{
		return false;
	}

	@Override
	public void move(double d0, double d1, double d2){

	}

	@Override
	public void g(double d0, double d1, double d2){

	}

	@Override
	public void setup(){
	}

	@Override
	public Location getLocation() {
		return this.getBEntity().getLocation();
	}

	@Override
	public Location getSpawn() {
		return this.spawn;
	}

	@Override
	public Villager getBEntity() {return ((Villager)CraftVillager.getEntity(this.getWorld().getServer(), this));}

	@Override
	public void onDeath() {
	}

	@Override
	public boolean isAttackReady() {
		return false;
	}

	@Override
	public void setTarget(Entity target) {
	}

	@Override
	public void lookAt(Entity target) {
	}

	@Override
	public void lookAt(Location target) {
	}

	@Override
	public void moveTo(Location loc) {
	}

	@Override
	public void moveTo(Location loc, double speed) {
	}

	@Override
	public void moveTo(double x, double y, double z) {
	}

	@Override
	public void moveTo(double x, double y, double z, double speed) {
	}

	@Override
	public Map<Item, Integer> getDrops() {
		return null;
	}

	@Override
	public void setDrops(Map<Item, Integer> drops) {
	}

	@Override
	public void addDrop(Item drop, int percent) {
	}

	@Override
	public void removeDrop(Item drop) {
	}

	@Override
	public boolean hasDrop(Item item) {
		return false;
	}

	@Override
	public boolean isMoving() {
		return false;
	}

	@Override
	public boolean damage(Entity damager, int amount, double aggroMultiplier) {
		return false;
	}

	@Override
	public BossBar getHealthBar() {
		return null;
	}

	@Override
	public void reset() {
	}

	@Override
	public boolean isResetting() {
		return false;
	}

	@Override
	public void setResetting(boolean resetting) {
	}

	@Override
	public void setPlayMusic(boolean playMusic) {
	}

	@Override
	public boolean doesPlayMusic() {
		return false;
	}

	@Override
	public void setDarkenSky(boolean darkenSky) {
	}

	@Override
	public boolean doesDarkenSky() {
		return false;
	}

	@Override
	public void setCreateFog(boolean createFog) {
	}

	@Override
	public boolean doesCreateFog() {
		return false;
	}

}