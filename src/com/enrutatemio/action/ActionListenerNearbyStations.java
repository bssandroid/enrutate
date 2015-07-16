package com.enrutatemio.action;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.enrutatemio.R;
import com.enrutatemio.mapa.ShowMapActivity;

public class ActionListenerNearbyStations implements OnClickListener {


	ShowMapActivity showMapActivity;
	
	public ActionListenerNearbyStations(ShowMapActivity showMapActivity) {
		// TODO Auto-generated constructor stub
		this.showMapActivity = showMapActivity;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// estaciones o paradas cercanos
		showMapActivity.touchMarker = null;
		if (showMapActivity.imHere != null)
			showMapActivity.touchMarker = showMapActivity.imHere;

		if (showMapActivity.touchMarker != null) {
			double latitud = showMapActivity.touchMarker.getPosition().latitude;
			double longitud = showMapActivity.touchMarker.getPosition().longitude;

			showMapActivity.progress.show();
			showMapActivity.progress.setContentView(R.layout.custom_progress);
			TextView textprogress = (TextView) showMapActivity.progress
					.findViewById(R.id.textprogress);
			textprogress.setText("Buscando paradas cercanas...");
			showMapActivity.service.findNearbyStations(latitud, longitud);
		}
	}


}