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
import com.restaurant.model.MenuDetails;
import com.restaurant.model.lkup.LkupPayTypeDetails;
import com.restaurant.util.JSONBind;
import com.restaurant.util.JSONParser;

public class LkupPayTypeController {
	private Context mContext;
	private LkupPayTypeDetailsFetch lkupPayTypeDetailsFetch;
	private LkupPayTypeDetailsPost lkupPayTypeDetailsPost;
	private FetchAsynchTask fetchAsynchTask;
	private PostAsynchTask postAsynchTask;

	public LkupPayTypeController(Context context) {
		mContext = context;
	}

	public LkupPayTypeController(Context context,
			LkupPayTypeDetailsPost lkupPayTypeDetailsPost) {
		mContext = context;
		this.lkupPayTypeDetailsPost = lkupPayTypeDetailsPost;
	}

	public LkupPayTypeController(Context context,
			LkupPayTypeDetailsFetch lkupPayTypeDetailsFetch) {
		mContext = context;
		this.lkupPayTypeDetailsFetch = lkupPayTypeDetailsFetch;
	}

	public interface LkupPayTypeDetailsFetch {
		void onLkupPayTypeDetailsFetch(String result);
	}

	public interface LkupPayTypeDetailsPost {
		void onLkupPayTypeDetailsPost(String result);
	}

	public void getLkupPayTypeDetails() {
		fetchAsynchTask = new FetchAsynchTask();
		fetchAsynchTask.execute();
	}

	public void postLkupPayTypeDetails(LkupPayTypeDetails lkupPayTypeDetails) {
		postAsynchTask = new PostAsynchTask(lkupPayTypeDetails);
		postAsynchTask.execute();
	}

	public void deleteLkupPayTypeDetails() {
		try {
//			DeleteBuilder<LkupPayTypeDetails, Integer> deleteBuilder = DatabaseManager
//					.getInstance().getLkupPayTypeDetailsDao().deleteBuilder();
//			deleteBuilder.delete();
			
			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), LkupPayTypeDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), LkupPayTypeDetails.class);
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
				JSONObject jsonResponse = jParser.restApiCall("PAYGT");

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:

					JSONObject jsonMenuInsert = jsonResponse
							.getJSONObject("jsonData");

					deleteLkupPayTypeDetails();

					JSONArray lkupPayTypeDetailsArray = jsonMenuInsert
							.getJSONArray("payTypeDetails");

					Gson gson = new Gson();

					System.err
							.println("-------------------------------------------------------------");

					for (int i = 0; i < lkupPayTypeDetailsArray.length(); i++) {
						JSONObject jsonObject = lkupPayTypeDetailsArray
								.getJSONObject(i);

						System.err.println(jsonObject.toString());

						LkupPayTypeDetails lkupPayTypeDetail = gson
								.fromJson(jsonObject.toString(),
										LkupPayTypeDetails.class);

						DatabaseManager.getInstance()
								.getLkupPayTypeDetailsDao()
								.create(lkupPayTypeDetail);
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
				if (lkupPayTypeDetailsFetch != null)
					lkupPayTypeDetailsFetch.onLkupPayTypeDetailsFetch(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class PostAsynchTask extends AsyncTask<String, Void, String> {
		private LkupPayTypeDetails lkupPayTypeDetails;

		public PostAsynchTask() {
		}

		public PostAsynchTask(LkupPayTypeDetails lkupPayTypeDetails) {
			this.lkupPayTypeDetails = lkupPayTypeDetails;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			try {

				Gson gson = new GsonBuilder().serializeNulls().create();
				String json = gson.toJson(lkupPayTypeDetails);
				System.out.println(json);

				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				try {
					jsonArray.put(new JSONObject(json));
					jsonObject.put("payTypeDetails", jsonArray);
					jsonObject = new JSONObject(jsonObject.toString());

					JSONBind jsonBind = new JSONBind();
					JSONObject jsonResponse = jsonBind.restApiCall("PAYPO",
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
				if (lkupPayTypeDetailsPost != null)
					lkupPayTypeDetailsPost.onLkupPayTypeDetailsPost(result);
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
