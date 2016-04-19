package fr.tenebrae.MMOCore.Entities;

import java.util.Random;

import org.bukkit.Location;

import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.EntityCreature;
import net.minecraft.server.v1_9_R1.MathHelper;
import net.minecraft.server.v1_9_R1.NavigationAbstract;
import net.minecraft.server.v1_9_R1.Vec3D;

public class RandomPositionGenerator {
	
	private static Vec3D a = new Vec3D(0.0D, 0.0D, 0.0D);

	public static Vec3D getRandomPosition(EntityCreature paramEntityCreature, int distanceXZmax, int distanceYmax) {
		return getRandomPosition(paramEntityCreature, distanceXZmax, distanceYmax, null);
	}
	
	public static Vec3D getRandomPosition(EntityCreature paramEntityCreature, Location loc, int distanceXZmax, int distanceYmax) {
		return getRandomPosition(paramEntityCreature, loc, distanceXZmax, distanceYmax, null);
	}

	public static Vec3D a(EntityCreature paramEntityCreature, int distanceXZmax, int distanceYmax, Vec3D paramVec3D) {
		a = paramVec3D.a(paramEntityCreature.locX, paramEntityCreature.locY, paramEntityCreature.locZ);
		return getRandomPosition(paramEntityCreature, distanceXZmax, distanceYmax, a);
	}

	public static Vec3D b(EntityCreature paramEntityCreature, int distanceXZmax, int distanceYmax, Vec3D paramVec3D) {
		a = new Vec3D(paramEntityCreature.locX, paramEntityCreature.locY, paramEntityCreature.locZ).d(paramVec3D);
		return getRandomPosition(paramEntityCreature, distanceXZmax, distanceYmax, a);
	}

	private static Vec3D getRandomPosition(EntityCreature paramEntityCreature, int distanceXZmax, int distanceYmax, Vec3D paramVec3D) {
		NavigationAbstract localNavigationAbstract = paramEntityCreature.getNavigation();
		Random localRandom = paramEntityCreature.getRandom();
		int i = 0;
		int j = 0; int k = 0; int m = 0;
		float f1 = -99999.0F;
		int n;
		if (paramEntityCreature.cY()) {
			double d1 = paramEntityCreature.cV().distanceSquared(MathHelper.floor(paramEntityCreature.locX), MathHelper.floor(paramEntityCreature.locY), MathHelper.floor(paramEntityCreature.locZ)) + 4.0D;
			double d2 = paramEntityCreature.cW() + distanceXZmax;
			n = d1 < d2 * d2 ? 1 : 0;
		} else {
			n = 0;
		}

		for (int i1 = 0; i1 < 10; i1++) {
			int i2 = localRandom.nextInt(2 * distanceXZmax + 1) - distanceXZmax;
			int i3 = localRandom.nextInt(2 * distanceYmax + 1) - distanceYmax;
			int i4 = localRandom.nextInt(2 * distanceXZmax + 1) - distanceXZmax;

			if ((paramVec3D == null) || (i2 * paramVec3D.x + i4 * paramVec3D.z >= 0.0D))
			{
				if ((paramEntityCreature.cY()) && (distanceXZmax > 1)) {
					BlockPosition localBlockPosition = paramEntityCreature.cV();
					if (paramEntityCreature.locX > localBlockPosition.getX())
						i2 -= localRandom.nextInt(distanceXZmax / 2);
					else {
						i2 += localRandom.nextInt(distanceXZmax / 2);
					}
					if (paramEntityCreature.locZ > localBlockPosition.getZ())
						i4 -= localRandom.nextInt(distanceXZmax / 2);
					else {
						i4 += localRandom.nextInt(distanceXZmax / 2);
					}
				}

				i2 += MathHelper.floor(paramEntityCreature.locX);
				i3 += MathHelper.floor(paramEntityCreature.locY);
				i4 += MathHelper.floor(paramEntityCreature.locZ);

				BlockPosition localBlockPosition = new BlockPosition(i2, i3, i4);

				if (((n == 0) || (paramEntityCreature.f(localBlockPosition))) && (localNavigationAbstract.b(localBlockPosition)))
				{
					float f2 = paramEntityCreature.a(localBlockPosition);
					if (f2 > f1) {
						f1 = f2;
						j = i2;
						k = i3;
						m = i4;
						i = 1;
					}
				}
			}
		}
		if (i != 0) {
			return new Vec3D(j, k, m);
		}

		return null;
	}

	private static Vec3D getRandomPosition(EntityCreature paramEntityCreature, Location loc, int distanceXZmax, int distanceYmax, Vec3D paramVec3D) {
		NavigationAbstract localNavigationAbstract = paramEntityCreature.getNavigation();
		Random localRandom = paramEntityCreature.getRandom();
		int i = 0;
		int j = 0; int k = 0; int m = 0;
		float f1 = -99999.0F;
		int n;
		if (paramEntityCreature.cY()) {
			double d1 = paramEntityCreature.cV().distanceSquared(MathHelper.floor(loc.getX()), MathHelper.floor(loc.getY()), MathHelper.floor(loc.getZ())) + 4.0D;
			double d2 = paramEntityCreature.cW() + distanceXZmax;
			n = d1 < d2 * d2 ? 1 : 0;
		} else {
			n = 0;
		}

		for (int i1 = 0; i1 < 10; i1++) {
			int i2 = localRandom.nextInt(2 * distanceXZmax + 1) - distanceXZmax;
			int i3 = localRandom.nextInt(2 * distanceYmax + 1) - distanceYmax;
			int i4 = localRandom.nextInt(2 * distanceXZmax + 1) - distanceXZmax;

			if ((paramVec3D == null) || (i2 * paramVec3D.x + i4 * paramVec3D.z >= 0.0D))
			{
				if ((paramEntityCreature.cY()) && (distanceXZmax > 1)) {
					BlockPosition localBlockPosition = paramEntityCreature.cV();
					if (loc.getX() > localBlockPosition.getX())
						i2 -= localRandom.nextInt(distanceXZmax / 2);
					else {
						i2 += localRandom.nextInt(distanceXZmax / 2);
					}
					if (loc.getZ() > localBlockPosition.getZ())
						i4 -= localRandom.nextInt(distanceXZmax / 2);
					else {
						i4 += localRandom.nextInt(distanceXZmax / 2);
					}
				}

				i2 += MathHelper.floor(loc.getX());
				i3 += MathHelper.floor(loc.getY());
				i4 += MathHelper.floor(loc.getZ());

				BlockPosition localBlockPosition = new BlockPosition(i2, i3, i4);

				if (((n == 0) || (paramEntityCreature.f(localBlockPosition))) && (localNavigationAbstract.b(localBlockPosition)))
				{
					float f2 = paramEntityCreature.a(localBlockPosition);
					if (f2 > f1) {
						f1 = f2;
						j = i2;
						k = i3;
						m = i4;
						i = 1;
					}
				}
			}
		}
		if (i != 0) {
			return new Vec3D(j, k, m);
		}

		return null;
	}
}