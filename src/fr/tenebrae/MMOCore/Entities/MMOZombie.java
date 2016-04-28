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
import net.minecraft.server.v1_9_R1.EntityBoat;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.EntityZombie;
import net.minecraft.server.v1_9_R1.EnumParticle;
import net.minecraft.server.v1_9_R1.GenericAttributes;
import net.minecraft.server.v1_9_R1.MathHelper;
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
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Entities.Pathfinders.PathfinderGoalRandomStroll;
import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.Mechanics.Damage;
import fr.tenebrae.MMOCore.Mechanics.Sound;
import fr.tenebrae.MMOCore.Utils.AnimationAPI;
import fr.tenebrae.MMOCore.Utils.AnimationAPI.Animation;
import fr.tenebrae.MMOCore.Utils.NamePlatesAPI;

public class MMOZombie extends EntityZombie implements ICreature {

	public double health = 50;
	public double maxHealth = 50;
	public double attackSpeed = 1000;
	public Date lastAttackDate = null;
	public int atkMin = 1;
	public int atkMax = 1;
	public int level = 1;
	public Map<Entity,Integer> aggroList = new HashMap<Entity,Integer>();
	public Map<Item,Double> drops = new HashMap<Item,Double>();
	public Entity target = null;
	public Entity lastDamager = null;
	public int givenXp = 1;
	public double attackRange = 1;
	public int nameId = 0;
	public Location spawn;
	public boolean resetting = false;
	public boolean isDead = false;
	public BossBar bossBar;

	public double walkSpeed = 0.24786667640209197000000000000001D;
	public double sprintSpeed = 0.37786667640209197000000000000001D;
	public double resetSpeed = 0.47786667640209197000000000000001D;

	public String hurtSound = "mob.mmocreature.hurt";
	public String aggroSound = "mob.mmocreature.aggro";
	public String attackSound = "mob.mmocreature.attack";
	public String idleSound = "mob.mmocreature.idle";
	public String stepSound = "mob.mmocreature.step";
	public String deathSound = "mob.mmocreature.death";

	public MMOZombie(World world) {
		super(world);
		this.clearGoals();
		this.setupGoals();
	}

	public MMOZombie(org.bukkit.World world) {
		super(((CraftWorld)world).getHandle());
		this.clearGoals();
		this.setupGoals();
	}

	public MMOZombie(Location loc) {
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
			onDropCombat();
			return;
		}
		if (this.target != null) {
			if (target == this.target) return;
			if (target instanceof EntityPlayer) {
				Player p = ((Player)CraftPlayer.getEntity(target.getWorld().getServer(), target));
				new Sound("warnings.tookAggro", Utils.getCharacter(p)).doesFollow(true).play();
				onChangeTarget(this.target, target);
			}
		} else {
			new Sound(this.aggroSound, SoundCategory.HOSTILE).setPitch(1.26F).setLoc(this.getLocation()).play();
			this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(sprintSpeed);
			onEnterCombat(target);
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
		if (this.lastDamager instanceof EntityPlayer) {
			fr.tenebrae.MMOCore.Characters.Character c = Main.connectedCharacters.get(((Player)CraftPlayer.getEntity(this.world.getServer(), this.lastDamager)));
			if (c.getLevel()-this.level < 6) c.addXp(this.givenXp);
		}
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
				for (int i = 0; i < 20; i++) {
					double d0 = random.nextGaussian() * 0.02D;
					double d1 = random.nextGaussian() * 0.02D;
					double d2 = random.nextGaussian() * 0.02D;

					world.addParticle(EnumParticle.EXPLOSION_NORMAL, locX + random.nextFloat() * width * 2.0F - width, locY + random.nextFloat() * length, locZ + random.nextFloat() * width * 2.0F - width, d0, d1, d2, new int[0]);
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
		onDamaged(damager, amount);
		return true;
	}

	@Override
	public MMOZombie spawn() {
		try {
			this.setPosition(spawn.getX(), spawn.getY(), spawn.getZ());
			(((CraftWorld) spawn.getWorld()).getHandle()).addEntity(this, SpawnReason.CUSTOM);
			this.setup();
			this.health = maxHealth;
			onSpawn();
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

	@Override
	public void n() {

		if (!co()) {
			this.motX *= 0.98D;
			this.motY *= 0.98D;
			this.motZ *= 0.98D;
		}

		if (Math.abs(this.motX) < 0.003D) {
			this.motX = 0.0D;
		}

		if (Math.abs(this.motY) < 0.003D) {
			this.motY = 0.0D;
		}

		if (Math.abs(this.motZ) < 0.003D) {
			this.motZ = 0.0D;
		}

		this.world.methodProfiler.a("ai");
		if (cf()) {
			this.bc = false;
			this.bd = 0.0F;
			this.be = 0.0F;
			this.bf = 0.0F;
		} else if (co()) {
			this.world.methodProfiler.a("newAi");
			doTick();
			this.world.methodProfiler.b();
		}

		this.world.methodProfiler.b();
		this.world.methodProfiler.a("jump");
		if (this.bc) {
			if (isInWater()) {
				ci();
			} else if (an()) {
				cj();
			}
		}

		this.world.methodProfiler.b();
		this.world.methodProfiler.a("travel");
		this.bd *= 0.98F;
		this.be *= 0.98F;
		this.bf *= 0.9F;
		g(this.bd, this.be);
		this.world.methodProfiler.b();
	}

	@Override
	public void m() {
		onTickBefore();
		if (!this.world.isClientSide) {
		      setFlag(6, aM());
		}
		U();

		n();
		double d0 = this.locX - this.lastX;
		double d1 = this.locZ - this.lastZ;
		float f = (float)(d0 * d0 + d1 * d1);
		float f1 = this.aM;
		float f2 = 0.0F;

		this.aV = this.aW;
		float f3 = 0.0F;

		if (f > 0.0025F) {
			f3 = 1.0F;
			f2 = (float)Math.sqrt(f) * 3.0F;
			f1 = (float)MathHelper.b(d1, d0) * 57.295776F - 90.0F;
		}

		if (this.aC > 0.0F) {
			f1 = this.yaw;
		}

		if (!this.onGround) {
			f3 = 0.0F;
		}

		this.aW += (f3 - this.aW) * 0.3F;
		this.world.methodProfiler.a("headTurn");
		f2 = h(f1, f2);
		this.world.methodProfiler.b();
		this.world.methodProfiler.a("rangeChecks");

		while (this.yaw - this.lastYaw < -180.0F) {
			this.lastYaw -= 360.0F;
		}

		while (this.yaw - this.lastYaw >= 180.0F) {
			this.lastYaw += 360.0F;
		}

		while (this.aM - this.aN < -180.0F) {
			this.aN -= 360.0F;
		}

		while (this.aM - this.aN >= 180.0F) {
			this.aN += 360.0F;
		}

		while (this.pitch - this.lastPitch < -180.0F) {
			this.lastPitch -= 360.0F;
		}

		while (this.pitch - this.lastPitch >= 180.0F) {
			this.lastPitch += 360.0F;
		}

		while (this.aO - this.aP < -180.0F) {
			this.aP -= 360.0F;
		}

		while (this.aO - this.aP >= 180.0F) {
			this.aP += 360.0F;
		}

		this.world.methodProfiler.b();



		if (!this.world.isClientSide) {
			if (this.ticksLived % 5 == 0) {
				boolean flag = !(bt() instanceof EntityInsentient);
				boolean flag1 = !(by() instanceof EntityBoat);

				this.goalSelector.a(5, (flag) && (flag1));
				this.goalSelector.a(2, flag);
			}
		}
		onTickAfter();
	}

	@Override
	public void cO() {}

	public void U() { // DONE
		
	    this.world.methodProfiler.a("entityBaseTick");
	    if ((isPassenger()) && (by().dead)) {
	      stopRiding();
	    }

	    if (this.j > 0) {
	      this.j -= 1;
	    }

	    this.lastX = this.locX;
	    this.lastY = this.locY;
	    this.lastZ = this.locZ;
	    this.lastPitch = this.pitch;
	    this.lastYaw = this.yaw;

	    aj();

	    if (an()) {
	      burnFromLava();
	      this.fallDistance *= 0.5F;
	    }

	    if (this.locY < -64.0D) {
	      die();
	    }

	    if (!this.world.isClientSide) {
	      setFlag(0, this.fireTicks > 0);
	    }

	    this.justCreated = false;
	    this.world.methodProfiler.b();
	    
	    


		this.aB = this.aC;
		this.world.methodProfiler.a("livingEntityBaseTick");

		this.aI = this.aJ;

		if (getHealth() <= 0.0F) {
			bC();
		}
		this.aY = this.aX;
		this.aN = this.aM;
		this.aP = this.aO;
		this.lastYaw = this.yaw;
		this.lastPitch = this.pitch;
		this.world.methodProfiler.b();



		this.world.methodProfiler.a("mobBaseTick");
		if ((isAlive()) && (this.random.nextInt(1000) < this.a_++)) {
			o();
			D();
		}

		this.world.methodProfiler.b();

		onTickBeforeCustom();

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
					Damage damage = new Damage(this, target, Utils.getRandomDamage(this.atkMin, this.atkMax), 1.0D);
					onDamage(damage);
					damage.apply();
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
		onTickAfter();
	}

	@Override
	public Zombie getBEntity() { return ((Zombie)CraftZombie.getEntity(this.getWorld().getServer(), this)); }

	@Override
	public void reset() {
		this.aggroList.clear();
		this.setTarget(null);
		this.setResetting(true);
		this.moveTo(this.spawn, 1.0D);
		this.bossBar.setVisible(false);
		this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.37786667640209197000000000000001D);
		onReset();
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
	public void onDamaged(Entity damager, int amount) {
		
	}

	@Override
	public void onDamage(Damage damage) {
		
	}

	@Override
	public void onEnterCombat(Entity target) {
		
	}

	@Override
	public void onReset() {
		
	}
	
	@Override
	public void onDropCombat() {
		
	}

	@Override
	public void onTickBefore() {
		
	}
	
	@Override
	public void onTickBeforeCustom() {
		
	}

	@Override
	public void onTickAfter() {
		
	}
	
	@Override
	public void onChangeTarget(Entity oldTarget, Entity newTarget) {
		
	}

	@Override
	public void onSpawn() {
		// TODO Auto-generated method stub
		
	}
}
