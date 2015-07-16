package com.enrutatemio.modelDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Stops")
public class Stops extends Model{

	@Column(name = "StopId")
	public String stopId;
	
	@Column(name = "Code")
	public String code;
	
	@Column(name = "Name")
	public String name;
	
	@Column(name = "Lat")
	public String lat;
	
	@Column(name = "Lon")
	public String lon;
	
	@Column(name = "LocationType")
	public Shapes locationType;
	
}
