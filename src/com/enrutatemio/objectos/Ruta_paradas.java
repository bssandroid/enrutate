package com.enrutatemio.objectos;

public class Ruta_paradas {

	
	public String ruta;
	public String sentido;
	public String paradas;
	public String tipo;
	public String zona;
	public String trayecto;
	public String horario;
	public boolean openLayout = false;
	
	
	
	public Ruta_paradas( String ruta,String sentido,String paradas,String tipo, String horario, String trayecto )
	{
		//this.id        = id;
		this.ruta      = ruta;
		this.sentido   = sentido;
		this.paradas   = paradas;
		this.tipo      = tipo;
	}
	public Ruta_paradas( String ruta,String sentido,String paradas,String tipo,String zona )
	{
		//this.id        = id;
		this.ruta      = ruta;
		this.sentido   = sentido;
		this.paradas   = paradas;
		this.tipo      = tipo;
		this.zona      = zona;
	}
	public Ruta_paradas(String ruta,String trayecto,String horario, String sentido )
	{
		
		this.ruta      = ruta;
		this.trayecto  = trayecto;
		this.horario   = horario;
		this.sentido   = sentido;
	}

    

	public boolean isOpenLayout() {
		return openLayout;
	}
	public void setOpenLayout(boolean openLayout) {
		this.openLayout = openLayout;
	}
	
	@Override
	public String toString()
	{
		return this.ruta + this.trayecto;
	}
	
	
	
	
}
