package com.enrutatemio.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.enrutatemio.R;
import com.enrutatemio.actividades.NoticiasTwitter;
import com.enrutatemio.fragmentos.FragmentChangeActivity;

public class WidgetEnrutateMio extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
	
		//	int currentWidgetId = appWidgetIds[i];
			/*String url = "http://www.tutorialspoint.com";
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.parse(url));
			
			//planear viaje
			Intent viaje = new Intent(context, FragmentChangeActivity.class);
		    viaje.putExtra("widget", 0);
		    viaje.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//ver listado de estaciones
			Intent estaciones = new Intent(context, FragmentChangeActivity.class);
			estaciones.putExtra("widget", 1);
			estaciones.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//ver información de rutas
			Intent rutas = new Intent(context, FragmentChangeActivity.class);
			rutas.putExtra("widget", 2);
			rutas.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			PendingIntent pending = PendingIntent.getActivity(context, 0,
					intent, 0);
			
			PendingIntent pendingTravel = PendingIntent.getActivity(context, 1,
					viaje, 0);
			
			PendingIntent pendingStations = PendingIntent.getActivity(context, 2,
					estaciones, 0);
			
			PendingIntent pendingRoutes = PendingIntent.getActivity(context, 3,
					rutas, 0);
			
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_dos);
			
			views.setOnClickPendingIntent(R.id.textNews, pending);
		    views.setOnClickPendingIntent(R.id.btnTravel, pendingTravel);
		    views.setOnClickPendingIntent(R.id.btnStations, pendingStations);
		    views.setOnClickPendingIntent(R.id.btnRoutes, pendingRoutes);
			
			 // Trigger widget layout update
			 AppWidgetManager.getInstance(context).updateAppWidget(
                    new ComponentName(context, WidgetEnrutateMio.class), views);
			 
			 super.onUpdate(context, appWidgetManager, appWidgetIds);*/
			
		//widget solo botones
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget_dos);
		
		//planear viaje
		Intent viaje = new Intent(context, FragmentChangeActivity.class);
	    viaje.putExtra("widget", 0);
	    viaje.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//ver listado de estaciones
		Intent estaciones = new Intent(context, FragmentChangeActivity.class);
		estaciones.putExtra("widget", 1);
		estaciones.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//ver información de rutas
		Intent rutas = new Intent(context, FragmentChangeActivity.class);
		rutas.putExtra("widget", 2);
		rutas.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		//noticias
		Intent noticias = new Intent(context, NoticiasTwitter.class);
		noticias.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
	
		//alimentadores
		Intent alimentadores = new Intent(context, FragmentChangeActivity.class);
		alimentadores.putExtra("widget", 3);
//		alimentadores.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		alimentadores.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		alimentadores.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	
		
		//expresos
	    Intent expresos = new Intent(context, FragmentChangeActivity.class);
	    expresos.putExtra("widget", 4);
//	     expresos.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	    expresos.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    expresos.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	   
	    
	  //pretroncales
	    Intent pretroncales = new Intent(context, FragmentChangeActivity.class);
	    pretroncales.putExtra("widget", 5);
//	    pretroncales.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	    pretroncales.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    pretroncales.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	
	    
	  //troncales
	    
	    Intent troncales = new Intent(context, FragmentChangeActivity.class);
	    troncales.putExtra("widget", 6);
//	    troncales.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	    troncales.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    troncales.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	  
		
		PendingIntent pendingTravel = PendingIntent.getActivity(context, 1,
				viaje, 0);
		
		PendingIntent pendingStations = PendingIntent.getActivity(context, 2,
				estaciones, 0);
		
		PendingIntent pendingRoutes = PendingIntent.getActivity(context, 3,
				rutas, 0);
	
		PendingIntent pendingNews = PendingIntent.getActivity(context, 4,
				noticias, 0);
		
		PendingIntent pendingAlimentadores = PendingIntent.getActivity(context, 5,
				alimentadores, 0);
		
		PendingIntent pendingExpress = PendingIntent.getActivity(context, 6,
				expresos, 0);
		
		PendingIntent pendingPreTroncal = PendingIntent.getActivity(context, 7,
				pretroncales, 0);
		
		PendingIntent pendingTroncal = PendingIntent.getActivity(context, 8,
				troncales, 0);
		
		
	    views.setOnClickPendingIntent(R.id.btnTravel, pendingTravel);
	    views.setOnClickPendingIntent(R.id.btnStations, pendingStations);
	    views.setOnClickPendingIntent(R.id.btnRoutes, pendingRoutes);
	    views.setOnClickPendingIntent(R.id.btnNews, pendingNews);
	    views.setOnClickPendingIntent(R.id.btnAlimentadores, pendingAlimentadores);
	    views.setOnClickPendingIntent(R.id.btnExpreso, pendingExpress);
	    views.setOnClickPendingIntent(R.id.btnPreTroncal, pendingPreTroncal);
	    views.setOnClickPendingIntent(R.id.btnTroncal, pendingTroncal);
		
		 // Trigger widget layout update
		 AppWidgetManager.getInstance(context).updateAppWidget(
                new ComponentName(context, WidgetEnrutateMio.class), views);
		 
		 super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		
		
	}
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}
}