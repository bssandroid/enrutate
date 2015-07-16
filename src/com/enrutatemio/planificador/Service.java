package com.enrutatemio.planificador;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.enrutatemio.mapa.Point;
import com.enrutatemio.mapa.Section;
import com.google.android.gms.maps.model.LatLng;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpResponse;

public class Service {

	private Handler messenger;
	private NearbyStops nStops;
	private NearbyPoint nearbyPoint;

	public Service(Handler messenger, NearbyStops nStops) {
		this.messenger = messenger;
		this.nStops = nStops;

	}

	
	public ArrayList<Section> connectByHTTP(String url) {
		//almacena las secciones de l mapa a retornar
		final ArrayList<Section> sectionsMap = new  ArrayList<Section>();
		
		
		try {
			AsyncHttpGet request = new AsyncHttpGet((new URI(url)));
			request.setTimeout(10000);
			AsyncHttpClient.getDefaultInstance().executeJSONObject(request,
					new AsyncHttpClient.JSONObjectCallback() {

						@Override
						public void onCompleted(Exception ex,
								AsyncHttpResponse arg1, JSONObject result) {
							
							if (ex != null) {
								Message msg = new Message();
								msg.arg1 = MessageCodes.ERROR;
								msg.obj = (ex.getMessage() != null) ? ex
										.getMessage()
										: "Hay un problema con tu conexión a internet :(";
								messenger.sendMessage(msg);
								return;
							}

							try {
								
								JSONObject data = result;
								JSONObject route = new JSONObject(data
										.getString("route"));

								JSONArray sections = route
										.getJSONArray("sections");

								for (int i = 0, len = sections.length(); i < len; ++i) {

									JSONObject section = sections
											.getJSONObject(i);
									JSONArray locations = section
											.getJSONArray("locations");

									Section s = new Section();
									s.type = section.getString("type");

									if(s.type.equals("GIS_ROUTE"))
										s.twalk = section.getString("timeToWalk");
									// Si es el recorrido de un bus
									if (section.has("name"))
										s.bus = section.getString("name");

									

									for (int j = 0, lenj = locations.length(); j < lenj; ++j) {
										JSONObject location = locations
												.getJSONObject(j);
										String _latStr = location
												.getString("y");
										String _lngStr = location
												.getString("x");

										String latStr = new StringBuilder(
												_latStr).insert(1, ".")
												.toString();
										String lngStr = new StringBuilder(
												_lngStr).insert(3, ".")
												.toString();

										Double lat = Double.parseDouble(latStr);
										Double lng = Double.parseDouble(lngStr);

										Point p = new Point();
										p.position = new LatLng(lat, lng);
										p.name = location.getString("name");
										p.startTime = location.getString("dep");

										if (p.name.indexOf("Cl ") != -1
												|| p.name.indexOf("Kr ") != -1
												|| p.name.indexOf("Av ") != -1
												|| p.name.indexOf("Tr ") != -1
												|| p.name.indexOf("Calle ") != -1
												|| p.name.indexOf("Puente ") != -1
												|| p.name
														.indexOf("Cementerio ") != -1
												|| p.name.indexOf("Aut ") != -1
												|| p.name.indexOf("Autopista ") != -1
												|| p.name.indexOf("K180 ") != -1) {
											p.type = "ADDRESS";
										}

										else
											p.type = "STATION";
										

										s.addPoint(p);

									}

									sectionsMap.add(s);
								}
								
								Message msg = new Message();
								msg.arg1 = MessageCodes.COMPUTED_ROUTE;
								messenger.sendMessage(msg);
								
								
							} catch (JSONException e) {
								Log.d("jsonserver", "ocurrio un error "+e.getMessage() );
								e.printStackTrace();
								Message msg = new Message();
								msg.arg1 = MessageCodes.PARSE_ERROR;
								msg.obj = e.getMessage();
								messenger.sendMessage(msg);
								return;
							}
						}
					});
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message msg = new Message();
			msg.arg1 = MessageCodes.SOCKET_ERROR;
			msg.obj = e.getMessage();
			messenger.sendMessage(msg);
			
			
		}
		return sectionsMap;
	}

	

	public void findNearbyStations(double lat, double lon) {

		AsyncHttpGet request;
		try {
			request = new AsyncHttpGet((new URI(
					"http://tuyo.herokuapp.com/nearby-stations?x1=" + lon
							+ "&y1=" + lat + "&max=5")));

			request.setTimeout(6000);
		
			AsyncHttpClient.getDefaultInstance().executeJSONObject(request,
					new AsyncHttpClient.JSONObjectCallback() {
						@Override
						public void onCompleted(Exception ex,
								AsyncHttpResponse arg1, JSONObject result) {
							if (ex != null) {
							
								Message msg = new Message();
								msg.arg1 = MessageCodes.ERROR;
								msg.obj = (ex.getMessage() != null) ? ex
										.getMessage()
										: "Hay un problema con tu conexión a internet :(";
								messenger.sendMessage(msg);

								return;
							}

							try {
								JSONObject data = result;

								JSONArray stops = data.getJSONArray("stops");

								nStops.getPoints().clear();
								for (int i = 0, len = stops.length(); i < len; ++i) {

									JSONObject st = stops.getJSONObject(i);

									String _latStr = st.getString("y");
									String _lngStr = st.getString("x");

									String latStr = new StringBuilder(_latStr)
											.insert(1, ".").toString();
									String lngStr = new StringBuilder(_lngStr)
											.insert(3, ".").toString();

									Double lat = Double.parseDouble(latStr);
									Double lng = Double.parseDouble(lngStr);
									nearbyPoint = new NearbyPoint();
									nearbyPoint.distance = st.getString("dist");
									nearbyPoint.name = st.getString("name");
									nearbyPoint.point = new LatLng(lat, lng);

									nStops.addPoint(nearbyPoint);

								}

								Message msg = new Message();
								msg.arg1 = MessageCodes.FIND_STATIONS;
								messenger.sendMessage(msg);
							} catch (JSONException e) {
								Message msg = new Message();
								msg.arg1 = MessageCodes.PARSE_ERROR;
								msg.obj = e.getMessage();
								messenger.sendMessage(msg);
							}
						}
					});
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
