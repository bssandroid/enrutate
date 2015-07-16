package com.enrutatemio.servicios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// here is the OnRevieve methode which will be called when boot completed
public class BootCompleted extends BroadcastReceiver{
     @Override
     public void onReceive(Context context, Intent intent) {
 //we double check here for only boot complete event
 if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))
   {
     //here we start the service   
	 
     Intent serviceIntent = new Intent(context, ServicioNoticias.class);
     context.startService(serviceIntent);
     
     //inicio servicio twitter luego de reiniciar el dispositivo
     Intent serviceTwitter = new Intent(context, ServicioTwitter.class);
     context.startService(serviceTwitter);
   }
 }
}