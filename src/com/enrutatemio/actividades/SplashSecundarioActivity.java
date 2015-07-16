package com.enrutatemio.actividades;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.enrutatemio.R;
import com.enrutatemio.fragmentos.FragmentChangeActivity;
import com.enrutatemio.servicios.ServicioNoticias;
import com.enrutatemio.servicios.ServicioTwitter;
import com.enrutatemio.util.GlobalData;

public class SplashSecundarioActivity extends Activity {
	
	public ImageView iv = null;
	public ImageView salida = null;
	public ImageView persona = null;
	public ImageView texto;
	public Runnable t;
	public Animation anim ;
	public Animation anim2;
	public boolean estadoHilo = false;
	
	 public void onAttachedToWindow() {
         super.onAttachedToWindow();
         Window window = getWindow();
         window.setFormat(PixelFormat.RGBA_8888);
     }
	 private static final int SPLASH_DISPLAY_TIME = 4500; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splasmio);
		texto = (ImageView) findViewById(R.id.texto);
		PreferenceManager.setDefaultValues(this, R.layout.preferencias, true);
		
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashSecundarioActivity.this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("Name",true);
		editor.commit();
		 
		if(GlobalData.get("launchservice", SplashSecundarioActivity.this) == null)
		{
			 Intent mainIntent = new Intent(SplashSecundarioActivity.this,
	         		ServicioNoticias.class);
	         SplashSecundarioActivity.this.startService(mainIntent);
	         //servicio twitter
	         Intent serviceTwitter = new Intent(SplashSecundarioActivity.this,
	        		 ServicioTwitter.class);
	         SplashSecundarioActivity.this.startService(serviceTwitter);
	         GlobalData.set("launchservice", "Y", SplashSecundarioActivity.this);
		}
          
          
		 String splash = preferences.getString("splash", null);
		 if(splash != null && splash.equalsIgnoreCase("visto"))
		 {
			 SplashSecundarioActivity.this.finish();
			 Intent i = new Intent(SplashSecundarioActivity.this,
					 FragmentChangeActivity.class);
			 startActivity(i); 
			 overridePendingTransition(R.anim.transition_frag_in, R.anim.transition_frag_out);
		 }
		 else
		 {
				 StartAnimations();
				 t = new Runnable() {
						
				        public void run() {
		
				        	if(!estadoHilo)
				        	{
				           
				            	SharedPreferences.Editor editor = preferences.edit();
				      		    editor.putString("splash","visto");
				      		    editor.commit();
				            	Intent i = new Intent(SplashSecundarioActivity.this,
				            			FragmentChangeActivity.class);
								startActivity(i);
								SplashSecundarioActivity.this.finish();
								overridePendingTransition(R.anim.transition_frag_in, R.anim.transition_frag_out);
				        	}
							
							
				        }
				    };
				 
				 new Handler().postDelayed(t, SPLASH_DISPLAY_TIME);
		 }
	}
	private void StartAnimations() 
	{
		anim = AnimationUtils.loadAnimation(this, R.anim.mainfadein);
        anim.reset();
        RelativeLayout l = (RelativeLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
      
	    anim2 = AnimationUtils.loadAnimation(SplashSecundarioActivity.this, R.anim.splashfadeout);
	    anim2.reset();
	    texto.clearAnimation();
	    texto.startAnimation(anim2);
	}
	@Override
	public void onResume()
	{
		super.onResume();
	}
	@Override
	public void onStop()
	{
		SplashSecundarioActivity.this.finish();
		super.onStop();
		
	}
	@Override
	public void onPause()
	{
		SplashSecundarioActivity.this.finish();
		super.onPause();
		
	}

	@Override
	public void onDestroy()
	{
		SplashSecundarioActivity.this.finish();
		super.onDestroy();
	}
	@Override
	public void onBackPressed()
	{
		return;
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    
	  if(keyCode == KeyEvent.KEYCODE_BACK)
      {
    	 onBackPressed();
         return true;
      }
    
      return super.onKeyDown(keyCode, event);
      }
}