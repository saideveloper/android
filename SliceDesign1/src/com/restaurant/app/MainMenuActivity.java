package com.restaurant.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.TextView;

import com.restaurant.adapter.MenuSearchAdapter;
import com.restaurant.controller.MenuController;
import com.restaurant.controller.MenuController.MenuDetailsFetch;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.util.ExceptionHandler;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;

public class MainMenuActivity extends Activity {

	SearchView search;
	ListView listView;
	LinearLayout linear, parentLayout, mainLayout;
	Canvas c;
	Context context;
	ImageView menuIcon, reviewIcon, galleryIcon, reserveIcon, dealsIcon,
			settingIcon, aboutIcon, callwaiterIcon;
	TextView menuTextView, reviewTextView, galleryTextView, reserveTextView,
			dealsTextView, settingTextView, aboutTextView, callwaiterTextView;
	RelativeLayout dealsLayout, menuLayout, reviewLayout, reserveLayout,
			galleryLayout, settingLayout, aboutLayout, relativeListLayout,
			callwaiterLayout;
	LinearLayout linear1;
	Typeface font;
	private ArrayAdapter<String> listAdapter = null;

	IDialog iDialog = new IDialog();
	ArrayList<String> itemCategoryList = new ArrayList<String>();
	ArrayList<String> courseList = new ArrayList<String>();

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {

			super.onCreate(savedInstanceState);
			Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

			IConstants.mContext = this;

			setContentView(R.layout.main_menu);
			// mFlipper = ((ViewFlipper) this.findViewById(R.id.flipper));
			// mFlipper.startFlipping();
			search = (SearchView) findViewById(R.id.searchView);
			// linear = (LinearLayout) findViewById(R.id.mainLayout);
			parentLayout = (LinearLayout) findViewById(R.id.parentlayout);
			mainLayout = (LinearLayout) findViewById(R.id.mainMenuLayout);
			menuIcon = (ImageView) findViewById(R.id.menuImg);
			reviewIcon = (ImageView) findViewById(R.id.reviewImg);
			galleryIcon = (ImageView) findViewById(R.id.galleryImg);
			reserveIcon = (ImageView) findViewById(R.id.reserveImg);
			dealsIcon = (ImageView) findViewById(R.id.dealsImg);
			settingIcon = (ImageView) findViewById(R.id.settingImg);
			aboutIcon = (ImageView) findViewById(R.id.aboutImg);
			callwaiterIcon = (ImageView) findViewById(R.id.callwaiterImg);

			menuLayout = (RelativeLayout) findViewById(R.id.menulayout);
			reviewLayout = (RelativeLayout) findViewById(R.id.reviewlayout);
			galleryLayout = (RelativeLayout) findViewById(R.id.gallerylayout);
			reserveLayout = (RelativeLayout) findViewById(R.id.reservelayout);
			dealsLayout = (RelativeLayout) findViewById(R.id.dealslayout);
			settingLayout = (RelativeLayout) findViewById(R.id.settinglayout);
			aboutLayout = (RelativeLayout) findViewById(R.id.aboutlayout);
			callwaiterLayout = (RelativeLayout) findViewById(R.id.callwaiterlayout);

			menuTextView = (TextView) findViewById(R.id.menutxtid);
			reviewTextView = (TextView) findViewById(R.id.reviewtxtid);
			galleryTextView = (TextView) findViewById(R.id.gallerytxtid);
			reserveTextView = (TextView) findViewById(R.id.reservetxtid);
			dealsTextView = (TextView) findViewById(R.id.dealstxtid);
			settingTextView = (TextView) findViewById(R.id.settingtxtid);
			aboutTextView = (TextView) findViewById(R.id.abouttxtid);
			callwaiterTextView = (TextView) findViewById(R.id.callwaitertxtid);

			font = Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf");

			menuTextView.setTypeface(font);
			reviewTextView.setTypeface(font);
			galleryTextView.setTypeface(font);
			callwaiterTextView.setTypeface(font);
			reserveTextView.setTypeface(font);
			dealsTextView.setTypeface(font);
			settingTextView.setTypeface(font);
			aboutTextView.setTypeface(font);

			menuTextView.setTextSize(24);
			reviewTextView.setTextSize(24);
			galleryTextView.setTextSize(24);
			callwaiterTextView.setTextSize(24);
			reserveTextView.setTextSize(24);
			dealsTextView.setTextSize(24);
			settingTextView.setTextSize(24);
			aboutTextView.setTextSize(24);

			// final Animation anim = AnimationUtils.loadAnimation(this,
			// R.anim.fadein);
			// final Animation animation = AnimationUtils.loadAnimation(this,
			// R.anim.animation);

			search.setOnSearchClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						View child = getLayoutInflater().inflate(
								R.layout.activity_list_view, null);
						parentLayout.addView(child);
						parentLayout.removeView(mainLayout);
						listView = (ListView) findViewById(R.id.listview);
						// ArrayList<String> arrayList = new
						// ArrayList<String>();
						// arrayList.add("SouthIndian");
						// arrayList.add("NorthIndian");
						// arrayList.add("Chinese");
						// listAdapter = new ArrayAdapter<String>(
						// MainMenuActivity.this,
						// android.R.layout.simple_list_item_1, arrayList);
						// listAdapter = new ArrayAdapter<String>(
						// MainMenuActivity.this, R.layout.simplerow,
						// R.id.rowTextView, arrayList);
						MenuSearchAdapter menuSearchAdapter = new MenuSearchAdapter();
						listView.setAdapter(menuSearchAdapter);
					} catch (Exception ex) {
						System.out.println("Exception found in search"
								+ ex.getMessage());
					}
					// TODO Auto-generated method stub
				}
			});

			search.setOnCloseListener(new OnCloseListener() {

				@Override
				public boolean onClose() {

					try {
						// TODO Auto-generated method stub
						// Toast.makeText(getApplicationContext(),
						// "Search closed", Toast.LENGTH_LONG).show();

						// relativeListLayout = (RelativeLayout)
						// findViewById(R.id.relativeListView);
						LinearLayout linear1 = (LinearLayout) findViewById(R.id.relativeListView);
						parentLayout.removeView(linear1);
						parentLayout.addView(mainLayout);

					} catch (Exception e) {
						System.out.println("Onclose Exception "
								+ e.getMessage());
					}
					return false;
				}
			});

			menuLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Toast.makeText(getApplicationContext(), "Menu Clicked",
					// Toast.LENGTH_LONG).show();
					try {
						
						MenuController menuController = new MenuController(
								getApplicationContext(), menuDetailsFetch);
						menuController.getMenuDetails();
						
//						if (android.os.Build.VERSION.SDK_INT > 16) {
//							v.setDrawingCacheEnabled(true);
//							v.setPressed(false);
//							v.refreshDrawableState();
//							Bitmap bm = v.getDrawingCache();
//							Canvas c = new Canvas(bm);
//							// c.drawARGB(255, 255, 0, 0);
//							ActivityOptions opts = ActivityOptions
//									.makeThumbnailScaleUpAnimation(v, bm, 0, 0);
//
//							// startActivity(new Intent(MainMenuActivity.this,
//							// MenuCategoryActivity.class), opts
//							// .toBundle());
//							MenuNewController menuController = new MenuNewController(
//									getApplicationContext(), menuDetailsFetch);
//							menuController.getMenuDetails();
//							v.setDrawingCacheEnabled(false);
//						} else {
////							startActivity(new Intent(MainMenuActivity.this,
////									MenuCategoryActivity.class));
//						}
					} catch (Exception e) {
						System.out.println("Exception in Menulayout "
								+ e.getMessage());
					}
					// return false;
				}
			});

			// menuLayout.setOnTouchListener(new OnTouchListener() {
			//
			// @Override
			// public boolean onTouch(View v, MotionEvent event) {
			// // TODO Auto-generated method stub
			// animation(menuLayout, menuTextView, menuIcon);
			// try {
			// v.setDrawingCacheEnabled(true);
			// v.setPressed(false);
			// v.refreshDrawableState();
			// Bitmap bm = v.getDrawingCache();
			// Canvas c = new Canvas(bm);
			// // c.drawARGB(255, 255, 0, 0);
			// ActivityOptions opts = ActivityOptions
			// .makeThumbnailScaleUpAnimation(v, bm, 0, 0);
			// startActivity(new Intent(MainActivity.this,
			// MenuActivity.class), opts.toBundle());
			// v.setDrawingCacheEnabled(false);
			// } catch (Exception e) {
			// System.out.println("Exception in Munulayout "
			// + e.getMessage());
			// }
			// return false;
			// }
			// });

			reviewLayout.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					// animation(reviewLayout, reviewTextView, reviewIcon);
					if (android.os.Build.VERSION.SDK_INT > 16) {
						// Toast.makeText(getApplicationContext(),
						// "Reviews Clicked", Toast.LENGTH_LONG).show();
						v.setDrawingCacheEnabled(true);
						v.setPressed(false);
						v.refreshDrawableState();
						Bitmap bm = v.getDrawingCache();
						Canvas c = new Canvas(bm);
						// c.drawARGB(255, 255, 0, 0);
						ActivityOptions opts = ActivityOptions
								.makeThumbnailScaleUpAnimation(v, bm, 0, 0);
						startActivity(new Intent(MainMenuActivity.this,
								ReviewActivity.class), opts.toBundle());
						v.setDrawingCacheEnabled(false);
					}
					return false;
				}
			});

			galleryLayout.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					// animation(galleryLayout, galleryTextView, galleryIcon);
					if (android.os.Build.VERSION.SDK_INT > 16) {
						// Toast.makeText(getApplicationContext(),
						// "Galley Clicked", Toast.LENGTH_LONG).show();
						v.setDrawingCacheEnabled(true);
						v.setPressed(false);
						v.refreshDrawableState();
						Bitmap bm = v.getDrawingCache();
						Canvas c = new Canvas(bm);
						// c.drawARGB(255, 255, 0, 0);
						ActivityOptions opts = ActivityOptions
								.makeThumbnailScaleUpAnimation(v, bm, 0, 0);
						startActivity(new Intent(MainMenuActivity.this,
								GalleryActivity.class), opts.toBundle());
						v.setDrawingCacheEnabled(false);
					}
					return false;
				}
			});

			reserveLayout.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					// animation(reserveLayout, reserveTextView, reserveIcon);
					if (android.os.Build.VERSION.SDK_INT > 16) {
						v.setDrawingCacheEnabled(true);
						v.setPressed(false);
						v.refreshDrawableState();
						Bitmap bm = v.getDrawingCache();
						Canvas c = new Canvas(bm);
						// c.drawARGB(255, 255, 0, 0);
						ActivityOptions opts = ActivityOptions
								.makeThumbnailScaleUpAnimation(v, bm, 0, 0);
						startActivity(new Intent(MainMenuActivity.this,
								ReservationActivity.class), opts.toBundle());
						v.setDrawingCacheEnabled(false);
					}
					return false;
				}
			});
			dealsLayout.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					// animation(dealsLayout, dealsTextView, dealsIcon);
					if (android.os.Build.VERSION.SDK_INT > 16) {
						v.setDrawingCacheEnabled(true);
						v.setPressed(false);
						v.refreshDrawableState();
						Bitmap bm = v.getDrawingCache();
						Canvas c = new Canvas(bm);
						// c.drawARGB(255, 255, 0, 0);
						ActivityOptions opts = ActivityOptions
								.makeThumbnailScaleUpAnimation(v, bm, 0, 0);
						startActivity(new Intent(MainMenuActivity.this,
								DealsOffersActivity.class), opts.toBundle());
						v.setDrawingCacheEnabled(false);
					} else {
						startActivity(new Intent(MainMenuActivity.this,
								DealsOffersActivity.class));
					}

					return false;
				}
			});
			settingLayout.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					// animation(settingLayout, settingTextView, settingIcon);
					if (android.os.Build.VERSION.SDK_INT > 16) {
						v.setDrawingCacheEnabled(true);
						v.setPressed(false);
						v.refreshDrawableState();
						Bitmap bm = v.getDrawingCache();
						Canvas c = new Canvas(bm);
						// c.drawARGB(255, 255, 0, 0);
						ActivityOptions opts = ActivityOptions
								.makeThumbnailScaleUpAnimation(v, bm, 0, 0);
						startActivity(new Intent(MainMenuActivity.this,
								SettingsActivity.class), opts.toBundle());
						v.setDrawingCacheEnabled(false);
					}
					return false;
				}
			});

			aboutLayout.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					// animation(aboutLayout, aboutTextView, aboutIcon);
					if (android.os.Build.VERSION.SDK_INT > 16) {
						v.setDrawingCacheEnabled(true);
						v.setPressed(false);
						v.refreshDrawableState();
						Bitmap bm = v.getDrawingCache();
						Canvas c = new Canvas(bm);
						// c.drawARGB(255, 255, 0, 0);
						ActivityOptions opts = ActivityOptions
								.makeThumbnailScaleUpAnimation(v, bm, 0, 0);
						startActivity(new Intent(MainMenuActivity.this,
								AboutAppActivity.class), opts.toBundle());
						v.setDrawingCacheEnabled(false);
					}
					return false;
				}
			});

		} catch (Exception es) {
			// Log.e("Exception found ",es.getMessage());
			System.out.println("Exception Found " + es.getMessage());
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
		actionBar.setTitle(IConstants.appRestName);
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

	// @SuppressLint("NewApi")
	// public void animate(View view) {
	// listView.setVisibility(LinearLayout.VISIBLE);
	// Animation animation = AnimationUtils.loadAnimation(this,
	// R.anim.animation);
	// animation.setDuration(500);
	// listView.setAnimation(animation);
	// listView.animate();
	// animation.start();
	// }

	public void handleTouch(View view) {
		// View view = findViewById(R.id.myButton1);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);

		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		view.setLayoutParams(params);

		ViewGroup.LayoutParams lparams = view.getLayoutParams();

		lparams.width = LayoutParams.FILL_PARENT;
		lparams.height = LayoutParams.FILL_PARENT;
		view.setLayoutParams(lparams);
	}

	public void animation(View v, TextView txt, ImageView img) {

		Animation mAnimation = new AlphaAnimation(1, 0);
		mAnimation.setDuration(150);
		mAnimation.setInterpolator(new LinearInterpolator());
		mAnimation.setRepeatCount(Animation.ABSOLUTE);
		mAnimation.setRepeatMode(Animation.REVERSE);
		v.startAnimation(mAnimation);
		txt.startAnimation(mAnimation);
		img.startAnimation(mAnimation);
	}

	public void listViewAnim(View v) {

		Animation mAnimation = new AlphaAnimation(1, 0);
		mAnimation.setDuration(150);
		mAnimation.setInterpolator(new LinearInterpolator());
		mAnimation.setRepeatCount(Animation.ABSOLUTE);
		mAnimation.setRepeatMode(Animation.REVERSE);
		v.startAnimation(mAnimation);
	}

	private MenuDetailsFetch menuDetailsFetch = new MenuDetailsFetch() {

		@Override
		public void onMenuDetailsFetch(String result) {
			// TODO Auto-generated method stub
			System.out.println("menuDetailsFetch details called");
			IConstants.showToast("MenuDetailsFetch " + result);
			// ListView listView = (ListView) findViewById(R.id.menulistview);
			// ArrayAdapter<String> adapter = null;
			try {
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
					startActivity(new Intent(MainMenuActivity.this,
							MenuCourseActivity.class));
				} else {
					startActivity(new Intent(MainMenuActivity.this,
							MenuCategoryActivity.class));
				}

				// ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				// MenuCategoryActivity.this,
				// android.R.layout.simple_list_item_1, itemCategoryList);

				// listView.setAdapter(arrayAdapter);
				// MenuCourseAdapter menuAdapter = new MenuCourseAdapter();
				// listView.setAdapter(menuAdapter);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("list color error " + e.getMessage());
				e.printStackTrace();
			}

			// listView.setOnItemClickListener(new OnItemClickListener() {
			// @Override
			// public void onItemClick(AdapterView<?> parent, View view,
			// int position, long id) {
			// // TODO Auto-generated method stub
			// IConstants.showToast(itemCategoryList.get(position));
			//
			// try {
			//
			// Intent intent = new Intent(IConstants.mContext,
			// MenuActivity.class);
			// intent.putExtra("itemCategory",
			// itemCategoryList.get(position));
			// IConstants.mContext.startActivity(intent);
			// IConstants.mContext.overridePendingTransition(
			// R.anim.slide_in_right, R.anim.slide_out_left);
			//
			// } catch (Exception ex) {
			// System.out.println("itemCategoryList event error "
			// + ex.getMessage());
			// }
			// }
			//
			// });

			iDialog.dismissProgress();
			// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// System.out.println("Exception caught " + e.getMessage());
			// }
			// }
		}

	};

}
