package fr.tenebrae.MMOCore.ItemsComponents;

public enum TriggerType {
	CHANCES_ON_HIT,
	CHANCES_ON_HITTED,
	USE,
	CHANCES_ON_CRITICAL,
	CHANCES_ON_DODGE,
	CHANCES_ON_DODGED,
	CHANCES_ON_HITTED_CRITICAL,
	CHANCES_ON_BLOCK,
	CHANCES_ON_BLOCKED,
	PERMANENT;
	
	public String getString(String language) {
		return this.name()+" : ";
	}
}
