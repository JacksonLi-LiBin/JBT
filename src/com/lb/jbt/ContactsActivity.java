package com.lb.jbt;

import java.util.ArrayList;
import java.util.List;

import com.lb.entities.Friend;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * get user contacts
 * 
 * @author jacks
 * 
 */
public class ContactsActivity extends Activity implements View.OnClickListener {
	private final Integer REQUEST_CONTACTS_CODE = 1;
	private final Integer REQUEST_CONTACTS_DETAILS_CODE = 2;
	// contact type
	private String contact_type = "";
	private ImageView right_menu_btn;
	private ListView contacts_list;
	private ContactsAdapter adapter = null;
	private List<Friend> friendsList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("pass_bundle");
		contact_type = bundle.getString("contact_type");
		friendsList = new ArrayList<Friend>();
		right_menu_btn = (ImageView) findViewById(R.id.right_menu_btn);
		contacts_list = (ListView) findViewById(R.id.contacts_list);

		right_menu_btn.setOnClickListener(this);
		// get contacts
		if (contact_type != null && !contact_type.equals("")) {
			if (contact_type.equals("local")) {
				friendsList = getLocalContacts();
			} else if (contact_type.equals("google")) {

			} else if (contact_type.equals("outlook")) {

			}
			adapter = new ContactsAdapter(friendsList, ContactsActivity.this);
			contacts_list.setAdapter(adapter);
		}
		contacts_list
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(ContactsActivity.this,
								ContactDetailsActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("passFriend",
								friendsList.get(arg2));
						intent.putExtra("friendBundle", bundle);
						ContactsActivity.this.startActivityForResult(intent,
								REQUEST_CONTACTS_DETAILS_CODE);
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_menu_btn:
			ContactsActivity.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class ContactsAdapter extends BaseAdapter {
		private List<Friend> friends = null;
		private Context context;

		public ContactsAdapter(List<Friend> friends, Context context) {
			super();
			this.friends = friends;
			this.context = context;
		}

		@Override
		public int getCount() {
			return friends.size();
		}

		@Override
		public Object getItem(int position) {
			return friends.get(position);
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
				view = LayoutInflater.from(context).inflate(
						R.layout.contacts_list, null);
			}
			TextView friend_name = (TextView) view
					.findViewById(R.id.friend_name);
			Friend friend = friends.get(position);
			friend_name.setText(friend.getFirstName());
			return view;
		}

	}

	private List<Friend> getLocalContacts() {
		List<Friend> friends = new ArrayList<Friend>();
		Uri uri = Uri.parse("content://com.android.contacts/contacts");// get
																		// all
																		// contacts
																		// data
		ContentResolver resolver = ContactsActivity.this.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { "_id" }, null, null,
				"sort_key asc");
		Friend friend = null;
		while (cursor.moveToNext()) {
			friend = new Friend();
			int contactsId = cursor.getInt(0);
			StringBuilder sb = new StringBuilder("contactsId:");
			sb.append(contactsId);
			uri = Uri.parse("content://com.android.contacts/contacts/"
					+ contactsId + "/data");// get
											// one
											// contact
											// data
			Cursor dataCursor = resolver.query(uri, new String[] { "mimetype",
					"data1", "data2" }, null, null, null);
			List<String> emails = new ArrayList<String>();
			List<String> phones = new ArrayList<String>();
			while (dataCursor.moveToNext()) {
				String data = dataCursor.getString(dataCursor
						.getColumnIndex("data1"));
				String type = dataCursor.getString(dataCursor
						.getColumnIndex("mimetype"));
				if ("vnd.android.cursor.item/name".equals(type)) {
					sb.append(", name:" + data);
					friend.setFirstName(data);
					friend.setLastName(data);
				} else if ("vnd.android.cursor.item/email_v2".equals(type)) {
					sb.append(", email:" + data);
					emails.add(data);
				} else if ("vnd.android.cursor.item/phone_v2".equals(type)) {
					sb.append(", phone:" + data);
					phones.add(data);
				}
			}
			friend.setEmails(emails);
			friend.setPhones(phones);
			friends.add(friend);
		}
		return friends;
	}
}
