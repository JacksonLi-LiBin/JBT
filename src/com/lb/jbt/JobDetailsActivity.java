package com.lb.jbt;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.lb.request.GetJobDetailsClient;

public class JobDetailsActivity extends Activity implements
		View.OnClickListener {
	private SharedPreferences spf = null;
	private String storedToken = "";
	private String jobId = "";
	private TextView job_title, location_value, experience_value,
			job_description, send_email;
	private LinearLayout requirement_items;
	private ImageView left_ope_btn, right_menu_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_details);
		spf = getSharedPreferences("jbt", MODE_PRIVATE);
		storedToken = spf.getString("userToken", "");
		Intent intent = getIntent();
		jobId = intent.getStringExtra("jobId");
		job_title = (TextView) findViewById(R.id.job_title);
		location_value = (TextView) findViewById(R.id.location_value);
		experience_value = (TextView) findViewById(R.id.experience_value);
		job_description = (TextView) findViewById(R.id.job_description);
		requirement_items = (LinearLayout) findViewById(R.id.requirement_items);
		send_email = (TextView) findViewById(R.id.send_email);
		left_ope_btn = (ImageView) findViewById(R.id.left_ope_btn);
		right_menu_btn = (ImageView) findViewById(R.id.right_menu_btn);
		left_ope_btn.setOnClickListener(this);
		right_menu_btn.setOnClickListener(this);
		Call<String> getJobDetailsCall = GetJobDetailsClient
				.getGetJobDetailsService().getJobDetails(storedToken, jobId);
		getJobDetailsCall.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Response<String> arg0, Retrofit arg1) {
				String result = arg0.body();
				JSONObject jobDetailsJson = JSONObject.parseObject(result);
				JSONObject jobDetails = JSONObject.parseObject(jobDetailsJson
						.getString("d"));
				job_title.setText(jobDetails.getString("Title"));
				location_value.setText(jobDetails.getString("Area"));
				experience_value.setText(jobDetails.getString("Experience"));
				job_description.setText(jobDetails.getString("Text"));
				send_email.setText(jobDetails.getString("Email"));
				String[] requirements = jobDetails.getString("Requirements")
						.split("<br>");
				RelativeLayout requirementParent = null;
				TextView requirementView = null;
				for (String requirement : requirements) {
					requirementParent = new RelativeLayout(
							JobDetailsActivity.this);
					LayoutParams layoutParams = new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT);
					requirementView = new TextView(JobDetailsActivity.this);
					requirementView.setText(requirement + " -");
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					requirementParent.addView(requirementView, params);
					requirement_items.addView(requirementParent, layoutParams);
				}
			}

			@Override
			public void onFailure(Throwable arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_ope_btn:
			JobDetailsActivity.this.finish();
			break;
		case R.id.right_menu_btn:
			ActivityCompat.finishAffinity(JobDetailsActivity.this);
			Intent intent = new Intent(JobDetailsActivity.this,
					MainMenuActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
