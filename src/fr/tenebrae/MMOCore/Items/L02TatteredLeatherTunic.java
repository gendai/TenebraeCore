package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L02TatteredLeatherTunic extends Item implements IEquipableItem {

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
		this.addStat(Stats.ARMOR, 5.0);
		this.setSellPrice(5);
		
		createItemStack();
	}

	@Override
	public void onEquip(Player player) {
		// TODO Auto-generated method stub
		
	}
}