package com.enrutatemio.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enrutatemio.R;
import com.enrutatemio.objectos.Opciones;

@SuppressLint("ViewHolder")
public class AdapterOpciones extends ArrayAdapter<Opciones> {

	private final ArrayList<Opciones> values;

	private Activity activity;
	private Typeface typeOptions;

	public AdapterOpciones(Activity context, final ArrayList<Opciones> values) {
		super(context, R.layout.listaopciones, values);
		this.activity = context;
		this.values = values;
	}

	public View getView(int pos, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		View item = convertView;

		if (item == null || !(item.getTag() instanceof ViewHolder)) {

			LayoutInflater mInflater = LayoutInflater.from(activity);
			item = mInflater.inflate(R.layout.listaopciones, parent, false);
			// Creates a ViewHolder and store references to the two children
			// views we want to bind data to.
			holder = new ViewHolder();

			holder.textView = (TextView) item.findViewById(R.id.textoopciones);
			holder.imageView = (ImageView) item.findViewById(R.id.imagenopcion);
			// Save holder
			item.setTag(holder);
		} else 
			holder = (ViewHolder) item.getTag();
		
		Opciones op = values.get(pos);

		if (op != null) {
			holder.textView.setTypeface(typeOptions);
			holder.textView.setText(op.texto);
			holder.imageView.setImageResource(op.imagen);
		}

		return item;
	}

	class ViewHolder {
		TextView textView;
		ImageView imageView;
	}

}
