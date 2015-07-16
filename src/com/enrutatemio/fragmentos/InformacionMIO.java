package com.enrutatemio.fragmentos;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.enrutatemio.R;
import com.enrutatemio.actividades.AlimentadoresActivity;
import com.enrutatemio.actividades.InfoRutasActivity;
import com.enrutatemio.actividades.Integracion;
import com.enrutatemio.actividades.PuntosRecargaActivity;

public class InformacionMIO extends SherlockFragment {

	public View v = null;
	
	public ImageView expreso, alimentadores, pretroncales, troncales,
			integracion, recargas;
	public TextView texto, pasaje;

	public InformacionMIO() {
		// TODO Auto-generated constructor stub
		setHasOptionsMenu(true);
	}
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		v = inflater.inflate(R.layout.tab2,null);
		init(v);
		
		// create your view using LayoutInflater
		return v;

	}


	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// initialise your views
		
		
		expreso.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				getActivity().finish();
				Intent i = new Intent(getActivity(), InfoRutasActivity.class);
				i.putExtra("tiporuta", "expreso");
				i.putExtra("mostrar", "Expresos");
				i.putExtra("tipo", 1);
				startActivity(i);

			}
		});

		alimentadores.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				getActivity().finish();
				Intent i = new Intent(getActivity(),
						AlimentadoresActivity.class);
				startActivity(i);
			}
		});

		pretroncales.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				getActivity().finish();
				Intent i = new Intent(getActivity(), InfoRutasActivity.class);
				i.putExtra("tiporuta", "pretroncal");
				i.putExtra("mostrar", "Pretroncales");
				i.putExtra("tipo", 2);
				startActivity(i);

			}
		});

		troncales.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				getActivity().finish();
				Intent i = new Intent(getActivity(), InfoRutasActivity.class);
				i.putExtra("tiporuta", "troncal");
				i.putExtra("mostrar", "Troncales");
				i.putExtra("tipo", 3);
				startActivity(i);

			}
		});

		integracion.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				getActivity().finish();
				Intent i = new Intent(getActivity(), Integracion.class);
				startActivity(i);
				getActivity().overridePendingTransition(
						R.anim.transition_frag_in, R.anim.transition_frag_out);

			}
		});
		recargas.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				getActivity().finish();
				Intent i = new Intent(getActivity(), PuntosRecargaActivity.class);
				startActivity(i);
			}
		});
	}
	

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.findItem(R.id.position).setVisible(false);
		menu.findItem(12).setVisible(false);
		menu.findItem(R.id.drawStations).setVisible(false);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@SuppressLint("NewApi")
	public void init(View view) {
		expreso = (ImageView) view.findViewById(R.id.expresos);
		alimentadores = (ImageView) view.findViewById(R.id.alimentadores);
		pretroncales = (ImageView) view.findViewById(R.id.pretroncales);
		troncales = (ImageView) view.findViewById(R.id.troncales);
		integracion = (ImageView) view.findViewById(R.id.integraciones);
		texto = (TextView) view.findViewById(R.id.hola);
		pasaje = (TextView) view.findViewById(R.id.pasaje);
		recargas = (ImageView) view.findViewById(R.id.recargas);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
			new cargarFecha().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			new cargarFecha().execute();
	}

	class cargarFecha extends AsyncTask<String, String, String> {

		String fecha = "";

		protected String doInBackground(String... args) {

			fecha = obtenerFecha();
			return null;
		}

		protected void onPostExecute(String file_url) {

			String[] fechaTratada = fecha.split(";");
			texto.setText(fechaTratada[0]);
			pasaje.setText("Valor pasaje " + fechaTratada[1] + " pesos");
		}

	}

	public String obtenerFecha() {

		Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH);
		String nmes = valor(cmonth);
		int cday = c.get(Calendar.DAY_OF_MONTH);
		int semana = c.get(Calendar.DAY_OF_WEEK);
		String valor = "1700";
		String diasemana = diasemana(semana);
	/*	if (dias[semana - 1].equalsIgnoreCase("Domingo")
				|| dias[semana - 1].equalsIgnoreCase("Sabado")) {
			valor = "1500";
		}*/
		String fecha = "Hoy " + diasemana + " " + cday + " de " + nmes + " de "
				+ cyear;

		return fecha + ";" + valor;

	}

	public String valor(int a) {
		String mes = "";

		if (a == 0) 
			mes = "Enero";
		else if (a == 1)
			mes = "Febrero";
		else if (a == 2) 
			mes = "Marzo";
		else if (a == 3)
			mes = "Abril";
		else if (a == 4) 
			mes = "Mayo";
		else if (a == 5) 
			mes = "Junio";
		else if (a == 6) 
			mes = "Julio";
		else if (a == 7) 
			mes = "Agosto";
		else if (a == 8) 
			mes = "Septiembre";
		else if (a == 9) 
			mes = "Octubre";
		else if (a == 10) 
			mes = "Noviembre";
		else if (a == 11) 
			mes = "Diciembre";
		
		return mes;

	}

	public String diasemana(int dia) {
		String semana = "";
		if (dia == 1) 
			semana = "Domingo";
		else if (dia == 2) 
			semana = "Lunes";
		else if (dia == 3) 
			semana = "Martes";
		else if (dia == 4) 
			semana = "Miercoles";
		else if (dia == 5) 
			semana = "Jueves";
		else if (dia == 6) 
			semana = "Viernes";
		else if (dia == 7) 
			semana = "Sabado";
		
		return semana;
	}


}
