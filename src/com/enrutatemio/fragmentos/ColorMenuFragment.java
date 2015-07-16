package com.enrutatemio.fragmentos;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.enrutatemio.R;
import com.enrutatemio.actividades.NoticiasTwitter;
import com.enrutatemio.actividades.PreferenciasFragment;
import com.enrutatemio.adapter.AdapterOpciones;
import com.enrutatemio.objectos.Opciones;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("NewApi")
public class ColorMenuFragment extends ListFragment {
	// View ColoredView = null;
	FragmentChangeActivity fragmentChangeActivity;
	
	View v = null;
	ArrayList<Opciones> op = new ArrayList<Opciones>();

	public ColorMenuFragment() {
		// TODO Auto-generated constructor stub
		
	}
	public ColorMenuFragment(FragmentChangeActivity fragmentChangeActivity) {
		this.fragmentChangeActivity = fragmentChangeActivity;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		if(fragmentChangeActivity == null)
			this.fragmentChangeActivity = (FragmentChangeActivity) getActivity();
			
		v = inflater.inflate(R.layout.list, container, false);
	
		op.add(new Opciones(R.drawable.icono_trazarruta, "Planear un viaje"));// 1
		op.add(new Opciones(R.drawable.icono_estaciones_menu,
				"Listado estaciones"));// 2
		op.add(new Opciones(R.drawable.ic_infomio, "Información rutas"));// 3
		op.add(new Opciones(R.drawable.ic_twitter, "Tweets"));// 4
		op.add(new Opciones(R.drawable.ic_noticias, "Noticias"));// 5
		op.add(new Opciones(R.drawable.ic_preferencias, "Preferencias"));// 6
		op.add(new Opciones(R.drawable.ic_acercade, "Acerca de"));// 7
		

		return v;
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		AdapterOpciones opcionesLista = new AdapterOpciones(getActivity(), op);
		getListView().setAdapter(opcionesLista);
		
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		getActivity().getActionBar().setIcon(R.drawable.ic_launcher);
		String title = "Planear un viaje";

		switch (position) {
		case 0:
			replaceFragment(fragmentChangeActivity.mapFragment);
			title = "Planear viaje";
			fragmentChangeActivity.getSlidingMenu().setTouchModeAbove(
					SlidingMenu.TOUCHMODE_NONE);
			break;
		case 1:
			replaceFragment(fragmentChangeActivity.listaEstacionesMIO);
			getActivity().getActionBar().setIcon(R.drawable.iconoestacionhd);
			title = "Listado estaciones";
			break;
		case 2:
			title = " Información rutas";
			replaceFragment(fragmentChangeActivity.informacionMIO);
			break;

		case 3:
			replaceFragment(fragmentChangeActivity.tweets);
			title = "  Tweets";
			break;
		case 4:
			// lanzar actividad de noticias
			getActivity().finish();
			Intent in = new Intent(getActivity(), NoticiasTwitter.class);
			startActivity(in);
			getActivity().overridePendingTransition(R.anim.transition_frag_in,
					R.anim.transition_frag_out);

			break;
		case 5:
			Intent i = new Intent(getActivity(), PreferenciasFragment.class);
			startActivity(i);
			getActivity().overridePendingTransition(R.anim.transition_frag_in,
					R.anim.transition_frag_out);
			break;
		case 6:
			title = " Acerca de";
			replaceFragment(fragmentChangeActivity.acercade);
			break;
		}

		if (position != 0)
			fragmentChangeActivity.getSlidingMenu().setTouchModeAbove(
					SlidingMenu.TOUCHMODE_FULLSCREEN);

		if (title != null)
			fragmentChangeActivity.getSupportActionBar().setTitle(title);

	}

	public void replaceFragment(Fragment fragment) {
		fragmentChangeActivity.replaceFragment(fragment);
	}

}
