package com.clug.lunchicken.game.gameLayer;

public class Location {

	private double posX, posY;
	
	public Location(double posX, double posY) {
		this.setPosX(posX);
		this.setPosY(posY);
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}
	
	
}
