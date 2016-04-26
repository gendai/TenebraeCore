package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.Entity;

import org.bukkit.entity.Player;

public interface IWeaponItem {

	public void onDealDamage(Player damager, Entity damaged, int damage, double distance, boolean critical, boolean success);
	
}
