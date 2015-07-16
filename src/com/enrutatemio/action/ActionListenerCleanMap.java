package com.enrutatemio.action;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.enrutatemio.R;
import com.enrutatemio.mapa.ShowMapActivity;
import com.enrutatemio.util.Mensajes;
import com.enrutatemio.util.Visibility;

public class ActionListenerCleanMap implements OnClickListener {
	
	ShowMapActivity showMapActivity;
	
	public ActionListenerCleanMap(ShowMapActivity showMapActivity) {
		// TODO Auto-generated constructor stub
		this.showMapActivity = showMapActivity;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (showMapActivity.travelSelected.isDrawed()) {
			//inicio dialogo
			Typeface robot_regular = Typeface.createFromAsset(v.getContext().getAssets(), "RobotoCondensed_Regular.ttf");
			final Dialog dialog = new Dialog(v.getContext());
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.setContentView(R.layout.modal_material_design);
			dialog.setCancelable(false);
		
			// set the custom dialog components - text, image and button
			TextView title_modal = (TextView) dialog.findViewById(R.id.title_modal);
			TextView text_modal = (TextView) dialog.findViewById(R.id.text_modal);
			Button dialogButton = (Button) dialog.findViewById(R.id.accept_modal);
			Button dialogButtonCancel = (Button) dialog.findViewById(R.id.cancel_modal);
			title_modal.setText("Enrútate mio");
			text_modal.setText("¿Deseas quitar el recorrido que hay en el mapa?");
			title_modal.setTypeface(robot_regular, Typeface.BOLD);
			text_modal.setTypeface(robot_regular);
			Visibility.visible(dialogButtonCancel);

			
			dialogButton.setTypeface(robot_regular);
			dialogButtonCancel.setTypeface(robot_regular);
			// if button is clicked, close the custom dialog
		
			dialogButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showMapActivity.mapGoogle.limpiarMapa();
					dialog.dismiss();
				}
			});
			
			
			// if button is clicked, close the custom dialog
			dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();
			//fin dialogo material design

		} else {
			try {
				showMapActivity.mapGoogle.limpiarMapa();
			} catch (Exception e) {
				Mensajes.mensajes(v.getContext(),
						"Error limpiando el mapa", 1);
				e.printStackTrace();
			}
		}
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(v.getContext());
		showMapActivity.textotutorial
				.setText("Buscar direcciones, barrios o lugares \n ó \n Descubre todas las opciones que Enrútate tiene para tí.");
		showMapActivity.imagentutorial.setVisibility(View.VISIBLE);
		showMapActivity.imagentutorial.setImageResource(R.drawable.mas_opciones);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("tutorial", true);
		editor.commit();
	}

}
