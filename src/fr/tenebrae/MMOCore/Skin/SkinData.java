package fr.tenebrae.MMOCore.Skin;

public class SkinData {
	
	private String name;
	private int copyX;
	private int copyY;
	private int priority;

	public SkinData(String name, int copyX, int copyY, int priority) {
		this.name = name;
		this.copyX = copyX;
		this.copyY = copyY;
		this.priority = priority;
	}

	public String getName() {
		return name;
	}
	public int getCopyX() {
		return copyX;
	}
	public int getCopyY() {
		return copyY;
	}
	public int getPriority() {
		return priority;
	}
}
