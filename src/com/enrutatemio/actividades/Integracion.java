package com.enrutatemio.actividades;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.enrutatemio.R;
import com.enrutatemio.fragmentos.FragmentChangeActivity;
import com.enrutatemio.fragmentos.FragmentPagerAdapterInte;


@SuppressLint("NewApi")
public class Integracion extends FragmentActivity {

    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.layout_integracion);
        getActionBar().setDisplayHomeAsUpEnabled(true); 
        getActionBar().setTitle(" Integración");
        /** Getting a reference to the ViewPager defined the layout file */        
        ViewPager pager = (ViewPager) findViewById(R.id.pagerintegracion);
        
        /** Getting fragment manager */
        FragmentManager fm = getSupportFragmentManager();
        
        /** Instantiating FragmentPagerAdapter */
        FragmentPagerAdapterInte pagerAdapter = new FragmentPagerAdapterInte(fm);
        
        /** Setting the pagerAdapter to the pager object */
        pager.setAdapter(pagerAdapter);
        
    }

    
    @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finishedActivity();
		super.onBackPressed();

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		finishedActivity();
		return super.onOptionsItemSelected(item);
	}

	public void finishedActivity() {
		finish();
		Intent intento = new Intent(Integracion.this,
				FragmentChangeActivity.class);
		intento.putExtra("fromnews", 1);
		startActivity(intento);
		overridePendingTransition(R.anim.transition_frag_in,
				R.anim.transition_frag_out);
	}

   
}
