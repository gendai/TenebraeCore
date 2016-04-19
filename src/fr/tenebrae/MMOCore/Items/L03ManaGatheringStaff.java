package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;

import fr.tenebrae.MMOCore.ItemsComponents.ItemQuality;
import fr.tenebrae.MMOCore.ItemsComponents.ItemType;
import fr.tenebrae.MMOCore.ItemsComponents.TriggerType;
import fr.tenebrae.MMOCore.ItemsComponents.WeaponPlace;
import fr.tenebrae.MMOCore.ItemsComponents.WeaponType;
import fr.tenebrae.MMOCore.Mechanics.Spells;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L03ManaGatheringStaff extends Item {

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
		this.addStat(Stats.ATTACK_SPEED, 2600);
		this.addStat(Stats.INTELLIGENCE, 1);
		this.setMinDmg(3);
		this.setMaxDmg(6);
		this.setSellPrice(163);
		this.addLinkedSpell(TriggerType.USE, Spells.HOLY_NOVA);
		
		createItemStack();
	}
}
