package com.restaurant.app;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.restaurant.adapter.OrderNewAdapter2;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.util.ExceptionHandler;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;

public class OrderNewActivity extends Activity {

	RelativeLayout imgBgLayout, actionbarBg;
	TextView restaurantName, restaurantCity;
	ImageView imageView;
	View imgBgMask;

	ExpandableListView expandableList;

	Typeface font;
	
	Button confirmOrder, cancelOrder, payBill;

	IDialog iDialog = new IDialog();

	public static int screenWidth = 0, screenHeight = 0;

	private ArrayList<String> parentItems = new ArrayList<String>();
	private ArrayList<Object> childItems = new ArrayList<Object>();

	@SuppressWarnings("deprecation")
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

		screenHeight = (int) (getWindowManager().getDefaultDisplay()
				.getHeight() / 1.6);

		// this is not really necessary as ExpandableListActivity contains an
		// ExpandableList
		setContentView(R.layout.order_list_new);

		font = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");

		expandableList = (ExpandableListView) findViewById(R.id.orderlist);

		imgBgLayout = (RelativeLayout) findViewById(R.id.imgBgLayout);

		imageView = (ImageView) findViewById(R.id.imgBg);
		imageView.setBackgroundResource(R.drawable.bg_repeat2);

		imgBgMask = (View) findViewById(R.id.imgBgMask);

		imgBgLayout.getLayoutParams().height = screenHeight + 50;
		imageView.getLayoutParams().height = screenHeight + 50;

		imgBgMask.getLayoutParams().height = screenHeight + 50;
		
		confirmOrder = (Button) findViewById(R.id.confirmOrder);
		cancelOrder = (Button) findViewById(R.id.cancelOrder);
		payBill = (Button) findViewById(R.id.payBill);
		
		confirmOrder.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						OrderNewActivity.this);

				// set title
				alertDialogBuilder.setTitle("Thank You");

				// set dialog message
				alertDialogBuilder.setMessage("Your Order #### is Successfully placed")
						.setCancelable(false)
						.setNeutralButton("Thanks", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
				
				confirmOrder.setVisibility(View.GONE);
				cancelOrder.setVisibility(View.GONE);
				payBill.setVisibility(View.VISIBLE);
			}
		});
		
		payBill.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//iDialog.paybill(OrderNewActivity.this);
				
				confirmOrder.setVisibility(View.VISIBLE);
				cancelOrder.setVisibility(View.VISIBLE);
				payBill.setVisibility(View.GONE);
			}
		});

		expandableList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				imgBgLayout.setTranslationY(-getScrollY() / 4);
				
				try {
					int c = (getScrollY() > 200 ? (getScrollY() - 210) : 0);
					c = c / 2;
					// System.out.println("c " + c);
					c = c > 75 ? 75 : c;
					String color = "#00000000";

					color = c > 0 ? "#" + (c > 9 ? c : "0" + c) + "000000"
							: "#00000000";

					// System.out.println("color " + color);

					actionbarBg.setBackgroundColor(Color.parseColor(color));
				} catch (Exception e) {
					// System.err.println(e);
				}
			}
		});

		expandableList.setDividerHeight(0);
		// expandableList.setGroupIndicator(null);
		expandableList.setClickable(true);

		//setupActionBar();
		
		setupNewActionBar();

		setGroupParents();
		setChildData();

		OrderNewAdapter2 adapter = new OrderNewAdapter2(this, parentItems,
				childItems, expandableList);

		adapter.setInflater(
				(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
				this);
		expandableList.setAdapter(adapter);
	}

//	private void setupActionBar() {
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
//
//		actionBar.setBackgroundDrawable(new ColorDrawable(Color
//				.parseColor("#55000000")));
//		actionBar.setTitle("" + IConstants.appName);
//	}
	
	private void setupNewActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#00000000")));
		actionBar.setTitle("" + IConstants.appRestName);

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View actionBarView = inflator.inflate(R.layout.order_actionbar, null);
		TextView title = (TextView) actionBarView.findViewById(R.id.title);
		//title.setText("" + IConstants.appName);
		title.setText("My Orders");
		title.setTextSize(26);
		title.setTypeface(font);
		
		title.setShadowLayer(2, 2, 2, Color.BLACK);

		actionbarBg = (RelativeLayout) actionBarView
				.findViewById(R.id.actionbarBg);

		ImageView homeup = (ImageView) actionBarView.findViewById(R.id.homeup);
		//ImageView home = (ImageView) actionBarView.findViewById(R.id.home);

		homeup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		title.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) { 
				finish();
			}
		});
		
		ImageView quicmenu = (ImageView) actionBarView.findViewById(R.id.quicmenu);
		
		quicmenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) { 
				
			}
		});

		actionBar.setCustomView(actionBarView);
	}

	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		
//		MenuItem search = menu.add(0, 0, 0, null).setIcon(
//				R.drawable.ic_action_search);
//		search.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//		
//		MenuItem settings = menu.add(0, 1, 0, null).setIcon(
//				R.drawable.abs__ic_menu_moreoverflow_normal_holo_dark);
//		settings.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//
//		return true;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		case 0:
			
			//iDialog.paybill(OrderNewActivity.this);
			
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public int getScrollY() {
		View c = expandableList.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = expandableList.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = screenHeight - 100;
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	public void setGroupParents() {
		parentItems.add("");
		parentItems.add("Order #1");
		parentItems.add("Order #2");
	}

	public void setChildData() {

		ArrayList<String> child = new ArrayList<String>();
		childItems.add(child);

		child = new ArrayList<String>();
		child.add("Item 1");
		child.add("Item 2");
		childItems.add(child);
		
		child = new ArrayList<String>();
		child.add("Item 3");
		child.add("Item 4");
		childItems.add(child);
	}
}
