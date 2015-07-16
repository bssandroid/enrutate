package com.enrutatemio.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentPagerAdapterInte extends FragmentPagerAdapter{
	
	final int PAGE_COUNT = 2;

	/** Constructor of the class */
	public FragmentPagerAdapterInte(FragmentManager fm) {
		super(fm);
	}

	/** This method will be invoked when a page is requested to create */
	@Override
	public Fragment getItem(int arg0) {
		
		FragmentIntegracion myFragment = new FragmentIntegracion();
		Bundle data = new Bundle();
		data.putInt("current_pageInte", arg0+1);
		myFragment.setArguments(data);
		return myFragment;
	}

	/** Returns the number of pages */
	@Override
	public int getCount() {		
		return PAGE_COUNT;
	}
	@Override
    public CharSequence getPageTitle(int position) {
		String resultado = "";
		
	
		return resultado;
		
        
    }
}
