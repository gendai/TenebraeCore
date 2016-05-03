package fr.tenebrae.MMOCore.Quests;


import org.bukkit.Bukkit;
import org.bukkit.Location;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Entities.MMOEntities.L01NpcTest;


public class InitQuests {
	
	public L01NpcTest npc;
	
	public InitQuests(){
	}
	
	public void spawn(){
		Location loc = new Location(Bukkit.getServer().getWorld("MMOErdrae"), 334.845, 67, -1574);
		npc = new L01NpcTest(loc);
		npc.spawn();
		npc.addQuestNpc(QuestRegistry.getQuest(1));
		npc.addQuestNpc(QuestRegistry.getQuest(2));
		Bukkit.getServer().getPluginManager().registerEvents(npc, Main.plugin);
	}
}
