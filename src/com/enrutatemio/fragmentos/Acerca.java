package com.enrutatemio.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.enrutatemio.R;

public class Acerca extends SherlockFragment {

	public Acerca() {
		// TODO Auto-generated constructor stub
		setHasOptionsMenu(true);
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		// create your view using LayoutInflater
		return inflater.inflate(R.layout.acercade, container, false);
	}

	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getView().setOnKeyListener(new View.OnKeyListener() {
	        @Override
	        public boolean onKey(View v, int keyCode, KeyEvent event) {
	         
	            if( keyCode == KeyEvent.KEYCODE_BACK ) {
	            	getActivity().finish();
	        		Intent intento = new Intent(getActivity(),
	        				FragmentChangeActivity.class);
	        		intento.putExtra("fromnews", 1);
	        		startActivity(intento);
	        		getActivity().overridePendingTransition(R.anim.transition_frag_in,
	        				R.anim.transition_frag_out);
	                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	                return true;
	            } else {
	                return false;
	            }
	        }
	    });
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.findItem(R.id.position).setVisible(false);
		menu.findItem(12).setVisible(false);
		menu.findItem(R.id.drawStations).setVisible(false);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	
	
	
	
	

}
