package com.restaurant.app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.restaurant.adapter.MenuExpandableCategoryAdapter;
import com.restaurant.adapter.MenuExpandableCategoryAdapter.AnimateOrderIcon;
import com.restaurant.adapter.MenuExpandableCategoryAdapter.CategoryAnimateOrderIcon;
import com.restaurant.adapter.MenuExpandableCategoryAdapter.SelectedItemCategory;
import com.restaurant.adapter.MenuExpandableCourseAdapter;
import com.restaurant.adapter.MenuExpandableCourseAdapter.CourseAnimateOrderIcon;
import com.restaurant.adapter.MenuExpandableCourseAdapter.SelectedCourse;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.OrderMenuDetails;
import com.restaurant.util.ExceptionHandler;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;

public class ExpandableMenuActivity extends Activity {

	RelativeLayout order, orderMenu;

	RelativeLayout imgBgLayout, actionbarBg;
	TextView restaurantMyOrders, orderMenuItemCount;
	ImageView imageView;
	View imgBgMask;
	Button reviewMyOrder;

	ExpandableListView expandableListView;

	String itemCategory = "", offerItemIds = "";
	int offerId = -1;

	String actionBarTitle = "Slice";

	public static IDialog iDialog = new IDialog();

	Typeface font;

	public static int screenHeight = 0;

	private ArrayList<String> parents = new ArrayList<String>();
	private ArrayList<Object> childern = new ArrayList<Object>();

	public static MenuExpandableCategoryAdapter menuExpandableCategoryAdapter;
	public static MenuExpandableCourseAdapter menuExpandableCourseAdapter;

	public static String whichAdapter = "course";

	public static boolean firsttime = true;
	public static int selectedItemCategoryPosition = 0;
	public static String selectedItemCategory = "All Categories";

	public static int selectedCoursePosition = 0;
	public static String selectedCourse = "All Courses";

	@Override
	protected void onResume() {
		super.onResume();

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		DatabaseManager.init(this);

		IConstants.mContext = this;

		//iDialog = new IDialog();
		
		animateOrderIcon();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		DatabaseManager.init(this);

		IConstants.mContext = this;

		// this is not really necessary as ExpandableListActivity contains an
		// ExpandableList
		setContentView(R.layout.expandable_menu_list);

		screenHeight = (int) (getWindowManager().getDefaultDisplay()
				.getHeight() / 1.6);

		font = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");

		expandableListView = (ExpandableListView) findViewById(R.id.menulist);

		imgBgLayout = (RelativeLayout) findViewById(R.id.imgBgLayout);

		imageView = (ImageView) findViewById(R.id.imgBg);
		imageView.setBackgroundResource(R.drawable.bg_repeat2);

		restaurantMyOrders = (TextView) findViewById(R.id.restaurantMyOrders);

		imgBgMask = (View) findViewById(R.id.imgBgMask);

		reviewMyOrder = (Button) findViewById(R.id.reviewMyOrder);

		imgBgLayout.getLayoutParams().height = screenHeight + 50;
		imageView.getLayoutParams().height = screenHeight + 50;

		imgBgMask.getLayoutParams().height = screenHeight + 50;

		expandableListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				imgBgLayout.setTranslationY(-getScrollY() / 4);

				restaurantMyOrders.setTranslationY(getScrollY() / 4);

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

		expandableListView.setDividerHeight(0);
		// expandableList.setGroupIndicator(null);
		expandableListView.setClickable(true);

		// setupActionBar();

		setupNewActionBar();

		reviewMyOrder.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(ExpandableMenuActivity.this,
						OrderNewActivity3.class));
			}
		});

		checkWhereToGo();
	}

	// private void setupActionBar() {
	// ActionBar actionBar = getActionBar();
	// actionBar.setDisplayHomeAsUpEnabled(true);
	//
	// actionBar.setBackgroundDrawable(new ColorDrawable(Color
	// .parseColor("#55000000")));
	// actionBar.setTitle("" + actionBarTitle);
	//
	// int titleId = getResources().getIdentifier("action_bar_title", "id",
	// "android");
	// TextView yourTextView = (TextView) findViewById(titleId);
	// yourTextView.setTypeface(font);
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
		View actionBarView = inflator.inflate(R.layout.menu_actionbar, null);
		TextView title = (TextView) actionBarView.findViewById(R.id.title);
		// title.setText("" + IConstants.appName);
		title.setText("Menu");
		title.setTextSize(30);
		title.setTypeface(font);

		title.setShadowLayer(2, 2, 2, Color.BLACK);

		title.setPadding(0, 0, 0, 5);

		actionbarBg = (RelativeLayout) actionBarView
				.findViewById(R.id.actionbarBg);

		ImageView homeup = (ImageView) actionBarView.findViewById(R.id.homeup);
		homeup.setPadding(0, 0, 0, 3);
		// ImageView home = (ImageView) actionBarView.findViewById(R.id.home);

		homeup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		order = (RelativeLayout) actionBarView.findViewById(R.id.order);
		orderMenu = (RelativeLayout) actionBarView.findViewById(R.id.orderMenu);
		orderMenuItemCount = (TextView) actionBarView
				.findViewById(R.id.orderMenuItemCount);

		order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(ExpandableMenuActivity.this,
						OrderNewActivity3.class));
			}
		});

		actionBar.setCustomView(actionBarView);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	//
	// MenuItem menuItem = menu.add(0, 0, 0, null).setIcon(
	// R.drawable.ic_action_search);
	// menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	//
	// menuItem = menu.add(0, 1, 0, null).setIcon(R.drawable.ic_action_delete);
	// menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	//
	// final MenuItem orderMenuItem = menuItem;
	//
	// final Handler handler = new Handler();
	// Runnable runnable = new Runnable() {
	//
	// @Override
	// public void run() {
	//
	// orderMenu = findViewById(orderMenuItem.getItemId());
	//
	// // Toast.makeText(getApplicationContext(), "" +
	// // orderMenuItem.getItemId(),
	// // Toast.LENGTH_SHORT).show();
	// }
	// };
	//
	// handler.postDelayed(runnable, 1000);
	//
	// menuItem = menu.add(0, 2, 0, null).setIcon(
	// R.drawable.abs__ic_menu_moreoverflow_normal_holo_dark);
	// menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	//
	// return true;
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {

		switch (menuItem.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		case 0:

			break;

		case 1:

			startActivity(new Intent(this, OrderNewActivity3.class));

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

	public int getScrollY() {
		View c = expandableListView.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = expandableListView.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = screenHeight - 100;
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	public void checkWhereToGo() {
		try {
			ArrayList<String> itemCategoryList = new ArrayList<String>();
			ArrayList<String> courseList = new ArrayList<String>();

			final List<MenuDetails> courses = DatabaseManager.getInstance()
					.getMenuDetailsDao().queryBuilder().distinct()
					.selectColumns("course").query();
			for (MenuDetails menuDetail : courses) {
				courseList.add(menuDetail.getCourse());
			}
			final List<MenuDetails> itemCategory = DatabaseManager
					.getInstance().getMenuDetailsDao().queryBuilder()
					.distinct().selectColumns("itemCategory").query();

			for (MenuDetails menuDetail : itemCategory) {
				itemCategoryList.add(menuDetail.getItemCategory());
			}
			itemCategoryList.removeAll(Arrays.asList(new Object[] { null,
					"null" }));
			if (courseList.size() > 0) {
				setCourseGroupParents(selectedCourse);
				whichAdapter = "course";
			} else if (itemCategoryList.size() > 0) {
				setItemCategoryGroupParents(selectedItemCategory);
				whichAdapter = "category";
			} else {
				IConstants.showToast("In Progress");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateADapter() {
		if (whichAdapter.equalsIgnoreCase("course")) {
			menuExpandableCourseAdapter.notifyDataSetChanged();
		} else if (whichAdapter.equalsIgnoreCase("category")) {
			menuExpandableCategoryAdapter.notifyDataSetChanged();
		} else {

		}
	}

	// ===========================setItemCategoryGroupParents==================================

	public void setItemCategoryGroupParents(String selectedItemCategory) {

		parents = new ArrayList<String>();
		childern = new ArrayList<Object>();

		parents.add("image");
		parents.add("category");

		ArrayList<MenuDetails> child = new ArrayList<MenuDetails>();
		childern.add(child);

		child = new ArrayList<MenuDetails>();
		childern.add(child);

		try {
			ArrayList<MenuDetails> itemCategories = new ArrayList<MenuDetails>();
			if (selectedItemCategory.equalsIgnoreCase("All Categories")) {
				itemCategories = (ArrayList<MenuDetails>) DatabaseManager
						.getInstance().getMenuDetailsDao().queryBuilder()
						.distinct().selectColumns("itemCategory").query();
			} else {
				itemCategories = (ArrayList<MenuDetails>) DatabaseManager
						.getInstance().getMenuDetailsDao().queryBuilder()
						.distinct().selectColumns("itemCategory").where()
						.eq("itemCategory", selectedItemCategory).query();
			}

			// System.out.println("course size " + courses.size());

			for (MenuDetails itemCategory : itemCategories) {
				parents.add("" + itemCategory.getItemCategory());

				child = new ArrayList<MenuDetails>();

				ArrayList<MenuDetails> menuDetails = (ArrayList<MenuDetails>) DatabaseManager
						.getInstance().getMenuDetailsDao().queryBuilder()
						.distinct().orderBy("itemName", true).where()
						.eq("itemCategory", itemCategory.getItemCategory())
						.query();

				ArrayList<String> distinctItemName = new ArrayList<String>();
				for (MenuDetails menuDetail : menuDetails) {
					if (!distinctItemName.contains(menuDetail.getItemName())) {
						distinctItemName.add(menuDetail.getItemName());

						menuDetail.setOrderedItemId(0);
						menuDetail.setQuantity(0);
						menuDetail.setYetToView(true);
						menuDetail.setFirstView(0);
						menuDetail.setToggleView(true);

						Bitmap sourceBitmap = BitmapFactory.decodeResource(
								getResources(), R.drawable.seafood);
						menuDetail
								.setItemImageCircle(getCroppedBitmap(sourceBitmap));

						sourceBitmap = BitmapFactory.decodeResource(
								getResources(), R.drawable.overlay_right_tick);
						menuDetail
								.setItemImageHoverCircle(getCroppedBitmap(sourceBitmap));

						menuDetail.setItemImageHover(View.GONE);

						child.add(menuDetail);
					}
				}

				childern.add(child);
			}

			firsttime = true;

			menuExpandableCategoryAdapter = new MenuExpandableCategoryAdapter(this, parents,
					childern, expandableListView,
					categoryAnimateOrderIconListener,
					selectedItemCategoryListener);

			menuExpandableCategoryAdapter
					.setInflater(
							(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
							this);
			expandableListView.setAdapter(menuExpandableCategoryAdapter);

			if (!selectedItemCategory.equalsIgnoreCase("All Categories")) {
				expandableListView.expandGroup(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ===========================setCourseGroupParents==================================

	public void setCourseGroupParents(String selectedCourse) {

		parents = new ArrayList<String>();
		childern = new ArrayList<Object>();

		parents.add("image");
		parents.add("course");

		ArrayList<MenuDetails> child = new ArrayList<MenuDetails>();
		childern.add(child);

		child = new ArrayList<MenuDetails>();
		childern.add(child);

		try {
			ArrayList<MenuDetails> itemCategories = new ArrayList<MenuDetails>();
			if (selectedCourse.equalsIgnoreCase("All Courses")) {
				itemCategories = (ArrayList<MenuDetails>) DatabaseManager
						.getInstance().getMenuDetailsDao().queryBuilder()
						.orderBy("itemCategory", true).groupBy("itemCategory")
						.query();
			} else {
				itemCategories = (ArrayList<MenuDetails>) DatabaseManager
						.getInstance().getMenuDetailsDao().queryBuilder()
						.orderBy("itemCategory", true).groupBy("itemCategory")
						.where().eq("course", selectedCourse).query();
			}

			for (MenuDetails itemCategory : itemCategories) {
				parents.add("" + itemCategory.getItemCategory());

				child = new ArrayList<MenuDetails>();

				ArrayList<MenuDetails> menuDetails = new ArrayList<MenuDetails>();

				if (selectedCourse.equalsIgnoreCase("All Courses")) {
					menuDetails = (ArrayList<MenuDetails>) DatabaseManager
							.getInstance().getMenuDetailsDao().queryBuilder()
							.distinct().orderBy("itemName", true).where()
							.eq("itemCategory", itemCategory.getItemCategory())
							.query();
				} else {
					menuDetails = (ArrayList<MenuDetails>) DatabaseManager
							.getInstance().getMenuDetailsDao().queryBuilder()
							.distinct().orderBy("itemName", true).where()
							.eq("itemCategory", itemCategory.getItemCategory())
							.and().eq("course", selectedCourse).query();
				}

				ArrayList<String> distinctItemName = new ArrayList<String>();
				for (MenuDetails menuDetail : menuDetails) {
					if (!distinctItemName.contains(menuDetail.getItemName())) {
						distinctItemName.add(menuDetail.getItemName());

						menuDetail.setOrderedItemId(0);
						menuDetail.setQuantity(0);
						menuDetail.setYetToView(true);
						menuDetail.setFirstView(0);
						menuDetail.setToggleView(true);

						Bitmap sourceBitmap = BitmapFactory.decodeResource(
								getResources(), R.drawable.seafood);
						menuDetail
								.setItemImageCircle(getCroppedBitmap(sourceBitmap));

						sourceBitmap = BitmapFactory.decodeResource(
								getResources(), R.drawable.overlay_right_tick);
						menuDetail
								.setItemImageHoverCircle(getCroppedBitmap(sourceBitmap));

						menuDetail.setItemImageHover(View.GONE);

						child.add(menuDetail);
					}
				}

				childern.add(child);
			}

			firsttime = true;

			menuExpandableCourseAdapter = new MenuExpandableCourseAdapter(this,
					parents, childern, expandableListView,
					courseAnimateOrderIconListener, selectedCourseListener);

			menuExpandableCourseAdapter
					.setInflater(
							(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
							this);
			expandableListView.setAdapter(menuExpandableCourseAdapter);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ===========================selectedItemCategory==================================

	private SelectedItemCategory selectedItemCategoryListener = new SelectedItemCategory() {
		public void selectedItemCategory(String selectedItemCategory) {

			// IConstants.showToast("selectedItemCategory " +
			// selectedItemCategory);
			setItemCategoryGroupParents(selectedItemCategory);

		}
	};

	// ===========================selectedCourse==================================

	private SelectedCourse selectedCourseListener = new SelectedCourse() {
		public void selectedCourse(String selectedCourse) {

			// IConstants.showToast("selectedCourse " + selectedCourse);
			setCourseGroupParents(selectedCourse);

		}
	};

	// ===========================CourseAnimateOrderIcon==================================

	private CourseAnimateOrderIcon courseAnimateOrderIconListener = new CourseAnimateOrderIcon() {

		@Override
		public void animate() {
			animateOrderIcon();
		}
	};

	private CategoryAnimateOrderIcon categoryAnimateOrderIconListener = new CategoryAnimateOrderIcon() {

		@Override
		public void animate() {
			animateOrderIcon();
		}
	};

	private Bitmap getCroppedBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		// final RectF sOuterRect = new RectF(0, 0, bitmap.getWidth(),
		// bitmap.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		canvas.drawBitmap(bitmap, rect, rect, paint);

		// //
		// -----------------------------------------------------------------------------------------------------
		// colorFilter = new PorterDuffColorFilter(Color.parseColor("#3399cc"),
		// PorterDuff.Mode.OVERLAY);
		// paint.setColorFilter(colorFilter);
		//
		// Bitmap overlay = BitmapFactory.decodeResource(
		// IConstants.mContext.getResources(), R.drawable.ic_action_done);
		// //canvas = new Canvas(overlay);
		// //paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		// canvas.drawBitmap(overlay, 0, 0, paint);
		// //
		// -----------------------------------------------------------------------------------------------------

		// canvas.saveLayer(sOuterRect, paint, Canvas.ALL_SAVE_FLAG);

		// Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
		// return _bmp;
		return output;
	}
	

	public void animateOrderIcon() {
		try {

			ObjectAnimator animator = ObjectAnimator.ofFloat(orderMenu,
					"rotation", 0, -10);
			animator.setDuration(0);
			animator.start();

			animator = ObjectAnimator.ofFloat(orderMenu, "rotation", -10, 20);
			animator.setRepeatCount(2);
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
					// orderMenuItemCount
					try {
						List<OrderMenuDetails> orderMenuDetailsList = DatabaseManager
								.getInstance().getOrderMenuDetailsDao()
								.queryBuilder().distinct().query();
						int itemCount = 0;
						for (OrderMenuDetails orderMenuDetail : orderMenuDetailsList) {
							itemCount += orderMenuDetail.getQuantity();
						}
						orderMenuItemCount.setText("" + itemCount);
					} catch (Exception e) {
					}
					ObjectAnimator animator = ObjectAnimator.ofFloat(orderMenu,
							"rotation", -10, 0);
					animator.setDuration(0);
					animator.start();
				}

				@Override
				public void onAnimationCancel(Animator animation) {
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}