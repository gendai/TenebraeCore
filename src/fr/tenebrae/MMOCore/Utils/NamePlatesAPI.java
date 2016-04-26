package fr.tenebrae.MMOCore.Utils;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityArmorStand;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EnumHand;
import net.minecraft.server.v1_9_R1.EnumInteractionResult;
import net.minecraft.server.v1_9_R1.ItemStack;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.Vec3D;
import net.minecraft.server.v1_9_R1.World;

import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class NamePlatesAPI {

	static private Map<EntityType, Double> elevationAmountNP = new HashMap<EntityType, Double>();
	static private Map<EntityType, Double> elevationAmountSNP = new HashMap<EntityType, Double>();
	static Map<Entity, NamePlate> namePlates = new HashMap<Entity, NamePlate>();
	static Map<Entity, NamePlate> subnamePlates = new HashMap<Entity, NamePlate>();

	private enum NamePlatesElevations {

		pl(EntityType.ARMOR_STAND, 2.05),
		ol(EntityType.ARROW, 1.0),
		mp(EntityType.BAT, 1.05),
		lm(EntityType.BLAZE, 2.05),
		kp(EntityType.BOAT, 1.45),
		nl(EntityType.CAVE_SPIDER, 1.05),
		lp(EntityType.CHICKEN, 1.05),
		mo(EntityType.COW, 1.45),
		mk(EntityType.CREEPER, 1.90),
		a(EntityType.DROPPED_ITEM, 0.75),
		b(EntityType.EGG, 0.55),
		c(EntityType.ENDER_CRYSTAL, 3.25),
		h(EntityType.ENDER_DRAGON, 4.25),
		f(EntityType.ENDER_PEARL, 0.55),
		fd(EntityType.ENDER_SIGNAL, 0.55),
		d(EntityType.ENDERMAN, 3.05),
		v(EntityType.ENDERMITE, 0.55),
		x(EntityType.EXPERIENCE_ORB, 0.55),
		bw(EntityType.FALLING_BLOCK, 1.05),
		w(EntityType.FIREBALL, 1.05),
		az(EntityType.FIREWORK, 0.75),
		q(EntityType.FISHING_HOOK, 0.55),
		z(EntityType.GHAST, 4.25),
		e(EntityType.GIANT, 12.25),
		r(EntityType.GUARDIAN, 1.75),
		t(EntityType.HORSE, 1.85),
		y(EntityType.IRON_GOLEM, 2.75),
		u(EntityType.ITEM_FRAME, 1.05),
		i(EntityType.LEASH_HITCH, 0.55),
		o(EntityType.MAGMA_CUBE, 0.50),
		p(EntityType.MINECART, 1.05),
		pp(EntityType.MINECART_CHEST, 1.25),
		oo(EntityType.MINECART_COMMAND, 1.25),
		ji(EntityType.MINECART_FURNACE, 1.25),
		hb(EntityType.MINECART_HOPPER, 1.25),
		nv(EntityType.MINECART_MOB_SPAWNER, 1.25),
		rr(EntityType.MINECART_TNT, 1.25),
		df(EntityType.MUSHROOM_COW, 1.25),
		av(EntityType.OCELOT, 0.75),
		aq(EntityType.PAINTING, 1.25),
		azy(EntityType.PIG, 1.05),
		vb(EntityType.PIG_ZOMBIE, 2.05),
		bc(EntityType.PLAYER, 2.05),
		yz(EntityType.PRIMED_TNT, 1.25),
		cf(EntityType.RABBIT, 0.75),
		bcb(EntityType.SHEEP, 1.25),
		nb(EntityType.SILVERFISH, 0.75),
		xff(EntityType.SKELETON, 2.05),
		xgfvbc(EntityType.SLIME, 0.50),
		ft(EntityType.SMALL_FIREBALL, 0.75),
		yt(EntityType.SNOWBALL, 0.75),
		hgf(EntityType.SNOWMAN, 2.05),
		hbv(EntityType.SPIDER, 1.25),
		kjh(EntityType.SPLASH_POTION, 0.75),
		lkj(EntityType.SQUID, 1.25),
		xr(EntityType.THROWN_EXP_BOTTLE, 0.35),
		cgv(EntityType.VILLAGER, 2.05),
		crtf(EntityType.WITCH, 2.05),
		cfh(EntityType.WITHER, 3.25),
		xf(EntityType.WITHER_SKULL, 1.25),
		sdf(EntityType.WOLF, 0.85),
		sd(EntityType.ZOMBIE, 2.05),
		fs(EntityType.UNKNOWN, 0.25),
		dd(EntityType.WEATHER, 0.25);

		private EntityType type;
		private double amount;
		
		private NamePlatesElevations(EntityType type, double amount) {
			this.type = type;
			this.amount = amount;
		}
		
		private void addToMap() {
			elevationAmountNP.put(type, amount);
		}
	}

	private enum SubNamePlatesElevations {

		k(EntityType.ARMOR_STAND, 1.8),
		m(EntityType.ARROW, 0.75),
		p(EntityType.BAT, 0.8),
		i(EntityType.BLAZE, 1.8),
		y(EntityType.BOAT, 1.20),
		r(EntityType.CAVE_SPIDER, 0.8),
		z(EntityType.CHICKEN, 0.8),
		f(EntityType.COW, 1.2),
		h(EntityType.CREEPER, 1.65),
		j(EntityType.DROPPED_ITEM, 0.5),
		l(EntityType.EGG, 0.3),
		o(EntityType.ENDER_CRYSTAL, 3.0),
		pp(EntityType.ENDER_DRAGON, 4.0),
		q(EntityType.ENDER_PEARL, 0.3),
		s(EntityType.ENDER_SIGNAL, 0.3),
		d(EntityType.ENDERMAN, 2.8),
		fp(EntityType.ENDERMITE, 0.3),
		g(EntityType.EXPERIENCE_ORB, 0.3),
		gh(EntityType.FALLING_BLOCK, 0.8),
		jh(EntityType.FIREBALL, 0.8),
		vbn(EntityType.FIREWORK, 0.5),
		uyt(EntityType.FISHING_HOOK, 0.3),
		fc(EntityType.GHAST, 4.0),
		fd(EntityType.GIANT, 12.0),
		fz(EntityType.GUARDIAN, 1.5),
		fg(EntityType.HORSE, 1.6),
		fh(EntityType.IRON_GOLEM, 2.5),
		az(EntityType.ITEM_FRAME, 0.8),
		aa(EntityType.LEASH_HITCH, 0.3),
		ae(EntityType.MAGMA_CUBE, 0.25),
		ar(EntityType.MINECART, 0.8),
		at(EntityType.MINECART_CHEST, 1.0),
		ay(EntityType.MINECART_COMMAND, 1.0),
		au(EntityType.MINECART_FURNACE, 1.0),
		ai(EntityType.MINECART_HOPPER, 1.0),
		aQ(EntityType.MINECART_MOB_SPAWNER, 1.0),
		as(EntityType.MINECART_TNT, 1.0),
		ad(EntityType.MUSHROOM_COW, 1.0),
		af(EntityType.OCELOT, 0.5),
		ag(EntityType.PAINTING, 1.0),
		wa(EntityType.PIG, 0.8),
		wx(EntityType.PIG_ZOMBIE, 1.8),
		wd(EntityType.PLAYER, 1.8),
		wf(EntityType.PRIMED_TNT, 1.0),
		jg(EntityType.RABBIT, 0.5),
		nbv(EntityType.SHEEP, 1.0),
		vvc(EntityType.SILVERFISH, 0.5),
		wdf(EntityType.SKELETON, 1.8),
		qds(EntityType.SLIME, 0.25),
		uytf(EntityType.SMALL_FIREBALL, 0.5),
		sedgfh(EntityType.SNOWBALL, 0.5),
		wsfdxgfc(EntityType.SNOWMAN, 1.8),
		kh(EntityType.SPIDER, 1.0),
		ghfg(EntityType.SPLASH_POTION, 0.5),
		wsfdxgc(EntityType.SQUID, 1.0),
		wsd(EntityType.THROWN_EXP_BOTTLE, 0.1),
		iuy(EntityType.VILLAGER, 1.8),
		dfg(EntityType.WITCH, 1.8),
		wfx(EntityType.WITHER, 3.0),
		dsq(EntityType.WITHER_SKULL, 1.0),
		dsw(EntityType.WOLF, 0.6),
		jhgf(EntityType.ZOMBIE, 1.8),
		bnv(EntityType.UNKNOWN, 0.0),
		bvn(EntityType.WEATHER, 0.0);

		private EntityType key;
		private double value;
		
		private SubNamePlatesElevations(EntityType key, double value) {
			this.key = key;
			this.value = value;
		}
		
		private void addToMap() {
			elevationAmountSNP.put(key, value);
		}
	}

	public static void init() {
		elevationAmountNP.clear();
		elevationAmountSNP.clear();
		
		for(NamePlatesElevations npe : NamePlatesElevations.values()) npe.addToMap();
		for(SubNamePlatesElevations snpe : SubNamePlatesElevations.values()) snpe.addToMap();
	}

	public static void clearPlates() {
		for (NamePlate np : namePlates.values()) np.stop();
		for (NamePlate snp : subnamePlates.values()) snp.stop();
	}

	public static double getNamePlateElevation(EntityType type) {
		return elevationAmountNP.get(type);
	}

	public static double getNamePlateElevation(Entity e) {
		double returned = 0.0;
		if (e.getBukkitEntity().getType() == EntityType.SKELETON) if (((Skeleton)e.getBukkitEntity()).getSkeletonType() == SkeletonType.WITHER) returned += 0.5;
		if (e.getBukkitEntity().getType() == EntityType.SLIME) returned += (elevationAmountNP.get(e.getBukkitEntity().getType()) * (((Slime)e.getBukkitEntity()).getSize()))-elevationAmountNP.get(e.getBukkitEntity().getType());
		if (e.getBukkitEntity().getType() == EntityType.MAGMA_CUBE) returned += (elevationAmountNP.get(e.getBukkitEntity().getType()) * (((MagmaCube)e.getBukkitEntity()).getSize()))-elevationAmountNP.get(e.getBukkitEntity().getType());
		return returned+elevationAmountNP.get(e.getBukkitEntity().getType());
	}

	public static double getNamePlateElevation(org.bukkit.entity.Entity e) {
		double returned = 0.0;
		if (e.getType() == EntityType.SKELETON) if (((Skeleton)e).getSkeletonType() == SkeletonType.WITHER) returned += 0.5;
		if (e.getType() == EntityType.SLIME) returned += (elevationAmountNP.get(e.getType()) * (((Slime)e).getSize()))-elevationAmountNP.get(e.getType());
		if (e.getType() == EntityType.MAGMA_CUBE) returned += (elevationAmountNP.get(e.getType()) * (((MagmaCube)e).getSize()))-elevationAmountNP.get(e.getType());
		return returned+elevationAmountNP.get(e.getType());
	}

	public static double getSubNamePlateElevation(EntityType type) {
		return elevationAmountSNP.get(type);
	}

	public static double getSubNamePlateElevation(Entity e) {
		double returned = 0.0;
		if (e.getBukkitEntity().getType() == EntityType.SKELETON) if (((Skeleton)e.getBukkitEntity()).getSkeletonType() == SkeletonType.WITHER) returned += 0.5;
		if (e.getBukkitEntity().getType() == EntityType.SLIME) returned += (elevationAmountNP.get(e.getBukkitEntity().getType()) * (((Slime)e.getBukkitEntity()).getSize()))-elevationAmountNP.get(e.getBukkitEntity().getType());
		if (e.getBukkitEntity().getType() == EntityType.MAGMA_CUBE) returned += (elevationAmountNP.get(e.getBukkitEntity().getType()) * (((MagmaCube)e.getBukkitEntity()).getSize()))-elevationAmountNP.get(e.getBukkitEntity().getType());
		return returned+elevationAmountSNP.get(e.getBukkitEntity().getType());
	}

	public static double getSubNamePlateElevation(org.bukkit.entity.Entity e) {
		double returned = 0.0;
		if (e.getType() == EntityType.SKELETON) if (((Skeleton)e).getSkeletonType() == SkeletonType.WITHER) returned += 0.5;
		if (e.getType() == EntityType.SLIME) returned += (elevationAmountNP.get(e.getType()) * (((Slime)e).getSize()))-elevationAmountNP.get(e.getType());
		if (e.getType() == EntityType.MAGMA_CUBE) returned += (elevationAmountNP.get(e.getType()) * (((MagmaCube)e).getSize()))-elevationAmountNP.get(e.getType());
		return returned+elevationAmountSNP.get(e.getType());
	}

	public static void setName(Entity e, String name) {
		if (!hasMMONamePlate(e)) {
			NamePlate np = new NamePlate(e, name, getNamePlateElevation(e), PlateType.NAME);
			namePlates.put(e, np);
			np.apply();
			return;
		} else if (hasMMONamePlate(e)) {
			getNamePlate(e).changeName(name);
			return;
		}
	}

	public static void setName(org.bukkit.entity.Entity be, String name) {
		Entity e = ((CraftEntity)be).getHandle();
		if (!hasMMONamePlate(e)) {
			NamePlate np = new NamePlate(e, name, getNamePlateElevation(e), PlateType.NAME);
			namePlates.put(e, np);
			np.apply();
			return;
		} else if (hasMMONamePlate(e)) {
			getNamePlate(e).changeName(name);
			return;
		}
	}

	public static void setSubName(org.bukkit.entity.Entity be, String name) {
		Entity e = ((CraftEntity)be).getHandle();
		if (!hasMMOSubNamePlate(e)) {
			NamePlate np = new NamePlate(e, name, getSubNamePlateElevation(e), PlateType.SUBNAME);
			subnamePlates.put(e, np);
			np.apply();
			return;
		} else if (hasMMOSubNamePlate(e)) {
			getSubNamePlate(e).changeName(name);
			return;
		}
	}

	public static void setSubName(Entity e, String name) {
		if (!hasMMOSubNamePlate(e)) {
			NamePlate np = new NamePlate(e, name, getSubNamePlateElevation(e), PlateType.SUBNAME);
			subnamePlates.put(e, np);
			np.apply();
			return;
		} else if (hasMMOSubNamePlate(e)) {
			getSubNamePlate(e).changeName(name);
			return;
		}
	}

	public static boolean hasMMONamePlate(Entity e) {
		return namePlates.containsKey(e);
	}

	public static boolean hasMMOSubNamePlate(Entity e) {
		return subnamePlates.containsKey(e);
	}

	public static boolean hasMMONamePlate(org.bukkit.entity.Entity be) {
		return namePlates.containsKey(((CraftEntity)be).getHandle());
	}

	public static boolean hasMMOSubNamePlate(org.bukkit.entity.Entity be) {
		return subnamePlates.containsKey(((CraftEntity)be).getHandle());
	}

	public static NamePlate getNamePlate(Entity e) {
		if (!hasMMONamePlate(e)) return null;
		return namePlates.get(e);
	}

	public static NamePlate getNamePlate(org.bukkit.entity.Entity be) {
		Entity e = ((CraftEntity)be).getHandle();
		if (!hasMMONamePlate(e)) return null;
		return namePlates.get(e);
	}

	public static NamePlate getSubNamePlate(Entity e) {
		if (!hasMMOSubNamePlate(e)) return null;
		return subnamePlates.get(e);
	}

	public static NamePlate getSubNamePlate(org.bukkit.entity.Entity be) {
		Entity e = ((CraftEntity)be).getHandle();
		if (!hasMMOSubNamePlate(e)) return null;
		return subnamePlates.get(e);
	}
}

class NamePlate extends EntityArmorStand {

	private Entity named = null;
	private String name = "";
	private double elevation = 0.0;
	private PlateType type = PlateType.NAME;
	
	public String getName() {
		return name;
	}
	
	public Entity getNamedEntity() {
		return named;
	}
	
	public org.bukkit.entity.Entity getBNamedEntity() {
		return named.getBukkitEntity();
	}
	
	public PlateType getPlateType() {
		return type;
	}

	public NamePlate(World world) {
		super(world);
	}

	public NamePlate(org.bukkit.World world) {
		super(((CraftWorld)world).getHandle());
	}

	public NamePlate(Entity named, String name, double elevation, PlateType type) {
		super(named.getWorld());
		this.named = named;
		this.name = name;
		this.elevation = elevation;
		this.type = type;
	}

	public NamePlate(org.bukkit.entity.Entity bNamed, String name, double elevation, PlateType type) {
		super(((CraftEntity)bNamed).getHandle().getWorld());
		this.named = ((CraftEntity)bNamed).getHandle();
		this.name = name;
		this.elevation = elevation;
		this.type = type;
	}
	
	public void changeName(String newName) {
		this.name = newName;
		this.setCustomName(newName);
	}

	public void apply() {
		this.setPosition(named.locX, named.locY+elevation, named.locZ);
		this.setCustomName(name);
		this.setCustomNameVisible(true);
		this.setMarker(true);
		this.setGravity(false);
		this.setInvisible(true);
		named.getWorld().addEntity(this, SpawnReason.CUSTOM);
	}
	
	public void stop() {
		this.die();
		NBTTagCompound eNBT = new NBTTagCompound();
		named.e(eNBT);
		if (type == PlateType.NAME) eNBT.setBoolean("HasMMONamePlate", false);
		if (type == PlateType.SUBNAME) eNBT.setBoolean("HasMMOSubNamePlate", false);
		named.f(eNBT);
	}

	@Override
	protected void cn() {}

	@Override
	public EnumInteractionResult a(EntityHuman entityhuman, Vec3D vec3d, ItemStack itemstack, EnumHand enumhand) {
		return EnumInteractionResult.PASS;
	}

	@Override
	public void g(float f, float f1) {}

	@Override
	public void U() {
		this.world.methodProfiler.a("entityBaseTick");
		
		if (!named.isAlive()) {
			this.stop();
		}
		
		this.locX = named.locX;
		this.locY = named.locY+elevation;
		this.locZ = named.locZ;
		
		this.lastX = this.locX;
		this.lastY = this.locY;
		this.lastZ = this.locZ;
		this.lastPitch = this.pitch;
		this.lastYaw = this.yaw;

		if (this.locY < -64.0D) { Y(); }
		if (justCreated) { setFlag(0, false); }
		
		this.justCreated = false;
		this.world.methodProfiler.b();
	}
}

enum PlateType {
	
	NAME,
	SUBNAME;
	
}