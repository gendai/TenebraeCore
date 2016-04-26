package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L02TarnishedMailBoots extends Item implements IEquipableItem {

	public L02TarnishedMailBoots(NBTTagCompound nbt) {
		super(nbt);
	}

	public L02TarnishedMailBoots() {
		this.setAmount(1);
		this.setId(12);
		this.setItemLevel(3);
		this.setMaxDurability(44);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(2);
		this.setType(ItemType.BOOTS);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(12);
		this.setLoreId(0);
		this.setMaterial(Material.CHAINMAIL_BOOTS);
		this.setDamageData(0);
		this.addStat(Stats.ARMOR, 3.0);
		this.setSellPrice(4);
		
		createItemStack();
	}

	@Override
	public void onEquip(Player player) {
		
	}
}