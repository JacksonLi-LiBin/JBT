package com.lb.jbt;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.lb.constants.MobileNetStatus;
import com.lb.tools.ConvertImage;
import com.lb.tools.ReadProperties;

public class MainActivity extends Activity {
	private ImageView main_bg_img;
	private SharedPreferences spf = null;
	private SharedPreferences.Editor editor = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		judgeMobileNet();

		main_bg_img = (ImageView) findViewById(R.id.main_bg_img);
		// jump to login activity after set seconds
		final Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				startActivity(intent);
				MainActivity.this.finish();
			}
		};
		// get background image from sharedpreference
		spf = getSharedPreferences("jbt", MODE_PRIVATE);
		editor = spf.edit();
		String main_bg_stream = spf.getString("main_bg_stream", "");
		String main_bg_time = spf.getString("showTime", "");
		Bitmap main_bg = null;
		if (main_bg_stream.trim().length() == 0) {
			main_bg = BitmapFactory.decodeResource(getResources(),
					com.lb.jbt.R.drawable.main_bg);
			if (MobileNetStatus.isNetUsable) {
				new DownLoadMainBGAsync().execute(ReadProperties.read("url",
						"jackson_ad_login_cp")
						+ "CommonMethods/getBackgroundImage");
			}
			timer.schedule(timerTask, 5000);
		} else {
			try {
				main_bg = ConvertImage.base64ToBitmap(main_bg_stream);
				timer.schedule(timerTask, Integer.parseInt(main_bg_time));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		main_bg_img.setImageBitmap(main_bg);
	}

	// download background image if not exist in sharedpreperence
	private class DownLoadMainBGAsync extends AsyncTask<String, Void, Bitmap> {
		private String bg_url = "";
		private URL url = null;
		private HttpURLConnection conn = null;
		private String main_bg_time = "";

		@Override
		protected Bitmap doInBackground(String... params) {
			String bg_req_url = params[0];
			try {
				url = new URL(bg_req_url);
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(10 * 1000);
				if (conn.getResponseCode() == 200) {
					InputStream is = conn.getInputStream();
					ByteArrayOutputStream bo = new ByteArrayOutputStream();
					int size = 1024;
					byte[] data = new byte[size];
					int len = 0;
					while ((len = is.read(data, 0, size)) != -1) {
						bo.write(data, 0, len);
					}
					if (!new String(bo.toByteArray(), "utf-8").equals("false")) {
						JSONObject bg_info = new JSONArray(new String(
								bo.toByteArray(), "utf-8")).getJSONObject(0);
						bg_url = bg_info.getString("path");
						main_bg_time = bg_info.getString("showTime");
						data = null;
						url = null;
						conn = null;
						if (bg_url != "false") {
							url = new URL(bg_url);
							conn = (HttpURLConnection) url.openConnection();
							conn.setConnectTimeout(60 * 1000);
							if (conn.getResponseCode() == 200) {
								InputStream iss = conn.getInputStream();
								Bitmap bitmap = BitmapFactory.decodeStream(iss);
								return bitmap;
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			String bg_base64 = ConvertImage.bitmapToBase64(result);
			// save background image into sharedpreference
			editor.putString("main_bg_stream", bg_base64);
			editor.putString("showTime", main_bg_time);
			editor.commit();
		}

	}

	// get mobile net status
	private void judgeMobileNet() {
		ConnectivityManager manager = (ConnectivityManager) MainActivity.this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileNetworkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetworkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (!mobileNetworkInfo.isConnected() && !wifiNetworkInfo.isConnected()) {
			MobileNetStatus.isNetUsable = false;
		} else {
			MobileNetStatus.isNetUsable = true;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (editor != null) {
			editor.clear();
		}
	}
}
