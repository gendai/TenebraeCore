package fr.tenebrae.MMOCore.Entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.server.v1_9_R1.Block;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityCaveSpider;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.GenericAttributes;
import net.minecraft.server.v1_9_R1.MinecraftKey;
import net.minecraft.server.v1_9_R1.PathEntity;
import net.minecraft.server.v1_9_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_9_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_9_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_9_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_9_R1.SoundCategory;
import net.minecraft.server.v1_9_R1.SoundEffect;
import net.minecraft.server.v1_9_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftCaveSpider;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Entities.Pathfinders.PathfinderGoalRandomStroll;
import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.Items.ItemRegistry;
import fr.tenebrae.MMOCore.Items.Coins.CopperCoin;
import fr.tenebrae.MMOCore.Items.Coins.GoldCoin;
import fr.tenebrae.MMOCore.Items.Coins.SilverCoin;
import fr.tenebrae.MMOCore.Mechanics.Damage;
import fr.tenebrae.MMOCore.Mechanics.Sound;
import fr.tenebrae.MMOCore.Utils.AnimationAPI;
import fr.tenebrae.MMOCore.Utils.AnimationAPI.Animation;
import fr.tenebrae.MMOCore.Utils.NamePlatesAPI;

public class L01MineSpider extends EntityCaveSpider implements ICreature {

	public double health = 41;
	public double maxHealth = 41;
	public double attackSpeed = 1250;
	public Date lastAttackDate = null;
	public int atkMin = 1;
	public int atkMax = 3;
	public int level = 1;
	public Map<Entity,Integer> aggroList = new HashMap<Entity,Integer>();
	public Map<Item,Double> drops = new HashMap<Item,Double>();
	public Entity target = null;
	public Entity lastDamager = null;
	public int givenXp = 12;
	public double attackRange = 1;
	public int nameId = 300000;
	public Location spawn;
	public boolean resetting = false;
	public boolean isDead = false;
	public BossBar bossBar;
	
	public double walkSpeed = 0.24786667640209197000000000000001D;
	public double sprintSpeed = 0.37786667640209197000000000000001D;
	public double resetSpeed = 0.47786667640209197000000000000001D;

	public String hurtSound = "mob.littleSpider.hurt";
	public String aggroSound = "mob.littleSpider.aggro";
	public String attackSound = "mob.littleSpider.attack";
	public String idleSound = "mob.littleSpider.idle";
	public String stepSound = "mob.littleSpider.step";
	public String deathSound = "mob.littleSpider.death";
	
	public L01MineSpider(World world) {
		super(world);
		this.clearGoals();
		this.setupGoals();
	}

	public L01MineSpider(org.bukkit.World world) {
		super(((CraftWorld)world).getHandle());
		this.clearGoals();
		this.setupGoals();
	}
	
	public L01MineSpider(Location loc) {
		super(((CraftWorld)loc.getWorld()).getHandle());
		this.spawn = loc;
		this.clearGoals();
		this.setupGoals();
	}
	
	protected void initAttributes() {
		super.initAttributes();
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(1.0D);
	}
	
	public void clearGoals() {
		LinkedHashSet<?> goalB = (LinkedHashSet<?>)Utils.getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
		LinkedHashSet<?> goalC = (LinkedHashSet<?>)Utils.getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
		LinkedHashSet<?> targetB = (LinkedHashSet<?>)Utils.getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
		LinkedHashSet<?> targetC = (LinkedHashSet<?>)Utils.getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();
	}
	
	public void setupGoals() {
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, this.spawn, 1.0D));
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
	}
	
	@Override
	protected void a(BlockPosition pos, Block block) {
		this.a(new SoundEffect(new MinecraftKey(this.stepSound)), 0.15F, 1.0F);
	}
	
	@Override
	protected SoundEffect G() {
		//if (this.isDead) return new SoundEffect(new MinecraftKey("null"));
		if (this.isDead) return null;
		return new SoundEffect(new MinecraftKey(this.idleSound));
	}
	
	@Override
	public void setup() {
		this.health = this.maxHealth;
		this.setHealth(1.0F);
		NamePlatesAPI.setName(this, ""+this.nameId+"@"+this.level);
		this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(walkSpeed);
		this.bossBar = Bukkit.createBossBar(""+this.nameId, BarColor.RED, Utils.getBarStyleByHP((int) this.maxHealth));
		this.bossBar.setVisible(true);
		this.drops.put(ItemRegistry.getItem(8), 5.0);
		this.drops.put(ItemRegistry.getItem(9), 5.0);
		this.drops.put(ItemRegistry.getItem(10), 5.0);
		this.drops.put(ItemRegistry.getItem(11), 5.0);
		this.drops.put(ItemRegistry.getItem(12), 5.0);
		this.drops.put(ItemRegistry.getItem(13), 5.0);
		this.drops.put(ItemRegistry.getItem(14), 5.0);
		this.drops.put(ItemRegistry.getItem(15), 5.0);
		this.drops.put(ItemRegistry.getItem(16), 1.0);
		this.drops.put(ItemRegistry.getItem(17), 1.0);
		this.drops.put(ItemRegistry.getItem(18), 1.0);
		this.drops.put(new CopperCoin(new Random().nextInt(5)+1), 100.0);
		this.drops.put(new SilverCoin(new Random().nextInt(3)+1), 50.0);
		this.drops.put(new GoldCoin(new Random().nextInt(1)+1), 10.0);
	}

	@Override
	public boolean isAttackReady() {
		if (this.lastAttackDate == null) return true;
		return (Math.abs(this.lastAttackDate.getTime() - new Date().getTime()) >= this.attackSpeed);
	}

	@Override
	public void setTarget(Entity target) {
		if (target == null) {
			this.target = null;
			return;
		}
		if (this.target != null) {
			if (target == this.target) return;
			if (target instanceof EntityPlayer) {
				Player p = ((Player)CraftPlayer.getEntity(target.getWorld().getServer(), target));
				new Sound("warnings.tookAggro", Utils.getCharacter(p)).doesFollow(true).play();
			}
		}
		if (this.target == null) {
			new Sound(this.aggroSound, SoundCategory.HOSTILE).setPitch(1.26F).setLoc(this.getLocation()).play();
			this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(sprintSpeed);
		}
		this.target = target;
		this.getControllerLook().a(target, 10.0F, 40F);
	}
	
	@Override
	public Entity getTarget() {
		return this.target;
	}
	
	@Override
	public void lookAt(Entity target) {
		this.getControllerLook().a(target.locX, target.locY + target.getHeadHeight(), target.locZ, 10.0F, 40F);
	}
	
	@Override
	public void lookAt(Location target) {
		this.getControllerLook().a(target.getX(), target.getY(), target.getZ(), 10.0F, 40F);
	}

	@Override
	public void moveTo(Location loc) {
		this.moveTo(loc, 1.0D);
	}

	@Override
	public void moveTo(double x, double y, double z) {
		this.moveTo(x, y, z, 1.0D);
	}

	@Override
	public void moveTo(Location loc, double speed) {
		PathEntity path = this.getNavigation().a(loc.getX(), loc.getY(), loc.getZ());
		if (path != null) this.getNavigation().a(path, speed);
	}

	@Override
	public void moveTo(double x, double y, double z, double speed) {
		PathEntity path = this.getNavigation().a(x, y, z);
		if (path != null) this.getNavigation().a(path, speed);
	}
	
	@Override
	public boolean isMoving() {
		return this.getNavigation().n();
	}
	
	@Override
	public void onDeath() {
		if (this.isDead) return;
		if (this.target instanceof EntityPlayer) {
			if (!((Player)(((EntityPlayer)target).getBukkitEntity())).isOnline()) {
				this.target = null;
			}
		}
		this.getBEntity().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 10, true, false));
		this.getBEntity().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, -10, true, false));
		new Sound(this.deathSound, SoundCategory.HOSTILE).setLoc(this.getLocation()).setPitch(1.26F).play();
        this.world.broadcastEntityEffect(this, (byte) 3);
        if (this.lastDamager instanceof EntityPlayer) Main.connectedCharacters.get(((Player)CraftPlayer.getEntity(this.world.getServer(), this.lastDamager))).addXp(this.givenXp);
        this.isDead = true;
        new BukkitRunnable() {
        	@Override
        	public void run() {
        		final Location loc = getLocation();
        		dead = true;
        		bossBar.setVisible(false);
        		if (!drops.isEmpty()) {
        			for (Entry<Item,Double> entry : drops.entrySet()) {
        				if ((double)(new Random().nextInt(10000))/100 <= entry.getValue()) {
        					final ItemStack is = entry.getKey().getItemStack();
        					new BukkitRunnable() {
        						@Override
        						public void run() {
        							loc.getWorld().dropItemNaturally(loc, is);
        						}
        					}.runTask(Main.plugin);
        				}
        			}
        		}
        	}
        }.runTaskLaterAsynchronously(Main.plugin, 1L);
	}
	
	@Override
	public boolean damage(Entity damager, int amount, double aggroMultiplier) {
		if (this.isResetting()) return false;
		AnimationAPI.sendAnimation(this, Animation.TAKE_DAMAGE);
		new Sound(this.hurtSound, SoundCategory.HOSTILE).setLoc(this.getLocation()).setPitch(1.26F).play();
		int aggroCount = 0;
		if (this.aggroList.containsKey(damager)) {
			aggroCount = this.aggroList.get(damager);
			this.aggroList.remove(damager);
		} else {
			if (damager instanceof EntityPlayer) this.bossBar.addPlayer((Player) CraftPlayer.getEntity(damager.getWorld().getServer(), damager));
		}
		this.aggroList.put(damager, (int) (amount*aggroMultiplier) + aggroCount);
		this.health -= amount;
		this.lastDamager = damager;
		this.bossBar.setProgress(this.health/this.maxHealth);
		return true;
	}
	
	@Override
	public L01MineSpider spawn() {
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
	public Location getLocation() {
		return this.getBEntity().getLocation();
	}
	
	@Override
	public Location getSpawn() {
		return this.spawn;
	}
	
	public void U() {
		super.U();
		if (this.health <= 0) {
			this.onDeath();
			return;
		}
		if (this.isResetting()) {
			this.moveTo(this.spawn, 2.14D);
			if (this.getBEntity().getLocation().distance(this.spawn) <= 1.5) {
				this.setResetting(false);
				this.health = this.maxHealth;
				this.aggroList.clear();
				this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(walkSpeed);
			}
			return;
		}
		if (this.spawn.distance(this.getLocation()) >= 27.613) {
			this.reset();
			return;
		}
		if (this.health > this.maxHealth) this.health = this.maxHealth;
		if (this.target != null) {
			this.lookAt(this.target);
			this.moveTo(this.target.locX, this.target.locY, this.target.locZ);
			if (Utils.getEntityLocation(this).distance(Utils.getEntityLocation(target)) <= this.attackRange) {
				if (this.isAttackReady()) {
					new Sound(this.attackSound, SoundCategory.HOSTILE).setLoc(this.getLocation()).setPitch(1.26F).play();
					this.lastAttackDate = new Date();
					new Damage(this, target, Utils.getRandomDamage(this.atkMin, this.atkMax), 1.0D).apply();
				}
			}
		}
		if (!this.aggroList.isEmpty()) {
			List<Integer> aggroCounts = new ArrayList<Integer>(this.aggroList.values());
			Collections.sort(aggroCounts);
			Collections.reverse(aggroCounts);
			this.setTarget((Entity) Utils.getKeyFromValue(this.aggroList, aggroCounts.get(0)));
		}
		for (Entity e : new ArrayList<Entity>(this.aggroList.keySet())) {
			if (Utils.getEntityLocation(e).distance(this.getLocation()) > 15) {
				this.aggroList.remove(e);
				if (this.aggroList.isEmpty()) {
					this.reset();
					return;
				}
				if (e instanceof EntityPlayer) this.bossBar.removePlayer((Player) CraftPlayer.getEntity(e.getWorld().getServer(), e));
			}
		}
	}
	
	@Override
	public CaveSpider getBEntity() { return ((CaveSpider)CraftCaveSpider.getEntity(this.getWorld().getServer(), this)); }
	
	@Override
	public void reset() {
		this.aggroList.clear();
		this.setTarget(null);
		this.setResetting(true);
		this.moveTo(this.spawn, 1.0D);
		this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.37786667640209197000000000000001D);
	}

	@Override
	public boolean isResetting() {
		return this.resetting;
	}

	@Override
	public void setResetting(boolean resetting) {
		this.resetting = resetting;
	}

	@Override
	public Map<Item, Double> getDrops() {
		return this.drops;
	}
	
	@Override
	public void setDrops(Map<Item,Double> drops) {
		this.drops = drops;
	}

	@Override
	public void addDrop(Item drop, double percent) {
		if (!drops.containsKey(drop)) drops.put(drop, percent);
	}

	@Override
	public void removeDrop(Item drop) {
		if (drops.containsKey(drop)) drops.remove(drop);
	}

	@Override
	public boolean hasDrop(Item item) {
		return drops.containsKey(item);
	}

	@Override
	public BossBar getHealthBar() {
		return this.bossBar;
	}

	@Override
	public void setPlayMusic(boolean playMusic) {
		if (playMusic) this.bossBar.addFlag(BarFlag.PLAY_BOSS_MUSIC);
		else this.bossBar.removeFlag(BarFlag.PLAY_BOSS_MUSIC);
	}

	@Override
	public boolean doesPlayMusic() {
		return this.bossBar.hasFlag(BarFlag.PLAY_BOSS_MUSIC);
	}

	@Override
	public void setDarkenSky(boolean darkenSky) {
		if (darkenSky) this.bossBar.addFlag(BarFlag.DARKEN_SKY);
		else this.bossBar.removeFlag(BarFlag.DARKEN_SKY);
	}

	@Override
	public boolean doesDarkenSky() {
		return this.bossBar.hasFlag(BarFlag.DARKEN_SKY);
	}

	@Override
	public void setCreateFog(boolean createFog) {
		if (createFog) this.bossBar.addFlag(BarFlag.CREATE_FOG);
		else this.bossBar.removeFlag(BarFlag.CREATE_FOG);
	}

	@Override
	public boolean doesCreateFog() {
		return this.bossBar.hasFlag(BarFlag.CREATE_FOG);
	}
	
	@Override
	public double getCriticalChance() {
		return 7.92D;
	}
	
	@Override
	public double getArmor() {
		return 0.0D;
	}
	
	@Override
	public double getMagicalArmor() {
		return 0.0D;
	}

	@Override
	public void onTickBefore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTickAfter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTickBeforeCustom() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDamaged(Entity damager, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDamage(Damage damage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnterCombat(Entity target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDropCombat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChangeTarget(Entity oldTarget, Entity newTarget) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSpawn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNameId() {
		return nameId;
	}

	@Override
	public int getSubNameId() {
		// TODO Auto-generated method stub
		return 0;
	}
}
