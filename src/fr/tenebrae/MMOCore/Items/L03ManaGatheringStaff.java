package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Items.Components.TriggerType;
import fr.tenebrae.MMOCore.Items.Components.WeaponPlace;
import fr.tenebrae.MMOCore.Items.Components.WeaponType;
import fr.tenebrae.MMOCore.Mechanics.Spells;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L03ManaGatheringStaff extends Item implements IWeaponItem {

	public L03ManaGatheringStaff(NBTTagCompound nbt) {
		super(nbt);
	}

	public L03ManaGatheringStaff() {
		this.setAmount(1);
		this.setId(17);
		this.setItemLevel(1);
		this.setMaxDurability(30);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(3);
		this.setType(ItemType.WEAPON);
		this.setWeaponType(WeaponType.STAFF);
		this.setWeaponPlace(WeaponPlace.MAIN_HAND);
		this.setQuality(ItemQuality.INHABITUEL);
		this.setNameId(17);
		this.setLoreId(0);
		this.setMaterial(Material.WOOD_HOE);
		this.setDamageData(0);
		this.addStat(Stats.ATTACK_SPEED, 2600.0);
		this.addStat(Stats.INTELLIGENCE, 1.0);
		this.setMinDmg(3);
		this.setMaxDmg(6);
		this.setSellPrice(163);
		this.addLinkedSpell(TriggerType.USE, Spells.HOLY_NOVA);
		
		createItemStack();
	}

	@Override
	public void onDealDamage(Player damager, Entity damaged, int damage, double distance, boolean critical, boolean success) {
		
	}
}
