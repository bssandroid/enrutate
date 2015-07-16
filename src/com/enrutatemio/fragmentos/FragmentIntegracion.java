package com.enrutatemio.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enrutatemio.R;


public class FragmentIntegracion extends Fragment{
	
	int mCurrentPage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/** Getting the arguments to the Bundle object */
		Bundle data = getArguments();
		
		/** Getting integer data of the key current_page from the bundle */
		mCurrentPage = data.getInt("current_pageInte", 0);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		View v = inflater.inflate(R.layout.integracion, container,false);
		TextView tv = (TextView ) v.findViewById(R.id.integracion);
		
		if(mCurrentPage == 1)
			tv.setBackgroundResource(R.drawable.modo_int_uno);
		else if(mCurrentPage == 2)
			tv.setBackgroundResource(R.drawable.modo_int_dos);
		
	
		return tv;
		
	}

}
