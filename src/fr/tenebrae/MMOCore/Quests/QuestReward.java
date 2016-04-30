package fr.tenebrae.MMOCore.Quests;

import org.bukkit.entity.Player;
import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Characters.Character;

public class QuestReward {

	private Object data0;
	private Object data1;
	private Object data2;
	private Object data3;
	private RewardType type;
	
	public QuestReward(RewardType type, Object data0, Object data1, Object data2, Object data3)
	{
		this.type = type;
		this.data0 = data0;
		this.data1 = data1;
		this.data2 = data2;
		this.data3 = data3;
	}
	
	public void giveReward(Player player){
		switch(type){
			case XP:
				player.giveExp((int)data0);
				break;
			case MONEY:
				Character ch = Main.connectedCharacters.get(player);
				ch.money += (int)data0;
				break;
		default:
			break;
		}
	}
	
	public Object getData0() {
		return data0;
	}

	public void setData0(Object data0) {
		this.data0 = data0;
	}

	public Object getData1() {
		return data1;
	}

	public void setData1(Object data1) {
		this.data1 = data1;
	}

	public Object getData2() {
		return data2;
	}

	public void setData2(Object data2) {
		this.data2 = data2;
	}

	public Object getData3() {
		return data3;
	}

	public void setData3(Object data3) {
		this.data3 = data3;
	}

	public RewardType getType() {
		return type;
	}

	public void setType(RewardType type) {
		this.type = type;
	}
	
	public enum RewardType{
		XP, MONEY, ITEM, SPELL
	}
}
