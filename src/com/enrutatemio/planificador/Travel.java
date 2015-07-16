package com.enrutatemio.planificador;

import java.util.ArrayList;

import com.enrutatemio.mapa.Section;
import com.enrutatemio.objectos.ListadoRutasGoogle;
import com.google.android.gms.maps.model.LatLng;

public class Travel {

	public LatLng start, end;
	public boolean drawed = false;
	public ArrayList<ListadoRutasGoogle> listaRutas = new ArrayList<ListadoRutasGoogle>();
	public ArrayList<Section> sections = new ArrayList<Section>();

	public void clearSections() {
		sections.clear();
		start = null;
		end = null;
	}

	public void clear() {
		sections.clear();

	}

	public String toString() {
		return "{\"start\": {\"lat\": \"" + start.latitude + "\", \"lng\": \""
				+ start.longitude + "\"}, \"end\": {\"lat\": \"" + end.latitude
				+ "\", \"lng\": \"" + end.longitude + "\"}}";
	}

	public String getLikeGet() {
		return "x1=" + start.longitude + "&y1=" + start.latitude + "&x2="
				+ end.longitude + "&y2=" + end.latitude;
	}

	public boolean isDrawed() {
		return this.drawed;
	}

	public void addSection(Section s) {
		sections.add(s);
		
	}
}
