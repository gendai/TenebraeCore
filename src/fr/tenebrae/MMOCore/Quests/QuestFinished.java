package fr.tenebrae.MMOCore.Quests;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import fr.tenebrae.MMOCore.Main;

public class QuestFinished implements Serializable{

	private static final long serialVersionUID = 7425544088021943517L;

	private String playerName;
	private int questId;
	//private List<Object> results = new ArrayList<Object>();

	public QuestFinished(){
	}

	public QuestFinished(String playerName, int questId){
		this.playerName = playerName;
		this.questId = questId;
	}

	private static void write(File storageFile, QuestFinished o) throws IOException {
		ObjectOutputStream oos = getOOS(storageFile);
		oos.writeObject(o);
		oos.flush();
		oos.close();
	}

	private static ObjectOutputStream getOOS(File storageFile) throws IOException {
		if (storageFile.exists()) {
			return new AppendableObjectOutputStream(new FileOutputStream(storageFile, true));
		} else {
			return new ObjectOutputStream(new FileOutputStream(storageFile));
		}
	}

	public void add(){
		try {
			write(Main.questFinishedFile, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*private void read(){
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(Main.questFinishedFile);
			while (true) {
				ObjectInputStream ois = new ObjectInputStream(fis);
				results.add(ois.readObject());
			}
		} catch (IOException | ClassNotFoundException ignored) {
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}*/
	private ArrayList<QuestFinished> readNon() throws IOException{
		ObjectInputStream objectinputstream = null;
		ArrayList<QuestFinished> recordList = new ArrayList<>();
		try {
			if(Main.questFinishedFile.exists()){
				FileInputStream streamIn = new FileInputStream(Main.questFinishedFile);
				objectinputstream = new ObjectInputStream(streamIn);
				QuestFinished readCase = null;
				do {
					readCase = (QuestFinished) objectinputstream.readObject();
					if(readCase != null){
						recordList.add(readCase);
					} 
				} while (readCase != null);        
			}
		} catch (EOFException e) {

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(objectinputstream != null){
				objectinputstream.close();
				return recordList;
			} 
		}
		return recordList;
	}


	public ArrayList<QuestFinished> getArrayOfFile(){
		/*read();
		ArrayList<QuestFinished> quests = new ArrayList<>();
		for(Object o : results){
			quests.add((QuestFinished)o);
		}*/
		try {
			return readNon();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<String> getPlayersFinishedId(int id){
		ArrayList<QuestFinished> quests = getArrayOfFile();
		ArrayList<String> names = new ArrayList<>();
		for(QuestFinished qf : quests){
			if(qf.getQuestId() == id){
				names.add(qf.getName());
			}
		}
		return names;
	}

	public ArrayList<Integer> getQuestFinishedByPlayer(String playerName){
		ArrayList<QuestFinished> quests = getArrayOfFile();
		ArrayList<Integer> questIds = new ArrayList<>();
		for(QuestFinished qf : quests){
			if(qf.getName().equals(playerName)){
				questIds.add(qf.getQuestId());
			}
		}
		return questIds;
	}

	public boolean containQuest(int questId, String playerName){
		ArrayList<QuestFinished> quests = getArrayOfFile();
		for(QuestFinished qf : quests){
			if(qf.getName().equals(playerName) && qf.getQuestId() == questId){
				return true;
			}
		}
		return false;
	}

	public String getName(){
		return this.playerName;
	}

	public int getQuestId(){
		return this.questId;
	}
}
class AppendableObjectOutputStream extends ObjectOutputStream {

	public AppendableObjectOutputStream(OutputStream out) throws IOException {
		super(out);
	}

	@Override
	protected void writeStreamHeader() throws IOException {
		reset();
	}
}