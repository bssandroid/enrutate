package com.enrutatemio.fragmentos;

import android.os.Bundle;

import com.enrutatemio.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity {

	
	public BaseActivity() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setTitle("Planear Viaje");
		// set the Behind View
		//setContentView(R.layout.menu_frame);
		setBehindContentView(R.layout.menu_frame);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setBehindScrollScale(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		
		
		sm.setOnOpenedListener(new OnOpenedListener() {

			@Override
			public void onOpened() {

				getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			}

		});
		sm.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {

				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			}
		});

	}


}
