package com.restaurant.app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.restaurant.adapter.MenuCategoryAdapter;
import com.restaurant.adapter.MenuCourseAdapter;
import com.restaurant.controller.MenuController;
import com.restaurant.controller.MenuController.MenuDetailsFetch;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.util.ExceptionHandler;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;

public class MenuCategoryActivity extends Activity {

	ListView listView;
	LayoutInflater layoutInflater;
	LinearLayout linearLayout;
	TextView txtView;
	// LKupMenuCategoryControllerTC lkupcontrollistener;
	MenuController menuController;
	Context context;
	// private ArrayAdapter<String> listAdapter = null;
	String itemCourse = "";
	IDialog iDialog = new IDialog();
	ArrayList<String> itemCategoryList = new ArrayList<String>();

	Typeface font;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
			DatabaseManager.init(this);
			IConstants.mContext = this;
			font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
			setContentView(R.layout.activity_menu);
			ListView listView = (ListView) findViewById(R.id.menulistview);
			iDialog.showProgress(this);

			try {

				Bundle bundle = getIntent().getExtras();
				if (null != bundle && bundle.containsKey("course")) {
					itemCourse = bundle.getString("course");
					//IConstants.showToast("itemCourse " + itemCourse);
					MenuCategoryAdapter menuCategoryAdapter = new MenuCategoryAdapter(
							itemCourse);
					listView.setAdapter(menuCategoryAdapter);
					iDialog.dismissProgress();
				}

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("" + e.getMessage());
				e.printStackTrace();
			}
			// menuController = new MenuNewController(MenuCategoryActivity.this,
			// menuDetailsFetch);
			// menuController.getMenuDetails();

			// TODO Auto-generated method stub
			// Toast.makeText(getApplicationContext(), "Main MenuView open",
			// Toast.LENGTH_LONG).show();
			// linearLayout = (LinearLayout)
			// findViewById(R.id.menuViewMainLayout);
			// listView = (ListView) findViewById(R.id.menulistview);

			setupActionBar();
		} catch (Exception ex) {
			System.out.println("Menu Exception " + ex.getMessage());
		}

	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		// actionBar.setIcon(R.drawable.ic_transparent);
		// actionBar.setIcon(R.drawable.ic_action_navigation_drawer);
		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#55000000")));
		actionBar.setTitle("Menu");

		int titleId = getResources().getIdentifier("action_bar_title", "id",
				"android");
		TextView yourTextView = (TextView) findViewById(titleId);
		// yourTextView.setTextColor(getResources().getColor(R.color.black));
		yourTextView.setTypeface(font);

		// getActionBarTitleView().setAlpha(0f);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.start, menu);

		MenuItem menuItem = menu.add(0, 1, 0, null).setIcon(
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

	// ===========================MenuDetailsFetch==================================

	// private MenuDetailsFetch menuDetailsFetch = new MenuDetailsFetch() {
	//
	// @Override
	// public void onMenuDetailsFetch(String result) {
	// // TODO Auto-generated method stub
	//
	// IConstants.showToast("MenuDetailsFetch " + result);
	//
	// // ArrayAdapter<String> adapter = null;
	// try {
	// // final List<MenuDetails> courses =
	// // DatabaseManager.getInstance()
	// // .getMenuDetailsDao().queryBuilder().distinct()
	// // .selectColumns("course").query();
	// // for (MenuDetails menuDetail : courses) {
	// // courseList.add(menuDetail.getCourse());
	// // }
	// final List<MenuDetails> itemCategory = DatabaseManager
	// .getInstance().getMenuDetailsDao().queryBuilder()
	// .distinct().selectColumns("itemCategory").query();
	//
	// for (MenuDetails menuDetail : itemCategory) {
	// itemCategoryList.add(menuDetail.getItemCategory());
	// }
	// itemCategoryList.removeAll(Arrays.asList(new Object[] { null,
	// "null" }));
	// ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
	// MenuCategoryActivity.this,
	// android.R.layout.simple_list_item_1, itemCategoryList);
	//
	// // listView.setAdapter(arrayAdapter);
	// MenuCourseAdapter menuAdapter = new MenuCourseAdapter();
	// listView.setAdapter(menuAdapter);
	// } catch (Exception e) {
	// // TODO: handle exception
	// System.out.println("list color error " + e.getMessage());
	// e.printStackTrace();
	// }
	//
	// listView.setOnItemClickListener(new OnItemClickListener() {
	//
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
	//
	// iDialog.dismissProgress();
	// // } catch (SQLException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // System.out.println("Exception caught " + e.getMessage());
	// // }
	// // }
	// }
	//
	// };
	//
	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// try {
	// DatabaseManager.getInstance().closeHelper();
	// if (menuController != null)
	// menuController.cancelTasks();
	// } catch (Exception e) {
	// // TODO: MenuActivity handle exception
	// }
	// };

}
