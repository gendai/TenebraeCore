package fr.tenebrae.MMOCore.Quests;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.tenebrae.MMOCore.Quests.MMOQuest.*;

public enum QuestRegistry {
	
	Quest(0, Quest.class),
	QuestTest(1, QuestTest.class),
	QuestTest2(2, QuestTest2.class);
	
	private int id = 0;
	private Class<? extends Quest> clazz = null;
	
	private QuestRegistry(int id, Class<? extends Quest> claszz){
		this.id = id;
		this.clazz = claszz;
	}
	
	public static void registerQuests() {
		for (QuestRegistry questReg : QuestRegistry.values()) {
			questReg.register();
		}
	}
	
	private static Map<Integer,Class<? extends Quest>> REGISTRY = new HashMap<Integer,Class<? extends Quest>>();
	public static void add(int id, Class<? extends Quest> quest) { if (!REGISTRY.containsKey(id) && !REGISTRY.containsValue(quest)) REGISTRY.put(id, quest); }
	public void register() { if (!REGISTRY.containsKey(id) && !REGISTRY.containsValue(clazz)) REGISTRY.put(id, clazz); }
	public static void remove(int id) { REGISTRY.remove(id); }
	public static boolean contains(int id) { return REGISTRY.containsKey(id); }
	public static boolean contains(Quest quest) { return REGISTRY.containsValue(quest); }
	
	public static int getId(Class<? extends Quest> quest) {
		int id = 0;
		for (Entry<Integer, Class<? extends Quest>> entry : REGISTRY.entrySet()) if ((entry.getValue() == quest)) return entry.getKey();
		return id;
	}
	
	public static Quest getQuest(int id) {
		if (REGISTRY.containsKey(id)) {
			try {
				return REGISTRY.get(id).newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		}
		else return null;
	}
}
