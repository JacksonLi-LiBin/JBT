package com.lb.fragments;

import com.lb.jbt.R;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecommendFriendFragment extends Fragment {
	private SharedPreferences spf = null;
	private String storedToken, userId;
	private EditText friend_name, friend_phone, friend_email, friend_interest;
	private TextView friend_name_hint, friend_phone_hint, friend_email_hint, friend_interest_hint;
	private Button recommend_friend_btn;
	private LinearLayout choose_contact_type_btn;

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
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
