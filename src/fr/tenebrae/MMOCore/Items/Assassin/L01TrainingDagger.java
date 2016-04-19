package fr.tenebrae.MMOCore.Items.Assassin;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;

import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.ItemsComponents.ItemQuality;
import fr.tenebrae.MMOCore.ItemsComponents.ItemType;
import fr.tenebrae.MMOCore.ItemsComponents.WeaponPlace;
import fr.tenebrae.MMOCore.ItemsComponents.WeaponType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L01TrainingDagger extends Item {

	public L01TrainingDagger(NBTTagCompound nbt) {
		super(nbt);
	}

	public L01TrainingDagger() {
		this.setAmount(1);
		this.setId(7);
		this.setItemLevel(1);
		this.setMaxDurability(17);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(0);
		this.setType(ItemType.WEAPON);
		this.setWeaponType(WeaponType.DAGGER);
		this.setWeaponPlace(WeaponPlace.MAIN_HAND);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(7);
		this.setLoreId(0);
		this.setMaterial(Material.WOOD_PICKAXE);
		this.setDamageData(0);
		this.addStat(Stats.ATTACK_SPEED, 1215);
		this.setMinDmg(1);
		this.setMaxDmg(3);
		
		createItemStack();
	}
}
