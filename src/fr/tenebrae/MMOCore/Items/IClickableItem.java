package fr.tenebrae.MMOCore.Items;

import org.bukkit.entity.Player;

public interface IClickableItem {

	public void onClick(Player clicker, int rawSlot);
	
}
