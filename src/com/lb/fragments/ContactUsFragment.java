package com.lb.fragments;

import org.json.JSONObject;

import com.lb.constants.MobileNetStatus;
import com.lb.jbt.LoginActivity;
import com.lb.jbt.R;
import com.lb.tools.ReadProperties;
import com.lb.widgets.CustomAlertDialog;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ContactUsFragment extends Fragment implements View.OnClickListener {
	private SharedPreferences spf = null;
	// private FragmentManager manager;
	private EditText contact_description, contact_email, contact_phone;
	private TextView contact_description_hint, contact_email_hint, contact_phone_hint;
	private Spinner contact_inquiry_subject;
	private Button contact_submit_btn;
	private InquirySubjectAdapter isAdapter;
	private String[] inquirySubjects = null;

	private String storedToken, userId, fullName, branch, coursename, coursenum, cyclenum, subjectnum;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		spf = ContactUsFragment.this.getActivity().getSharedPreferences("jbt",
				ContactUsFragment.this.getActivity().MODE_PRIVATE);
		// manager = getFragmentManager();
		// Bundle bundle = manager.findFragmentByTag("cotactus").getArguments();
		storedToken = spf.getString("userToken", "");
		userId = spf.getString("userId", "");
		fullName = spf.getString("fullName", "");
		branch = spf.getString("branch", "");
		coursename = spf.getString("coursename", "");
		coursenum = spf.getString("coursenum", "");
		cyclenum = spf.getString("cyclenum", "");
		inquirySubjects = getResources().getStringArray(R.array.contact_inquiry_subjects);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_us_fragment, container, false);
		contact_description = (EditText) view.findViewById(R.id.contact_description);
		contact_email = (EditText) view.findViewById(R.id.contact_email);
		contact_phone = (EditText) view.findViewById(R.id.contact_phone);
		contact_inquiry_subject = (Spinner) view.findViewById(R.id.contact_inquiry_subject);
		isAdapter = new InquirySubjectAdapter(inquirySubjects);
		contact_inquiry_subject.setAdapter(isAdapter);
		isAdapter.notifyDataSetChanged();
		contact_description_hint = (TextView) view.findViewById(R.id.contact_description_hint);
		contact_email_hint = (TextView) view.findViewById(R.id.contact_email_hint);
		contact_phone_hint = (TextView) view.findViewById(R.id.contact_phone_hint);
		contact_submit_btn = (Button) view.findViewById(R.id.contact_submit_btn);
		contact_submit_btn.setOnClickListener(this);
		contact_description.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				contact_description_hint.setTextColor(getResources().getColor(R.color.hint_text));
				if (s.toString().length() == 0) {
					contact_description_hint.setVisibility(View.VISIBLE);
				} else {
					contact_description_hint.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		contact_email.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				contact_email_hint.setTextColor(getResources().getColor(R.color.hint_text));
				if (s.toString().length() == 0) {
					contact_email_hint.setVisibility(View.VISIBLE);
				} else {
					contact_email_hint.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		contact_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				contact_phone_hint.setTextColor(getResources().getColor(R.color.hint_text));
				if (s.toString().length() == 0) {
					contact_phone_hint.setVisibility(View.VISIBLE);
				} else {
					contact_phone_hint.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		contact_inquiry_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				LinearLayout ll = (LinearLayout) view;
				if (ll != null) {
					TextView tv = (TextView) ll.getChildAt(0);
					TextView sub_num = (TextView) ll.getChildAt(1);
					subjectnum = sub_num.getText().toString();
					if (position == 0) {
						tv.setTextColor(getResources().getColor(R.color.hint_text));
					} else {
						tv.setTextColor(getResources().getColor(R.color.black_text));
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// submit contact us content
		case R.id.contact_submit_btn:
			if (MobileNetStatus.isNetUsable) {
				int ret = 1;
				contact_description_hint.setTextColor(getResources().getColor(R.color.hint_text));
				contact_email_hint.setHintTextColor(getResources().getColor(R.color.hint_text));
				contact_phone_hint.setTextColor(getResources().getColor(R.color.hint_text));
				if (contact_inquiry_subject.getSelectedItem().toString()
						.indexOf(getResources().getString(R.string.contact_inquiry_subject_hint)) > -1) {
					((TextView) contact_inquiry_subject.getChildAt(0).findViewById(R.id.inquirySubject))
							.setTextColor(getResources().getColor(R.color.red_light));
					ret = -1;
					return;
				} else if (contact_description.getText().toString().length() <= 0) {
					contact_description_hint.setTextColor(getResources().getColor(R.color.red_light));
					ret = -1;
					return;
				} else if (contact_email.getText().toString().length() <= 0) {
					contact_email_hint.setTextColor(getResources().getColor(R.color.red_light));
					ret = -1;
					return;
				} else if (contact_phone.getText().toString().length() <= 0) {
					contact_phone_hint.setTextColor(getResources().getColor(R.color.red_light));
					ret = -1;
					return;
				}
				if (ret > 0) {
					new ContactSubmitAsync().execute();
				}
			} else {
				// network is unavailable
				Toast.makeText(ContactUsFragment.this.getActivity(), getResources().getString(R.string.net_unusable),
						Toast.LENGTH_SHORT).show();
			}

			break;

		default:
			break;
		}
	}

	private class InquirySubjectAdapter extends BaseAdapter {
		private String[] inquirySubjects = null;

		public InquirySubjectAdapter(String[] inquirySubjects) {
			super();
			this.inquirySubjects = inquirySubjects;
		}

		@Override
		public int getCount() {
			return inquirySubjects.length;
		}

		@Override
		public Object getItem(int position) {
			return inquirySubjects[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			try {
				if (convertView != null) {
					view = convertView;
				} else {
					view = LayoutInflater.from(ContactUsFragment.this.getActivity()).inflate(R.layout.inquiry_list,
							null);
				}
				TextView inquirySubject = (TextView) view.findViewById(R.id.inquirySubject);
				TextView inquirySubjectNum = (TextView) view.findViewById(R.id.subjectnum);
				if (position == 0) {
					inquirySubject.setTextColor(getResources().getColor(R.color.hint_text));
				}
				String inquirySubjectStr = inquirySubjects[position];
				String subjectTitle = inquirySubjectStr.split(">")[0];
				String subjectNum = inquirySubjectStr.split(">")[1];
				inquirySubject.setText(subjectTitle);
				inquirySubjectNum.setText(subjectNum);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return view;
		}
	}

	private class ContactSubmitAsync extends AsyncTask<Void, Void, String> {
		private final OkHttpClient okHttpClient = new OkHttpClient();
		private Integer opeType = null;

		@Override
		protected String doInBackground(Void... params) {
			try {
				HttpUrl checkTimeoutUrl = HttpUrl
						.parse(ReadProperties.read("url", "jackson_timeout") + storedToken + "/jobListRequest");
				Request checkTimeoutReq = new Request.Builder().url(checkTimeoutUrl).build();
				Response checkTimeoutRes = okHttpClient.newCall(checkTimeoutReq).execute();
				if (checkTimeoutRes.isSuccessful()) {
					String getResult = checkTimeoutRes.body().string();
					if (getResult.equals("timeout")) {
						// exit and login
						return "{opeType:0}";
					} else {
						HttpUrl checkJBHUrl = HttpUrl.parse(ReadProperties.read("url", "jackson_jbh_check") + fullName
								+ "&id=" + userId + "&coursename=" + coursename + "&coursenum=" + coursenum
								+ "&cyclenum=" + cyclenum + "&branch=" + branch + "&phone="
								+ contact_phone.getText().toString() + "&email=" + contact_email.getText().toString()
								+ "&subjectnum=" + subjectnum + "&details=" + contact_description.getText().toString());
						Request checkJBHReq = new Request.Builder().url(checkJBHUrl).build();
						Response checkJBHRes = okHttpClient.newCall(checkJBHReq).execute();
						if (checkJBHRes.isSuccessful()) {
							String jbhResult = checkJBHRes.body().string();
							JSONObject jbhResultJson = new JSONObject(jbhResult);
							if (jbhResultJson.get("d").equals("\"good\"")) {
								HttpUrl saveLocalUrl = HttpUrl
										.parse(ReadProperties.read("url", "jackson_student_inquiry_local") + storedToken
												+ "/create?fullname=" + fullName + "&id=" + userId + "&coursename="
												+ coursename + "&coursenum=" + coursenum + "&cyclenum=" + cyclenum
												+ "&branch=" + branch + "&phone=" + contact_phone.getText().toString()
												+ "&email=" + contact_email.getText().toString() + "&subjectnum="
												+ subjectnum + "&details=" + contact_description.getText().toString());
								Request saveLocalReq = new Request.Builder().url(saveLocalUrl).build();
								Response saveLoalRes = okHttpClient.newCall(saveLocalReq).execute();
								if (saveLoalRes.isSuccessful()) {
									String saveLocalResult = saveLoalRes.body().string();
									if (saveLocalResult.equals("true")) {
										// add inquiry success
										return "{opeType:2}";
									} else {
										// add inquiry failed
										return "{opeType:3}";
									}
								}
							} else {
								// remote check failed
								return "{opeType:1}";
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				opeType = Integer.parseInt(new JSONObject(result).getString("opeType"));
				Intent intent = null;
				CustomAlertDialog.Builder builder = null;
				Dialog dialog = null;
				switch (opeType) {
				// timeout exit and login
				case 0:
					ActivityCompat.finishAffinity(ContactUsFragment.this.getActivity());
					intent = new Intent(ContactUsFragment.this.getActivity(), LoginActivity.class);
					startActivity(intent);
					Toast.makeText(ContactUsFragment.this.getActivity(),
							getResources().getString(R.string.ope_time_out), Toast.LENGTH_SHORT).show();
					break;
				case 1:
					// remote check failed
					builder = new CustomAlertDialog.Builder(ContactUsFragment.this.getActivity(), false);
					builder.setDialogText(getResources().getString(R.string.contact_remote_check_fail));
					dialog = builder.create();
					dialog.setCancelable(false);
					dialog.show();
					break;
				case 2:
					// add inquiry success
					builder = new CustomAlertDialog.Builder(ContactUsFragment.this.getActivity(), true);
					builder.setDialogText(getResources().getString(R.string.contact_add_inquiry_success));
					builder.setPosiClickListener(new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							ContactUsFragment.this.getActivity().finish();
						}
					});
					dialog = builder.create();
					dialog.setCancelable(false);
					dialog.show();
					break;
				case 3:
					// add inquiry failed
					builder = new CustomAlertDialog.Builder(ContactUsFragment.this.getActivity(), false);
					builder.setDialogText(getResources().getString(R.string.contact_add_inquiry_failed));
					dialog = builder.create();
					dialog.setCancelable(false);
					dialog.show();
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
