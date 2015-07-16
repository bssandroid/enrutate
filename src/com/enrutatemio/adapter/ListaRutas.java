package com.enrutatemio.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enrutatemio.R;

@SuppressLint("DefaultLocale")
public class ListaRutas extends ArrayAdapter<String> {
	private LayoutInflater vi;
	
	//private Activity activity;
	private static int mResource = R.layout.listarutas;
	
	
	public ListaRutas(Context activity, final String [] values) {
		super(activity, mResource, values);
		vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
	}
        public static class ViewHolder {
        	private TextView textView ;
    		private ImageView imageView ;

		public ViewHolder(View v) {
			this.textView = (TextView) v
					.findViewById(R.id.rutasparadaoverlay);
			
			this.imageView = (ImageView) v
					.findViewById(R.id.imagenruta);
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) 
		{
			convertView = vi.inflate(mResource, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} 
		else 
			holder = (ViewHolder) convertView.getTag();
		

		final String item = getItem(position);
		if (item != null) {
			holder.textView.setText(" "+item.toUpperCase().trim());
			char letra = item.charAt(0);
			
			if(letra =='E')
			{
				//buses expresos
				holder.textView.setBackgroundColor(Color.parseColor("#ECDF3E"));
				
				holder.imageView.setImageResource(R.drawable.busexpreso);
			}
			else if(letra == 'P')
			{
				//buses pretroncal
				holder.textView.setBackgroundColor(Color.parseColor("#2E79B9"));
				holder.imageView.setImageResource(R.drawable.buspretroncal);
			}
			else if(letra == 'T')
			{
				//bus trocal
				holder.textView.setBackgroundColor(Color.parseColor("#E30613"));
				holder.imageView.setImageResource(R.drawable.bustroncal);
			}
			else if(letra == 'A')
			{
				//bus alimentador
				holder.textView.setBackgroundColor(Color.parseColor("#5FB985"));
				holder.imageView.setImageResource(R.drawable.busalimentador);
			}
			else
				holder.imageView.setImageResource(R.drawable.busalimentador);
			
			
			
			

		}
		return convertView;
	}
	
		  

}