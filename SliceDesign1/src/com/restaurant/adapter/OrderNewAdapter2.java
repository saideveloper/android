package com.restaurant.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.restaurant.app.ExpandableMenuActivity;
import com.restaurant.app.R;

public class OrderNewAdapter2 extends BaseExpandableListAdapter {

	private Activity activity;
	private ArrayList<Object> childtems;
	private LayoutInflater inflater;
	private ArrayList<String> parentItems, child;
	private ExpandableListView accordion;
	public int lastExpandedGroupPosition;
	
	Typeface font;

	public OrderNewAdapter2(Activity activity, ArrayList<String> parents,
			ArrayList<Object> childern, ExpandableListView accordion) {
		this.activity = activity;
		this.parentItems = parents;
		this.childtems = childern;
		this.accordion = accordion;
		
		font = Typeface.createFromAsset(activity.getAssets(),
				"Roboto-Regular.ttf");
	}

	public void setInflater(LayoutInflater inflater, Activity activity) {
		this.inflater = inflater;
		this.activity = activity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		child = (ArrayList<String>) childtems.get(groupPosition);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.child_order_list_item, null);
		}
		
		TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
		itemName.setText(child.get(childPosition));
		
		itemName.setTypeface(font);
		
		//TextView desc = (TextView) convertView.findViewById(R.id.desc);

		final ImageView layoutAdd = (ImageView) convertView
				.findViewById(R.id.layoutAdd);

		final View layoutAddRemoveDivider = (View) convertView
				.findViewById(R.id.layoutAddRemoveDivider);

		final ImageView layoutRemove = (ImageView) convertView
				.findViewById(R.id.layoutRemove);
		
		//final EditText chefNotes = (EditText) convertView.findViewById(R.id.chefNotes);
		
		LinearLayout chefNotesLayout = (LinearLayout) convertView.findViewById(R.id.chefNotesLayout);
		
		if(isLastChild){
			chefNotesLayout.setVisibility(View.VISIBLE);
		} else {
			chefNotesLayout.setVisibility(View.GONE);
		}

		layoutAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutAddRemoveDivider.setVisibility(View.VISIBLE);
				layoutRemove.setVisibility(View.VISIBLE);
			}
		});

		layoutRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutAddRemoveDivider.setVisibility(View.GONE);
				layoutRemove.setVisibility(View.GONE);
			}
		});

		// convertView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View view) {
		// Toast.makeText(activity, child.get(childPosition),
		// Toast.LENGTH_SHORT).show();
		// }
		// });

		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		// if (convertView == null) {
		// convertView = inflater.inflate(R.layout.parent_menu_list_item, null);
		// }
		//
		// ((CheckedTextView)
		// convertView).setText(parentItems.get(groupPosition));
		// ((CheckedTextView) convertView).setChecked(isExpanded);
		//
		// return convertView;

		System.out.println("groupPosition " + groupPosition + ", isExpanded "
				+ isExpanded);

		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.parent_order_list_item, null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.laptop);
		item.setText(parentItems.get(groupPosition));
		
		item.setTypeface(font);

		TextView itemCount = (TextView) convertView
				.findViewById(R.id.itemCount);
		itemCount.setVisibility(View.GONE);

		RelativeLayout imgBgLayout = (RelativeLayout) convertView
				.findViewById(R.id.imgBgLayout);

		ImageView imgBg = (ImageView) convertView.findViewById(R.id.imgBg);

		LinearLayout layoutListItem = (LinearLayout) convertView
				.findViewById(R.id.layoutListItem);
		layoutListItem.setBackgroundColor(Color.TRANSPARENT);
		
		RelativeLayout content = (RelativeLayout) convertView
				.findViewById(R.id.content);
		
		RelativeLayout innerContent = (RelativeLayout) convertView
				.findViewById(R.id.innerContent);

		LinearLayout spinner1Layout = (LinearLayout) convertView
				.findViewById(R.id.spinner1Layout);
		
		View divider = (View) convertView
				.findViewById(R.id.divider);
		
		ImageView groupIndicator = (ImageView) convertView
				.findViewById(R.id.groupIndicator);
		// int imageResourceId = isExpanded ? R.drawable.down : R.drawable.next;
//		int imageResourceId = isExpanded ? android.R.drawable.arrow_up_float
//				: android.R.drawable.arrow_down_float;
		int imageResourceId = isExpanded ? R.drawable.expander_close_holo_light : R.drawable.expander_open_holo_light;
		
		groupIndicator.setImageResource(imageResourceId);

		if (groupPosition == 0) {

			imgBgLayout.setVisibility(View.INVISIBLE);

			content.setVisibility(View.GONE);

			spinner1Layout.setVisibility(View.GONE);

			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			});

			try {
				android.widget.AbsListView.LayoutParams layoutParams = new android.widget.AbsListView.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				layoutListItem.setLayoutParams(layoutParams);
				System.out.println("layoutListItem " + layoutListItem);
				System.out.println("layoutListItem.getLayoutParams().height " + layoutListItem.getLayoutParams().height);
				System.out.println("ExpandableMenuActivity.screenHeight " + ExpandableMenuActivity.screenHeight);
				layoutListItem.getLayoutParams().height = ExpandableMenuActivity.screenHeight;
				imgBgLayout.getLayoutParams().height = ExpandableMenuActivity.screenHeight;
				imgBg.getLayoutParams().height = ExpandableMenuActivity.screenHeight;
			} catch (Exception e) {
				e.printStackTrace();
			}

//		} else if (groupPosition == 1) {
//			try {
//				layoutListItem.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
//			} catch (Exception e) {
//			}
//
//			imgBgLayout.setVisibility(View.GONE);
//
//			content.setVisibility(View.GONE);
//
//			spinner1Layout.setVisibility(View.VISIBLE);
//
//			Spinner spinner1 = (Spinner) convertView
//					.findViewById(R.id.spinner1);
//
//			List<String> list = new ArrayList<String>();
//			list.add("All Categories");
//			list.add("Chicken");
//			list.add("Mutton");
//			list.add("Mushroom");
//			list.add("Soup");
//			list.add("Curries");
//			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
//					activity, R.layout.spinner_list, list);
//			dataAdapter
//					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			spinner1.setAdapter(dataAdapter);
//
//			convertView.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//				}
//			});
		} else {

			try {
				layoutListItem.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
			} catch (Exception e) {
			}

			imgBgLayout.setVisibility(View.GONE);
			spinner1Layout.setVisibility(View.GONE);
			content.setVisibility(View.VISIBLE);

			if (groupPosition == 1) {
				innerContent.setBackgroundResource(R.drawable.corner_black);
				content.setPadding(5, 5, 5, 0);
			} else {
				innerContent.setBackgroundResource(R.drawable.nocorner_black);
				content.setPadding(5, 0, 5, 0);
				
				if(groupPosition == getGroupCount()-1 && !isExpanded){
					content.setPadding(5, 0, 5, 5);
					innerContent.setBackgroundResource(R.drawable.corner_black_bottom);
					divider.setVisibility(View.GONE);
				} else {
					divider.setVisibility(View.VISIBLE);
				}
			}
		}

		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<String>) childtems.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return parentItems.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);

		if (groupPosition != lastExpandedGroupPosition) {
			accordion.collapseGroup(lastExpandedGroupPosition);
		}

		lastExpandedGroupPosition = groupPosition;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
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
