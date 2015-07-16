package com.enrutatemio.fragmentos;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.enrutatemio.R;
import com.enrutatemio.BD.AdminSQLiteOpenHelper;
import com.enrutatemio.adapter.AdapterNoticias;
import com.enrutatemio.objectos.Noticia;
import com.enrutatemio.util.Mensajes;
import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.contextualundo.ContextualUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.contextualundo.ContextualUndoAdapter.DeleteItemCallback;


public class Noticias extends SherlockFragment implements  OnDismissCallback, DeleteItemCallback
{

	AdapterNoticias adapter;
	private ListView listanoticias;
	private ArrayList<Noticia> noticias = new ArrayList<Noticia>();
	int position = 0;
	
	//private static String url_all_story = "http://enrutatemio.com/enrutate/noticias.php";
	//JSONArray noti = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
      
        // create your view using LayoutInflater 
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.noticia, container, false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do your variables initialisations here except Views!!!
        setRetainInstance(true);
        new cargarNoticias().execute();
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
		
      
		listanoticias = (ListView) view.findViewById(R.id.listanoticias);
		
		
		
		listanoticias.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				  position = pos;
				  final Noticia noti = (Noticia) listanoticias.getItemAtPosition(pos);
				  new AlertDialog.Builder(getActivity())
     				.setIcon(R.drawable.inicio)
     				.setTitle("Noticia")
     				.setMessage(noti.noticia)
     				.setPositiveButton(android.R.string.ok,
     						new DialogInterface.OnClickListener() {
     							@Override
     							public void onClick(DialogInterface dialog,	int which)
     							{
     								
     							    noticiaLeida(noti.noticia);
     							    //adapter.notifyDataSetChanged();
     							   new cargarNoticias().execute();
     							   dialog.dismiss();	
     							 
     											
     							 
     							}
     						})
     				.show();
			}
		});
		
     
    }
	public void updateNoticia(String noticia)
	{
		try
		{
			AdminSQLiteOpenHelper data = new AdminSQLiteOpenHelper(getActivity());
			SQLiteDatabase db = data.getWritableDatabase();
			String query = "UPDATE noticias SET estado = 'Y' where noticia = '"+noticia+"'";
			db.execSQL(query);
			db.close();
			data.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void noticiaLeida(String noticia)
	{
		try
		{
			AdminSQLiteOpenHelper data = new AdminSQLiteOpenHelper(getActivity());
			SQLiteDatabase db = data.getWritableDatabase();
			String query = "UPDATE noticias SET estado = 'R' where noticia = '"+noticia+"'";
			db.execSQL(query);
			db.close();
			data.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public ArrayList<Noticia> cargarNoticiasBD()
	{
		noticias.clear();
		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity());
	    SQLiteDatabase    bd           = admin.getWritableDatabase();
		
		
		Cursor rs = bd.rawQuery("select DISTINCT noticia, fecha, estado from noticias where estado = 'N' or estado = 'R' order by fecha DESC", null);
		if(rs != null)
		{
			while (rs.moveToNext()) {
				
				Noticia n = new Noticia(rs.getString(0),rs.getString(1),rs.getString(2));
				noticias.add(n);
			}
		}
	
		rs.close();
		bd.close();
		admin.close();
		return noticias;
	}
	 class cargarNoticias extends AsyncTask<String, String, String> {

		 ProgressDialog pDialog;
		 TextView textprogress;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				
			    pDialog = new ProgressDialog(getActivity());
				//pDialog.setMessage("Cargando noticias...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
				pDialog.setContentView(R.layout.custom_progress);
				textprogress = (TextView)pDialog.findViewById(R.id.textprogress); 
				textprogress.setText("Cargando noticias...");
			}

			/**
			 * getting All empleados from url
			 * */
			protected String doInBackground(String... args) {
				noticias.clear();
				noticias = cargarNoticiasBD();
			
				return null;
			}
			

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
			
			
				 new Handler().postDelayed(new Runnable() {
				        @Override
				        public void run() {
				        	if(noticias != null && noticias.size() > 0)
							{
				        		adapter = new AdapterNoticias(getActivity(), noticias);
				        		ContextualUndoAdapter adap = new ContextualUndoAdapter(adapter, R.layout.undo_row, R.id.undo_row_undobutton,3000, Noticias.this);
				        	    adap.setAbsListView(listanoticias);
				        	    listanoticias.setAdapter(adap);
				        	    listanoticias.setSelection(position);
				        	        //listanoticias.setAdapter(animateDismissAdapter);
								//listanoticias.setAdapter(adapter);
							}
							else
							{
								
								Mensajes.mensajes(getActivity(), "No hay noticias nuevas ó debes conectarte a internet", 1);
							}
				        	pDialog.dismiss();
				        }
				    }, 300);
				
				
			}

		}
	  
	 @Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	        menu.findItem(R.id.position).setVisible(false);
	        menu.findItem(12).setVisible(false);
	        menu.findItem(R.id.drawStations).setVisible(false);
	        super.onCreateOptionsMenu(menu, inflater);
	    }

	@Override
	public void deleteItem(int position) {
		// TODO Auto-generated method stub
		  Noticia noti = (Noticia)listanoticias.getItemAtPosition(position);
		  updateNoticia(noti.noticia);
		  adapter.remove(noti);
	      adapter.notifyDataSetChanged();
	}

	@Override
	public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {
		// TODO Auto-generated method stub
		 for (int position : reverseSortedPositions) {
             Noticia noti = (Noticia)listView.getItemAtPosition(position);
             updateNoticia(noti.noticia);
         	adapter.remove(noti);
         }
		
	}    
	  
  
	
}//cierre de actividad