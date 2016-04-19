package fr.tenebrae.MMOCore.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

/**
 * <p>Helper class for getting targets using various methods</p>
 */
public class TargetHelper {

    /**
     * <p>Number of pixels that end up displaying about 1 degree of vision in the client window</p>
     * <p>Not really useful since you can't get the client's window size, but I added it in case
     * it becomes useful sometime</p>
     */
    @SuppressWarnings("unused")
	private static final int PIXELS_PER_DEGREE = 35;

    /**
     * <p>Gets all entities the player is looking at within the range</p>
     * <p>Has a little bit of tolerance to make targeting easier</p>
     *
     * @param source living entity to get the targets of
     * @param range  maximum range to check
     * @return       all entities in the player's vision line
     */
    public static List<LivingEntity> getLivingTargets(LivingEntity source, double range) {
        return getLivingTargets(source, range, 4);
    }

    /**
     * <p>Gets all entities the player is looking at within the range using
     * the given tolerance.</p>
     *
     * @param source living entity to get the targets of
     * @param range  maximum range to check
     * @param tolerance tolerance of the line calculation
     * @return       all entities in the player's vision line
     */
    public static List<LivingEntity> getLivingTargets(LivingEntity source, double range, double tolerance) {
        List<Entity> list = source.getNearbyEntities(range, range, range);
        List<LivingEntity> targets = new ArrayList<LivingEntity>();

        Vector facing = source.getLocation().getDirection();
        double fLengthSq = facing.lengthSquared();

        for (Entity entity : list) {
            if (!isInFront(source, entity) || !(entity instanceof LivingEntity)) continue;

            Vector relative = entity.getLocation().subtract(source.getLocation()).toVector();
            double dot = relative.dot(facing);
            double rLengthSq = relative.lengthSquared();
            double cosSquared = (dot * dot) / (rLengthSq * fLengthSq);
            double sinSquared = 1 - cosSquared;
            double dSquared = rLengthSq * sinSquared;

            // If close enough to vision line, return the entity
            if (dSquared < 4) targets.add((LivingEntity)entity);
        }

        return targets;
    }

    /**
     * <p>Gets the entity the player is looking at</p>
     * <p>Has a little bit of tolerance to make targeting easier</p>
     *
     * @param source living entity to get the target of
     * @param range  maximum range to check
     * @return       entity player is looing at or null if not found
     */
    public static LivingEntity getLivingTarget(LivingEntity source, double range)
    {
        return getLivingTarget(source, range, 4);
    }

    /**
     * <p>Gets the entity the player is looking at</p>
     * <p>Has a little bit of tolerance to make targeting easier</p>
     *
     * @param source living entity to get the target of
     * @param range  maximum range to check
     * @param tolerance tolerance of the line calculation
     * @return       entity player is looing at or null if not found
     */
    public static LivingEntity getLivingTarget(LivingEntity source, double range, double tolerance) {
        List<LivingEntity> targets = getLivingTargets(source, range, tolerance);
        if (targets.size() == 0) return null;
        LivingEntity target = targets.get(0);
        double minDistance = target.getLocation().distanceSquared(source.getLocation());
        for (LivingEntity entity : targets) {
            double distance = entity.getLocation().distanceSquared(source.getLocation());
            if (distance < minDistance) {
                minDistance = distance;
                target = entity;
            }
        }
        return target;
    }

    /**
     * Gets the targets in a cone
     *
     * @param source entity to get the targets for
     * @param arc    arc angle of the cone
     * @param range  range of the cone
     * @return       list of targets
     */
    public static List<LivingEntity> getConeTargets(LivingEntity source, double arc, double range) {
        List<LivingEntity> targets = new ArrayList<LivingEntity>();
        List<Entity> list = source.getNearbyEntities(range, range, range);
        if (arc <= 0) return targets;

        // Initialize values
        Vector dir = source.getLocation().getDirection();
        dir.setY(0);
        double cos = Math.cos(arc * Math.PI / 180);
        double cosSq = cos * cos;
        double dirSq = dir.lengthSquared();

        // Get the targets in the cone
        for (Entity entity : list) {
            if (entity instanceof LivingEntity) {

                // Greater than 360 degrees is all targets
                if (arc >= 360) {
                    targets.add((LivingEntity)entity);
                }

                // Otherwise, select targets based on dot product
                else {
                    Vector relative = entity.getLocation().subtract(source.getLocation()).toVector();
                    relative.setY(0);
                    double dot = relative.dot(dir);
                    double value = dot * dot / (dirSq * relative.lengthSquared());
                    if (arc < 180 && dot > 0 && value >= cosSq) targets.add((LivingEntity)entity);
                    else if (arc >= 180 && (dot > 0 || dot <= cosSq)) targets.add((LivingEntity)entity);
                }
            }
        }

        return targets;
    }

    /**
     * Checks if the entity is in front of the entity
     *
     * @param entity entity to check for
     * @param target target to check against
     * @return       true if the target is in front of the entity
     */
    public static boolean isInFront(Entity entity, Entity target) {

        // Get the necessary vectors
        Vector facing = entity.getLocation().getDirection();
        Vector relative = target.getLocation().subtract(entity.getLocation()).toVector();

        // If the dot product is positive, the target is in front
        return facing.dot(relative) >= 0;
    }

    /**
     * Checks if the entity is in front of the entity restricted to the given angle
     *
     * @param entity entity to check for
     * @param target target to check against
     * @param angle  angle to restrict it to (0-360)
     * @return       true if the target is in front of the entity
     */
    public static boolean isInFront(Entity entity, Entity target, double angle) {
        if (angle <= 0) return false;
        if (angle >= 360) return true;

        // Get the necessary data
        double dotTarget = Math.cos(angle);
        Vector facing = entity.getLocation().getDirection();
        Vector relative = target.getLocation().subtract(entity.getLocation()).toVector().normalize();

        // Compare the target dot product with the actual result
        return facing.dot(relative) >= dotTarget;
    }

    /**
     * Checks if the target is behind the entity
     *
     * @param entity entity to check for
     * @param target target to check against
     * @return       true if the target is behind the entity
     */
    public static boolean isBehind(Entity entity, Entity target) {
        return !isInFront(entity, target);
    }

    /**
     * Checks if the entity is behind the player restricted to the given angle
     *
     * @param entity entity to check for
     * @param target target to check against
     * @param angle  angle to restrict it to (0-360)
     * @return       true if the target is behind the entity
     */
    public static boolean isBehind(Entity entity, Entity target, double angle) {
        if (angle <= 0) return false;
        if (angle >= 360) return true;

        // Get the necessary data
        double dotTarget = Math.cos(angle);
        Vector facing = entity.getLocation().getDirection();
        Vector relative = entity.getLocation().subtract(target.getLocation()).toVector().normalize();

        // Compare the target dot product and the actual result
        return facing.dot(relative) >= dotTarget;
    }
    
    public static List<LivingEntity> getEntitiesInLine(Location origin, double length, LivingEntity... ignore) {
    	ArrayList<Chunk> chunks = new ArrayList<>();
    	 
    	BlockIterator iterator = new BlockIterator(origin, 0, (int) Math.ceil(length));
    	while (iterator.hasNext()) {
    		Chunk chunk = iterator.next().getChunk();
    		if (!chunks.contains(chunk)) {
    			chunks.add(chunk);
    		}
    	}
    	 
    	ArrayList<LivingEntity> entities = new ArrayList<>();
    	 
    	for (Chunk chunk : chunks) {
    		for (Entity e : chunk.getEntities()) {
    			if (e instanceof LivingEntity && (ignore.length == 0 || !Arrays.asList(ignore).contains(e))) {
    				entities.add((LivingEntity) e);
    			}
    		}
    	}
    	 
    	return entities;
    }
    
    public static Player getTargetPlayer(final Player player) {
        return getTarget(player, player.getWorld().getPlayers());
    }
 
    public static Entity getTargetEntity(final Entity entity) {
        return getTarget(entity, entity.getWorld().getEntities());
    }
 
    public static <T extends Entity> T getTarget(final Entity entity,
            final Iterable<T> entities) {
        if (entity == null)
            return null;
        T target = null;
        final double threshold = 1;
        for (final T other : entities) {
            final Vector n = other.getLocation().toVector()
                    .subtract(entity.getLocation().toVector());
            if (entity.getLocation().getDirection().normalize().crossProduct(n)
                    .lengthSquared() < threshold
                    && n.normalize().dot(
                            entity.getLocation().getDirection().normalize()) >= 0) {
                if (target == null
                        || target.getLocation().distanceSquared(
                                entity.getLocation()) > other.getLocation()
                                .distanceSquared(entity.getLocation()))
                    target = other;
            }
        }
        return target;
    }
}