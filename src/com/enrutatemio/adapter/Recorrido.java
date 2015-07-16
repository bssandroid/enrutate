package com.enrutatemio.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.enrutatemio.R;

public class Recorrido extends ArrayAdapter<String> {
	private final Context context;
	private final ArrayList<String> values;

	public Recorrido(Context context, ArrayList<String> values) {
		super(context, R.layout.recorrido, values);
		this.context = context;
		this.values = values;
	}
    
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.recorrido, parent, false);
		TextView estacion = (TextView) rowView.findViewById(R.id.estacion);
		
		String reco = values.get(position);
		
        if(reco.contains("viaje"))
        {
        	estacion.setText(reco);
        	estacion.setTextSize(17);
        	estacion.setTextColor(Color.WHITE);
			//direccion.setText("");
        }
        else
        	estacion.setText(reco.toString());
			
        

		return rowView;
	}


}