package com.enrutatemio.actividades;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.enrutatemio.R;
import com.enrutatemio.fragmentos.FragmentChangeActivity;

@SuppressLint("NewApi")
public class PreferenciasFragment extends SherlockPreferenceActivity {

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferencias);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// do your variables initialisations here except Views!!!

		Preference preference = findPreference("sincronizacion");
	
		if (preference instanceof ListPreference) {
			final ListPreference editTextPreference = (ListPreference) preference;
			editTextPreference
					.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

						@Override
						public boolean onPreferenceChange(
								Preference preference, Object newValue) {
							// TODO Auto-generated method stub
							String value = "";
							String tmp = newValue.toString();
							if (tmp.equals("0"))
								value = "15 min";

							else if (tmp.equals("1"))
								value = "30 min";
							else if (tmp.equals("2"))
								value = "1 Hora";
							else if (tmp.equals("3"))
								value = "3 Horas";
							else if (tmp.equals("4"))
								value = "6 Horas";
							else if (tmp.equals("5"))
								value = "12 Horas";
							else if (tmp.equals("6"))
								value = "1 Dia";

							editTextPreference.setSummary(value);
							editTextPreference.setValueIndex(Integer.parseInt(newValue.toString()));

							return false;
						}
					});
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		finish();
		Intent intento = new Intent(PreferenciasFragment.this,
				FragmentChangeActivity.class);
		intento.putExtra("fromnews", 1);
		startActivity(intento);
		overridePendingTransition(R.anim.transition_frag_in,
				R.anim.transition_frag_out);
		return super.onOptionsItemSelected(item);
	}

}
