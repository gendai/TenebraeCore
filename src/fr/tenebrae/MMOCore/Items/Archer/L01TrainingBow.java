package fr.tenebrae.MMOCore.Items.Archer;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;

import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.ItemsComponents.ItemQuality;
import fr.tenebrae.MMOCore.ItemsComponents.ItemType;
import fr.tenebrae.MMOCore.ItemsComponents.WeaponPlace;
import fr.tenebrae.MMOCore.ItemsComponents.WeaponType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L01TrainingBow extends Item {

	public L01TrainingBow(NBTTagCompound nbt) {
		super(nbt);
	}

	public L01TrainingBow() {
		this.setAmount(1);
		this.setId(3);
		this.setItemLevel(1);
		this.setMaxDurability(17);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(0);
		this.setType(ItemType.WEAPON);
		this.setWeaponType(WeaponType.BOW);
		this.setWeaponPlace(WeaponPlace.MAIN_HAND);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(3);
		this.setLoreId(0);
		this.setMaterial(Material.WOOD_SPADE);
		this.setDamageData(0);
		this.addStat(Stats.ATTACK_SPEED, 2000);
		this.setMinDmg(2);
		this.setMaxDmg(5);
		
		createItemStack();
	}
}