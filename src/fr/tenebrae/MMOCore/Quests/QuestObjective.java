package fr.tenebrae.MMOCore.Quests;

public class QuestObjective {

	private Object data0;
	private Object data1;
	private Object data2;
	private Object data3;
	private ObjectiveType type;
	
	public QuestObjective(ObjectiveType type, Object data0, Object data1, Object data2, Object data3){
		this.type = type;
		this.data0 = data0;
		this.data1 = data1;
		this.data2 = data2;
		this.data3 = data3;
	}
	
	public boolean isCompleted(){
		switch(type){
			case KILL:
				KillCounter kc = (KillCounter)data2;
				return (int)this.data1 <= kc.getCount();
			case DISCOVER:
				DiscoverCoord dc = (DiscoverCoord)data1;
				return dc.getIsArrived();
			default:
				return false;
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

	public ObjectiveType getType() {
		return type;
	}

	public void setType(ObjectiveType type) {
		this.type = type;
	}

	public enum ObjectiveType{
		KILL, DISCOVER, CAST_SPELL, TALK_TO_NPC
	}
	
}
