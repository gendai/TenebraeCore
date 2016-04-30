package fr.tenebrae.MMOCore.Entities.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MMODeathEvent extends Event{

	public static HandlerList handlers = new HandlerList();
	private Player player;
	private Class<?> entity;
	
	public MMODeathEvent(Class<?> entity, Player player){
		this.player = player;
		this.entity = entity;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}

	public Class<?> getEntity() {
		return entity;
	}

}
