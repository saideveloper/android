package com.restaurant.app;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.restaurant.adapter.DealsAdapter;
import com.restaurant.controller.OfferController;
import com.restaurant.controller.OfferController.OfferDetailsFetch;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.util.ExceptionHandler;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;

public class DealsOffersActivity extends Activity {

	IDialog iDialog = new IDialog();
	ListView listview;
	LayoutInflater layoutInflater;
	RelativeLayout relativeLayout;
	
	OfferController offerController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		DatabaseManager.init(this);
		IConstants.mContext = this;
		
		setContentView(R.layout.activity_deals_offers);
		
		iDialog.showProgress(this);

		offerController = new OfferController(
				DealsOffersActivity.this, offerDetailsFetchListener);
		offerController.getOfferDetails();

		relativeLayout = (RelativeLayout) findViewById(R.id.dealsofferMainlayout);
		listview = (ListView) findViewById(R.id.dealsListView);
		
		setupActionBar();
	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		// actionBar.setIcon(R.drawable.ic_transparent);
		// actionBar.setIcon(R.drawable.ic_action_navigation_drawer);
		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#55000000")));
		actionBar.setTitle("Menus | Deals & Offers");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuItem menuItem = menu.add(0, 0, 0, null).setIcon(
				R.drawable.abs__ic_menu_moreoverflow_normal_holo_dark);
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

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

	// ===========================offerDetailsControllerTC==================================

	private OfferDetailsFetch offerDetailsFetchListener = new OfferDetailsFetch() {
		@Override
		public void onOfferDetailsFetch(String result) {
			
			IConstants.showToast("OfferDetailsFetch " + result);
			
			DealsAdapter adapter = new DealsAdapter();
			listview.setAdapter(adapter);
			iDialog.dismissProgress();
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			DatabaseManager.getInstance().closeHelper();
			offerController.cancelTasks();
		} catch (Exception e) {
			// TODO: MenuActivity handle exception
		}
	};
}
