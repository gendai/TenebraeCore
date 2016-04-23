package fr.tenebrae.MMOCore.Quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import fr.tenebrae.MMOCore.Utils.EntityNameConverter;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.NBTTagList;
import net.minecraft.server.v1_9_R1.NBTTagString;

public class Quest {
	
	private String description;
	private String niveauQuete;
	private int idNom;
	private int idDescription;
	private ArrayList<QuestObjective> objectives = new ArrayList<>();
	private ArrayList<QuestCondition> conditions = new ArrayList<>();
	private ArrayList<QuestReward> reward = new ArrayList<>();
	
	public Quest(String description, String niveauQuest, int idNom, int idDescritpion,ArrayList<QuestObjective> objs, ArrayList<QuestCondition> conditions, ArrayList<QuestReward> rewards)
	{
		this.description = description;
		this.niveauQuete = niveauQuest;
		this.objectives = objs;
		this.conditions = conditions;
		this.reward = rewards;
		this.idNom = idNom;
		this.idDescription = idDescritpion;
	}
	
	public boolean isDone(){
		for(QuestObjective obj : objectives){
			if(!obj.isCompleted()){
				return false;
			}
		}
		for(QuestReward r : reward){
			r.giveReward();
		}
		return true;
	}
	
	public boolean canHaveQuest(){
		for(QuestCondition con : conditions){
			if(!con.isAuthorize()){
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<QuestReward> getRewards(){
		return this.reward;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNiveauQuete() {
		return niveauQuete;
	}

	public void setNiveauQuete(String niveauQuete) {
		this.niveauQuete = niveauQuete;
	}

	public int getIdNom() {
		return idNom;
	}

	public int getIdDescription() {
		return idDescription;
	}

	public ArrayList<QuestObjective> getObjectives() {
		return objectives;
	}

	public ArrayList<QuestCondition> getConditions() {
		return conditions;
	}

	public ArrayList<QuestReward> getReward() {
		return reward;
	}
	
	public ItemStack getWrittenBook(){
		EntityNameConverter enc = new EntityNameConverter();
		ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
		net.minecraft.server.v1_9_R1.ItemStack nmsis = CraftItemStack.asNMSCopy(writtenBook);
		List<String> pages = new ArrayList<String>();
		String condi = "";
		for(QuestCondition qc : conditions){
			condi += qc.getType().toString()+" "+qc.getData0().toString()+", ";
		}
		condi = condi.substring(0, condi.length()-2);
		pages.add("Conditions: "+condi);
		String obj = "";
		for(QuestObjective  qo : objectives){
			if(qo.getType().equals(QuestObjective.ObjectiveType.KILL)){
			obj += qo.getType().toString()+" "+enc.toString(qo.getData0())+" "+qo.getData1().toString()+", ";
			}else if(qo.getType().equals(QuestObjective.ObjectiveType.DISCOVER)){
				obj += qo.getType().toString()+" "+qo.getData0().toString()+", ";
			}
		}
		obj = obj.substring(0, obj.length()-2);
		pages.add("Objectives: "+obj);
		String rewa = "";
		for(QuestReward qr : reward){
			rewa += qr.getType().toString()+" "+qr.getData0().toString()+", ";
		}
		rewa = rewa.substring(0, rewa.length()-2);
		pages.add("Rewards: "+rewa);
		NBTTagCompound bd = new NBTTagCompound();
		bd.setString("title", this.getDescription());
		NBTTagList bp = new NBTTagList();
		for(String text : pages) {
			bp.add(new NBTTagString(text));
		}
		bd.set("pages", bp);
		nmsis.setTag(bd);
		writtenBook = CraftItemStack.asBukkitCopy(nmsis);
		return writtenBook;
	}
}
