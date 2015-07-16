package com.enrutatemio.actividades;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.enrutatemio.R;
import com.enrutatemio.BD.Consultas;
import com.enrutatemio.adapter.Adaptadorparadas;
import com.enrutatemio.fragmentos.FragmentChangeActivity;
import com.enrutatemio.mapa.JSONParser;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingLeftInAnimationAdapter;

public class ParadasActivity extends SherlockActivity {

	private ArrayList<String> paradas = new ArrayList<String>();
	public  Typeface robot_regular;
	public  ArrayAdapter<String> adaptador;
	public  String[] sentidoruta = { "Sur- norte", "Norte - sur" };
	public  String[] opciones = { "Ver rutas", "Salir" };
	public  int flag = 0, postItem = 0, desde,sentido;
	public  ListView lista;
	public  String parada, ruta, pal ="";
	public  JSONParser jParser = new JSONParser();
	private ProgressDialog pDialog;
	public  JSONArray estaciones = null;
	public  JSONArray ruta_paradas = null;
	public  TextView tx, tx2;
	public  String nombre_ruta, titulo, dir, tratado, tipoTratado;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paradas);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		lista = (ListView) findViewById(R.id.ListView02);
		tx = (TextView) findViewById(R.id.texto1);
		tx2 = (TextView) findViewById(R.id.texto2);
		robot_regular = Typeface.createFromAsset(this.getAssets(),
				"RobotoCondensed_Regular.ttf");
		tx.setTypeface(robot_regular,Typeface.BOLD);
		tx2.setTypeface(robot_regular);
		Bundle extras = getIntent().getExtras();

		if (extras == null) {
			return;
		}
		nombre_ruta = extras.getString("Ruta");
		titulo = extras.getString("titulo");
		dir = extras.getString("direccion");
		sentido = extras.getInt("sentido");
		desde = extras.getInt("desde");
		tratado = nombre_ruta.toLowerCase().trim();
		
		paradas.clear();
		
		
		getSupportActionBar().setTitle(" Ruta: " + nombre_ruta );
		
		// paradas = consultaPHP(tratado, sentido);
		new LoadAllStations().execute(tratado, String.valueOf(sentido));

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		finish();
		overridePendingTransition(R.anim.transition_frag_in, R.anim.transition_frag_out);
		return super.onOptionsItemSelected(item);
	}

	
	class LoadAllStations extends AsyncTask<String, String, String> {

		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(ParadasActivity.this);
			pDialog.setMessage("Cargando paradas...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}
		protected String doInBackground(String... args) {

			tipoTratado = Consultas.tipoParada(ParadasActivity.this,titulo);
			paradas = Consultas.consulta(ParadasActivity.this,tratado, sentido);

			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all empleados
			pDialog.dismiss();
			
			if (paradas != null && paradas.size() > 0)
				tmp2();
			else
				Toast.makeText(ParadasActivity.this,"No existen paradas para esta ruta " + tratado + " en BD",Toast.LENGTH_LONG).show();
			
		}

	}

	// consulta
	public void tmp2() {
		flag = 0;
		for (int i = 0, len = paradas.size(); i < len; ++i) {
			String cadena = paradas.get(i).toLowerCase().trim();
			String subcadena = titulo.toLowerCase().trim();
			
			if (cadena.indexOf(subcadena) != -1) 
				++flag;
			
			
		}
		if (desde == 1) {
			tx.setText("Recorrido de la Ruta: " + nombre_ruta.toUpperCase());
			setTitle("Ruta " + nombre_ruta.toUpperCase());
			tx2.setText("" + dir/*
								 * "Desde "+dir.toLowerCase().trim().replace("-",
								 * "A")
								 */);
			Adaptadorparadas adapter = new Adaptadorparadas(
					ParadasActivity.this, paradas, titulo, desde);
			AnimationAdapter animAdapter = new SwingLeftInAnimationAdapter(
					adapter);
			animAdapter.setAbsListView(lista);
			lista.setAdapter(animAdapter);
			
		} else {
			if (flag == 0) {
				new AlertDialog.Builder(ParadasActivity.this)
						.setIcon(R.drawable.inicio)
						.setTitle("Información")
						.setMessage(
								"La ruta " + tratado.toUpperCase()
										+ " no para en la estación " + titulo
										+ " en este sentido")
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										finish();
										Intent intento = new Intent(
												ParadasActivity.this,
												FragmentChangeActivity.class);
										intento.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(intento);
										dialog.cancel();

									}
								}).show();
			} else {
				tx.setText(tipoTratado + " " + titulo + ": "
						+ nombre_ruta.toUpperCase());
				tx2.setText("" + dir);
				Adaptadorparadas adapter = new Adaptadorparadas(
						ParadasActivity.this, paradas, titulo, desde);
				AnimationAdapter animAdapter = new SwingLeftInAnimationAdapter(
						adapter);
				animAdapter.setAbsListView(lista);
				lista.setAdapter(animAdapter);
			
			}
		}
	}


}
