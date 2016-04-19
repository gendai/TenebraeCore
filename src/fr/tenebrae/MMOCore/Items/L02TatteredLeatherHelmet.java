package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;

import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L02TatteredLeatherHelmet extends Item {

	public L02TatteredLeatherHelmet(NBTTagCompound nbt) {
		super(nbt);
	}

	public L02TatteredLeatherHelmet() {
		this.setAmount(1);
		this.setId(9);
		this.setItemLevel(3);
		this.setMaxDurability(41);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(2);
		this.setType(ItemType.HELMET);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(9);
		this.setLoreId(0);
		this.setMaterial(Material.LEATHER_HELMET);
		this.setDamageData(0);
		this.addStat(Stats.ARMOR, 3);
		this.setSellPrice(4);
		
		createItemStack();
	}
}