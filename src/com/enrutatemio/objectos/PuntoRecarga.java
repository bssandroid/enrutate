package com.enrutatemio.objectos;

public class PuntoRecarga {

	public String numero;
	public String nombre;
	public String direccion;
	public double lat;
	public double lon;
	public String tipo;
	
	public PuntoRecarga(String nombre, String direccion, String tipo)
	{
		this.nombre       = nombre;
		this.direccion    = direccion;
		this.tipo         = tipo;
	}
	public PuntoRecarga(String numero , String nombre, String direccion,double lat, double lon ,String tipo)
	{
		this.numero       = numero;
		this.nombre       = nombre;
		this.direccion    = direccion;
		this.lat          = lat;
		this.lon          = lon;
		this.tipo         = tipo;
	}

	@Override
	public String toString()
	{
		return this.nombre+ " "+this.direccion;
	}
}
