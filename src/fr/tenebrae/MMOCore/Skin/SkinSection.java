package fr.tenebrae.MMOCore.Skin;

import java.io.File;
import java.util.ArrayList;

public class SkinSection {
	
	
	private ArrayList<File> files;
	private SkinData data;

	public SkinSection (SkinData data, ArrayList<File> files) {
		this.data = data;
		this.files = files;
	}
	
	public SkinData getData() {
		return data;
	}
	
	public ArrayList<File> getFiles() {
		return files;
	}
}
