package fr.tenebrae.MMOCore.Bags;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.Items.ItemUtils;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Utils.ItemStackBuilder;
import fr.tenebrae.MMOCore.Utils.Serializers.InventorySerializer;

public class Bag {

	public static ItemStack LOCKER = new ItemStackBuilder()
											.withMaterial(Material.STAINED_GLASS_PANE)
											.withDurability(7)
											.withDisplayName("§r")
											.build();
	private Inventory content;
	public int size = 0;

	public Bag(int size) {
		this.size = size;
		int invSize = (size/9);
		if (invSize%9 != 0) invSize = 9*invSize+9;
		Inventory inv = Bukkit.createInventory(null, invSize, " ");
		if (size%9 != 0) {
			for (int k = 0; k < size%9; k++) {
				inv.setItem(inv.getSize()-k-1, getLocker());
			}
		}
		this.content = inv;
		this.content.setMaxStackSize(1);
	}
	
	public Bag(int size, String serializedInventory) {
		this.size = size;
		int invSize = (size/9);
		if (invSize%9 != 0) invSize = 9*invSize+9;
		try {
			this.content = InventorySerializer.fromBase64(serializedInventory);
		} catch (IOException e) {
			Inventory inv = Bukkit.createInventory(null, invSize, " ");
			if (size%9 != 0) {
				for (int k = 0; k < size%9; k++) {
					inv.setItem(inv.getSize()-k-1, getLocker());
				}
			}
			this.content = inv;
		}
		this.content.setMaxStackSize(1);
	}
	
	public Bag(String serializedBag) {
		try {
			String[] infos = serializedBag.split("@#@");
			this.size = Integer.valueOf(infos[0]);
			this.content = InventorySerializer.fromBase64(infos[1]);
			this.content.setMaxStackSize(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setItems(ItemStack... items) {
		this.clear();
		this.addItem(items);
	}
	
	public void addItem(ItemStack... items) {
		if (this.isFull()) return;
		for (ItemStack is : items) {
			if (this.isFull()) break;
			
			if (is != null) this.content.addItem(is);
		}
	}
	
	public void clear() {
		this.content.clear();
		if (size%9 != 0) {
			for (int k = 0; k < size%9; k++) {
				content.setItem(content.getSize()-k-1, getLocker());
			}
		}
	}
	
	public boolean isFull() {
		for (ItemStack i: this.content.getContents()) {
			if (i == null) return false;
		}
		
		return true;
	}
	
	public Inventory getInventory() {
		return this.content;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public static ItemStack getLocker() {
		return Bag.LOCKER.clone();
	}
	
	@Override
	public String toString() {
		return this.size+"@#@"+InventorySerializer.toBase64(this.content);
	}
	
	public void display(Inventory sheet) {
		int k = 5;
		int i = 0;
		int j = 0;
		for (ItemStack is : this.content.getContents()) {
			if (ItemUtils.isMMOItem(is)) {
				Item item = new Item(is);
				if (ItemUtils.isEquipableItem(item) || item.getType() == ItemType.WEAPON || item.getType() == ItemType.OFFHAND) {
					sheet.setItem(k, is);
					k++;
					i++;
					if (i >= 4) {
						k += 5;
						i = 0;
					}
					if (k > 53) return;
				}
			}
		}
		for(j = k; j < 53; j++) {
			sheet.setItem(j, null);
			i++;
			if (i >= 4) {
				j += 5;
				i = 0;
			}
		}
	}
}
