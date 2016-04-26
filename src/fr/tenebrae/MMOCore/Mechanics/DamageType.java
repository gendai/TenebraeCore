package fr.tenebrae.MMOCore.Mechanics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public enum DamageType {
	
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
	
	private static List<DamageType> magical = new ArrayList<DamageType>(Arrays.asList(DamageType.PLAYER_ATTACK_MAGICAL,
							DamageType.PLAYER_SPELL_MAGICAL,
							DamageType.ENTITY_ATTACK_MAGICAL,
							DamageType.ENTITY_SPELL_MAGICAL,
							DamageType.OTHER_MAGICAL));
	
	public static boolean isMagical(DamageType type) {
		return (magical.contains(type));
	}
	
	public static boolean isPhysical(DamageType type) {
		return !(magical.contains(type));
	}
	
	public boolean isMagical() {
		return DamageType.isMagical(this);
	}
	
	public boolean isPhysical() {
		return DamageType.isPhysical(this);
	}
}
