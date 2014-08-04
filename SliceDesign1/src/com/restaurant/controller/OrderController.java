package com.restaurant.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.TableUtils;
import com.restaurant.controller.MenuController.FetchAsynchTask;
import com.restaurant.controller.MenuController.PostAsynchTask;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.ModifierQuestionAnswer;
import com.restaurant.model.OrderMasterDetails;
import com.restaurant.model.OrderMenuDetails;
import com.restaurant.model.OrderOfferDetails;
import com.restaurant.util.JSONBind;
import com.restaurant.util.JSONParser;

public class OrderController {
	private Context mContext;
	private OrderDetailsFetch orderDetailsFetchListener;
	private OrderDetailsPost orderDetailsPostListener;
	private FetchAsynchTask fetchAsynchTask;
	private PostAsynchTask postAsynchTask;

	public OrderController(Context context) {
		mContext = context;
	}

	public OrderController(Context context,
			OrderDetailsPost orderDetailsPostListener) {
		mContext = context;
		this.orderDetailsPostListener = orderDetailsPostListener;
	}

	public OrderController(Context context,
			OrderDetailsFetch orderDetailsFetchListener) {
		mContext = context;
		this.orderDetailsFetchListener = orderDetailsFetchListener;
	}

	public interface OrderDetailsFetch {
		void onOrderDetailsFetch(String result);
	}

	public interface OrderDetailsPost {
		void onOrderDetailsPost(String result);
	}

	public void getOrderDetails(int orderNo) {
		new FetchAsynchTask().execute("" + orderNo);
	}

	public void postOrderDetails(JSONObject jsonObject, JSONObject printerJsonObject) {
		new PostAsynchTask(jsonObject, printerJsonObject).execute();
	}

	public void deleteOrderDetails() {
		try {
			// DeleteBuilder<OrderMasterDetails, Integer>
			// orderMasterDeleteBuilder = DatabaseManager
			// .getInstance().getOrderMasterDetailsDao().deleteBuilder();
			// orderMasterDeleteBuilder.delete();
			//
			// DeleteBuilder<OrderMenuDetails, Integer> orderMenuDeleteBuilder =
			// DatabaseManager
			// .getInstance().getOrderMenuDetailsDao().deleteBuilder();
			// orderMenuDeleteBuilder.delete();
			//
			// DeleteBuilder<OrderOfferDetails, Integer> orderOfferDeleteBuilder
			// = DatabaseManager
			// .getInstance().getOrderOfferDetailsDao().deleteBuilder();
			// orderOfferDeleteBuilder.delete();

			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), OrderMasterDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), OrderMasterDetails.class);

			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), OrderMenuDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), OrderMenuDetails.class);

			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), OrderOfferDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), OrderOfferDetails.class);

		} catch (Exception e) {
		}
	}

	public class FetchAsynchTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			try {

				JSONParser jParser = new JSONParser();
				JSONObject jsonResponse;

				if (params.length > 0) {
					jsonResponse = jParser.restApiCallWithArgs("ORDGT", "orderNo=" + params[0]);
				} else {
					jsonResponse = jParser.restApiCall("ORDGT");
				}

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:

					JSONObject jsonOrderInsert = jsonResponse
							.getJSONObject("jsonData");

					deleteOrderDetails();

					// Getting JSON Array node
					JSONArray orderMasterDetailsArray = jsonOrderInsert
							.getJSONArray("orderMasterDetails");

					Gson gson = new GsonBuilder()
							.excludeFieldsWithoutExposeAnnotation().create();

					System.err
							.println("-------------------------------------------------------------");

					// looping through All Data
					for (int i = 0; i < orderMasterDetailsArray.length(); i++) {
						JSONObject masterJsonObject = orderMasterDetailsArray
								.getJSONObject(i);

						System.err.println(masterJsonObject.toString());

						OrderMasterDetails orderMasterDetail = gson.fromJson(
								masterJsonObject.toString(),
								OrderMasterDetails.class);

						DatabaseManager.getInstance()
								.getOrderMasterDetailsDao()
								.create(orderMasterDetail);

						JSONArray orderMenuArray = masterJsonObject
								.getJSONArray("itemList");

						for (int j = 0; j < orderMenuArray.length(); j++) {
							JSONObject jsonObject = orderMenuArray
									.getJSONObject(j);

							System.err.println(jsonObject.toString());

							gson = new Gson();

							OrderMenuDetails orderMenuDetail = gson.fromJson(
									jsonObject.toString(),
									OrderMenuDetails.class);

							// gson = new GsonBuilder()
							// .excludeFieldsWithoutExposeAnnotation().create();

							MenuDetails menuDetail = (DatabaseManager
									.getInstance().getMenuDetailsDao()
									.queryBuilder().distinct().where()
									.eq("itemId", orderMenuDetail.getItemId())
									.query()).get(0);

							orderMenuDetail.setItemPrice(menuDetail.getPrice());
							orderMenuDetail.setOrderNo(orderMasterDetail
									.getOrderNo());
							
							orderMenuDetail.setEditable(false);

							if (menuDetail.getModifierStatus()
									.equalsIgnoreCase("y")) {
								ModifierQuestionAnswer modifierQuestionAnswer = (DatabaseManager
										.getInstance()
										.getModifierQuestionAnswerDao()
										.queryBuilder()
										.distinct()
										.where()
										.eq("itemId",
												orderMenuDetail.getItemId())
										.query()).get(0);
								orderMenuDetail
										.setItemName(modifierQuestionAnswer
												.getItemName());
								orderMenuDetail.setModifierId(""
										+ orderMenuDetail.getItemId());
								orderMenuDetail
										.setModifierCategory(modifierQuestionAnswer
												.getModifierCategory());
							} else {
								orderMenuDetail.setItemName(menuDetail
										.getItemName());
								orderMenuDetail.setModifierId("");
								orderMenuDetail.setModifierCategory("");
							}

							orderMenuDetail.setModifierDesc("");

							String json = gson.toJson(orderMenuDetail);

							System.out.println("orderMenuDetail " + json);

							try {
								DatabaseManager.getInstance()
										.getOrderMenuDetailsDao()
										.create(orderMenuDetail);

							} catch (Exception e) {
								e.printStackTrace();
							}

							// try {
							// List<OrderMenuDetails> orderMenuDetailsList =
							// DatabaseManager
							// .getInstance().getOrderMenuDetailsDao().queryBuilder()
							// .distinct()
							// .query();
							//
							// System.out.println("orderMenuDetailsList.size() "
							// + orderMenuDetailsList.size());
							//
							// for (OrderMenuDetails omd : orderMenuDetailsList)
							// {
							// System.out.println(omd.getOrderMenuId());
							// }
							// } catch (Exception e) {
							// e.printStackTrace();
							// }
						}

						// JSONArray orderOfferArray = masterJsonObject
						// .getJSONArray("offerList");
						// // looping through All item
						// for (int j = 0; j < orderOfferArray.length(); j++) {
						// JSONObject jsonObject = orderOfferArray
						// .getJSONObject(j);
						//
						// System.err.println(jsonObject.toString());
						//
						// OrderOfferDetails orderOfferDetail = gson.fromJson(
						// jsonObject.toString(),
						// OrderOfferDetails.class);
						//
						// DatabaseManager.getInstance()
						// .getOrderOfferDetailsDao()
						// .create(orderOfferDetail);
						// }
					}

					System.err
							.println("-------------------------------------------------------------");

					// organiseMenuDetailsToInsert(jsonMenuInsert);

					// List<MenuDetails> menuDetailList =
					// organiseMenuDetailsToInsert(jsonMenuInsert);
					//
					// for (MenuDetails menuDetail : menuDetailList) {
					// DatabaseManager.getInstance().getMenuDetailsDao()
					// .create(menuDetail);
					// }
					result = "success";

					break;
				case 304:
					result = "notmodified";
					break;
				case 0:
					result = "failure";
					break;
				default:
					result = "failure";
					break;
				}

				// menuDetails =
				// DatabaseManager.getInstance().getMenuDetailsDao()
				// .queryForAll();

			} catch (Exception e) {
				result = "" + e;
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// System.out.println("onPostExecute " + result);
			// Toast.makeText(mContext, "onPostExecute " + result,
			// Toast.LENGTH_SHORT)
			// .show();
			try {
				// if (result.equals("success")) {
				// List<MenuDetails> orderDetails =
				// DatabaseManager.getInstance().getMenuDetailsDao().queryForAll();

				// if(orderDetails.size()>0){
				//
				// } else {
				//
				// }

				if (orderDetailsFetchListener != null)
					orderDetailsFetchListener.onOrderDetailsFetch(result);
				// } else {
				// System.out.println("onPostExecute");
				// // TODO Toast needed to display the response
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class PostAsynchTask extends AsyncTask<String, Void, String> {
		private JSONObject orderDetails, printerDetails;

		public PostAsynchTask() {
			// TODO Auto-generated constructor stub
		}

		public PostAsynchTask(JSONObject jsonObject, JSONObject printerDetails) {
			this.orderDetails = jsonObject;
			this.printerDetails = printerDetails;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			try {
				JSONBind jsonBind = new JSONBind();
				JSONObject jsonResponse = jsonBind.restApiCall("ORDPO",
						orderDetails.toString());

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:
					// result =
					// jsonResponse.getJSONObject("jsonData").getString(
					// "successDesc");
					result = jsonResponse.getJSONObject("jsonData").toString();
					break;
				case 0:
					result = jsonResponse.getJSONObject("jsonData").getString(
							"errorDesc");
					break;
				default:
					result = jsonResponse.getJSONObject("jsonData").getString(
							"errorDesc");
					break;
				}
				
				
				jsonResponse = jsonBind.restApiCall("PROPO",
						printerDetails.toString());
				
				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:
					// result =
					// jsonResponse.getJSONObject("jsonData").getString(
					// "successDesc");
					//result = jsonResponse.getJSONObject("jsonData").toString();
					break;
				case 0:
					//result = jsonResponse.getJSONObject("jsonData").getString(
					//		"errorDesc");
					break;
				default:
					//result = jsonResponse.getJSONObject("jsonData").getString(
					//		"errorDesc");
					break;
				}
				
			} catch (Exception e) {
				result = "failure";
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println(result);
			try {
				if (orderDetailsPostListener != null)
					orderDetailsPostListener.onOrderDetailsPost(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void cancelTasks() {
		if (fetchAsynchTask != null) {
			fetchAsynchTask.cancel(true);
		}
		if (postAsynchTask != null) {
			postAsynchTask.cancel(true);
		}
	}
}
