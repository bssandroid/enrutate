package com.enrutatemio.action;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;

import com.enrutatemio.R;
import com.enrutatemio.mapa.ShowMapActivity;
import com.enrutatemio.objectos.PuntoRecarga;
import com.enrutatemio.util.Mensajes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class ActionListenerPuntosRecarga  implements OnClickListener {

	ShowMapActivity showMapActivity;
	
	public ActionListenerPuntosRecarga(ShowMapActivity showMapActivity) {
		// TODO Auto-generated constructor stub
		this.showMapActivity = showMapActivity;
	}

	@Override
	public void onClick(View v) {
	
		showMapActivity.touchMarker = null;
		if (showMapActivity.imHere != null)
			showMapActivity.touchMarker = showMapActivity.imHere;

		if (showMapActivity.touchMarker != null) {
			double latitud = showMapActivity.touchMarker.getPosition().latitude;
			double longitud = showMapActivity.touchMarker.getPosition().longitude;
			ArrayList<PuntoRecarga> puntos = showMapActivity.loadNearbyPoints(latitud,
					longitud, 500);
			if (puntos.size() == 0)
				puntos = showMapActivity.loadNearbyPoints(latitud, longitud, 1000);

			if (puntos != null && puntos.size() > 0) {
				if (showMapActivity.marcasRecargasCercanas != null
						&& showMapActivity.marcasRecargasCercanas.size() > 0) {
					for (int i = 0, len = showMapActivity.marcasRecargasCercanas.size(); i < len; ++i) {
						Marker tmp = showMapActivity.marcasRecargasCercanas.get(i);
						tmp.remove();
					}
					showMapActivity.marcasRecargasCercanas.clear();
				}

				for (int i = 0, len = puntos.size(); i < len; ++i) {

					PuntoRecarga puntoRecarga = puntos.get(i);

					Marker marker = showMapActivity.mapGoogle
							.addMarker(
									puntoRecarga.lat,
									puntoRecarga.lon,
									puntoRecarga.nombre + ".",
									puntoRecarga.direccion.equals("") ? puntoRecarga.tipo
											: (puntoRecarga.direccion
													+ ", Punto: " + puntoRecarga.tipo),
									R.drawable.iconotarjetarecarga);
					showMapActivity.marcasRecargasCercanas.add(marker);
				}

				showMapActivity.mapGoogle.map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(showMapActivity.touchMarker.getPosition().latitude,showMapActivity.touchMarker.getPosition().longitude), 14), 1400, null);

			} else
				Mensajes.mensajes(v.getContext(),
						"No hay puntos de recarga cercanos", 1);
		}
	}


}