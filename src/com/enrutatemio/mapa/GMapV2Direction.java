package com.enrutatemio.mapa;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class GMapV2Direction {
	public final static String MODE_DRIVING = "driving";
	public final static String MODE_WALKING = "walking";
	public String duracion;
	public GMapV2Direction() { }
	
	public Document getDocument(LatLng start, LatLng end) {
		String url = "http://maps.googleapis.com/maps/api/directions/xml?" 
        		+ "origin=" + start.latitude + "," + start.longitude  
        		+ "&destination=" + end.latitude + "," + end.longitude 
        		+ "&sensor=false&language=es&mode=walking";
		
		Log.d("GoogleMapsDirection", url);
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            InputStream in = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	public String getDocumentJSON(LatLng start, LatLng end) {
		String url = "http://maps.googleapis.com/maps/api/directions/json?" 
        		+ "origin=" + start.latitude + "," + start.longitude  
        		+ "&destination=" + end.latitude + "," + end.longitude 
        		+ "&sensor=false&language=es&avoid=highway|tolls&waypoints=optimize:true&smode=walking";
		
		String json = downloadUrl(url);
		Log.d("GoogleMapsDirection", url);
     
		return json;
	}
	/** A method to download json data from url */
	public String downloadUrl(String strUrl){
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
			// Log.d("Exception while downloading url", e.toString());
		} finally {
			try {
				iStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			urlConnection.disconnect();
		}
		return data;
	}
	
	
	/** Receives a JSONObject and returns a list of lists containing latitude and longitude */
	public ArrayList<Point> parse(JSONObject jObject){
		
		ArrayList<Point> routes = new ArrayList<Point>();
		JSONArray jRoutes = null;
		JSONArray jLegs = null;
		JSONArray jSteps = null;	
		LatLng punto;
		try {
			
			jRoutes = jObject.getJSONArray("routes");
			//Log.i("pasos", jRoutes.toString());
			/** Traversing all routes */
			for(int i=0, len = jRoutes.length(); i < len ; ++i){		
				jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
				//List path = new ArrayList<HashMap<String, String>>();
				
				/** Traversing all legs */
				for(int j=0 , lenj = jLegs.length() ;j < lenj;++j){
					jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");
					
					// duracion = (String)((JSONObject)((JSONObject)jLegs.get(0)).get("duration")).get("text");
					/** Traversing all steps */
					for(int k=0, lenk = jSteps.length() ; k < lenk ; ++k){
						String polyline = "";
						String instruccion_html = ((JSONObject)jSteps.get(k)).getString("html_instructions");
						polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
						Point point = new Point();
						List<LatLng> list = decodePoly(polyline);
						//Spanned marked_up = Html.fromHtml(instruccion_html);
						//String htmltratado = marked_up.toString();
						point.htmlInstructions = instruccion_html.replaceAll("\\<.*?>","");
						for (int l = 0, lenLista = list.size(); l < lenLista; ++l) {
							punto = new LatLng(list.get(l).latitude,list.get(l).longitude);
							point.position = punto;
						}
					
						
						//instrucciones.put(htmltratado, punto);
						routes.add(point);
//						guia.add(htmltratado);
//						/** Traversing all points */
//						for(int l=0, lenlista = list.size();l<lenlista;++l){
//							HashMap<String, String> hm = new HashMap<String, String>();
//							
//							hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
//							hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
//							path.add(hm);	
//							
//						}								
					}
					
					
				}
			}
			
		} catch (JSONException e) {			
			e.printStackTrace();
		}catch (Exception e){			
		}
		
		
		return routes;
	}	
	public String getDurationText (Document doc) {
		NodeList nl1 = doc.getElementsByTagName("duration");
        Node node1 = nl1.item(nl1.getLength() - 1);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "text"));
        Log.i("DurationText", node2.getTextContent());
		return node2.getTextContent();
	}
	
	public int getDurationValue (Document doc) {
		NodeList nl1 = doc.getElementsByTagName("duration");
        Node node1 = nl1.item(nl1.getLength() - 1);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
        Log.i("DurationValue", node2.getTextContent());
		return Integer.parseInt(node2.getTextContent());
	}
	
	public String getDistanceText (Document doc) {
		NodeList nl1 = doc.getElementsByTagName("distance");
        Node node1 = nl1.item(nl1.getLength() - 1);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "text"));
        Log.i("DistanceText", node2.getTextContent());
		return node2.getTextContent();
	}
	
	public int getDistanceValue (Document doc) {
		NodeList nl1 = doc.getElementsByTagName("distance");
        Node node1 = nl1.item(nl1.getLength() - 1);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
        Log.i("DistanceValue", node2.getTextContent());
		return Integer.parseInt(node2.getTextContent());
	}
	
	public String getStartAddress (Document doc) {
		NodeList nl1 = doc.getElementsByTagName("start_address");
        Node node1 = nl1.item(0);
        Log.i("StartAddress", node1.getTextContent());
		return node1.getTextContent();
	}
	
	public String getEndAddress (Document doc) {
		NodeList nl1 = doc.getElementsByTagName("end_address");
        Node node1 = nl1.item(0);
        Log.i("StartAddress", node1.getTextContent());
		return node1.getTextContent();
	}
	
	public String getCopyRights (Document doc) {
		NodeList nl1 = doc.getElementsByTagName("copyrights");
        Node node1 = nl1.item(0);
        Log.i("CopyRights", node1.getTextContent());
		return node1.getTextContent();
	}
	
	public ArrayList<String> getInstructions (Document doc) {
        NodeList nl1, nl2;
        ArrayList<String> listDirections = new ArrayList<String>();
        nl1 = doc.getElementsByTagName("step");
        if (nl1.getLength() > 0) {
            for (int i = 0; i < nl1.getLength(); i++) {
                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();
                Node directionNode = nl2.item(getNodeIndex(nl2, "html_instructions"));
                String instruction = directionNode.getTextContent();
                listDirections.add(instruction.replaceAll("\\<.*?>",""));
            }
        }
        return listDirections;  
    }
	
	public ArrayList<Point> getDirection (Document doc) {
		NodeList nl1, nl2, nl3;
        ArrayList<Point> listGeopoints = new ArrayList<Point>();
        nl1 = doc.getElementsByTagName("step");
        Point p = new Point();
        if (nl1.getLength() > 0) {
            for (int i = 0; i < nl1.getLength(); i++) {
                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();
                Node directionNode = nl2.item(getNodeIndex(nl2, "html_instructions"));
                String instruction = directionNode.getTextContent().replaceAll("\\<.*?>","");
                
                Node locationNode = nl2.item(getNodeIndex(nl2, "start_location"));
                nl3 = locationNode.getChildNodes();
                Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
                double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                double lng = Double.parseDouble(lngNode.getTextContent());
                p.position = new LatLng(lat, lng);
                p.htmlInstructions = instruction;
                listGeopoints.add(p);

                locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "points"));
                ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
                for(int j = 0 ; j < arr.size() ; j++) {
                	  p.position = new LatLng(arr.get(j).latitude, arr.get(j).longitude);
                     
                      listGeopoints.add(p);
                	//listGeopoints.add(new LatLng(arr.get(j).latitude, arr.get(j).longitude));
                }

                locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));
                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                lng = Double.parseDouble(lngNode.getTextContent());
                p.position = new LatLng(lat, lng);
                p.htmlInstructions = instruction;
                listGeopoints.add(p);
                //listGeopoints.add(new LatLng(lat, lng));
            }
        }
        
        return listGeopoints;
	}
	
	private int getNodeIndex(NodeList nl, String nodename) {
		for(int i = 0 ; i < nl.getLength() ; i++) {
			if(nl.item(i).getNodeName().equals(nodename))
				return i;
		}
		return -1;
	}
	
	private ArrayList<LatLng> decodePoly(String encoded) {
		ArrayList<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;
			
			LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
			poly.add(position);
		}
		return poly;
	}
}
