package fr.tenebrae.MMOCore.Characters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tenebrae.MMOCore.Items.Item;
import fr.tenebrae.MMOCore.Items.ItemUtils;
import fr.tenebrae.MMOCore.Utils.ItemStackBuilder;
import fr.tenebrae.MMOCore.Utils.TranslatedString;
import fr.tenebrae.MMOCore.Utils.Serializers.ItemStackSerializer;

public class CharacterEquipment {

	private Item helmet;
	private Item chestplate;
	private Item leggings;
	private Item gloves;
	private Item boots;
	private Item necklace;
	private Item ring1;
	private Item ring2;
	
	private Character character;

	public CharacterEquipment(Character c) {
		this.character = c;
	}
	
	public CharacterEquipment(Character c, String serialized) {
		this.character = c;
		
		List<Item> list = new ArrayList<Item>();
		for (String sEquipment : serialized.split("#@#")) {
			if (!sEquipment.equals("null")) {
				list.add(new Item(ItemStackSerializer.fromBase64(sEquipment), true));
			} else {
				list.add(null);
			}
		}

		setHelmet(list.get(0), false);
		setNecklace(list.get(1), false);
		setChestplate(list.get(2), false);
		setGloves(list.get(3), false);
		setFirstRing(list.get(4), false);
		setSecondRing(list.get(5), false);
		setLeggings(list.get(6), false);
		setBoots(list.get(7), false);
		setWeapon(list.get(8), false);
		setOffhand(list.get(9), false);
	}

	public Item getHelmet() {
		return helmet;
	}

	public void setHelmet(Item helmet) {
		setHelmet(helmet, true);
	}

	public void setHelmet(Item helmet, boolean update) {
		if (helmet == null) {
			this.helmet = null;
			this.character.getAccount().getInventory().setHelmet(null);
			this.character.updateStats();
			return;
		}
		if (!ItemUtils.isEquipableItem(helmet)) return;
		this.helmet = helmet;
		this.character.getAccount().getInventory().setHelmet(helmet.getItemStack());
		if (update) this.character.updateStats();
	}

	public Item getChestplate() {
		return chestplate;
	}

	public void setChestplate(Item chestplate) {
		setChestplate(chestplate, true);
	}

	public void setChestplate(Item chestplate, boolean update) {
		if (chestplate == null) {
			this.chestplate = null;
			this.character.getAccount().getInventory().setChestplate(null);
			this.character.updateStats();
			return;
		}
		if (!ItemUtils.isEquipableItem(chestplate)) return;
		this.chestplate = chestplate;
		this.character.getAccount().getInventory().setChestplate(chestplate.getItemStack());
		if (update) this.character.updateStats();
	}

	public Item getLeggings() {
		return leggings;
	}

	public void setLeggings(Item leggings) {
		setLeggings(leggings, true);
	}

	public void setLeggings(Item leggings, boolean update) {
		if (leggings == null) {
			this.leggings = null;
			this.character.getAccount().getInventory().setLeggings(null);
			this.character.updateStats();
			return;
		}
		if (!ItemUtils.isEquipableItem(leggings)) return;
		this.leggings = leggings;
		this.character.getAccount().getInventory().setLeggings(leggings.getItemStack());
		if (update) this.character.updateStats();
	}

	public Item getGloves() {
		return gloves;
	}

	public void setGloves(Item gloves) {
		setGloves(gloves, true);
	}

	public void setGloves(Item gloves, boolean update) {
		if (gloves == null) {
			this.gloves = null;
			this.character.updateStats();
			return;
		}
		if (!ItemUtils.isEquipableItem(gloves)) return;
		this.gloves = gloves;
		if (update) this.character.updateStats();
	}

	public Item getBoots() {
		return boots;
	}

	public void setBoots(Item boots) {
		setBoots(boots, true);
	}

	public void setBoots(Item boots, boolean update) {
		if (boots == null) {
			this.boots = null;
			this.character.getAccount().getInventory().setBoots(null);
			this.character.updateStats();
			return;
		}
		if (!ItemUtils.isEquipableItem(boots)) return;
		this.boots = boots;
		this.character.getAccount().getInventory().setBoots(boots.getItemStack());
		if (update) this.character.updateStats();
	}

	public Item getNecklace() {
		return necklace;
	}

	public void setNecklace(Item necklace) {
		setNecklace(necklace, true);
	}

	public void setNecklace(Item necklace, boolean update) {
		if (necklace == null) {
			this.necklace = null;
			this.character.updateStats();
			return;
		}
		if (!ItemUtils.isEquipableItem(necklace)) return;
		this.necklace = necklace;
		if (update) this.character.updateStats();
	}

	public Item getFirstRing() {
		return ring1;
	}

	public void setFirstRing(Item ring1) {
		setFirstRing(ring1, true);
	}

	public void setFirstRing(Item ring1, boolean update) {
		if (ring1 == null) {
			this.ring1 = null;
			this.character.updateStats();
			return;
		}
		if (!ItemUtils.isEquipableItem(ring1)) return;
		this.ring1 = ring1;
		if (update) this.character.updateStats();
	}

	public Item getSecondRing() {
		return ring2;
	}

	public void setSecondRing(Item ring2) {
		setSecondRing(ring2, true);
	}

	public void setSecondRing(Item ring2, boolean update) {
		if (ring2 == null) {
			this.ring2 = null;
			this.character.updateStats();
			return;
		}
		if (!ItemUtils.isEquipableItem(ring2)) return;
		this.ring2 = ring2;
		if (update) this.character.updateStats();
	}

	public Item getWeapon() {
		ItemStack is = this.character.getAccount().getInventory().getItem(0);
		if (is == null) return null;
		if (ItemUtils.isMMOItem(is)) {
			Item item = new Item(is);
			if (ItemUtils.isWeaponItem(item)) return item;
			else return null;
		} else return null;
	}
	
	public Item getOffhand() {
		ItemStack is = this.character.getAccount().getInventory().getItemInOffHand();
		if (is == null) return null;
		if (ItemUtils.isMMOItem(is)) {
			Item item = new Item(is);
			if (ItemUtils.isOffhandItem(item)) return item;
			else if (this.character.canDualWield()) {
				if (ItemUtils.isWeaponItem(item)) return item;
				else return null;
			} else return null;
		} else return null;
	}

	public void setWeapon(Item weapon) {
		setWeapon(weapon, true);
	}

	public void setWeapon(Item weapon, boolean update) {
		if (weapon == null) {
			this.character.getAccount().getInventory().setItem(0, null);
			this.character.updateStats();
			return;
		}
		if (!ItemUtils.isWeaponItem(weapon)) return;
		this.character.getAccount().getInventory().setItem(0, weapon.getItemStack());
		if (update) this.character.updateStats();
	}

	public void setOffhand(Item offhand) {
		setOffhand(offhand, true);
	}

	public void setOffhand(Item offhand, boolean update) {
		if (offhand == null) {
			this.character.getAccount().getInventory().setItemInOffHand(null);
			this.character.updateStats();
			return;
		}
		if (!ItemUtils.isWeaponItem(offhand) && !ItemUtils.isOffhandItem(offhand)) return;
		this.character.getAccount().getInventory().setItemInOffHand(offhand.getItemStack());
		if (update) this.character.updateStats();
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}
	
	public void display(Inventory inv) {
		if (inv.getSize() < 54) return;
		
		if (this.helmet != null) {
			inv.setItem(0, this.helmet.getItemStack());
		} else {
			inv.setItem(0, new ItemStackBuilder()
					.withMaterial(Material.BEETROOT)
					.withDisplayName("§f"+TranslatedString.getString(30000, character.getAccount()))
					.build());
		}
		if (this.necklace != null) {
			inv.setItem(9, this.necklace.getItemStack());
		} else {
			inv.setItem(9, new ItemStackBuilder()
					.withMaterial(Material.RABBIT_FOOT)
					.withDisplayName("§f"+TranslatedString.getString(30004, character.getAccount()))
					.build());
		}
		if (this.chestplate != null) {
			inv.setItem(18, this.chestplate.getItemStack());
		} else {
			inv.setItem(18, new ItemStackBuilder()
					.withMaterial(Material.RABBIT_STEW)
					.withDisplayName("§f"+TranslatedString.getString(30001, character.getAccount()))
					.build());
		}
		if (this.ring1 != null) {
			inv.setItem(19, this.ring1.getItemStack());
		} else {
			inv.setItem(19, new ItemStackBuilder()
					.withMaterial(Material.BLAZE_POWDER)
					.withDisplayName("§f"+TranslatedString.getString(30005, character.getAccount()))
					.build());
		}
		if (this.gloves != null) {
			inv.setItem(27, this.gloves.getItemStack());
		} else {
			inv.setItem(27, new ItemStackBuilder()
					.withMaterial(Material.GOLDEN_CARROT)
					.withDisplayName("§f"+TranslatedString.getString(30018, character.getAccount()))
					.build());
		}
		if (this.ring2 != null) {
			inv.setItem(28, this.ring2.getItemStack());
		} else {
			inv.setItem(28, new ItemStackBuilder()
					.withMaterial(Material.BLAZE_POWDER)
					.withDisplayName("§f"+TranslatedString.getString(30005, character.getAccount()))
					.build());
		}
		if (this.leggings != null) {
			inv.setItem(36, this.leggings.getItemStack());
		} else {
			inv.setItem(36, new ItemStackBuilder()
					.withMaterial(Material.GHAST_TEAR)
					.withDisplayName("§f"+TranslatedString.getString(30002, character.getAccount()))
					.build());
		}
		if (this.boots != null) {
			inv.setItem(45, this.boots.getItemStack());
		} else {
			inv.setItem(45, new ItemStackBuilder()
					.withMaterial(Material.NETHER_STALK)
					.withDisplayName("§f"+TranslatedString.getString(30003, character.getAccount()))
					.build());
		}
		if (this.getWeapon() != null) {
			inv.setItem(47, this.getWeapon().getItemStack());
		} else {
			inv.setItem(47, new ItemStackBuilder()
			.withMaterial(Material.DIAMOND_SWORD)
			.withDisplayName("§f"+TranslatedString.getString(30019, character.getAccount()))
			.build());
		}
		if (this.getOffhand() != null) {
			inv.setItem(48, this.getOffhand().getItemStack());
		} else {
			inv.setItem(48, new ItemStackBuilder()
			.withMaterial(Material.DIAMOND_HOE)
			.withDisplayName("§f"+TranslatedString.getString(30013, character.getAccount()))
			.build());
		}
	}
	
	public List<Item> asList() {
		List<Item> returned = new ArrayList<Item>();

		returned.add(this.helmet);
		returned.add(this.necklace);
		returned.add(this.chestplate);
		returned.add(this.gloves);
		returned.add(this.ring1);
		returned.add(this.ring2);
		returned.add(this.leggings);
		returned.add(this.boots);
		returned.add(getWeapon());
		returned.add(getOffhand());
		
		return returned;
	}
}
