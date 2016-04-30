package fr.tenebrae.MMOCore.Utils;

import fr.tenebrae.MMOCore.Entities.MMOEntities.L10AlphaTestZombie;

public class EntityNameConverter {
	
	public EntityNameConverter(){
	}

	public String toString(Object o){
		if(o.equals(L10AlphaTestZombie.class)){
			return "Zombie";
		}else{
			return "";
		}
	}
	
	public Object getClass(String s){
		switch(s){
		case "zombie":
			return L10AlphaTestZombie.class;
		default:
			return null;
		}
	}
}
