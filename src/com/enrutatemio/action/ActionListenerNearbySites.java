package com.enrutatemio.action;

import android.view.View;
import android.view.View.OnClickListener;

import com.enrutatemio.mapa.ShowMapActivity;
import com.enrutatemio.util.ConnectionDetector;
import com.enrutatemio.util.Mensajes;

public class ActionListenerNearbySites implements OnClickListener {


	ShowMapActivity showMapActivity;
	
	public ActionListenerNearbySites(ShowMapActivity showMapActivity) {
		// TODO Auto-generated constructor stub
		this.showMapActivity = showMapActivity;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// sitios cercanos
		showMapActivity.touchMarker = null;
		if (showMapActivity.imHere != null)
			showMapActivity.touchMarker = showMapActivity.imHere;
		else if (showMapActivity.imGoingTo != null)
			showMapActivity.touchMarker = showMapActivity.imGoingTo;

		if (showMapActivity.touchMarker != null)
			if(ConnectionDetector.isConnectingToInternet(v.getContext()))
				showMapActivity.new LoadSitesFoursquare(showMapActivity.touchMarker.getPosition().latitude,showMapActivity.touchMarker.getPosition().longitude).execute();
			else
				Mensajes.mensajes(v.getContext(), "Debes tener acceso a internet", 1);
	}


}
