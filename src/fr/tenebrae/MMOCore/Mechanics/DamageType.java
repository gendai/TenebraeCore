package fr.tenebrae.MMOCore.Mechanics;

import net.minecraft.server.v1_9_R1.Entity;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;

import fr.tenebrae.MMOCore.Characters.Character;

public class DamageType {
	
	public Entity damager;
	public DamageTypeList type;
	
	public DamageType(DamageTypeList type, Entity damager) {
		this.damager = damager;
		this.type = type;
	}
	
	public DamageType(DamageTypeList type, Character damager) {
		this.damager = ((CraftPlayer)damager.getAccount()).getHandle();
		this.type = type;
	}
	
	public DamageType(DamageTypeList type) {
		this.type = type;
	}
	
	public Entity getDamager() {
		if (type == DamageTypeList.ENTITY_ATTACK || type == DamageTypeList.ENTITY_SPELL || type == DamageTypeList.PLAYER_ATTACK || type == DamageTypeList.PLAYER_SPELL || type == DamageTypeList.ENTITY_ATTACK_MAGICAL || type == DamageTypeList.ENTITY_SPELL_MAGICAL || type == DamageTypeList.PLAYER_ATTACK_MAGICAL || type == DamageTypeList.PLAYER_SPELL_MAGICAL) {
			return damager;
		} else {
			return null;
		}
	}
	
	public enum DamageTypeList {
		PLAYER_ATTACK,
		PLAYER_ATTACK_MAGICAL,
		PLAYER_SPELL,
		PLAYER_SPELL_MAGICAL,
		FALL_DAMAGE,
		ENVIRONMENT_DAMAGE,
		ENTITY_ATTACK,
		ENTITY_SPELL,
		ENTITY_ATTACK_MAGICAL,
		ENTITY_SPELL_MAGICAL,
		OTHER,
		OTHER_MAGICAL;
	}
}
