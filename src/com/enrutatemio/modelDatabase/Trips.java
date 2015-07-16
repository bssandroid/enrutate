package com.enrutatemio.modelDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Trips")
public class Trips extends Model{

	@Column(name = "TripId")
	public String tripId;
	
	@Column(name = "RoutesId")
	public Routes routeId;
	
	@Column(name = "ServiceId")
	public String serviceId;
	
	@Column(name = "DirectionId")
	public String directionId;
	
	@Column(name = "TripHeadsign")
	public String tripHeadsign;
	
	@Column(name = "ShapeId")
	public Shapes shapeId;
	
}
