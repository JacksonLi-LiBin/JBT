package com.lb.jbt;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lb.entities.FriendSelected;
import com.lb.fragments.ChangePasswordFragment;
import com.lb.fragments.ContactUsFragment;
import com.lb.fragments.CourseSylibusFragment;
import com.lb.fragments.JobsFragment;
import com.lb.fragments.MyGradesFragment;
import com.lb.fragments.RecommendFriendFragment;

public class MainMenuItemActivity extends Activity {
	private FragmentManager manager;
	private FragmentTransaction transaction;
	private EditText friend_name, friend_phone, friend_email;
	private TextView friend_name_hint, friend_phone_hint, friend_email_hint;

	// private String storedToken;
	// private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu_item);

		manager = getFragmentManager();
		transaction = manager.beginTransaction();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		Integer itemType = bundle.getInt("itemType");
		Fragment fragment = null;
		switch (itemType) {
		case 0:
			fragment = new CourseSylibusFragment();
			transaction.replace(R.id.menuItemFragment, fragment,
					"coursesylibus");
			transaction.commit();
			break;
		case 1:
			// get my grades
			fragment = new MyGradesFragment();
			transaction.replace(R.id.menuItemFragment, fragment, "mygrades");
			transaction.commit();
			break;
		case 2:

			break;
		case 3:
			// get my jobs
			fragment = new JobsFragment();
			transaction.replace(R.id.menuItemFragment, fragment, "jobs");
			transaction.commit();
			break;
		case 4:
			// recommend friend
			fragment = new RecommendFriendFragment();
			transaction.replace(R.id.menuItemFragment, fragment,
					"recommendfriend");
			transaction.commit();
			break;
		case 5:
			// contact us
			fragment = new ContactUsFragment();
			// fragment.setArguments(passBundle);
			transaction.replace(R.id.menuItemFragment, fragment, "cotactus");
			transaction.commit();
			break;
		case 6:
			// change password
			fragment = new ChangePasswordFragment();
			// fragment.setArguments(passBundle);
			transaction.replace(R.id.menuItemFragment, fragment, "changepwd");
			transaction.commit();
			break;
		case 7:

			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		friend_name = (EditText) findViewById(R.id.friend_name);
		friend_phone = (EditText) findViewById(R.id.friend_phone);
		friend_email = (EditText) findViewById(R.id.friend_email);
		friend_name_hint = (TextView) findViewById(R.id.friend_name_hint);
		friend_phone_hint = (TextView) findViewById(R.id.friend_phone_hint);
		friend_email_hint = (TextView) findViewById(R.id.friend_email_hint);
	}

	// back to main menu
	public void backToMainNenu(View view) {
		MainMenuItemActivity.this.finish();
	}

	// different operation depend on fragment type
	public void ownOperation(View view) {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// InputMethodManager imm = (InputMethodManager)
		// MainMenuItemActivity.this
		// .getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromInputMethod(MainMenuItemActivity.this
		// .getCurrentFocus().getWindowToken(), 0);
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (data != null) {
				Bundle bundle = data.getBundleExtra("sf");
				FriendSelected friendSelected = (FriendSelected) bundle
						.getSerializable("selectedFriend");
				friend_name.setText("");
				friend_phone.setText("");
				friend_email.setText("");
				friend_name_hint.setVisibility(View.VISIBLE);
				friend_phone_hint.setVisibility(View.VISIBLE);
				friend_email_hint.setVisibility(View.VISIBLE);
				friend_name.setText(friendSelected.getFirstName());
				friend_name_hint.setVisibility(View.GONE);
				if (friendSelected.getPhone().length() > 0) {
					friend_phone.setText(friendSelected.getPhone());
					friend_phone_hint.setVisibility(View.GONE);
				}
				if (friendSelected.getEmail().length() > 0) {
					friend_email.setText(friendSelected.getEmail());
					friend_email_hint.setVisibility(View.GONE);
				}
			}
			break;
		case 2:
			if (data != null) {
				Bundle bundle = data.getExtras();
				List<String> areasList = bundle.getStringArrayList("areasList");
				List<String> domainsList = bundle
						.getStringArrayList("domainsList");
				if (areasList.size() == 0 && domainsList.size() == 0) {

				} else {
					JobsFragment jobsFragment = new JobsFragment();
					jobsFragment.updateJobs(areasList, domainsList);
				}
			}
			break;
		default:
			break;
		}
	}

	public interface FilterJobsCallBack {
		public void updateJobs();
	}
}
