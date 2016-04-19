package fr.tenebrae.MMOCore;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.world.ChunkUnloadEvent;

public class Listeners implements Listener {

	public Main plugin;
	
	public Listeners(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onUnloadChunk(ChunkUnloadEvent evt) {
		evt.setCancelled(true);
	}
	
	@EventHandler
	public void entityDamage(EntityDamageEvent evt) {
		if (evt.getCause() == DamageCause.FALL) {
			evt.setDamage(evt.getDamage()/7.5);
		} else {
			if (evt.getEntity() instanceof Player) {
				
			}
		}
	}
}
