package com.enrutatemio.planificador;

import java.util.ArrayList;

public class NearbyStops {

	private ArrayList<NearbyPoint> points = new ArrayList<NearbyPoint>();
	
	public ArrayList<NearbyPoint> getPoints() {
		return points;
	}

	public void addPoint(NearbyPoint p) {
		this.points.add(p);
	}
}
