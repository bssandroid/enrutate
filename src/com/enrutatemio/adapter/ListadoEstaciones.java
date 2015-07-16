package com.enrutatemio.adapter;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enrutatemio.R;
import com.enrutatemio.objectos.Estacion;

public class ListadoEstaciones extends ArrayAdapter<Estacion> {
	
	private LayoutInflater vi;
	public Activity ac;
	public Typeface robot_regular;
	public Typeface robot_light;
	
	public ListadoEstaciones(Activity activity, final ArrayList<Estacion> values) {
		super(activity, R.layout.listado_estaciones_mio, values);
		vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.ac = activity;
		robot_regular = Typeface.createFromAsset(ac.getAssets(), "RobotoCondensed_Regular.ttf");
		robot_light = Typeface.createFromAsset(ac.getAssets(), "RobotoCondensed_Light.ttf");
		
	}
        public  class ViewHolder {
     
        private TextView nombre;	
		private TextView direccion;
		private ImageView imagenEstacion;
		
		public ViewHolder(View v) {
		
			this.nombre    = (TextView) v.findViewById(R.id.noticiasmensaje);
			this.direccion = (TextView) v.findViewById(R.id.fechanoticia);
			this.imagenEstacion = (ImageView) v.findViewById(R.id.imagenNoticia);
			this.nombre.setTypeface(robot_regular); 
			this.direccion.setTypeface(robot_light);
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = vi.inflate(R.layout.listado_estaciones_mio, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else 
			holder = (ViewHolder) convertView.getTag();
		

		final Estacion item = getItem(position);
		if (item != null) 
		{
			holder.imagenEstacion.setImageResource(R.drawable.iconoestacionhd);
		    holder.nombre.setText(item.nombre);
			holder.direccion.setText(item.direccion);
			
		}
		return convertView;
	}
	 
}
	