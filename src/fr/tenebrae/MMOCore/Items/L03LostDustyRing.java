package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Mechanics.Stats;

public class L03LostDustyRing extends Item implements IEquipableItem {

	public L03LostDustyRing(NBTTagCompound nbt) {
		super(nbt);
	}

	public L03LostDustyRing() {
		this.setAmount(1);
		this.setId(19);
		this.setItemLevel(7);
		this.setMaxDurability(0);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(2);
		this.setType(ItemType.RING);
		this.setQuality(ItemQuality.INHABITUEL);
		this.setNameId(19);
		this.setLoreId(0);
		this.setMaterial(Material.RABBIT);
		this.setDamageData(0);
		this.addStat(Stats.STRENGTH, 1.0);
		this.addStat(Stats.STAMINA, 1.0);
		this.addStat(Stats.AGILITY, 1.0);
		this.addStat(Stats.INTELLIGENCE, 1.0);
		this.addStat(Stats.SPIRIT, 1.0);
		this.setSellPrice(102);
		
		createItemStack();
	}

	@Override
	public void onEquip(Player player) {
		
	}
}
