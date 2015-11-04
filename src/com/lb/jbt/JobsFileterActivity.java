package com.lb.jbt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
								showFilters(
										areas.size() >= 2 ? 2 : areas.size(),
										areasFilterImg, area_items, areas);
								// LinearLayout layout = (LinearLayout)
								// layoutInflater
								// .inflate(
								// R.layout.area_domain_check_item,
								// null);
								// TextView filter_item_title = (TextView)
								// layout
								// .findViewById(R.id.filter_item_title);
								// ImageView filter_item_img = (ImageView)
								// layout
								// .findViewById(R.id.filter_item_img);
								// areasFilterImg.add(filter_item_img);
								// filter_item_title
								// .setText(JobsFileterActivity.this
								// .getResources().getString(
								// R.string.check_all));
								// filter_item_img
								// .setOnClickListener(new
								// View.OnClickListener() {
								// @Override
								// public void onClick(View v) {
								// ImageView iv = (ImageView) v;
								// String imageDes = (String) iv
								// .getContentDescription();
								// if (imageDes.equals("checked")) {
								// for (ImageView imageView : areasFilterImg) {
								// imageView
								// .setContentDescription(JobsFileterActivity.this
								// .getResources()
								// .getString(
								// R.string.filter_uncheck_des));
								// imageView
								// .setImageBitmap(BitmapFactory
								// .decodeResource(
								// JobsFileterActivity.this
								// .getResources(),
								// R.drawable.filter_uncheck));
								// }
								// } else {
								// for (ImageView imageView : areasFilterImg) {
								// imageView
								// .setContentDescription(JobsFileterActivity.this
								// .getResources()
								// .getString(
								// R.string.filter_checked_des));
								// imageView
								// .setImageBitmap(BitmapFactory
								// .decodeResource(
								// JobsFileterActivity.this
								// .getResources(),
								// R.drawable.filter_check));
								// }
								// }
								// }
								// });
								// area_items.addView(layout);
								// if (areas.size() >= 2) {
								// for (int i = 0; i < 2; i++) {
								// layout = (LinearLayout) layoutInflater
								// .inflate(
								// R.layout.area_domain_check_item,
								// null);
								// filter_item_title = (TextView) layout
								// .findViewById(R.id.filter_item_title);
								// filter_item_img = (ImageView) layout
								// .findViewById(R.id.filter_item_img);
								// areasFilterImg.add(filter_item_img);
								// final String areaTitle = areas
								// .getJSONObject(i).getString(
								// "Title");
								// filter_item_title.setText(areaTitle);
								// filter_item_img
								// .setOnClickListener(new
								// View.OnClickListener() {
								// @Override
								// public void onClick(View v) {
								// ImageView iv = (ImageView) v;
								// String imageDes = (String) iv
								// .getContentDescription();
								// if (imageDes
								// .equals("checked")) {
								// iv.setImageBitmap(BitmapFactory
								// .decodeResource(
								// JobsFileterActivity.this
								// .getResources(),
								// R.drawable.filter_uncheck));
								// iv.setContentDescription(JobsFileterActivity.this
								// .getResources()
								// .getString(
								// R.string.filter_uncheck_des));
								// } else {
								// iv.setImageBitmap(BitmapFactory
								// .decodeResource(
								// JobsFileterActivity.this
								// .getResources(),
								// R.drawable.filter_check));
								// iv.setContentDescription(JobsFileterActivity.this
								// .getResources()
								// .getString(
								// R.string.filter_checked_des));
								// }
								// }
								// });
								// area_items.addView(layout);
								// }
								// } else {
								// // one area filter
								// layout = (LinearLayout) layoutInflater
								// .inflate(
								// R.layout.area_domain_check_item,
								// null);
								// filter_item_title = (TextView) layout
								// .findViewById(R.id.filter_item_title);
								// filter_item_img = (ImageView) layout
								// .findViewById(R.id.filter_item_img);
								// areasFilterImg.add(filter_item_img);
								// final String areaTitle = areas
								// .getJSONObject(0)
								// .getString("Title");
								// filter_item_title.setText(areaTitle);
								// filter_item_img
								// .setOnClickListener(new
								// View.OnClickListener() {
								// @Override
								// public void onClick(View v) {
								// ImageView iv = (ImageView) v;
								// String imageDes = (String) iv
								// .getContentDescription();
								// if (imageDes
								// .equals("checked")) {
								// iv.setImageBitmap(BitmapFactory
								// .decodeResource(
								// JobsFileterActivity.this
								// .getResources(),
								// R.drawable.filter_uncheck));
								// iv.setContentDescription(JobsFileterActivity.this
								// .getResources()
								// .getString(
								// R.string.filter_uncheck_des));
								// } else {
								// iv.setImageBitmap(BitmapFactory
								// .decodeResource(
								// JobsFileterActivity.this
								// .getResources(),
								// R.drawable.filter_check));
								// iv.setContentDescription(JobsFileterActivity.this
								// .getResources()
								// .getString(
								// R.string.filter_checked_des));
								// }
								// }
								// });
								// area_items.addView(layout);
								// }
							}
							// domain filter exist
							if (domains.size() > 0) {
								showFilters(
										domains.size() >= 2 ? 2 : domains
												.size(), domainsFilterImg,
										domain_items, domains);
								// LinearLayout layout = (LinearLayout)
								// layoutInflater
								// .inflate(
								// R.layout.area_domain_check_item,
								// null);
								// TextView filter_item_title = (TextView)
								// layout
								// .findViewById(R.id.filter_item_title);
								// ImageView filter_item_img = (ImageView)
								// layout
								// .findViewById(R.id.filter_item_img);
								// domainsFilterImg.add(filter_item_img);
								// filter_item_title
								// .setText(JobsFileterActivity.this
								// .getResources().getString(
								// R.string.check_all));
								// filter_item_img
								// .setOnClickListener(new
								// View.OnClickListener() {
								// @Override
								// public void onClick(View v) {
								// ImageView iv = (ImageView) v;
								// String imageDes = (String) iv
								// .getContentDescription();
								// if (imageDes.equals("checked")) {
								// for (ImageView imageView : domainsFilterImg)
								// {
								// imageView
								// .setContentDescription(JobsFileterActivity.this
								// .getResources()
								// .getString(
								// R.string.filter_uncheck_des));
								// imageView
								// .setImageBitmap(BitmapFactory
								// .decodeResource(
								// JobsFileterActivity.this
								// .getResources(),
								// R.drawable.filter_uncheck));
								// }
								// } else {
								// for (ImageView imageView : domainsFilterImg)
								// {
								// imageView
								// .setContentDescription(JobsFileterActivity.this
								// .getResources()
								// .getString(
								// R.string.filter_checked_des));
								// imageView
								// .setImageBitmap(BitmapFactory
								// .decodeResource(
								// JobsFileterActivity.this
								// .getResources(),
								// R.drawable.filter_check));
								// }
								// }
								// }
								// });
								// domain_items.addView(layout);
								// if (domains.size() >= 2) {
								// for (int i = 0; i < 2; i++) {
								// layout = (LinearLayout) layoutInflater
								// .inflate(
								// R.layout.area_domain_check_item,
								// null);
								// filter_item_title = (TextView) layout
								// .findViewById(R.id.filter_item_title);
								// filter_item_img = (ImageView) layout
								// .findViewById(R.id.filter_item_img);
								// domainsFilterImg.add(filter_item_img);
								// final String domainTitle = domains
								// .getJSONObject(i).getString(
								// "Title");
								// filter_item_title.setText(domainTitle);
								// filter_item_img
								// .setOnClickListener(new
								// View.OnClickListener() {
								// @Override
								// public void onClick(View v) {
								// ImageView iv = (ImageView) v;
								// String imageDes = (String) iv
								// .getContentDescription();
								// if (imageDes
								// .equals("checked")) {
								// iv.setImageBitmap(BitmapFactory
								// .decodeResource(
								// JobsFileterActivity.this
								// .getResources(),
								// R.drawable.filter_uncheck));
								// iv.setContentDescription(JobsFileterActivity.this
								// .getResources()
								// .getString(
								// R.string.filter_uncheck_des));
								// } else {
								// iv.setImageBitmap(BitmapFactory
								// .decodeResource(
								// JobsFileterActivity.this
								// .getResources(),
								// R.drawable.filter_check));
								// iv.setContentDescription(JobsFileterActivity.this
								// .getResources()
								// .getString(
								// R.string.filter_checked_des));
								// }
								// }
								// });
								// domain_items.addView(layout);
								// }
								// } else {
								// // one area filter
								// layout = (LinearLayout) layoutInflater
								// .inflate(
								// R.layout.area_domain_check_item,
								// null);
								// filter_item_title = (TextView) layout
								// .findViewById(R.id.filter_item_title);
								// filter_item_img = (ImageView) layout
								// .findViewById(R.id.filter_item_img);
								// domainsFilterImg.add(filter_item_img);
								// final String domainTitle = domains
								// .getJSONObject(0)
								// .getString("Title");
								// filter_item_title.setText(domainTitle);
								// filter_item_img
								// .setOnClickListener(new
								// View.OnClickListener() {
								// @Override
								// public void onClick(View v) {
								// ImageView iv = (ImageView) v;
								// String imageDes = (String) iv
								// .getContentDescription();
								// if (imageDes
								// .equals("checked")) {
								// iv.setImageBitmap(BitmapFactory
								// .decodeResource(
								// JobsFileterActivity.this
								// .getResources(),
								// R.drawable.filter_uncheck));
								// iv.setContentDescription(JobsFileterActivity.this
								// .getResources()
								// .getString(
								// R.string.filter_uncheck_des));
								// } else {
								// iv.setImageBitmap(BitmapFactory
								// .decodeResource(
								// JobsFileterActivity.this
								// .getResources(),
								// R.drawable.filter_check));
								// iv.setContentDescription(JobsFileterActivity.this
								// .getResources()
								// .getString(
								// R.string.filter_checked_des));
								// }
								// }
								// });
								// domain_items.addView(layout);
								// }
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

	public void showFilters(int showItems, final List<ImageView> filterImages,
			LinearLayout parentLayout, JSONArray jsonArray) {
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
				}
			}
		});
		parentLayout.addView(layout);
		for (int i = 0; i < showItems; i++) {
			layout = (LinearLayout) layoutInflater.inflate(
					R.layout.area_domain_check_item, null);
			if (i == (showItems - 1)) {
				LinearLayout linearLayout = (LinearLayout) layout.getChildAt(0);
				linearLayout.setBackgroundColor(JobsFileterActivity.this
						.getResources().getColor(R.color.white_bg));
			}
			filter_item_title = (TextView) layout
					.findViewById(R.id.filter_item_title);
			filter_item_img = (ImageView) layout
					.findViewById(R.id.filter_item_img);
			filterImages.add(filter_item_img);
			final String areaTitle = jsonArray.getJSONObject(i).getString(
					"Title");
			filter_item_title.setText(areaTitle);
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
					} else {
						iv.setImageBitmap(BitmapFactory.decodeResource(
								JobsFileterActivity.this.getResources(),
								R.drawable.filter_check));
						iv.setContentDescription(JobsFileterActivity.this
								.getResources().getString(
										R.string.filter_checked_des));
					}
				}
			});
			parentLayout.addView(layout);
		}
	}
}
