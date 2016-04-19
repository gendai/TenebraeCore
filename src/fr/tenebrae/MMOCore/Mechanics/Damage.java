package fr.tenebrae.MMOCore.Mechanics;

import java.util.Random;

import net.minecraft.server.v1_9_R1.DamageSource;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import fr.tenebrae.MMOCore.Mechanics.DamageType.DamageTypeList;

public class Damage {
	
	private Entity target;
	private float damage;
	private DamageType type;


	public Damage(Entity target, int damage) {
		this.setTarget(target);
		this.setDamage(damage);
	}
	
	public Damage(Entity target, float damage) {
		this.setTarget(target);
		this.setDamage(damage);
	}
	
	public Damage(org.bukkit.entity.Entity target, float damage) {
		this.setTarget((Entity)target);
		this.setDamage(damage);
	}
	
	public Damage(org.bukkit.entity.Entity target, int damage) {
		this.setTarget((Entity)target);
		this.setDamage(damage);
	}

	public Damage(Entity target, int damage, DamageType type) {
		this.setTarget(target);
		this.setDamage(damage);
		this.setType(type);
	}
	
	public Damage(Entity target, float damage, DamageType type) {
		this.setTarget(target);
		this.setDamage(damage);
		this.setType(type);
	}
	
	public Damage(org.bukkit.entity.Entity target, float damage, DamageType type) {
		this.setTarget((Entity)target);
		this.setDamage(damage);
		this.setType(type);
	}
	
	public Damage(org.bukkit.entity.Entity target, int damage, DamageType type) {
		this.setTarget((Entity)target);
		this.setDamage(damage);
		this.setType(type);
	}

	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public DamageType getType() {
		return type;
	}

	public void setType(DamageType type) {
		this.type = type;
	}
	
	public double getIncreasedDamage() {
		NBTTagCompound damagerNBT = new NBTTagCompound();
		int level = damagerNBT.getInt("MMOLevel");
		int power = 0;
		double increasePercent = 0;
		if (type.type == DamageTypeList.PLAYER_ATTACK || type.type == DamageTypeList.ENTITY_ATTACK || type.type == DamageTypeList.ENTITY_SPELL || type.type == DamageTypeList.PLAYER_SPELL) power = damagerNBT.getInt("MMOPower");
		else if (type.type == DamageTypeList.PLAYER_ATTACK_MAGICAL || type.type == DamageTypeList.ENTITY_ATTACK_MAGICAL || type.type == DamageTypeList.ENTITY_SPELL_MAGICAL || type.type == DamageTypeList.PLAYER_SPELL_MAGICAL) power = damagerNBT.getInt("MMOMagicalPower");
		if (power > 0) increasePercent = power/(0.20*level);
		if (increasePercent > 100) increasePercent = 100;
		double totalDamagePercent = 100 + increasePercent;
		float increasedDamage = (float) ((damage*totalDamagePercent)/100);
		double criticalChance = damagerNBT.getDouble("CriticalChances")/100;
		if (new Random().nextDouble() <= criticalChance) {
			increasedDamage = (float) (increasedDamage*damagerNBT.getDouble("CritDamageMultiplier"));
		}
		return increasedDamage;
	}
	
	public double getFinalDamage() {
		NBTTagCompound targetNBT = new NBTTagCompound();
		int level = targetNBT.getInt("MMOLevel");
		int armor = 0;
		double reductionPercent = 0;
		if (type.type == DamageTypeList.PLAYER_ATTACK || type.type == DamageTypeList.ENTITY_ATTACK || type.type == DamageTypeList.ENTITY_SPELL || type.type == DamageTypeList.PLAYER_SPELL) armor = targetNBT.getInt("MMOArmor");
		else if (type.type == DamageTypeList.PLAYER_ATTACK_MAGICAL || type.type == DamageTypeList.ENTITY_ATTACK_MAGICAL || type.type == DamageTypeList.ENTITY_SPELL_MAGICAL || type.type == DamageTypeList.PLAYER_SPELL_MAGICAL) armor = targetNBT.getInt("MMOMagicalArmor");
		if (armor > 0) reductionPercent = armor/(0.25*level);
		if (reductionPercent > 100) reductionPercent = 100;
		double totalDamagePercent = 100 - reductionPercent;
		float finalDamage = (float) ((getIncreasedDamage()*totalDamagePercent)/100);
		double blockChance = targetNBT.getDouble("BlockChances")/100;
		if (new Random().nextDouble() <= blockChance) {
			finalDamage = (float) (finalDamage/targetNBT.getDouble("BlocageEfficiency"));
		}
		return finalDamage;
	}
	
	public void apply() {
		double health = ((org.bukkit.entity.LivingEntity)target.getBukkitEntity()).getHealth();
		double damage = getFinalDamage();
		if (damage > health) {
			target.damageEntity(DamageSource.GENERIC, (float)health);
		} else {
			target.damageEntity(DamageSource.GENERIC, (float)damage);
		}
	}
}
