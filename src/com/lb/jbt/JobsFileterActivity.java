package com.lb.jbt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;

public class JobsFileterActivity extends Activity {
	private static final int FILTER_REQUEST_CODE = 2;
	private ImageView right_menu_btn, left_ope_btn;
	private List<String> areasList = null;
	private List<String> domainsList = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobs_filter);
		areasList = new ArrayList<String>();
		domainsList = new ArrayList<String>();
		right_menu_btn = (ImageView) findViewById(R.id.right_menu_btn);
		left_ope_btn = (ImageView) findViewById(R.id.left_ope_btn);
		right_menu_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityCompat.finishAffinity(JobsFileterActivity.this);
				Intent intent = new Intent(JobsFileterActivity.this,
						MainMenuActivity.class);
				startActivity(intent);
			}
		});
		left_ope_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent = new Intent();
				// Bundle bundle = new Bundle();
				// bundle.putSerializable("areasList", (Serializable)
				// areasList);
				// bundle.putSerializable("domainsList",
				// (Serializable) domainsList);
				// intent.putExtras(bundle);
				JobsFileterActivity.this.setResult(FILTER_REQUEST_CODE, null);
				JobsFileterActivity.this.finish();
			}
		});
	}

}
