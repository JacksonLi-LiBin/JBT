package com.lb.jbt;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lb.constants.MobileNetStatus;
import com.lb.entities.Course;
import com.lb.entities.Student;
import com.lb.tools.ReadProperties;
import com.lb.widgets.CustomAlertDialog;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class ForgotPasswordActivity extends Activity implements View.OnTouchListener, View.OnClickListener {
	private ScrollView forgot_pwd_items_layout;
	private RelativeLayout stu_num_layout;
	private EditText stu_num;
	private Spinner course_num;
	private TextView stu_num_title, course_num_title;
	private Button forget_pwd_btn;
	private SharedPreferences spf = null;
	private String storedUserId = "";
	private List<String> masualList = null;
	private MasualAdapter masualAdapter = null;
	private String userEmail = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		spf = getSharedPreferences("jbt", MODE_PRIVATE);
		storedUserId = spf.getString("userId", "");
		masualList = new ArrayList<String>();
		masualAdapter = new MasualAdapter(masualList, ForgotPasswordActivity.this);
		forgot_pwd_items_layout = (ScrollView) findViewById(R.id.forgot_pwd_items_layout);
		forgot_pwd_items_layout.setOnTouchListener(this);
		stu_num_layout = (RelativeLayout) findViewById(R.id.stu_num_layout);
		stu_num = (EditText) findViewById(R.id.stu_num);
		course_num = (Spinner) findViewById(R.id.course_num);
		course_num.setAdapter(masualAdapter);
		course_num.setOnTouchListener(this);
		stu_num_title = (TextView) findViewById(R.id.stu_num_title);
		course_num_title = (TextView) findViewById(R.id.course_num_title);
		forget_pwd_btn = (Button) findViewById(R.id.forget_pwd_btn);
		forget_pwd_btn.setOnClickListener(this);
		stu_num.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				stu_num_title.setTextColor(getResources().getColor(R.color.hint_text));
				if (s.toString().length() == 0) {
					stu_num_title.setVisibility(View.VISIBLE);
				} else {
					stu_num_title.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		stu_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (stu_num.getText().toString().length() > 0) {
						if (MobileNetStatus.isNetUsable) {
							new GetUserinfoAsync().execute(0);
						} else {
							// network is unavailable
							Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.net_unusable),
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		});
		if (!(storedUserId.equals("") || storedUserId == null)) {
			stu_num.setText(storedUserId);
			stu_num_title.setVisibility(View.GONE);
		}
	}

	private class GetUserinfoAsync extends AsyncTask<Integer, Void, String> {
		private final OkHttpClient okHttpClient = new OkHttpClient();
		private Integer opeType = null;

		@Override
		protected String doInBackground(Integer... params) {
			opeType = params[0];
			try {
				switch (opeType) {
				case 0:
					HttpUrl url = HttpUrl.parse(
							ReadProperties.read("url", "getuserinfo") + "?userId=" + stu_num.getText().toString());
					Request request = new Request.Builder().url(url).build();
					Response response = okHttpClient.newCall(request).execute();
					if (response.isSuccessful()) {
						return response.body().string();
					}
					break;
				case 1:
					if (userEmail.length() > 0 && userEmail != null) {
						HttpUrl urlSendEmail = HttpUrl.parse(
								ReadProperties.read("url", "jackson_ad_login_cp") + "CommonMethods/sendEmail?userId="
										+ stu_num.getText().toString() + "&email=" + userEmail);
						Request requestSendEmail = new Request.Builder().url(urlSendEmail).build();
						Response responseSendEmail = okHttpClient.newCall(requestSendEmail).execute();
						if (responseSendEmail.isSuccessful()) {
							String result = responseSendEmail.body().string();
							if (result.equals("true")) {
								return "{backResult:2}";
							} else {
								return "{backResult:1}";
							}
						}
					} else {
						return "{backResult:0}";
					}
					break;
				default:
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
					switch (opeType) {
					case 0:
						JSONObject getInfo = new JSONObject(result);
						String getUser = getInfo.getString("d");
						if (getUser.equals("\"Empty\"")) {
							// user is unavailable
							Toast.makeText(ForgotPasswordActivity.this,
									getResources().getString(R.string.login_user_unusable), Toast.LENGTH_SHORT).show();
							masualList.clear();
							masualAdapter.notifyDataSetChanged();
							course_num_title.setVisibility(View.VISIBLE);
						} else {
							JSONObject userInfo = new JSONObject(getUser);
							String stuId = userInfo.getString("ID");
							userEmail = userInfo.getString("Email");
							String courseStr = userInfo.getString("Course");
							JSONArray courseArray = new JSONArray(courseStr);
							List<Course> courseList = new ArrayList<Course>();
							for (int i = 0; i < courseArray.length(); i++) {
								JSONObject courseObj = courseArray.getJSONObject(i);
								Course course = new Course(courseObj.getString("CourseNumber"),
										courseObj.getString("CourseName"), courseObj.getString("CycleNumber"));
								courseList.add(course);
							}
							Student student = new Student(stuId, courseList);
							if (student.getCourseList().size() > 0) {
								course_num_title.setVisibility(View.GONE);
							} else {
								course_num_title.setVisibility(View.VISIBLE);
							}
							masualList.clear();
							for (Course course : student.getCourseList()) {
								String masualNum = course.getCourseNumber() + "/" + course.getCycleNumber() + "-->"
										+ course.getCourseName();
								masualList.add(masualNum);
							}
							masualAdapter.notifyDataSetChanged();
						}
						break;
					case 1:
						Integer backResult = new JSONObject(result).getInt("backResult");
						CustomAlertDialog.Builder builder = null;
						Dialog dialog = null;
						switch (backResult) {
						case 0:
							// user email not exist
							Toast.makeText(ForgotPasswordActivity.this,
									getResources().getString(R.string.no_email_text), Toast.LENGTH_SHORT).show();
							break;
						case 1:
							// send email failed
							builder = new CustomAlertDialog.Builder(ForgotPasswordActivity.this, false);
							builder.setDialogText(getResources().getString(R.string.send_email_failed));
							dialog = builder.create();
							dialog.setCancelable(false);
							dialog.show();
							break;
						case 2:
							// send email succeed
							builder = new CustomAlertDialog.Builder(ForgotPasswordActivity.this, true);
							builder.setDialogText(getResources().getString(R.string.send_email_succeed));
							builder.setPosiClickListener(new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									ForgotPasswordActivity.this.finish();
								}
							});
							dialog = builder.create();
							dialog.setCancelable(false);
							dialog.show();
							break;
						default:
							break;
						}
						break;
					default:
						break;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// request failed
				Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.request_failed),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// course masual adapter
	private class MasualAdapter extends BaseAdapter {
		private List<String> masualList = null;
		private Context mContext = null;

		public MasualAdapter(List<String> masualList, Context mContext) {
			super();
			this.masualList = masualList;
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return masualList.size();
		}

		@Override
		public Object getItem(int position) {
			return masualList.get(position);
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
				view = LayoutInflater.from(mContext).inflate(R.layout.masual_list, null);
			}
			TextView masualNum = (TextView) view.findViewById(R.id.masualNum);
			TextView course_name = (TextView) view.findViewById(R.id.course_name);
			String courseInfo = masualList.get(position);
			String masualNumStr = courseInfo.split("-->")[0];
			String courseNameStr = courseInfo.split("-->")[1];
			masualNum.setText(masualNumStr);
			course_name.setText(courseNameStr);
			return view;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) ForgotPasswordActivity.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(stu_num.getWindowToken(), 0);
		if (stu_num.isFocused()) {
			stu_num.clearFocus();
			stu_num_layout.setFocusable(true);
			stu_num_layout.setFocusableInTouchMode(true);
			stu_num_layout.requestFocus();
		}
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) ForgotPasswordActivity.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(stu_num.getWindowToken(), 0);
		if (stu_num.isFocused()) {
			stu_num.clearFocus();
			stu_num_layout.setFocusable(true);
			stu_num_layout.setFocusableInTouchMode(true);
			stu_num_layout.requestFocus();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		int ret = 1;
		stu_num_title.setTextColor(getResources().getColor(R.color.hint_text));
		course_num_title.setTextColor(getResources().getColor(R.color.hint_text));
		switch (v.getId()) {
		case R.id.forget_pwd_btn:
			if (stu_num.getText().toString().length() <= 0) {
				ret = -1;
				stu_num_title.setTextColor(getResources().getColor(R.color.red_light));
			} else if (masualList.size() == 0 || course_num.getSelectedItem().toString().length() <= 0) {
				ret = -1;
				course_num_title.setTextColor(getResources().getColor(R.color.red_light));
			}
			if (ret > 0) {
				if (MobileNetStatus.isNetUsable) {
					new GetUserinfoAsync().execute(1);
				} else {
					// network is unavailable
					Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.net_unusable),
							Toast.LENGTH_SHORT).show();
				}
			}
			break;

		default:
			break;
		}
	}
}
