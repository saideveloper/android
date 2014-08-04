package com.restaurant.adapter;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.restaurant.app.DealsOffersActivity;
import com.restaurant.app.ExpandableMenuActivity;
import com.restaurant.app.HomeActivity;
import com.restaurant.app.MainMenuActivity;
import com.restaurant.app.MenuActivity;
import com.restaurant.app.MenuCategoryActivity;
import com.restaurant.app.MenuCourseActivity;
import com.restaurant.app.R;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.RestItemTypeDetails;
import com.restaurant.model.RestTimingDetails;
import com.restaurant.model.RestaurantInfoDetails;
import com.restaurant.model.lkup.LkupPayTypeDetails;
import com.restaurant.model.lkup.LkupPrepTypeDetails;
import com.restaurant.util.IConstants;

public class HomeAdapter extends BaseAdapter {
	private Context mContext;

	private LayoutInflater inflater;
	private ArrayList<MenuDetails> listArray;

	Typeface font;

	String[] desc, itemsColor;

	String cuisines = "", services = "", operationHours = "", payments = "";
	LinearLayout cuisinesLayout, servicesLayout, paymentsLayout;

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("deprecation")
	public HomeAdapter(Context context, String[] items, String[] desc,
			String[] itemsColor) {
		this.mContext = context;
		listArray = new ArrayList<MenuDetails>();

		font = Typeface.createFromAsset(context.getAssets(),
				"Roboto-Regular.ttf");
		
		this.desc = desc;
		this.itemsColor = itemsColor;

		for (int i = 0; i < items.length; i++) {
			MenuDetails menuDetails = new MenuDetails();
			menuDetails.setItemId(i);
			menuDetails.setItemName(items[i]);
			listArray.add(menuDetails);
		}

		try {

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			
			cuisinesLayout = new LinearLayout(context);
			cuisinesLayout.setLayoutParams(layoutParams);
			cuisinesLayout.setOrientation(LinearLayout.HORIZONTAL);
			//servicesLayout.setBackgroundColor(Color.WHITE);
			//cuisinesLayout.setBackgroundResource(R.drawable.corner);
			
			List<LkupPrepTypeDetails> lkupPrepTypeDetails = DatabaseManager
					.getInstance().getLkupPrepTypeDetailsDao().queryForAll();
			
			View[] cuisinesViews = new View[lkupPrepTypeDetails.size()];
			int cuisinesInc = 0;
			int starting = 0;
			for (LkupPrepTypeDetails lkupPrepTypeDetail : lkupPrepTypeDetails) {
				if (starting < lkupPrepTypeDetails.size() - 1) {
					starting++;
					cuisines += "" + lkupPrepTypeDetail.getPrepType() + ", ";
				} else {
					cuisines += "" + lkupPrepTypeDetail.getPrepType() + " ";
				}
				
				TextView textView = new TextView(context);
				textView.setText("" + lkupPrepTypeDetail.getPrepType());
				textView.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_check_on_holo_dark, 0, 0, 0);
				textView.setTextSize(14);
				textView.setPadding(10, 10, 10, 10);
				//cuisinesLayout.addView(textView);
				cuisinesViews[cuisinesInc++] = textView;
			}
			
			populateText(cuisinesLayout, cuisinesViews, context);

			// cuisines += "\n";

			starting = 0;

			servicesLayout = new LinearLayout(context);
			servicesLayout.setLayoutParams(layoutParams);
			servicesLayout.setOrientation(LinearLayout.HORIZONTAL);
			//servicesLayout.setBackgroundColor(Color.WHITE);
			//servicesLayout.setBackgroundResource(R.drawable.corner);

			List<RestaurantInfoDetails> restaurantInfoDetails = DatabaseManager
					.getInstance().getRestaurantInfoDetailsDao().queryForAll();

			for (RestaurantInfoDetails restaurantInfoDetail : restaurantInfoDetails) {
				if (starting < restaurantInfoDetails.size() - 1) {
					starting++;
					services += "" + restaurantInfoDetail.getSpecification()
							+ ", ";
				} else {
					services += "" + restaurantInfoDetail.getSpecification()
							+ " ";
				}

				TextView textView = new TextView(context);
				textView.setText("" + restaurantInfoDetail.getSpecification());
				textView.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_check_on_holo_dark, 0, 0, 0);
				textView.setTextSize(14);
				textView.setPadding(10, 10, 10, 10);
				servicesLayout.addView(textView);
			}

			// services += "\n";

			starting = 0;

			List<RestTimingDetails> restTimingDetails = DatabaseManager
					.getInstance().getRestTimingDetailsDao().queryForAll();

			for (RestTimingDetails restTimingDetail : restTimingDetails) {
				try {
					SimpleDateFormat parseFormat = new SimpleDateFormat(
							"HH:mm:ss");
					Date dt = parseFormat.parse(restTimingDetail.getFromTime());

					SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
					String fromTime = sdf.format(dt);

					dt = parseFormat.parse(restTimingDetail.getToTime());
					sdf = new SimpleDateFormat("hh:mm aa");
					String toTime = sdf.format(dt);

					operationHours += "" + fromTime + " - " + toTime + " \n";
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			List<LkupPayTypeDetails> lkupPayTypeDetails = DatabaseManager
					.getInstance().getLkupPayTypeDetailsDao().queryForAll();

			paymentsLayout = new LinearLayout(context);
			paymentsLayout.setLayoutParams(layoutParams);
			paymentsLayout.setOrientation(LinearLayout.HORIZONTAL);
			//paymentsLayout.setBackgroundColor(Color.WHITE);
			//servicesLayout.setBackgroundResource(R.drawable.corner);

			for (LkupPayTypeDetails lkupPayTypeDetail : lkupPayTypeDetails) {
				if (starting < lkupPayTypeDetails.size() - 1) {
					starting++;
					payments += "" + lkupPayTypeDetail.getPayDesc() + ", ";
				} else {
					payments += "" + lkupPayTypeDetail.getPayDesc() + " "; 
				}

				TextView textView = new TextView(context);
				textView.setText("" + lkupPayTypeDetail.getPayDesc());
				textView.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_check_on_holo_dark, 0, 0, 0);
				textView.setPadding(10, 10, 10, 10);
				textView.setTextSize(14);
				paymentsLayout.addView(textView);
			}

			// payments += "\n";

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	private void populateText(LinearLayout ll, View[] views , Context mContext) { 
	    Display display = IConstants.mContext.getWindowManager().getDefaultDisplay();
	    ll.removeAllViews();
	    int maxWidth = display.getWidth() - 20;

	    LinearLayout.LayoutParams params;
	    LinearLayout newLL = new LinearLayout(mContext);
	    newLL.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
	            LayoutParams.WRAP_CONTENT));
	    newLL.setGravity(Gravity.LEFT);
	    newLL.setOrientation(LinearLayout.HORIZONTAL);

	    int widthSoFar = 0;

	    for (int i = 0 ; i < views.length ; i++ ){
	        LinearLayout LL = new LinearLayout(mContext);
	        LL.setOrientation(LinearLayout.HORIZONTAL);
	        LL.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
	        LL.setLayoutParams(new ListView.LayoutParams(
	                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	        //my old code
	        //TV = new TextView(mContext);
	        //TV.setText(textArray[i]);
	        //TV.setTextSize(size);  <<<< SET TEXT SIZE
	        //TV.measure(0, 0);
	        views[i].measure(0,0);
	        params = new LinearLayout.LayoutParams(views[i].getMeasuredWidth(),
	                LayoutParams.WRAP_CONTENT);
	        //params.setMargins(5, 0, 5, 0);  // YOU CAN USE THIS
	        //LL.addView(TV, params);
	        LL.addView(views[i], params);
	        LL.measure(0, 0);
	        widthSoFar += views[i].getMeasuredWidth();// YOU MAY NEED TO ADD THE MARGINS
	        if (widthSoFar >= maxWidth) {
	            ll.addView(newLL);

	            newLL = new LinearLayout(mContext);
	            newLL.setLayoutParams(new LayoutParams(
	                    LayoutParams.FILL_PARENT,
	                    LayoutParams.WRAP_CONTENT));
	            newLL.setOrientation(LinearLayout.HORIZONTAL);
	            newLL.setGravity(Gravity.LEFT);
	            params = new LinearLayout.LayoutParams(LL
	                    .getMeasuredWidth(), LL.getMeasuredHeight());
	            newLL.addView(LL, params);
	            widthSoFar = LL.getMeasuredWidth();
	        } else {
	            newLL.addView(LL);
	        }
	    }
	    ll.addView(newLL);
	}
	
	
	@Override
	public int getCount() {
		return listArray.size(); // total number of elements in the list
	}

	@Override
	public Object getItem(int index) {
		return listArray.get(index); // single item in the list
	}

	@Override
	public long getItemId(int index) {
		return index; // index number
	}

	@Override
	public View getView(final int index, View view, final ViewGroup parent) {

		try {
			final ViewHolder viewHolder;

			if (view == null) {
				if (inflater == null) {
					inflater = LayoutInflater.from(parent.getContext());
				}

				view = inflater.inflate(R.layout.home_list_item, parent, false);

				viewHolder = new ViewHolder();

				viewHolder.layoutListItem = (LinearLayout) view
						.findViewById(R.id.layoutListItem);
				viewHolder.layoutTitle = (LinearLayout) view
						.findViewById(R.id.layoutTitle);
				viewHolder.layoutInnerContent = (LinearLayout) view
						.findViewById(R.id.layoutInnerContent);

				// viewHolder.layoutInfo = (LinearLayout) view
				// .findViewById(R.id.layoutInfo);

				viewHolder.imgBgLayout = (RelativeLayout) view
						.findViewById(R.id.imgBgLayout);
				viewHolder.imgBg = (ImageView) view.findViewById(R.id.imgBg);

				viewHolder.item = (TextView) view.findViewById(R.id.item);
				viewHolder.desc = (TextView) view.findViewById(R.id.desc);
				// viewHolder.cuisinesDetails = (TextView) view
				// .findViewById(R.id.cuisinesDetails);
				// viewHolder.servicesDetails = (TextView) view
				// .findViewById(R.id.servicesDetails);
				// viewHolder.operationHoursDetails = (TextView) view
				// .findViewById(R.id.operationHoursDetails);
				// viewHolder.paymentModeDetails = (TextView) view
				// .findViewById(R.id.paymentModeDetails);
				// viewHolder.addressDetails = (TextView) view
				// .findViewById(R.id.addressDetails);

				viewHolder.item.setTypeface(font);
				viewHolder.desc.setTypeface(font);

				// viewHolder.cuisinesDetails.setTypeface(font);
				// viewHolder.servicesDetails.setTypeface(font);
				// viewHolder.paymentModeDetails.setTypeface(font);
				// viewHolder.operationHoursDetails.setTypeface(font);
				// viewHolder.addressDetails.setTypeface(font);

				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			final MenuDetails menuDetails = listArray.get(index);
			viewHolder.item.setText("" + menuDetails.getItemName());
			viewHolder.item.setTextColor(Color.parseColor(itemsColor[index]));
			
			viewHolder.desc.setText("" + desc[index]);

			viewHolder.desc.setVisibility(View.VISIBLE);

			if (desc[index].equalsIgnoreCase("")) {
				viewHolder.desc.setVisibility(View.GONE);
				viewHolder.item.setPadding(10, 20, 0, 20);
			} else if (desc[index].equalsIgnoreCase("menu")) {
				//if (!cuisines.equalsIgnoreCase("")) {
					viewHolder.desc.setText(cuisines);
				//} else {
					viewHolder.desc.setVisibility(View.GONE);
				//	viewHolder.item.setPadding(10, 20, 0, 20);
				//}
				viewHolder.layoutInnerContent.addView(cuisinesLayout);
			} else if (desc[index].equalsIgnoreCase("service")) {
				viewHolder.desc.setText(services);
				viewHolder.desc.setVisibility(View.GONE);
				viewHolder.layoutInnerContent.addView(servicesLayout);
			} else if (desc[index].equalsIgnoreCase("pay")) {
				viewHolder.desc.setText(payments);
				viewHolder.desc.setVisibility(View.GONE);
				viewHolder.layoutInnerContent.addView(paymentsLayout);
			} else if (desc[index].equalsIgnoreCase("buffet")) {
				// viewHolder.desc.setText(payments);
			} else if (desc[index].equalsIgnoreCase("address")) {
				viewHolder.desc
						.setText("Address: " + IConstants.appRestAddress + ", " + IConstants.appRestLocation);
				viewHolder.desc.setTextSize(14);
				viewHolder.desc.setTextColor(Color.BLACK);
			}

			if (index == 0) {
				viewHolder.layoutListItem.getLayoutParams().height = HomeActivity.screenHeight;
				viewHolder.layoutListItem.setBackgroundColor(Color.TRANSPARENT);
				viewHolder.imgBgLayout.setVisibility(View.INVISIBLE);
				viewHolder.layoutTitle.setVisibility(View.GONE);

				viewHolder.imgBgLayout.getLayoutParams().height = HomeActivity.screenHeight;
				viewHolder.imgBg.getLayoutParams().height = HomeActivity.screenHeight;

			} else {
				viewHolder.layoutListItem.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;

				// viewHolder.layoutListItem.setBackgroundColor(Color.BLACK);
				viewHolder.layoutListItem.setBackgroundColor(Color.WHITE);
				// viewHolder.layoutListItem.setBackgroundColor(Color
				// .parseColor(itemsColor[index]));
				viewHolder.imgBgLayout.setVisibility(View.GONE);
				viewHolder.layoutTitle.setVisibility(View.VISIBLE);

				// viewHolder.item.setTextColor(Color.parseColor(itemsColor[index]));
			}

			// if (index < 6) {
			// viewHolder.layoutInfo.setVisibility(View.GONE);
			// viewHolder.layoutTitle.setVisibility(View.VISIBLE);
			// } else {
			// viewHolder.layoutInfo.setVisibility(View.VISIBLE);
			// viewHolder.layoutTitle.setVisibility(View.GONE);
			//
			// viewHolder.cuisinesDetails.setText(cuisines);
			//
			// viewHolder.servicesDetails.setText(services);
			//
			// viewHolder.operationHoursDetails.setText(operationHours);
			//
			// viewHolder.paymentModeDetails.setText(payments);
			//
			// viewHolder.addressDetails.setText(""
			// + IConstants.appRestAddress + " \n");
			// }

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (menuDetails.getItemName() == "Menu") {
						mContext.startActivity(new Intent(mContext,
								ExpandableMenuActivity.class));
					}
				}
			});

			// view.setOnTouchListener(new OnTouchListener() {
			//
			// @Override
			// public boolean onTouch(View v, MotionEvent event) {
			// if (!menuDetails.getItemName().equalsIgnoreCase("")) {
			// try {
			// switch (event.getAction()) {
			// case MotionEvent.ACTION_DOWN:
			// if (index < 6 && index > 0) {
			// viewHolder.layoutListItem.setBackgroundColor(Color
			// .parseColor(itemsColor[index]));
			// viewHolder.item.setTextColor(Color.WHITE);
			// }
			// break;
			// case MotionEvent.ACTION_UP:
			// // viewHolder.layoutListItem
			// // .setBackgroundColor(Color.BLACK);
			// viewHolder.layoutListItem
			// .setBackgroundColor(Color.WHITE);
			// viewHolder.item.setTextColor(Color
			// .parseColor(itemsColor[index]));
			//
			// if (menuDetails.getItemName() == "Menu") {
			//
			// // mContext.startActivity(new Intent(mContext,
			// // MainMenuActivity.class));
			// ArrayList<String> courseList = new ArrayList<String>();
			// ArrayList<String> itemCategoryList = new ArrayList<String>();
			//
			// try {
			// final List<MenuDetails> courses = DatabaseManager.getInstance()
			// .getMenuDetailsDao().queryBuilder().distinct()
			// .selectColumns("course").query();
			// for (MenuDetails menuDetail : courses) {
			// courseList.add(menuDetail.getCourse());
			// }
			// final List<MenuDetails> itemCategory = DatabaseManager
			// .getInstance().getMenuDetailsDao().queryBuilder()
			// .distinct().selectColumns("itemCategory").query();
			//
			// for (MenuDetails menuDetail : itemCategory) {
			// itemCategoryList.add(menuDetail.getItemCategory());
			// }
			// itemCategoryList.removeAll(Arrays.asList(new Object[] { null,
			// "null" }));
			//
			//
			// if (courseList.size() > 0) {
			// mContext.startActivity(new Intent(mContext,
			// MenuCourseActivity.class));
			// } else if (itemCategoryList.size() > 0) {
			// mContext.startActivity(new Intent(mContext,
			// MenuCategoryActivity.class));
			// } else {
			// mContext.startActivity(new Intent(mContext,
			// MenuActivity.class));
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			//
			// } else if (menuDetails.getItemName() == "Deals & Offers") {
			// mContext.startActivity(new Intent(mContext,
			// DealsOffersActivity.class));
			// }
			//
			// // mContext.startActivity(new Intent(mContext,
			// // MenuActivity.class));
			//
			// IConstants.mContext.overridePendingTransition(
			// R.anim.slide_in_right,
			// R.anim.slide_out_left);
			//
			// break;
			// case MotionEvent.ACTION_MOVE:
			// // viewHolder.layoutListItem
			// // .setBackgroundColor(Color.BLACK);
			// viewHolder.layoutListItem
			// .setBackgroundColor(Color.WHITE);
			// viewHolder.item.setTextColor(Color
			// .parseColor(itemsColor[index]));
			// break;
			// case MotionEvent.ACTION_CANCEL:
			// // viewHolder.layoutListItem
			// // .setBackgroundColor(Color.BLACK);
			// viewHolder.layoutListItem
			// .setBackgroundColor(Color.WHITE);
			// viewHolder.item.setTextColor(Color
			// .parseColor(itemsColor[index]));
			// break;
			// }
			// } catch (Exception e) {
			// }
			// }
			//
			// return false;
			// }
			// });
		} catch (Exception e) {
			System.err.println(e);
		}

		return view;
	}

	private class ViewHolder {
		RelativeLayout imgBgLayout;
		ImageView imgBg;
		TextView item, desc;
		// TextView item, desc, servicesDetails, cuisinesDetails,
		// operationHoursDetails, paymentModeDetails, addressDetails;
		// LinearLayout layoutListItem, layoutTitle, layoutInfo;
		LinearLayout layoutListItem, layoutTitle, layoutInnerContent;
	}

	// public void changeColor(final View view, final String color, final String
	// string) {
	// final float[] curr = new float[3], from = new float[3], to = new
	// float[3];
	//
	// Color.colorToHSV(Color.parseColor("#FFFFFFFF"), from); // from white
	// Color.colorToHSV(Color.parseColor(color), to); // to red
	//
	// final ValueAnimator anim = ValueAnimator.ofFloat(0, 1); // values are
	// // arbitrary
	// anim.setDuration(300); // duration of animation
	//
	// anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	// @Override
	// public void onAnimationUpdate(ValueAnimator animation) {
	// curr[0] = from[0] + (to[0] - from[0])
	// * animation.getAnimatedFraction();
	// curr[1] = from[1] + (to[1] - from[1])
	// * animation.getAnimatedFraction();
	// curr[2] = from[2] + (to[2] - from[2])
	// * animation.getAnimatedFraction();
	//
	// view.setBackgroundColor(Color.HSVToColor(curr));
	// }
	// });
	//
	// anim.addListener(new AnimatorListener() {
	//
	// @Override
	// public void onAnimationStart(Animator animation) {
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animator animation) {
	// }
	//
	// @Override
	// public void onAnimationEnd(Animator animation) {
	// backToOrgColor(view, color, string);
	// }
	//
	// @Override
	// public void onAnimationCancel(Animator animation) {
	// }
	// });
	//
	// anim.start();
	// }
	//
	// public void backToOrgColor(final View view, String color, String string)
	// {
	// final float[] curr = new float[3], from = new float[3], to = new
	// float[3];
	//
	// Color.colorToHSV(Color.parseColor(color), from); // from white
	// Color.colorToHSV(Color.parseColor("#FFFFFFFF"), to); // to red
	//
	// final ValueAnimator anim = ValueAnimator.ofFloat(0, 1); // values are
	// // arbitrary
	// anim.setDuration(300); // duration of animation
	//
	// anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	// @Override
	// public void onAnimationUpdate(ValueAnimator animation) {
	// curr[0] = from[0] + (to[0] - from[0])
	// * animation.getAnimatedFraction();
	// curr[1] = from[1] + (to[1] - from[1])
	// * animation.getAnimatedFraction();
	// curr[2] = from[2] + (to[2] - from[2])
	// * animation.getAnimatedFraction();
	//
	// view.setBackgroundColor(Color.HSVToColor(curr));
	// }
	// });
	//
	// anim.addListener(new AnimatorListener() {
	//
	// @Override
	// public void onAnimationStart(Animator animation) {
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animator animation) {
	// }
	//
	// @Override
	// public void onAnimationEnd(Animator animation) {
	// // try {
	// // Thread.sleep(1000);
	// // //view.setBackgroundColor(Color.WHITE);
	// // } catch (InterruptedException e) {
	// // e.printStackTrace();
	// // }
	// mContext.startActivity(new Intent(mContext,
	// MainMenuActivity.class));
	// }
	//
	// @Override
	// public void onAnimationCancel(Animator animation) {
	// }
	// });
	//
	// anim.start();
	// }
}
