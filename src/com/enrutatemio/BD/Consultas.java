package com.enrutatemio.BD;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.enrutatemio.objectos.Estacion;
import com.enrutatemio.objectos.PuntoRecarga;
import com.enrutatemio.objectos.Ruta_paradas;
import com.google.android.gms.maps.model.LatLng;

public class Consultas {

	// consultas a la base de datos

	public static String tipoParada(Activity activity, String a) {
		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(activity);
		SQLiteDatabase bd = admin.getWritableDatabase();
		String nombre = "";
		Cursor fila = null;

		fila = bd.rawQuery("select tipo from estaciones where nombre LIKE '%"
				+ a + "%'", null);

		if (fila.moveToFirst()) {
			String numt = fila.getString(0);
			if (numt.equalsIgnoreCase("0"))
				nombre = "Estación";
			else if (numt.equalsIgnoreCase("1"))
				nombre = "Parada";
			else if (numt.equalsIgnoreCase("2"))
				nombre = "Alimentador";

		}
		fila.close();
		bd.close();
		admin.close();

		return nombre;

	}

	public static ArrayList<String> consultaAmbosSentidos(Activity activity,
			String a) {
		ArrayList<String> paradas = new ArrayList<String>();
		try {
			AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(activity);
			SQLiteDatabase bd = admin.getWritableDatabase();
			String para = "";
			Cursor fila = null;
			String partidas[] = null;

			fila = bd.rawQuery(
					"select sentido,paradas from ruta_paradas where ruta LIKE '%"
							+ a + "%' ", null);

			if (fila != null) {
				while (fila.moveToNext()) {
					para = fila.getString(1);
					partidas = para.split(",");
					for (int i = 0, len = partidas.length; i < len; ++i)
						paradas.add(partidas[i]);

				}
			}
			fila.close();
			bd.close();
			admin.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return paradas;

	}

	public static String consultaListadoParadas(Activity ac, String a) {
		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(ac);
		SQLiteDatabase bd = admin.getWritableDatabase();

		Cursor fila = null;
		String dir = "";

		fila = bd.rawQuery(
				"select direccion, listadorutas from estaciones where nombre  = '"
						+ a.toUpperCase() + "'", null);

		if (fila != null)
			if (fila.moveToFirst())
				dir = fila.getString(0) + ";" + fila.getString(1);

		fila.close();
		bd.close();
		admin.close();
		return dir;

	}

	public static String[] sentidodelaruta(Activity ac, String ruta, String g[]) {

		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(ac);
		SQLiteDatabase bd = admin.getWritableDatabase();
		String sen = "";
		String paradas = "";
		Cursor fila = bd.rawQuery(
				"select sentido, paradas from ruta_paradas where ruta LIKE '%"
						+ ruta + "%'", null);

		if (fila != null) {
			while (fila.moveToNext()) {
				sen = fila.getString(0);
				paradas = fila.getString(1);
				if (sen.equalsIgnoreCase("surnorte")) {
					String par[] = paradas.split(",");
					g[0] = par[0] + " a " + par[par.length - 1];

				} else {
					String par[] = paradas.split(",");
					g[1] = par[0] + " a " + par[par.length - 1];

				}
			}
			// g[2] = "Salir";
			fila.close();
			bd.close();
			admin.close();
		}

		return g;

	}

	public static String consultaUnSentido(Activity ac, String a) {

		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(ac);
		SQLiteDatabase bd = admin.getWritableDatabase();
		Cursor fila = null;

		fila = bd.rawQuery(
				"select sentido,paradas from ruta_paradas where ruta LIKE '%"
						+ a + "%' ", null);

		int contador = 0;
		StringBuilder acum = new StringBuilder();
		String paradas = "";
		String sentido = "";

		if (fila != null) {
			while (fila.moveToNext()) {
				if (contador == 0) {
					sentido = fila.getString(0);
					paradas = fila.getString(1);
					acum.append(paradas + ";" + sentido + ";");
				} else {
					sentido = fila.getString(0);
					paradas = fila.getString(1);
					acum.append(paradas + ";" + sentido);
				}

				++contador;
			}
		}
		fila.close();
		bd.close();
		admin.close();
		return acum.toString();

	}

	public static LatLng buscarDireccion2(Activity ac, String a) {
		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(ac);
		SQLiteDatabase bd = admin.getWritableDatabase();

		Cursor fila = null;
		LatLng p = null;

		fila = bd.rawQuery(
				"select latitud,longitud from estaciones where nombre LIKE '%"
						+ a + "%'", null);

		while (fila.moveToNext()) {
			String latitud = fila.getString(0);
			String longitud = fila.getString(1);

			double la = Double.parseDouble(latitud) / 1E6;
			double lo = Double.parseDouble(longitud) / 1E6;
			p = new LatLng(la, lo);

		}

		bd.close();
		fila.close();
		admin.close();
		return p;
	}

	public static ArrayList<String> consultaSentidos(Activity activity,
			String a, int sen) {

		ArrayList<String> paradas = new ArrayList<String>();
		try {

			AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(activity);
			SQLiteDatabase bd = admin.getWritableDatabase();
			String para = "";
			Cursor fila = null;
			String partidas[] = null;
			if (sen == 0)
				fila = bd.rawQuery(
						"select sentido,paradas from ruta_paradas where ruta LIKE '%"
								+ a + "%' and sentido = 'surnorte'", null);
			else if (sen == 1)
				fila = bd.rawQuery(
						"select sentido,paradas from ruta_paradas where ruta LIKE '%"
								+ a + "%' and sentido = 'nortesur'", null);

			if (fila.moveToFirst()) {
				para = fila.getString(1);
				partidas = para.split(",");
				int tamPartidas = partidas.length;
				for (int i = 0; i < tamPartidas; ++i)
					paradas.add(partidas[i]);

			}
			fila.close();
			bd.close();
			admin.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return paradas;

	}

	public static HashMap<String, LatLng> buscarDireccion(Activity activity,
			String a) {

		HashMap<String, LatLng> resultadoBusqueda = new HashMap<String, LatLng>();
		try {
			AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(activity);
			SQLiteDatabase bd = admin.getWritableDatabase();

			Cursor fila = null;
			LatLng p = null;

			fila = bd.rawQuery(
					"select latitud,longitud, nombre from estaciones where nombre LIKE '%"
							+ a + "%'", null);

			if (fila != null) {
				while (fila.moveToNext()) {
					String latitud = fila.getString(0);
					String longitud = fila.getString(1);
					String nombre = fila.getString(2);
					double la = Double.parseDouble(latitud) / 1E6;
					double lo = Double.parseDouble(longitud) / 1E6;
					p = new LatLng(la, lo);
					resultadoBusqueda.put(nombre, p);

				}
			}
			fila.close();
			bd.close();
			admin.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return resultadoBusqueda;
	}

	public static ArrayList<Estacion> cargarEstacionesLista(Activity ac) {
		ArrayList<Estacion> estaciones = new ArrayList<Estacion>();
		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(ac);
		SQLiteDatabase bd = admin.getWritableDatabase();

		Cursor fila = null;
		fila = bd
				.rawQuery(
						"select nombre,latitud,longitud,listadorutas,tipo,direccion from estaciones where tipo = '0' ORDER BY nombre ASC",
						null);

		if (fila != null)
			while (fila.moveToNext())
				estaciones.add(new Estacion(fila.getString(0), fila
						.getString(1), fila.getString(2), fila.getString(3),
						fila.getString(4), fila.getString(5)));

		fila.close();
		bd.close();
		admin.close();
		return estaciones;
	}

	public static ArrayList<String> consulta(Activity ac, String a, int sen) {
		ArrayList<String> paradas = new ArrayList<String>();
		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(ac);
		SQLiteDatabase bd = admin.getWritableDatabase();
		String para = "";
		Cursor fila = null;
		String partidas[] = null;
		if (sen == 0) {
			fila = bd.rawQuery(
					"select sentido,paradas from ruta_paradas where ruta LIKE '%"
							+ a + "%' and sentido = 'surnorte'", null);

		} else if (sen == 1) {
			fila = bd.rawQuery(
					"select sentido,paradas from ruta_paradas where ruta LIKE '%"
							+ a + "%' and sentido = 'nortesur'", null);
		}

		if (fila.moveToFirst()) {
			para = fila.getString(1);
			partidas = para.split(",");

			for (int i = 0, len = partidas.length; i < len; ++i)
				paradas.add(partidas[i].replace("\n", "").trim());

		}
		bd.close();
		fila.close();
		admin.close();
		return paradas;

	}

	public static ArrayList<Ruta_paradas> listarRutasOrdenadas(
			Activity activity, String a) {
		ArrayList<Ruta_paradas> rutas = new ArrayList<Ruta_paradas>();

		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(activity);
		SQLiteDatabase bd = admin.getWritableDatabase();
		Cursor fila = null;

		fila = bd
				.rawQuery(
						"select DISTINCT  (ruta), trayecto, horario, paradas from ruta_paradas where tipo = '"
								+ a
								+ "' and estado = 'A' and sentido = 'nortesur' ORDER BY ruta ASC",
						null);

		if (fila != null)
			while (fila.moveToNext())
				rutas.add(new Ruta_paradas(fila.getString(0),
						fila.getString(1), fila.getString(2), fila.getString(3)));

		fila.close();
		bd.close();
		admin.close();
		return rutas;

	}

	public static ArrayList<PuntoRecarga> cargarPuntosdeRecarga(
			Activity activity) {
		ArrayList<PuntoRecarga> puntorecarga = new ArrayList<PuntoRecarga>();
		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(activity);
		SQLiteDatabase bd = admin.getWritableDatabase();

		Cursor fila = null;

		fila = bd
				.rawQuery(
						"select nombre,direccion, tipo from puntorecarga where estado = 'A'  ORDER BY nombre ASC",
						null);

		if (fila != null) 
			while (fila.moveToNext()) 
				puntorecarga.add(new PuntoRecarga(fila.getString(0),fila.getString(1), fila.getString(2)));

			
		
		fila.close();
		bd.close();
		admin.close();
		return puntorecarga;

	}

}
