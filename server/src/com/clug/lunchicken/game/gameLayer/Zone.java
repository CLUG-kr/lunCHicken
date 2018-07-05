package com.clug.lunchicken.game.gameLayer;

public class Zone {
	
	// 좌측 상단에 있는 점
	private Location location1;
	// 우측 하단에 있는 점
	private Location location2;
	
	public Zone(Location loc1, Location loc2) {
		this.setLocation1(loc1);
		this.setLocation2(loc2);
	}
	
	
	/**
	 * 왼쪽 상단에 위치한 점을 반환하는 메소드
	 * @return Location 왼쪽 상단의 점
	 */
	public Location getLocation1() {
		return location1;
	}
	/**
	 * 왼쪽 상단에 위치할 Location으로 설정한다.
	 * @param Location
	 */
	public void setLocation1(Location loc1) {
		if (location2 == null || (loc1.getPosX() <= location2.getPosX() && loc1.getPosY() >= location2.getPosY())) {
			this.location1 = loc1;
		}
		else {
			this.location1 = this.location2;
			this.location2 = loc1;
		}
	}
	/**
	 * 오른쪽 하단에 위치할 Location을 반환하는 메소드
	 * @return Location 오른쪽 하단의 점
	 */
	public Location getLocation2() {
		return location2;
	}
	/**
	 * 오른쪽 하단의 Location으로 세팅한다.
	 * @param Location
	 */
	public void setLocation2(Location loc2) {
		if (location1 == null || (location1.getPosX() <= loc2.getPosX() && location1.getPosY() >= loc2.getPosY())) {
			this.location2 = loc2;
		}
		else {
			this.location2 = location1;
			this.location2 = loc2;
		}
	}
	
	
	/**
	 * 설정한 좌표 값을 바탕으로 Location1, Location2 를 다시 설정한다.
	 * Location1 은 왼쪽 상단으로, Location2 는 우측 하단으로 
	 */
	public void adjustLocation() {
		if (location1 == null || location2 == null) {
			return;
		}
		else {
			double x1 = location1.getPosX();
			double x2 = location2.getPosX();
			double y1 = location1.getPosY();
			double y2 = location2.getPosY();
			
			// x 좌표 맞추기
			// location1 이 x좌표가 더 작다.
			if (x1 < x2) {
				location1.setPosX(x1);
				location2.setPosX(x2);
			}
			else {
				location1.setPosX(x2);
				location2.setPosX(x1);
			}
			
			// y 좌표 맞추기
			// location1 가 y좌표가 더 크다
			if (y1 < y2) {
				location1.setPosY(y2);
				location2.setPosY(y1);
			}
			else {
				location1.setPosY(y1);
				location2.setPosY(y2);
			}
		}
	}
}
