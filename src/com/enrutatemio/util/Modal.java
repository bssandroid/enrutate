package com.enrutatemio.util;

import java.lang.reflect.InvocationTargetException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class Modal {

	private AlertDialog dialog;

	/**
	 * ejemplo con icono:  </br>
	 * &nbsp;&nbsp;&nbsp;new Modal(MainActivity.this, "text title", "text body", R.drawable.ic_launcher).show(); </br></br>
	 * &nbsp;&nbsp;&nbsp;Modal myModal = new Modal(MainActivity.this, "text title", "text body", R.drawable.ic_launcher) </br>
	 * &nbsp;&nbsp;&nbsp;myModal.show(); </br>
	 *  </br>
	 * sin icono: </br>
	 * &nbsp;&nbsp;&nbsp;new Modal(MainActivity.this,"exitos", "callback ok ", 0).show(); </br>
	 *  </br>
	 * @param context 
	 * @param title
	 * @param body
	 * @param icon ejemplo:(R.drawable.ic_launcher)
	 */
	public Modal(Context context, String title, String body, int icon) {
		dialog = new AlertDialog.Builder(context).setTitle(title)
				.setMessage(body).create();
		if (icon != 0)
			dialog.setIcon(icon);

		
	}

	public void addButtonYES(String text,
			DialogInterface.OnClickListener listener) {
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, text, listener);
	}

	public void addButtonYES(String text, final Object obj,
			final String callback) {

		dialog.setButton(DialogInterface.BUTTON_POSITIVE, text,
				createListener(text, obj, callback));
	}

	public void addButtonNO(String text,
			DialogInterface.OnClickListener listener) {
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, text, listener);
	}

	public void addButtonNO(String text, final Object obj, final String callback) {
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, text,
				createListener(text, obj, callback));
	}

	public void addButtonCANCEL(String text,
			DialogInterface.OnClickListener listener) {
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, text, listener);
	}

	public void addButtonCANCEL(String text, final Object obj,
			final String callback) {
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, text,
				createListener(text, obj, callback));
	}

	public DialogInterface.OnClickListener createListener(String text,
			final Object obj, final String callback) {

		DialogInterface.OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				java.lang.reflect.Method method = null;
				try {
					method = obj.getClass().getMethod(callback);
				} catch (SecurityException e) {
					// ...
				} catch (NoSuchMethodException e) {
					// ...
				}

				try {
					method.invoke(obj, null);
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}

			}
		};
		return listener;
	}

	public void show() {
		dialog.show();
	}
}
