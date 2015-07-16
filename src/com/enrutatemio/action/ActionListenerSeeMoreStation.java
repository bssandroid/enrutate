package com.enrutatemio.action;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

import com.enrutatemio.R;
import com.enrutatemio.BD.Consultas;
import com.enrutatemio.adapter.ListaRutas;
import com.enrutatemio.mapa.ShowMapActivity;
import com.enrutatemio.util.ConnectionDetector;
import com.enrutatemio.util.Mensajes;

public class ActionListenerSeeMoreStation implements OnClickListener {


	ShowMapActivity showMapActivity;
	
	public ActionListenerSeeMoreStation(ShowMapActivity showMapActivity) {
		// TODO Auto-generated constructor stub
		this.showMapActivity = showMapActivity;
	}

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		//detalles estacion seleccionada
		try {
			if (showMapActivity.ma != null) {

				String nombre = showMapActivity.ma.getTitle();
				String dir = Consultas.consultaListadoParadas(showMapActivity.getActivity(),showMapActivity.ma.getTitle()
						.replaceAll("ESTACIÓN ", "").trim());
				String[] tmp = dir.split(";");
				if (tmp != null && tmp.length > 0) {
					String dirParada = tmp[0];
					showMapActivity.rutas = tmp[1];

					AlertDialog.Builder dialog = new AlertDialog.Builder(
							v.getContext());
					dialog.setIcon(R.drawable.iconoestacionmapa);

					if (nombre.contains("ESTACIÓN "))
						nombre = nombre.replace("ESTACIÓN ", "");

					if (dirParada == null)
						dirParada = "";
					else
						dirParada = "\n" + "Dir: " + dirParada;

					dialog.setTitle(nombre + dirParada);
					dialog.setAdapter(new ListaRutas(v.getContext(),
							showMapActivity.rutas.split(",")),
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int item) {

									// ma.hideInfoWindow();
									showMapActivity.it = item;
									showMapActivity.paradasunsentido.clear();

									showMapActivity.new consultarPHPHilo()
											.execute(showMapActivity.rutas.split(",")[showMapActivity.it]
													.trim());
								}
							});

					dialog.setPositiveButton(R.string.salir,
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {

									// ma.hideInfoWindow();
									dialog.cancel();
								}
							});

					dialog.setNeutralButton("Sitios cercanos",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {

									double p1 = showMapActivity.ma.getPosition().latitude;
									double p2 = showMapActivity.ma.getPosition().longitude;
									if(ConnectionDetector.isConnectingToInternet(v.getContext()))
										showMapActivity.new LoadSitesFoursquare(p1, p2).execute();
									else
										Mensajes.mensajes(v.getContext(), "Debes tener acceso a internet", 1);
									
								}
							});

					dialog.show();

				} else {
					Mensajes.mensajes(
							v.getContext(),
							"Error al obtener información de la estación",
							1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.mensajes(v.getContext(),
					"Error al obtener información de la estación", 1);
		}
		
	}


}
