package fr.tenebrae.MMOCore.Quests.MMOQuest;

import java.util.ArrayList;

import fr.tenebrae.MMOCore.Entities.MMOEntities.L10AlphaTestZombie;
import fr.tenebrae.MMOCore.Quests.KillCounter;
import fr.tenebrae.MMOCore.Quests.Quest;
import fr.tenebrae.MMOCore.Quests.QuestCondition;
import fr.tenebrae.MMOCore.Quests.QuestObjective;
import fr.tenebrae.MMOCore.Quests.QuestReward;
import fr.tenebrae.MMOCore.Quests.QuestCondition.ConditionType;
import fr.tenebrae.MMOCore.Quests.QuestObjective.ObjectiveType;
import fr.tenebrae.MMOCore.Quests.QuestReward.RewardType;

public class QuestTest extends Quest{

	private static final long serialVersionUID = -9078907684749919207L;

	public QuestTest() {
		super();
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
		this.title = "Quêtes test";
		this.description = "Ceci est une description pour la quête principale";
		this.setNiveauQuete("1");
		this.idNom = 70132;
		this.idDescription = 70134;
		this.setConditions(conditionArr);
		this.setObjectives(objArr);
		this.setReward(rewardArr);
	}

}
