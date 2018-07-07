package com.clug.lunchicken.game.gameLayer;

public class CircleZone {

	private Location center;
	private double radius;
	public CircleZone(Location center, double radius) {
		this.setCenter(center);
		this.setRadius(radius);
	}
	public Location getCenter() {return center;}
	public void setCenter(Location center) {this.center = center;}	
	public double getRadius() {return radius;}
	public void setRadius(double radius) {this.radius = radius;}
	
}
