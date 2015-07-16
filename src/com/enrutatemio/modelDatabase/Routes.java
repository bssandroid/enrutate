package com.enrutatemio.modelDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "Routes")
public class Routes extends Model{

	@Column(name = "Route_id")
	public String routeId;
	
	@Column(name = "Agency_id")
	public String agencyId;
	
	@Column(name = "ShortName")
	public String shortName;
	
	@Column(name = "LongName")
	public String longName;
	
	@Column(name = "Type")
	public String type;
	
	
}
