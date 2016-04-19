package fr.tenebrae.MMOCore.ItemsComponents;

import fr.tenebrae.MMOCore.Mechanics.Stats;

public class GemSlot {

	private GemType type;
	private boolean used = false;
	private Stats givenStat = null;
	
	public GemSlot(GemType type) {
		this.type = type;
	}
	
	public GemSlot(GemType type, boolean used) {
		this.type = type;
		this.used = used;
	}
	
	public GemSlot(GemType type, boolean used, Stats givenStat) {
		this.type = type;
		this.used = used;
		this.givenStat = givenStat;
	}
	
	public GemSlot(GemType type, Stats givenStat) {
		this.type = type;
		this.used = true;
		this.givenStat = givenStat;
	}
	
	public GemType getType() {
		return type;
	}
	
	public boolean isUsed() {
		return used;
	}
	
	public Stats getStat() {
		if (givenStat == null) return null;
		return givenStat;
	}
}
