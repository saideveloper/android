package com.restaurant.app;

import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.restaurant.adapter.OrderAdapter;
import com.restaurant.adapter.OrderNewAdapter;
import com.restaurant.controller.OrderController;
import com.restaurant.controller.OrderController.OrderDetailsPost;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.OrderMasterDetails;
import com.restaurant.model.OrderMenuDetails;
import com.restaurant.model.OrderOfferDetails;
import com.restaurant.util.ExceptionHandler;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;

public class OrderActivity extends Activity {
	ListView listView;
	OrderNewAdapter orderAdapter;

	OrderController orderController;
	IDialog iDialog = new IDialog();
	
	Typeface font;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		DatabaseManager.init(this);

		IConstants.mContext = this;
		
		font = Typeface.createFromAsset(getAssets(),
				"Roboto-Regular.ttf");

		setContentView(R.layout.order_list);

		listView = (ListView) findViewById(R.id.listView1);
		listView.setFastScrollEnabled(true);
		try {
			//orderAdapter = new OrderAdapter(this);
			orderAdapter = new OrderNewAdapter(this);
			listView.setAdapter(orderAdapter);
		} catch (Exception e) {
			System.err.println(e);
		}

		setupActionBar();
	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		// actionBar.setIcon(R.drawable.ic_transparent);
		// actionBar.setIcon(R.drawable.ic_action_navigation_drawer);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#55000000")));
		actionBar.setTitle("My Orders");
		
		int titleId = getResources().getIdentifier("action_bar_title", "id",
	            "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    //yourTextView.setTextColor(getResources().getColor(R.color.black));
	    yourTextView.setTypeface(font);

		// actionBar.setBackgroundDrawable(new ColorDrawable(Color
		// .parseColor("#000000")));

		// getActionBarTitleView().setAlpha(0f);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.start, menu);

		// Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		// ((View) configMI).startAnimation(shake);

		final MenuItem pay = menu.add(0, 0, 0, null).setIcon(
				R.drawable.ic_rupee);
		pay.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		pay.setVisible(false);

		final MenuItem cancel = menu.add(0, 1, 0, null).setIcon(
				R.drawable.ic_action_remove_holo_light);
		cancel.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		final MenuItem confirm = menu.add(0, 2, 0, null).setIcon(
				R.drawable.order_icon);
		confirm.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		pay.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				confirm.setVisible(true);
				cancel.setVisible(true);
				pay.setVisible(false);
				return false;
			}
		});

		cancel.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// confirm.setVisible(false);
				// cancel.setVisible(false);
				// pay.setVisible(true);

//				int count = orderAdapter.getCount();
//				for (int i = count - 1; i >= 0; i--) {
//					orderAdapter.removeItem(i);
//				}
				
				try {
					DeleteBuilder<OrderMenuDetails, Integer> orderMenuDeleteBuilder = DatabaseManager
							.getInstance().getOrderMenuDetailsDao().deleteBuilder();
					orderMenuDeleteBuilder.delete();
					orderAdapter.notifyDataSetChanged();
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return false;
			}
		});

		confirm.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// confirm.setVisible(false);
				// cancel.setVisible(false);
				// pay.setVisible(true);

				ArrayList<OrderMenuDetails> itemList = new ArrayList<OrderMenuDetails>();
				ArrayList<OrderOfferDetails> offerList = new ArrayList<OrderOfferDetails>();

				int count = orderAdapter.getCount();
				for (int i = 0; i < count; i++) {
					MenuDetails menuDetails = (MenuDetails) orderAdapter
							.getItem(i);
					OrderMasterDetails orderMasterDetail = menuDetails
							.getOrderMasterDetails();

					OrderMenuDetails orderMenuDetail = new OrderMenuDetails();
					orderMenuDetail.setItemId(orderMasterDetail.getItemId());
					orderMenuDetail.setQuantity(orderMasterDetail.getQuantity());
					orderMenuDetail.setPrice(orderMasterDetail.getPrice());
					orderMenuDetail.setChefNote(orderMasterDetail
							.getChefnotes());
					orderMenuDetail.setAllergy("");
					orderMenuDetail.setCookTime(menuDetails.getPrepTime());
					orderMenuDetail.setCourse(menuDetails.getCourse());
					orderMenuDetail.setOrderType("");

					itemList.add(orderMenuDetail);
				}

				OrderMasterDetails orderMasterDetails = new OrderMasterDetails();
				orderMasterDetails.setOrderNo(0);
				orderMasterDetails.setOrderMenuDetails(itemList);
				orderMasterDetails.setOrderOfferDetails(offerList);

				try {
					Gson gson = new GsonBuilder().excludeFieldsWithModifiers(
							Modifier.TRANSIENT).create();

					String json = gson.toJson(orderMasterDetails);
					// System.out.println(json);
					JSONObject jsonObject = new JSONObject();
					JSONArray jsonArray = new JSONArray();

					jsonArray.put(new JSONObject(json));
					jsonObject.put("orderMasterDetails", jsonArray);
					jsonObject = new JSONObject(jsonObject.toString());
					System.out.println(jsonObject.toString());
					
//					iDialog.showProgress(OrderActivity.this);
					
//					orderController = new OrderController(getApplicationContext(), orderDetailsPostListener);
//					orderController.postOrderDetails(jsonObject);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return false;
			}
		});

		MenuItem settings = menu.add(0, 3, 0, null).setIcon(
				R.drawable.abs__ic_menu_moreoverflow_normal_holo_light);
		settings.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		case 0:

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	// ===========================OrderDetailsPost==================================

	private OrderDetailsPost orderDetailsPostListener = new OrderDetailsPost() {
		public void onOrderDetailsPost(String result) {

			IConstants.showToast("OrderDetailsPost " + result);

			iDialog.dismissProgress();
		}
	};
}