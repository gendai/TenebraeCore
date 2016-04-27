package fr.tenebrae.MMOCore.Quests;

import org.bukkit.Location;

public class DiscoverCoord {
	
	double x,y,z,radx,rady;
	boolean isArrived = false;
	
	public DiscoverCoord(double x, double y, double z, double radx, double rady){
		this.x = x;
		this.y = y;
		this.z = z;
		this.radx = radx;
		this.rady = rady;
	}
	
	public boolean hasReached(Location loc){
		if(loc.getX() >= (x-radx) && loc.getX() <= (x+radx)
				&& loc.getY() >= (y-rady) && loc.getY() <= (y+rady)
				&& loc.getZ() >= z-radx && loc.getZ() <= z+radx){
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

	public double getRady() {
		return rady;
	}
	
	public void setIsArrived(boolean b){
		this.isArrived = b;
	}
	
	public boolean getIsArrived(){
		return this.isArrived;
	}
}
