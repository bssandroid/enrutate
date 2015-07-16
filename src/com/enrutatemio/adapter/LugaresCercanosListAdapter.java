package com.enrutatemio.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enrutatemio.R;
import com.enrutatemio.foursquare.FoursquareVenueDTO;

/**
 * Clase encargada de construir el arrayadapter de los lugares cercanos
 *
 */
public class LugaresCercanosListAdapter extends ArrayAdapter<FoursquareVenueDTO> {
        private List<FoursquareVenueDTO> datos;
        //categories
        private static final String PARKS = "parks_outdoors";
        private static final String SHOPS = "shops";
        private static final String BUILDING = "building";
        private static final String TRAVEL = "travel";
        private static final String EDUCATION = "education";
        private static final String ARTS = "arts_entertainment";
        private static final String FOOD = "food";
        private static final String NIGHTLIFE = "nightlife";
        static  Typeface robot_regular;
        public LugaresCercanosListAdapter(Context context, List<FoursquareVenueDTO> objects){
                super(context, R.layout.list_view_lugares_cercanos, objects);
                datos = objects;
                robot_regular = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed_Regular.ttf");
        }
        
        @SuppressLint("ViewHolder")
		public View getView(int position, View convertView, ViewGroup parent) {         
            
        	
        	LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View item = inflater.inflate(R.layout.list_view_lugares_cercanos, parent, false);
            FoursquareVenueDTO foursquareVenueDTO = datos.get(position);
            TextView distancia = (TextView)item.findViewById(R.id.tv_distance);
            ImageView image = (ImageView)item.findViewById(R.id.image_site_foursquare);
            distancia.setText(Integer.toString(foursquareVenueDTO.distance)+"m");    
            TextView nombre = (TextView)item.findViewById(R.id.tv_name);
            nombre.setText(foursquareVenueDTO.name);                   
            TextView direccion = (TextView)item.findViewById(R.id.tv_address);
            String dir = "No disponible";
            nombre.setTypeface(robot_regular);
            direccion.setTypeface(robot_regular);
            distancia.setTypeface(robot_regular);
            
            if(foursquareVenueDTO.address != null && foursquareVenueDTO.address.length() > 0)
            	direccion.setText(foursquareVenueDTO.address); 	
            else
            	direccion.setText(dir); 
            
            
            image.setImageResource(R.drawable.foursquare_btn);
         /*   if(foursquareVenueDTO.type != null)
            {
	            if(foursquareVenueDTO.type.equals(PARKS))
	            	image.setImageResource(R.drawable.ic_acercade);
	            else if(foursquareVenueDTO.type.equals(SHOPS))
	            	image.setImageResource(R.drawable.ic_infomio);
	            else if(foursquareVenueDTO.type.equals(BUILDING))
	            	image.setImageResource(R.drawable.ic_noticias);
	            else if(foursquareVenueDTO.type.equals(TRAVEL))
	            	image.setImageResource(R.drawable.ic_noticias_nuevas);
	            else if(foursquareVenueDTO.type.equals(EDUCATION))
	            	image.setImageResource(R.drawable.ic_launcher);
	            else if(foursquareVenueDTO.type.equals(ARTS))
	            	image.setImageResource(R.drawable.ic_preferencias);
	            else if(foursquareVenueDTO.type.equals(FOOD))
	            	image.setImageResource(R.drawable.ic_transbordo);
	            else if(foursquareVenueDTO.type.equals(NIGHTLIFE))
	            	image.setImageResource(R.drawable.ic_twitter);
            }*/
                     
            
           
        return (item);
   }
        
        public List<FoursquareVenueDTO> getDatos() {
                return datos;
        }

    
        public void setDatos(List<FoursquareVenueDTO> datos) {
                this.datos = datos;
        }       
}
