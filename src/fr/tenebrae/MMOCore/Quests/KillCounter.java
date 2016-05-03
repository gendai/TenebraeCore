package fr.tenebrae.MMOCore.Quests;

import java.io.Serializable;

public class KillCounter implements Serializable{
	
	private static final long serialVersionUID = -8828061892812630748L;
	int count = 0;
	
	public KillCounter(){
	}
	
	public void add(int n){
		this.count += n;
	}
	
	public int getCount(){
		return this.count;
	}
	
	public void setCount(int n){
		this.count = n;
	}
}
