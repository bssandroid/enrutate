package com.enrutatemio.adapter;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.enrutatemio.R;
import com.enrutatemio.objectos.ListadoRutasGoogle;

public class RecorridoGoogle extends ArrayAdapter<ListadoRutasGoogle> {
	
	private LayoutInflater vi;
	public RecorridoGoogle(Activity activity, final ArrayList<ListadoRutasGoogle> values) {
		super(activity, R.layout.recorrido, values);
		vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
	}
        public  class ViewHolder {
		private TextView direccion;
	
		public ViewHolder(View v) {
			this.direccion = (TextView) v.findViewById(R.id.estacion);
		
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = vi.inflate(R.layout.recorrido, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		

		final ListadoRutasGoogle item = getItem(position);
		if (item != null) 
			holder.direccion.setText((position + 1)+"- "+item.direccion);
			
		
		return convertView;
	}
}
	