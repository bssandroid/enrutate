package com.enrutatemio.action;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.enrutatemio.R;
import com.enrutatemio.adapter.TrayectoFinal;
import com.enrutatemio.mapa.ShowMapActivity;
import com.enrutatemio.objectos.ListadoRutasGoogle;
import com.enrutatemio.util.Mensajes;
import com.google.android.gms.maps.model.LatLng;

public class ActionListenerStopsTravel implements OnClickListener {

	ShowMapActivity showMapActivity;

	public ActionListenerStopsTravel (ShowMapActivity showMapActivity) {
		// TODO Auto-generated constructor stub
		this.showMapActivity = showMapActivity;
	}

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		try {

			if (showMapActivity.travelSelected.listaRutas.size() > 0) {

				Double viaje = showMapActivity.travelSelected.listaRutas.size() * 2.4;
				showMapActivity.duracionGoogle = String.valueOf(viaje.intValue());

				new AlertDialog.Builder(v.getContext())
						.setIcon(R.drawable.clock)

						.setTitle(" Tiempo: " + showMapActivity.duracionGoogle
										+ " m aprox.")
						.setAdapter(
								new TrayectoFinal(showMapActivity.getActivity(),
										showMapActivity.travelSelected.listaRutas),
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int po) {
										// TODO Auto-generated method stub
										ListadoRutasGoogle li = showMapActivity.travelSelected.listaRutas
												.get(po);

										LatLng position = new LatLng(
												li.geolocalizacion.latitude,
												li.geolocalizacion.longitude);
										showMapActivity.mapGoogle.vista3D(v.getContext(),li.geolocalizacion.latitude,li.geolocalizacion.longitude);
										
										if (showMapActivity.marcaGoogle != null)
											showMapActivity.marcaGoogle.remove();

										showMapActivity.marcaGoogle = showMapActivity.mapGoogle
												.addMarker(
														position,
														"Aquí",
														li.direccion,
														R.drawable.walk);
								}
									
									
						})
						.setNegativeButton("Quitar recorrido",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialog,
											int which) {

										showMapActivity.mapGoogle.limpiarMapa();
										showMapActivity.travelSelected.listaRutas
												.clear();
									

									}
								})
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialog,
											int which) {
										// Salir
										dialog.cancel();
									}
								}).show();
				
				showMapActivity.animate(showMapActivity.limpiarMapTodo);
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(v.getContext());
				Editor editort = sharedPreferences.edit();
				editort.putBoolean("ultimoSlide", true);
				editort.putBoolean("tutorial", true);
				editort.commit();
				showMapActivity.textotutorial
						.setText("Si quieres reiniciar el recorrido o limpiar el mapa, toca el botón");
				showMapActivity.imagentutorial.setVisibility(View.VISIBLE);
				showMapActivity.imagentutorial
						.setImageResource(R.drawable.limpiar_tuto);

			} else 
				Mensajes.mensajes(v.getContext(), "No hay recorridos", 1);

			
		} catch (Exception e) {
			Mensajes.mensajes(v.getContext(),
					"Ocurrio un error, vuelve a intentarlo", 1);
		}

	}
}
