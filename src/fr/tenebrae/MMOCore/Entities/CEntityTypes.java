package fr.tenebrae.MMOCore.Entities;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_9_R1.BiomeBase;
import net.minecraft.server.v1_9_R1.BiomeBase.BiomeMeta;
import net.minecraft.server.v1_9_R1.EntityCaveSpider;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.EntityTypes;

import org.bukkit.entity.EntityType;

public enum CEntityTypes {
	
	L01MineSpider("L01MineSpider", 59, EntityType.SPIDER, EntityCaveSpider.class, L01MineSpider.class);
	
	private String name;
	private int id;
	private EntityType entityType;
	private Class<? extends EntityInsentient> nmsClass;
	private Class<? extends EntityInsentient> customClass;
	 
	private CEntityTypes(/*The name of the NMS entity*/String name, /*The entity id*/int id, /*The entity type*/EntityType entityType, /*The origional NMS class*/ Class<? extends EntityInsentient> nmsClass, /*The custom class*/ Class<? extends EntityInsentient> customClass) {
		this.name = name;
		this.id = id;
		this.entityType = entityType;
		this.nmsClass = nmsClass;
		this.customClass = customClass;
		
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
	
	/*Register our entities to the server, add to onEnable()*/
	public static void registerEntities() {
		for (CEntityTypes entity : values()) /*Get our entities*/
		a(entity.getCustomClass(), entity.getName(), entity.getID());
		/*Get all biomes on the server*/
		BiomeBase[] biomes;
		try {
		biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
		} catch (Exception exc) {
		return;
		}
		for (BiomeBase biomeBase : biomes) {
		if (biomeBase == null)
		break;
		for (String field : new String[] { "at", "au", "av", "aw" }) //Lists that hold all entity types
		try {
		Field list = BiomeBase.class.getDeclaredField(field);
		list.setAccessible(true);
		@SuppressWarnings("unchecked")
		List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);
		 
		for (BiomeMeta meta : mobList)
		for (CEntityTypes entity : values())
		if (entity.getNMSClass().equals(meta.b)) /*Test if the entity has the custom entity type*/
		meta.b = entity.getCustomClass(); //Set it's meta to our custom class's meta
		} catch (Exception e) {
		e.printStackTrace();
		}
		}
	}
	
	/*Method(add to onDisable()) to prevent server leaks when the plugin gets disabled*/
	@SuppressWarnings("rawtypes")
	public static void unregisterEntities() {
		for (CEntityTypes entity : values()) {
			// Remove our class references.
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
			biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes"); /*Get all biomes again*/
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
	
	/*A little Util for getting a private field*/
	@SuppressWarnings("rawtypes")
	private static Object getPrivateStatic(Class clazz, String f) throws Exception {
		Field field = clazz.getDeclaredField(f);
		field.setAccessible(true);
		return field.get(null);
	
	}
	
	/*Set data into the entitytypes class from NMS*/
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
}