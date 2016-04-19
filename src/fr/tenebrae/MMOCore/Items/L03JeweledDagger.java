package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;

import fr.tenebrae.MMOCore.ItemsComponents.ItemQuality;
import fr.tenebrae.MMOCore.ItemsComponents.ItemType;
import fr.tenebrae.MMOCore.ItemsComponents.WeaponPlace;
import fr.tenebrae.MMOCore.ItemsComponents.WeaponType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L03JeweledDagger extends Item {

	public L03JeweledDagger(NBTTagCompound nbt) {
		super(nbt);
	}

	public L03JeweledDagger() {
		this.setAmount(1);
		this.setId(16);
		this.setItemLevel(7);
		this.setMaxDurability(25);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(3);
		this.setType(ItemType.WEAPON);
		this.setWeaponType(WeaponType.DAGGER);
		this.setWeaponPlace(WeaponPlace.BOTH);
		this.setQuality(ItemQuality.INHABITUEL);
		this.setNameId(16);
		this.setLoreId(0);
		this.setMaterial(Material.WOOD_PICKAXE);
		this.setDamageData(0);
		this.addStat(Stats.ATTACK_SPEED, 1170);
		this.addStat(Stats.AGILITY, 1);
		this.setMinDmg(3);
		this.setMaxDmg(5);
		this.setSellPrice(251);
		
		createItemStack();
	}
}
