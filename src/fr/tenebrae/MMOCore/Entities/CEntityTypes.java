package fr.tenebrae.MMOCore.Entities;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_9_R1.BiomeBase;
import net.minecraft.server.v1_9_R1.BiomeBase.BiomeMeta;
import net.minecraft.server.v1_9_R1.EntityCaveSpider;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.EntityTypes;
import net.minecraft.server.v1_9_R1.EntityVillager;
import net.minecraft.server.v1_9_R1.EntityZombie;

import org.bukkit.entity.EntityType;

import fr.tenebrae.MMOCore.Entities.MMOEntities.L01MineSpider;
import fr.tenebrae.MMOCore.Entities.MMOEntities.L02MineBroodMother;
import fr.tenebrae.MMOCore.Entities.MMOEntities.L10AlphaTestZombie;
import fr.tenebrae.MMOCore.Entities.MMOEntities.L17NivlasSpider;
import fr.tenebrae.MMOCore.Entities.MMOEntities.L01NpcTest;

public enum CEntityTypes {

	L01MineSpider("L01MineSpider", 59, EntityType.CAVE_SPIDER, EntityCaveSpider.class, L01MineSpider.class, 300000),
	L02MineBroodMother("L02MineBroodMother", 52, EntityType.SPIDER, EntityCaveSpider.class, L02MineBroodMother.class, 300001),
	L10AlphaTestZombie("L10AlphaTestZombie", 54, EntityType.ZOMBIE, EntityZombie.class, L10AlphaTestZombie.class, 300003),
	L17NivlasSpider("L17NivlasSpider", 59, EntityType.CAVE_SPIDER, EntityCaveSpider.class, L17NivlasSpider.class, 300002),
	L01NpcTest("L01NpcTest",120,EntityType.VILLAGER, EntityVillager.class, L01NpcTest.class, 300003);
	
	private String name;
	private int id;
	private EntityType entityType;
	private Class<? extends EntityInsentient> nmsClass;
	private Class<? extends EntityInsentient> customClass;
	private int nameId;
	private static Map<Class<? extends EntityInsentient>, Integer> REGISTRY = new HashMap<Class<? extends EntityInsentient>, Integer>();
	 
	private CEntityTypes(String name, int id, EntityType entityType, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass, int nameId) {
		this.name = name;
		this.id = id;
		this.entityType = entityType;
		this.nmsClass = nmsClass;
		this.customClass = customClass;
		this.nameId = nameId;
	}
	
	public static int getId(Class<? extends EntityInsentient> clazz) {
		if (REGISTRY.containsKey(clazz)) return REGISTRY.get(clazz);
		else return 0;
	}
	 
	public String getName() {
		return name;
	}
	 
	public int getID() {
		return id;
	}
	 
	public EntityType getEntityType() {
		return entityType;
	}
	 
	public Class<? extends EntityInsentient> getNMSClass() {
		return nmsClass;
	}
	 
	public Class<? extends EntityInsentient> getCustomClass() {
		return customClass;
	}
	
	public static void registerEntities() {
		for (CEntityTypes entity : values()) {
			a(entity.getCustomClass(), entity.getName(), entity.getID());
			REGISTRY.put(entity.getCustomClass(), entity.getNameId());
		}
		BiomeBase[] biomes;
		try {
		biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
		} catch (Exception exc) {
		return;
		}
		for (BiomeBase biomeBase : biomes) {
		if (biomeBase == null)
		break;
		for (String field : new String[] { "at", "au", "av", "aw" })
		try {
		Field list = BiomeBase.class.getDeclaredField(field);
		list.setAccessible(true);
		@SuppressWarnings("unchecked")
		List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);
		 
		for (BiomeMeta meta : mobList)
		for (CEntityTypes entity : values())
		if (entity.getNMSClass().equals(meta.b))
		meta.b = entity.getCustomClass();
		} catch (Exception e) {
		e.printStackTrace();
		}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void unregisterEntities() {
		for (CEntityTypes entity : values()) {
			try { ((Map) getPrivateStatic(EntityTypes.class, "d")).remove(entity.getCustomClass()); }
			catch (Exception e) { e.printStackTrace(); }
		 
			try { ((Map) getPrivateStatic(EntityTypes.class, "f")).remove(entity.getCustomClass()); }
			catch (Exception e) { e.printStackTrace(); }
		}
		 
		for (CEntityTypes entity : values()) {
			try {
				a(entity.getNMSClass(), entity.getName(), entity.getID());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		 
		BiomeBase[] biomes;
		try {
			biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
		} catch (Exception exc) {
			return;
		}
		for (BiomeBase biomeBase : biomes) {
			if (biomeBase == null) break;
			 
			for (String field : new String[] { "at", "au", "av", "aw" }) {
				try {
					Field list = BiomeBase.class.getDeclaredField(field);
					list.setAccessible(true);
					@SuppressWarnings("unchecked")
					List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);
				 
					for (BiomeMeta meta : mobList) for (CEntityTypes entity : values()) if (entity.getCustomClass().equals(meta.b)) meta.b = entity.getNMSClass(); /*Set the entities meta back to the NMS one*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static Object getPrivateStatic(Class clazz, String f) throws Exception {
		Field field = clazz.getDeclaredField(f);
		field.setAccessible(true);
		return field.get(null);
	
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void a(Class paramClass, String paramString, int paramInt) {
		try {
			((Map) getPrivateStatic(EntityTypes.class, "c")).put(paramString, paramClass);
			((Map) getPrivateStatic(EntityTypes.class, "d")).put(paramClass, paramString);
			((Map) getPrivateStatic(EntityTypes.class, "e")).put(Integer.valueOf(paramInt), paramClass);
			((Map) getPrivateStatic(EntityTypes.class, "f")).put(paramClass, Integer.valueOf(paramInt));
			((Map) getPrivateStatic(EntityTypes.class, "g")).put(paramString, Integer.valueOf(paramInt));
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public int getNameId() {
		return nameId;
	}
}