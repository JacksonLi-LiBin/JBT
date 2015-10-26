package com.lb.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.lb.constants.MobileNetStatus;
import com.lb.entities.CourseSyllabus;
import com.lb.jbt.LoginActivity;
import com.lb.jbt.R;
import com.lb.tools.ReadProperties;
import com.lb.widgets.CustomAlertDialog;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tjerkw.slideexpandable.library.AbstractSlideExpandableListAdapter.SetExpandItemCallBack;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CourseSylibusFragment extends Fragment {
	private SharedPreferences spf = null;
	private List<CourseSyllabus> courseSyllabusList = null;
	private ActionSlideExpandableListView course_sylibus_list;
	private CourseSylibusExpandAdapter adapter = null;
	private String userToken, userId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		spf = CourseSylibusFragment.this.getActivity().getSharedPreferences("jbt",
				CourseSylibusFragment.this.getActivity().MODE_PRIVATE);
		userToken = spf.getString("userToken", "");
		userId = spf.getString("userId", "");
		courseSyllabusList = new ArrayList<CourseSyllabus>();
		adapter = new CourseSylibusExpandAdapter(courseSyllabusList);
		if (MobileNetStatus.isNetUsable) {
			new CourseSylibusAsync().execute();
		} else {
			// network is unavailable
			Toast.makeText(CourseSylibusFragment.this.getActivity(), getResources().getString(R.string.net_unusable),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.course_sylibus_fragment, container, false);
		course_sylibus_list = (ActionSlideExpandableListView) view.findViewById(R.id.course_sylibus_list);
		course_sylibus_list.setAdapter(adapter);
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void setExpandItem(int itemNum, SetExpandItemCallBack callBack) {
		callBack.setExpandItem(itemNum);
	}

	public void setItemNum(int itemNum) {
		setExpandItem(itemNum, new SetExpandItemCallBack() {
			@Override
			public void setExpandItem(int itemNum) {
				MobileNetStatus.expandItemNum = itemNum;
				if (adapter == null) {
					adapter = new CourseSylibusExpandAdapter(courseSyllabusList);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	private class CourseSylibusAsync extends AsyncTask<Void, Void, String> {
		private final OkHttpClient okHttpClient = new OkHttpClient();

		@Override
		protected String doInBackground(Void... params) {
			try {
				HttpUrl courseSylibusUrl = HttpUrl.parse(ReadProperties.read("url", "jackson_schedule") + userToken
						+ "/getCourseSyllabusByUser?userId=" + userId);
				Request courseSylibusReq = new Request.Builder().url(courseSylibusUrl).build();
				Response courseSylibusRes = okHttpClient.newCall(courseSylibusReq).execute();
				if (courseSylibusRes.isSuccessful()) {
					String courseSylibusStr = courseSylibusRes.body().string();
					return courseSylibusStr;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ("timeout".equals(result)) {
				ActivityCompat.finishAffinity(CourseSylibusFragment.this.getActivity());
				Intent intent = new Intent(CourseSylibusFragment.this.getActivity(), LoginActivity.class);
				startActivity(intent);
				Toast.makeText(CourseSylibusFragment.this.getActivity(),
						getResources().getString(R.string.ope_time_out), Toast.LENGTH_SHORT).show();
			} else if ("null".equals(result)) {
				CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(
						CourseSylibusFragment.this.getActivity(), false);
				builder.setDialogText(getResources().getString(R.string.cs_no_training_schedule));
				Dialog dialog = builder.create();
				dialog.setCancelable(false);
				dialog.show();
			} else {
				try {
					com.alibaba.fastjson.JSONObject courseSylibusJson = com.alibaba.fastjson.JSONObject
							.parseObject(result);
					com.alibaba.fastjson.JSONArray courseSylibusArray = com.alibaba.fastjson.JSONArray
							.parseArray(courseSylibusJson.getString("d"));
					CourseSyllabus courseSyllabus = null;
					for (int i = 0; i < courseSylibusArray.size(); i++) {
						String meetDate = courseSylibusArray.getJSONObject(i).getString("MeetDate");
						String meetStart = courseSylibusArray.getJSONObject(i).getString("MeetStart");
						String meetEnd = courseSylibusArray.getJSONObject(i).getString("MeetEnd");
						String meetNum = courseSylibusArray.getJSONObject(i).getString("MeetNum");
						String meetBranch = courseSylibusArray.getJSONObject(i).getString("MeetBranch");
						String meetBuildingNum = courseSylibusArray.getJSONObject(i).getString("MeetBuildingNum");
						String meetBuildingName = courseSylibusArray.getJSONObject(i).getString("MeetBuildingName");
						String meetClassNum = courseSylibusArray.getJSONObject(i).getString("MeetClassNum");
						String meetTeacher = courseSylibusArray.getJSONObject(i).getString("MeetTeacher");
						String meetStatus = courseSylibusArray.getJSONObject(i).getString("MeetStatus");
						String meetTopic = courseSylibusArray.getJSONObject(i).getString("MeetTopic");
						String meetClassName = courseSylibusArray.getJSONObject(i).getString("MeetClassName");
						courseSyllabus = new CourseSyllabus(meetDate, meetStart, meetEnd, meetNum, meetBranch,
								meetBuildingNum, meetBuildingName, meetClassNum, meetTeacher, meetStatus, meetTopic,
								meetClassName);
						if (!(courseSyllabus.toString().indexOf("null") > -1)) {
							courseSyllabusList.add(courseSyllabus);
						}
					}
					if (courseSyllabusList.size() > 0) {
						adapter.notifyDataSetChanged();
					} else {
						CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(
								CourseSylibusFragment.this.getActivity(), false);
						builder.setDialogText(getResources().getString(R.string.cs_no_training_schedule));
						Dialog dialog = builder.create();
						dialog.setCancelable(false);
						dialog.show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class CourseSylibusExpandAdapter extends BaseAdapter {
		private List<CourseSyllabus> courseSyllabusList = null;
		private Calendar calendar = Calendar.getInstance();
		private SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
		private SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
		private ViewHolder holder = null;

		public CourseSylibusExpandAdapter(List<CourseSyllabus> courseSyllabusList) {
			super();
			this.courseSyllabusList = courseSyllabusList;
		}

		@Override
		public int getCount() {
			return courseSyllabusList.size();
		}

		@Override
		public Object getItem(int position) {
			return courseSyllabusList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = LayoutInflater.from(CourseSylibusFragment.this.getActivity())
						.inflate(R.layout.course_sylibus_expand_items, null);
				holder = new ViewHolder();
				holder.start_end_time = (TextView) convertView.findViewById(R.id.start_end_time);
				holder.meeting_date = (TextView) convertView.findViewById(R.id.meeting_date);
				holder.class_number = (TextView) convertView.findViewById(R.id.class_number);
				holder.teacher_name = (TextView) convertView.findViewById(R.id.teacher_name);
				holder.lesson_topic = (TextView) convertView.findViewById(R.id.lesson_topic);
				holder.classroom_number = (TextView) convertView.findViewById(R.id.classroom_number);
				holder.expand_icon = (ImageView) convertView.findViewById(R.id.expand_icon);
				holder.expand_icon_hidden = (ImageView) convertView.findViewById(R.id.expand_icon_hidden);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			CourseSyllabus courseSyllabus = courseSyllabusList.get(position);
			try {
				if (MobileNetStatus.expandItemNum == position) {
					holder.expand_icon.setVisibility(View.GONE);
					holder.expand_icon_hidden.setVisibility(View.VISIBLE);
				} else {
					holder.expand_icon.setVisibility(View.VISIBLE);
					holder.expand_icon_hidden.setVisibility(View.GONE);
				}
				Date timeStart = new Date(Long.valueOf(courseSyllabus.getMeetStart()));
				Date timeEnd = new Date(Long.valueOf(courseSyllabus.getMeetEnd()));
				holder.start_end_time.setText(formatTime.format(timeEnd) + " - " + formatTime.format(timeStart));
				Date date = new Date(Long
						.valueOf(courseSyllabus.getMeetDate().substring(6, courseSyllabus.getMeetDate().length() - 2)));
				calendar.setTime(date);
				String date_text = "";
				switch (calendar.get(calendar.DAY_OF_WEEK)) {
				case 1:
					date_text += "יום ראשון";
					break;
				case 2:
					date_text += "יום שני";
					break;
				case 3:
					date_text += "יום שלישי";
					break;
				case 4:
					date_text += "יום רביעי";
					break;
				case 5:
					date_text += "יום חמישי";
					break;
				case 6:
					date_text += "יום שישי";
					break;
				case 7:
					date_text += "יום שבת";
					break;
				default:
					break;
				}
				date_text += (" - " + formatDate.format(date));
				holder.meeting_date.setText(date_text);
			} catch (Exception e) {
				e.printStackTrace();
			}
			holder.class_number.setText(courseSyllabus.getMeetClassNum());
			holder.teacher_name.setText(courseSyllabus.getMeetTeacher());
			holder.lesson_topic.setText(courseSyllabus.getMeetTopic());
			holder.classroom_number.setText(courseSyllabus.getMeetBuildingNum());
			return convertView;
		}

		class ViewHolder {
			TextView start_end_time;
			TextView meeting_date;
			TextView class_number;
			TextView teacher_name;
			TextView lesson_topic;
			TextView classroom_number;
			ImageView expand_icon;
			ImageView expand_icon_hidden;
		}
	}
}
