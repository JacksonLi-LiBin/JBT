package com.lb.jbt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ForgotPasswordActivity extends Activity {
	private EditText stu_num;
	private Spinner course_num;
	private TextView stu_num_title, course_num_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		stu_num = (EditText) findViewById(R.id.stu_num);
		course_num = (Spinner) findViewById(R.id.course_num);
		stu_num_title = (TextView) findViewById(R.id.stu_num_title);
		course_num_title = (TextView) findViewById(R.id.course_num_title);
	}
}
