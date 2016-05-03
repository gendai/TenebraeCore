package fr.tenebrae.MMOCore.Quests;

import java.io.Serializable;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class QuestCondition implements Serializable{
	
	private static final long serialVersionUID = 419780409392449689L;
	private Object data0;
	private Object data1;
	private Object data2;
	private Object data3;
	private ConditionType type;
	
	public QuestCondition(ConditionType type, Object data0, Object data1, Object data2, Object data3)
	{
		this.type = type;
		this.data0 = data0;
		this.data1 = data1;
		this.data2 = data2;
		this.data3 = data3;
	}
	
	public boolean isAuthorize(Player player){
		switch(type){
			case LEVEL:
				if((int)data0 <= player.getLevel() || player.getGameMode().equals(GameMode.CREATIVE)){
					return true;
				}else{
					return false;
				}
			default:
				return true;
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


	public ConditionType getType() {
		return type;
	}


	public void setType(ConditionType type) {
		this.type = type;
	}


	public enum ConditionType{
		LEVEL, CLASS, COMPLETE_QUEST, ITEM
	}
}
