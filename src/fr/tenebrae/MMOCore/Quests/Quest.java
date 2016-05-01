package fr.tenebrae.MMOCore.Quests;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.tenebrae.MMOCore.Main;
import fr.tenebrae.MMOCore.Entities.CEntityTypes;
import fr.tenebrae.MMOCore.Utils.TranslatedString;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.NBTTagCompound;

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


	@SuppressWarnings("unchecked")
	public ItemStack getWrittenBook(Player player){
		ItemStack writtenBook = new ItemStack(Material.BOOK);
		net.minecraft.server.v1_9_R1.ItemStack nmsis = CraftItemStack.asNMSCopy(writtenBook);
		String condi = "";
		for(QuestCondition qc : conditions){
			condi += qc.getType().toString()+" "+qc.getData0().toString()+", ";
		}
		condi = condi.substring(0, condi.length()-2);
		String obj = "";
		for(QuestObjective  qo : objectives){
			if(qo.getType().equals(QuestObjective.ObjectiveType.KILL)){
				obj += TranslatedString.getString(70112, player)+" "+qo.getData1().toString()+" "+TranslatedString.getString(CEntityTypes.getId((Class<? extends EntityInsentient>)qo.getData0()), player)+", ";
			}else if(qo.getType().equals(QuestObjective.ObjectiveType.DISCOVER)){
				DiscoverCoord dc = (DiscoverCoord)qo.getData1();
				obj += TranslatedString.getString(70114, player)+" "+qo.getData0().toString()+"["+dc.getX()+","+dc.getY()+","+dc.getZ()+"], ";
			}
		}
		obj = obj.substring(0, obj.length()-2);
		String rewa = "";
		for(QuestReward qr : reward){
			rewa += TranslatedString.getString(qr.getTranslateId(qr.getType()), player)+" "+qr.getData0().toString()+", ";
		}
		rewa = rewa.substring(0, rewa.length()-2);
		NBTTagCompound bd = new NBTTagCompound();
		bd.setString("title", this.getTitle());
		nmsis.setTag(bd);
		writtenBook = CraftItemStack.asBukkitCopy(nmsis);
		ItemMeta meta = writtenBook.getItemMeta();
		ArrayList<String> lores = new ArrayList<>();
		lores.add("§r");
		lores.add(ChatColor.GOLD+TranslatedString.getString(70117, player)+": ");
		lores.add(ChatColor.GRAY+"    "+condi);
		lores.add("§r");
		lores.add(ChatColor.GOLD+TranslatedString.getString(70118,player)+": ");
		lores.add(ChatColor.GRAY+"    "+obj);
		lores.add("§r");
		lores.add(ChatColor.GOLD+TranslatedString.getString(70119,player)+": ");
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

	@SuppressWarnings("unchecked")
	public void informUpdate(Player player){
		String[] s = new String[objectives.size()+1];
		for(int i = 0; i < objectives.size(); i++){
			switch(objectives.get(i).getType()){
			case KILL:
				KillCounter kc = (KillCounter)objectives.get(i).getData2();
				if(kc.getCount() >= (int)objectives.get(i).getData1()){
					s[i] = ChatColor.GOLD+"["+TranslatedString.getString(70113, player)+"]"+this.title+ChatColor.GREEN+": "+TranslatedString.getString(70112, player)+" "+TranslatedString.getString(CEntityTypes.getId((Class<? extends EntityInsentient>)objectives.get(i).getData0()), Main.connectedCharacters.get(player).getLanguage())+" "+kc.getCount()+"/"+objectives.get(i).getData1();
				}else{
					s[i] = ChatColor.GOLD+"["+TranslatedString.getString(70113, player)+"]"+this.title+ChatColor.RED+": "+TranslatedString.getString(70112, player)+" "+TranslatedString.getString(CEntityTypes.getId((Class<? extends EntityInsentient>)objectives.get(i).getData0()), Main.connectedCharacters.get(player).getLanguage())+" "+kc.getCount()+"/"+objectives.get(i).getData1();
				}
				break;
			case DISCOVER:
				DiscoverCoord dc  = (DiscoverCoord)objectives.get(0).getData1();
				if(dc.isArrived){
					s[i] = ChatColor.GOLD+"["+TranslatedString.getString(70113, player)+"]"+this.title+ChatColor.GREEN+": "+TranslatedString.getString(70121, player)+" "+objectives.get(i).getData0();
				}else{
					s[i] = ChatColor.GOLD+"["+TranslatedString.getString(70113, player)+"]"+this.title+ChatColor.RED+": "+TranslatedString.getString(70120, player)+" "+objectives.get(i).getData0();
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
			s[objectives.size()] = ChatColor.GREEN+"["+TranslatedString.getString(70113, player)+"]"+this.title+": "+TranslatedString.getString(70122, player)+".";
			this.completed = true;
		}
		player.sendMessage(s);
	}
}
