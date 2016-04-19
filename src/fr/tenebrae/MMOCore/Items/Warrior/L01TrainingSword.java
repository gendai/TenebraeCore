package fr.tenebrae.MMOCore.Items.Warrior;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;

import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.ItemsComponents.ItemQuality;
import fr.tenebrae.MMOCore.ItemsComponents.ItemType;
import fr.tenebrae.MMOCore.ItemsComponents.WeaponPlace;
import fr.tenebrae.MMOCore.ItemsComponents.WeaponType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L01TrainingSword extends Item {

	public L01TrainingSword(NBTTagCompound nbt) {
		super(nbt);
	}

	public L01TrainingSword() {
		this.setAmount(1);
		this.setId(1);
		this.setItemLevel(1);
		this.setMaxDurability(17);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(0);
		this.setType(ItemType.WEAPON);
		this.setWeaponType(WeaponType.SWORD_1H);
		this.setWeaponPlace(WeaponPlace.BOTH);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(1);
		this.setLoreId(0);
		this.setMaterial(Material.WOOD_SWORD);
		this.setDamageData(0);
		this.addStat(Stats.ATTACK_SPEED, 1600);
		this.setMinDmg(2);
		this.setMaxDmg(3);
		
		createItemStack();
	}
}
