package com.enrutatemio.action;

import android.view.View;
import android.view.View.OnClickListener;

import com.enrutatemio.R;
import com.enrutatemio.mapa.ShowMapActivity;
import com.google.android.gms.maps.GoogleMap;

public class ActionListenerChangeMap implements OnClickListener {


	ShowMapActivity showMapActivity;
	
	public ActionListenerChangeMap(ShowMapActivity showMapActivity) {
		// TODO Auto-generated constructor stub
		this.showMapActivity = showMapActivity;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// estaciones o paradas cercanos
		if (showMapActivity.mapGoogle != null) {
			if (showMapActivity.mapGoogle.map != null) {
				if (!showMapActivity.statusMap) {
					showMapActivity.mapGoogle.map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
					showMapActivity.changeMap.setImageResource(R.drawable.mapa_normal);
					showMapActivity.statusMap = true;

				} else {
					showMapActivity.mapGoogle.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					showMapActivity.changeMap.setImageResource(R.drawable.satelite);
					showMapActivity.statusMap = false;
				}

			}
		}
	}


}