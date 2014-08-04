package com.restaurant.app;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.restaurant.adapter.OrderNewAdapter2;
import com.restaurant.adapter.OrderNewAdapter3;
import com.restaurant.adapter.OrderNewAdapter3.ShowConfirmOrderOrPayBill;
import com.restaurant.controller.OrderController;
import com.restaurant.controller.OrderController.OrderDetailsFetch;
import com.restaurant.controller.OrderController.OrderDetailsPost;
import com.restaurant.controller.SalesController;
import com.restaurant.controller.SalesController.SalesDetailsPost;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.OrderMasterDetails;
import com.restaurant.model.OrderMenuDetails;
import com.restaurant.model.OrderOfferDetails;
import com.restaurant.util.ExceptionHandler;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;

public class OrderNewActivity3 extends Activity {

	RelativeLayout imgBgLayout, actionbarBg;
	TextView restaurantName, restaurantCity;
	ImageView imageView;
	View imgBgMask;

	ListView listView;

	Typeface font;

	Button confirmOrder, cancelOrder, payBill;

	IDialog iDialog = new IDialog();

	OrderController orderController;
	SalesController salesController;

	OrderNewAdapter3 orderNewAdapter3;

	public static int screenWidth = 0, screenHeight = 0;

	public static int orderNo = 0;
	public static String chefnotes = "";

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
				.getHeight() / 3);

		// this is not really necessary as ExpandableListActivity contains an
		// ExpandableList
		setContentView(R.layout.order_list_new);

		font = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");

		listView = (ListView) findViewById(R.id.orderlist);

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

		cancelOrder.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							OrderNewActivity3.this);

					// set dialog message
					alertDialogBuilder
					.setTitle("Cancel Items")
							.setMessage(
									"Do you want to cancel items which is not yet ordered?")
							.setCancelable(false)
							.setPositiveButton("Yes", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();

									try {
										List<OrderMenuDetails> orderMenuDetailsList = DatabaseManager
												.getInstance()
												.getOrderMenuDetailsDao()
												.queryBuilder().distinct()
												.query();

										for (OrderMenuDetails orderMenuDetail : orderMenuDetailsList) {
											if (orderMenuDetail.isEditable())
												DatabaseManager
														.getInstance()
														.getOrderMenuDetailsDao()
														.delete(orderMenuDetail);
										}

										orderNewAdapter3.removeAllItems();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}).setNegativeButton("No", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					
					//alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

					// show it
					alertDialog.show();

					// List<OrderMenuDetails> orderMenuDetailsList =
					// DatabaseManager
					// .getInstance().getOrderMenuDetailsDao()
					// .queryBuilder().distinct().query();
					//
					// for (OrderMenuDetails orderMenuDetail :
					// orderMenuDetailsList) {
					// if (orderMenuDetail.isEditable())
					// DatabaseManager.getInstance()
					// .getOrderMenuDetailsDao()
					// .delete(orderMenuDetail);
					// }
					//
					// orderNewAdapter3.removeAllItems();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		confirmOrder.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ArrayList<OrderMenuDetails> itemList = new ArrayList<OrderMenuDetails>();
				ArrayList<OrderOfferDetails> offerList = new ArrayList<OrderOfferDetails>();

				try {
					// int count = orderNewAdapter3.getCount();

					List<OrderMenuDetails> orderMenuDetailsList = DatabaseManager
							.getInstance().getOrderMenuDetailsDao()
							.queryBuilder().distinct().query();

					for (OrderMenuDetails orderMenuDetail : orderMenuDetailsList) {
						if (orderMenuDetail.isEditable())
							itemList.add(orderMenuDetail);
					}

					// for (int i = 0; i < count; i++) {
					//
					// OrderMenuDetails orderMenuDetail = (OrderMenuDetails)
					// orderNewAdapter3
					// .getItem(i);
					// orderMenuDetail.setItemId(orderMenuDetail.getItemId());
					// orderMenuDetail.setQuantity(orderMenuDetail
					// .getQuantity());
					// orderMenuDetail.setPrice(orderMenuDetail.getPrice());
					// orderMenuDetail.setChefNote(orderMenuDetail
					// .getChefNote());
					// orderMenuDetail.setAllergy(orderMenuDetail.getAllergy());
					//
					// MenuDetails menuDetail = (DatabaseManager.getInstance()
					// .getMenuDetailsDao().queryBuilder().distinct()
					// .where()
					// .eq("itemId", orderMenuDetail.getItemId())
					// .query()).get(0);
					//
					// orderMenuDetail.setCookTime(menuDetail.getPrepTime());
					// orderMenuDetail.setCourse(menuDetail.getCourse());
					//
					// orderMenuDetail.setOrderType("");
					//
					// itemList.add(orderMenuDetail);
					// }

					List<OrderMasterDetails> orderMasterDetailsList = DatabaseManager
							.getInstance().getOrderMasterDetailsDao()
							.queryBuilder().distinct().query();

					OrderMasterDetails orderMasterDetails = new OrderMasterDetails();

					if (orderMasterDetailsList.size() > 0) {
						orderMasterDetails = orderMasterDetailsList.get(0);
					} else {
						orderMasterDetails.setOrderNo(0);
					}

					orderMasterDetails.setChefNotes(chefnotes);

					orderMasterDetails.setOrderMenuDetails(itemList);
					orderMasterDetails.setOrderOfferDetails(offerList);

					// Gson gson = new GsonBuilder().excludeFieldsWithModifiers(
					// Modifier.TRANSIENT).create();
					Gson gson = new GsonBuilder()
							.excludeFieldsWithoutExposeAnnotation().create();

					String json = gson.toJson(orderMasterDetails);
					// System.out.println(json);
					JSONObject jsonObject = new JSONObject();
					JSONObject printerJsonObject = new JSONObject();
					JSONArray jsonArray = new JSONArray();

					jsonArray.put(new JSONObject(json));
					jsonObject.put("orderMasterDetails", jsonArray);
					printerJsonObject.put("printerDetails", jsonArray);
					jsonObject = new JSONObject(jsonObject.toString());
					printerJsonObject = new JSONObject(printerJsonObject
							.toString());
					System.out.println(jsonObject.toString());
					System.out.println(printerJsonObject.toString());

					iDialog.showProgress(OrderNewActivity3.this);

					orderController = new OrderController(
							getApplicationContext(), orderDetailsPostListener);
					orderController.postOrderDetails(jsonObject,
							printerJsonObject);

					chefnotes = "";
				} catch (Exception e) {
					e.printStackTrace();
				}

				// AlertDialog.Builder alertDialogBuilder = new
				// AlertDialog.Builder(
				// OrderNewActivity3.this);
				//
				// // set title
				// alertDialogBuilder.setTitle("Thank You");
				//
				// // set dialog message
				// alertDialogBuilder.setMessage("Your Order #### is Successfully placed")
				// .setCancelable(false)
				// .setNeutralButton("Thanks", new OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// dialog.cancel();
				// }
				// });
				//
				// // create alert dialog
				// AlertDialog alertDialog = alertDialogBuilder.create();
				//
				// // show it
				// alertDialog.show();
				//
				// confirmOrder.setVisibility(View.GONE);
				// cancelOrder.setVisibility(View.GONE);
				// payBill.setVisibility(View.VISIBLE);
			}
		});

		salesController = new SalesController(this, salesDetailsPost);

		payBill.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				iDialog.paybill(OrderNewActivity3.this, salesController);

				// confirmOrder.setVisibility(View.VISIBLE);
				// cancelOrder.setVisibility(View.VISIBLE);
				// payBill.setVisibility(View.GONE);
			}
		});

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				imgBgLayout.setTranslationY(-getScrollY() / 4);
				// System.out.println("getScrollY() " + getScrollY());

				try {
					// int c = (getScrollY() > 200 ? (getScrollY() - 210) : 0);
					int c = getScrollY();
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

		setupNewActionBar();

		orderNewAdapter3 = new OrderNewAdapter3(this, showConfirmOrderOrPayBill);
		listView.setAdapter(orderNewAdapter3);

		// iDialog.paybill(OrderNewActivity3.this, salesController);
	}

	private SalesDetailsPost salesDetailsPost = new SalesDetailsPost() {

		@Override
		public void onSalesDetailsPost(String result) {
			// IConstants.showToast(result);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					OrderNewActivity3.this);

			// set title
			alertDialogBuilder.setTitle("Thank You");

			// set dialog message
			alertDialogBuilder.setMessage("Get your Bill from Server!")
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

			orderController.deleteOrderDetails();
			orderNewAdapter3.removeAllItems();
			orderNewAdapter3.notifyDataSetChanged();
		}
	};

	private ShowConfirmOrderOrPayBill showConfirmOrderOrPayBill = new ShowConfirmOrderOrPayBill() {

		@Override
		public void showConfirmOrderOrPayBill(Boolean b) {
			if (b) {
				confirmOrder.setVisibility(View.VISIBLE);
				cancelOrder.setVisibility(View.VISIBLE);
				payBill.setVisibility(View.GONE);
			} else {
				confirmOrder.setVisibility(View.GONE);
				cancelOrder.setVisibility(View.GONE);
				payBill.setVisibility(View.VISIBLE);
			}
		}
	};

	// private void setupActionBar() {
	// ActionBar actionBar = getActionBar();
	// actionBar.setDisplayHomeAsUpEnabled(true);
	//
	// actionBar.setBackgroundDrawable(new ColorDrawable(Color
	// .parseColor("#55000000")));
	// actionBar.setTitle("" + IConstants.appName);
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
		View actionBarView = inflator.inflate(R.layout.order_actionbar, null);
		TextView title = (TextView) actionBarView.findViewById(R.id.title);
		// title.setText("" + IConstants.appName);
		title.setText("My Orders");
		title.setTextSize(26);
		title.setTypeface(font);

		title.setShadowLayer(2, 2, 2, Color.BLACK);

		actionbarBg = (RelativeLayout) actionBarView
				.findViewById(R.id.actionbarBg);

		ImageView homeup = (ImageView) actionBarView.findViewById(R.id.homeup);
		// ImageView home = (ImageView) actionBarView.findViewById(R.id.home);

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

		ImageView quicmenu = (ImageView) actionBarView
				.findViewById(R.id.quicmenu);

		quicmenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		actionBar.setCustomView(actionBarView);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	//
	// MenuItem search = menu.add(0, 0, 0, null).setIcon(
	// R.drawable.ic_action_search);
	// search.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	//
	// MenuItem settings = menu.add(0, 1, 0, null).setIcon(
	// R.drawable.abs__ic_menu_moreoverflow_normal_holo_dark);
	// settings.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	//
	// return true;
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		case 0:

			iDialog.paybill(OrderNewActivity3.this, salesController);

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
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

	// ===========================OrderDetailsPost==================================

	private String orderResult = "";

	private OrderDetailsPost orderDetailsPostListener = new OrderDetailsPost() {

		@Override
		public void onOrderDetailsPost(String result) {

			// IConstants.showToast("OrderDetailsPost " + result);
			System.out.println("OrderDetailsPost " + result);

			orderResult = result;

			// AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			// OrderNewActivity3.this);
			//
			// // set title
			// alertDialogBuilder.setTitle("Thank You");
			//
			// // if (result.equalsIgnoreCase("success")) {
			// // result = "Your Order #### is Successfully placed";
			// // }
			//
			// // set dialog message
			// alertDialogBuilder.setMessage(result).setCancelable(false)
			// .setNeutralButton("Thanks", new OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// dialog.cancel();
			// }
			// });
			//
			// // create alert dialog
			// AlertDialog alertDialog = alertDialogBuilder.create();
			//
			// // show it
			// alertDialog.show();
			//
			// confirmOrder.setVisibility(View.GONE);
			// cancelOrder.setVisibility(View.GONE);
			// payBill.setVisibility(View.VISIBLE);

			try {
				JSONObject jsonObject = new JSONObject(result);

				if (jsonObject.has("orderNo")) {
					orderNo = Integer.parseInt(jsonObject.getString("orderNo"));
					orderController = new OrderController(
							getApplicationContext(), orderDetailsFetchListener);
					orderController.getOrderDetails(orderNo);
				} else {
					// IConstants.showToast(orderResult);
				}
			} catch (Exception e) {
				e.printStackTrace();

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						OrderNewActivity3.this);

				// set title
				alertDialogBuilder.setTitle("Thank You");

				// set dialog message
				alertDialogBuilder.setMessage(result).setCancelable(false)
						.setNeutralButton("Thanks", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

				iDialog.dismissProgress();
			}

			// iDialog.dismissProgress();
		}
	};

	private OrderDetailsFetch orderDetailsFetchListener = new OrderDetailsFetch() {

		@Override
		public void onOrderDetailsFetch(String result) {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					OrderNewActivity3.this);

			// set title
			alertDialogBuilder.setTitle("Thank You");

			// if (result.equalsIgnoreCase("success")) {
			// result = "Your Order #### is Successfully placed";
			// }

			JSONObject jsonObject = new JSONObject();

			try {
				jsonObject = new JSONObject(orderResult);

				// set dialog message
				alertDialogBuilder
						.setMessage(
								jsonObject.getString("successDesc").toString())
						.setCancelable(false)
						.setNeutralButton("Thanks", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			} catch (JSONException e) {
				e.printStackTrace();
			}

			confirmOrder.setVisibility(View.GONE);
			cancelOrder.setVisibility(View.GONE);
			payBill.setVisibility(View.VISIBLE);

			iDialog.dismissProgress();

			orderNewAdapter3.refreshList();

			showConfirmOrderOrPayBill.showConfirmOrderOrPayBill(false);
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			DatabaseManager.getInstance().closeHelper();
			if (orderController != null)
				orderController.cancelTasks();
		} catch (Exception e) {
			// TODO: OrderActivity handle exception
		}
	};
}
