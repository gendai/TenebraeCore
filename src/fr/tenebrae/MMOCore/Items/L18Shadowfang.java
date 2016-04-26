package fr.tenebrae.MMOCore.Items;

import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.tenebrae.MMOCore.Items.Components.ItemQuality;
import fr.tenebrae.MMOCore.Items.Components.ItemType;
import fr.tenebrae.MMOCore.Items.Components.WeaponPlace;
import fr.tenebrae.MMOCore.Items.Components.WeaponType;
import fr.tenebrae.MMOCore.Mechanics.Stats;
import fr.tenebrae.MMOCore.Utils.ParticleEffect;

public class L18Shadowfang extends Item implements IWeaponItem {

	public L18Shadowfang(NBTTagCompound nbt) {
		super(nbt);
	}

	public L18Shadowfang() {
		this.setAmount(1);
		this.setId(18);
		this.setItemLevel(24);
		this.setMaxDurability(31);
		this.setDurability(this.getMaxDurability());
		this.setLevelRequired(18);
		this.setType(ItemType.WEAPON);
		this.setWeaponType(WeaponType.SWORD_1H);
		this.setWeaponPlace(WeaponPlace.BOTH);
		this.setQuality(ItemQuality.RARE);
		this.setNameId(18);
		this.setLoreId(0);
		this.setMaterial(Material.STONE_SWORD);
		this.setDamageData(0);
		this.addStat(Stats.ATTACK_SPEED, 1150.0);
		this.addStat(Stats.STRENGTH, 5.0);
		this.addStat(Stats.AGILITY, 5.0);
		this.addStat(Stats.POWER, 3.0);
		this.addStat(Stats.CRITICAL_CHANCE, 12.0);
		this.setMinDmg(16);
		this.setMaxDmg(30);
		this.setSellPrice(2964);
		
		createItemStack();
	}

	@Override
	public void onDealDamage(Player damager, Entity damaged, int damage, double distance, boolean critical, boolean success) {
		ParticleEffect.SPELL_WITCH.display(0.1F, 0.1F, 0.1F, 0.1F, 10, damaged.getBukkitEntity().getLocation(), 32);
	}
}