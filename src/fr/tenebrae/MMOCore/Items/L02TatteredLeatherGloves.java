package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L02TatteredLeatherGloves extends Item implements IEquipableItem {

	public L02TatteredLeatherGloves(NBTTagCompound nbt) {
		super(nbt);
	}

	public L02TatteredLeatherGloves() {
		this.setAmount(1);
		this.setId(20);
		this.setItemLevel(3);
		this.setMaxDurability(42);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(2);
		this.setType(ItemType.GLOVES);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(20);
		this.setLoreId(0);
		this.setMaterial(Material.CLAY_BALL);
		this.setDamageData(0);
		this.addStat(Stats.ARMOR, 3.0);
		this.setSellPrice(4);
		
		createItemStack();
	}

	@Override
	public void onEquip(Player player) {
		// TODO Auto-generated method stub
		
	}
}