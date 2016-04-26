package fr.tenebrae.MMOCore.Items.Mage;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.tenebrae.MMOCore.Items.IEquipableItem;
import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L01ApprenticeRobe extends Item implements IEquipableItem {

	public L01ApprenticeRobe(NBTTagCompound nbt) {
		super(nbt);
	}

	public L01ApprenticeRobe() {
		this.setAmount(1);
		this.setId(6);
		this.setItemLevel(1);
		this.setMaxDurability(38);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(0);
		this.setType(ItemType.CHESTPLATE);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(6);
		this.setLoreId(0);
		this.setMaterial(Material.GOLD_CHESTPLATE);
		this.setDamageData(0);
		this.addStat(Stats.ARMOR, 1.0);
		
		createItemStack();
	}

	@Override
	public void onEquip(Player player) {
		
	}
}