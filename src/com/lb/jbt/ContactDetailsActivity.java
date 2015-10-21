package com.lb.jbt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lb.entities.Friend;

public class ContactDetailsActivity extends Activity implements
		View.OnClickListener {
	private final Integer REQUEST_CONTACTS_DETAILS_CODE = 2;
	private ImageView right_menu_btn;
	private ExpandableListView contact_details_lv;
	private Button confirm_contact_btn;
	// email and phone
	private List<String> detailsTitles = null;
	// email contents and phone contents
	private List<List<String>> detailsContents = null;
	private ContactDetailExpandableAdapter adapter = null;
	// passed friend
	private Friend passFriend = null;
	// selected phone number and email
	private String selectedPhone = "";
	private String selectedEmail = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_details);
		detailsTitles = new ArrayList<String>();
		detailsTitles.add(getResources().getString(R.string.phone));
		detailsTitles.add(getResources().getString(R.string.emial));
		detailsContents = new ArrayList<List<String>>();
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("friendBundle");
		passFriend = (Friend) bundle.get("passFriend");
		detailsContents.add(passFriend.getPhones());
		detailsContents.add(passFriend.getEmails());
		right_menu_btn = (ImageView) findViewById(R.id.right_menu_btn);
		contact_details_lv = (ExpandableListView) findViewById(R.id.contact_details_lv);
		right_menu_btn.setOnClickListener(this);
		confirm_contact_btn = (Button) findViewById(R.id.confirm_contact_btn);
		confirm_contact_btn.setOnClickListener(this);
		adapter = new ContactDetailExpandableAdapter(
				ContactDetailsActivity.this, detailsTitles, detailsContents);
		contact_details_lv.setAdapter(adapter);
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			contact_details_lv.expandGroup(i);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_menu_btn:
			ContactDetailsActivity.this.setResult(
					REQUEST_CONTACTS_DETAILS_CODE, null);
			ContactDetailsActivity.this.finish();
			break;
		case R.id.confirm_contact_btn:
			Intent data = new Intent();
			data.putExtra("selectedPhone", selectedPhone);
			data.putExtra("selectedEmail", selectedEmail);
			ContactDetailsActivity.this.setResult(
					REQUEST_CONTACTS_DETAILS_CODE, data);
			ContactDetailsActivity.this.finish();
			break;
		default:
			break;
		}
	}

	private class ContactDetailExpandableAdapter extends
			BaseExpandableListAdapter {
		private Context context;
		private List<String> titles;
		private List<List<String>> contents;
		private Map<Integer, Integer> checkedGroupItemMap = new HashMap<Integer, Integer>();

		public ContactDetailExpandableAdapter(Context context,
				List<String> titles, List<List<String>> contents) {
			super();
			this.context = context;
			this.titles = titles;
			this.contents = contents;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return contents.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			ChildViewHolder childViewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.contact_details_contents, null);
				childViewHolder = new ChildViewHolder();
				childViewHolder.content_item = (TextView) convertView
						.findViewById(R.id.content_item);
				childViewHolder.checked_btn = (CheckBox) convertView
						.findViewById(R.id.checked_btn);
				convertView.setTag(childViewHolder);
			} else {
				childViewHolder = (ChildViewHolder) convertView.getTag();
			}
			childViewHolder.content_item.setText(contents.get(groupPosition)
					.get(childPosition));
			final int gp = groupPosition;
			final int cp = childPosition;
			childViewHolder.checked_btn
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (checkedGroupItemMap.get(groupPosition) != null) {
								// click the checked check box
								if (checkedGroupItemMap.get(groupPosition) == childPosition) {
									checkedGroupItemMap.remove(gp);
								} else {
									// click unchecked check box should uncheck
									// the checked box
									ContactDetailExpandableAdapter.this
											.notifyDataSetChanged();
								}
							}
							checkedGroupItemMap.put(gp, cp);
							// 0 phone 1 email
							switch ((int) getGroupId(groupPosition)) {
							case 0:
								selectedPhone = contents.get(groupPosition)
										.get(childPosition);
								break;
							case 1:
								selectedEmail = contents.get(groupPosition)
										.get(childPosition);
								break;
							default:
								break;
							}
						}
					});
			if (checkedGroupItemMap.get(groupPosition) != null) {
				if (checkedGroupItemMap.get(groupPosition) == childPosition) {
					childViewHolder.checked_btn.setChecked(true);
				} else {
					childViewHolder.checked_btn.setChecked(false);
				}
			} else {
				childViewHolder.checked_btn.setChecked(false);
			}
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return contents.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return getGroup(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return titles.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.contact_details_title, null);
			}
			CheckedTextView ctv = (CheckedTextView) convertView
					.findViewById(R.id.detail_title_item);
			ImageView el_arrow = (ImageView) convertView
					.findViewById(R.id.el_arrow);
			if (isExpanded) {
				el_arrow.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.more_on));
			} else {
				el_arrow.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.more_off));
			}
			ctv.setText(titles.get(groupPosition));
			ctv.setClickable(isExpanded);
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
	}

	static class ChildViewHolder {
		TextView content_item;
		CheckBox checked_btn;
	}
}
