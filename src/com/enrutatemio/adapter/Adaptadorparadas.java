package com.enrutatemio.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enrutatemio.R;
import com.enrutatemio.BD.AdminSQLiteOpenHelper;

public class Adaptadorparadas extends ArrayAdapter<String> {
	
	
	private final String subc;
	private final int desde;
	private Activity ac;
	private ArrayList<String> values;
	public Typeface robot_regular;
	public Typeface robot_light;
	
	public Adaptadorparadas(Activity activity, final ArrayList<String> values, String subc,int desde) {
		super(activity, R.layout.paradas_adapter, values);
		
		this.subc         = subc;
		this.desde        = desde;
		this.ac           = activity;
		this.values       = values; 
		robot_regular = Typeface.createFromAsset(ac.getAssets(), "RobotoCondensed_Regular.ttf");
		robot_light = Typeface.createFromAsset(ac.getAssets(), "RobotoCondensed_Light.ttf");
	}
   
	@SuppressLint("ViewHolder")
	public View getView(int position, View convertView, ViewGroup parent) {
	
		LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.paradas_adapter, parent, false);
		
        TextView nombreParada = (TextView) view.findViewById(R.id.nombreParada);
        ImageView imagenParada        = (ImageView) view.findViewById(R.id.imagenParada); 
        TextView dirParadas         = (TextView) view.findViewById(R.id.direccionParada);
		nombreParada.setTypeface(robot_regular);
		dirParadas.setTypeface(robot_light);

		final String item = values.get(position);
		String cadena      = item.toLowerCase().trim();
		
        cadena  = validarEntrada(cadena);
        String dirParada = busqueda(cadena);
        String subcadena   = subc.toLowerCase().trim();
        String dir  = consulta(dirParada.toUpperCase());
        if(dir == null)
        	dir = "";
        
		if (item != null) 
		{
			if(dir.equalsIgnoreCase("null") || dir.length() == 0)
			{
				dirParadas.setVisibility(View.GONE);
				nombreParada.setGravity(Gravity.CENTER_VERTICAL);
				LayoutParams params = nombreParada.getLayoutParams();
				params.height = LayoutParams.FILL_PARENT;
				nombreParada.setLayoutParams(params);
			}
			if(desde == 1)
			{
				nombreParada.setText((position+1)+"- "+item);
				dirParadas.setText(dir);
				if(position == 0)
					imagenParada.setImageResource(R.drawable.banderainicio);
				else
				{
					if(position == (values.size() - 1))
						imagenParada.setImageResource(R.drawable.banderafin);
					else
						imagenParada.setImageResource(R.drawable.siguiente);
				}
			}
			else
			{
				nombreParada.setText((position+1)+"- "+item);
				dirParadas.setText(dir);
			    if (cadena.indexOf (subcadena) != -1)
				{
			    	imagenParada.getLayoutParams().height = 20;
			    	imagenParada.getLayoutParams().width = 20;
			    	imagenParada.setImageResource(R.drawable.flecha_contratante);
				}
				else
				{
					if(position == 0)
						imagenParada.setImageResource(R.drawable.banderainicio);
					else
					{
						if(position == (values.size() - 1))
							imagenParada.setImageResource(R.drawable.banderafin);
						else
							imagenParada.setImageResource(R.drawable.siguiente);
							
					}
					
				}
			}   
			
			
		}
		return view;
	}
	public static String validarEntrada(String nombre)
	  {
		  String name [] = nombre.split(" ");
		  String tmp = ""; 
		  if(name.length > 1)
		  {
			  
			  if(name[0].contains("estac"))
				  for(int i = 1,len = name.length; i < len; ++i)
					  tmp += name[i]+" "; 
				  
			  else
				  for(int i = 0, len = name.length; i < len; ++i)
					  tmp += name[i]+" "; 
		  }
		  
		  return tmp.trim();
	  }
	 public String busqueda(String palabra)
	 {
		 String pal = "";
		
	        if(palabra.length() > 0 && palabra.contains("("))
	        {
	           palabra = palabra.substring(0, palabra.indexOf("("));
	           palabra = palabra.trim();
	        }
	        pal = palabra;
	        return pal;
	     
	        
	 }
	 public String consulta(String a)
	 {
	        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(ac);
	        SQLiteDatabase bd           = admin.getWritableDatabase();
	        
	        Cursor fila = null;
	        String dir = "";
	       
	        fila = bd.rawQuery("select direccion from estaciones where nombre  = '"
						+ a.toUpperCase() + "'",null);
	       
	         
	       if(fila != null)
	    	   if (fila.moveToFirst())
	    		   dir = fila.getString(0);
      
		    bd.close();
	        fila.close();
	        admin.close();
			return dir;            

	    }
}
	