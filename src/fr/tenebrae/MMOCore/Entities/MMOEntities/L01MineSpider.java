package fr.tenebrae.MMOCore.Entities.MMOEntities;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;

import fr.tenebrae.MMOCore.Entities.MMOCaveSpider;
import fr.tenebrae.MMOCore.Items.ItemRegistry;
import fr.tenebrae.MMOCore.Items.Coins.CopperCoin;

public class L01MineSpider extends MMOCaveSpider {

	public L01MineSpider(Location loc) {
		super(((CraftWorld)loc.getWorld()).getHandle());
		this.spawn = loc;

		this.drops.put(ItemRegistry.getItem(8), 1.5);
		this.drops.put(ItemRegistry.getItem(9), 1.5);
		this.drops.put(ItemRegistry.getItem(10), 1.5);
		this.drops.put(ItemRegistry.getItem(11), 1.5);
		this.drops.put(ItemRegistry.getItem(12), 1.5);
		this.drops.put(ItemRegistry.getItem(13), 1.5);
		this.drops.put(ItemRegistry.getItem(14), 1.5);
		this.drops.put(ItemRegistry.getItem(15), 1.5);
		this.drops.put(ItemRegistry.getItem(16), 0.5);
		this.drops.put(ItemRegistry.getItem(17), 0.5);
		this.drops.put(ItemRegistry.getItem(19), 1.5);
		this.drops.put(ItemRegistry.getItem(20), 1.5);
		this.drops.put(ItemRegistry.getItem(21), 1.5);
		this.drops.put(new CopperCoin(new Random().nextInt(5)+1), 80.0);
		
		this.maxHealth = 41;
		this.attackSpeed = 1250;
		this.atkMin = 1;
		this.atkMin = 3;
		this.level = 1;
		this.givenXp = 12;
		this.attackRange = 1;
		this.nameId = 300000;
		this.walkSpeed = 0.24786667640209197000000000000001D;
		this.sprintSpeed = 0.37786667640209197000000000000001D;
		this.resetSpeed = 0.47786667640209197000000000000001D;

		this.hurtSound = "mob.littleSpider.hurt";
		this.aggroSound = "mob.littleSpider.aggro";
		this.attackSound = "mob.littleSpider.attack";
		this.idleSound = "mob.littleSpider.idle";
		this.stepSound = "mob.littleSpider.step";
		this.deathSound = "mob.littleSpider.death";
		
		this.clearGoals();
		this.setupGoals();
	}
}
