package com.restaurant.app;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;

import com.restaurant.adapter.MenuCourseAdapter;
import com.restaurant.util.IDialog;

public class MenuCourseActivity extends Activity {

	IDialog iDialog = new IDialog();
	ArrayList<String> courseList = new ArrayList<String>();
	WindowManager windowManager;
	Context context;

	// String[] colorCodes = { "#F9C0B7", "#F7ADA1", "#F59889", "#F48370",
	// "#F26E58", "#F05B42", "#EF4E36", "#EE462A", "#EB3113", "#D32C11" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_course);
		ListView listView = (ListView) findViewById(R.id.menuCourseListView);
		// ViewGroup.LayoutParams params = listView.getLayoutParams();
		// params.height = params.FILL_PARENT;
		// listView.setLayoutParams(params);
		// listView.requestLayout();
		// List<MenuDetails> courses;
		MenuCourseAdapter menuAdapter = new MenuCourseAdapter();
//		RowHeight rowHeight = new RowHeight();
//		RowHeight.setListViewHeightBasedOnChildren(listView);
		listView.setAdapter(menuAdapter);
		// for (int i = 0; i < colorCodes.length; i++) {
		// System.out.println("colorcodes " + colorCodes[i]);
		// listView.setBackgroundColor(Color.parseColor(colorCodes[i]));
		//
		// }
		iDialog.dismissProgress();
		setupActionBar();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_course, menu);
//		return true;
//	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		// actionBar.setIcon(R.drawable.ic_transparent);
		// actionBar.setIcon(R.drawable.ic_action_navigation_drawer);
		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#55000000")));
		actionBar.setTitle("Menu");
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

	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

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
	// final List<MenuDetails> courses = DatabaseManager.getInstance()
	// .getMenuDetailsDao().queryBuilder().distinct()
	// .selectColumns("course").query();
	// for (MenuDetails menuDetail : courses) {
	// courseList.add(menuDetail.getCourse());
	// }
	// // final List<MenuDetails> itemCategory = DatabaseManager
	// // .getInstance().getMenuDetailsDao().queryBuilder()
	// // .distinct().selectColumns("itemCategory").query();
	// //
	// // for (MenuDetails menuDetail : itemCategory) {
	// // itemCategoryList.add(menuDetail.getItemCategory());
	// // }
	// // itemCategoryList.removeAll(Arrays.asList(new Object[] { null,
	// // "null" }));
	//
	// // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
	// // MenuCourseActivity.this,
	// // android.R.layout.simple_list_item_1, courseList);
	//
	// // listView.setAdapter(arrayAdapter);
	// MenuCategoryAdapter menuAdapter = new MenuCategoryAdapter();
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
	// IConstants.showToast(courseList.get(position));
	//
	// try {
	//
	// Intent intent = new Intent(IConstants.mContext,
	// MenuActivity.class);
	// intent.putExtra("course", courseList.get(position));
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

};
