package com.enrutatemio.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.enrutatemio.R;
import com.enrutatemio.actividades.WebViewClientDemoActivity;
import com.enrutatemio.util.ConnectionDetector;

public class Tab3 extends SherlockFragment {

    public ImageView metrocali ;
    public ImageView transito ;
    public final static String TWITTER_TRANSITO  = "https://twitter.com/MOVILIDADCALI";
    public final static String TWITTER_METROCALI = "https://twitter.com/METROCALI";
  
    public Tab3() {
		// TODO Auto-generated constructor stub
    	setHasOptionsMenu(true);
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
      
        // create your view using LayoutInflater 
        return inflater.inflate(R.layout.tweets, container, false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setRetainInstance(true);
        // do your variables initialisations here except Views!!!
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    	metrocali = (ImageView) view.findViewById(R.id.metrocali);
		transito  = (ImageView) view.findViewById(R.id.transito);

		transito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	
            	if(ConnectionDetector.isConnectingToInternet(getActivity()))
            	{
            		Intent intent = new Intent(getActivity(), WebViewClientDemoActivity.class);
                	intent.putExtra("url", TWITTER_TRANSITO);  
                	intent.putExtra("titulo","Tweets Transito");
                  	startActivity(intent);
            	}
            	else
            		Toast.makeText(getActivity(), R.string.acceso_internet, Toast.LENGTH_SHORT).show();
            }
	    });
		metrocali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            
            	if(ConnectionDetector.isConnectingToInternet(getActivity()))
            	{
            		Intent intent = new Intent(getActivity(), WebViewClientDemoActivity.class);
                	intent.putExtra("url", TWITTER_METROCALI);  
                	intent.putExtra("titulo","Tweets Metrocali");
                 	startActivity(intent);
            	}
            	else
            		Toast.makeText(getActivity(), R.string.acceso_internet, Toast.LENGTH_SHORT).show();
           }
	    });
	}

	@Override
	public void onResume() 
	{
		super.onResume();
		
	} 
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.position).setVisible(false);
        menu.findItem(12).setVisible(false);
        menu.findItem(R.id.drawStations).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }    
 }
