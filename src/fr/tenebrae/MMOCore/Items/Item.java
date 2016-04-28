package fr.tenebrae.MMOCore.Items;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.server.v1_9_R1.MinecraftKey;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.NBTTagList;
import net.minecraft.server.v1_9_R1.NBTTagString;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.SQLResultSet;
import fr.tenebrae.MMOCore.Characters.Character;
import fr.tenebrae.MMOCore.Items.Components.GemSlot;
import fr.tenebrae.MMOCore.Items.Components.GemType;
import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Items.Components.TriggerType;
import fr.tenebrae.MMOCore.Items.Components.WeaponPlace;
import fr.tenebrae.MMOCore.Items.Components.WeaponType;
import fr.tenebrae.MMOCore.Mechanics.Spells;
import fr.tenebrae.MMOCore.Mechanics.Stats;
import fr.tenebrae.MMOCore.Utils.SQLHelper;
import fr.tenebrae.MMOCore.Utils.Serializers.ItemStackSerializer;

public class Item {

	private NBTTagCompound nbt, mmoNBT;

	private int id = 0;

	private Material material = null;
	private int damageData = 0;
	private ItemType type = null;
	private ItemQuality quality = null;
	private Map<Stats, Double> stats = new HashMap<Stats, Double>();
	private int itemLevel = 0;
	private int levelRequired = 0;
	private List<GemSlot> gemSlots = new ArrayList<GemSlot>();
	private int nameId = 0;
	private int loreId = 0;

	private String locale = "english";

	private int minDmg = 0;
	private int maxDmg = 0;

	private int sellPrice = 0;

	private int amount = 1;
	private int durability = 0;
	private int maxDurability = 0;

	private int slots = 0;
	private Map<TriggerType, Spells> linkedSpells = new HashMap<TriggerType, Spells>();
	private List<Spells> learnSpells = new ArrayList<Spells>();

	private WeaponPlace weaponPlace = null;
	private WeaponType weaponType = null;

	private net.minecraft.server.v1_9_R1.ItemStack itemStack = null;


	public Item() {
		this.itemStack = new net.minecraft.server.v1_9_R1.ItemStack(net.minecraft.server.v1_9_R1.Item.REGISTRY.get(new MinecraftKey("minecraft:stone")));
		this.setAmount(1);
		this.setId(0);
		this.setItemLevel(34);
		this.setMaxDurability(44);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(1);
		this.setType(ItemType.HELMET);
		this.setQuality(ItemQuality.ARTEFACT);
		this.setNameId(1);
		this.setLoreId(20000);
		this.setMaterial(Material.DIAMOND_HELMET);
		this.setDamageData(0);
		this.setSellPrice(1);
	}

	public Item(NBTTagCompound nbt) {
		setInfosFrom(nbt);

		createItemStack();
	}

	public Item(ItemStack is) {
		this.itemStack = ItemUtils.asNMS(is);
		NBTTagCompound nbt = new NBTTagCompound();
		this.itemStack.save(nbt);
		setInfosFrom(nbt);
	}

	public Item(ItemStack is, boolean emergency) {
		if (!emergency) {
			this.itemStack = ItemUtils.asNMS(is);
			NBTTagCompound nbt = new NBTTagCompound();
			this.itemStack.save(nbt);
			setInfosFrom(nbt);
		} else {
			CraftItemStack cIS = CraftItemStack.asCraftCopy(is);
			cIS.setItemMeta(CraftItemStack.getItemMeta(CraftItemStack.asNMSCopy(is)));
			this.itemStack = ItemUtils.asNMS(cIS);
			NBTTagCompound nbt = new NBTTagCompound();
			this.itemStack.save(nbt);
			setInfosFrom(nbt);
		}
	}

	public NBTTagCompound setInfosOn(NBTTagCompound nbt) {
		ItemStack returned = new ItemStack(material);
		returned.setAmount(amount);
		returned.setDurability((short) damageData);
		CraftItemStack.asNMSCopy(returned).save(nbt);
		NBTTagCompound mmoNBT = new NBTTagCompound();
		mmoNBT.setInt("itemLevel", this.getItemLevel());
		mmoNBT.setInt("levelRequired", this.getLevelRequired());
		mmoNBT.setString("itemType", this.getType().toString());
		mmoNBT.setString("itemQuality", this.getQuality().toString());
		mmoNBT.setString("locale", this.getLocale());
		mmoNBT.setInt("id", this.getId());
		mmoNBT.setInt("nameId", this.getNameId());
		mmoNBT.setInt("loreId", this.getLoreId());
		mmoNBT.setInt("durability", this.getDurability());
		mmoNBT.setInt("maxDurability", this.getMaxDurability());
		mmoNBT.setInt("minDmg", this.getMinDmg());
		mmoNBT.setInt("maxDmg", this.getMaxDmg());
		if (weaponPlace != null) mmoNBT.setString("weaponPlace", weaponPlace.toString());
		if (weaponType != null) mmoNBT.setString("weaponType", weaponType.toString());
		if (sellPrice > 0) mmoNBT.setInt("sellPrice", this.getSellPrice());
		if (slots > 0) mmoNBT.setInt("slots", this.getSlots());

		if (!stats.isEmpty()) {
			NBTTagList statsList = new NBTTagList();
			for (Entry<Stats, Double> entry : stats.entrySet()) {
				NBTTagCompound cmpnd = new NBTTagCompound();
				cmpnd.setString("stat", entry.getKey().toString());
				cmpnd.setDouble("amount", entry.getValue());
				statsList.add(cmpnd);
			}
			mmoNBT.set("stats", statsList);
		}
		if (!gemSlots.isEmpty()) {
			NBTTagList gemSlotsList = new NBTTagList();
			for (GemSlot gemSlot : gemSlots) {
				NBTTagCompound cmpnd = new NBTTagCompound();
				cmpnd.setString("gemType", gemSlot.getType().toString());
				cmpnd.setBoolean("isUsed", gemSlot.isUsed());
				if (gemSlot.getStat() != null) cmpnd.setString("givenStat", gemSlot.getStat().toString());
				gemSlotsList.add(cmpnd);
			}
			mmoNBT.set("gemSlots", gemSlotsList);
		}
		if (!learnSpells.isEmpty()) {
			NBTTagList spellsList = new NBTTagList();
			for (Spells spell : learnSpells) spellsList.add(new NBTTagString(spell.toString()));
			mmoNBT.set("learnSpells", spellsList);
		}
		if (!linkedSpells.isEmpty()) {
			NBTTagList linkedSpellsList = new NBTTagList();
			for (Entry<TriggerType, Spells> entry : linkedSpells.entrySet()) {
				NBTTagCompound cmpnd = new NBTTagCompound();
				cmpnd.setString("triggerType", entry.getKey().toString());
				cmpnd.setString("spell", entry.getValue().toString());
				linkedSpellsList.add(cmpnd);
			}
			mmoNBT.set("linkedSpells", linkedSpellsList);
		}
		NBTTagCompound tag = nbt.getCompound("tag");
		tag.set("MMOTags", mmoNBT);
		nbt.set("tag", tag);
		return nbt;
	}

	public Item setInfosFrom(NBTTagCompound nbt) {
		stats.clear();
		gemSlots.clear();
		learnSpells.clear();
		linkedSpells.clear();
		if (nbt.hasKey("id") && nbt.hasKey("Count") && nbt.hasKey("Damage")) {
			net.minecraft.server.v1_9_R1.ItemStack nmsHolder = new net.minecraft.server.v1_9_R1.ItemStack(((net.minecraft.server.v1_9_R1.Item)net.minecraft.server.v1_9_R1.Item.REGISTRY.get(new MinecraftKey(nbt.getString("id")))));
			nmsHolder.c(nbt);
			ItemStack holder = CraftItemStack.asBukkitCopy(nmsHolder);
			this.setMaterial(holder.getType());
			this.setAmount(holder.getAmount());
			this.setDamageData(holder.getDurability());
		}
		if (!nbt.hasKey("tag")) return this;
		NBTTagCompound tag = nbt.getCompound("tag");
		if (tag.hasKey("MMOTags")) {
			NBTTagCompound mmoNBT = tag.getCompound("MMOTags");
			this.setItemLevel(mmoNBT.getInt("itemLevel"));
			this.setLevelRequired(mmoNBT.getInt("levelRequired"));
			this.setType(ItemType.valueOf(mmoNBT.getString("itemType")));
			this.setQuality(ItemQuality.valueOf(mmoNBT.getString("itemQuality")));
			this.setLocale(mmoNBT.getString("locale"));
			this.setId(mmoNBT.getInt("id"));
			this.setNameId(mmoNBT.getInt("nameId"));
			this.setLoreId(mmoNBT.getInt("loreId"));
			this.setDurability(mmoNBT.getInt("durability"));
			this.setMaxDurability(mmoNBT.getInt("maxDurability"));
			this.setMinDmg(mmoNBT.getInt("minDmg"));
			this.setMaxDmg(mmoNBT.getInt("maxDmg"));
			if (mmoNBT.hasKey("weaponPlace")) this.setWeaponPlace(WeaponPlace.valueOf(mmoNBT.getString("weaponPlace")));
			else this.weaponPlace = null;
			if (mmoNBT.hasKey("weaponType")) this.setWeaponType(WeaponType.valueOf(mmoNBT.getString("weaponType")));
			else this.weaponType = null;
			if (mmoNBT.hasKey("stats")) {
				NBTTagList list = mmoNBT.getList("stats", 10);
				int i = 0;
				while (i < list.size()) {
					NBTTagCompound cmpnd = list.get(i);
					stats.put(Stats.valueOf(cmpnd.getString("stat")), cmpnd.getDouble("amount"));
					i++;
				}
			}
			if (mmoNBT.hasKey("learnSpells")) {
				NBTTagList list = mmoNBT.getList("learnSpells", 8);
				int i = 0;
				while (i < list.size()) {
					learnSpells.add(Spells.valueOf(list.getString(i)));
					i++;
				}
			}
			if (mmoNBT.hasKey("gemSlots")) {
				NBTTagList list = mmoNBT.getList("gemSlots", 10);
				int i = 0;
				while (i < list.size()) {
					NBTTagCompound gemSlotCompound = list.get(i);
					if (gemSlotCompound.hasKey("givenStat")) gemSlots.add(new GemSlot(GemType.valueOf(gemSlotCompound.getString("gemType")), gemSlotCompound.getBoolean("isUsed"), Stats.valueOf(gemSlotCompound.getString("givenStat"))));
					else gemSlots.add(new GemSlot(GemType.valueOf(gemSlotCompound.getString("gemType")), gemSlotCompound.getBoolean("isUsed")));
					i++;
				}
			}
			if (mmoNBT.hasKey("linkedSpells")) {
				NBTTagList list = mmoNBT.getList("linkedSpells", 10);
				int i = 0;
				while (i < list.size()) {
					NBTTagCompound lnkdSpellsNBT = list.get(i);
					linkedSpells.put(TriggerType.valueOf(lnkdSpellsNBT.getString("triggerType")), Spells.valueOf(lnkdSpellsNBT.getString("spell")));
					i++;
				}
			}
			if (mmoNBT.hasKey("sellPrice")) this.setSellPrice(mmoNBT.getInt("sellPrice"));
			else this.setSellPrice(0);
			if (mmoNBT.hasKey("slots")) this.setSlots(mmoNBT.getInt("slots"));
			else this.setSlots(0);
		}
		return this;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public ItemQuality getQuality() {
		return quality;
	}

	public void setQuality(ItemQuality quality) {
		this.quality = quality;
	}

	public Map<Stats, Double> getStats() {
		return stats;
	}

	public void setStats(Map<Stats, Double> stats) {
		this.stats = stats;
	}

	public int getItemLevel() {
		return itemLevel;
	}

	public void setItemLevel(int itemLevel) {
		this.itemLevel = itemLevel;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public void setLevelRequired(int levelRequired) {
		this.levelRequired = levelRequired;
	}

	public List<GemSlot> getGemSlots() {
		return gemSlots;
	}

	public void setGemSlots(List<GemSlot> gemSlots) {
		this.gemSlots = gemSlots;
	}

	public int getNameId() {
		return nameId;
	}

	public String getName(String language) {
		String returned = "null";
		SQLResultSet sqlRS = null;
		try {
			sqlRS = SQLHelper.getSortedEntrys(Main.DB_DATABASE, Main.DB_STRING_TEMPLATE, "entry", nameId);
			ResultSet nameRow = sqlRS.getResultSet();
			if (!nameRow.next()) throw new SQLException("String template did not contained the requested entry ("+nameId+")");
			if (nameRow.isAfterLast()) throw new SQLException("String template did not contained the requested entry ("+nameId+")");
			returned = nameRow.getString(language);
			sqlRS.close();
		} catch (Exception e) {
			try {
				if (sqlRS != null) sqlRS.close();
			} catch (SQLException s) {}
			e.printStackTrace();
		}
		if (returned == null) returned = "null";
		return returned;
	}

	public List<String> getLore(String language) {
		List<String> returned = new ArrayList<String>();

		SQLResultSet sqlRS = null;
		try {
			sqlRS = SQLHelper.getSortedEntrys(Main.DB_DATABASE, Main.DB_STRING_TEMPLATE, "entry", loreId);
			ResultSet loreRow = sqlRS.getResultSet();
			if (!loreRow.next()) throw new SQLException("String template did not contained the requested entry ("+loreId+")");
			if (loreRow.isAfterLast()) throw new SQLException("String template did not contained the requested entry ("+loreId+")");
			String value = loreRow.getString(language);
			while (value.length() > 32) {
				returned.add(value.substring(0, 32));
				value = "§8"+value.substring(32);
			}
			returned.add(value);
			sqlRS.close();
		} catch (Exception e) { 
			try {
				if (sqlRS != null) sqlRS.close();
			} catch (SQLException s) {}
			e.printStackTrace();
		}

		return returned;
	}

	public void setNameId(int nameId) {
		this.nameId = nameId;
	}

	public int getLoreId() {
		return loreId;
	}

	public void setLoreId(int loreId) {
		this.loreId = loreId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public int getSlots() {
		return slots;
	}

	public void setSlots(int slots) {
		this.slots = slots;
	}

	public Map<TriggerType, Spells> getLinkedSpells() {
		return linkedSpells;
	}

	public void setLinkedSpells(Map<TriggerType, Spells> linkedSpells) {
		this.linkedSpells = linkedSpells;
	}

	public void addLinkedSpell(TriggerType trigger, Spells spell) {
		this.linkedSpells.put(trigger, spell);
	}

	public List<Spells> getLearnSpells() {
		return learnSpells;
	}

	public void setLearnSpells(List<Spells> learnSpells) {
		this.learnSpells = learnSpells;
	}

	public NBTTagCompound getNBT() {
		return nbt;
	}

	public void setNBT(NBTTagCompound nbt) {
		this.nbt = nbt;
	}

	public NBTTagCompound getMmoNBT() {
		return mmoNBT;
	}

	public void setMmoNBT(NBTTagCompound mmoNBT) {
		this.mmoNBT = mmoNBT;
	}

	public int getDamageData() {
		return damageData;
	}

	public void setDamageData(int damageData) {
		this.damageData = damageData;
	}

	public int getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	public List<String> buildLore(String language) {
		List<String> returned = new ArrayList<String>();

		returned.add("§r");
		returned.add("§7"+(this.getWeaponType() != null ? this.getWeaponType().getString(language) : this.getType().getString(language)));
		if (this.getLevelRequired() > 0) returned.add("§7"+getLocaleString(40000, language)+"§6"+this.getLevelRequired());
		returned.add("§7"+getLocaleString(40001, language)+"§6"+this.getItemLevel());
		returned.add(this.getQuality().getColor()+"§l"+this.getQuality().getString(language));
		returned.add("§r");
		if (this.maxDmg > 0) {
			returned.add("§7"+getLocaleString(40004, language)+"§f"+this.minDmg+" - "+this.maxDmg);
		}
		if (!this.getStats().isEmpty()) {
			if (this.getStats().containsKey(Stats.ATTACK_SPEED)) {
				returned.add("§7"+Stats.ATTACK_SPEED.getString(language)+(this.getStats().get(Stats.ATTACK_SPEED) > 0 ? "§6" : "§c-")+(Double.valueOf(this.getStats().get(Stats.ATTACK_SPEED)))/1000);
			}
			if (this.getStats().containsKey(Stats.ARMOR)) {
				returned.add("§7"+Stats.ARMOR.getString(language)+(this.getStats().get(Stats.ARMOR) > 0 ? "§2+" : "§c-")+this.getStats().get(Stats.ARMOR));
			}
			for (Entry<Stats,Double> stat : this.getStats().entrySet()) {
				if (stat.getKey() != Stats.ARMOR && stat.getKey() != Stats.ATTACK_SPEED)
					returned.add("§7"+stat.getKey().getString(language)+(stat.getValue() > 0 ? "§2+" : "§c-")+stat.getValue());
			}
			returned.add("§r");
		}
		if (!this.getLinkedSpells().isEmpty()) {
			for (Entry<TriggerType,Spells> spell : this.getLinkedSpells().entrySet()) {
				returned.add("§2"+spell.getKey().getString(language));
				for (String s : spell.getValue().getDescription(language)) {
					returned.add("§7"+s);
				}
			}
			returned.add("§r");
		}
		if (this.getMaxDurability() > 0) returned.add("§3"+getLocaleString(40002, language)+"§a"+this.getDurability()+"§3/§f"+this.getMaxDurability());
		returned.add("§3"+getLocaleString(40003, language)+getFormattedPrice(this.getSellPrice()));
		if (this.loreId != 0) {
			returned.add("§r");
			returned.addAll(this.getLore(language));
		}
		return returned;
	}

	public String getFormattedPrice(int amount) {
		String returned = "§f";
		if (amount > 10000) {
			String price = String.valueOf(amount);
			returned += price.substring(0, price.length()-4)+"郂";
			returned += " "+price.substring(price.length()-4, price.length()-2)+"郁";
			returned += " "+price.substring(price.length()-2, price.length())+"郀";
		} else if (amount > 100) {
			String price = String.valueOf(amount);
			returned += price.substring(0, price.length()-2)+"郁";
			returned += " "+price.substring(price.length()-2, price.length())+"郀";
		} else {
			returned += String.valueOf(amount)+"郀";
		}
		return returned;
	}

	public String getLocaleString(int id, String language) {
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

	public void updateItemStack(Character c) {
		this.updateItemStack(c.getLanguage());
	}

	public void updateItemStack(final String language) {
		new BukkitRunnable() {
			@Override
			public void run() {
				itemStack.count = amount;
				itemStack.setData(damageData);
				ItemMeta meta = CraftItemStack.getItemMeta(itemStack);
				meta.setDisplayName(getQuality().getColor()+getName(language));
				meta.setLore(buildLore(language));
				meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
				CraftItemStack.setItemMeta(itemStack, meta);
				NBTTagCompound itemNBT = new NBTTagCompound();
				try {
					itemStack.save(itemNBT);
				} catch (ConcurrentModificationException e) {
					Main.log.warning("Error attempting to save vanilla informations on a NBTTag (Attempt 1). MMOItem ID: "+getId());
					try {
						itemStack.save(itemNBT);
					} catch (ConcurrentModificationException e1) {
						Main.log.warning("Error attempting to save vanilla informations on a NBTTag (Attempt 2). MMOItem ID: "+getId());
						try {
							itemStack.save(itemNBT);
						} catch (ConcurrentModificationException e2) {
							Main.log.warning("Error attempting to save vanilla informations on a NBTTag (Attempt 3). MMOItem ID: "+getId());
							Main.log.severe("Attention: an item could not set his informations on a NBTTag. MMOItem ID: "+getId());
						}
					}
				}
				setInfosOn(itemNBT);
				itemStack.c(itemNBT);
				int result = 0;
				if (getMaxDurability() > 0) {
					result = (getDurability() * itemStack.getItem().getMaxDurability())/getMaxDurability();
					result = itemStack.getItem().getMaxDurability() - result;
				}
				itemStack.setData(Math.round(result));
			}
		}.runTaskAsynchronously(Main.plugin);
	}

	protected void createItemStack() {
		ItemStack returned = new ItemStack(material);
		returned.setAmount(amount);
		returned.setDurability((short) damageData);
		ItemMeta meta = returned.getItemMeta();
		meta.setDisplayName(getQuality().getColor()+getName("english"));
		meta.setLore(buildLore("english"));
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		returned.setItemMeta(meta);
		net.minecraft.server.v1_9_R1.ItemStack nmsReturned = CraftItemStack.asNMSCopy(returned);
		NBTTagCompound itemNBT = new NBTTagCompound();
		nmsReturned.save(itemNBT);
		setInfosOn(itemNBT);
		itemStack.c(itemNBT);
		int result = 0;
		if (getMaxDurability() > 0) {
			result = (getDurability() * itemStack.getItem().getMaxDurability())/getMaxDurability();
			result = itemStack.getItem().getMaxDurability() - result;
		}
		itemStack.setData(Math.round(result));
	}

	public ItemStack getItemStack() {
		return CraftItemStack.asBukkitCopy(this.itemStack);
	}

	public net.minecraft.server.v1_9_R1.ItemStack getNMSItemStack() {
		return this.itemStack;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public WeaponPlace getWeaponPlace() {
		return weaponPlace;
	}

	public void setWeaponPlace(WeaponPlace weaponPlace) {
		this.weaponPlace = weaponPlace;
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(WeaponType weaponType) {
		this.weaponType = weaponType;
	}

	public int getMaxDurability() {
		return maxDurability;
	}

	public void setMaxDurability(int maxDurability) {
		this.maxDurability = maxDurability;
	}

	public void addStat(Stats stat, Double amount) {
		this.stats.put(stat, amount);
	}

	public void removeStat(Stats stat) {
		this.stats.remove(stat);
	}

	public int getMinDmg() {
		return minDmg;
	}

	public void setMinDmg(int minDmg) {
		this.minDmg = minDmg;
	}

	public int getMaxDmg() {
		return maxDmg;
	}

	public void setMaxDmg(int maxDmg) {
		this.maxDmg = maxDmg;
	}

	public boolean instanceOf(Class<? extends Item> clazz) {
		try {
			if (clazz.newInstance().getId() == this.getId()) return true;
			else return false;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String toString() {
		return ItemStackSerializer.toBase64(getItemStack());
	}

	public String getLocale() {
		return locale;
	}

	public Item setLocale(String locale) {
		if (this.locale.equals(locale)) return this;
		this.locale = locale;
		updateItemStack(locale);
		return this;
	}
}
