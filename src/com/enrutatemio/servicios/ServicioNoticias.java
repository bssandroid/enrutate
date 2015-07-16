package com.enrutatemio.servicios;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.enrutatemio.R;
import com.enrutatemio.BD.AdminSQLiteOpenHelper;
import com.enrutatemio.actividades.NoticiasTwitter;
import com.enrutatemio.mapa.JSONParser;
import com.enrutatemio.util.GlobalData;

public class ServicioNoticias extends Service {
	private static final int ID_NOTIFICACION_CREAR = 1;
	private NotificationManager notManager;
	private static final  String url_all_story = "http://enrutatemio.com/enrutate/noticias.php";
    private static final int TIME = 86400000; //cada dia
	JSONArray noti = null;
	JSONParser jParser = new JSONParser();
	ArrayList<String> noticias = new ArrayList<String>();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {

					try {
						SharedPreferences sharedPreferences = PreferenceManager
								.getDefaultSharedPreferences(ServicioNoticias.this);
						boolean result = sharedPreferences.getBoolean("preferencianoticias",
								false);
                     //   Log.i("servicionoticias", ""+result);
						if (result)
							revisarNoticias(ServicioNoticias.this);
						
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}, 0, TIME);// Cada hora verifica si hay noticias nuevas
	

	    
	     return Service.START_STICKY;
	}
	
	

	private void insertarNoticia(String noticia, String fecha, String estado,
			Service s) {
		AdminSQLiteOpenHelper databasehelper = new AdminSQLiteOpenHelper(s);
		SQLiteDatabase db = databasehelper.getWritableDatabase();

		String query = "INSERT INTO noticias (noticia,fecha,estado) values('"
				+ noticia + "','" + fecha + "','" + estado + "')  ";

		db.execSQL(query);

		db.close();
		databasehelper.close();

	}

	public ArrayList<String> comparar(Service s) {
		try {
			noticias.clear();

			AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(s);
			SQLiteDatabase db = admin.getWritableDatabase();

			// Creamos el cursor
			Cursor c = db.rawQuery("select noticia from noticias", null);
			if (c != null) {
				while (c.moveToNext()) {
					String noticia = c.getString(0);
					noticias.add(noticia);

				}
			}
			c.close();
			db.close();
			admin.close();
		}

		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		return noticias;

	}

	@SuppressLint("SimpleDateFormat")
	public void revisarNoticias(Service s) {
		try {
			noticias = comparar(s);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL

			JSONObject json = jParser.makeHttpRequest(url_all_story, "GET",	params);

			if(json != null)
			{
			
				int cont = 0;
	
				
				// Checking for SUCCESS TAG
				int success = json.getInt("success");
	
				if (success == 1) {
					// empleados found
					// Getting Array of empleados
					noti = json.getJSONArray("noticias");
	
					// looping through All empleados
					for (int i = 0, len = noti.length(); i < len; ++i) {
						JSONObject c = noti.getJSONObject(i);
	
						String noticia = c.getString("noticia");
						String fecha = c.getString("fecha");
						String estado = c.getString("estado");
						Date date = new Date();
						SimpleDateFormat format = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						date = format.parse(fecha);
						String fechaFormato = format.format(date);
	
						if (!noticias.contains(noticia)) {
							++cont;
							insertarNoticia(noticia, fechaFormato, estado, s);
						} 
	
					}
					if (cont != 0) 
						notificar(s);
				}
	
				
	
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parar() {
		Intent servicioN = new Intent(ServicioNoticias.this,
				ServicioNoticias.class);
		ServicioNoticias.this.stopService(servicioN);
	}

	private void notificar(Service s) {

		int id = (int) System.currentTimeMillis();

		String ns = s.NOTIFICATION_SERVICE;
		notManager = (NotificationManager) s.getSystemService(ns);

		// Configuramos la notificaci�n
		int icono = R.drawable.inicio;
		CharSequence textoEstado = "Hay nuevas noticias";
		long hora = System.currentTimeMillis();

		Notification notif = new Notification(icono, textoEstado, hora);

		CharSequence titulo = "Enrútate Mio";
		CharSequence descripcion = "Hay nuevas noticias";

		Intent notIntent = new Intent(s, NoticiasTwitter.class);
	//	notIntent.putExtra("noti", 1);
		
		notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent contIntent = PendingIntent.getActivity(s, id, notIntent,
				1);

		notif.setLatestEventInfo(s, titulo, descripcion, contIntent);

		// AutoCancel: cuando se pulsa la notificai�n ésta desaparece
		notif.flags |= Notification.FLAG_AUTO_CANCEL;
       
		// Añadir sonido, vibraci�n y luces
		notif.defaults |= Notification.DEFAULT_SOUND;
		notif.defaults |= Notification.DEFAULT_VIBRATE;
		notif.defaults |= Notification.DEFAULT_ALL;

		// Enviar notificaci�n
		GlobalData.set("notification", "Y", s);
		notManager.notify(ID_NOTIFICACION_CREAR, notif);

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

}
