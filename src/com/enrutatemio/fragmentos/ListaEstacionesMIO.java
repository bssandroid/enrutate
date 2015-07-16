package com.enrutatemio.fragmentos;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.enrutatemio.R;
import com.enrutatemio.BD.Consultas;
import com.enrutatemio.actividades.ParadasActivity;
import com.enrutatemio.adapter.ListaRutas;
import com.enrutatemio.adapter.ListadoEstaciones;
import com.enrutatemio.adapter.Recorrido2;
import com.enrutatemio.objectos.Estacion;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingLeftInAnimationAdapter;

@SuppressLint("NewApi")
public class ListaEstacionesMIO extends SherlockFragment implements TextWatcher {

	private ListView listaestaciones;
	private EditText buscar;
	public ListadoEstaciones adapter;
	private ArrayList<Estacion> pr = new ArrayList<Estacion>();
	private ArrayList<String> paradas = new ArrayList<String>();
	public ProgressDialog pDialog;
	public boolean status = false;
	public Estacion esta;
	private int flag = 0;
	private String cadena;
	private int it = 0;
	private String[] sentido = { "No hay sentido sur - norte en esta ruta",
			"No hay sentido norte - sur en esta ruta" };
	public View view;

	public ListaEstacionesMIO() {
		// TODO Auto-generated constructor stub
		setHasOptionsMenu(true);
	}
	@SuppressLint("NewApi")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// create your view using LayoutInflater
		view = inflater
				.inflate(R.layout.listadoestacionesmio, container, false);

		init(view);

		return view;
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
			new cargarEstaciones().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			new cargarEstaciones().execute();
		
		listaestaciones.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				esta = (Estacion) listaestaciones.getItemAtPosition(pos);
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						getActivity());

				dialog.setIcon(R.drawable.iconoestacionmapa);
				dialog.setTitle("Rutas " + esta.nombre + "\n" + esta.direccion);
				dialog.setAdapter(
						new ListaRutas(getActivity(), esta.paradas.split(",")),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								it = item;

								new consultarPHPHilo().execute(esta.paradas
										.split(",")[it].trim());
							}
						});

				dialog.setPositiveButton(R.string.salir,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								dialog.cancel();
							}
						});

				dialog.show();

			}
		});

	}

	public boolean buscarCoincidencia(ArrayList<String> paradas, String titulo) {
		flag = 0;
		boolean estado = false;
		for (int i = 0, len = paradas.size(); i < len; ++i) {
			String cadena = paradas.get(i).toLowerCase().trim();
			String subcadena = titulo.toLowerCase().trim();
			if (cadena.indexOf(subcadena) != -1)
				flag = flag + 1;
		}
		if (flag == 0)
			estado = false;
		else
			estado = true;

		return estado;
	}

	public void unSentido() {
		ArrayList<String> p1 = new ArrayList<String>();
		ArrayList<String> p2 = new ArrayList<String>();
		String[] resultado = null;
		resultado = cadena.split(";");

		String paradas1 = null, sentido1 = null, paradas2 = null, sentido2 = null, s1 = "", s2 = "";
		String[] partidas1 = null, partidas2 = null;
		if (resultado.length == 2) {
			paradas1 = resultado[0];
			sentido1 = resultado[1];
			partidas1 = paradas1.split(",");
			s1 = "";
			for (int i = 0, len = partidas1.length; i < len; ++i)
				p1.add(partidas1[i]);

			s1 = partidas1[0] + " - " + partidas1[partidas1.length - 1];
			boolean estadoUno = buscarCoincidencia(p1, esta.nombre);
			boolean estadoDos = false;
			if (estadoUno == true && estadoDos == false) {

				int dirSentido = 0;
				if (sentido1.equalsIgnoreCase("surnorte"))
					dirSentido = 0;
				else if (sentido1.equalsIgnoreCase("nortesur"))
					dirSentido = 1;

				Intent infoRuta = new Intent(getActivity(),
						ParadasActivity.class);
				infoRuta.putExtra("Ruta", esta.paradas.split(",")[it]);
				infoRuta.putExtra("sentido", dirSentido);
				infoRuta.putExtra("titulo", esta.nombre);
				infoRuta.putExtra("direccion", s1);
				getActivity().startActivity(infoRuta);

			}
		} else {
			try {
				paradas1 = resultado[0];
				sentido1 = resultado[1];
				partidas1 = paradas1.split(",");
				s1 = "";

				paradas2 = resultado[2];
				sentido2 = resultado[3];
				partidas2 = paradas2.split(",");
				s2 = "";
				for (int i = 0, len = partidas1.length; i < len; ++i)
					p1.add(partidas1[i]);

				for (int j = 0, lenp = partidas2.length; j < lenp; ++j)
					p2.add(partidas2[j]);

				s1 = partidas1[0] + " - " + partidas1[partidas1.length - 1];
				s2 = partidas2[0] + " - " + partidas2[partidas2.length - 1];
				boolean estadoUno = buscarCoincidencia(p1, esta.nombre);
				boolean estadoDos = buscarCoincidencia(p2, esta.nombre);

				if (estadoUno == true && estadoDos == false) {

					int dirSentido = 0;
					if (sentido1.equalsIgnoreCase("surnorte"))
						dirSentido = 0;
					else if (sentido1.equalsIgnoreCase("nortesur"))
						dirSentido = 1;

					Intent infoRuta = new Intent(getActivity(),
							ParadasActivity.class);
					infoRuta.putExtra("Ruta", esta.paradas.split(",")[it]);
					infoRuta.putExtra("sentido", dirSentido);
					infoRuta.putExtra("titulo", esta.nombre);
					infoRuta.putExtra("direccion", s1);
					getActivity().startActivity(infoRuta);

				} else if (estadoUno == false && estadoDos == true) {

					int dirSentido2 = 0;
					if (sentido2.equalsIgnoreCase("surnorte")) {
						dirSentido2 = 0;
					} else if (sentido2.equalsIgnoreCase("nortesur")) {
						dirSentido2 = 1;
					}
					Intent infoRuta = new Intent(getActivity(),
							ParadasActivity.class);
					infoRuta.putExtra("Ruta", esta.paradas.split(",")[it]);
					infoRuta.putExtra("sentido", 0);
					infoRuta.putExtra("titulo", esta.nombre);
					infoRuta.putExtra("direccion", s2);
					getActivity().startActivity(infoRuta);

				}
			} catch (Exception e) {
				Toast.makeText(getActivity(), "Error al cargar los sentidos",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public void consultasCodigo() {
		if (buscarCoincidenciaParadas(paradas, esta.nombre) >= 2) {
			// Tiene doble sentido
			String ruta = esta.paradas.split(",")[it].trim();
			// sentido = sentidodelarutaPHP(ruta, sentido);
			sentido = Consultas.sentidodelaruta(getActivity(), ruta, sentido);
			new AlertDialog.Builder(getActivity())
					.setIcon(R.drawable.inicio)
					.setTitle(
							"Ruta " + esta.paradas.split(",")[it].trim() + " "
									+ "Selecciona el sentido")
					.setAdapter(new Recorrido2(getActivity(), sentido),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {

									if (item == 0) {
										// sur - norte
										paradas.clear();
										
										new LoadAllStations(0).execute(
												esta.paradas.split(",")[it]
														.trim(), String
														.valueOf(0));

									} else if (item == 1) {
										// norte - sur
										paradas.clear();
										
										new LoadAllStations(1).execute(
												esta.paradas.split(",")[it]
														.trim(), String
														.valueOf(1));

									} else if (item == 2)
										dialog.cancel();

								}
							}).show();

		} else {
			// tiene un solo sentido esta ruta
			try {

				String ruta = esta.paradas.split(",")[it].trim();

				cadena = Consultas.consultaUnSentido(getActivity(), ruta);
				if (cadena.length() > 0)
					unSentido();
				else
					Toast.makeText(getActivity(),
							"la ruta " + ruta + " no se ha añadido al sistema",
							Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				Toast.makeText(getActivity(),
						"No se encontraron coincidencias", Toast.LENGTH_LONG)
						.show();
			}

		}
	}

	class LoadAllStations extends AsyncTask<String, String, String> {

		int opcion = 0;

		public LoadAllStations(int opcion) {
			this.opcion = opcion;
		}
		protected String doInBackground(String... args) {

			paradas = Consultas.consultaSentidos(getActivity(),
					esta.paradas.split(",")[it].trim(), opcion);

			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all empleados
			consultaParadasRuta(opcion);

		}

	}

	public void consultaParadasRuta(int opcion) {
		if (buscarCoincidencia(paradas, esta.nombre)) {
			Intent infoRuta = new Intent(getActivity(), ParadasActivity.class);
			infoRuta.putExtra("Ruta", esta.paradas.split(",")[it]);
			infoRuta.putExtra("sentido", opcion);
			infoRuta.putExtra("titulo", esta.nombre);
			infoRuta.putExtra("direccion", sentido[opcion]);
			getActivity().startActivity(infoRuta);

		} else {
			new AlertDialog.Builder(getActivity())
					.setIcon(R.drawable.inicio)
					.setTitle("Informacion")
					.setMessage(
							"La ruta "
									+ esta.paradas.split(",")[it].toUpperCase()
									+ " no para en la estacion " + esta.nombre
									+ " en este sentido")
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									Intent infoRuta = new Intent(getActivity(),
											FragmentChangeActivity.class);
									infoRuta.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									getActivity().startActivity(infoRuta);
									dialog.cancel();

								}
							}).show();
		}
	}

	class consultarPHPHilo extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Buscando sentidos...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		protected String doInBackground(String... args) {

			paradas = Consultas.consultaAmbosSentidos(getActivity(), args[0]);
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all empleados
			pDialog.dismiss();
			consultasCodigo();

		}
	}

	public int buscarCoincidenciaParadas(ArrayList<String> paradas,
			String titulo) {
		flag = 0;

		for (int i = 0, len = paradas.size(); i < len; ++i) {
			String cadena = paradas.get(i).toLowerCase().trim();
			String subcadena = titulo.toLowerCase().trim();
			if (cadena.indexOf(subcadena) != -1)
				flag = flag + 1;

		}

		return flag;
	}



	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		try {
			adapter.getFilter().filter(s.toString());
		} catch (Exception er) {
			er.printStackTrace();
		}

	}

	class cargarEstaciones extends AsyncTask<String, String, String> {

		TextView textprogress;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(getActivity());
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			pDialog.setContentView(R.layout.custom_progress);
			textprogress = (TextView) pDialog.findViewById(R.id.textprogress);
			textprogress.setText("Cargando estaciones...");

		}

		protected String doInBackground(String... args) {
			pr.clear();
			pr = Consultas.cargarEstacionesLista(getActivity());

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();

			try {
				adapter = new ListadoEstaciones(getActivity(), pr);
				AnimationAdapter animAdapter = new SwingLeftInAnimationAdapter(
						adapter);
				animAdapter.setAbsListView(listaestaciones);
				listaestaciones.setAdapter(animAdapter);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void init(View view) {
		listaestaciones = (ListView) view.findViewById(R.id.listaestacionesmio);
		buscar = (EditText) view.findViewById(R.id.filtroestaciones);
		listaestaciones.setTextFilterEnabled(true);
		buscar.addTextChangedListener(this);
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(buscar.getWindowToken(), 0);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.findItem(R.id.position).setVisible(false);
		menu.findItem(12).setVisible(false);
		menu.findItem(R.id.drawStations).setVisible(false);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	
}// cierre class

