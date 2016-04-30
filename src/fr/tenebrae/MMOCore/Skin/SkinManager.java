package fr.tenebrae.MMOCore.Skin;

import java.io.File;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public class SkinManager {
	private static ArrayList<SkinSection> skins = new ArrayList<SkinSection>();
	
	public static void addSkinSection(SkinSection section) {
		if (skins.isEmpty())
			skins.add(section);
		else {
			for (int i=0;i<skins.size();i++) {
				if (section.getData().getPriority() > skins.get(i).getData().getPriority()) {
					skins.add(i, section);
					return;
				}
			}
			skins.add(section);
		}
	}
	public static ArrayList<SkinSection> getSkins() {
		return skins;
	}
	
	
	public static File getSkin(String[] section, String[] name) throws Exception {
		if (section.length != name.length) throw new InvalidParameterException("name and section should have the same length");
		
		String finalName = "";
		
		for(int i=0;i<section.length;i++) {
			finalName += getSkinPart(section[i],name[i]).getName();
		}
		
		return Paths.get(CacheHandler.cacheFolder.getAbsolutePath()).resolve(finalName).toFile();
	}
	
	
	public static SkinSection getSkinSection(String name) {
		for (SkinSection skin : skins) {
			if (skin.getData().getName().equalsIgnoreCase(name))
				return skin;
		}
		return skins.get(0);
	}
	
	public static File getSkinPart(String sectionName, String skinName) {
		SkinSection section = getSkinSection(sectionName);
		
		for(File file : section.getFiles()) {
			if (file.getName().equalsIgnoreCase(skinName))
				return file;
		}
		return section.getFiles().get(0);
	}
}
