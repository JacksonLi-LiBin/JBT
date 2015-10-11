package com.lb.fragments;

import com.lb.jbt.ContactsActivity;
import com.lb.jbt.R;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
			startActivityForResult(intent, REQUEST_CONTACTS_CODE);
			contact_type_items.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
}
