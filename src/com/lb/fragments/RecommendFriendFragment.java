package com.lb.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.lb.constants.MobileNetStatus;
import com.lb.jbt.ContactsActivity;
import com.lb.jbt.R;

public class RecommendFriendFragment extends Fragment implements
		View.OnClickListener {
	private final Integer REQUEST_CONTACTS_CODE = 1;
	private SharedPreferences spf = null;
	private String storedToken, userId;
	private EditText friend_name, friend_phone, friend_email, friend_interest;
	private TextView friend_name_hint, friend_phone_hint, friend_email_hint,
			friend_interest_hint;
	private Button recommend_friend_btn, read_local_contacts,
			read_google_contacts, read_outlook_contacts;
	private LinearLayout choose_contact_type_btn, contact_type_items;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		spf = RecommendFriendFragment.this.getActivity().getSharedPreferences(
				"jbt", RecommendFriendFragment.this.getActivity().MODE_PRIVATE);
		storedToken = spf.getString("userToken", "");
		userId = spf.getString("userId", "");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recommend_friend_fragment,
				container, false);
		friend_name = (EditText) view.findViewById(R.id.friend_name);
		friend_phone = (EditText) view.findViewById(R.id.friend_phone);
		friend_email = (EditText) view.findViewById(R.id.friend_email);
		friend_interest = (EditText) view.findViewById(R.id.friend_interest);
		friend_name_hint = (TextView) view.findViewById(R.id.friend_name_hint);
		friend_phone_hint = (TextView) view
				.findViewById(R.id.friend_phone_hint);
		friend_email_hint = (TextView) view
				.findViewById(R.id.friend_email_hint);
		friend_interest_hint = (TextView) view
				.findViewById(R.id.friend_interest_hint);
		recommend_friend_btn = (Button) view
				.findViewById(R.id.recommend_friend_btn);
		choose_contact_type_btn = (LinearLayout) view
				.findViewById(R.id.choose_contact_type_btn);
		contact_type_items = (LinearLayout) view
				.findViewById(R.id.contact_type_items);
		read_local_contacts = (Button) view
				.findViewById(R.id.read_local_contacts);
		read_google_contacts = (Button) view
				.findViewById(R.id.read_google_contacts);
		read_outlook_contacts = (Button) view
				.findViewById(R.id.read_outlook_contacts);
		choose_contact_type_btn.setOnClickListener(this);
		read_local_contacts.setOnClickListener(this);
		read_google_contacts.setOnClickListener(this);
		read_outlook_contacts.setOnClickListener(this);
		recommend_friend_btn.setOnClickListener(this);
		friend_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				friend_name_hint.setTextColor(getResources().getColor(
						R.color.hint_text));
				if (s.toString().length() > 0) {
					friend_name_hint.setVisibility(View.GONE);
				} else {
					friend_name_hint.setVisibility(View.VISIBLE);
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
		friend_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				friend_phone_hint.setTextColor(getResources().getColor(
						R.color.hint_text));
				if (s.toString().length() > 0) {
					friend_phone_hint.setVisibility(View.GONE);
				} else {
					friend_phone_hint.setVisibility(View.VISIBLE);
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
		friend_email.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				friend_email_hint.setTextColor(getResources().getColor(
						R.color.hint_text));
				if (s.toString().length() > 0) {
					friend_email_hint.setVisibility(View.GONE);
				} else {
					friend_email_hint.setVisibility(View.VISIBLE);
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
		friend_interest.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				friend_interest_hint.setTextColor(getResources().getColor(
						R.color.hint_text));
				if (s.toString().length() > 0) {
					friend_interest_hint.setVisibility(View.GONE);
				} else {
					friend_interest_hint.setVisibility(View.VISIBLE);
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
			intent = new Intent(RecommendFriendFragment.this.getActivity(),
					ContactsActivity.class);
			bundle = new Bundle();
			bundle.putString("contact_type", "local");
			intent.putExtra("pass_bundle", bundle);
			RecommendFriendFragment.this.getActivity().startActivityForResult(
					intent, REQUEST_CONTACTS_CODE);
			contact_type_items.setVisibility(View.GONE);
			break;
		case R.id.recommend_friend_btn:
			int ret = 1;
			friend_name_hint.setTextColor(getResources().getColor(
					R.color.hint_text));
			friend_phone_hint.setHintTextColor(getResources().getColor(
					R.color.hint_text));
			friend_email_hint.setTextColor(getResources().getColor(
					R.color.hint_text));
			friend_interest_hint.setTextColor(getResources().getColor(
					R.color.hint_text));
			if (friend_name.getText().toString().length() <= 0) {
				ret = -1;
				friend_name_hint.setTextColor(getResources().getColor(
						R.color.red_light));
				return;
			} else if (friend_phone.getText().toString().length() <= 0) {
				ret = -1;
				friend_phone_hint.setTextColor(getResources().getColor(
						R.color.red_light));
				return;
			} else if (friend_email.getText().toString().length() <= 0) {
				ret = -1;
				friend_email_hint.setTextColor(getResources().getColor(
						R.color.red_light));
				return;
			} else if (friend_interest.getText().toString().length() <= 0) {
				ret = -1;
				friend_interest_hint.setTextColor(getResources().getColor(
						R.color.red_light));
				return;
			}
			if (ret > 0) {
				if (MobileNetStatus.isNetUsable) {

				} else {
					// network is unavailable
					Toast.makeText(RecommendFriendFragment.this.getActivity(),
							getResources().getString(R.string.net_unusable),
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		default:
			break;
		}
	}
}
