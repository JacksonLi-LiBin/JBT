package com.lb.fragments;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lb.constants.MobileNetStatus;
import com.lb.entities.Job;
import com.lb.jbt.JobsFileterActivity;
import com.lb.jbt.LoginActivity;
import com.lb.jbt.MainMenuItemActivity;
import com.lb.jbt.MainMenuItemActivity.FilterJobsCallBack;
import com.lb.jbt.R;
import com.lb.request.GetJobsClient;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

public class JobsFragment extends Fragment {
	private static final int FILTER_REQUEST_CODE = 2;
	private SharedPreferences spf = null;
	private String storedToken = "";
	private boolean withFilter = false;
	private List<Job> originalList = null;
	private List<Job> filterList = null;
	private BaseAdapter adapter = null;
	private DynamicListView dynamicListview = null;
	private AnimationAdapter animationAdapter = null;
	private ImageView left_ope_btn = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		spf = JobsFragment.this.getActivity().getSharedPreferences("jbt",
				JobsFragment.this.getActivity().MODE_PRIVATE);
		storedToken = spf.getString("userToken", "");
		originalList = new ArrayList<Job>();
		filterList = new ArrayList<Job>();
		left_ope_btn = (ImageView) JobsFragment.this.getActivity()
				.findViewById(R.id.left_ope_btn);
		left_ope_btn.setImageBitmap(BitmapFactory.decodeResource(
				JobsFragment.this.getActivity().getResources(),
				R.drawable.filter));
		left_ope_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JobsFragment.this.getActivity(),
						JobsFileterActivity.class);
				startActivityForResult(intent, FILTER_REQUEST_CODE);
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.jobs_fragment, container, false);
		dynamicListview = (DynamicListView) view
				.findViewById(R.id.dynamiclistview_listview);
		adapter = new MyJobsAdapter(JobsFragment.this.getActivity(), filterList);
		animationAdapter = new SwingBottomInAnimationAdapter(adapter);
		animationAdapter.setAbsListView(dynamicListview);
		dynamicListview.setAdapter(animationAdapter);
		if (MobileNetStatus.isNetUsable) {
			Call<String> getJobsCall = GetJobsClient.getGetJobsService()
					.getJobs(storedToken);
			getJobsCall.enqueue(new Callback<String>() {

				@Override
				public void onResponse(Response<String> arg0, Retrofit arg1) {
					String result = arg0.body();
					if (result.equals("timeout")) {
						ActivityCompat.finishAffinity(JobsFragment.this
								.getActivity());
						Intent intent = new Intent(JobsFragment.this
								.getActivity(), LoginActivity.class);
						startActivity(intent);
						Toast.makeText(
								JobsFragment.this.getActivity(),
								getResources().getString(R.string.ope_time_out),
								Toast.LENGTH_SHORT).show();
					} else if (result.equals("null")) {

					} else {
						JSONObject getJobsResult = JSONObject
								.parseObject(result);
						JSONArray jobItems = JSONArray.parseArray(getJobsResult
								.getString("d"));
						Job job = null;
						for (int i = 0; i < jobItems.size(); i++) {
							job = new Job();
							JSONObject jobObject = jobItems.getJSONObject(i);
							job.setJobId(jobObject.getString("ID"));
							job.setTitle(jobObject.getString("Title"));
							originalList.add(job);
						}
						if (!withFilter) {
							filterList = originalList;
						}
						adapter.notifyDataSetChanged();
					}
				}

				@Override
				public void onFailure(Throwable arg0) {

				}
			});
		} else {
			// network is unavailable
			Toast.makeText(JobsFragment.this.getActivity(),
					getResources().getString(R.string.net_unusable),
					Toast.LENGTH_SHORT).show();
		}
		dynamicListview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						System.out.println("---->"
								+ filterList.get(position).getTitle());
					}
				});
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private class MyJobsAdapter extends BaseAdapter {
		private Context context;

		public MyJobsAdapter(Context context, List<Job> jobList) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			return filterList.size();
		}

		@Override
		public Object getItem(int position) {
			return filterList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView != null) {
				view = convertView;
			} else {
				view = LayoutInflater.from(context).inflate(R.layout.jobs_list,
						null);
			}
			TextView jobs_title = (TextView) view.findViewById(R.id.jobs_title);
			jobs_title.setText(filterList.get(position).getTitle());
			return view;
		}
	}

	private void updateJobsList(List<Job> jobsList, FilterJobsCallBack action) {
		action.updateJobs(jobsList);
	}

	public void updateJobs(List<Job> jobsList) {
		updateJobsList(jobsList, new FilterJobsCallBack() {
			@Override
			public void updateJobs(List<Job> filterJobs) {
				filterList = filterJobs;
				adapter.notifyDataSetChanged();
			}
		});
	}
}
