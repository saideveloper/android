package com.restaurant.app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.j256.ormlite.table.TableUtils;
import com.restaurant.adapter.HomeAdapter;
import com.restaurant.adapter.MenuAdapter;
import com.restaurant.controller.LkupOrderStatusController;
import com.restaurant.controller.LkupOrderStatusController.LkupOrderStatusDetailsFetch;
import com.restaurant.controller.LkupPayTypeController;
import com.restaurant.controller.LkupPayTypeController.LkupPayTypeDetailsFetch;
import com.restaurant.controller.LkupPrepTypeController;
import com.restaurant.controller.LkupPrepTypeController.LkupPrepTypeDetailsFetch;
import com.restaurant.controller.LkupRestTaxController;
import com.restaurant.controller.LkupRestTaxController.LkupRestTaxDetailsFetch;
import com.restaurant.controller.LkupTaxListController;
import com.restaurant.controller.LkupTaxListController.LkupTaxListDetailsFetch;
import com.restaurant.controller.MenuController;
import com.restaurant.controller.MenuController.MenuDetailsFetch;
import com.restaurant.controller.RestAddonItemsController;
import com.restaurant.controller.RestAddonItemsController.RestAddonItemsDetailsFetch;
import com.restaurant.controller.RestItemTypeController;
import com.restaurant.controller.RestItemTypeController.RestItemTypeDetailsFetch;
import com.restaurant.controller.RestTimingController;
import com.restaurant.controller.RestTimingController.RestTimingDetailsFetch;
import com.restaurant.controller.RestaurantInfoController;
import com.restaurant.controller.RestaurantInfoController.RestaurantInfoDetailsFetch;
import com.restaurant.controller.TrxCodeController;
import com.restaurant.controller.TrxCodeController.TrxCodeDetailsFetch;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.ModifierQuestionAnswer;
import com.restaurant.model.OrderMasterDetails;
import com.restaurant.model.OrderMenuDetails;
import com.restaurant.model.OrderOfferDetails;
import com.restaurant.model.TrxCodeDetails;
import com.restaurant.model.lkup.LkupOrderStatusDetails;
import com.restaurant.util.ExceptionHandler;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;

public class HomeActivity extends Activity {

	RelativeLayout imgBgLayout, actionbarBg;
	TextView restaurantName, restaurantCity, actionBarTitle;
	ImageView imageView;
	View imgBgMask;
	ListView listView;

	TrxCodeController trxCodeController;
	RestItemTypeController restItemTypeController;
	RestaurantInfoController restaurantInfoController;
	RestTimingController restTimingController;
	LkupPayTypeController lkupPayTypeController;
	LkupPrepTypeController lkupPrepTypeController;
	LkupTaxListController lkupTaxListController;
	LkupRestTaxController lkupRestTaxController;
	RestAddonItemsController restAddonItemsController;
	LkupOrderStatusController lkupOrderStatusController;
	MenuController menuController;

	private DrawerLayout mDrawerLayout;
	private LinearLayout mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	IDialog iDialog = new IDialog();

	// String[] items = new String[] { "", "Menu", "Reservation",
	// "Deals & Offers", "Reviews and Ratings", "Photographs", "Info" };
	//
	// String[] desc = new String[] {
	// "",
	// "Show restaurant menu details",
	// "Book your table now for your next event",
	// "Find the best deals and offers provided by restaurant",
	// "Overall Restaurant Rating: ##, Total Reviews: ##, Total Likes: ## {From Restaurant table}",
	// "Photos uploaded by Slice Team and Others", "Info" };
	//
	// String[] itemsColor = new String[] { "#00000000", "#ef4e36", "#265563",
	// "#faae1c", "#00a087", "#4c119a", "#ffffff" };

	String[] items = new String[] { "", "Menu", "Services", "Payment Method",
			"Buffet", "Map" };

	String[] desc = new String[] { "", "menu", "service", "pay", "", "address" };

	String[] itemsColor = new String[] { "#00000000", "#ef4e36", "#265563",
			"#faae1c", "#00a087", "#4c119a" };

	public static int screenWidth = 0, screenHeight = 0;

	public static ArrayList<MenuDetails> orderedMenuDetails = new ArrayList<MenuDetails>();
	public static ArrayList<MenuDetails> menuListArray;

	public static MenuAdapter menuAdapter;

	public static Map<String, ModifierQuestionAnswer> modifierAddedItems = new HashMap<String, ModifierQuestionAnswer>();
	public static HashMap<String, ModifierQuestionAnswer> modifierAddedItemsStack = new HashMap<String, ModifierQuestionAnswer>();

	Typeface font;

	@Override
	protected void onResume() {
		super.onResume();

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		DatabaseManager.init(this);

		IConstants.mContext = this;

		// iDialog = new IDialog();
	}

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

		screenWidth = (int) (getWindowManager().getDefaultDisplay().getWidth());
		screenHeight = (int) (getWindowManager().getDefaultDisplay()
				.getHeight() / 1.6);

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		IConstants.appRestName = sharedPreferences.getString("appRestName",
				null);

		setContentView(R.layout.home_list);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (LinearLayout) findViewById(R.id.left_drawer);

		font = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");

		// // ActionBarDrawerToggle ties together the the proper interactions
		// // between the sliding drawer and the action bar app icon
		// mDrawerToggle = new ActionBarDrawerToggle(
		// this, /* host Activity */
		// mDrawerLayout, /* DrawerLayout object */
		// R.drawable.ic_action_navigation_drawer, /* nav drawer image to
		// replace 'Up' caret */
		// R.string.drawer_open, /* "open drawer" description for accessibility
		// */
		// R.string.drawer_close /* "close drawer" description for accessibility
		// */
		// ) {
		// public void onDrawerClosed(View view) {
		// //getActionBar().setTitle(mTitle);
		// invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
		// }
		//
		// public void onDrawerOpened(View drawerView) {
		// //getActionBar().setTitle(mDrawerTitle);
		// invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
		// }
		// };

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		imgBgLayout = (RelativeLayout) findViewById(R.id.imgBgLayout);

		imageView = (ImageView) findViewById(R.id.imgBg);
		imageView.setBackgroundResource(R.drawable.bg_repeat2);

		imgBgMask = (View) findViewById(R.id.imgBgMask);

		restaurantName = (TextView) findViewById(R.id.restaurantName);
		restaurantCity = (TextView) findViewById(R.id.restaurantCity);

		StringBuilder sb = new StringBuilder("" + IConstants.appRestName);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));

		restaurantName.setText(sb.toString());

		sb = new StringBuilder("" + IConstants.appRestLocation);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));

		restaurantCity.setText(sb.toString());

		restaurantName.setTypeface(font);
		restaurantCity.setTypeface(font);

		imgBgLayout.getLayoutParams().height = screenHeight + 50;
		imageView.getLayoutParams().height = screenHeight + 50;
		imgBgMask.getLayoutParams().height = screenHeight + 50;

		listView = (ListView) findViewById(R.id.listView1);

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//System.out.println("" + getScrollY());
				imgBgLayout.setTranslationY(-getScrollY() / 4);

				try {
					
					if(getScrollY()<80){
						restaurantName.setTranslationY(-getScrollY() / 4);
						restaurantCity.setTranslationY(-getScrollY() / 4);
						
						restaurantName.setTranslationX(-getScrollY() / 6);
						restaurantCity.setTranslationX(-getScrollY() / 6);
						
						restaurantName.setAlpha(1f);
						restaurantCity.setAlpha(1f);
						actionBarTitle.setAlpha(0);
					} else if(getScrollY()<120){
						restaurantName.setAlpha(0);
						restaurantCity.setAlpha(0);
						actionBarTitle.setAlpha(1f);
					} 
					
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

		// setupActionBar();
		setupNewActionBar();

		iDialog.showProgress(this);

		trxCodeController = new TrxCodeController(this,
				trxCodeDetailsFetchListener);
		trxCodeController.execute();

		// final Timer t = new Timer();
		// t.schedule(new TimerTask() {
		//
		// @Override
		// public void run() {
		//
		// runOnUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// View decor = getWindow().getDecorView();
		// ViewGroup actionBar = (ViewGroup) decor
		// .findViewById(getResources().getIdentifier(
		// "action_bar", "id", "android"));
		// View v = actionBar.getChildAt(0);
		// ActionBar.LayoutParams p = new ActionBar.LayoutParams(
		// ViewGroup.LayoutParams.MATCH_PARENT,
		// ViewGroup.LayoutParams.MATCH_PARENT);
		// p.gravity = Gravity.CENTER;
		// v.setLayoutParams(p);
		// v.setBackgroundColor(Color.BLACK);
		//
		// }
		// });
		//
		// t.cancel();
		// }
		// }, 3000);

		try {

			System.err
					.println("================================================================");

			String string = "{\"m\":[{\"ItemName\":\"1\",\"price\":\"25\"},{\"ItemName\":\"2\",\"price\":\"25\"}]}";

			System.out.println(string);

			JSONObject jsonObject = new JSONObject(string);

			System.out.println(jsonObject);

			JSONArray jsonArray = jsonObject.getJSONArray("m");

			System.out.println(jsonArray);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject mu = jsonArray.getJSONObject(i);
				System.out.println(mu.get("ItemName"));
				System.out.println(mu.get("price"));
			}

			// TextView answer = (TextView)
			// convertView.findViewById(R.id.answer);
			// answer.setText("" + jsonObject.getJSONObject("ItemName"));
			//
			// TextView rupees = (TextView)
			// convertView.findViewById(R.id.rupees);
			// rupees.setText("" + jsonObject.getJSONObject("price"));

			System.err
					.println("================================================================");

			// Fragment fragment = new MainMenuFragment();
			// FragmentManager fragmentManager = getFragmentManager();
			// FragmentTransaction fragmentTransaction = fragmentManager
			// .beginTransaction();
			// fragmentTransaction.replace(R.id.fragment_place, fragment);
			// fragmentTransaction.commit();

			// FragmentManager fragmentManager = getFragmentManager();
			// FragmentTransaction fragmentTransaction =
			// fragmentManager.beginTransaction();
			//
			// MainMenuFragment mainMenuFragment = new MainMenuFragment();
			// fragmentTransaction.add(R.id.fragment_place, mainMenuFragment);
			//
			// fragmentTransaction.commit();

		} catch (JSONException e) {
			e.printStackTrace();
		}

//		try {
//			TableUtils.dropTable(DatabaseManager.getHelper()
//					.getConnectionSource(), OrderMasterDetails.class, true);
//			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
//					.getConnectionSource(), OrderMasterDetails.class);
//
//			TableUtils.dropTable(DatabaseManager.getHelper()
//					.getConnectionSource(), OrderMenuDetails.class, true);
//			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
//					.getConnectionSource(), OrderMenuDetails.class);
//
//			TableUtils.dropTable(DatabaseManager.getHelper()
//					.getConnectionSource(), OrderOfferDetails.class, true);
//			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
//					.getConnectionSource(), OrderOfferDetails.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	// private void setupActionBar() {
	// ActionBar actionBar = getActionBar();
	// // actionBar.setIcon(R.drawable.ic_transparent);
	// // actionBar.setIcon(R.drawable.ic_action_navigation_drawer);
	// actionBar.setDisplayHomeAsUpEnabled(false);
	//
	// actionBar.setBackgroundDrawable(new ColorDrawable(Color
	// .parseColor("#99000000")));
	// actionBar.setTitle("" + IConstants.appRestName);
	//
	// actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	//
	// LayoutInflater inflator = (LayoutInflater) this
	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// View actionBarView = inflator.inflate(R.layout.actionbar, null);
	// TextView title = (TextView) actionBarView.findViewById(R.id.title);
	// title.setText("" + IConstants.appRestName);
	// title.setTextSize(26);
	//
	// ImageView imgLeftMenu = (ImageView) actionBarView
	// .findViewById(R.id.home);
	// imgLeftMenu.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
	// mDrawerLayout.closeDrawer(mDrawerList);
	// } else {
	// mDrawerLayout.openDrawer(mDrawerList);
	// }
	// }
	// });
	//
	// int actionBarHeight = 0;
	//
	// // Calculate ActionBar height
	// TypedValue tv = new TypedValue();
	// if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
	// {
	// actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
	// getResources().getDisplayMetrics());
	// }
	//
	// // //get resources
	// // Resources r = getResources();
	// // float pxLeftMargin =
	// // TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
	// // r.getDisplayMetrics());
	// // float pxTopMargin =
	// // TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
	// // r.getDisplayMetrics());
	// // float pxRightMargin =
	// // TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
	// // r.getDisplayMetrics());
	// // float pxBottomMargin =
	// // TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
	// // r.getDisplayMetrics());
	//
	// // //get layout params...
	// // LinearLayout.LayoutParams params= new
	// // LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
	// // LayoutParams.MATCH_PARENT);
	// // //params.setMargins(Math.round(pxLeftMargin),
	// // Math.round(pxTopMargin), Math.round(pxRightMargin),
	// // Math.round(pxBottomMargin));
	// // params.setMargins(0, actionBarHeight, 0, 0);
	// //
	// // mDrawerList.setLayoutParams(params);
	//
	// // LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
	// // mDrawerList.getLayoutParams();
	// // params.setMargins(0, 50, 0, 0);
	// // mDrawerList.setLayoutParams(params);
	//
	// // setMargins(mDrawerList, 0, actionBarHeight, (int) (screenWidth -
	// // screenWidth/2.5), 0);
	// setMargins(mDrawerList, 0, actionBarHeight, 0, 0);
	//
	// mDrawerList.getLayoutParams().width = (int) (screenWidth / 2.5);
	//
	// actionBar.setCustomView(actionBarView);
	//
	// // getActionBarTitleView().setAlpha(0f);
	// }

	private void setupNewActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#00000000")));
		actionBar.setTitle("" + IConstants.appRestName);

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View actionBarView = inflator.inflate(R.layout.actionbar, null);
		actionBarTitle = (TextView) actionBarView.findViewById(R.id.title);
		// title.setText("" + IConstants.appName);
		StringBuilder sb = new StringBuilder("" + IConstants.appRestName);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));

		actionBarTitle.setText(sb.toString());
		//title.setText("");
		actionBarTitle.setTextSize(26);
		
		actionBarTitle.setAlpha(0f);

		actionbarBg = (RelativeLayout) actionBarView
				.findViewById(R.id.actionbarBg);

		// ImageView search = (ImageView)
		// actionBarView.findViewById(R.id.search);
		// Bitmap sourceBitmap = BitmapFactory.decodeResource(
		// getResources(), R.drawable.ic_action_search);
		// search.setImageBitmap(getRoundedCornerBitmap(sourceBitmap, 32));

		actionBar.setCustomView(actionBarView);
	}

	public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	// public void setMargins(View v, int l, int t, int r, int b) {
	// if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
	// ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v
	// .getLayoutParams();
	// p.setMargins(l, t, r, b);
	// v.requestLayout();
	// }
	// }

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

	// ===========================TrxCodeDetailsFetch==================================

	private TrxCodeDetailsFetch trxCodeDetailsFetchListener = new TrxCodeDetailsFetch() {
		public void onTrxCodeDetailsFetch(String result) {

			// IConstants.showToast("TrxCodeDetailsFetch " + result);

			try {
				List<TrxCodeDetails> trxCodeDetailsList;
				trxCodeDetailsList = DatabaseManager.getInstance()
						.getTrxCodeDetailsDao().queryForAll();
				for (TrxCodeDetails trxCodeDetail : trxCodeDetailsList) {
					// trxCodeDetail.setETag("xyz");
					IConstants.trxCode.put(trxCodeDetail.getTrxCode(),
							trxCodeDetail);

					System.out.println("trxCodeDetail.getETag() "
							+ trxCodeDetail.getETag());
				}

				restItemTypeController = new RestItemTypeController(
						getApplicationContext(),
						restItemTypeDetailsFetchListener);
				restItemTypeController.getRestItemTypeDetails();

			} catch (SQLException e) {
				e.printStackTrace();
			}

			// iDialog.dismissProgress();
		}
	};

	// ===========================RestItemTypeDetailsFetch==================================

	private RestItemTypeDetailsFetch restItemTypeDetailsFetchListener = new RestItemTypeDetailsFetch() {
		public void onRestItemTypeDetailsFetch(String result) {

			// IConstants.showToast("RestItemTypeDetailsFetch " + result);

			restaurantInfoController = new RestaurantInfoController(
					getApplicationContext(), restaurantInfoDetailsFetchListener);
			restaurantInfoController.getRestaurantInfoDetails();
		}
	};

	// ===========================RestaurantInfoDetailsFetch==================================

	private RestaurantInfoDetailsFetch restaurantInfoDetailsFetchListener = new RestaurantInfoDetailsFetch() {
		public void onRestaurantInfoDetailsFetch(String result) {

			// IConstants.showToast("RestaurantInfoDetailsFetch " + result);

			restTimingController = new RestTimingController(
					getApplicationContext(), restTimingDetailsFetchListener);
			restTimingController.getRestTimingDetails();
		}
	};

	// ===========================RestaurantInfoDetailsFetch==================================

	private RestTimingDetailsFetch restTimingDetailsFetchListener = new RestTimingDetailsFetch() {
		public void onRestTimingDetailsFetch(String result) {

			// IConstants.showToast("RestTimingDetailsFetch " + result);

			lkupPayTypeController = new LkupPayTypeController(
					getApplicationContext(), lkupPayTypeDetailsFetchListener);
			lkupPayTypeController.getLkupPayTypeDetails();
		}
	};

	// ===========================LkupPayTypeDetailsFetch==================================

	private LkupPayTypeDetailsFetch lkupPayTypeDetailsFetchListener = new LkupPayTypeDetailsFetch() {
		public void onLkupPayTypeDetailsFetch(String result) {

			// IConstants.showToast("LkupPayTypeDetailsFetch " + result);

			lkupPrepTypeController = new LkupPrepTypeController(
					getApplicationContext(), lkupPrepTypeDetailsFetchListener);
			lkupPrepTypeController.getLkupPrepTypeDetails();
		}
	};

	// ===========================LkupPrepTypeDetailsFetch==================================

	private LkupPrepTypeDetailsFetch lkupPrepTypeDetailsFetchListener = new LkupPrepTypeDetailsFetch() {
		public void onLkupPrepTypeDetailsFetch(String result) {

			// IConstants.showToast("LkupPrepTypeDetailsFetch " + result);

			restAddonItemsController = new RestAddonItemsController(
					getApplicationContext(), restAddonItemsDetailsFetchListener);
			restAddonItemsController.getRestAddonItemsDetails();
		}
	};

	// ===========================RestAddonItemsDetailsFetch==================================

	private RestAddonItemsDetailsFetch restAddonItemsDetailsFetchListener = new RestAddonItemsDetailsFetch() {
		public void onRestAddonItemsDetailsFetch(String result) {

			// IConstants.showToast("RestAddonItemsDetailsFetch " + result);

			lkupTaxListController = new LkupTaxListController(
					getApplicationContext(), lkupTaxListDetailsFetchListener);
			lkupTaxListController.getLkupTaxListDetails();

			// iDialog.dismissProgress();
		}
	};

	// ===========================LkupTaxListDetailsFetch==================================

	private LkupTaxListDetailsFetch lkupTaxListDetailsFetchListener = new LkupTaxListDetailsFetch() {
		public void onLkupTaxListDetailsFetch(String result) {

			// IConstants.showToast("LkupTaxListDetailsFetch " + result);

			lkupRestTaxController = new LkupRestTaxController(
					getApplicationContext(), lkupRestTaxDetailsFetchListener);
			lkupRestTaxController.getLkupRestTaxDetails();

			// iDialog.dismissProgress();
		}
	};

	// ===========================LkupRestTaxDetailsFetch==================================

	private LkupRestTaxDetailsFetch lkupRestTaxDetailsFetchListener = new LkupRestTaxDetailsFetch() {
		public void onLkupRestTaxDetailsFetch(String result) {

			// IConstants.showToast("LkupRestTaxDetailsFetch " + result);

			lkupOrderStatusController = new LkupOrderStatusController(getApplicationContext(),
					lkupOrderStatusDetailsFetchListener);
			lkupOrderStatusController.getLkupOrderStatusDetails();

			// iDialog.dismissProgress();
		}
	};

	// ===========================LkupOrderStatusDetailsFetch==================================

	private LkupOrderStatusDetailsFetch lkupOrderStatusDetailsFetchListener = new LkupOrderStatusDetailsFetch() {
		public void onLkupOrderStatusDetailsFetch(String result) {

			// IConstants.showToast("LkupRestTaxDetailsFetch " + result);

			menuController = new MenuController(getApplicationContext(),
					menuDetailsFetchListener);
			menuController.getMenuDetails();

			// iDialog.dismissProgress();
		}
	};

	// ===========================MenuDetailsFetch==================================

	private MenuDetailsFetch menuDetailsFetchListener = new MenuDetailsFetch() {

		@Override
		public void onMenuDetailsFetch(String result) {
			// System.out.println("MenuDetailsFetch " + result);
			// IConstants.showToast("MenuDetailsFetch " + result);

			HomeAdapter homeAdapter = new HomeAdapter(HomeActivity.this, items,
					desc, itemsColor);
			listView.setAdapter(homeAdapter);

			iDialog.dismissProgress();

			List<ModifierQuestionAnswer> modifierQuestionAnswerList;
			try {
				modifierQuestionAnswerList = DatabaseManager.getInstance()
						.getModifierQuestionAnswerDao().queryBuilder()
						.distinct().query();
				System.out.println("modifierQuestionAnswerList "
						+ modifierQuestionAnswerList.size());
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// startActivity(new Intent(HomeActivity.this,
			// MenuActivity.class));
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			DatabaseManager.getInstance().closeHelper();
			if (trxCodeController != null)
				trxCodeController.cancel(true);
			if (menuController != null)
				menuController.cancelTasks();
		} catch (Exception e) {
			// TODO: HomeActivity handle exception
		}
	};
}
