package com.restaurant.app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.restaurant.adapter.MenuAdapter;
import com.restaurant.adapter.MenuAdapter.AnimateOrderIcon;
import com.restaurant.controller.MenuController;
import com.restaurant.controller.MenuController.MenuDetailsFetch;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.util.ExceptionHandler;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;

public class MenuActivity extends Activity {

	View orderMenu;

	RelativeLayout imgBgLayout;
	TextView restaurantMyOrders;
	ImageView imageView;
	ListView listView;

	MenuController menuController;

	String itemCategory = "", offerItemIds = "";
	int offerId = -1;

	String actionBarTitle = "Slice";

	IDialog iDialog = new IDialog();

	Typeface font;

	public static int screenHeight = 0;

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
				.getHeight() / 1.3);

		font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");

		setContentView(R.layout.menu_list);

		imgBgLayout = (RelativeLayout) findViewById(R.id.imgBgLayout);

		imageView = (ImageView) findViewById(R.id.imgBg);
		imageView.setBackgroundResource(R.drawable.photo1);

		imgBgLayout.getLayoutParams().height = screenHeight + 50;
		imageView.getLayoutParams().height = screenHeight + 50;

		listView = (ListView) findViewById(R.id.listView1);

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				System.out.println("" + getScrollY());
				imgBgLayout.setTranslationY(-getScrollY() / 4);
			}
		});

		setupActionBar();

		iDialog.showProgress(this);

		System.out.println("MenuActivity " + IConstants.lol);

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		IConstants.appRestIp = sharedPreferences.getString("appRestIp", null);
		IConstants.appRestPort = sharedPreferences.getString("appRestPort",
				null);

		IConstants.appRestName = sharedPreferences.getString("appRestName",
				null);
		IConstants.appRestLocation = sharedPreferences.getString(
				"appRestLocation", null);

		System.out.println("MenuActivity " + IConstants.appRestIp + ", "
				+ IConstants.appRestPort);
		System.out.println("MenuActivity " + IConstants.appRestName + ", "
				+ IConstants.appRestLocation);

		menuController = new MenuController(this,
				menuDetailsFetchListener);
		menuController.getMenuDetails();
	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#55000000")));
		actionBar.setTitle("" + actionBarTitle);

		int titleId = getResources().getIdentifier("action_bar_title", "id",
				"android");
		TextView yourTextView = (TextView) findViewById(titleId);
		yourTextView.setTypeface(font);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem menuItem = menu.add(0, 0, 0, null).setIcon(
				R.drawable.ic_action_search);
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menuItem = menu.add(0, 1, 0, null).setIcon(R.drawable.ic_action_delete);
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		final MenuItem orderMenuItem = menuItem;

		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				orderMenu = findViewById(orderMenuItem.getItemId());

				// Toast.makeText(getApplicationContext(), "" +
				// orderMenuItem.getItemId(),
				// Toast.LENGTH_SHORT).show();
			}
		};

		handler.postDelayed(runnable, 1000);

		menuItem = menu.add(0, 2, 0, null).setIcon(
				R.drawable.ic_action_navigation_drawer);
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {

		switch (menuItem.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		case 0:

			break;

		case 1:

			break;

		case 2:

			// View menuItemView = findViewById(item.getItemId()); // SAME ID AS
			// MENU ID
			// PopupMenu popupMenu = new PopupMenu(this, menuItemView);
			// popupMenu.inflate(R.menu.activity_crossfade);
			// // ...
			// popupMenu.show();

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(menuItem);
	}

	public ArrayList<MenuDetails> removeMenuDups(ArrayList<MenuDetails> menuList) {
		Set<MenuDetails> noDups = new HashSet<MenuDetails>();
		noDups.addAll(menuList);
		return new ArrayList<MenuDetails>(noDups);
	}

	public int getScrollY() {
		View c = listView.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = listView.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = screenHeight - 100;
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	// ===========================MenuDetailsFetch==================================

	private MenuDetailsFetch menuDetailsFetchListener = new MenuDetailsFetch() {
		@SuppressWarnings("deprecation")
		public void onMenuDetailsFetch(String result) {

			// Toast.makeText(MenuActivity.this, "MenuControllerTC",
			// Toast.LENGTH_SHORT).show();
			IConstants.showToast("MenuDetailsFetch " + result);

			// ------------------------------- course --------------------------

			// course = "";

			try {

				if (itemCategory != "") {
					HomeActivity.menuAdapter = new MenuAdapter(
							MenuActivity.this, getWindowManager()
									.getDefaultDisplay().getWidth(),
							DatabaseManager.getInstance().getMenuDetailsDao()
									.queryBuilder().distinct().where()
									.eq("itemCategory", itemCategory).query(),
							animateOrderIconListener);
				} else if (offerId > 0) {

					System.err
							.println("==================================================");

					List<MenuDetails> menuDetailsList = new ArrayList<MenuDetails>();

					for (int i = 0; i < offerItemIds.split("/").length; i++) {
						String tempitem = offerItemIds.split("/")[i];

						MenuDetails menuDetail = (DatabaseManager.getInstance()
								.getMenuDetailsDao().queryBuilder().distinct()
								.where().in("itemId", tempitem.split("-")[0])
								.query()).get(0);

						menuDetail.setItemName(menuDetail.getItemName()
								+ " | ("
								+ Integer.parseInt(tempitem.split("-")[1])
								+ ")");
						menuDetail.setOfferQuantity(Integer.parseInt(tempitem
								.split("-")[1]));

						menuDetailsList.add(menuDetail);
					}

					System.out.println("menuDetailsList "
							+ menuDetailsList.size());

					// for (MenuDetails menuDetail : menuDetails) {
					// System.out.println(menuDetail.getItemName());
					// }

					System.err
							.println("==================================================");

					HomeActivity.menuAdapter = new MenuAdapter(
							MenuActivity.this, getWindowManager()
									.getDefaultDisplay().getWidth(),
							menuDetailsList, animateOrderIconListener);

				} else {
					HomeActivity.menuAdapter = new MenuAdapter(
							MenuActivity.this, getWindowManager()
									.getDefaultDisplay().getWidth(),
							DatabaseManager.getInstance().getMenuDetailsDao()
									.queryBuilder().distinct().query(),
							animateOrderIconListener);
				}

				listView.setAdapter(HomeActivity.menuAdapter);

			} catch (SQLException e) {
				e.printStackTrace();
			}

			// -----------------------------------------------------------------

			iDialog.dismissProgress();
		}
	};

	private AnimateOrderIcon animateOrderIconListener = new AnimateOrderIcon() {

		@Override
		public void animate() {
			try {

				ObjectAnimator animator = ObjectAnimator.ofFloat(orderMenu,
						"rotation", 0, -10);
				animator.setDuration(0);
				animator.start();

				animator = ObjectAnimator.ofFloat(orderMenu, "rotation", -10,
						10);
				animator.setRepeatCount(3);
				animator.setDuration(100);
				animator.start();
				animator.addListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator animation) {
					}

					@Override
					public void onAnimationRepeat(Animator animation) {
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						ObjectAnimator animator = ObjectAnimator.ofFloat(
								orderMenu, "rotation", -10, 0);
						animator.setDuration(0);
						animator.start();
					}

					@Override
					public void onAnimationCancel(Animator animation) {
					}
				});

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "" + e,
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			DatabaseManager.getInstance().closeHelper();
			// if (menuController != null)
			// menuController.cancel(true);
			menuController.cancelTasks();
		} catch (Exception e) {
			// TODO: MenuActivity handle exception
		}
	};
}
