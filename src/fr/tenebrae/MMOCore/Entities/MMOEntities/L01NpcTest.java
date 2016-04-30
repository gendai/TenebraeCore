package fr.tenebrae.MMOCore.Entities.MMOEntities;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;

import fr.tenebrae.MMOCore.Entities.QuestNpc;

public class L01NpcTest extends QuestNpc{

	public L01NpcTest(Location loc) {
		super(((CraftWorld)loc.getWorld()).getHandle());
		this.spawn = loc;
		
		this.health = 16;
		this.maxHealth = 16;
		this.attackSpeed = 1000;
		this.lastAttackDate = null;
		this.atkMin = 1;
		this.atkMax = 3;
		this.level = 1;
		this.target = null;
		this.lastDamager = null;
		this.givenXp = 4;
		this.attackRange = 1;
		this.name = "Quest NPC";

		this.walkSpeed = 0D;
		this.sprintSpeed = 0D;
		this.resetSpeed = 0D;

		this.hurtSound = "mob.villager.hurt";
		this.idleSound = "mob.villager.idle";
		this.stepSound = "mob.villager.step";
		this.deathSound = "mob.villager.death";
	}
	

}
