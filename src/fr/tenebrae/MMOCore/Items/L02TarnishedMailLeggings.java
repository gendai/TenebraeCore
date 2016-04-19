package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;

import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L02TarnishedMailLeggings extends Item {

	public L02TarnishedMailLeggings(NBTTagCompound nbt) {
		super(nbt);
	}

	public L02TarnishedMailLeggings() {
		this.setAmount(1);
		this.setId(15);
		this.setItemLevel(3);
		this.setMaxDurability(49);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(2);
		this.setType(ItemType.LEGGINGS);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(15);
		this.setLoreId(0);
		this.setMaterial(Material.CHAINMAIL_LEGGINGS);
		this.setDamageData(0);
		this.addStat(Stats.ARMOR, 5);
		this.setSellPrice(7);
		
		createItemStack();
	}
}