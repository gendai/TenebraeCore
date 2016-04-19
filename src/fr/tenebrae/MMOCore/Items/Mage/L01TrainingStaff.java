package fr.tenebrae.MMOCore.Items.Mage;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;

import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Items.Components.WeaponPlace;
import fr.tenebrae.MMOCore.Items.Components.WeaponType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L01TrainingStaff extends Item {

	public L01TrainingStaff(NBTTagCompound nbt) {
		super(nbt);
	}

	public L01TrainingStaff() {
		this.setAmount(1);
		this.setId(5);
		this.setItemLevel(1);
		this.setMaxDurability(17);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(0);
		this.setType(ItemType.WEAPON);
		this.setWeaponType(WeaponType.STAFF);
		this.setWeaponPlace(WeaponPlace.MAIN_HAND);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(5);
		this.setLoreId(0);
		this.setMaterial(Material.WOOD_HOE);
		this.setDamageData(0);
		this.addStat(Stats.ATTACK_SPEED, 2300);
		this.setMinDmg(1);
		this.setMaxDmg(4);
		
		createItemStack();
	}
}
