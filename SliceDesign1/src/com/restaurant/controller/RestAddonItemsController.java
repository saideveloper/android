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
import com.restaurant.model.OfferDetails;
import com.restaurant.model.RestAddonItemsDetails;
import com.restaurant.util.JSONBind;
import com.restaurant.util.JSONParser;

public class RestAddonItemsController {
	private Context mContext;
	private RestAddonItemsDetailsFetch restAddonItemsDetailsFetch;
	private RestAddonItemsDetailsPost restAddonItemsDetailsPost;
	private FetchAsynchTask fetchAsynchTask;
	private PostAsynchTask postAsynchTask;

	public RestAddonItemsController(Context context) {
		mContext = context;
	}

	public RestAddonItemsController(Context context,
			RestAddonItemsDetailsPost restAddonItemsDetailsPost) {
		mContext = context;
		this.restAddonItemsDetailsPost = restAddonItemsDetailsPost;
	}

	public RestAddonItemsController(Context context,
			RestAddonItemsDetailsFetch restAddonItemsDetailsFetch) {
		mContext = context;
		this.restAddonItemsDetailsFetch = restAddonItemsDetailsFetch;
	}

	public interface RestAddonItemsDetailsFetch {
		void onRestAddonItemsDetailsFetch(String result);
	}

	public interface RestAddonItemsDetailsPost {
		void onRestAddonItemsDetailsPost(String result);
	}

	public void getRestAddonItemsDetails() {
		fetchAsynchTask = new FetchAsynchTask();
		fetchAsynchTask.execute();
	}

	public void postRestAddonItemsDetails(
			RestAddonItemsDetails restAddonItemsDetails) {
		postAsynchTask = new PostAsynchTask(restAddonItemsDetails);
		postAsynchTask.execute();
	}

	public void deleteRestAddonItemsDetails() {
		try {
//			DeleteBuilder<RestAddonItemsDetails, Integer> deleteBuilder = DatabaseManager
//					.getInstance().getRestAddonItemsDetailsDao()
//					.deleteBuilder();
//			deleteBuilder.delete();
			
			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), RestAddonItemsDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), RestAddonItemsDetails.class);
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
				JSONObject jsonResponse = jParser.restApiCall("RAIGT");

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:

					JSONObject jsonMenuInsert = jsonResponse
							.getJSONObject("jsonData");

					deleteRestAddonItemsDetails();

					JSONArray restAddonItemsDetailsArray = jsonMenuInsert
							.getJSONArray("restAddonItemsDetails");

					Gson gson = new Gson();

					System.err
							.println("-------------------------------------------------------------");

					for (int i = 0; i < restAddonItemsDetailsArray.length(); i++) {
						JSONObject jsonObject = restAddonItemsDetailsArray
								.getJSONObject(i);

						System.err.println(jsonObject.toString());

						RestAddonItemsDetails restAddonItemsDetail = gson
								.fromJson(jsonObject.toString(),
										RestAddonItemsDetails.class);

						DatabaseManager.getInstance()
								.getRestAddonItemsDetailsDao()
								.create(restAddonItemsDetail);
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
				if (restAddonItemsDetailsFetch != null)
					restAddonItemsDetailsFetch
							.onRestAddonItemsDetailsFetch(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class PostAsynchTask extends AsyncTask<String, Void, String> {
		private RestAddonItemsDetails restAddonItemsDetails;

		public PostAsynchTask() {
			// TODO Auto-generated constructor stub
		}

		public PostAsynchTask(RestAddonItemsDetails restAddonItemsDetails) {
			this.restAddonItemsDetails = restAddonItemsDetails;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			try {

				Gson gson = new GsonBuilder().serializeNulls().create();
				String json = gson.toJson(restAddonItemsDetails);
				System.out.println(json);

				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				try {
					jsonArray.put(new JSONObject(json));
					jsonObject.put("restAddonItemsDetails", jsonArray);
					jsonObject = new JSONObject(jsonObject.toString());

					// System.out.println(jsonObject.getJSONArray("RestAddonItemsDetails").getJSONObject(0).getString("itemName"));

					JSONBind jsonBind = new JSONBind();
					JSONObject jsonResponse = jsonBind.restApiCall("RAIPO",
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
				if (restAddonItemsDetailsPost != null)
					restAddonItemsDetailsPost
							.onRestAddonItemsDetailsPost(result);
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
