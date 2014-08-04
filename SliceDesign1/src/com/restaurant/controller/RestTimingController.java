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
import com.restaurant.model.RestTimingDetails;
import com.restaurant.util.JSONBind;
import com.restaurant.util.JSONParser;

public class RestTimingController {
	private Context mContext;
	private RestTimingDetailsFetch restTimingDetailsFetch;
	private RestTimingDetailsPost restTimingDetailsPost;
	private FetchAsynchTask fetchAsynchTask;
	private PostAsynchTask postAsynchTask;

	public RestTimingController(Context context) {
		mContext = context;
	}

	public RestTimingController(Context context,
			RestTimingDetailsPost restTimingDetailsPost) {
		mContext = context;
		this.restTimingDetailsPost = restTimingDetailsPost;
	}

	public RestTimingController(Context context,
			RestTimingDetailsFetch restTimingDetailsFetch) {
		mContext = context;
		this.restTimingDetailsFetch = restTimingDetailsFetch;
	}

	public interface RestTimingDetailsFetch {
		void onRestTimingDetailsFetch(String result);
	}

	public interface RestTimingDetailsPost {
		void onRestTimingDetailsPost(String result);
	}

	public void getRestTimingDetails() {
		fetchAsynchTask = new FetchAsynchTask();
		fetchAsynchTask.execute();
	}

	public void postRestTimingDetails(
			RestTimingDetails restTimingDetails) {
		postAsynchTask = new PostAsynchTask(restTimingDetails);
		postAsynchTask.execute();
	}

	public void deleteRestTimingDetails() {
		try {
//			DeleteBuilder<RestTimingDetails, Integer> deleteBuilder = DatabaseManager
//					.getInstance().getRestTimingDetailsDao().deleteBuilder();
//			deleteBuilder.delete();
			
			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), RestTimingDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), RestTimingDetails.class);
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
				JSONObject jsonResponse = jParser.restApiCall("RTDGT");

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:

					JSONObject jsonMenuInsert = jsonResponse
							.getJSONObject("jsonData");

					deleteRestTimingDetails();

					JSONArray restTimingDetailsArray = jsonMenuInsert
							.getJSONArray("restTimingDetails");

					Gson gson = new Gson();

					System.err
							.println("-------------------------------------------------------------");

					for (int i = 0; i < restTimingDetailsArray.length(); i++) {
						JSONObject jsonObject = restTimingDetailsArray
								.getJSONObject(i);

						System.err.println(jsonObject.toString());

						RestTimingDetails restTimingDetail = gson
								.fromJson(jsonObject.toString(),
										RestTimingDetails.class);

						DatabaseManager.getInstance()
								.getRestTimingDetailsDao()
								.create(restTimingDetail);
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
				if (restTimingDetailsFetch != null)
					restTimingDetailsFetch.onRestTimingDetailsFetch(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class PostAsynchTask extends AsyncTask<String, Void, String> {
		private RestTimingDetails restTimingDetails;

		public PostAsynchTask() {
		}

		public PostAsynchTask(RestTimingDetails restTimingDetails) {
			this.restTimingDetails = restTimingDetails;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			try {

				Gson gson = new GsonBuilder().serializeNulls().create();
				String json = gson.toJson(restTimingDetails);
				System.out.println(json);

				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				try {
					jsonArray.put(new JSONObject(json));
					jsonObject.put("restTimingDetails", jsonArray);
					jsonObject = new JSONObject(jsonObject.toString());

					JSONBind jsonBind = new JSONBind();
					JSONObject jsonResponse = jsonBind.restApiCall("RTDPO",
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
				if (restTimingDetailsPost != null)
					restTimingDetailsPost.onRestTimingDetailsPost(result);
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
