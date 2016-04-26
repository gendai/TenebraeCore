package fr.tenebrae.MMOCore.Quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.tenebrae.MMOCore.Utils.EntityNameConverter;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.NBTTagList;
import net.minecraft.server.v1_9_R1.NBTTagString;

public class Quest {
	
	private String title;
	private String description;
	private String niveauQuete;
	private int idNom;
	private int idDescription;
	private ArrayList<QuestObjective> objectives = new ArrayList<>();
	private ArrayList<QuestCondition> conditions = new ArrayList<>();
	private ArrayList<QuestReward> reward = new ArrayList<>();
	private boolean finished = false;
	public boolean completed = false;
	
	public Quest(String title, String description, String niveauQuest, int idNom, int idDescritpion,ArrayList<QuestObjective> objs, ArrayList<QuestCondition> conditions, ArrayList<QuestReward> rewards)
	{
		this.title = title;
		this.description = description;
		this.niveauQuete = niveauQuest;
		this.objectives = objs;
		this.conditions = conditions;
		this.reward = rewards;
		this.idNom = idNom;
		this.idDescription = idDescritpion;
	}
	
	public boolean isDone(Player player){
		for(QuestObjective obj : objectives){
			if(!obj.isCompleted()){
				return false;
			}
		}
		for(QuestReward r : reward){
			r.giveReward(player);
		}
		this.finished = true;
		return true;
	}
	
	public boolean canHaveQuest(Player player){
		for(QuestCondition con : conditions){
			if(!con.isAuthorize(player)){
				return false;
			}
		}
		return true;
	}
	
	public boolean getFinished(){
		return this.finished;
	}
	
	public void setFinished(boolean b){
		this.finished = b;
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

	
	public ItemStack getWrittenBook(){
		EntityNameConverter enc = new EntityNameConverter();
		ItemStack writtenBook = new ItemStack(Material.BOOK);
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
		bd.setString("title", this.getTitle());
		NBTTagList bp = new NBTTagList();
		for(String text : pages) {
			bp.add(new NBTTagString(text));
		}
		bd.set("pages", bp);
		nmsis.setTag(bd);
		writtenBook = CraftItemStack.asBukkitCopy(nmsis);
		ItemMeta meta = writtenBook.getItemMeta();
		ArrayList<String> lores = new ArrayList<>();
		lores.add("§r");
		lores.add(ChatColor.GOLD+"Conditions: ");
		lores.add(ChatColor.GRAY+"    "+condi);
		lores.add("§r");
		lores.add(ChatColor.GOLD+"Objectif: ");
		lores.add(ChatColor.GRAY+"    "+obj);
		lores.add("§r");
		lores.add(ChatColor.GOLD+"Récompenses: ");
		lores.add(ChatColor.GRAY+"    "+rewa);
		meta.setLore(lores);
		meta.setDisplayName(this.title);
		writtenBook.setItemMeta(meta);
		return writtenBook;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void informUpdate(Player player){
		EntityNameConverter enc = new EntityNameConverter();
		String[] s = new String[objectives.size()+1];
		for(int i = 0; i < objectives.size(); i++){
			switch(objectives.get(i).getType()){
			case KILL:
				KillCounter kc = (KillCounter)objectives.get(i).getData2();
				s[i] = ChatColor.GOLD+"[Quest]"+this.title+": Kill "+enc.toString(objectives.get(i).getData0())+" "+kc.getCount()+"/"+objectives.get(i).getData1();
				break;
			case DISCOVER:
				DiscoverCoord dc  = (DiscoverCoord)objectives.get(0).getData1();
				if(dc.isArrived){
					s[i] = ChatColor.GOLD+"[Quest]"+this.title+": You have discovered "+objectives.get(i).getData0();
				}else{
					s[i] = ChatColor.GOLD+"[Quest]"+this.title+": You didn't discovered "+objectives.get(i).getData0();
				}
				break;
			default:
				break;
			}
		}
		boolean finish = true;
		for(QuestObjective obj : objectives){
			if(!obj.isCompleted()){
				finish = false;
			}
		}
		if(finish){
			s[objectives.size()] = ChatColor.GREEN+"[Quest]"+this.title+": All objectives are completed.";
			this.completed = true;
		}
		player.sendMessage(s);
	}
}
