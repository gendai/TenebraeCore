package fr.tenebrae.MMOCore.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.tenebrae.MMOCore.Items.Archer.L01RecruitTunic;
import fr.tenebrae.MMOCore.Items.Archer.L01TrainingBow;
import fr.tenebrae.MMOCore.Items.Assassin.L01TrainingDagger;
import fr.tenebrae.MMOCore.Items.Mage.L01ApprenticeRobe;
import fr.tenebrae.MMOCore.Items.Mage.L01TrainingStaff;
import fr.tenebrae.MMOCore.Items.Warrior.L01RecruitShirt;
import fr.tenebrae.MMOCore.Items.Warrior.L01TrainingSword;

public enum ItemRegistry {

	Item(0, Item.class),
	L01TrainingSword(1, L01TrainingSword.class),
	L01RecruitShirt(2, L01RecruitShirt.class),
	L01TrainingBow(3, L01TrainingBow.class),
	L01RecruitTunic(4, L01RecruitTunic.class),
	L01TrainingStaff(5, L01TrainingStaff.class),
	L01ApprenticeRobe(6, L01ApprenticeRobe.class),
	L01TrainingDagger(7, L01TrainingDagger.class),
	L02TatteredLeatherTunic(8, L02TatteredLeatherTunic.class),
	L02TatteredLeatherHelmet(9, L02TatteredLeatherHelmet.class),
	L02TatteredLeatherLeggings(10, L02TatteredLeatherLeggings.class),
	L02TatteredLeatherBoots(11, L02TatteredLeatherBoots.class),
	L02TarnishedMailBoots(12, L02TarnishedMailBoots.class),
	L02TarnishedMailChestplate(13, L02TarnishedMailChestplate.class),
	L02TarnishedMailHelmet(14, L02TarnishedMailHelmet.class),
	L02TarnishedMailLeggings(15, L02TarnishedMailLeggings.class),
	L03JeweledDagger(16, L03JeweledDagger.class),
	L03ManaGatheringStaff(17, L03ManaGatheringStaff.class);
	
	private int id = 0;
	private Class<? extends Item> clazz = null;
	
	private ItemRegistry(int id, Class<? extends Item> clazz) {
		this.id = id;
		this.clazz = clazz;
	}
	
	public static void registerItems() {
		for (ItemRegistry itemReg : ItemRegistry.values()) {
			itemReg.register();
		}
	}
	
	private static Map<Integer,Class<? extends Item>> REGISTRY = new HashMap<Integer,Class<? extends Item>>();
	public static void add(int id, Class<? extends Item> item) { if (!REGISTRY.containsKey(id) && !REGISTRY.containsValue(item)) REGISTRY.put(id, item); }
	public void register() { if (!REGISTRY.containsKey(id) && !REGISTRY.containsValue(clazz)) REGISTRY.put(id, clazz); }
	public static void remove(int id) { REGISTRY.remove(id); }
	public static boolean contains(int id) { return REGISTRY.containsKey(id); }
	public static boolean contains(Item item) { return REGISTRY.containsValue(item); }
	
	public static int getId(Class<? extends Item> item) {
		int id = 0;
		for (Entry<Integer, Class<? extends Item>> entry : REGISTRY.entrySet()) if ((entry.getValue() == item)) return entry.getKey();
		return id;
	}
	
	public static Item getItem(int id) {
		if (REGISTRY.containsKey(id)) {
			try {
				return REGISTRY.get(id).newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		}
		else return null;
	}
}
