package com.restaurant.adapter;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.restaurant.app.MenuActivity;
import com.restaurant.app.MenuCategoryActivity;
import com.restaurant.app.R;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.util.IConstants;

public class MenuCategoryAdapter extends BaseAdapter {

	ImageView button;
	Context context;
	
	private static final String TAG = MenuCourseAdapter.class.getSimpleName();
	ArrayList<MenuDetails> listArray;

	public MenuCategoryAdapter(String itemCourse) {
		System.out.println("MenuCategoryAdapter called..");
		listArray = new ArrayList<MenuDetails>();
		try {
			listArray = (ArrayList<MenuDetails>) DatabaseManager.getInstance()
					.getMenuDetailsDao().queryBuilder().distinct()
					.selectColumns("itemCategory").where()
					.eq("course", itemCourse).query();
			System.out.println("listArray size " + listArray.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public int getCount() {
		return listArray.size(); // total number of elements in the list
	}

	@Override
	public Object getItem(int i) {
		return listArray.get(i); // single item in the list
	}

	@Override
	public long getItemId(int i) {
		return i; // index number
	}

	@Override
	public View getView(final int index, View view, final ViewGroup parent) {
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.simplerow, parent, false);
		}

		final MenuDetails menuDetails = listArray.get(index);

		TextView textView = (TextView) view.findViewById(R.id.rowTextView);
		textView.setText(menuDetails.getItemCategory());
		if (index % 2 == 0) {
			view.setBackgroundColor(Color.parseColor("#FF742E"));
		} else
			view.setBackgroundColor(Color.parseColor("#F05000"));

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(TAG, "string: " + menuDetails.getItemCategory());
				// Toast.makeText(parent.getContext(),
				// menuDetails.getItemCategory() + " clicked",
				// Toast.LENGTH_SHORT).show();
				// startActivity(new Intent(MenuCategoryActivity.this,
				// MenuActivity.class));
				Intent intent = new Intent(IConstants.mContext,
						MenuActivity.class);
				intent.putExtra("itemCategory", menuDetails.getItemCategory());
				// IConstants.appItemCategory = menuDetails.getItemCategory();
				IConstants.mContext.startActivity(intent);

				IConstants.mContext.overridePendingTransition(
						R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});

		return view;
	}
}
