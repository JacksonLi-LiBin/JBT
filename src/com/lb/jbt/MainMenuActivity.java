package com.lb.jbt;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.lb.constants.MobileNetStatus;
import com.lb.tools.ReadProperties;
import com.lb.widgets.CustomAlertDialogWithButtons;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class MainMenuActivity extends Activity {
	private SharedPreferences spf = null;
	private SharedPreferences.Editor editor = null;
	private String storedToken = "";
	private String userId = "";
	private String fullName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		spf = getSharedPreferences("jbt", MODE_PRIVATE);
		userId = spf.getString("userId", "");
		storedToken = spf.getString("userToken", "");
		fullName = spf.getString("fullName", "");
		editor = spf.edit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			logout();
		}
		return false;
	}

	// user logout
	private void logout() {
		CustomAlertDialogWithButtons.Builder builder = new CustomAlertDialogWithButtons.Builder(MainMenuActivity.this);
		builder.setTitle(getResources().getString(R.string.dialog_title));
		builder.setMessage(getResources().getString(R.string.logout_message));
		builder.setPosiClickListener(getResources().getString(R.string.dialog_sure),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (MobileNetStatus.isNetUsable) {
							new LogoutAsync().execute(dialog);
						} else {
							dialog.dismiss();
							MainMenuActivity.this.finish();
						}
					}
				});
		builder.setNegaClickListener(getResources().getString(R.string.dialog_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		Dialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	private class LogoutAsync extends AsyncTask<DialogInterface, Void, String> {
		private final OkHttpClient okHttpClient = new OkHttpClient();
		private DialogInterface dialog = null;
		private final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

		@Override
		protected String doInBackground(DialogInterface... params) {
			dialog = params[0];
			try {
				HttpUrl urlLogout = HttpUrl
						.parse(ReadProperties.read("url", "jackson_ad_login_cp") + "UserLogin/logout/" + storedToken);
				Request requestLogout = new Request.Builder().url(urlLogout)
						.post(RequestBody.create(MEDIA_TYPE_MARKDOWN, "")).build();
				Response responseLogout = okHttpClient.newCall(requestLogout).execute();
				if (responseLogout.isSuccessful()) {
					String logoutResult = responseLogout.body().string();
					return logoutResult;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals("true") || result.equals("timeout")) {
				editor.putString("userToken", "");
				editor.commit();
			}
			dialog.dismiss();
			MainMenuActivity.this.finish();
		}
	}

	public void menuItemClick(View view) {
		Integer viewId = view.getId();
		Intent intent = new Intent(MainMenuActivity.this, MainMenuItemActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("userId", userId);
		bundle.putString("fullName", fullName);
		bundle.putString("storedToken", storedToken);
		switch (viewId) {
		case R.id.open_course:
			bundle.putInt("itemType", 0);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.open_grades:
			bundle.putInt("itemType", 1);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.chat:
			bundle.putInt("itemType", 2);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.open_job:
			bundle.putInt("itemType", 3);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.recommend_friend:
			bundle.putInt("itemType", 4);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.contact_us:
			bundle.putInt("itemType", 5);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.change_pwd:
			bundle.putInt("itemType", 6);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.exit_app:
			logout();
			break;

		default:
			break;
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
