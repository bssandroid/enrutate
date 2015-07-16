package com.enrutatemio.actividades;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.enrutatemio.R;
import com.enrutatemio.BD.AdminSQLiteOpenHelper;
import com.enrutatemio.adapter.ManejoRutas;
import com.enrutatemio.adapter.Recorrido2;
import com.enrutatemio.fragmentos.FragmentChangeActivity;
import com.enrutatemio.objectos.Ruta_paradas;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingLeftInAnimationAdapter;

public class AlimentadoresActivity extends SherlockActivity implements TextWatcher{
	
	static ManejoRutas adapter;
	private ListView listaalimentadores;
	
	private EditText buscar;
	private Spinner  combo;
	private ArrayList<Ruta_paradas> alimentadores = new ArrayList<Ruta_paradas>();
	private String[] sentidoruta = {"sentido sur - norte", "No hay sentido norte - sur en esta ruta"};
	int postItem = 0;
	String ruta = "";
	private String parada = "";
	private ProgressDialog pDialog;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listaalimentadores);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    //inicializo componentes de la GUI
		initComponentes();
	
		   
		
		//sur, centro,norte,todas
		combo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		   
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int pos, long id) {
				// TODO Auto-generated method stub
				long zonaEscogida = parent.getItemIdAtPosition(pos);
				String zona = "";
				if(zonaEscogida == 3)
				{
					zona = "sur";
					//texto.setText("Rutas alimentadoras zona Sur");
					setTitle("Alimentadores zona Sur");
				}
				else if(zonaEscogida == 1)
				{
					zona = "centro";
					//texto.setText("Rutas alimentadoras zona Centro");
					setTitle("Alimentadores zona Centro");
				}
				else if(zonaEscogida == 2)
				{
					zona = "norte";
					//texto.setText("Rutas alimentadoras zona Norte");
					setTitle("Alimentadores zona Norte");
				}
				else if(zonaEscogida == 0)
				{
					zona = "todas";
					//texto.setText("Todas las rutas alimentadoras");
					setTitle("Todas las rutas alimentadoras");
				}
				else if(zonaEscogida == 4)
				{
					zona = "oriente";
					//texto.setText("Rutas alimentadoras zona Oriente");
					setTitle("Alimentadores zona Oriente");
				}
				else if(zonaEscogida == 5)
				{
					zona = "occidente";
					//texto.setText("Rutas alimentadoras zona Occidente");
					setTitle("Alimentadores zona Occidente");
				}
			
				alimentadores = cargarAlimentadores(zona);
				adapter = new ManejoRutas(AlimentadoresActivity.this, alimentadores, 4);
				 AnimationAdapter animAdapter = new SwingLeftInAnimationAdapter(adapter);
			      animAdapter.setAbsListView(listaalimentadores);
				listaalimentadores.setTextFilterEnabled(true);
				buscar.addTextChangedListener(AlimentadoresActivity.this); 
				listaalimentadores.setAdapter(animAdapter);
				   
				adapter.notifyDataSetChanged();
				//Mensajes.mensajes(AlimentadoresActivity.this, "zona "+ zonaEscogida+ " "+ parent.getItemIdAtPosition(pos), 1);
				
					

				
			}
		});
		/*listaalimentadores.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				postItem = position;
				Ruta_paradas rutaParadas = (Ruta_paradas)listaalimentadores.getItemAtPosition(postItem);
				ruta = rutaParadas.getRuta().toLowerCase().trim();
		
				new sentidosHilo().execute(ruta);			
				
			}
		});*/
		
	
	
	}

	 class sentidosHilo extends AsyncTask<String, String, String> {

			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				
				pDialog = new ProgressDialog(AlimentadoresActivity.this);
				pDialog.setMessage("Consultando sentidos..");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
				
				
			}

			/**
			 * getting All empleados from url
			 * */
			protected String doInBackground(String... args) {
				
			//	runOnUiThread(new Runnable() {
					//public void run() {
				     
			     	//sentidodelarutaPHP(args[0],	sentidoruta);
				//status = false;
				//	}	
					//});
				sentidodelaruta(ruta, sentidoruta);

				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog after getting all empleados
				pDialog.dismiss();
				
				busquedaSentidos();
				/*if(status)
				{
				   busquedaSentidos();
		        	      
				}
				else
				{
					
				}*/
				
			}

		}
	 public String[] sentidodelaruta(String ruta, String g[]) {

			AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
			SQLiteDatabase bd = admin.getWritableDatabase();
			String sen = "";
			String paradas = "";
			Cursor fila = bd.rawQuery(
					"select sentido, paradas from ruta_paradas where ruta LIKE '%"
							+ ruta + "%'", null);

			if (fila != null) {
				while (fila.moveToNext()) {
					sen = fila.getString(0);
					paradas = fila.getString(1);
					if (sen.equalsIgnoreCase("surnorte")) {
						String par[] = paradas.split(",");
						g[0] = par[0] + " - " + par[par.length - 1];

					} else {
						String par[] = paradas.split(",");
						g[1] = par[0] + " - " + par[par.length - 1];

					}
				}
				//g[2] = "Salir";
				fila.close();
				bd.close();
				admin.close();
			}

			return g;

		}
	 public void busquedaSentidos()
	 {
		 new AlertDialog.Builder(
				 AlimentadoresActivity.this)
					.setIcon(R.drawable.inicio)
					.setTitle("Ruta "+ ruta.toUpperCase()+ " Selecciona el sentido")
					.setAdapter(new Recorrido2(AlimentadoresActivity.this,sentidoruta),
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int item) {
									String para = parada;

									if (item == 0) {
										// sur -
										// norte

										Intent infoRuta = new Intent(AlimentadoresActivity.this,
												ParadasActivity.class);
										infoRuta.putExtra("Ruta",ruta);
										infoRuta.putExtra("sentido",0);
										infoRuta.putExtra("titulo",para);
										infoRuta.putExtra("direccion",sentidoruta[0]);
										infoRuta.putExtra("desde", 1);
										AlimentadoresActivity.this.startActivity(infoRuta);
										//InfoRutasActivity.this.stopService(infoRuta);

									} else if (item == 1) {
										// norte
										// - sur

										Intent infoRuta = new Intent(
												AlimentadoresActivity.this,
												ParadasActivity.class);
										infoRuta.putExtra("Ruta",ruta);
										infoRuta.putExtra("sentido",1);
										infoRuta.putExtra("titulo",para);
										infoRuta.putExtra("direccion",sentidoruta[1]);
										infoRuta.putExtra("desde", 1);
										AlimentadoresActivity.this.startActivity(infoRuta);
										//InfoRutasActivity.this.stopService(infoRuta);

									} 
								}
							})

					.setPositiveButton(
							"Volver al mapa",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
                                    finish();
									Intent infoRuta = new Intent(
											AlimentadoresActivity.this,
											FragmentChangeActivity.class);
									infoRuta.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									AlimentadoresActivity.this
											.startActivity(infoRuta);
									/*InfoRutasActivity.this
											.stopService(infoRuta);*/

								}
							})
					.setNegativeButton(
							R.string.salir,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {

									dialog.cancel();

								}
							}).show();
	 }
	public ArrayList<Ruta_paradas> cargarAlimentadores(String zona)
	{
		 ArrayList<Ruta_paradas> alimen = new ArrayList<Ruta_paradas>();
		 AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
		 SQLiteDatabase bd = admin.getWritableDatabase();
			Cursor fila = null;
			if(zona.equalsIgnoreCase("todas"))
			{
				fila = bd.rawQuery(
						"select DISTINCT(ruta), trayecto,horario, paradas from ruta_paradas where tipo = 'alimentador' and estado = 'A' and sentido = 'nortesur'", null);
				Log.d("rutas","ejecuto query");
			}
			else
			{
				fila = bd.rawQuery(
						"select DISTINCT(ruta), trayecto,horario, paradas from ruta_paradas where zona = '" + zona
								+ "' and tipo = 'alimentador' and estado = 'A' and sentido = 'nortesur' ", null);
	
			}

		

			if (fila != null) {
				while (fila.moveToNext())
				{
					Log.d("rutas","info: "+fila.getString(0) + " "+ fila.getString(1) );
					//Ruta_paradas rutaParadas = new Ruta_paradas(fila.getString(0),"","" ,"");
					Ruta_paradas rutaParadas = new Ruta_paradas(fila.getString(0),fila.getString(1) ,fila.getString(2),fila.getString(3));
	                alimen.add(rutaParadas);

				}

			} 
			//Log.d("rutas",""+rutas.size());
			fila.close();
			bd.close();
			admin.close();
		 
		 
		 
		 
		 return alimen;
	}
	public void initComponentes()
	{
		listaalimentadores = (ListView) findViewById(R.id.listaalimentadores);
		buscar             = (EditText) findViewById(R.id.filtroalimentadores);
		combo              = (Spinner)  findViewById(R.id.spinner1);
		//Creamos el adaptador
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.zonas,android.R.layout.simple_spinner_item);
		//Añadimos el layout para el menú
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//Le indicamos al spinner el adaptador a usar
		combo.setAdapter(adapter);
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
		adapter.getFilter().filter(s.toString());
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finishedActivity();
		super.onBackPressed();

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		finishedActivity();
		return super.onOptionsItemSelected(item);
	}

	public void finishedActivity() {
		finish();
		Intent intento = new Intent(AlimentadoresActivity.this,
				FragmentChangeActivity.class);
		intento.putExtra("fromnews", 1);
		startActivity(intento);
		overridePendingTransition(R.anim.transition_frag_in,
				R.anim.transition_frag_out);
	}

}