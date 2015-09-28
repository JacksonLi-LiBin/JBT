package com.lb.jbt;

import com.lb.fragments.ChangePasswordFragment;
import com.lb.fragments.ContactUsFragment;
import com.lb.fragments.CourseSylibusFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MainMenuItemActivity extends Activity {
	private FragmentManager manager;
	private FragmentTransaction transaction;

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
		// userId = bundle.getString("userId");
		// storedToken = bundle.getString("storedToken");
		Fragment fragment = null;
		// Bundle passBundle = new Bundle();
		// passBundle.putString("storedToken", storedToken);
		// passBundle.putString("userId", userId);
		switch (itemType) {
		case 0:
			fragment = new CourseSylibusFragment();
			transaction.replace(R.id.menuItemFragment, fragment,
					"coursesylibus");
			transaction.commit();
			break;
		case 1:

			break;
		case 2:

			break;
		case 3:

			break;
		case 4:

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

}
