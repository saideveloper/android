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
import com.restaurant.model.lkup.LkupRestTaxDetails;
import com.restaurant.model.lkup.LkupOrderStatusDetails;
import com.restaurant.util.JSONBind;
import com.restaurant.util.JSONParser;

public class LkupOrderStatusController {
	private Context mContext;
	private LkupOrderStatusDetailsFetch lkupOrderStatusDetailsFetch;
	private LkupOrderStatusDetailsPost lkupOrderStatusDetailsPost;
	private FetchAsynchTask fetchAsynchTask;
	private PostAsynchTask postAsynchTask;

	public LkupOrderStatusController(Context context) {
		mContext = context;
	}

	public LkupOrderStatusController(Context context,
			LkupOrderStatusDetailsPost lkupOrderStatusDetailsPost) {
		mContext = context;
		this.lkupOrderStatusDetailsPost = lkupOrderStatusDetailsPost;
	}

	public LkupOrderStatusController(Context context,
			LkupOrderStatusDetailsFetch lkupOrderStatusDetailsFetch) {
		mContext = context;
		this.lkupOrderStatusDetailsFetch = lkupOrderStatusDetailsFetch;
	}

	public interface LkupOrderStatusDetailsFetch {
		void onLkupOrderStatusDetailsFetch(String result);
	}

	public interface LkupOrderStatusDetailsPost {
		void onLkupOrderStatusDetailsPost(String result);
	}

	public void getLkupOrderStatusDetails() {
		fetchAsynchTask = new FetchAsynchTask();
		fetchAsynchTask.execute();
	}

	public void postLkupOrderStatusDetails(LkupOrderStatusDetails lkupOrderStatusDetails) {
		postAsynchTask = new PostAsynchTask(lkupOrderStatusDetails);
		postAsynchTask.execute();
	}

	public void deleteLkupOrderStatusDetails() {
		try {
//			DeleteBuilder<LkupOrderStatusDetails, Integer> deleteBuilder = DatabaseManager
//					.getInstance().getLkupOrderStatusDetailsDao().deleteBuilder();
//			deleteBuilder.delete();
			
			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), LkupOrderStatusDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), LkupOrderStatusDetails.class);
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
				JSONObject jsonResponse = jParser.restOtherApiCallWithArgs("OSDGT", "statusType='bill'");

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:

					JSONObject jsonParse = jsonResponse
							.getJSONObject("jsonData");

					deleteLkupOrderStatusDetails();

					JSONArray lkupOrderStatusDetailsArray = jsonParse
							.getJSONArray("orderStatusDetails");

					Gson gson = new Gson();

					System.err
							.println("-------------------------------------------------------------");

					for (int i = 0; i < lkupOrderStatusDetailsArray.length(); i++) {
						JSONObject jsonObject = lkupOrderStatusDetailsArray
								.getJSONObject(i);

						System.err.println(jsonObject.toString());

						LkupOrderStatusDetails lkupOrderStatusDetail = gson
								.fromJson(jsonObject.toString(),
										LkupOrderStatusDetails.class);

						DatabaseManager.getInstance()
								.getLkupOrderStatusDetailsDao()
								.create(lkupOrderStatusDetail);
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
				if (lkupOrderStatusDetailsFetch != null)
					lkupOrderStatusDetailsFetch.onLkupOrderStatusDetailsFetch(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class PostAsynchTask extends AsyncTask<String, Void, String> {
		private LkupOrderStatusDetails lkupOrderStatusDetails;

		public PostAsynchTask() {
		}

		public PostAsynchTask(LkupOrderStatusDetails lkupOrderStatusDetails) {
			this.lkupOrderStatusDetails = lkupOrderStatusDetails;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			try {

				Gson gson = new GsonBuilder().serializeNulls().create();
				String json = gson.toJson(lkupOrderStatusDetails);
				System.out.println(json);

				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				try {
					jsonArray.put(new JSONObject(json));
					jsonObject.put("orderStatusDetails", jsonArray);
					jsonObject = new JSONObject(jsonObject.toString());

					JSONBind jsonBind = new JSONBind();
					JSONObject jsonResponse = jsonBind.restApiCall("OSDPO",
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
				if (lkupOrderStatusDetailsPost != null)
					lkupOrderStatusDetailsPost.onLkupOrderStatusDetailsPost(result);
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
