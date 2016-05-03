package fr.tenebrae.MMOCore.Quests;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Entities.MMOEntities.L01NpcTest;
import fr.tenebrae.MMOCore.Entities.MMOEntities.L10AlphaTestZombie;
import fr.tenebrae.MMOCore.Quests.QuestCondition.ConditionType;
import fr.tenebrae.MMOCore.Quests.QuestObjective.ObjectiveType;
import fr.tenebrae.MMOCore.Quests.QuestReward.RewardType;

public class InitQuests {
	
	public L01NpcTest npc;
	
	public InitQuests(){
	}
	
	public void spawn(){
		QuestCondition condition = new QuestCondition(ConditionType.LEVEL, (int)1, null, null, null);
		KillCounter kc = new KillCounter();
		QuestObjective obj = new QuestObjective(ObjectiveType.KILL, L10AlphaTestZombie.class, (int)1, kc, null);
		QuestReward rew = new QuestReward(RewardType.XP, (int)30, null, null, null);
		ArrayList<QuestCondition> conditionArr = new ArrayList<>();
		conditionArr.add(condition);
		ArrayList<QuestObjective> objArr = new ArrayList<>();
		objArr.add(obj);
		ArrayList<QuestReward> rewardArr = new ArrayList<>();
		rewardArr.add(rew);
		Quest q = new Quest("Quêtes test","Ceci est une description pour la quête principale", "1", 70132, 70134, objArr, conditionArr, rewardArr);
		Main.quests.put(q.getIdNom(), q);
		
		condition = new QuestCondition(ConditionType.LEVEL, (int)5, null, null, null);
		obj = new QuestObjective(ObjectiveType.DISCOVER, "Test Land", new DiscoverCoord(301.663, 67, -1586.501, 3, 3), null, null);
		KillCounter killc = new KillCounter();
		QuestObjective obj2 = new QuestObjective(ObjectiveType.KILL, L10AlphaTestZombie.class, 3, killc, null);
		rew = new QuestReward(RewardType.MONEY, (int)20, null, null, null);
		ArrayList<QuestCondition> conditionArr2 = new ArrayList<>();
		conditionArr2.add(condition);
		ArrayList<QuestObjective> objArr2 = new ArrayList<>();
		objArr2.add(obj);
		objArr2.add(obj2);
		ArrayList<QuestReward> rewardArr2 = new ArrayList<>();
		rewardArr2.add(rew);
		Quest q2 = new Quest("Quêtes Secondaire","Ceci est une description pour la quête secondaire", "1", 70133, 70135, objArr2, conditionArr2, rewardArr2);
		Main.quests.put(q2.getIdNom(), q2);
		
		Location loc = new Location(Bukkit.getServer().getWorld("MMOErdrae"), 334.845, 67, -1574);
		npc = new L01NpcTest(loc);
		npc.spawn();
		npc.addQuestNpc(q);
		npc.addQuestNpc(q2);
		Bukkit.getServer().getPluginManager().registerEvents(npc, Main.plugin);
	}
}
