package fr.tenebrae.MMOCore.Quests;

public class KillCounter {
	
	int count = 0;
	
	public KillCounter(){
	}
	
	public void add(int n){
		this.count += n;
	}
	
	public int getCount(){
		return this.count;
	}
}
