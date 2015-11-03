package com.lb.jbt;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.lb.constants.MobileNetStatus;
import com.lb.request.GetAreaDomainFiltersClient;

public class JobsFileterActivity extends Activity {
	private SharedPreferences spf = null;
	private String storedToken = "";
	private LayoutInflater layoutInflater = null;
	private static final int FILTER_REQUEST_CODE = 2;
	private ImageView right_menu_btn, left_ope_btn;
	private List<String> areasList = null;
	private List<String> domainsList = null;
	private LinearLayout area_items, domain_items;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobs_filter);
		spf = JobsFileterActivity.this.getSharedPreferences("jbt",
				JobsFileterActivity.this.MODE_PRIVATE);
		storedToken = spf.getString("userToken", "");
		areasList = new ArrayList<String>();
		domainsList = new ArrayList<String>();
		right_menu_btn = (ImageView) findViewById(R.id.right_menu_btn);
		left_ope_btn = (ImageView) findViewById(R.id.left_ope_btn);
		area_items = (LinearLayout) findViewById(R.id.area_items);
		domain_items = (LinearLayout) findViewById(R.id.domain_items);
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
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("areasList",
						(ArrayList) areasList);
				bundle.putParcelableArrayList("domainsList",
						(ArrayList) domainsList);
				intent.putExtras(bundle);
				JobsFileterActivity.this.setResult(FILTER_REQUEST_CODE, intent);
				JobsFileterActivity.this.finish();
			}
		});
		if (MobileNetStatus.isNetUsable) {
			Call<String> areaDomainCall = GetAreaDomainFiltersClient
					.getAreaDomainFiltersService()
					.getDomainAndArea(storedToken);
			areaDomainCall.enqueue(new Callback<String>() {
				@Override
				public void onResponse(Response<String> arg0, Retrofit arg1) {
					String result = arg0.body();
					if (result.equals("timeout")) {
						ActivityCompat.finishAffinity(JobsFileterActivity.this);
						Intent intent = new Intent(JobsFileterActivity.this,
								LoginActivity.class);
						startActivity(intent);
						Toast.makeText(
								JobsFileterActivity.this,
								getResources().getString(R.string.ope_time_out),
								Toast.LENGTH_SHORT).show();
					} else if (result.equals("null")) {

					} else {
						try {
							layoutInflater = LayoutInflater
									.from(JobsFileterActivity.this);
							JSONObject areaDomainJson = new JSONObject(result);
							JSONArray areas = JSONArray
									.parseArray((String) areaDomainJson
											.get("Area"));
							JSONArray domains = JSONArray
									.parseArray((String) areaDomainJson
											.get("Domain"));
							// area filter exist
							if (areas.size() > 0) {
								LinearLayout layout = null;
								if (areas.size() >= 2) {
									for (int i = 0; i < 2; i++) {
										layout = (LinearLayout) layoutInflater
												.inflate(
														R.layout.area_domain_check_item,
														null);
										TextView filter_item_title = (TextView) layout
												.findViewById(R.id.filter_item_title);
										ImageView filter_item_img = (ImageView) layout
												.findViewById(R.id.filter_item_img);
										final String areaTitle = areas
												.getJSONObject(i).getString(
														"Title");
										filter_item_title.setText(areaTitle);
										filter_item_img
												.setOnClickListener(new View.OnClickListener() {
													@Override
													public void onClick(View v) {
														Toast.makeText(
																JobsFileterActivity.this,
																areaTitle, 0)
																.show();
													}
												});
										area_items.addView(layout);
									}
								} else {
									// one area filter
									layout = (LinearLayout) layoutInflater
											.inflate(
													R.layout.area_domain_check_item,
													null);
									TextView filter_item_title = (TextView) layout
											.findViewById(R.id.filter_item_title);
									ImageView filter_item_img = (ImageView) layout
											.findViewById(R.id.filter_item_img);
									final String areaTitle = areas
											.getJSONObject(0)
											.getString("Title");
									filter_item_title.setText(areaTitle);
									filter_item_img
											.setOnClickListener(new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													Toast.makeText(
															JobsFileterActivity.this,
															areaTitle, 0)
															.show();
												}
											});
									area_items.addView(layout);
								}
							}
							// domain filter exist
							if (domains.size() > 0) {
								LinearLayout layout = null;
								if (domains.size() >= 2) {
									for (int i = 0; i < 2; i++) {
										layout = (LinearLayout) layoutInflater
												.inflate(
														R.layout.area_domain_check_item,
														null);
										TextView filter_item_title = (TextView) layout
												.findViewById(R.id.filter_item_title);
										ImageView filter_item_img = (ImageView) layout
												.findViewById(R.id.filter_item_img);
										final String domainTitle = domains
												.getJSONObject(i).getString(
														"Title");
										filter_item_title.setText(domainTitle);
										filter_item_img
												.setOnClickListener(new View.OnClickListener() {
													@Override
													public void onClick(View v) {
														Toast.makeText(
																JobsFileterActivity.this,
																domainTitle, 0)
																.show();
													}
												});
										domain_items.addView(layout);
									}
								} else {
									// one area filter
									layout = (LinearLayout) layoutInflater
											.inflate(
													R.layout.area_domain_check_item,
													null);
									TextView filter_item_title = (TextView) layout
											.findViewById(R.id.filter_item_title);
									ImageView filter_item_img = (ImageView) layout
											.findViewById(R.id.filter_item_img);
									final String domainTitle = domains
											.getJSONObject(0)
											.getString("Title");
									filter_item_title.setText(domainTitle);
									filter_item_img
											.setOnClickListener(new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													Toast.makeText(
															JobsFileterActivity.this,
															domainTitle, 0)
															.show();
												}
											});
									domain_items.addView(layout);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				@Override
				public void onFailure(Throwable arg0) {

				}
			});
		} else {
			// network is unavailable
			Toast.makeText(JobsFileterActivity.this,
					getResources().getString(R.string.net_unusable),
					Toast.LENGTH_SHORT).show();
		}
	}
}
