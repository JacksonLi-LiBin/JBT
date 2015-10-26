package com.lb.fragments;

import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lb.constants.MobileNetStatus;
import com.lb.jbt.LoginActivity;
import com.lb.jbt.R;
import com.lb.request.GetMyGradesClient;

public class MyGradesFragment extends Fragment {
	private SharedPreferences spf = null;
	private String storedToken = "";
	private String storedUserId = "";
	private String storedCourseNum = "";
	private String storedCycleNum = "";
	private LinearLayout subjects_items;
	private TextView average_grades_value;
	private LayoutInflater layoutInflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		spf = MyGradesFragment.this.getActivity().getSharedPreferences("jbt",
				MyGradesFragment.this.getActivity().MODE_PRIVATE);
		storedToken = spf.getString("userToken", "");
		storedUserId = spf.getString("userId", "");
		storedCourseNum = spf.getString("coursenum", "");
		storedCycleNum = spf.getString("cyclenum", "");
		layoutInflater = LayoutInflater.from(MyGradesFragment.this
				.getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_grades_fragment, container,
				false);
		subjects_items = (LinearLayout) view.findViewById(R.id.subjects_items);
		average_grades_value = (TextView) view
				.findViewById(R.id.average_grades_value);
		if (MobileNetStatus.isNetUsable) {
			Call<String> getGradesCall = GetMyGradesClient
					.getGetMyGradesService().getMyGrades(storedToken,
							storedUserId, storedCourseNum, storedCycleNum);
			getGradesCall.enqueue(new Callback<String>() {
				@Override
				public void onResponse(Response<String> arg0, Retrofit arg1) {
					String result = arg0.body();
					if (result.equals("timeout")) {
						ActivityCompat.finishAffinity(MyGradesFragment.this
								.getActivity());
						Intent intent = new Intent(MyGradesFragment.this
								.getActivity(), LoginActivity.class);
						startActivity(intent);
						Toast.makeText(
								MyGradesFragment.this.getActivity(),
								getResources().getString(R.string.ope_time_out),
								Toast.LENGTH_SHORT).show();
					} else if (result.equals("null")) {

					} else {
						JSONObject getGradesResult = JSONObject
								.parseObject(result);
						JSONArray gradeItems = JSONArray
								.parseArray(getGradesResult.getString("d"));
						System.out.println("-->" + gradeItems);
						for (int i = 0; i < gradeItems.size(); i++) {
							JSONObject jsonObject = gradeItems.getJSONObject(i);
							LinearLayout layout = (LinearLayout) layoutInflater
									.inflate(R.layout.grade_item_layout, null)
									.findViewById(R.id.grade_items_parent);
							TextView sub_subject_title = (TextView) layout
									.findViewById(R.id.sub_subject_title);
							TextView subject_score = (TextView) layout
									.findViewById(R.id.subject_score);
							sub_subject_title.setText(jsonObject
									.getString("Subject"));
							subject_score.setText(jsonObject
									.getString("Points"));
							subjects_items.addView(layout);
						}
					}
				}

				@Override
				public void onFailure(Throwable arg0) {

				}
			});

		} else {
			// network is unavailable
			Toast.makeText(MyGradesFragment.this.getActivity(),
					getResources().getString(R.string.net_unusable),
					Toast.LENGTH_SHORT).show();
		}
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
