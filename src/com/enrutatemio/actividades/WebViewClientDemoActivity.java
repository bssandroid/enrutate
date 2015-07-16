package com.enrutatemio.actividades;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.enrutatemio.R;
import com.enrutatemio.fragmentos.Tab3;
import com.enrutatemio.util.Visibility;

public class WebViewClientDemoActivity extends SherlockActivity {
	/** Called when the activity is first created. */

	public WebView webView;
	public Activity activity = this;
	public ProgressBar progressBar1;
	public TextView text_progress;
	public String titulo = "";
	
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.webview);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		webView = (WebView) findViewById(R.id.webView);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		text_progress = (TextView) findViewById(R.id.text_progress);
		webView.getSettings().setJavaScriptEnabled(true);
		
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
	

		Bundle b = getIntent().getExtras();
		String urlTweet = "";

		if (b != null) {
			urlTweet = b.getString("url");
			titulo = b.getString("titulo");
		}

		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				activity.setTitle("" + titulo);
				//activity.setProgress(progress * 100);

				Visibility.visible(text_progress);
				text_progress.setText("" + progress + "%");
				if (progress == 100) {
					activity.setTitle(R.string.app_name);
					Visibility.gone(text_progress);
					text_progress.setText("");
				}

			}
		});

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				Visibility.visible(progressBar1);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				Visibility.gone(progressBar1);
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		if (urlTweet.equals(""))
			webView.loadUrl(Tab3.TWITTER_METROCALI);
		else
			webView.loadUrl(urlTweet);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		webView.loadUrl("");
		Visibility.gone(progressBar1);
		finish();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {
		Visibility.gone(progressBar1);
		webView.loadUrl("");
		finish();
		super.onDestroy();

	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
