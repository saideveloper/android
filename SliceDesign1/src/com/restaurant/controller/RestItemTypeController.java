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
import com.restaurant.model.RestItemTypeDetails;
import com.restaurant.model.RestaurantInfoDetails;
import com.restaurant.util.JSONBind;
import com.restaurant.util.JSONParser;

public class RestItemTypeController {
	private Context mContext;
	private RestItemTypeDetailsFetch restItemTypeDetailsFetch;
	private RestItemTypeDetailsPost restItemTypeDetailsPost;
	private FetchAsynchTask fetchAsynchTask;
	private PostAsynchTask postAsynchTask;

	public RestItemTypeController(Context context) {
		mContext = context;
	}

	public RestItemTypeController(Context context,
			RestItemTypeDetailsPost restItemTypeDetailsPost) {
		mContext = context;
		this.restItemTypeDetailsPost = restItemTypeDetailsPost;
	}

	public RestItemTypeController(Context context,
			RestItemTypeDetailsFetch restItemTypeDetailsFetch) {
		mContext = context;
		this.restItemTypeDetailsFetch = restItemTypeDetailsFetch;
	}

	public interface RestItemTypeDetailsFetch {
		void onRestItemTypeDetailsFetch(String result);
	}

	public interface RestItemTypeDetailsPost {
		void onRestItemTypeDetailsPost(String result);
	}

	public void getRestItemTypeDetails() {
		fetchAsynchTask = new FetchAsynchTask();
		fetchAsynchTask.execute();
	}

	public void postRestItemTypeDetails(
			RestItemTypeDetails restItemTypeDetails) {
		postAsynchTask = new PostAsynchTask(restItemTypeDetails);
		postAsynchTask.execute();
	}

	public void deleteRestItemTypeDetails() {
		try {
//			DeleteBuilder<RestItemTypeDetails, Integer> deleteBuilder = DatabaseManager
//					.getInstance().getRestItemTypeDetailsDao().deleteBuilder();
//			deleteBuilder.delete();
			
			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), RestItemTypeDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), RestItemTypeDetails.class);
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
				JSONObject jsonResponse = jParser.restApiCall("RITGT");

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:

					JSONObject jsonMenuInsert = jsonResponse
							.getJSONObject("jsonData");

					deleteRestItemTypeDetails();

					JSONArray restItemTypeDetailsArray = jsonMenuInsert
							.getJSONArray("restItemType");

					Gson gson = new Gson();

					System.err
							.println("-------------------------------------------------------------");

					for (int i = 0; i < restItemTypeDetailsArray.length(); i++) {
						JSONObject jsonObject = restItemTypeDetailsArray
								.getJSONObject(i);

						System.err.println(jsonObject.toString());

						RestItemTypeDetails restItemTypeDetail = gson
								.fromJson(jsonObject.toString(),
										RestItemTypeDetails.class);

						DatabaseManager.getInstance()
								.getRestItemTypeDetailsDao()
								.create(restItemTypeDetail);
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
				if (restItemTypeDetailsFetch != null)
					restItemTypeDetailsFetch.onRestItemTypeDetailsFetch(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class PostAsynchTask extends AsyncTask<String, Void, String> {
		private RestItemTypeDetails restItemTypeDetails;

		public PostAsynchTask() {
		}

		public PostAsynchTask(RestItemTypeDetails restItemTypeDetails) {
			this.restItemTypeDetails = restItemTypeDetails;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			try {

				Gson gson = new GsonBuilder().serializeNulls().create();
				String json = gson.toJson(restItemTypeDetails);
				System.out.println(json);

				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				try {
					jsonArray.put(new JSONObject(json));
					jsonObject.put("restItemType", jsonArray);
					jsonObject = new JSONObject(jsonObject.toString());

					JSONBind jsonBind = new JSONBind();
					JSONObject jsonResponse = jsonBind.restApiCall("RITPO",
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
				if (restItemTypeDetailsPost != null)
					restItemTypeDetailsPost.onRestItemTypeDetailsPost(result);
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
