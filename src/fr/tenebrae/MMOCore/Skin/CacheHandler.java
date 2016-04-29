package fr.tenebrae.MMOCore.Skin;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import fr.tenebrae.MMOCore.Main;


public class CacheHandler {
	
	public static File cacheFolder;
	
	public static void create() throws JSONException, IOException {
		File dataFolder = Main.plugin.getDataFolder();
		
		if (!dataFolder.exists()) {
			dataFolder.mkdir();
			return;
		}
		File skinGenFolder = Paths.get(dataFolder.getAbsolutePath()).resolve("skin").toFile();
		
		if (!skinGenFolder.exists()) {
			skinGenFolder.mkdir();
			return;
		}
		File[] skinsFolder = skinGenFolder.listFiles();
		
		if (skinsFolder == null) {
			return;
		}
		
		for (File skinFolder : skinsFolder) {
			if (!skinFolder.isDirectory()) continue;
			
			File data = ((Path) Paths.get(skinFolder.getAbsolutePath())).resolve("data.json").toFile();
			
			if (!data.exists()) continue;
			
			JSONObject json = new JSONObject(new FileReader(data.getAbsolutePath()));
			
			SkinData skindata = new SkinData(json.getString("name"), json.getInt("copyX"), json.getInt("copyY"), json.getInt("priority"));
			
			ArrayList<File> files = new ArrayList<File>();
			for (File skinFile : files) {
				if (skinFile.equals(data)) {
					files.remove(skinFile);
					break;
				}
			}
			
			if (files.isEmpty()) continue;
			
			
			SkinManager.addSkinSection(new SkinSection(skindata, files));
		}	
		cacheFolder = Paths.get(skinGenFolder.getAbsolutePath()).resolve("cache").toFile();
		
		if (cacheFolder.exists())
			FileUtils.deleteDirectory(cacheFolder);
		cacheFolder.mkdir();
		
		
		ArrayList<SkinSection> sections = SkinManager.getSkins();
		int[] skinRef = new int[sections.size()];
		for(int i=0;i<sections.size();i++) {
			skinRef[i]=0;
		}
		
		while(true) {
			try {
				incrementRef(skinRef);
			} catch(Exception e) {break;}
			
			processImg(skinRef);
		}
		Main.plugin.getLogger().info("All skins have been generated.");
	}

	private static void processImg(int[] skinRef) {
		try {
			ArrayList<SkinSection> sections = SkinManager.getSkins();
			File[] imgs = new File[skinRef.length];
			SkinData[] data = new SkinData[skinRef.length];
			
			for (int i=0;i<skinRef.length;i++) {
				imgs[i] = sections.get(i).getFiles().get(skinRef[i]);
				data[i] = sections.get(i).getData();
			}
			
			
			BufferedImage img = ImageIO.read(imgs[0]);
			Graphics graph = img.getGraphics();
			String outputName = imgs[0].getName();
			for (int i=1;i<imgs.length;i++) {
				BufferedImage topImg = ImageIO.read(imgs[1]);
				graph.drawImage(topImg, data[i].getCopyX(), data[i].getCopyY(), null);
				outputName += imgs[i].getName();
			}
			ImageIO.write(img,"png",Paths.get(CacheHandler.cacheFolder.getAbsolutePath()).resolve(outputName).toFile());
		} catch (IOException e) {
			Main.plugin.getLogger().severe("a problem occured during skins generation!");
			e.printStackTrace();
		}
		
		
	}

	private static int[] incrementRef(int[] skinRef) throws Exception {
		ArrayList<SkinSection> sections = SkinManager.getSkins();
		int max;
		for (int i=0;i<skinRef.length;i++) {
			max = sections.get(0).getFiles().size()-1;
			if (skinRef[i] < max) {
				skinRef[i] = ++skinRef[i];
				break;
			}  else {
				
				if (i == skinRef.length-1)
					throw new Exception();
				
				skinRef[i] = 0;
			}
		}
		return skinRef;
	}
}
