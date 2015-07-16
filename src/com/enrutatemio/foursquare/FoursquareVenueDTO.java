package com.enrutatemio.foursquare;


import java.util.Comparator;

import android.location.Location;

public class FoursquareVenueDTO  implements Comparator<FoursquareVenueDTO>{
      
	    public String id;
        public String name;
        public String address;
        public String type;
     
		public Location location;
        public int direction;
        public int distance;
      
		@Override
		public int compare(FoursquareVenueDTO lhs, FoursquareVenueDTO rhs) {
			// TODO Auto-generated method stub
			return lhs.name.compareTo(rhs.name);
			
		}
      

}
