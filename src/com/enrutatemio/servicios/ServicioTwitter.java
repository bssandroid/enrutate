package com.enrutatemio.servicios;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.enrutatemio.util.ConnectionDetector;
import com.enrutatemio.util.TwitterChannel;

public class ServicioTwitter extends Service {

	TwitterChannel twitterChannel;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		twitterChannel = TwitterChannel.getInstance(ServicioTwitter.this);
		
		long time = getTimeSync();
		Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {

					try {
						if(ConnectionDetector.isConnectingToInternet(ServicioTwitter.this))
						{
							SharedPreferences sharedPreferences = PreferenceManager
									.getDefaultSharedPreferences(ServicioTwitter.this);
							boolean result = sharedPreferences.getBoolean("preferencianoticias",false);
	                        
							if (result)
							   twitterChannel.connectTwitter();
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}, 0, time);// Cada hora verifica si hay noticias nuevas
	

	    
	     return Service.START_STICKY;
	}
	
	private long getTimeSync() {

		SharedPreferences mySharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(ServicioTwitter.this);
		String tmp = mySharedPreferences.getString(
				"sincronizacion", "3600000");

		int option = (tmp != null) ? Integer.parseInt(tmp) : -1;

		switch (option) {
		case 0:
			return 900000;

		case 1:
			return 1800000;

		case 2:
			return 3600000;

		case 3:
			return 1800000;

		case 4:
			return 10800000;

		case 5:
			return 21600000;
		case 6:
			return 86400000;

		default:
			return 3600000;

		}

	}
	
	

}
