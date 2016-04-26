package fr.tenebrae.MMOCore.Items.Warrior;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.tenebrae.MMOCore.Items.IEquipableItem;
import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L01RecruitShirt extends Item implements IEquipableItem {

	public L01RecruitShirt(NBTTagCompound nbt) {
		super(nbt);
	}

	public L01RecruitShirt() {
		this.setAmount(1);
		this.setId(2);
		this.setItemLevel(1);
		this.setMaxDurability(38);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(0);
		this.setType(ItemType.CHESTPLATE);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(2);
		this.setLoreId(0);
		this.setMaterial(Material.LEATHER_CHESTPLATE);
		this.setDamageData(0);
		this.addStat(Stats.ARMOR, 2.0);
		
		createItemStack();
	}

	@Override
	public void onEquip(Player player) {
		
	}
}