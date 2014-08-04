package com.restaurant.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.TableUtils;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.RestAddonItemsDetails;
import com.restaurant.model.RestaurantInfoDetails;
import com.restaurant.util.JSONBind;
import com.restaurant.util.JSONParser;

public class RestaurantInfoController {
	private Context mContext;
	private RestaurantInfoDetailsFetch restaurantInfoDetailsFetch;
	private RestaurantInfoDetailsPost restaurantInfoDetailsPost;
	private FetchAsynchTask fetchAsynchTask;
	private PostAsynchTask postAsynchTask;

	public RestaurantInfoController(Context context) {
		mContext = context;
	}

	public RestaurantInfoController(Context context,
			RestaurantInfoDetailsPost restaurantInfoDetailsPost) {
		mContext = context;
		this.restaurantInfoDetailsPost = restaurantInfoDetailsPost;
	}

	public RestaurantInfoController(Context context,
			RestaurantInfoDetailsFetch restaurantInfoDetailsFetch) {
		mContext = context;
		this.restaurantInfoDetailsFetch = restaurantInfoDetailsFetch;
	}

	public interface RestaurantInfoDetailsFetch {
		void onRestaurantInfoDetailsFetch(String result);
	}

	public interface RestaurantInfoDetailsPost {
		void onRestaurantInfoDetailsPost(String result);
	}

	public void getRestaurantInfoDetails() {
		fetchAsynchTask = new FetchAsynchTask();
		fetchAsynchTask.execute();
	}

	public void postRestaurantInfoDetails(
			RestaurantInfoDetails restaurantInfoDetails) {
		postAsynchTask = new PostAsynchTask(restaurantInfoDetails);
		postAsynchTask.execute();
	}

	public void deleteRestaurantInfoDetails() {
		try {
//			DeleteBuilder<RestaurantInfoDetails, Integer> deleteBuilder = DatabaseManager
//					.getInstance().getRestaurantInfoDetailsDao().deleteBuilder();
//			deleteBuilder.delete();
			
			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), RestaurantInfoDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), RestaurantInfoDetails.class);
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
				JSONObject jsonResponse = jParser.restApiCall("RSIGT");

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:

					JSONObject jsonMenuInsert = jsonResponse
							.getJSONObject("jsonData");

					deleteRestaurantInfoDetails();

					JSONArray restaurantInfoDetailsArray = jsonMenuInsert
							.getJSONArray("restaurantInfoDetails");

					Gson gson = new Gson();

					System.err
							.println("-------------------------------------------------------------");

					for (int i = 0; i < restaurantInfoDetailsArray.length(); i++) {
						JSONObject jsonObject = restaurantInfoDetailsArray
								.getJSONObject(i);

						System.err.println(jsonObject.toString());

						RestaurantInfoDetails restaurantInfoDetail = gson
								.fromJson(jsonObject.toString(),
										RestaurantInfoDetails.class);

						DatabaseManager.getInstance()
								.getRestaurantInfoDetailsDao()
								.create(restaurantInfoDetail);
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
			System.out.println(result);
			try {
				if (restaurantInfoDetailsFetch != null)
					restaurantInfoDetailsFetch.onRestaurantInfoDetailsFetch(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class PostAsynchTask extends AsyncTask<String, Void, String> {
		private RestaurantInfoDetails restaurantInfoDetails;

		public PostAsynchTask() {
		}

		public PostAsynchTask(RestaurantInfoDetails restaurantInfoDetails) {
			this.restaurantInfoDetails = restaurantInfoDetails;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			try {

				Gson gson = new GsonBuilder().serializeNulls().create();
				String json = gson.toJson(restaurantInfoDetails);
				System.out.println(json);

				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				try {
					jsonArray.put(new JSONObject(json));
					jsonObject.put("restaurantInfoDetails", jsonArray);
					jsonObject = new JSONObject(jsonObject.toString());

					JSONBind jsonBind = new JSONBind();
					JSONObject jsonResponse = jsonBind.restApiCall("RSIPO",
							jsonObject.toString());

					switch (Integer.parseInt(jsonResponse.getString("status"))) {
					case 200:
						result = jsonResponse.getJSONObject("jsonData")
								.getString("successDesc");
						break;
					case 0:
						result = jsonResponse.getJSONObject("jsonData")
								.getString("errorDesc");
						break;
					default:
						result = jsonResponse.getJSONObject("jsonData")
								.getString("errorDesc");
						break;
					}
				} catch (Exception e) {
					result = "failure";
					e.printStackTrace();
				}
			} catch (Exception e) {
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println(result);
			try {
				if (restaurantInfoDetailsPost != null)
					restaurantInfoDetailsPost.onRestaurantInfoDetailsPost(result);
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
