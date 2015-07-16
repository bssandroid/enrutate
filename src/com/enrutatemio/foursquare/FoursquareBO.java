package com.enrutatemio.foursquare;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationManager;

import com.enrutatemio.aplicacion.Utilidades;
import com.enrutatemio.mapa.JSONParser;


public class FoursquareBO {
        //URL del API de foursquare
        private static final String API_URL = "https://api.foursquare.com/v2/venues/search?ll=";
        private static final int METROS = 300;
        public FoursquareBO()
        {
        	
        }
        /**
         * Metodo para obtener los lugares cercanos con foursaquare: Ejemplo adaptado de : http://blog.doityourselfandroid.com/2011/09/05/integrate-foursquare-android-application/ 
         * @param latitude
         * @param longitude
         * @return
         * @throws Exception
         */
        public ArrayList<FoursquareVenueDTO> getNearby(double latitude, double longitude)  {
                ArrayList<FoursquareVenueDTO> venueList = new ArrayList<FoursquareVenueDTO>();          
                try {
                        // Latitud y longitud para la consulta del API de foursquare
                        String ll       = String.valueOf(latitude) + "," + String.valueOf(longitude);                   
                        String urlStr = API_URL + ll + "&"+"client_id=C5GPXVLBYWXFSN4ZCEPGRPAZXJETY0HNM5R4VX3XFH014Q3G" +"&" + "client_secret=OBBLG32GB4DYRYBQOM5G504V0M03WSLVTQ0FDZVZDXJDDDO2" +"&v="+Utilidades.getCurrentTimeStampyyyyMMdd();
                       // Log.d("FECHA: ", Utilidades.getCurrentTimeStampyyyyMMdd());
                        // Se usa restTemplate por compatibilidad con Android ICS
                      
                        
                        JSONObject jsonObj      = JSONParser.getJSONFromUrl(urlStr);
                      
                        JSONArray items         = (JSONArray) jsonObj.getJSONObject("response").getJSONArray("venues");
                        // Se recorren los lugares cercanos
                        for (int i = 0, len = items.length(); i < len; ++i) {
                                JSONObject item = (JSONObject) items.get(i);                                    
                                FoursquareVenueDTO venue        = new FoursquareVenueDTO(); 
                                JSONObject location = (JSONObject) item.getJSONObject("location");
                                venue.distance  = location.getInt("distance");
                                if(venue.distance <= METROS)
                                {
	                                venue.id                = item.getString("id");
	                                venue.name              = item.getString("name");
	                                Location loc    = new Location(LocationManager.GPS_PROVIDER);
	                                loc.setLatitude(Double.valueOf(location.getString("lat")));
	                                loc.setLongitude(Double.valueOf(location.getString("lng")));
	                                venue.location  = loc;
	                                if(!location.isNull("address"))
	                                        venue.address   = location.getString("address");
	                               
	                                JSONArray categories = (JSONArray) item.getJSONArray("categories");
	                                if(categories.length() > 0)
	                                {
		                                String type = ((JSONObject)categories.get(0)).getJSONObject("icon").getString("prefix").replace("https://ss3.4sqi.net/img/categories_v2/", "").split("/")[0];
		                                venue.type = type;
	                                }
	                               // venue.herenow   = item.getJSONObject("hereNow").getInt("count");
	                                venueList.add(venue);
                                }
                               
                        }

                } catch (Exception ex) {
                       ex.printStackTrace();
                }
                
                return venueList;
        }
        
        
}
