package com.lb.jbt;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lb.constants.MobileNetStatus;
import com.lb.entities.Branch;
import com.lb.entities.Course;
import com.lb.entities.Student;
import com.lb.tools.ReadProperties;
import com.lb.widgets.CustomAlertDialog;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class LoginActivity extends Activity implements View.OnClickListener, View.OnTouchListener {
	private static final String TAG = "LoginActivity";
	private RelativeLayout stu_num_layout;
	private ScrollView login_items_layout;
	private EditText stu_num, stu_pwd;
	private TextView stu_num_title, stu_name, course_num_title, stu_pwd_title, forgot_pwd;
	private Spinner course_num;
	private Button user_login_btn;
	private List<String> masualList = null;
	private MasualAdapter masualAdapter = null;
	private SharedPreferences spf = null;
	private SharedPreferences.Editor editor = null;

	private String storedUserId, storedToken, selectedMasualNum, selectedCourseName, selectedBranch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		spf = getSharedPreferences("jbt", MODE_PRIVATE);
		editor = spf.edit();
		// judge whether user has logon
		storedUserId = spf.getString("userId", "");
		storedToken = spf.getString("userToken", "");

		masualList = new ArrayList<String>();
		masualAdapter = new MasualAdapter(masualList, LoginActivity.this);

		login_items_layout = (ScrollView) findViewById(R.id.login_items_layout);
		login_items_layout.setOnTouchListener(this);
		stu_num_layout = (RelativeLayout) findViewById(R.id.stu_num_layout);
		stu_num = (EditText) findViewById(R.id.stu_num);
		stu_name = (TextView) findViewById(R.id.stu_name);
		course_num_title = (TextView) findViewById(R.id.course_num_title);

		course_num = (Spinner) findViewById(R.id.course_num);
		course_num.setAdapter(masualAdapter);
		course_num.setOnTouchListener(this);

		stu_pwd = (EditText) findViewById(R.id.stu_pwd);
		stu_num_title = (TextView) findViewById(R.id.stu_num_title);
		stu_pwd_title = (TextView) findViewById(R.id.stu_pwd_title);
		user_login_btn = (Button) findViewById(R.id.user_login_btn);
		user_login_btn.setOnClickListener(this);
		forgot_pwd = (TextView) findViewById(R.id.forgot_pwd);
		forgot_pwd.setOnClickListener(this);

		// display or hide hint text
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
							new LoginAsync().execute(0);
						} else {
							// network is unavailable
							Toast.makeText(LoginActivity.this, getResources().getString(R.string.net_unusable),
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		});
		stu_pwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				stu_pwd_title.setTextColor(getResources().getColor(R.color.hint_text));
				if (s.toString().trim().length() == 0) {
					stu_pwd_title.setVisibility(View.VISIBLE);
				} else {
					stu_pwd_title.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		course_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				LinearLayout ll = (LinearLayout) view;
				if (ll != null) {
					TextView masualNumTV = (TextView) ll.getChildAt(0);
					TextView courseNameTV = (TextView) ll.getChildAt(1);
					selectedMasualNum = masualNumTV.getText().toString();
					selectedCourseName = courseNameTV.getText().toString();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		// user login out abnormal
		if (storedToken != null && !storedToken.equals("")) {
			if (MobileNetStatus.isNetUsable) {
				new LoginAsync().execute(2);
			} else {
				// network is unavailable
				Toast.makeText(LoginActivity.this, getResources().getString(R.string.net_unusable), Toast.LENGTH_SHORT)
						.show();
			}
		}
		// set user id when app start
		if (storedUserId != null && !storedUserId.equals("")) {
			stu_num.setText(storedUserId);
			stu_num_title.setVisibility(View.GONE);
		}
	}

	public class LoginAsync extends AsyncTask<Integer, Void, String> {
		private final OkHttpClient okHttpClient = new OkHttpClient();
		Integer opeType = null;

		@Override
		protected String doInBackground(Integer... params) {
			opeType = params[0];
			switch (opeType) {
			// 0 get user info
			case 0:
				try {
					// HttpUrl url = new HttpUrl.Builder()
					// .scheme("http")
					// .host(ReadProperties.read("url", "common_url"))
					// .port(Integer.parseInt(ReadProperties.read("url",
					// "common_port"))).addPathSegment("JBT")
					// .addPathSegment("rest")
					// .addPathSegment("CommonMethods")
					// .addPathSegment("getUserInfos")
					// .addQueryParameter("userId", passContent).build();
					HttpUrl url = HttpUrl.parse(
							ReadProperties.read("url", "getuserinfo") + "?userId=" + stu_num.getText().toString());
					Request request = new Request.Builder().url(url).build();
					Response response = okHttpClient.newCall(request).execute();
					if (response.isSuccessful()) {
						String getInfoStr = response.body().string();
						return getInfoStr;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			// 1 user login
			case 1:
				try {
					// judge whether has user logon
					// if (storedToken == null || storedToken.equals("")) {}
					HttpUrl urlCheckLogon = HttpUrl.parse(ReadProperties.read("url", "jackson_ad_login_cp")
							+ "CommonMethods/judgeWeatherUserLogin?userId=" + stu_num.getText().toString()
							+ "&password=" + stu_pwd.getText().toString() + "&token=" + storedToken);
					Request requestCheckLogon = new Request.Builder().url(urlCheckLogon).build();
					Response responseCheckLogon = okHttpClient.newCall(requestCheckLogon).execute();
					if (responseCheckLogon.isSuccessful()) {
						String whetherLogon = responseCheckLogon.body().string();
						if (whetherLogon.equals("false")) {
							// new user or old user 0 new 1 old false user
							// does not exist
							HttpUrl urlCheckFlag = HttpUrl.parse(ReadProperties.read("url", "jackson_ad_login_cp")
									+ "CommonMethods/getUserFlag?userId=" + stu_num.getText().toString());
							Request requestCheckFlag = new Request.Builder().url(urlCheckFlag).build();
							Response responseCheckFlag = okHttpClient.newCall(requestCheckFlag).execute();
							if (responseCheckFlag.isSuccessful()) {
								String getFlag = responseCheckFlag.body().string();
								if (getFlag.equals("false")) {
									return "{type:1}";
								} else {
									if (getFlag.equals("0")) {
										if (!stu_pwd.getText().toString().equals("000000")) {
											return "{type:2}";
										}
									}
									// login
									HttpUrl urlLogin = HttpUrl.parse(ReadProperties.read("url", "jackson_ad_login_cp")
											+ "UserLogin/login?userId=" + stu_num.getText().toString() + "&password="
											+ stu_pwd.getText().toString() + "&registionId=000000&passToken="
											+ (storedToken == null ? "" : storedToken) + "&flag=" + getFlag);
									Request requestLogin = new Request.Builder().url(urlLogin).build();
									Response responseLogin = okHttpClient.newCall(requestLogin).execute();
									if (responseLogin.isSuccessful()) {
										String loginReturn = responseLogin.body().string();
										if (loginReturn.length() > 0) {
											editor.putString("userId", stu_num.getText().toString());
											editor.putString("fullName", stu_name.getText().toString());
											editor.putString("userToken",
													loginReturn.substring(0, loginReturn.length() - 1));
											editor.putString("branch", selectedBranch);
											editor.putString("coursename", selectedCourseName);
											editor.putString("coursenum", selectedMasualNum.split("/")[0]);
											editor.putString("cyclenum", selectedMasualNum.split("/")[1]);
											editor.commit();
											return "{type:4}";
										} else {
											return "{type:3}";
										}
									}
								}
							}
						} else if (whetherLogon.equals("usable")) {
							return "{type:5}";
						} else {
							return "{type:0}";
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try {
					// user exit abnormal and login automatically when start app
					HttpUrl urlCheckToken = HttpUrl.parse(ReadProperties.read("url", "jackson_ad_login_cp")
							+ "CommonMethods/judgeWeatherUserLogin?userId=" + stu_num.getText().toString());
					Request requestCheckToken = new Request.Builder().url(urlCheckToken).build();
					Response responseCheckToken = okHttpClient.newCall(requestCheckToken).execute();
					if (responseCheckToken.isSuccessful()) {
						String checkResult = responseCheckToken.body().string();
						if (checkResult.equals("true")) {
							return "{type:1}";
						} else {
							return "{type:0}";
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			switch (opeType) {
			// 0 get user info
			case 0:
				if (result != null) {
					try {
						JSONObject getInfo = new JSONObject(result);
						String getUser = getInfo.getString("d");
						if (getUser.equals("\"Empty\"")) {
							// user is unavailable
							Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_user_unusable),
									Toast.LENGTH_SHORT).show();
							stu_name.setText("");
							masualList.clear();
							masualAdapter.notifyDataSetChanged();
							course_num_title.setVisibility(View.VISIBLE);
						} else {
							JSONObject userInfo = new JSONObject(getUser);
							String stuId = userInfo.getString("ID");
							String firstName = userInfo.getString("FirstName");
							String lastName = userInfo.getString("LastName");
							String email = userInfo.getString("Email");
							String courseStr = userInfo.getString("Course");
							JSONArray courseArray = new JSONArray(courseStr);
							List<Course> courseList = new ArrayList<Course>();
							for (int i = 0; i < courseArray.length(); i++) {
								JSONObject courseObj = courseArray.getJSONObject(i);
								Course course = new Course(courseObj.getString("CourseNumber"),
										courseObj.getString("CourseName"), courseObj.getString("CycleNumber"));
								courseList.add(course);
							}
							String branchStr = userInfo.getString("Branch");
							JSONArray branchArray = new JSONArray(branchStr);
							List<Branch> branchList = new ArrayList<Branch>();
							for (int i = 0; i < branchArray.length(); i++) {
								JSONObject branchObj = branchArray.getJSONObject(i);
								Branch branch = new Branch(branchObj.getString("Branch"));
								branchList.add(branch);
							}
							Student student = new Student(stuId, firstName, lastName, email, courseList, branchList);
							stu_name.setText(student.getFirstName() + " " + student.getLastName());
							if (branchList.size() > 0) {
								selectedBranch = branchList.get(0) == null ? "" : branchList.get(0).getBranchId();
							}
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
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// request failed
					Toast.makeText(LoginActivity.this, getResources().getString(R.string.request_failed),
							Toast.LENGTH_SHORT).show();
				}
				break;
			// 1 user login
			case 1:
				if (result != null) {
					Dialog dialog = null;
					try {
						JSONObject returnValue = new JSONObject(result);
						Integer returnType = Integer.parseInt(returnValue.getString("type"));
						switch (returnType) {
						// login failed
						case 0:
							Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_user_offline),
									Toast.LENGTH_SHORT).show();
							break;
						case 1:
							Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_unexist_user),
									Toast.LENGTH_SHORT).show();
							break;
						case 2:
							Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_default_pwd),
									Toast.LENGTH_SHORT).show();
							break;
						case 3:
							// password wrong
							CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(LoginActivity.this,
									false);
							builder.setDialogText(getResources().getString(R.string.login_failed));
							dialog = builder.create();
							dialog.setCancelable(false);
							dialog.show();
							break;
						case 4:
						case 5:
							// login successfully
							Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
							startActivity(intent);
							LoginActivity.this.finish();
							break;
						default:
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case 2:
				try {
					JSONObject returnValue = new JSONObject(result);
					Integer returnType = Integer.parseInt(returnValue.getString("type"));
					switch (returnType) {
					// token unusable
					case 0:
						// set user id when app start
						if (storedUserId != null && !storedUserId.equals("")) {
							stu_num.setText(storedUserId);
							stu_num_title.setVisibility(View.GONE);
						}
						break;
					// token usable
					case 1:
						Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
						startActivity(intent);
						LoginActivity.this.finish();
						break;
					default:
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
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
	protected void onDestroy() {
		super.onDestroy();
		if (editor != null) {
			editor.clear();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_login_btn:
			if (MobileNetStatus.isNetUsable) {
				int ret = 1;
				stu_num_title.setTextColor(getResources().getColor(R.color.hint_text));
				stu_name.setHintTextColor(getResources().getColor(R.color.hint_text));
				course_num_title.setTextColor(getResources().getColor(R.color.hint_text));
				stu_pwd_title.setTextColor(getResources().getColor(R.color.hint_text));
				// validate user input
				if (stu_num.getText().toString().length() <= 0) {
					ret = -1;
					stu_num_title.setTextColor(getResources().getColor(R.color.red_light));
					return;
				} else if (stu_name.getText().toString().length() <= 0) {
					ret = -1;
					stu_name.setHintTextColor(getResources().getColor(R.color.red_light));
					return;
				} else if (masualList.size() == 0 || course_num.getSelectedItem().toString().length() <= 0) {
					ret = -1;
					course_num_title.setTextColor(getResources().getColor(R.color.red_light));
					return;
				} else if (stu_pwd.getText().toString().length() <= 0) {
					ret = -1;
					stu_pwd_title.setTextColor(getResources().getColor(R.color.red_light));
					return;
				}
				if (ret > 0) {
					new LoginAsync().execute(1);
				}
			} else {
				// network is unavailable
				Toast.makeText(LoginActivity.this, getResources().getString(R.string.net_unusable), Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.forgot_pwd:
			Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	// hide soft input
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
		InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(stu_num.getWindowToken(), 0);
		if (stu_num.isFocused()) {
			stu_num.clearFocus();
			stu_num_layout.setFocusable(true);
			stu_num_layout.setFocusableInTouchMode(true);
			stu_num_layout.requestFocus();
		}
		return false;
	}
}
