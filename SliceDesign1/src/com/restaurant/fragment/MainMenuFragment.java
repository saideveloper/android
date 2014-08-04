package com.restaurant.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.restaurant.adapter.MenuSearchAdapter;
import com.restaurant.app.AboutAppActivity;
import com.restaurant.app.DealsOffersActivity;
import com.restaurant.app.GalleryActivity;
import com.restaurant.app.MainMenuActivity;
import com.restaurant.app.MenuCategoryActivity;
import com.restaurant.app.MenuCourseActivity;
import com.restaurant.app.R;
import com.restaurant.app.ReservationActivity;
import com.restaurant.app.ReviewActivity;
import com.restaurant.app.SettingsActivity;
import com.restaurant.controller.MenuController;
import com.restaurant.controller.MenuController.MenuDetailsFetch;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SearchView.OnCloseListener;

public class MainMenuFragment extends Fragment {

	private Activity fragmentActivity;

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

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {

		fragmentActivity = super.getActivity();
		
		final View fragmentView = inflater.inflate(R.layout.main_menu,
				container, false);
		
		
		
		
		try {
			
			
			// mFlipper = ((ViewFlipper) this.findViewById(R.id.flipper));
			// mFlipper.startFlipping();
			search = (SearchView) fragmentView.findViewById(R.id.searchView);
			// linear = (LinearLayout) findViewById(R.id.mainLayout);
			parentLayout = (LinearLayout) fragmentView
					.findViewById(R.id.parentlayout);
			mainLayout = (LinearLayout) fragmentView
					.findViewById(R.id.mainMenuLayout);
			menuIcon = (ImageView) fragmentView.findViewById(R.id.menuImg);
			reviewIcon = (ImageView) fragmentView.findViewById(R.id.reviewImg);
			galleryIcon = (ImageView) fragmentView.findViewById(R.id.galleryImg);
			reserveIcon = (ImageView) fragmentView.findViewById(R.id.reserveImg);
			dealsIcon = (ImageView) fragmentView.findViewById(R.id.dealsImg);
			settingIcon = (ImageView) fragmentView.findViewById(R.id.settingImg);
			aboutIcon = (ImageView) fragmentView.findViewById(R.id.aboutImg);
			callwaiterIcon = (ImageView) fragmentView
					.findViewById(R.id.callwaiterImg);

			menuLayout = (RelativeLayout) fragmentView
					.findViewById(R.id.menulayout);
			reviewLayout = (RelativeLayout) fragmentView
					.findViewById(R.id.reviewlayout);
			galleryLayout = (RelativeLayout) fragmentView
					.findViewById(R.id.gallerylayout);
			reserveLayout = (RelativeLayout) fragmentView
					.findViewById(R.id.reservelayout);
			dealsLayout = (RelativeLayout) fragmentView
					.findViewById(R.id.dealslayout);
			settingLayout = (RelativeLayout) fragmentView
					.findViewById(R.id.settinglayout);
			aboutLayout = (RelativeLayout) fragmentView
					.findViewById(R.id.aboutlayout);
			callwaiterLayout = (RelativeLayout) fragmentView
					.findViewById(R.id.callwaiterlayout);

			menuTextView = (TextView) fragmentView.findViewById(R.id.menutxtid);
			reviewTextView = (TextView) fragmentView
					.findViewById(R.id.reviewtxtid);
			galleryTextView = (TextView) fragmentView
					.findViewById(R.id.gallerytxtid);
			reserveTextView = (TextView) fragmentView
					.findViewById(R.id.reservetxtid);
			dealsTextView = (TextView) fragmentView.findViewById(R.id.dealstxtid);
			settingTextView = (TextView) fragmentView
					.findViewById(R.id.settingtxtid);
			aboutTextView = (TextView) fragmentView.findViewById(R.id.abouttxtid);
			callwaiterTextView = (TextView) fragmentView
					.findViewById(R.id.callwaitertxtid);

			font = Typeface.createFromAsset(fragmentActivity.getAssets(),
					"Roboto-Bold.ttf");

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
						View child = fragmentActivity.getLayoutInflater()
								.inflate(R.layout.activity_list_view, null);
						parentLayout.addView(child);
						parentLayout.removeView(mainLayout);
						listView = (ListView) fragmentView
								.findViewById(R.id.listview);
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
						LinearLayout linear1 = (LinearLayout) fragmentView
								.findViewById(R.id.relativeListView);
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
					 IConstants.showToast("menuLayout");
					try {

						MenuController menuController = new MenuController(
								fragmentActivity.getApplicationContext(),
								menuDetailsFetch);
						menuController.getMenuDetails();

						// if (android.os.Build.VERSION.SDK_INT > 16) {
						// v.setDrawingCacheEnabled(true);
						// v.setPressed(false);
						// v.refreshDrawableState();
						// Bitmap bm = v.getDrawingCache();
						// Canvas c = new Canvas(bm);
						// // c.drawARGB(255, 255, 0, 0);
						// ActivityOptions opts = ActivityOptions
						// .makeThumbnailScaleUpAnimation(v, bm, 0, 0);
						//
						// // startActivity(new Intent(MainMenuActivity.this,
						// // MenuCategoryActivity.class), opts
						// // .toBundle());
						// MenuNewController menuController = new
						// MenuNewController(
						// getApplicationContext(), menuDetailsFetch);
						// menuController.getMenuDetails();
						// v.setDrawingCacheEnabled(false);
						// } else {
						// // startActivity(new Intent(MainMenuActivity.this,
						// // MenuCategoryActivity.class));
						// }
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
						startActivity(new Intent(fragmentActivity,
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
						startActivity(new Intent(fragmentActivity,
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
						startActivity(new Intent(fragmentActivity,
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
						startActivity(new Intent(fragmentActivity,
								DealsOffersActivity.class), opts.toBundle());
						v.setDrawingCacheEnabled(false);
					} else {
						startActivity(new Intent(fragmentActivity,
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
						startActivity(new Intent(fragmentActivity,
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
						startActivity(new Intent(fragmentActivity,
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

		return fragmentView;
		// return super.onCreateView(inflater, fragmentView, savedInstanceState);
	}

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
					startActivity(new Intent(fragmentActivity,
							MenuCourseActivity.class));
				} else {
					startActivity(new Intent(fragmentActivity,
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
