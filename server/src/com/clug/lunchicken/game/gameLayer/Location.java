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
	/**
	 * 해당 위치와의 거리를 구하는 메소드
	 * @param loc 거리를 구할 위치
	 * @return 파라메터가 null 이면 -1, 아닐 경우 거리
	 */
	public double getDistance(Location loc) {
		// null check
		if (loc == null) return -1;
		return Math.sqrt(Math.pow(this.getPosX() - loc.getPosX(), 2) + Math.pow(this.getPosY() - loc.getPosY(), 2));
	}
	
}
