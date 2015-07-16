package com.enrutatemio.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.enrutatemio.R;

public class Recorrido2 extends ArrayAdapter<String> {
	private final Context context;
	private final String [] values;

	public Recorrido2(Activity context, String [] values) {
		super(context, R.layout.recorrido2, values);
		this.context = context;
		this.values = values;
	}
	public Recorrido2(Context context, String [] values) {
		super(context, R.layout.recorrido2, values);
		this.context = context;
		this.values = values;
	}
    
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.recorrido2, parent, false);
		TextView parada = (TextView) rowView.findViewById(R.id.parada);
		String reco = values[position];
		int tam = reco.length();
		if(tam > 0 && tam <= 4)
			parada.setText(reco.toUpperCase().trim());
		else
			parada.setText(reco);
		
	  return rowView;
	}
	
		  

}