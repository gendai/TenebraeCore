package fr.tenebrae.MMOCore.Quests;

import org.bukkit.Location;

public class DiscoverCoord {
	
	double x,y,z,radx,radz;
	boolean isArrived = false;
	
	public DiscoverCoord(double x, double y, double z, double radx, double radz){
		this.x = x;
		this.y = y;
		this.z = z;
		this.radx = radx;
		this.radz = radz;
	}
	
	public boolean hasReached(Location loc){
		if(loc.getX() >= (x-radx) && loc.getX() <= (x+radx)
				&& loc.getY() >= (y-1.0) && loc.getY() <= (y+1.0)
				&& loc.getZ() >= z-radz && loc.getZ() <= z+radz){
			return true;
		}
		return false;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getRadx() {
		return radx;
	}

	public double getRadz() {
		return radz;
	}
	
	public void setIsArrived(boolean b){
		this.isArrived = b;
	}
	
	public boolean getIsArrived(){
		return this.isArrived;
	}
}
