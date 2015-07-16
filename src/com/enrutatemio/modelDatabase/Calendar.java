package com.enrutatemio.modelDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Calendar")
public class Calendar extends Model{

	@Column(name = "Service_id")
	public String serviceId;
	
	@Column(name = "Monday")
	public String monday;
	
	@Column(name = "Tuesday")
	public String tuesday;
	
	@Column(name = "Wednesday")
	public String wednesday;
	
	@Column(name = "Thursday")
	public String thursday;
	
	@Column(name = "Friday")
	public String friday;
	
	@Column(name = "Saturday")
	public String saturday;
	
	@Column(name = "Sunday")
	public String sunday;
	
	@Column(name = "Start_date")
	public String startDate;
	
	@Column(name = "End_date")
	public String endDate;
}
