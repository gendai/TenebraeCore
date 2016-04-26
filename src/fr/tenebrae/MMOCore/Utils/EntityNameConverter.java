package fr.tenebrae.MMOCore.Utils;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftZombie;

public class EntityNameConverter {
	
	public EntityNameConverter(){
	}

	public String toString(Object o){
		if(o.equals(CraftZombie.class)){
			return "Zombie";
		}else{
			return "";
		}
	}
	
	public Object getClass(String s){
		switch(s){
		case "zombie":
			return CraftZombie.class;
		default:
			return null;
		}
	}
}
