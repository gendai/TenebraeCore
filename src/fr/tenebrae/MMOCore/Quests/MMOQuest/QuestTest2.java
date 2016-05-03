package fr.tenebrae.MMOCore.Quests.MMOQuest;

import java.util.ArrayList;

import fr.tenebrae.MMOCore.Entities.MMOEntities.L10AlphaTestZombie;
import fr.tenebrae.MMOCore.Quests.DiscoverCoord;
import fr.tenebrae.MMOCore.Quests.KillCounter;
import fr.tenebrae.MMOCore.Quests.Quest;
import fr.tenebrae.MMOCore.Quests.QuestCondition;
import fr.tenebrae.MMOCore.Quests.QuestObjective;
import fr.tenebrae.MMOCore.Quests.QuestReward;
import fr.tenebrae.MMOCore.Quests.QuestCondition.ConditionType;
import fr.tenebrae.MMOCore.Quests.QuestObjective.ObjectiveType;
import fr.tenebrae.MMOCore.Quests.QuestReward.RewardType;

public class QuestTest2 extends Quest{

	
	private static final long serialVersionUID = -2268661713719735986L;

	public QuestTest2(){
		super();
		QuestCondition condition = new QuestCondition(ConditionType.LEVEL, (int)5, null, null, null);
		QuestObjective obj = new QuestObjective(ObjectiveType.DISCOVER, "Test Land", new DiscoverCoord(301.663, 67, -1586.501, 3, 3), null, null);
		KillCounter killc = new KillCounter();
		QuestObjective obj2 = new QuestObjective(ObjectiveType.KILL, L10AlphaTestZombie.class, 3, killc, null);
		QuestReward rew = new QuestReward(RewardType.MONEY, (int)20, null, null, null);
		ArrayList<QuestCondition> conditionArr2 = new ArrayList<>();
		conditionArr2.add(condition);
		ArrayList<QuestObjective> objArr2 = new ArrayList<>();
		objArr2.add(obj);
		objArr2.add(obj2);
		ArrayList<QuestReward> rewardArr2 = new ArrayList<>();
		rewardArr2.add(rew);
		this.title = "Quêtes Secondaire";
		this.description = "Ceci est une description pour la quête secondaire";
		this.setNiveauQuete("1");
		this.idNom = 70133;
		this.idDescription = 70135;
		this.setConditions(conditionArr2);
		this.setObjectives(objArr2);
		this.setReward(rewardArr2);
	}
}
