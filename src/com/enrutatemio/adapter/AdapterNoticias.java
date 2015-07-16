package com.enrutatemio.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enrutatemio.R;
import com.enrutatemio.objectos.Noticia;
import com.enrutatemio.util.DateEnrutate;

public class AdapterNoticias extends ArrayAdapter<Noticia> {
	
	private LayoutInflater vi;
	public Typeface robot_regular;
	
	public AdapterNoticias(Activity activity, final ArrayList<Noticia> values) {
		super(activity, R.layout.noticiasadapter, values);
		vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		robot_regular = Typeface.createFromAsset(activity.getAssets(), "RobotoCondensed_Regular.ttf");
		
	}
        public class ViewHolder {
		private TextView mensaje;
		private TextView fecha;
		private RelativeLayout layoutnoticia;
		private ImageView  imagenNoticia;
		

		public ViewHolder(View v) {
			this.mensaje       = (TextView) v.findViewById(R.id.noticiasmensaje);
			this.fecha         = (TextView) v.findViewById(R.id.fechanoticia);
			this.layoutnoticia = (RelativeLayout) v.findViewById(R.id.layoutnoticiasid);
			this.imagenNoticia = (ImageView) v.findViewById(R.id.imagenNoticia);
			this.mensaje.setTypeface(robot_regular);
			this.fecha.setTypeface(robot_regular);
		}
	}

	
	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = vi.inflate(R.layout.noticiasadapter, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else 
			holder = (ViewHolder) convertView.getTag();
		
       
		final Noticia item = getItem(position);
		if (item != null) 
		{
			
			holder.mensaje.setText(item.noticia);
			
			if(item.noticia.contains("#ATENCIÓN"))
			{

				String horaPublicada = DateEnrutate.getUpdate(item.fecha);
				holder.fecha.setText("" + horaPublicada);
			}
			else
				holder.fecha.setText("Publicada el "+item.fecha);
			
			
			if(item.estado.equalsIgnoreCase("R"))
			{
				holder.layoutnoticia.setBackgroundColor(Color.TRANSPARENT);
				holder.mensaje.setTextColor(Color.parseColor("#88CCCCCC"));
				holder.fecha.setTextColor(Color.parseColor("#88CCCCCC"));
				holder.imagenNoticia.setImageResource(R.drawable.ic_noticias);
				holder.imagenNoticia.setAlpha(128);
				
			}
			else
			{
				
				holder.layoutnoticia.setBackgroundColor(Color.parseColor("#aab7e1f4"));
				holder.mensaje.setTextColor(Color.WHITE);
				holder.fecha.setTextColor(Color.parseColor("#57697d"));
				holder.imagenNoticia.setImageResource(R.drawable.ic_launcher);
		
			}
		}
		return convertView;
	}
	
	
}
	