package com.enrutatemio.objectos;

public class Estacion {

	
	public String nombre;
	public String latitud;
	public String longitud;
	public String paradas;
	public String tipo;
	public String direccion;
	
	public Estacion (String nombre, String latitud, String longitud, String paradas,String tipo,String direccion)
	{
		this.nombre           = nombre;
		this.latitud          = latitud;
		this.longitud         = longitud;
		this.paradas          = paradas;
		this.tipo             = tipo;
		this.direccion        = direccion;
	}


	@Override
	public String toString()
	{
		return this.nombre;
	}
}
