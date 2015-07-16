package com.enrutatemio.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enrutatemio.R;
import com.enrutatemio.actividades.ParadasActivity;
import com.enrutatemio.objectos.Ruta_paradas;

public class ViewHolderManejoRutas {

	public TextView textView, trayecto, horarioRuta;
	public TextView sentidoNorteSur;
	public TextView sentidoSurNorte;
	public ImageView imageView;
	public RelativeLayout layoutrutas;
	public RelativeLayout layoutInfo, layoutInfo2, containerInfo2;
	public FrameLayout containerInfo;
	public int pos;
	public Animation scale;
	public Animation fadein;
	public Animation scale_close;

	public Ruta_paradas ruta;

	public ViewHolderManejoRutas(View v, Ruta_paradas ruta) {
		this.ruta = ruta;
		
		this.textView = (TextView) v.findViewById(R.id.labelruta);
		this.trayecto = (TextView) v.findViewById(R.id.trayecto);
		this.imageView = (ImageView) v.findViewById(R.id.logorutas);
		this.layoutrutas = (RelativeLayout) v.findViewById(R.id.layoutrutas);
		this.layoutInfo = (RelativeLayout) v.findViewById(R.id.layoutInfo);
		this.layoutInfo2 = (RelativeLayout) v.findViewById(R.id.layoutInfo2);
		this.containerInfo = (FrameLayout) v.findViewById(R.id.containerInfo);
		this.containerInfo2 = (RelativeLayout) v
				.findViewById(R.id.containerInfo2);
		this.horarioRuta = (TextView) v.findViewById(R.id.horarioRuta);
		this.sentidoNorteSur = (TextView) v.findViewById(R.id.sentidoNorteSur);
		this.sentidoSurNorte = (TextView) v.findViewById(R.id.sentidoSurNorte);


		scale = AnimationUtils.loadAnimation(v.getContext(),
				R.anim.translate_info_ruta);
		fadein = AnimationUtils.loadAnimation(v.getContext(), R.anim.fadein);
		scale_close = AnimationUtils.loadAnimation(v.getContext(),
				R.anim.scale_close);

		initHandlers();

	}

	private void initHandlers() {

		layoutrutas.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ruta.setOpenLayout((ruta.isOpenLayout()) ? false : true);

				// si esta abierto cierra el layout
				if (containerInfo.getVisibility() == View.VISIBLE) {
				
					containerInfo2.setVisibility(View.GONE);
					scale_close.setAnimationListener(new AnimationListener() {
						@Override
						public void onAnimationEnd(Animation arg0) {
							containerInfo.setVisibility(View.GONE);

						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
						}

						@Override
						public void onAnimationStart(Animation arg0) {
						}
					});

					containerInfo.startAnimation(scale_close);

				} else {
					// animaci�n abrir el layout
					containerInfo.setVisibility(View.VISIBLE);

					scale.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {
						
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
						}

						@Override
						public void onAnimationEnd(Animation animation) {
							
							containerInfo2.setVisibility(View.VISIBLE);

							fadein.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {
								}

								@Override
								public void onAnimationRepeat(
										Animation animation) {
								}

								@Override
								public void onAnimationEnd(Animation animation) {
									layoutInfo2.setVisibility(View.VISIBLE);

								}
							});
							layoutInfo2.startAnimation(fadein);

						}
					});

					layoutInfo.startAnimation(scale);

				}
			}
		});

		sentidoNorteSur.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String[] paradas = ruta.sentido.split(",");
				String sentido = "";

				if (paradas != null) {
					String norteSur = paradas[0].replace("\n", "").trim();
					String surNorte = paradas[paradas.length - 1].replace("\n",
							"").trim();
					sentido = "Desde: " + norteSur + "\nA: " + surNorte;
				} else
					sentido = "Norte - Sur";
				
				launchIntentStops(v.getContext(),1,sentido);
			}
		});

		sentidoSurNorte.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String[] paradas = ruta.sentido.split(",");
				String sentido = "";

				if (paradas != null) {
					String norteSur = paradas[0].replace("\n", "").trim();
					String surNorte = paradas[paradas.length - 1].replace("\n",
							"").trim();
					sentido = "Desde: " + surNorte + "\nA: " + norteSur;
				} else
					sentido = "Sur - Norte";
				
				launchIntentStops(v.getContext(),0,sentido);
				

			}
		});

	}
	public void launchIntentStops(Context cxt, int sentido, String direccion)
	{
		Intent infoRuta = new Intent(cxt,
				ParadasActivity.class);
		infoRuta.putExtra("Ruta", ruta.ruta);
		infoRuta.putExtra("sentido", sentido);
		infoRuta.putExtra("titulo", "hola2");
		infoRuta.putExtra("direccion", direccion);
		infoRuta.putExtra("desde", 1);
		cxt.startActivity(infoRuta);
	}
}
