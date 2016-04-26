package fr.tenebrae.MMOCore.Utils.Serializers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializer {
	
	public static String locationToString(Location loc) {
		return loc == null ? "" : new StringBuilder().append(loc.getWorld().getName()).append(",").append(loc.getX()).append(",").append(loc.getY()).append(",").append(loc.getZ()).append(",").append(loc.getYaw()).append(",").append(loc.getPitch()).toString();
	}

	public static List<String> locationToStringList(List<Location> locs) {
		if (locs == null) return Arrays.asList("");
	    List<String> list = new ArrayList<String>();
	    for (Location loc : locs) list.add(locationToString(loc));
	    return list;
	}
	
	public static String getFormattedNumber(int number) {
		String returned = String.valueOf(number);
		if (number < 10) returned = "0"+returned;
		return returned;
	}

	public static Location stringToLocation(String str) {
		return new Location(Bukkit.getWorld(str.split(",")[0]), Double.parseDouble(str.split(",")[1]), Double.parseDouble(str.split(",")[2]), Double.parseDouble(str.split(",")[3]), Float.parseFloat(str.split(",")[4]), Float.parseFloat(str.split(",")[5]));
	}

	public static List<Location> stringListToLocation(List<String> list) {
		if (list == null) return null;
		List<Location> locs = new ArrayList<Location>();
		for (String s : list) locs.add(stringToLocation(s));
		return locs;
	}
}
