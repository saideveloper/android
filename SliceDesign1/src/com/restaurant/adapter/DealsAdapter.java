package com.restaurant.adapter;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.restaurant.app.MenuActivity;
import com.restaurant.app.R;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.OfferDetails;
import com.restaurant.util.IConstants;

public class DealsAdapter extends BaseAdapter {

	ImageView button;
	Context context;

	private static final String TAG = MenuCategoryAdapter.class.getSimpleName();
	ArrayList<OfferDetails> listArray;
	ArrayList<MenuDetails> menuArrayList;
	ArrayList<String> listSelected = new ArrayList<String>();

	public DealsAdapter() {
		System.out.println("DealsAdapter called..");
		listArray = new ArrayList<OfferDetails>();
		try {

			// listArray = (ArrayList<OfferDetails>)
			// DatabaseManager.getInstance()
			// .getofferDetailsDao().queryBuilder().distinct().selectColumns("category")
			// .query();

			listArray = (ArrayList<OfferDetails>) DatabaseManager.getInstance()
					.getofferDetailsDao().queryBuilder().distinct().where()
					.eq("category", "Deals").query();

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

		final OfferDetails offerDetails = listArray.get(index);

		TextView textView = (TextView) view.findViewById(R.id.rowTextView);
		textView.setText(offerDetails.getTitle());

		if (index % 2 == 0) {
			view.setBackgroundColor(Color.parseColor("#FFBE28"));
		} else
			view.setBackgroundColor(Color.parseColor("#EAA400"));

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(TAG, "string: " + offerDetails.getCategory());
				Toast.makeText(
						parent.getContext(),
						offerDetails.getTitle() + ", offerId "
								+ offerDetails.getOfferId() + ", itemId "
										+ offerDetails.getItemId() + " clicked",
						Toast.LENGTH_SHORT).show();

				// startActivity(new Intent(MenuCategoryActivity.this,
				// MenuActivity.class));

				// MenuNewController menuNewController = new MenuNewController(
				// context, menuDetailsFetch);
				// menuNewController.getMenuDetails();
				//
				// try {
				// menuArrayList = (ArrayList<MenuDetails>) DatabaseManager
				// .getInstance().getMenuDetailsDao().queryBuilder()
				// .distinct().where().eq("itemId","").query();
				// } catch (SQLException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				
				Intent intent = new Intent(IConstants.mContext,
						MenuActivity.class);
				
				Bundle extras = new Bundle();
				extras.putInt("offerId", offerDetails.getOfferId());
				extras.putString("title", offerDetails.getTitle());
				extras.putString("itemId", offerDetails.getItemId());
				intent.putExtras(extras);
				
				IConstants.mContext.startActivity(intent);
				
				IConstants.mContext.overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		return view;
	}

	// private MenuDetailsFetch menuDetailsFetch = new MenuDetailsFetch() {
	//
	// @Override
	// public void onTaskCompleted(List<MenuDetails> menuDetails) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// };
}