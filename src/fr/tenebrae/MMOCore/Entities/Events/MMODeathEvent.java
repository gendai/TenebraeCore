package fr.tenebrae.MMOCore.Entities.Events;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.minecraft.server.v1_9_R1.Entity;

public class MMODeathEvent extends Event{

	public static HandlerList handlers = new HandlerList();
	private ArrayList<Entity> entities;
	private Class<?> entity;
	
	public MMODeathEvent(Class<?> entity, Set<Entity> entities){
		this.entities = new ArrayList<>();
		this.entity = entity;
		for(Entity en : entities){
			this.entities.add(en);
		}
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public Class<?> getEntity() {
		return entity;
	}

}
