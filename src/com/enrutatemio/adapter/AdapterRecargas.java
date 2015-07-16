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
import com.enrutatemio.objectos.PuntoRecarga;

public class AdapterRecargas extends ArrayAdapter<PuntoRecarga> {
		
	private LayoutInflater vi;
	public Typeface robot_regular;
	
	public AdapterRecargas(Activity activity, final ArrayList<PuntoRecarga> values) {
		super(activity, R.layout.recargasadapter, values);
		vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		robot_regular = Typeface.createFromAsset(activity.getAssets(), "RobotoCondensed_Regular.ttf");
		
	}
        public class ViewHolder {
		private TextView nombrepuntorecarga,dirparada;
		private ImageView imagenPunto;
		
		public ViewHolder(View v) {
			this.nombrepuntorecarga = (TextView) v.findViewById(R.id.nombrepuntorecarga);
			this.dirparada          = (TextView) v.findViewById(R.id.dirparada);
			this.imagenPunto        = (ImageView) v.findViewById(R.id.imagenrecargaadapter); 
			this.nombrepuntorecarga.setTypeface(robot_regular);
			this.dirparada.setTypeface(robot_regular);
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = vi.inflate(R.layout.recargasadapter, parent,false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else 
			holder = (ViewHolder) convertView.getTag();
		

		final PuntoRecarga item = getItem(position);
		
		if (item != null) 
		{
			holder.nombrepuntorecarga.setText(item.nombre);
			holder.dirparada.setText(item.direccion);
			
			if(item.tipo.equalsIgnoreCase("ESTACIÓN"))
				holder.imagenPunto.setImageResource(R.drawable.iconoestacionmapa);
			else if(item.tipo.equalsIgnoreCase("GANE"))
				holder.imagenPunto.setImageResource(R.drawable.iconotarjetarecarga);
			else
				holder.imagenPunto.setImageResource(R.drawable.iconotarjetarecarga);
			
		}
		return convertView;
	}
}
	