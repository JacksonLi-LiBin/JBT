package com.lb.jbt;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JobDetailsActivity extends Activity {
	private SharedPreferences spf = null;
	private String storedToken = "";
	private TextView location_value, experience_value;
	private LinearLayout job_description, requirement_items, send_email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_details);
		spf = getSharedPreferences("jbt", MODE_PRIVATE);
		storedToken = spf.getString("userToken", "");
		location_value = (TextView) findViewById(R.id.location_value);
		experience_value = (TextView) findViewById(R.id.experience_value);
		job_description = (LinearLayout) findViewById(R.id.job_description);
		requirement_items = (LinearLayout) findViewById(R.id.requirement_items);
		send_email = (LinearLayout) findViewById(R.id.send_email);
	}
}
