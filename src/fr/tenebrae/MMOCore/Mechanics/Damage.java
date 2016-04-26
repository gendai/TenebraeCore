package fr.tenebrae.MMOCore.Mechanics;

import java.util.Random;

import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityArmorStand;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.EnumHand;
import net.minecraft.server.v1_9_R1.EnumInteractionResult;
import net.minecraft.server.v1_9_R1.ItemStack;
import net.minecraft.server.v1_9_R1.Vec3D;
import net.minecraft.server.v1_9_R1.World;

import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Characters.Character;
import fr.tenebrae.MMOCore.Entities.ICreature;
import fr.tenebrae.MMOCore.Items.IWeaponItem;
import fr.tenebrae.MMOCore.Items.ItemRegistry;


public class Damage {

	private double aggroMultiplier = 1.0D;
	private Object damager;
	private Object target;
	private int damage = 0;
	private DamageType type = DamageType.ENVIRONMENT_DAMAGE;
	private boolean isCritical = false;

	public Damage(Object damager, Object target, int damage) {
		setDamager(damager);
		setTarget(target);
		setDamage(damage);
	}

	public Damage(Object damager, Object target, int damage, double aggroMultiplier) {
		setDamager(damager);
		setTarget(target);
		setDamage(damage);
		setAggroMultiplier(aggroMultiplier);
	}

	public Object getDamager() {
		return damager;
	}

	public void setDamager(Object damager) {
		if (!(damager instanceof ICreature) && !(damager instanceof Character)) return;
		this.damager = damager;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		if (!(target instanceof ICreature) && !(target instanceof Character) && !(target instanceof Player) && !(target instanceof Entity)) return;
		this.target = target;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public DamageType getType() {
		return type;
	}

	public void setType(DamageType type) {
		this.type = type;
	}

	public boolean isCritical() {
		return isCritical;
	}

	public void setCritical(boolean isCritical) {
		this.isCritical = isCritical;
	}

	public double getAggroMultiplier() {
		return aggroMultiplier;
	}

	public void setAggroMultiplier(double aggroMultiplier) {
		this.aggroMultiplier = aggroMultiplier;
	}

	public void apply() {
		try {
			if (damager == null) return;
			if (target == null) return;
			if (damager instanceof Character) {
				Main.log.info("Damager is character");
				Character c = (Character)damager;
				Stats usedStat = Stats.STRENGTH;
				Stats powerStat = Stats.POWER;
				if (type.isMagical()) {
					usedStat = Stats.INTELLIGENCE;
					powerStat = Stats.MAGICAL_POWER;
				}

				damage = (int) (damage+((int)(c.getStat(usedStat)*1.5))+c.getStat(powerStat));
				if ((double)(new Random().nextInt(10000))/100 <= c.getStat(Stats.CRITICAL_CHANCE)) {
					this.isCritical = true;
					damage *= Math.min(((double)(new Random().nextInt(100))/100)+1.5, 2.25);
				}
			} else if (damager instanceof ICreature) {
				Main.log.info("Damager is creature");
				ICreature c = (ICreature)damager;
				if ((double)(new Random().nextInt(10000))/100 <= c.getCriticalChance()) {
					this.isCritical = true;
					damage *= Math.min(((double)(new Random().nextInt(100))/100)+1.5, 2.25);
				}
			}

			if (target instanceof Character) {
				Main.log.info("Target is character");
				Character c = (Character)target;
				Stats defStat = Stats.ARMOR;
				if (type.isMagical()) defStat = Stats.MAGICAL_ARMOR;

				double reducPercent = c.getStat(defStat);
				Main.log.info("Reduction Percent is "+reducPercent);
				Main.log.info("Damage before is "+damage);

				damage = (int) (damage - ((damage*reducPercent)/100));
				if (damage < 0) damage = 0;
				Main.log.info("Damage after is "+damage);

				c.damage((damager instanceof Character ? ((Character)damager).getNMSAccount() : (Entity)damager), damage);
				new DamageIndicator(c.getNMSAccount(), damage, this.isCritical()).spawn();

				if (damager instanceof Character) {
					Character ch = (Character)damager;
					if (ch.equipment.getWeapon() != null) {
						((IWeaponItem)ItemRegistry.getItem(ch.equipment.getWeapon().getId())).onDealDamage(ch.getAccount(), c.getNMSAccount(), damage, ch.getAccount().getLocation().distance(c.getAccount().getLocation()), this.isCritical, true);
					}
				}
			} else if (target instanceof EntityPlayer) {
				Main.log.info("Target is entityplayer");
				Character c = Main.connectedCharacters.get(((Player)CraftPlayer.getEntity(((EntityPlayer)target).getWorld().getServer(), ((EntityPlayer)target))));
				Stats defStat = Stats.ARMOR;
				if (type.isMagical()) defStat = Stats.MAGICAL_ARMOR;

				double reducPercent = c.getStat(defStat);
				Main.log.info("Reduction Percent is "+reducPercent);
				Main.log.info("Damage before is "+damage);

				damage = (int) (damage - ((damage*reducPercent)/100));
				if (damage < 0) damage = 0;
				Main.log.info("Damage after is "+damage);

				c.damage((damager instanceof Character ? ((Character)damager).getNMSAccount() : (Entity)damager), damage);
				new DamageIndicator(c.getNMSAccount(), damage, this.isCritical()).spawn();

				if (damager instanceof Character) {
					Character ch = (Character)damager;
					if (ch.equipment.getWeapon() != null) {
						((IWeaponItem)ItemRegistry.getItem(ch.equipment.getWeapon().getId())).onDealDamage(ch.getAccount(), c.getNMSAccount(), damage, ch.getAccount().getLocation().distance(c.getAccount().getLocation()), this.isCritical, true);
					}
				}
			} else if (target instanceof Player) {
				Main.log.info("Target is player");
				Character c = Main.connectedCharacters.get(((Player)target));
				Stats defStat = Stats.ARMOR;
				if (type.isMagical()) defStat = Stats.MAGICAL_ARMOR;

				double reducPercent = c.getStat(defStat);
				Main.log.info("Reduction Percent is "+reducPercent);
				Main.log.info("Damage before is "+damage);

				damage = (int) (damage - ((damage*reducPercent)/100));
				if (damage < 0) damage = 0;
				Main.log.info("Damage after is "+damage);

				c.damage((damager instanceof Character ? ((Character)damager).getNMSAccount() : (Entity)damager), damage);
				new DamageIndicator(c.getNMSAccount(), damage, this.isCritical()).spawn();

				if (damager instanceof Character) {
					Character ch = (Character)damager;
					if (ch.equipment.getWeapon() != null) {
						((IWeaponItem)ItemRegistry.getItem(ch.equipment.getWeapon().getId())).onDealDamage(ch.getAccount(), c.getNMSAccount(), damage, ch.getAccount().getLocation().distance(c.getAccount().getLocation()), this.isCritical, true);
					}
				}
			} else if (target instanceof ICreature) {
				Main.log.info("Target is creature");
				ICreature c = (ICreature)target;
				double reducPercent = (type.isMagical() ? c.getMagicalArmor() : c.getArmor());
				Main.log.info("Reduction Percent is "+reducPercent);
				Main.log.info("Damage before is "+damage);
				damage = (int) (damage - ((damage*reducPercent)/100));
				if (damage < 0) damage = 0;
				Main.log.info("Damage after is "+damage);

				c.damage((damager instanceof Character ? ((Character)damager).getNMSAccount() : (Entity)damager), damage, aggroMultiplier);
				new DamageIndicator((Entity)c, damage, this.isCritical()).spawn();

				if (damager instanceof Character) {
					Character ch = (Character)damager;
					if (ch.equipment.getWeapon() != null) {
						((IWeaponItem)ItemRegistry.getItem(ch.equipment.getWeapon().getId())).onDealDamage(ch.getAccount(), (Entity)c, damage, ch.getAccount().getLocation().distance(c.getLocation()), this.isCritical, true);
					}
				}
			} else if (target instanceof Entity) {
				if (((Entity)target).getBukkitEntity().getType() != EntityType.PLAYER) return;
				Character c = Main.connectedCharacters.get((Player)((Entity)target).getBukkitEntity());
				Stats defStat = Stats.ARMOR;
				if (type.isMagical()) defStat = Stats.MAGICAL_ARMOR;

				double reducPercent = c.getStat(defStat);

				damage = (int) (damage - ((damage*reducPercent)/100));
				if (damage < 0) damage = 0;

				c.damage((damager instanceof Character ? ((Character)damager).getNMSAccount() : (Entity)damager), damage);
				new DamageIndicator(c.getNMSAccount(), damage, this.isCritical()).spawn();

				if (damager instanceof Character) {
					Character ch = (Character)damager;
					if (ch.equipment.getWeapon() != null) {
						((IWeaponItem)ItemRegistry.getItem(ch.equipment.getWeapon().getId())).onDealDamage(ch.getAccount(), c.getNMSAccount(), damage, ch.getAccount().getLocation().distance(c.getAccount().getLocation()), this.isCritical, true);
					}
				}
			}
		} catch (NullPointerException npe) {}
	}
}

class DamageIndicator extends EntityArmorStand {

	private int tickk = 0;

	public DamageIndicator(World world) {
		super(world);
	}

	public DamageIndicator(org.bukkit.World world) {
		super(((CraftWorld)world).getHandle());
	}

	public DamageIndicator(Entity damaged, int damage, boolean critical) {
		super(damaged.getWorld());
		this.setPosition(damaged.locX+(-0.5+Math.random()+Math.random()), damaged.locY+1.5, damaged.locZ+(-0.5+Math.random()+Math.random()));
		this.setInvisible(true);
		this.setGravity(false);
		this.setMarker(true);
		this.setCustomName("§"+(damage > 0 ? "c-" : "6-")+damage);
		this.setCustomNameVisible(true);
	}

	public ArmorStand spawn() {
		this.world.addEntity(this, SpawnReason.CUSTOM);
		return (ArmorStand) this.getBukkitEntity();
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
		tickk++;
		if (tickk >= 30) {
			this.die();
			return;
		}

		this.world.methodProfiler.a("entityBaseTick");

		if (justCreated) { setFlag(0, false); }

		this.justCreated = false;
		this.world.methodProfiler.b();
	}
}
