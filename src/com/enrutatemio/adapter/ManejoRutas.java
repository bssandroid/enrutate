package com.enrutatemio.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.enrutatemio.R;
import com.enrutatemio.objectos.Ruta_paradas;

public class ManejoRutas extends ArrayAdapter<Ruta_paradas> {

	private LayoutInflater vi;
	private int op;
	private Activity ac;
	static Typeface robot_regular;
	static Typeface robot_light;

	ArrayList<Ruta_paradas> valuesArrayList;

	public ManejoRutas(Activity activity, final ArrayList<Ruta_paradas> values,
			int op) {
		super(activity, R.layout.lista2, values);
		vi = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.op = op;
		this.ac = activity;
		this.valuesArrayList = values;
		robot_regular = Typeface.createFromAsset(ac.getAssets(),
				"RobotoCondensed_Regular.ttf");
		robot_light = Typeface.createFromAsset(ac.getAssets(),
				"RobotoCondensed_Light.ttf");

	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolderManejoRutas holder;

		Ruta_paradas item = getItem(position);
		int index = valuesArrayList.indexOf(item);
		item = valuesArrayList.get(index);

		if (convertView == null) {
			convertView = vi.inflate(R.layout.lista2, parent, false);
			holder = new ViewHolderManejoRutas(convertView, item);
			holder.pos = index;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderManejoRutas) convertView.getTag();
			holder.pos = index;
			holder.ruta = item;
		}

		if (item != null) {
			holder.textView.setText(item.ruta);
			holder.layoutrutas.setBackgroundColor(Color.TRANSPARENT);
			holder.horarioRuta.setText((item.horario != null) ? item
					.horario : "No disponible");
			String[] paradas = item.sentido.split(",");

			if (paradas != null) {
				String norteSur = paradas[0];
				String surNorte = paradas[paradas.length - 1];
				if (norteSur.length() >= 26) 
					norteSur = norteSur.substring(0, 26) + "...";
				
				if (surNorte.length() >= 26) 
					surNorte = surNorte.substring(0, 26) + "...";
				
				holder.sentidoNorteSur.setText("DE: "
						+ norteSur.replace("\n", "").trim() + "\nA: "
						+ surNorte.replace("\n", "").trim());
				holder.sentidoSurNorte.setText("DE: "
						+ surNorte.replace("\n", "").trim() + "\nA: "
						+ norteSur.replace("\n", "").trim());
			}

			if (op == 1) {
				// buses expresos
				holder.imageView.setImageResource(R.drawable.busexpreso);
				if (item.trayecto != null)
					holder.trayecto.setText("" + item.trayecto);
			} else if (op == 2) {
				// buses pretroncal
				holder.imageView.setImageResource(R.drawable.buspretroncal);
				if (item.trayecto != null)
					holder.trayecto.setText("" + item.trayecto);
			} else if (op == 3) {
				// bus trocal
				holder.imageView.setImageResource(R.drawable.bustroncal);

				if (item.trayecto != null)
					holder.trayecto.setText("" + item.trayecto);
			} else if (op == 4) {
				// bus alimentador
				holder.imageView.setImageResource(R.drawable.busalimentador);
				if (item.trayecto != null)
					holder.trayecto.setText("" + item.trayecto);
				else
					holder.trayecto.setText(" ");
			} else 
				holder.imageView.setImageResource(R.drawable.busalimentador);
			

			// inicia la fila con el layout abierto o no dependiendo de si ha
			// sido abierto antes( recycler view list)
			holder.containerInfo.setVisibility((item.isOpenLayout()) ? View.VISIBLE: View.GONE);
			holder.containerInfo2.setVisibility((item.isOpenLayout()) ? View.VISIBLE: View.GONE);
		

		}

		return convertView;
	}

}
