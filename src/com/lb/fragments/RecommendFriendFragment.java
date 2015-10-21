package com.lb.fragments;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lb.constants.MobileNetStatus;
import com.lb.entities.FriendPassStore;
import com.lb.jbt.ContactsActivity;
import com.lb.jbt.LoginActivity;
import com.lb.jbt.R;
import com.lb.requestinterface.RecommendFriendClient;
import com.lb.tools.ReadProperties;
import com.lb.widgets.CustomAlertDialog;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class RecommendFriendFragment extends Fragment implements View.OnClickListener {
	private final Integer REQUEST_CONTACTS_CODE = 1;
	private SharedPreferences spf = null;
	private String storedToken, userId;
	private EditText friend_name, friend_phone, friend_email, friend_interest;
	private TextView friend_name_hint, friend_phone_hint, friend_email_hint, friend_interest_hint;
	private Button recommend_friend_btn, read_local_contacts, read_google_contacts, read_outlook_contacts;
	private LinearLayout choose_contact_type_btn, contact_type_items;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		spf = RecommendFriendFragment.this.getActivity().getSharedPreferences("jbt",
				RecommendFriendFragment.this.getActivity().MODE_PRIVATE);
		storedToken = spf.getString("userToken", "");
		userId = spf.getString("userId", "");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recommend_friend_fragment, container, false);
		friend_name = (EditText) view.findViewById(R.id.friend_name);
		friend_phone = (EditText) view.findViewById(R.id.friend_phone);
		friend_email = (EditText) view.findViewById(R.id.friend_email);
		friend_interest = (EditText) view.findViewById(R.id.friend_interest);
		friend_name_hint = (TextView) view.findViewById(R.id.friend_name_hint);
		friend_phone_hint = (TextView) view.findViewById(R.id.friend_phone_hint);
		friend_email_hint = (TextView) view.findViewById(R.id.friend_email_hint);
		friend_interest_hint = (TextView) view.findViewById(R.id.friend_interest_hint);
		recommend_friend_btn = (Button) view.findViewById(R.id.recommend_friend_btn);
		choose_contact_type_btn = (LinearLayout) view.findViewById(R.id.choose_contact_type_btn);
		contact_type_items = (LinearLayout) view.findViewById(R.id.contact_type_items);
		read_local_contacts = (Button) view.findViewById(R.id.read_local_contacts);
		read_google_contacts = (Button) view.findViewById(R.id.read_google_contacts);
		read_outlook_contacts = (Button) view.findViewById(R.id.read_outlook_contacts);
		choose_contact_type_btn.setOnClickListener(this);
		read_local_contacts.setOnClickListener(this);
		read_google_contacts.setOnClickListener(this);
		read_outlook_contacts.setOnClickListener(this);
		recommend_friend_btn.setOnClickListener(this);
		friend_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				friend_name_hint.setTextColor(getResources().getColor(R.color.hint_text));
				if (s.toString().length() > 0) {
					friend_name_hint.setVisibility(View.GONE);
				} else {
					friend_name_hint.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		friend_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				friend_phone_hint.setTextColor(getResources().getColor(R.color.hint_text));
				if (s.toString().length() > 0) {
					friend_phone_hint.setVisibility(View.GONE);
				} else {
					friend_phone_hint.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		friend_email.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				friend_email_hint.setTextColor(getResources().getColor(R.color.hint_text));
				if (s.toString().length() > 0) {
					friend_email_hint.setVisibility(View.GONE);
				} else {
					friend_email_hint.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		friend_interest.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				friend_interest_hint.setTextColor(getResources().getColor(R.color.hint_text));
				if (s.toString().length() > 0) {
					friend_interest_hint.setVisibility(View.GONE);
				} else {
					friend_interest_hint.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		Bundle bundle = null;
		switch (v.getId()) {
		case R.id.choose_contact_type_btn:
			if (contact_type_items.getVisibility() == View.GONE) {
				contact_type_items.setVisibility(View.VISIBLE);
			} else {
				contact_type_items.setVisibility(View.GONE);
			}
			break;
		case R.id.read_local_contacts:
			intent = new Intent(RecommendFriendFragment.this.getActivity(), ContactsActivity.class);
			bundle = new Bundle();
			bundle.putString("contact_type", "local");
			intent.putExtra("pass_bundle", bundle);
			RecommendFriendFragment.this.getActivity().startActivityForResult(intent, REQUEST_CONTACTS_CODE);
			contact_type_items.setVisibility(View.GONE);
			break;
		case R.id.recommend_friend_btn:
			int ret = 1;
			friend_name_hint.setTextColor(getResources().getColor(R.color.hint_text));
			friend_phone_hint.setHintTextColor(getResources().getColor(R.color.hint_text));
			friend_email_hint.setTextColor(getResources().getColor(R.color.hint_text));
			friend_interest_hint.setTextColor(getResources().getColor(R.color.hint_text));
			if (friend_name.getText().toString().length() <= 0) {
				ret = -1;
				friend_name_hint.setTextColor(getResources().getColor(R.color.red_light));
				return;
			} else if (friend_phone.getText().toString().length() <= 0) {
				ret = -1;
				friend_phone_hint.setTextColor(getResources().getColor(R.color.red_light));
				return;
			} else if (friend_email.getText().toString().length() <= 0) {
				ret = -1;
				friend_email_hint.setTextColor(getResources().getColor(R.color.red_light));
				return;
			} else if (friend_interest.getText().toString().length() <= 0) {
				ret = -1;
				friend_interest_hint.setTextColor(getResources().getColor(R.color.red_light));
				return;
			}
			if (ret > 0) {
				if (MobileNetStatus.isNetUsable) {
					new RecommendAsync().execute();
				} else {
					// network is unavailable
					Toast.makeText(RecommendFriendFragment.this.getActivity(),
							getResources().getString(R.string.net_unusable), Toast.LENGTH_SHORT).show();
				}
			}
			break;
		default:
			break;
		}
	}

	private class RecommendAsync extends AsyncTask<Void, Void, String> {
		private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		private final OkHttpClient okHttpClient = new OkHttpClient();

		@Override
		protected String doInBackground(Void... params) {
			try {
				HttpUrl url = HttpUrl
						.parse(ReadProperties.read("url", "jackson_schedule") + storedToken + "/tokenIsUsable");
				Request req = new Request.Builder().url(url).build();
				Response res = okHttpClient.newCall(req).execute();
				if (res.isSuccessful()) {
					String checkRe = res.body().string();
					if (checkRe.equals("false")) {
						return "{reType:0}";
					}
					// url = HttpUrl
					// .parse(ReadProperties.read("url",
					// "jackson_recommend_remote")
					// + friend_name.getText().toString()
					// + "&lastName="
					// + friend_name.getText().toString()
					// + "&fullPhoneNumber="
					// + friend_phone.getText().toString()
					// + "&email="
					// + friend_email.getText().toString()
					// + "&organization=jbt&dateAndTimeOfLead="
					// + sdf.format(new Date())
					// +
					// "&campaign=448&leadSource=894&studyExtention=1&agreeForAdvertisement=0");
					// req=new Request.Builder().url(url).build();
					// res=okHttpClient.newCall(req).execute();
					// if(res.isSuccessful()){
					// String remoteRe=res.body().string();
					// if(!remoteRe.equals("Parameters")){
					// return "{reType:1}";
					// }
					FriendPassStore friendPassStore = new FriendPassStore();
					friendPassStore.setFirstName(friend_name.getText().toString());
					friendPassStore.setLastName(friend_name.getText().toString());
					friendPassStore.setPhone(friend_phone.getText().toString());
					friendPassStore.setEmail(friend_email.getText().toString());
					friendPassStore.setDataAndTimeOfLead(sdf.format(new Date()));
					friendPassStore.setInterest(friend_interest.getText().toString());
					friendPassStore.setUserId(userId);
					// url = HttpUrl.parse(ReadProperties.read("url",
					// "jackson_recommend_local") + storedToken + "/add");
					// req = new Request.Builder()
					// .url(url)
					// .post(RequestBody.create(JSON,
					// friendPassStore.toString())).build();
					// res = okHttpClient.newCall(req).execute();
					Call<String> recommendCall = RecommendFriendClient.getRecommendFriendClient()
							.recommendFriend(storedToken, friendPassStore);
					recommendCall.enqueue(new Callback<String>() {
						@Override
						public void onFailure(Throwable arg0) {
						}

						@Override
						public void onResponse(retrofit.Response<String> arg0, Retrofit arg1) {
							System.out.println("success-------->" + arg0.body());
						}
					});
					// if (res.isSuccessful()) {
					// String storeRe = res.body().string();
					// if (storeRe.equals("true")) {
					// return "{reType:2}";
					// } else {
					// return "{reType:3}";
					// }
					// }
					// }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				JSONObject jo = JSONObject.parseObject(result);
				Integer reType = Integer.valueOf(jo.getString("reType"));
				CustomAlertDialog.Builder builder = null;
				Dialog dialog = null;
				switch (reType) {
				case 0:
					// user time out
					ActivityCompat.finishAffinity(RecommendFriendFragment.this.getActivity());
					Intent intent = new Intent(RecommendFriendFragment.this.getActivity(), LoginActivity.class);
					startActivity(intent);
					break;
				case 1:
					// remote request failed
					break;
				case 2:
					// local request success
					builder = new CustomAlertDialog.Builder(RecommendFriendFragment.this.getActivity(), true);
					builder.setDialogText(getResources().getString(R.string.recommend_success));
					builder.setPosiClickListener(new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
					dialog = builder.create();
					dialog.setCancelable(false);
					dialog.show();
					break;
				case 3:
					// local request failed
					builder = new CustomAlertDialog.Builder(RecommendFriendFragment.this.getActivity(), false);
					builder.setDialogText(getResources().getString(R.string.recommend_failed));
					dialog = builder.create();
					dialog.setCancelable(false);
					dialog.show();
					break;
				default:
					break;
				}
			}
		}
	}
}
