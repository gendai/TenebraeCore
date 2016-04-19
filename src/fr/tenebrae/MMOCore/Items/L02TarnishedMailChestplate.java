package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;

import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L02TarnishedMailChestplate extends Item {

	public L02TarnishedMailChestplate(NBTTagCompound nbt) {
		super(nbt);
	}

	public L02TarnishedMailChestplate() {
		this.setAmount(1);
		this.setId(13);
		this.setItemLevel(3);
		this.setMaxDurability(52);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(2);
		this.setType(ItemType.CHESTPLATE);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(13);
		this.setLoreId(0);
		this.setMaterial(Material.CHAINMAIL_CHESTPLATE);
		this.setDamageData(0);
		this.addStat(Stats.ARMOR, 6);
		this.setSellPrice(7);
		
		createItemStack();
	}
}