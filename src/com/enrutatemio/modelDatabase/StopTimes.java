package com.enrutatemio.modelDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "StopTimes")
public class StopTimes extends Model{

	@Column(name = "Trips")
	public Trips Trip_id;
	
	@Column(name = "ArrivalTime")
	public String lat;
	
	@Column(name = "DepartureTime")
	public String lon;
	
	@Column(name = "Stops")
	public Stops stopId;
	
	@Column(name = "StopSequence")
	public String stopSequence;
	
	@Column(name = "StopHeadsign")
	public String stopHeadsign;
	
	@Column(name = "Shapes")
	public Shapes distance;
}
