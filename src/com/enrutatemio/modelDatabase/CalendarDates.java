package com.enrutatemio.modelDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "CalendarDates")
public class CalendarDates extends Model{

	@Column(name = "Service_id")
	public String serviceId;
	
	@Column(name = "Date")
	public String date;
	
	@Column(name = "Exception_type")
	public String exceptionType;
	
}
