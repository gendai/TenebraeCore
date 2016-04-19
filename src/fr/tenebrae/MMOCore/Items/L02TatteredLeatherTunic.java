package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;

import fr.tenebrae.MMOCore.ItemsComponents.ItemQuality;
import fr.tenebrae.MMOCore.ItemsComponents.ItemType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L02TatteredLeatherTunic extends Item {

	public L02TatteredLeatherTunic(NBTTagCompound nbt) {
		super(nbt);
	}

	public L02TatteredLeatherTunic() {
		this.setAmount(1);
		this.setId(8);
		this.setItemLevel(3);
		this.setMaxDurability(49);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(2);
		this.setType(ItemType.CHESTPLATE);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(8);
		this.setLoreId(0);
		this.setMaterial(Material.LEATHER_CHESTPLATE);
		this.setDamageData(0);
		this.addStat(Stats.ARMOR, 5);
		this.setSellPrice(5);
		
		createItemStack();
	}
}