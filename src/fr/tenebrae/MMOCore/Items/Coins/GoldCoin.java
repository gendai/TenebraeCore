package fr.tenebrae.MMOCore.Items.Coins;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;

import fr.tenebrae.MMOCore.Items.ICoinItem;
import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;

public class GoldCoin extends Item implements ICoinItem {

	private int coinAmount = 1;
	
	public GoldCoin(NBTTagCompound nbt) {
		super(nbt);
	}

	public GoldCoin(int amount) {
		this.setAmount(1);
		this.setId(999);
		this.setItemLevel(1);
		this.setMaxDurability(1);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(0);
		this.setType(ItemType.OTHER);
		this.setQuality(ItemQuality.COMMUN);
		this.setNameId(999);
		this.setLoreId(0);
		this.setMaterial(Material.IRON_INGOT);
		this.setDamageData(0);
		this.setSellPrice(1);
		this.setCoinAmount(amount);
		
		createItemStack();
	}

	@Override
	public void setCoinAmount(int amount) {
		if (amount > 99) amount = 99;
		this.coinAmount = amount;
		this.setAmount(amount);
	}
	
	@Override
	public int getCoinAmount() {
		return this.coinAmount;
	}
}
