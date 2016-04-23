package fr.tenebrae.MMOCore.Utils;

import net.minecraft.server.v1_9_R1.EntityZombie;

public class EntityNameConverter {
	
	public EntityNameConverter(){
	}

	public String toString(Object o){
		if(o.equals(EntityZombie.class)){
			return "Zombie";
		}else{
			return "";
		}
	}
}
