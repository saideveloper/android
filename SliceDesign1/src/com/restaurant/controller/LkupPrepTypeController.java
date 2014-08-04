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
import com.restaurant.model.lkup.LkupPayTypeDetails;
import com.restaurant.model.lkup.LkupPrepTypeDetails;
import com.restaurant.util.JSONBind;
import com.restaurant.util.JSONParser;

public class LkupPrepTypeController {
	private Context mContext;
	private LkupPrepTypeDetailsFetch lkupPrepTypeDetailsFetch;
	private LkupPrepTypeDetailsPost lkupPrepTypeDetailsPost;
	private FetchAsynchTask fetchAsynchTask;
	private PostAsynchTask postAsynchTask;

	public LkupPrepTypeController(Context context) {
		mContext = context;
	}

	public LkupPrepTypeController(Context context,
			LkupPrepTypeDetailsPost lkupPrepTypeDetailsPost) {
		mContext = context;
		this.lkupPrepTypeDetailsPost = lkupPrepTypeDetailsPost;
	}

	public LkupPrepTypeController(Context context,
			LkupPrepTypeDetailsFetch lkupPrepTypeDetailsFetch) {
		mContext = context;
		this.lkupPrepTypeDetailsFetch = lkupPrepTypeDetailsFetch;
	}

	public interface LkupPrepTypeDetailsFetch {
		void onLkupPrepTypeDetailsFetch(String result);
	}

	public interface LkupPrepTypeDetailsPost {
		void onLkupPrepTypeDetailsPost(String result);
	}

	public void getLkupPrepTypeDetails() {
		fetchAsynchTask = new FetchAsynchTask();
		fetchAsynchTask.execute();
	}

	public void postLkupPrepTypeDetails(LkupPrepTypeDetails lkupPrepTypeDetails) {
		postAsynchTask = new PostAsynchTask(lkupPrepTypeDetails);
		postAsynchTask.execute();
	}

	public void deleteLkupPrepTypeDetails() {
		try {
//			DeleteBuilder<LkupPrepTypeDetails, Integer> deleteBuilder = DatabaseManager
//					.getInstance().getLkupPrepTypeDetailsDao().deleteBuilder();
//			deleteBuilder.delete();
			
			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), LkupPrepTypeDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), LkupPrepTypeDetails.class);
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
				JSONObject jsonResponse = jParser.restApiCall("PPTGT");

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:

					JSONObject jsonMenuInsert = jsonResponse
							.getJSONObject("jsonData");

					deleteLkupPrepTypeDetails();

					JSONArray lkupPrepTypeDetailsArray = jsonMenuInsert
							.getJSONArray("prepTypeDetails");

					Gson gson = new Gson();

					System.err
							.println("-------------------------------------------------------------");

					for (int i = 0; i < lkupPrepTypeDetailsArray.length(); i++) {
						JSONObject jsonObject = lkupPrepTypeDetailsArray
								.getJSONObject(i);

						System.err.println(jsonObject.toString());

						LkupPrepTypeDetails lkupPrepTypeDetail = gson
								.fromJson(jsonObject.toString(),
										LkupPrepTypeDetails.class);

						DatabaseManager.getInstance()
								.getLkupPrepTypeDetailsDao()
								.create(lkupPrepTypeDetail);
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
				if (lkupPrepTypeDetailsFetch != null)
					lkupPrepTypeDetailsFetch.onLkupPrepTypeDetailsFetch(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class PostAsynchTask extends AsyncTask<String, Void, String> {
		private LkupPrepTypeDetails lkupPrepTypeDetails;

		public PostAsynchTask() {
		}

		public PostAsynchTask(LkupPrepTypeDetails lkupPrepTypeDetails) {
			this.lkupPrepTypeDetails = lkupPrepTypeDetails;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			try {

				Gson gson = new GsonBuilder().serializeNulls().create();
				String json = gson.toJson(lkupPrepTypeDetails);
				System.out.println(json);

				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				try {
					jsonArray.put(new JSONObject(json));
					jsonObject.put("prepTypeDetails", jsonArray);
					jsonObject = new JSONObject(jsonObject.toString());

					JSONBind jsonBind = new JSONBind();
					JSONObject jsonResponse = jsonBind.restApiCall("PPTPO",
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
				if (lkupPrepTypeDetailsPost != null)
					lkupPrepTypeDetailsPost.onLkupPrepTypeDetailsPost(result);
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
