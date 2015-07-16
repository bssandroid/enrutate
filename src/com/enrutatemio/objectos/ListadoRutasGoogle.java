package com.enrutatemio.objectos;

import com.google.android.gms.maps.model.LatLng;

public class ListadoRutasGoogle 
{
   public String direccion;
   public LatLng geolocalizacion;
   public int transbordo;
   

   public ListadoRutasGoogle(String direccion, LatLng geolocalizacion, int trans)
   {
	   this.direccion            = direccion;
	   this.geolocalizacion      = geolocalizacion;
	   this.transbordo           = trans;
   }
  
	@Override
	public String toString()
	{
	    return this.direccion+this.geolocalizacion+" - "+this.transbordo;
	}
}
