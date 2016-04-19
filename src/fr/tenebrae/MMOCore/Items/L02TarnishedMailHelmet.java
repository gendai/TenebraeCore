package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;

import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L02TarnishedMailHelmet extends Item {

	public L02TarnishedMailHelmet(NBTTagCompound nbt) {
		super(nbt);
	}

	public L02TarnishedMailHelmet() {
		this.setAmount(1);
		this.setId(14);
		this.setItemLevel(3);
		this.setMaxDurability(44);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(2);
		this.setType(ItemType.HELMET);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(14);
		this.setLoreId(0);
		this.setMaterial(Material.CHAINMAIL_HELMET);
		this.setDamageData(0);
		this.addStat(Stats.ARMOR, 4);
		this.setSellPrice(5);
		
		createItemStack();
	}
}