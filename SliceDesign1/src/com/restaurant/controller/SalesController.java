package com.restaurant.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.table.TableUtils;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.SalesDetails;
import com.restaurant.util.JSONBind;
import com.restaurant.util.JSONParser;

public class SalesController {
	private Context mContext;
	private SalesDetailsFetch salesDetailsFetchListener;
	private SalesDetailsPost salesDetailsPostListener;
	private FetchAsynchTask fetchAsynchTask;
	private PostAsynchTask postAsynchTask;

	public SalesController(Context context) {
		mContext = context;
	}

	public SalesController(Context context,
			SalesDetailsPost salesDetailsPostListener) {
		mContext = context;
		this.salesDetailsPostListener = salesDetailsPostListener;
	}

	public SalesController(Context context,
			SalesDetailsFetch salesDetailsFetchListener) {
		mContext = context;
		this.salesDetailsFetchListener = salesDetailsFetchListener;
	}

	public interface SalesDetailsFetch {
		void onSalesDetailsFetch(String result);
	}

	public interface SalesDetailsPost {
		void onSalesDetailsPost(String result);
	}

	public void getsalesDetails(int salesNo) {
		new FetchAsynchTask().execute("" + salesNo);
	}

	public void postsalesDetails(JSONObject jsonObject,
			SalesDetails salesDetails) {
		new PostAsynchTask(jsonObject, salesDetails).execute();
	}

	public void deletesalesDetails() {
		try {
			// DeleteBuilder<SalesDetails, Integer>
			// salesMasterDeleteBuilder = DatabaseManager
			// .getInstance().getSalesDetailsDao().deleteBuilder();
			// salesMasterDeleteBuilder.delete();

			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), SalesDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), SalesDetails.class);

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
					jsonResponse = jParser.restApiCallWithArgs("SLEGT",
							"salesNo=" + params[0]);
				} else {
					jsonResponse = jParser.restApiCall("SLEGT");
				}

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:

					JSONObject jsonsalesInsert = jsonResponse
							.getJSONObject("jsonData");

					deletesalesDetails();

					// Getting JSON Array node
					JSONArray salesMasterDetailsArray = jsonsalesInsert
							.getJSONArray("salesDetails");

					Gson gson = new GsonBuilder()
							.excludeFieldsWithoutExposeAnnotation().create();

					System.err
							.println("-------------------------------------------------------------");

					// looping through All Data
					for (int i = 0; i < salesMasterDetailsArray.length(); i++) {
						JSONObject masterJsonObject = salesMasterDetailsArray
								.getJSONObject(i);

						System.err.println(masterJsonObject.toString());

						SalesDetails salesDetail = gson
								.fromJson(masterJsonObject.toString(),
										SalesDetails.class);

						// DatabaseManager.getInstance()
						// .getSalesDetailsDao()
						// .create(salesDetail);
					}

					System.err
							.println("-------------------------------------------------------------");

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
				// List<MenuDetails> salesDetails =
				// DatabaseManager.getInstance().getMenuDetailsDao().queryForAll();

				// if(salesDetails.size()>0){
				//
				// } else {
				//
				// }

				if (salesDetailsFetchListener != null)
					salesDetailsFetchListener.onSalesDetailsFetch(result);
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
		private JSONObject salesDetails;
		private SalesDetails printerDetails;

		public PostAsynchTask() {
			// TODO Auto-generated constructor stub
		}

		public PostAsynchTask(JSONObject salesDetails,
				SalesDetails printerDetails) {
			this.salesDetails = salesDetails;
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
				JSONObject jsonResponse = jsonBind.restApiCall("SLEPO",
						salesDetails.toString());

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:
					result = jsonResponse.getJSONObject("jsonData").getString(
							"status");
					
					printerDetails.setReceiptNo(Integer.parseInt(jsonResponse
							.getJSONObject("jsonData").getString("receiptNo")));

					Gson gson = new GsonBuilder()
							.excludeFieldsWithoutExposeAnnotation().create();

					String json = gson.toJson(printerDetails);
					JSONObject jsonObject = new JSONObject();
					JSONArray jsonArray = new JSONArray();

					jsonArray.put(new JSONObject(json));
					jsonObject.put("salesDetails", jsonArray);
					jsonObject = new JSONObject(jsonObject.toString());
					System.out.println(jsonObject.toString());
					
					jsonResponse = jsonBind.restApiCallWithExtras("PRNPO", "PrintBill",
							jsonObject.toString());
					
					switch (Integer.parseInt(jsonResponse.getString("status"))) {
					case 200:
//						result = jsonResponse.getJSONObject("jsonData").getString(
//								"status");
						// result =
						// jsonResponse.getJSONObject("jsonData").toString();
						break;
					case 0:
//						result = jsonResponse.getJSONObject("jsonData").getString(
//								"errorDesc");
						break;
					default:
//						result = jsonResponse.getJSONObject("jsonData").getString(
//								"errorDesc");
						break;
					}
					
					// result =
					// jsonResponse.getJSONObject("jsonData").toString();
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
				if (salesDetailsPostListener != null)
					salesDetailsPostListener.onSalesDetailsPost(result);
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
