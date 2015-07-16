package com.enrutatemio.mapa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.enrutatemio.R;
import com.enrutatemio.BD.AdminSQLiteOpenHelper;
import com.enrutatemio.BD.Consultas;
import com.enrutatemio.action.ActionListenerChangeMap;
import com.enrutatemio.action.ActionListenerCleanMap;
import com.enrutatemio.action.ActionListenerNearbySites;
import com.enrutatemio.action.ActionListenerNearbyStations;
import com.enrutatemio.action.ActionListenerPuntosRecarga;
import com.enrutatemio.action.ActionListenerSeeMoreStation;
import com.enrutatemio.action.ActionListenerStopsTravel;
import com.enrutatemio.actividades.ParadasActivity;
import com.enrutatemio.adapter.ListaRutas;
import com.enrutatemio.adapter.LugaresCercanosListAdapter;
import com.enrutatemio.adapter.Recorrido2;
import com.enrutatemio.foursquare.FoursquareBO;
import com.enrutatemio.foursquare.FoursquareVenueDTO;
import com.enrutatemio.objectos.ListadoRutasGoogle;
import com.enrutatemio.objectos.PuntoRecarga;
import com.enrutatemio.planificador.MessageCodes;
import com.enrutatemio.planificador.NearbyPoint;
import com.enrutatemio.planificador.NearbyStops;
import com.enrutatemio.planificador.Service;
import com.enrutatemio.planificador.Travel;
import com.enrutatemio.util.ConnectionDetector;
import com.enrutatemio.util.DialogEnrutate;
import com.enrutatemio.util.MapGoogle;
import com.enrutatemio.util.Mensajes;
import com.enrutatemio.util.Visibility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class ShowMapActivity extends SherlockFragment implements
		OnMarkerClickListener, LocationListener, OnMapClickListener,
		OnInfoWindowClickListener {
	//private float[] mRotationMatrix = new float[16];
	public ArrayList<Marker> marcasParadasCercanas = new ArrayList<Marker>();
	public ArrayList<PuntoRecarga> puntoRecargas = new ArrayList<PuntoRecarga>();
	public ArrayList<Marker> marcasRecargasCercanas = new ArrayList<Marker>();
	public ProgressDialog pDialog;
	public GMapV2Direction md;
	public MapGoogle mapGoogle;
	public Context cxt;
	public ArrayList<String> paradas = new ArrayList<String>();
	public ArrayList<String> paradasunsentido = new ArrayList<String>();
	public List<Marker> marcasFoursquare = new ArrayList<Marker>();
	public String[] sentido = { "No hay sentido sur - norte en esta ruta",
			"No hay sentido norte - sur en esta ruta" };
	public int flag = 0, it = 0;
	public Polyline lineas, lineas1, lineas2;
	public static final String LESS_BUS = "lessBuses";
	public static final String LESS_WALK = "lessWalk";
	public Marker marcaGoogle, finRecorrido, touchMarker, inicioRecorrido,
			marca, ma, imHere, imGoingTo;
	public LinearLayout footermap, anysite;
	public Button vermas, nearbystations, puntoRecargamio;
	public FrameLayout contenedortutorial;
	public ArrayList<ListadoRutasGoogle> listaRutas = new ArrayList<ListadoRutasGoogle>();
	public String duracionGoogle, modo = "", direBuscar, mode = "start", rutas,
			tmp2 = "", cadena = "";
	public EditText input;
	public HashMap<String, LatLng> resultadoBusqueda = new HashMap<String, LatLng>();
	public String[] elementos = null;
	public ArrayList<FoursquareVenueDTO> mNearbyList;
	public FoursquareBO fours;
	public ArrayList<Marker> marcasenmapa = new ArrayList<Marker>();
	public SharedPreferences sharedPreferences;
	public View v, viewMapa, viewLimpiar;
	public ProgressDialog progress;
	public Service service;
	public NearbyStops nearbyStops = new NearbyStops();
	public Handler messenger;
	public Vibrator vibe = null;
	public TextView textotutorial, omitirtutorial, textobienvenida;
	public ImageView imagentutorial, limpiarMapTodo, changeMap
			,nearbysites;
	
	//public ImageView change_travel;
	public boolean tutorial;
	public boolean statusMap = false;
	public Travel travelSelected = new Travel();
	public int indexTravelSelected = 0;
	public List<Travel> listTravels = new ArrayList<Travel>();
	public LinearLayout container_steps;
	
	//public int flag_change_travel = 0;

	// device sensor manager

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (v != null) {
			ViewGroup parent = (ViewGroup) v.getParent();
			if (parent != null)
				parent.removeView(v);
		}
		try {
			v = inflater.inflate(R.layout.main, container, false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return init(v);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ViewServer.get(getActivity()).removeWindow(getActivity());
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// do your variables initialisations here except Views!!!

		new loadPoints().execute();

		listTravels.add(new Travel());
		listTravels.add(new Travel());
		ViewServer.get(getActivity()).addWindow(getActivity());
		// initialize your android device sensor capabilities

	}
	

	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        
		
		
		
		tutorial = sharedPreferences.getBoolean("tutorial", false);
		if (tutorial)
			if (contenedortutorial.getVisibility() == View.VISIBLE)
				contenedortutorial.setVisibility(View.GONE);
		try {
			int status = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(getActivity());
			if (status != ConnectionResult.SUCCESS) {
				int requestCode = 10;
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
						getActivity(), requestCode);
				dialog.show();
			} else {

				mapGoogle = new MapGoogle(this, this);
				// // init listeners map
				mapGoogle.setOnMapClickListener(this);
				mapGoogle.setOnMarkerClickListener(this);
				mapGoogle.setInfoWindowClickListener(this);

				if (!ConnectionDetector.isConnectingToInternet(getActivity())) {
					Mensajes.mensajes(
							getActivity(),
							"Debes tener acceso a internet o datos para cargar el mapa",
							0);
				}

				progress = new ProgressDialog(getActivity());
				progress.setIndeterminate(false);
				progress.setCancelable(false);
				messenger = new Handler() {
					@Override
					public void handleMessage(Message msg) {

						switch (msg.arg1) {
						case MessageCodes.CONNECTED:
							DialogEnrutate.createDialog(getActivity(),
									"Enrútate MIO", "Conectado!");
							break;

						case MessageCodes.COMPUTED_ROUTE:
								drawRoute();
								mapGoogle.showFunctionsMap();
								progress.dismiss();

							break;

						case MessageCodes.DISCONNECTED:
							progress.dismiss();
							mapGoogle.limpiarMapa();
							
								DialogEnrutate
										.createDialog(getActivity(),
												"Enrútate MIO",
												"Hemos perdido la conexión con el servidor");
						
							break;

						case MessageCodes.ERROR:

							if (progress.isShowing()) {
								progress.dismiss();
							}

					
								DialogEnrutate
										.createDialog(getActivity(),
												"Enrútate MIO",
												"Por favor comprueba tu conexión a internet.");
								mapGoogle.limpiarMapa();
							//else
								//flag_change_travel = 0;

							break;

						case MessageCodes.PARSE_ERROR:
							progress.dismiss();
							mapGoogle.limpiarMapa();
							//++flag_change_travel;
							//if (flag_change_travel == 1)
								DialogEnrutate
										.createDialog(getActivity(),
												"Enrútate MIO",
												"No se han encontrado rutas hacia tu lugar de destino");
							//else
								//flag_change_travel = 0;

							break;

						case MessageCodes.SOCKET_ERROR:
							progress.dismiss();
							mapGoogle.limpiarMapa();
							//++flag_change_travel;
							//if (flag_change_travel == 1)
								DialogEnrutate
										.createDialog(getActivity(),
												"Enrútate MIO",
												"Ocurrió un error al trazar el recorrido, vuelve a intentarlo");
							//else
								//flag_change_travel = 0;
							break;

						case MessageCodes.FIND_STATIONS:
							progress.dismiss();
							drawFindStations();
							break;
						case MessageCodes.PROGRESS_ROUTE:

							break;
						}
					}
				};

				service = new Service(messenger, nearbyStops);

			}

		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.mensajes(getActivity(), "Error al cargar el mapa", 1);
		}

		// ver los detalles de una estación
		vermas.setOnClickListener(new ActionListenerSeeMoreStation(this));

		// mostrar listado de pasos del viaje
		container_steps.setOnClickListener(new ActionListenerStopsTravel(this));

		// listener limpiar mapa
		limpiarMapTodo.setOnClickListener(new ActionListenerCleanMap(this));

		// sitios cercanos
		nearbysites.setOnClickListener(new ActionListenerNearbySites(this));

		// estaciones o paradas cercanas
		nearbystations
				.setOnClickListener(new ActionListenerNearbyStations(this));

		// muestra puntos de recarga cercanos
		puntoRecargamio
				.setOnClickListener(new ActionListenerPuntosRecarga(this));

		// cambiar tipo de mapa hibrido/normal
		changeMap.setOnClickListener(new ActionListenerChangeMap(this));

		omitirtutorial.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				omitirTutorial();
			}
		});

//		change_travel.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View view) {
//				change_travel.clearAnimation();
//				indexTravelSelected = (indexTravelSelected == 0) ? 1 : 0;
//				drawRoute();
//			}
//		});

	}



	public void onResume() {
		// for the system's orientation sensor registered listeners

		if (!tutorial) {
			contenedortutorial.setVisibility(View.VISIBLE);
			int status = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(getActivity());
			if (status != ConnectionResult.SUCCESS) {
				int requestCode = 10;
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
						getActivity(), requestCode);
				dialog.show();
			}

		}
		ViewServer.get(getActivity()).setFocusedWindow(getActivity());
		super.onResume();
	}

	public class buscarDireccionPHP extends AsyncTask<String, String, String> {

		String cadena = "";
		boolean estadoRutas = false;

		ArrayList<String> rutas = new ArrayList<String>();

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(cxt);
			pDialog.setMessage("Buscando dirección...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		protected String doInBackground(String... args) {

			cadena = args[0];
			if (rutas.contains(cadena.toUpperCase()))
				estadoRutas = true;
			else {
				resultadoBusqueda.clear();
				resultadoBusqueda = Consultas.buscarDireccion(getActivity(),
						cadena);
				estadoRutas = false;
			}

			return null;
		}

		protected void onPostExecute(String file_url) {

			if (estadoRutas)
				pDialog.dismiss();
			else {

				elementos = new String[resultadoBusqueda.size()];
				elementos = resultadoBusqueda.keySet().toArray(elementos);

				if (resultadoBusqueda.size() > 1) {
					pDialog.dismiss();
					new AlertDialog.Builder(cxt)
							.setIcon(R.drawable.inicio)
							.setTitle(
									resultadoBusqueda.size() + " "
											+ "Resultados de busqueda")
							.setAdapter(new Recorrido2(cxt, elementos),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int item) {

											String escogido = elementos[item];
											LatLng punto = resultadoBusqueda
													.get(escogido);
											mapGoogle.map
													.moveCamera(CameraUpdateFactory
															.newLatLngZoom(
																	punto, 17));
											mapGoogle.map.animateCamera(
													CameraUpdateFactory
															.zoomTo(17), 2000,
													null);

										}
									}).show();

				} else if (resultadoBusqueda.size() == 1) {
					pDialog.dismiss();
					elementos = new String[resultadoBusqueda.size()];
					elementos = resultadoBusqueda.keySet().toArray(elementos);
					LatLng punto = resultadoBusqueda.get(elementos[0]);// buscarDireccion2(direBuscar);
					if (punto != null)
						mapGoogle.map.animateCamera(CameraUpdateFactory
								.newLatLngZoom(punto, 16));
					else
						Toast.makeText(cxt, "Error en la busqueda",
								Toast.LENGTH_LONG).show();

				} else {

					direBuscar = cadena;
					// busqueda con google
					String location = direBuscar + ",Cali,colombia";

					if (location == null || location.equals("")) {
						Toast.makeText(cxt, "Lugar no encontrado",
								Toast.LENGTH_SHORT).show();
						return;
					}

					String url = "https://maps.googleapis.com/maps/api/geocode/json?";

					try {

						location = URLEncoder.encode(location, "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

					String address = "address=" + location;

					String sensor = "sensor=false";
					url = url + address + "&" + sensor;
					DownloadTaskDirecciones downloadTask = new DownloadTaskDirecciones();
					// Start downloading the geocoding places
					downloadTask.execute(url);

				}
			}

		}

	}

	private String downloadUrlDirecciones(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);
			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuilder sb = new StringBuilder();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();
			br.close();

		} catch (Exception e) {

		} finally {
			iStream.close();
			urlConnection.disconnect();
		}

		return data;
	}

	/** A class, to download Places from Geocoding webservice */
	private class DownloadTaskDirecciones extends
			AsyncTask<String, Integer, String> {

		String data = null;

		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try {
				data = downloadUrlDirecciones(url[0]);
			} catch (Exception e) {
				// Log.d("Background Task",e.toString());
			}
			return data;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result) {

			pDialog.dismiss();
			ParserTaskDirecciones parserTask = new ParserTaskDirecciones();
			parserTask.execute(result);
		}
	}

	/** A class to parse the Geocoding Places in non-ui thread */
	class ParserTaskDirecciones extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		// Invoked by execute() method of this object
		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;
			GeocodeJSONParser parser = new GeocodeJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				/** Getting the parsed data as a an ArrayList */
				places = parser.parse(jObject);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return places;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> list) {

			try {
				// Creating a marker
				MarkerOptions markerOptions = new MarkerOptions();

				// Getting a place from the places list
				HashMap<String, String> hmPlace = list.get(0);

				// Getting latitude of the place
				double lat = Double.parseDouble(hmPlace.get("lat") + "785");

				// Getting longitude of the place
				double lng = Double.parseDouble(hmPlace.get("lng") + "5556");

				// Getting name
				String name = hmPlace.get("formatted_address");

				LatLng latLng = new LatLng(lat, lng);

				// Setting the position for the marker
				markerOptions.position(latLng);
				markerOptions.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.banderainicio));
				// Setting the title for the marker
				markerOptions.title(name.replaceAll(
						", Cali, Valle del Cauca, Colombia", ""));
				markerOptions.snippet("Direccion");
				name.replaceAll(", Cali, Valle del Cauca, Colombia", "");

				if (!name.equalsIgnoreCase("Cali, Valle del Cauca, Colombia")) {
					// inicioRecorrido = map.addMarker(markerOptions);
					iniciarViaje(latLng, 0);
					mapGoogle.map.animateCamera(CameraUpdateFactory
							.newLatLngZoom(latLng, 16));

				} else
					Mensajes.mensajes(cxt,
							"No se encontrarón resultados para: " + direBuscar,
							0);

			} catch (Exception e) {
				Mensajes.mensajes(cxt, "Error al realizar la busqueda", 1);
			}

		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub

		ma = marker;

		if (ma.getTitle().equalsIgnoreCase("inicio")
				|| ma.getTitle().equalsIgnoreCase("fin")
				|| ma.getTitle().equalsIgnoreCase("Aquí")) {

		} else if (ma.getTitle().equalsIgnoreCase("Transbordo Aquí")) {

		} else if (ma.getTitle().equalsIgnoreCase("Inicio recorrido")
				|| ma.getTitle().equalsIgnoreCase("Fin recorrido")) {

		} else if (ma.getSnippet().contains("Foursquare"))
			resetFooter();
		else if (ma.getSnippet().equalsIgnoreCase("Direccion")) {

		} else if (ma.getTitle().contains("ESTAC")) {

			String name = ma.getTitle().toString();
			if (ma.getTitle().toString().contains("TERMINAL"))
				name = name.replace("ESTACIÓN ", "");

			if (anysite.getVisibility() == View.VISIBLE) {
				anysite.setVisibility(View.INVISIBLE);
				footermap.setVisibility(View.VISIBLE);
				if (vermas.getVisibility() == View.GONE) {
					textobienvenida.setVisibility(View.GONE);
					vermas.setVisibility(View.VISIBLE);
					vermas.setText("Ver más " + name);

				} else {
					vermas.setText("Ver más " + name);
					textobienvenida.setVisibility(View.GONE);
				}

			} else if (footermap.getVisibility() == View.VISIBLE) {

				vermas.setText("Ver más " + name);
				vermas.setVisibility(View.VISIBLE);
				textobienvenida.setVisibility(View.GONE);

			}
			if (travelSelected.isDrawed()) {

				// preguntar si quiere trazar nuevo recorrido
				new AlertDialog.Builder(getActivity())
						.setIcon(R.drawable.inicio)
						.setTitle("Recorrido")
						.setMessage("¿Deseas iniciar un nuevo recorrido?")
						.setNegativeButton(android.R.string.cancel,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// Salir
										dialog.dismiss();

									}
								})
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										mapGoogle.limpiarMapa();

									}
								}).show();
			}

		}

		return false;
	}

	public class LoadSitesFoursquare extends AsyncTask<String, String, String> {
		ProgressDialog mProgress;
		double latitude, longitude;

		public LoadSitesFoursquare(double latitude, double longitude) {
			// TODO Auto-generated constructor stub
			this.latitude = latitude;
			this.longitude = longitude;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mProgress = new ProgressDialog(getActivity());
			mProgress.setMessage("Consultando Foursquare...");
			mProgress.setIcon(R.drawable.foursquaremapa);
			mProgress.setCancelable(false);
			mProgress.setTitle("Sitios cercanos");
			mProgress.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			mNearbyList = fours.getNearby(latitude, longitude);
			Collections.sort(mNearbyList, new FoursquareVenueDTO());
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgress.dismiss();

			if (mNearbyList.size() == 0)
				Toast.makeText(getActivity(), R.string.no_hay_sitios_cercanos,
						Toast.LENGTH_SHORT).show();
			else {
				new AlertDialog.Builder(getActivity())
						.setIcon(R.drawable.foursquaremapa)
						.setTitle("Sitios cercanos")
						.setCancelable(false)
						.setAdapter(
								new LugaresCercanosListAdapter(getActivity(),
										mNearbyList), new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int po) {
										// TODO Auto-generated method stub
										FoursquareVenueDTO four = mNearbyList
												.get(po);
										String dir = "";
										if (four.address != null
												&& four.address.length() > 0) {
											dir = four.address;
										} else
											dir = "";

										Marker marker = mapGoogle.addMarker(
												new LatLng(four.location
														.getLatitude(),
														four.location
																.getLongitude()),
												four.name + ".", "Foursquare "
														+ dir,
												R.drawable.foursquaremapa);

										marcasFoursquare.add(marker);

									}
								})
						.setNegativeButton("Ubicar sitios",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										for (int i = 0, len = mNearbyList
												.size(); i < len; ++i) {
											FoursquareVenueDTO four = mNearbyList
													.get(i);
											String dir = "";
											if (four.address != null
													&& four.address.length() > 0)
												dir = four.address;
											else
												dir = "";

											Marker marker = mapGoogle.addMarker(
													four.location.getLatitude(),
													four.location
															.getLongitude(),
													four.name,

													"Foursquare " + dir,
													R.drawable.foursquaremapa);

											marcasFoursquare.add(marker);
										}

										mapGoogle.map.animateCamera(
												CameraUpdateFactory
														.newLatLngZoom(
																new LatLng(
																		latitude,
																		longitude),
																15), 1400, null);

									}
								})
						.setPositiveButton(R.string.salir,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// Salir
										dialog.dismiss();
									}
								}).show();
			}
		}
	}

	public class consultarPHPHilo extends AsyncTask<String, String, String> {

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
			paradas.clear();
			paradas = Consultas.consultaAmbosSentidos(getActivity(), args[0]);

			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all empleados
			pDialog.dismiss();
			consultasCodigo();
		}
	}

	public void consultasCodigo() {
		if (buscarCoincidenciaParadas(paradas, ma.getTitle()) >= 2) {
			// Tiene doble sentido
			String ruta = rutas.split(",")[it].trim();
			// sentido = sentidodelarutaPHP(ruta, sentido);
			sentido = Consultas.sentidodelaruta(getActivity(), ruta, sentido);

			new AlertDialog.Builder(getActivity())
					.setIcon(R.drawable.inicio)
					.setTitle(
							"Ruta " + rutas.split(",")[it].trim() + " "
									+ "Selecciona el sentido")
					.setAdapter(new Recorrido2(getActivity(), sentido),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {

									if (item == 0) {
										// sur - norte
										paradas.clear();

										new LoadAllStations(0).execute(
												rutas.split(",")[it].trim(),
												String.valueOf(0));

									} else if (item == 1) {
										// norte - sur
										paradas.clear();

										new LoadAllStations(1).execute(
												rutas.split(",")[it].trim(),
												String.valueOf(1));

									} else if (item == 2)
										dialog.cancel();

								}
							}).show();

		} else {
			// tiene un solo sentido esta ruta
			try {

				String ruta = rutas.split(",")[it].trim();
				Log.d("resultadosentidoruta", "ruta: " + ruta);
				cadena = "";
				cadena = Consultas.consultaUnSentido(getActivity(), ruta);
				if (cadena.length() > 0)
					unSentido(ruta);
				else
					Toast.makeText(getActivity(),
							"la ruta " + ruta + " no se ha añadido al sistema",
							Toast.LENGTH_LONG).show();

			} catch (Exception e) {
				Toast.makeText(getActivity(),
						"No se encontrarón coincidencias", Toast.LENGTH_LONG)
						.show();
			}

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

	public void unSentido(String ruta) {

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
			boolean estadoUno = buscarCoincidencia(p1, ma.getTitle());
			boolean estadoDos = false;

			if (estadoUno == true && estadoDos == false) {

				int dirSentido = 0;
				if (sentido1.equalsIgnoreCase("surnorte"))
					dirSentido = 0;
				else if (sentido1.equalsIgnoreCase("nortesur"))
					dirSentido = 1;

				Intent infoRuta = new Intent(getActivity(),
						ParadasActivity.class);
				infoRuta.putExtra("Ruta", ma.getSnippet().split(",")[it]);
				infoRuta.putExtra("sentido", dirSentido);
				infoRuta.putExtra("titulo", ma.getTitle());
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
				boolean estadoUno = buscarCoincidencia(p1, ma.getTitle());
				boolean estadoDos = buscarCoincidencia(p2, ma.getTitle());

				if (estadoUno == true && estadoDos == false) {

					int dirSentido = 0;
					if (sentido1.equalsIgnoreCase("surnorte"))
						dirSentido = 0;
					else if (sentido1.equalsIgnoreCase("nortesur"))
						dirSentido = 1;

					Intent infoRuta = new Intent(getActivity(),
							ParadasActivity.class);
					infoRuta.putExtra("Ruta", ruta);
					infoRuta.putExtra("sentido", dirSentido);
					infoRuta.putExtra("titulo", ma.getTitle());
					infoRuta.putExtra("direccion", s1);
					getActivity().startActivity(infoRuta);
				} else if (estadoUno == false && estadoDos == true) {

					int dirSentido2 = 0;
					if (sentido2.equalsIgnoreCase("surnorte"))
						dirSentido2 = 0;
					else if (sentido2.equalsIgnoreCase("nortesur"))
						dirSentido2 = 1;

					Intent infoRuta = new Intent(getActivity(),
							ParadasActivity.class);
					infoRuta.putExtra("Ruta", ruta);
					infoRuta.putExtra("sentido", 0);
					infoRuta.putExtra("titulo", ma.getTitle());
					infoRuta.putExtra("direccion", s2);
					getActivity().startActivity(infoRuta);
				}
			} catch (Exception e) {
				Toast.makeText(getActivity(), "Error al cargar los sentidos",
						Toast.LENGTH_LONG).show();
			}
		}
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

	class LoadAllStations extends AsyncTask<String, String, String> {

		int opcion = 0;

		public LoadAllStations(int opcion) {
			this.opcion = opcion;
		}

		protected String doInBackground(String... args) {

			paradas = Consultas.consultaSentidos(getActivity(),
					rutas.split(",")[it].trim(), opcion);
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all empleados
			consultaParadasRuta(opcion);

		}

	}

	public void consultaParadasRuta(int opcion) {
		if (buscarCoincidencia(paradas, ma.getTitle())) {
			Intent infoRuta = new Intent(getActivity(), ParadasActivity.class);
			infoRuta.putExtra("Ruta", rutas.split(",")[it]);
			infoRuta.putExtra("sentido", opcion);
			infoRuta.putExtra("titulo", ma.getTitle());
			infoRuta.putExtra("direccion", sentido[opcion].toString());
			getActivity().startActivity(infoRuta);

		} else {
			new AlertDialog.Builder(getActivity())
					.setIcon(R.drawable.inicio)
					.setTitle("Información")
					.setMessage(
							"La ruta "
									+ ma.getSnippet().split(",")[it]
											.toUpperCase()
									+ " no para en la estación "
									+ ma.getTitle() + " en este sentido")
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.cancel();

								}
							}).show();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			LatLng userLocation = new LatLng(lat, lon);
			mapGoogle.map.moveCamera(CameraUpdateFactory
					.newLatLng(userLocation));
			mapGoogle.map.animateCamera(CameraUpdateFactory.zoomTo(14));
		}
	
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void resetFooter() {
		anysite.setVisibility(View.INVISIBLE);
		footermap.setVisibility(View.VISIBLE);
		vermas.setVisibility(View.GONE);
		textobienvenida.setVisibility(View.VISIBLE);
	}

	public void clearStations() {
		if (marcasenmapa != null && marcasenmapa.size() > 0) {
			for (int i = 0, len = marcasenmapa.size(); i < len; ++i) {
				Marker tmp = marcasenmapa.get(i);
				tmp.remove();
			}
			marcasenmapa.clear();
		}
	}

	public String getEstation(String tmpe) {
		if (tmpe.contains("]"))
			tmpe = tmpe.replace("]", "");

		if (tmpe.contains("["))
			tmpe = tmpe.replace("[", "");

		return tmpe;
	}

	public void loadStations() {
		try {
			String estado = "Iniciar viaje";

			AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(cxt);
			SQLiteDatabase bd = admin.getWritableDatabase();
			String nombre = "";
			String latitud = "";
			String longitud = "";

			Cursor fila = bd
					.rawQuery(
							"select nombre, latitud, longitud from estaciones where tipo ='0'",
							null);

			while (fila.moveToNext()) {
				nombre = fila.getString(0);
				latitud = fila.getString(1);
				longitud = fila.getString(2);

				double la = Double.parseDouble(latitud) / 1E6;
				double lo = Double.parseDouble(longitud) / 1E6;
				marca = mapGoogle.addMarker(la, lo, "ESTACIÓN " + nombre,
						estado, R.drawable.iconoestacionmapa);

				// fin animacion por marca
				marcasenmapa.add(marca);

			}
			marca.hideInfoWindow();
			fila.close();
			bd.close();
			admin.close();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void cargarEstaciones(int op) {

		if (op == 1) {
			ma = null;
			anysite.setVisibility(View.INVISIBLE);
			footermap.setVisibility(View.VISIBLE);
			vermas.setVisibility(View.GONE);
			textobienvenida.setVisibility(View.VISIBLE);
		}
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(cxt);
		boolean isDark = pref.getBoolean("Name", false);
		if (isDark) {

			try {
				AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(cxt);
				SQLiteDatabase bd = admin.getWritableDatabase();
				String nombre = "";
				String latitud = "";
				String longitud = "";

				Cursor fila = bd
						.rawQuery(
								"select nombre, latitud, longitud from estaciones where tipo ='0'",
								null);

				while (fila.moveToNext()) {
					nombre = fila.getString(0);
					latitud = fila.getString(1);
					longitud = fila.getString(2);

					double la = Double.parseDouble(latitud) / 1E6;
					double lo = Double.parseDouble(longitud) / 1E6;

					marca = mapGoogle.addMarker(la, lo, "ESTACIÓN " + nombre,
							"Iniciar viaje", R.drawable.iconoestacionmapa);
					mapGoogle.dropPinEffect(marca);
					// Thread.sleep(16);
					marcasenmapa.add(marca);

				}
				marca.hideInfoWindow();
				fila.close();
				bd.close();
				admin.close();

			} catch (Exception e) {

				e.printStackTrace();

			}
		} else {
			if (travelSelected.isDrawed())
				clearStations();
			else {
				if (imHere != null)
					clearStations();
				else
					mapGoogle.map.clear();
			}

		}

	}

	public void drawRoute() {

		listaRutas.clear();
		mapGoogle.map.clear();

		travelSelected = listTravels.get(indexTravelSelected);
		ListadoRutasGoogle recorrido = null;
		ArrayList<Section> sections = travelSelected.sections;
		LatLng lastPosition = null;
		Marker lastMarker = null;
		for (int i = 0, len = sections.size(); i < len; ++i) {
			Section s = sections.get(i);
			PolylineOptions sectionPath = new PolylineOptions().width(8)
					.geodesic(true);
			boolean walk = false, bus = true;

			for (int j = 0, lenj = s.points.size(); j < lenj; ++j) {

				Point p = s.points.get(j);
				if (i == 0)
				{
					mapGoogle.addMarker(s.points.get(0).position, "Inicio ",
							p.name, R.drawable.banderainicio);
					//listaRutas.add(new ListadoRutasGoogle("Caminar", s.points.get(0).position, 0));
				}

				// Determinar el tipo de secciÓn
				if (s.type.equals("GIS_ROUTE") || s.type.equals("WALK")) {
					if (!walk) {

						walk = true;
						recorrido = new ListadoRutasGoogle("Caminar",
								p.position, 0);
						listaRutas.add(recorrido);
					}

//					
					sectionPath.add(p.position);
					sectionPath.color(Color.rgb(120, 171, 89));
				}

				else if (s.type.equals("JOURNEY")) {
					sectionPath.add(p.position);
					sectionPath.color(Color.rgb(111, 142, 191));

					if (bus) {
						if (p.position == lastPosition) {

							lastMarker.remove();
							lastPosition = null;
							mapGoogle.addMarker(p.position,
									"Tomar el " + s.bus + " a las " + p.startTime,
									p.name, R.drawable.ic_launcher);
							bus = false;
							recorrido = new ListadoRutasGoogle("Tomar el " + s.bus
									+ " a las " + p.startTime + " en " + p.name,
									p.position, 1);
						} else {
							lastMarker = mapGoogle.addMarker(p.position,
									"Tomar el " + s.bus + " a las " + p.startTime,
									p.name, R.drawable.bus);

							bus = false;
							recorrido = new ListadoRutasGoogle("Tomar el " + s.bus
									+ " a las " + p.startTime + " en " + p.name,
									p.position, 1);
							listaRutas.add(recorrido);
							lastPosition = p.position;
						}
						
					}

					else {
						// Adicionar marcas segun el tipo de punto
						if (p.type.equals("ADDRESS")) {
							mapGoogle.addMarker(p.position, "Parada", p.name,
									R.drawable.stop);
							recorrido = new ListadoRutasGoogle("Parada "
									+ p.name, p.position, 0);
							listaRutas.add(recorrido);
						}

						else if (p.type.equals("STATION")) {
							mapGoogle.addMarker(p.position, "Estación" + "-",
									p.name, R.drawable.iconoestacionmapa);

							recorrido = new ListadoRutasGoogle("Estación "
									+ p.name, p.position, 0);
							listaRutas.add(recorrido);
						}
					}
				}
			}

			mapGoogle.map.addPolyline(sectionPath);
			travelSelected.listaRutas = listaRutas;
			travelSelected.drawed = true;
			
		}
		
		ListadoRutasGoogle b = listaRutas.get(listaRutas.size() - 1);
		mapGoogle.addMarker(b.geolocalizacion, "fin del recorrido",
				b.direccion, R.drawable.banderafin);
		if (sections.size() > 0) {
			mapGoogle.map.animateCamera(
					CameraUpdateFactory.newLatLngZoom(
							sections.get(0).points.get(0).position, 13), 2000,
					null);
			mode = "start";
			animate(container_steps);
			textotutorial
					.setText("!Perfecto!, Enrútate ha encontrado la mejor ruta para tu viaje. \n para conocer mas detalles del recorrido, toca el botón");

			imagentutorial.setVisibility(View.VISIBLE);
			imagentutorial.setImageResource(R.drawable.mapa_tuto);
		}

	}

	@Override
	public void onMapClick(final LatLng point) {
		// mapGoogle.map.animateCamera(CameraUpdateFactory.newLatLng(point));

		if (!travelSelected.isDrawed()) {
			showOnlyCleanButton();
			resetFooterOptionPerson();
		}

		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		if (travelSelected.isDrawed()) {
			
			//inicio dialogo
			Typeface robot_regular = Typeface.createFromAsset(getActivity().getAssets(), "RobotoCondensed_Regular.ttf");
			final Dialog dialog = new Dialog(getActivity());
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.setContentView(R.layout.modal_material_design);
			dialog.setCancelable(false);
		
			// set the custom dialog components - text, image and button
			TextView title_modal = (TextView) dialog.findViewById(R.id.title_modal);
			TextView text_modal = (TextView) dialog.findViewById(R.id.text_modal);
			Button dialogButton = (Button) dialog.findViewById(R.id.accept_modal);
			Button dialogButtonCancel = (Button) dialog.findViewById(R.id.cancel_modal);
			title_modal.setText("Recorrido");
			text_modal.setText("¿Deseas iniciar un nuevo recorrido?");
			title_modal.setTypeface(robot_regular, Typeface.BOLD);
			text_modal.setTypeface(robot_regular);
			Visibility.visible(dialogButtonCancel);

			
			dialogButton.setTypeface(robot_regular);
			dialogButtonCancel.setTypeface(robot_regular);
			// if button is clicked, close the custom dialog
		
			dialogButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mapGoogle.hideFunctionsMap();
					
					SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(cxt);
					boolean isDark = pref.getBoolean("Name",
							false);
					if (isDark) {

						mapGoogle.limpiarMapa();

						mapGoogle.map.clear();
						showOnlyCleanButton();
						resetFooterOptionPerson();
						mode = "start";
						iniciarViaje(point);

					} else {
						mapGoogle.map.clear();
						mapGoogle.limpiarMapa();
						showOnlyCleanButton();
						// loadStations();
						resetFooterOptionPerson();
						mode = "start";
						iniciarViaje(point, 0);

					}
				
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
			// preguntar si quiere trazar nuevo recorrido
		
		} else
			iniciarViaje(point);

	}

	public void resetFooterOptionPerson() {
		if (footermap.getVisibility() == View.VISIBLE) {
			footermap.setVisibility(View.INVISIBLE);
			vermas.setVisibility(View.GONE);
			textobienvenida.setVisibility(View.VISIBLE);
			anysite.setVisibility(View.VISIBLE);

		}
	}

	public void showOnlyCleanButton() {
		Visibility.visible(viewLimpiar);
		Visibility.visible(limpiarMapTodo);
		Visibility.invisible(viewMapa);
		Visibility.gone(container_steps);
	}

	public void iniciarViaje(LatLng point) {

		if (travelSelected.isDrawed()) {
			mapGoogle.map.clear();
			travelSelected.clearSections();
			travelSelected.drawed = false;

		}

		if (mode.equals("start")) {
			if (imHere != null)
				imHere.remove();

			textotutorial
					.setText("Ahora toca el cuadro sobre el personaje para \n confirmar tu punto de partida");
			imagentutorial.setVisibility(View.VISIBLE);
			imagentutorial.setImageResource(R.drawable.iniciar_aqui);
			imHere = mapGoogle.addMarker(point, "Viaje", "Iniciar aquí",
					R.drawable.walk);

			imHere.showInfoWindow();
			for (Travel travel : listTravels)
				travel.start = point;
		}

		else {
			if (imGoingTo != null)
				imGoingTo.remove();

			textotutorial
					.setText("Vamos bien, ahora toca el cuadro para \n confirmar tu destino");
			imagentutorial.setVisibility(View.VISIBLE);
			imagentutorial.setImageResource(R.drawable.final_aqui);
			imGoingTo = mapGoogle.addMarker(point, "Viaje", "Finalizar aquí",
					R.drawable.walk);

			imGoingTo.showInfoWindow();
			for (Travel travel : listTravels)
				travel.end = point;

		}
	}

	public void iniciarViaje(LatLng point, int op) {

		if (travelSelected.isDrawed()) {

			travelSelected.clearSections();
			travelSelected.drawed = false;

		}
		if (mode.equals("start")) {
			if (imHere != null)
				imHere.remove();

			imHere = mapGoogle.addMarker(point, "Viaje", "Iniciar aquí",
					R.drawable.walk);

			imHere.showInfoWindow();

			for (Travel travel : listTravels)
				travel.start = point;

		}

		else {
			if (imGoingTo != null)
				imGoingTo.remove();

			imGoingTo = mapGoogle.addMarker(point, "Viaje", "Finalizar aquí",
					R.drawable.walk);

			imGoingTo.showInfoWindow();

			for (Travel travel : listTravels)
				travel.end = point;

		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {

		/*
		 * salir en caso click sobre punto de recarga *
		 */
		if (marker != null && marker.getTitle().contains(".")) {
			return;
		}

		if (marker != null && marker.getTitle().contains("-")) {

			ma = marker;

			String nombre = marker.getSnippet();

			String tmp[] = nombre.split(" ");

			if (tmp != null) {
				if (tmp.length == 2) {
					if (tmp[1].length() == 2)
						tmp2 = tmp[0];

				} else if (tmp.length == 3) {
					if (tmp[2].length() == 2)
						tmp2 = tmp[0] + " " + tmp[1];

				} else if (tmp.length == 4) {
					if (tmp[3].length() == 2)
						tmp2 = tmp[0] + " " + tmp[1] + " " + tmp[2];

				}

			}
			ma.setTitle(tmp2);
			String dir = Consultas.consultaListadoParadas(getActivity(), tmp2);

			String[] tmpS = dir.split(";");
			if (tmpS != null) {
				try {
					String dirParada = tmpS[0];
					rutas = tmpS[1];

					AlertDialog.Builder dialog = new AlertDialog.Builder(
							getActivity());
					dialog.setIcon(R.drawable.iconoestacionmapa);

					if (nombre.contains("ESTACIÓN "))
						nombre = nombre.replace("ESTACIÓN ", "");

					if (dirParada == null)
						dirParada = "";
					else
						dirParada = "\n" + "Dir: " + dirParada;

					dialog.setTitle(nombre + dirParada);
					dialog.setAdapter(
							new ListaRutas(getActivity(), rutas.split(",")),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {

									it = item;
									paradasunsentido.clear();

									new consultarPHPHilo().execute(rutas
											.split(",")[it].trim());
								}
							});

					dialog.setPositiveButton(R.string.salir,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									dialog.cancel();
								}
							});

					dialog.setNeutralButton("Sitios cercanos",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									if (ConnectionDetector
											.isConnectingToInternet(getActivity()))
										new LoadSitesFoursquare(ma
												.getPosition().latitude, ma
												.getPosition().longitude)
												.execute();
									else
										Mensajes.mensajes(
												getActivity(),
												"Debes tener acceso a internet",
												1);
								}
							});

					dialog.show();
				} catch (Exception e) {
					Mensajes.mensajes(getActivity(),
							"Ocurrió un error al cargar información", 1);
				}
			}

		}

		travelSelected = listTravels.get(indexTravelSelected);

		if (marker.getTitle().contains("%")) {

			if (travelSelected.isDrawed()) {

				travelSelected.clearSections();
				travelSelected.drawed = false;

			}
			if (mode.equals("start")) {

				if (travelSelected != null) {
					travelSelected.clearSections();
					travelSelected.drawed = false;
				}

				for (Travel travel : listTravels)
					travel.start = marker.getPosition();

				mode = "end";
				vibracion();
				for (int i = 0, len = marcasParadasCercanas.size(); i < len; ++i) {
					marker = marcasParadasCercanas.get(i);
					marker.setSnippet("Finalizar viaje");
				}
				for (int i = 0, len = marcasenmapa.size(); i < len; ++i) {
					marker = marcasenmapa.get(i);
					marker.setSnippet("Finalizar viaje");
				}
				Mensajes.mensajes(getActivity(), "Ahora escoge tu destino", 1);
			}

			else {

				marker.setSnippet("Finalizar viaje");
				for (Travel travel : listTravels)
					travel.end = marker.getPosition();

			}
			if (travelSelected.start != null && travelSelected.end != null) {
				vibracion();

				try {
					if (ConnectionDetector
							.isConnectingToInternet(getActivity())) {
						progress.show();
						progress.setContentView(R.layout.custom_progress);
						TextView textprogress = (TextView) progress
								.findViewById(R.id.textprogress);
						textprogress.setText("Trazando ruta...");
						// se lanza el servicio para viaje
						launchServiceTravel();
					} else
						Mensajes.mensajes(
								getActivity(),
								"Debes tener conexión a internet para planear un viaje",
								1);

					for (int i = 0, len = marcasenmapa.size(); i < len; ++i) {
						marker = marcasenmapa.get(i);
						marker.setSnippet("Iniciar viaje");
					}
					for (int i = 0, len = marcasParadasCercanas.size(); i < len; ++i) {
						marker = marcasParadasCercanas.get(i);
						marker.setSnippet("Iniciar viaje");
					}

				}

				catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		if (!marker.getSnippet().contains("Foursquare")) {
			if (marker.getTitle().contains("ESTACIÓN")) {

				if (travelSelected.isDrawed()) {

					travelSelected.clearSections();
					travelSelected.drawed = false;

				}
				if (mode.equals("start")) {

					if (travelSelected != null) {
						travelSelected.clearSections();
						travelSelected.drawed = false;
					}
					for (Travel travel : listTravels)
						travel.start = marker.getPosition();

					mode = "end";
					vibracion();
					for (int i = 0, len = marcasenmapa.size(); i < len; ++i) {
						marker = marcasenmapa.get(i);
						marker.setSnippet("Finalizar viaje");
					}
					Mensajes.mensajes(getActivity(), "Ahora escoge tu destino",
							1);
				}

				else {

					marker.setSnippet("Finalizar viaje");
					for (Travel travel : listTravels)
						travel.end = marker.getPosition();

				}
				if (travelSelected.start != null && travelSelected.end != null) {
					vibracion();

					try {
						if (ConnectionDetector
								.isConnectingToInternet(getActivity())) {
							progress.show();
							progress.setContentView(R.layout.custom_progress);
							TextView textprogress = (TextView) progress
									.findViewById(R.id.textprogress);
							textprogress.setText("Trazando ruta...");
							// se lanza el servicio para viaje
							launchServiceTravel();
						} else
							Mensajes.mensajes(
									getActivity(),
									"Debes tener conexión a internet para planear un viaje",
									1);

						// s.connectByHTTPSecond();
						for (int i = 0, len = marcasenmapa.size(); i < len; ++i) {
							marker = marcasenmapa.get(i);
							marker.setSnippet("Iniciar viaje");
						}

					}

					catch (Exception e) {
						e.printStackTrace();
					}

				}
			}

			else {

				if (!travelSelected.isDrawed()) {
					if (mode.equals("start")) {
						mode = "end";

						vibracion();

						Mensajes.mensajes(getActivity(),
								"Ahora escoge tu destino", 1);
						textotutorial
								.setText("Muy bien! \n ahora escoge otro punto en el mapa para tu destino.");
						imagentutorial.setVisibility(View.GONE);
						for (int i = 0, len = marcasenmapa.size(); i < len; ++i) {
							marker = marcasenmapa.get(i);
							marker.setSnippet("Finalizar viaje");
						}
					}

					else {

						if (travelSelected.start != null
								&& travelSelected.end != null) {
							vibracion();

							try {

								if (ConnectionDetector
										.isConnectingToInternet(getActivity())) {
									progress.show();
									progress.setContentView(R.layout.custom_progress);
									TextView textprogress = (TextView) progress
											.findViewById(R.id.textprogress);
									textprogress.setText("Trazando ruta...");

									// se lanza el servicio para viaje
									launchServiceTravel();

								} else
									Mensajes.mensajes(
											getActivity(),
											"Debes tener conexión a internet para planear un viaje",
											1);

							}

							catch (Exception e) {
								e.printStackTrace();
							}

						}

					}
				}

			}
		}
	}

	public void launchServiceTravel() {
		listTravels.get(0).sections = service
				.connectByHTTP("http://tuyo.herokuapp.com/request-route?"
						+ travelSelected.getLikeGet() + "&mode=" + LESS_BUS);

//		listTravels.get(1).sections = service
//				.connectByHTTP("http://tuyo.herokuapp.com/request-route?"
//						+ travelSelected.getLikeGet() + "&mode=" + LESS_BUS);
	}

	public void showPosition() {

		if (mapGoogle != null) {
			if (mapGoogle.map != null) {
				Location l = mapGoogle.map.getMyLocation();

				if (l != null) {
					LatLng mLocation = new LatLng(l.getLatitude(),
							l.getLongitude());
					if (mLocation != null)
						mapGoogle.map.animateCamera(CameraUpdateFactory
								.newLatLngZoom(mLocation, 15));
				}
				textotutorial
						.setText("Bien, ahora toca sobre el mapa para marcar el punto de partida de tu viaje.");
				imagentutorial.setVisibility(View.GONE);
			}
		}

	}

	public void buscarSite(String text) {
		new buscarDireccionPHP().execute(text);
	}

	public void vibracion() {
		boolean result = sharedPreferences.getBoolean("preferenciavibracion",
				false);

		if (result)
			vibe.vibrate(50);

	}

	public View init(View view) {
		try {
			cxt = getActivity();
			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getActivity());

			Typeface type = Typeface.createFromAsset(getActivity().getAssets(),
					"gothic.ttf");
			Typeface typeBien = Typeface.createFromAsset(getActivity()
					.getAssets(), "Lobster_1.3.otf");
			footermap = (LinearLayout) view.findViewById(R.id.footermap);
			anysite = (LinearLayout) view.findViewById(R.id.anysite);
			vermas = (Button) view.findViewById(R.id.vermas);
			nearbysites = (ImageView) view.findViewById(R.id.nearbysites);
			nearbystations = (Button) view.findViewById(R.id.nearbystations);
			textobienvenida = (TextView) view
					.findViewById(R.id.textobienvenida);
			
			limpiarMapTodo = (ImageView) view.findViewById(R.id.limpiaMapaTodo);
			textotutorial = (TextView) view.findViewById(R.id.textotutorial);
			contenedortutorial = (FrameLayout) view
					.findViewById(R.id.contenedortutorial);
			omitirtutorial = (TextView) view.findViewById(R.id.omitirtutorial);
			imagentutorial = (ImageView) view.findViewById(R.id.imagentutorial);
			changeMap = (ImageView) view.findViewById(R.id.changeMap);
			puntoRecargamio = (Button) view.findViewById(R.id.puntoRecargamio);
		//	change_travel = (ImageView) view.findViewById(R.id.change_travel);
			viewMapa = (View) view.findViewById(R.id.view2);
			viewLimpiar = (View) view.findViewById(R.id.view1);
			container_steps = (LinearLayout) view.findViewById(R.id.container_steps);
			
			fours = new FoursquareBO();
			// Initializing

			vibe = (Vibrator) getActivity().getSystemService(
					Context.VIBRATOR_SERVICE);
			textotutorial.setTypeface(type);
			textobienvenida.setTypeface(typeBien);
			

			return view;
		} catch (Exception e) {
			DialogEnrutate.createDialog(getActivity(), "Enrútate MIO",
					"Ocurrió un error al cargar los componentes");
		}
		return view;
	}

	public void animate(View v) {

		boolean tutorial = sharedPreferences.getBoolean("tutorial", false);
		if (!tutorial) {
			final Animation animationFadeOut = AnimationUtils.loadAnimation(
					getActivity(), R.anim.animacionprueba);
			v.startAnimation(animationFadeOut);
		}

	}

	private void drawFindStations() {
		limpiarMapaNearby();
		int draw = 0;
		Marker tmpMarker;
		ArrayList<NearbyPoint> ns = nearbyStops.getPoints();

		if (ns != null && ns.size() > 0) {
			for (int i = 0, len = ns.size(); i < len; ++i) {
				NearbyPoint tmp = ns.get(i);
				if (tmp.name.toUpperCase().contains("ESTAC")
						|| tmp.name.toUpperCase().contains("TERMIN"))
					draw = R.drawable.iconoestacionmapa;
				else
					draw = R.drawable.station_small;

				tmpMarker = mapGoogle.addMarker(tmp.point, tmp.name
						+ ", esta a " + tmp.distance + " m" + " % ",
						"Iniciar viaje", draw);
				marcasParadasCercanas.add(tmpMarker);
			}
			mapGoogle.map.animateCamera(
					CameraUpdateFactory.newLatLngZoom(ns.get(0).point, 14),
					1400, null);

		}

	}

	public void limpiarMapaNearby() {

		// mode = "start";
		if (marcasParadasCercanas != null && marcasParadasCercanas.size() > 0) {
			for (int i = 0, len = marcasParadasCercanas.size(); i < len; ++i) {
				Marker tmp = marcasParadasCercanas.get(i);
				tmp.remove();
			}
			marcasParadasCercanas.clear();
		}

		if (travelSelected.listaRutas != null)
			travelSelected.listaRutas.clear();

		if (listaRutas != null)
			listaRutas.clear();

		if (inicioRecorrido != null)
			inicioRecorrido.remove();

		if (lineas1 != null)
			lineas1.remove();

		if (finRecorrido != null)
			finRecorrido.remove();

		if (lineas2 != null)
			lineas2.remove();

		if (lineas != null)
			lineas.remove();

		if (marcaGoogle != null)
			marcaGoogle.remove();

		if (travelSelected != null) {
			travelSelected.clearSections();
			travelSelected.drawed = false;
		}

	}

	public void omitirTutorial() {
		if (contenedortutorial.getVisibility() == View.VISIBLE)
			contenedortutorial.setVisibility(View.GONE);

		Editor editor = sharedPreferences.edit();
		editor.putBoolean("ultimoSlide", true);
		editor.putBoolean("tutorial", true);
		editor.commit();
	}

	public String loadJSONFromAsset() {
		String json = null;
		try {

			InputStream is = getActivity().getAssets().open(
					"puntos_recarga.json");

			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;

	}

	public class loadPoints extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			JSONArray obj;
			puntoRecargas.clear();
			try {

				obj = new JSONArray(loadJSONFromAsset());

				for (int i = 0, len = obj.length(); i < len; ++i) {

					JSONObject tmp = obj.getJSONObject(i);
					String numero = tmp.getString("NUMERO");
					String nombre = tmp.getString("NOMBRE");
					String direccion = tmp.getString("DIRECCION");
					String latitud = tmp.getString("LATITUD");
					String longitud = tmp.getString("LONGITUD");
					String tipo = tmp.getString("TIPO");
					Double lat = Double.parseDouble(latitud);
					Double lng = Double.parseDouble(longitud);
					puntoRecargas.add(new PuntoRecarga(numero, nombre,
							direccion, lat, lng, tipo));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	public ArrayList<PuntoRecarga> loadNearbyPoints(double lat, double lon,
			int dis) {
		ArrayList<PuntoRecarga> tmp = new ArrayList<PuntoRecarga>();

		if (puntoRecargas != null) {
			for (int i = 0, len = puntoRecargas.size(); i < len; ++i) {
				PuntoRecarga pr = puntoRecargas.get(i);
				double distance = mapGoogle.distFrom(lat, lon, pr.lat, pr.lon);

				if (distance <= dis)
					tmp.add(pr);

			}

		}
		return tmp;
	}

}
