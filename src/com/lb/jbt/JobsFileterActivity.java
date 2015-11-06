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
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
	private LinearLayout area_items, domain_items, area_more, domain_more;
	private List<ImageView> areasFilterImg, domainsFilterImg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobs_filter);
		spf = JobsFileterActivity.this.getSharedPreferences("jbt",
				JobsFileterActivity.this.MODE_PRIVATE);
		storedToken = spf.getString("userToken", "");
		areasList = new ArrayList<String>();
		domainsList = new ArrayList<String>();
		areasFilterImg = new ArrayList<ImageView>();
		domainsFilterImg = new ArrayList<ImageView>();
		right_menu_btn = (ImageView) findViewById(R.id.right_menu_btn);
		left_ope_btn = (ImageView) findViewById(R.id.left_ope_btn);
		area_items = (LinearLayout) findViewById(R.id.area_items);
		domain_items = (LinearLayout) findViewById(R.id.domain_items);
		area_more = (LinearLayout) findViewById(R.id.area_more);
		domain_more = (LinearLayout) findViewById(R.id.domain_more);
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
								showFilters(areasFilterImg, area_items,
										area_more, areas, "area");
							}
							// domain filter exist
							if (domains.size() > 0) {
								showFilters(domainsFilterImg, domain_items,
										domain_more, domains, "domain");
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

	public void showFilters(final List<ImageView> filterImages,
			final LinearLayout parentLayout, LinearLayout more_layout,
			final JSONArray jsonArray, final String filterType) {
		LinearLayout layout = (LinearLayout) layoutInflater.inflate(
				R.layout.area_domain_check_item, null);
		TextView filter_item_title = (TextView) layout
				.findViewById(R.id.filter_item_title);
		ImageView filter_item_img = (ImageView) layout
				.findViewById(R.id.filter_item_img);
		filterImages.add(filter_item_img);
		filter_item_title.setText(JobsFileterActivity.this.getResources()
				.getString(R.string.check_all));
		filter_item_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView iv = (ImageView) v;
				String imageDes = (String) iv.getContentDescription();
				if (imageDes.equals("checked")) {
					for (ImageView imageView : filterImages) {
						imageView
								.setContentDescription(JobsFileterActivity.this
										.getResources().getString(
												R.string.filter_uncheck_des));
						imageView.setImageBitmap(BitmapFactory.decodeResource(
								JobsFileterActivity.this.getResources(),
								R.drawable.filter_uncheck));
						if (filterType.equals("area")) {
							areasList.clear();
						} else {
							domainsList.clear();
						}
					}
				} else {
					for (ImageView imageView : filterImages) {
						imageView
								.setContentDescription(JobsFileterActivity.this
										.getResources().getString(
												R.string.filter_checked_des));
						imageView.setImageBitmap(BitmapFactory.decodeResource(
								JobsFileterActivity.this.getResources(),
								R.drawable.filter_check));
					}
					if (filterType.equals("area")) {
						areasList.clear();
						for (int i = 0; i < jsonArray.size(); i++) {
							String filterTitle = jsonArray.getJSONObject(i)
									.getString("Title");
							areasList.add(filterTitle);
						}
					} else {
						domainsList.clear();
						for (int i = 0; i < jsonArray.size(); i++) {
							String filterTitle = jsonArray.getJSONObject(i)
									.getString("Title");
							domainsList.add(filterTitle);
						}
					}

				}
			}
		});
		parentLayout.addView(layout);
		for (int i = 0; i < jsonArray.size(); i++) {
			layout = (LinearLayout) layoutInflater.inflate(
					R.layout.area_domain_check_item, null);
			if (i == 1) {
				LinearLayout linearLayout = (LinearLayout) layout.getChildAt(0);
				linearLayout.setBackgroundColor(JobsFileterActivity.this
						.getResources().getColor(R.color.white_bg));
			}
			filter_item_title = (TextView) layout
					.findViewById(R.id.filter_item_title);
			filter_item_img = (ImageView) layout
					.findViewById(R.id.filter_item_img);
			filterImages.add(filter_item_img);
			final String filterTitle = jsonArray.getJSONObject(i).getString(
					"Title");
			filter_item_title.setText(filterTitle);
			filter_item_img.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ImageView iv = (ImageView) v;
					String imageDes = (String) iv.getContentDescription();
					if (imageDes.equals("checked")) {
						iv.setImageBitmap(BitmapFactory.decodeResource(
								JobsFileterActivity.this.getResources(),
								R.drawable.filter_uncheck));
						iv.setContentDescription(JobsFileterActivity.this
								.getResources().getString(
										R.string.filter_uncheck_des));
						if (filterImages.get(0).getContentDescription()
								.equals("checked")) {
							filterImages
									.get(0)
									.setContentDescription(
											JobsFileterActivity.this
													.getResources()
													.getString(
															R.string.filter_uncheck_des));
							filterImages.get(0).setImageBitmap(
									BitmapFactory.decodeResource(
											JobsFileterActivity.this
													.getResources(),
											R.drawable.filter_uncheck));
						}
						if (filterType.equals("area")) {
							areasList.remove(filterTitle);
						} else {
							domainsList.remove(filterTitle);
						}
					} else {
						iv.setImageBitmap(BitmapFactory.decodeResource(
								JobsFileterActivity.this.getResources(),
								R.drawable.filter_check));
						iv.setContentDescription(JobsFileterActivity.this
								.getResources().getString(
										R.string.filter_checked_des));
						if (filterType.equals("area")) {
							areasList.add(filterTitle);
						} else {
							domainsList.add(filterTitle);
						}
					}
				}
			});
			parentLayout.addView(layout);
			if (i >= 2) {
				layout.setVisibility(View.GONE);
			}
		}
		LinearLayout more_btn_layout = (LinearLayout) layoutInflater.inflate(
				R.layout.area_domain_more_btn, null);
		final LinearLayout slidedown_more_items_btn = (LinearLayout) more_btn_layout
				.findViewById(R.id.slidedown_more_items_btn);
		final RelativeLayout slideup_more_items_btn = (RelativeLayout) more_btn_layout
				.findViewById(R.id.slideup_more_items_btn);
		slidedown_more_items_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// show all items
				for (int i = 0; i < parentLayout.getChildCount(); i++) {
					LinearLayout childLayout = (LinearLayout) parentLayout
							.getChildAt(i);
					childLayout.getChildAt(0).setBackgroundDrawable(
							JobsFileterActivity.this.getResources()
									.getDrawable(
											R.drawable.contacts_items_border));
					if (i == (parentLayout.getChildCount() - 1)) {
						childLayout.getChildAt(0).setBackgroundColor(
								JobsFileterActivity.this.getResources()
										.getColor(R.color.white_bg));
					}
					childLayout.setVisibility(View.VISIBLE);
				}
				slideup_more_items_btn.setVisibility(View.VISIBLE);
				((LinearLayout) v).setVisibility(View.GONE);
			}
		});
		slideup_more_items_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// collapse items
				for (int i = 0; i < parentLayout.getChildCount(); i++) {
					LinearLayout childLayout = (LinearLayout) parentLayout
							.getChildAt(i);
					if (i == 2) {
						childLayout.getChildAt(0).setBackgroundColor(
								JobsFileterActivity.this.getResources()
										.getColor(R.color.white_bg));
					}
					if (i < 3) {
						childLayout.setVisibility(View.VISIBLE);
					} else {
						childLayout.setVisibility(View.GONE);
					}
				}
				slidedown_more_items_btn.setVisibility(View.VISIBLE);
				((RelativeLayout) v).setVisibility(View.GONE);
			}
		});
		more_layout.addView(more_btn_layout);
	}
}
