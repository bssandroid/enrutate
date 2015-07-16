package com.enrutatemio.mapa;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class Section {
	public String type;
	public String bus;
	public String twalk;
	public ArrayList<Point> points = new ArrayList<Point>();
	public ArrayList<Point> pointsSecondOption = new ArrayList<Point>();
	
	public void addPoint(Point p)
	{
		this.points.add(p);
	}
	public void addPointSecondOption(Point p)
	{
		this.pointsSecondOption.add(p);
	}
	
	public LatLng[] getPoints()
	{
		LatLng[] _points = new LatLng[points.size()];
		
		for(int i = 0, len = points.size(); i < len; ++i)
			_points[i] = points.get(i).position;
		
		
		return _points;
	}
}
