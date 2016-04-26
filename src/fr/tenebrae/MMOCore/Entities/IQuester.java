package fr.tenebrae.MMOCore.Entities;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import fr.tenebrae.MMOCore.Quests.Quest;

public interface IQuester {
	
	public ArrayList<Quest> quests = new ArrayList<>();
	public void addQuestNpc(Quest quest);
	public boolean isQuestAviable(Quest quest, Player player);
	public void giveQuest(Quest quest, Player player);
	public boolean hasFinishedQuest(Quest quest, Player player);
	public void giveReward(Quest quest, Player player);
	public boolean hasAlreadyQuest(Quest quest, Player player);
}
