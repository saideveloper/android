package com.restaurant.adapter;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
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
import android.widget.Toast;

import com.restaurant.app.MenuCategoryActivity;
import com.restaurant.app.R;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.util.IConstants;

public class MenuCourseAdapter extends BaseAdapter {

	ImageView button;
	Context context;
	// String[] colorCodes = { "#F9C0B7", "#F7ADA1", "#F59889", "#F48370",
	// "#F26E58", "#F05B42", "#EF4E36", "#EE462A", "#EB3113", "#D32C11" };

	// String[] coloring = { "#D32C11", "#EB3113", "#EE462A", "#EF4E36",
	// "#F05B42", "#F26E58" };

	private static final String TAG = MenuCourseAdapter.class.getSimpleName();
	ArrayList<MenuDetails> listArray;

	public MenuCourseAdapter() {
		System.out.println("MenuCourseAdapter called..");
		listArray = new ArrayList<MenuDetails>();
		try {

			listArray = (ArrayList<MenuDetails>) DatabaseManager.getInstance()
					.getMenuDetailsDao().queryBuilder().distinct()
					.selectColumns("course").query();
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
		textView.setText(menuDetails.getCourse());
		// LayoutParams layoutParams = (LayoutParams)
		// textView.getLayoutParams();
		// int mRowHeight = layoutParams.MATCH_PARENT;
		// if (layoutParams == null) {
		// layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
		// mRowHeight);
		// } else {
		// layoutParams.height = mRowHeight;
		// }
		// view.setLayoutParams(layoutParams);
		getIntFromColor(197, 71, 71);

		// final LayoutParams params = (LayoutParams) view.getLayoutParams();
		// int mRowHeight = 80;
		// if (params == null) {
		// view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// mRowHeight));
		// } else {
		// params.height = mRowHeight;
		// }

		// if (index % 2 == 0) {
		// view.setBackgroundColor(Color.parseColor("#FF742E"));
		// } else
		// view.setBackgroundColor(Color.parseColor("#F05000"));
		// view.setBackgroundColor(Color.parseColor(coloring[index]));

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(TAG, "string: " + menuDetails.getItemCategory());
//				Toast.makeText(parent.getContext(),
//						menuDetails.getCourse() + " clicked",
//						Toast.LENGTH_SHORT).show();
				// startActivity(new Intent(MenuCourseAdapter.this,
				// MenuActivity.class));
				Intent intent = new Intent(IConstants.mContext,
						MenuCategoryActivity.class);
				intent.putExtra("course", menuDetails.getCourse());
				// IConstants.appItemCategory = menuDetails.getItemCategory();
				IConstants.mContext.startActivity(intent);
				IConstants.mContext.overridePendingTransition(
						R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});

		return view;
	}

	public int getIntFromColor(int red, int green, int blue) {
		System.out.println("getIntFromColor called ");

		int R = Math.round(255 * red);
		int G = Math.round(255 * green);
		int B = Math.round(255 * blue);

		R = (R << 16) & 0x00FF0000;
		G = (G << 8) & 0x0000FF00;
		B = B & 0x000000FF;

		return 0xFF000000 | R | G | B;
	}
}
