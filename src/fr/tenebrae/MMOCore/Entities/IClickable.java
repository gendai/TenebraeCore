package fr.tenebrae.MMOCore.Entities;


import net.minecraft.server.v1_9_R1.Item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.tenebrae.MMOCore.Characters.Character;

public interface IClickable {
	
	public void onInteract(Character clicker, Item nomItem, double distance);
	public Inventory openAvailableQuestGui(Player player);
	public Inventory openFinishedQuestGui(Player player);
	public Inventory openPendingQuestGui(Player player);
}
