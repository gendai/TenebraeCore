package fr.tenebrae.MMOCore.Items;

import java.lang.reflect.Field;

import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import fr.tenebrae.MMOCore.Items.Components.ItemType;

public class ItemUtils {

	public static boolean isMMOItem(ItemStack is) {
		return isMMOItem(asNMS(is));
	}
	
	public static boolean isMMOItem(net.minecraft.server.v1_9_R1.ItemStack is) {
		if (is == null) return false;
		NBTTagCompound nbt = new NBTTagCompound();
		is.save(nbt);
		if (!nbt.hasKey("tag")) return false;
		NBTTagCompound tag = nbt.getCompound("tag");
		if (tag.hasKey("MMOTags")) return true;
		else return false;
	}
	
	public static net.minecraft.server.v1_9_R1.ItemStack asNMS(org.bukkit.inventory.ItemStack original) {
		if (original == null) return null;
		try {
			CraftItemStack stack = (CraftItemStack)original;
			Field f;
			f = stack.getClass().getDeclaredField("handle");
			f.setAccessible(true);
			return (net.minecraft.server.v1_9_R1.ItemStack) f.get(stack);
		} catch (Exception e) { e.printStackTrace(); return null; }
	}
	
	public static net.minecraft.server.v1_9_R1.ItemStack asNMS(CraftItemStack stack) {
		if (stack == null) return null;
		try {
			Field f;
			f = stack.getClass().getDeclaredField("handle");
			f.setAccessible(true);
			return (net.minecraft.server.v1_9_R1.ItemStack) f.get(stack);
		} catch (Exception e) { e.printStackTrace(); return null; }
	}
	
	public static boolean isClickableItem(Item item) {
		if (item == null) return false;
		if (ItemRegistry.getItem(item.getId()) instanceof IClickableItem) return true;
		else return false;
	}
	
	public static boolean isUsableItem(Item item) {
		if (item == null) return false;
		if (ItemRegistry.getItem(item.getId()) instanceof IUsableItem) return true;
		else return false;
	}
	
	public static boolean isWeaponItem(Item item) {
		if (item == null) return false;
		if (ItemRegistry.getItem(item.getId()) instanceof IWeaponItem) return true;
		else return false;
	}
	
	public static boolean isEquipableItem(Item item) {
		if (item == null) return false;
		if (ItemRegistry.getItem(item.getId()) instanceof IEquipableItem) return true;
		else return false;
	}
	
	public static boolean isCoinItem(Item item) {
		if (item == null) return false;
		if (item.getId() == 997 || item.getId() == 998 || item.getId() == 999) return true;
		else return false;
	}
	
	public static boolean isOffhandItem(Item item) {
		if (item == null) return false;
		if (ItemRegistry.getItem(item.getId()).getType() == ItemType.OFFHAND) return true;
		else return false;
	}
}
