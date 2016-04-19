package fr.tenebrae.MMOCore.Entities.Pathfinders;

import net.minecraft.server.v1_9_R1.EntityCreature;
import net.minecraft.server.v1_9_R1.PathfinderGoal;
import net.minecraft.server.v1_9_R1.Vec3D;

import org.bukkit.Location;

import fr.tenebrae.MMOCore.Entities.RandomPositionGenerator;

public class PathfinderGoalRandomStroll extends PathfinderGoal
{
	private EntityCreature a;
	private double b;
	private double c;
	private double d;
	private double speed;
	private int timeBetweenStrolls;
	private boolean g;
	private Location spawn;

	public PathfinderGoalRandomStroll(EntityCreature paramEntityCreature, Location spawn, double speed)
	{
		this(paramEntityCreature, spawn, speed, 120);
	}

	public PathfinderGoalRandomStroll(EntityCreature paramEntityCreature, Location spawn, double speed, int timeBetweenStrolls) {
		this.a = paramEntityCreature;
		this.speed = speed;
		this.timeBetweenStrolls = timeBetweenStrolls;
		this.spawn = spawn;
		a(1);
	}

	public boolean a()
	{
		if (!this.g) {
			if (this.a.bK() >= 100) {
				return false;
			}
			if (this.a.getRandom().nextInt(this.timeBetweenStrolls) != 0) {
				return false;
			}
		}

		Vec3D localVec3D = RandomPositionGenerator.getRandomPosition(this.a, this.spawn, 10, 7);
		if (localVec3D == null) {
			return false;
		}
		this.b = localVec3D.x;
		this.c = localVec3D.y;
		this.d = localVec3D.z;
		this.g = false;
		return true;
	}

	public boolean b()
	{
		return !this.a.getNavigation().n();
	}

	public void c()
	{
		this.a.getNavigation().a(this.b, this.c, this.d, this.speed);
	}

	public void f() {
		this.g = true;
	}

	public void setTimeBetweenMovement(int timeBetweenStrolls) {
		this.timeBetweenStrolls = timeBetweenStrolls;
	}
}