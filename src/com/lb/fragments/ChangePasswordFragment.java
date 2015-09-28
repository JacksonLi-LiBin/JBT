package com.lb.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lb.constants.MobileNetStatus;
import com.lb.jbt.LoginActivity;
import com.lb.jbt.R;
import com.lb.tools.ReadProperties;
import com.lb.widgets.CustomAlertDialog;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class ChangePasswordFragment extends Fragment implements OnTouchListener {
	// private FragmentManager manager;
	// private FragmentTransaction transaction;
	private SharedPreferences spf = null;
	private TextView old_pwd_hint, new_pwd_hint, confirm_pwd_hint;
	private EditText old_pwd, new_pwd, confirm_pwd;
	private Button change_pwd_btn;
	private String storedToken, userId;
	private ScrollView sv_change_password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		spf = ChangePasswordFragment.this.getActivity().getSharedPreferences(
				"jbt", ChangePasswordFragment.this.getActivity().MODE_PRIVATE);
		// manager = getFragmentManager();
		// transaction = manager.beginTransaction();
		// Bundle bundle =
		// manager.findFragmentByTag("changepwd").getArguments();
		userId = spf.getString("userId", "");
		storedToken = spf.getString("userToken", "");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.change_password_fragment,
				container, false);
		sv_change_password = (ScrollView) view
				.findViewById(R.id.sv_change_password);
		sv_change_password.setOnTouchListener(this);
		old_pwd_hint = (TextView) view.findViewById(R.id.old_pwd_hint);
		new_pwd_hint = (TextView) view.findViewById(R.id.new_pwd_hint);
		confirm_pwd_hint = (TextView) view.findViewById(R.id.confirm_pwd_hint);
		old_pwd = (EditText) view.findViewById(R.id.old_pwd);
		new_pwd = (EditText) view.findViewById(R.id.new_pwd);
		confirm_pwd = (EditText) view.findViewById(R.id.confirm_pwd);
		change_pwd_btn = (Button) view.findViewById(R.id.change_pwd_btn);
		old_pwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				old_pwd_hint.setTextColor(getResources().getColor(
						R.color.hint_text));
				if (s.toString().length() == 0) {
					old_pwd_hint.setVisibility(View.VISIBLE);
				} else {
					old_pwd_hint.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		new_pwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				new_pwd_hint.setHintTextColor(getResources().getColor(
						R.color.hint_text));
				if (s.toString().length() == 0) {
					new_pwd_hint.setVisibility(View.VISIBLE);
				} else {
					new_pwd_hint.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		confirm_pwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				confirm_pwd_hint.setTextColor(getResources().getColor(
						R.color.hint_text));
				if (s.toString().length() == 0) {
					confirm_pwd_hint.setVisibility(View.VISIBLE);
				} else {
					confirm_pwd_hint.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		change_pwd_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (MobileNetStatus.isNetUsable) {
					int ret = 1;
					old_pwd_hint.setTextColor(getResources().getColor(
							R.color.hint_text));
					new_pwd_hint.setHintTextColor(getResources().getColor(
							R.color.hint_text));
					confirm_pwd_hint.setTextColor(getResources().getColor(
							R.color.hint_text));
					if (old_pwd.getText().toString().length() <= 0) {
						ret = -1;
						old_pwd_hint.setTextColor(getResources().getColor(
								R.color.red_light));
						return;
					} else if (new_pwd.getText().toString().length() <= 0) {
						ret = -1;
						new_pwd_hint.setTextColor(getResources().getColor(
								R.color.red_light));
						return;
					} else if (confirm_pwd.getText().toString().length() <= 0) {
						ret = -1;
						confirm_pwd_hint.setTextColor(getResources().getColor(
								R.color.red_light));
						return;
					}
					if (new_pwd.getText().toString().trim().length() < 8
							|| new_pwd.getText().toString().trim().length() > 12) {
						ret = -1;
						CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(
								ChangePasswordFragment.this.getActivity(),
								false);
						builder.setDialogText(getResources().getString(
								R.string.pwd_length_noti));
						Dialog dialog = builder.create();
						dialog.setCancelable(false);
						dialog.show();
						return;
					}
					if (!new_pwd.getText().toString().trim()
							.equals(confirm_pwd.getText().toString().trim())) {
						ret = -1;
						CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(
								ChangePasswordFragment.this.getActivity(),
								false);
						builder.setDialogText(getResources().getString(
								R.string.different_new_pwd));
						Dialog dialog = builder.create();
						dialog.setCancelable(false);
						dialog.show();
						return;
					}
					if (ret > 0) {
						new ChangePwdAsync().execute();
					}
				} else {
					// network is unavailable
					Toast.makeText(ChangePasswordFragment.this.getActivity(),
							getResources().getString(R.string.net_unusable),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private class ChangePwdAsync extends AsyncTask<Void, Void, String> {
		private final OkHttpClient okHttpClient = new OkHttpClient();
		private CustomAlertDialog.Builder builder = null;
		private Dialog dialog = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				HttpUrl url = HttpUrl.parse(ReadProperties.read("url",
						"jackson_ad_login_cp")
						+ "User/"
						+ storedToken
						+ "/changePassword?userId="
						+ userId
						+ "&oldPwd="
						+ old_pwd.getText().toString().trim()
						+ "&newPwd="
						+ new_pwd.getText().toString().trim());
				Request request = new Request.Builder().url(url).build();
				Response response = okHttpClient.newCall(request).execute();
				if (response.isSuccessful()) {
					String resResult = response.body().string();
					return resResult;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals("true")) {
				builder = new CustomAlertDialog.Builder(
						ChangePasswordFragment.this.getActivity(), true);
				builder.setDialogText(getResources().getString(
						R.string.change_success_text));
				builder.setPosiClickListener(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						ChangePasswordFragment.this.getActivity().finish();
					}
				});
				dialog = builder.create();
				dialog.setCancelable(false);
				dialog.show();
			} else if (result.equals("false")) {
				builder = new CustomAlertDialog.Builder(
						ChangePasswordFragment.this.getActivity(), false);
				builder.setDialogText(getResources().getString(
						R.string.change_failed_text));
				dialog = builder.create();
				dialog.setCancelable(false);
				dialog.show();
			} else if (result.equals("timeout")) {
				ActivityCompat.finishAffinity(ChangePasswordFragment.this
						.getActivity());
				Intent intent = new Intent(
						ChangePasswordFragment.this.getActivity(),
						LoginActivity.class);
				startActivity(intent);
				// ChangePasswordFragment.this.getActivity().finish();
				// ((MainMenuActivity)
				// ChangePasswordFragment.this.getActivity()).finish();
				Toast.makeText(ChangePasswordFragment.this.getActivity(),
						getResources().getString(R.string.ope_time_out),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) ChangePasswordFragment.this
				.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromInputMethod(ChangePasswordFragment.this
				.getActivity().getCurrentFocus().getWindowToken(), 0);
		return false;
	}
}
