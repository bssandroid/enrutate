package com.enrutatemio.modelDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Agency")
public class Agency extends Model{

	@Column(name = "Agency_id")
	public String agencyId;
	
	@Column(name = "Agency_name")
	public String name;
	
	@Column(name = "Agency_url")
	public String url;
	
	@Column(name = "Agency_timezone")
	public String timezone;
	
	@Column(name = "Agency_lang")
	public String lang;
	
	@Column(name = "Agency_phone")
	public String phone;
	
}
