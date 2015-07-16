package com.enrutatemio.adapter;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enrutatemio.R;
import com.enrutatemio.objectos.ListadoRutasGoogle;

@SuppressLint("ViewHolder")
public class TrayectoFinal extends ArrayAdapter<ListadoRutasGoogle> {
	
	private Activity ac;
    private ArrayList<ListadoRutasGoogle> values;
	
	public TrayectoFinal(Activity activity, final ArrayList<ListadoRutasGoogle> values) {
		super(activity, R.layout.trayectofinal, values);
	
		this.ac = activity;
		this.values = values;
	}
 
        @Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		LayoutInflater inflater = (LayoutInflater) ac	.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    		View rowView                  = inflater.inflate(R.layout.trayectofinal, null);
    		TextView pos                  = (TextView) rowView.findViewById(R.id.pos);
    		TextView direccion            = (TextView) rowView.findViewById(R.id.direccionpos);
    		ImageView imagenTransbordo    = (ImageView) rowView.findViewById(R.id.imagentransbordo);
    		RelativeLayout layoutTrayecto = (RelativeLayout) rowView.findViewById(R.id.layouttrayecto);
    		final ListadoRutasGoogle item = values.get(position);
    		
            if(item != null)
            {
            	String dir = item.direccion;
    			dir = dir.replace("\n", " ").trim().toString();
    			   		
    				if(item.transbordo == 0)
    				{
    					imagenTransbordo.setVisibility(View.GONE);
    					pos.setText(""+(position + 1));
    					direccion.setText(dir);
    			 
    				}
    				else if(item.transbordo == 1)
    				{
    					imagenTransbordo.setVisibility(View.VISIBLE);
    					imagenTransbordo.setImageResource(R.drawable.ic_transbordo);	
    					pos.setText(""+(position + 1));
    					layoutTrayecto.setBackgroundResource(R.drawable.backgroundtransbordo);
    					direccion.setText(dir);
    				}
            }
    		
    		return rowView;
    	}
      
}