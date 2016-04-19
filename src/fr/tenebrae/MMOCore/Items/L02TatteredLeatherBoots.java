package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;

import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L02TatteredLeatherBoots extends Item {

	public L02TatteredLeatherBoots(NBTTagCompound nbt) {
		super(nbt);
	}

	public L02TatteredLeatherBoots() {
		this.setAmount(1);
		this.setId(11);
		this.setItemLevel(3);
		this.setMaxDurability(41);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(2);
		this.setType(ItemType.BOOTS);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(11);
		this.setLoreId(0);
		this.setMaterial(Material.LEATHER_BOOTS);
		this.setDamageData(0);
		this.addStat(Stats.ARMOR, 2);
		this.setSellPrice(3);
		
		createItemStack();
	}
}