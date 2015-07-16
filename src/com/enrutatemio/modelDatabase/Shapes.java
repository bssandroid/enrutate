package com.enrutatemio.modelDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Shapes")
public class Shapes extends Model{

	@Column(name = "Shape_id")
	public String shapeId;
	
	@Column(name = "Lat")
	public String lat;
	
	@Column(name = "Lon")
	public String lon;
	
	@Column(name = "Sequence")
	public String sequence;
	
	@Column(name = "Distance")
	public String distance;
	
}
