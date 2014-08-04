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
import com.restaurant.model.lkup.LkupTaxListDetails;
import com.restaurant.util.JSONBind;
import com.restaurant.util.JSONParser;

public class LkupTaxListController {
	private Context mContext;
	private LkupTaxListDetailsFetch lkupTaxListDetailsFetch;
	private LkupTaxListDetailsPost lkupTaxListDetailsPost;
	private FetchAsynchTask fetchAsynchTask;
	private PostAsynchTask postAsynchTask;

	public LkupTaxListController(Context context) {
		mContext = context;
	}

	public LkupTaxListController(Context context,
			LkupTaxListDetailsPost lkupTaxListDetailsPost) {
		mContext = context;
		this.lkupTaxListDetailsPost = lkupTaxListDetailsPost;
	}

	public LkupTaxListController(Context context,
			LkupTaxListDetailsFetch lkupTaxListDetailsFetch) {
		mContext = context;
		this.lkupTaxListDetailsFetch = lkupTaxListDetailsFetch;
	}

	public interface LkupTaxListDetailsFetch {
		void onLkupTaxListDetailsFetch(String result);
	}

	public interface LkupTaxListDetailsPost {
		void onLkupTaxListDetailsPost(String result);
	}

	public void getLkupTaxListDetails() {
		fetchAsynchTask = new FetchAsynchTask();
		fetchAsynchTask.execute();
	}

	public void postLkupTaxListDetails(LkupTaxListDetails lkupTaxListDetails) {
		postAsynchTask = new PostAsynchTask(lkupTaxListDetails);
		postAsynchTask.execute();
	}

	public void deleteLkupTaxListDetails() {
		try {
//			DeleteBuilder<LkupTaxListDetails, Integer> deleteBuilder = DatabaseManager
//					.getInstance().getLkupTaxListDetailsDao().deleteBuilder();
//			deleteBuilder.delete();
			
			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), LkupTaxListDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), LkupTaxListDetails.class);
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
				JSONObject jsonResponse = jParser.restOtherApiCall("TAXGT");

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:

					JSONObject jsonParse = jsonResponse
							.getJSONObject("jsonData");

					deleteLkupTaxListDetails();

					JSONArray lkupTaxListDetailsArray = jsonParse
							.getJSONArray("taxListDetails");

					Gson gson = new Gson();

					System.err
							.println("-------------------------------------------------------------");

					for (int i = 0; i < lkupTaxListDetailsArray.length(); i++) {
						JSONObject jsonObject = lkupTaxListDetailsArray
								.getJSONObject(i);

						System.err.println(jsonObject.toString());

						LkupTaxListDetails lkupTaxListDetail = gson
								.fromJson(jsonObject.toString(),
										LkupTaxListDetails.class);

						DatabaseManager.getInstance()
								.getLkupTaxListDetailsDao()
								.create(lkupTaxListDetail);
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
				if (lkupTaxListDetailsFetch != null)
					lkupTaxListDetailsFetch.onLkupTaxListDetailsFetch(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class PostAsynchTask extends AsyncTask<String, Void, String> {
		private LkupTaxListDetails lkupTaxListDetails;

		public PostAsynchTask() {
		}

		public PostAsynchTask(LkupTaxListDetails lkupTaxListDetails) {
			this.lkupTaxListDetails = lkupTaxListDetails;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			try {

				Gson gson = new GsonBuilder().serializeNulls().create();
				String json = gson.toJson(lkupTaxListDetails);
				System.out.println(json);

				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				try {
					jsonArray.put(new JSONObject(json));
					jsonObject.put("taxListDetails", jsonArray);
					jsonObject = new JSONObject(jsonObject.toString());

					JSONBind jsonBind = new JSONBind();
					JSONObject jsonResponse = jsonBind.restApiCall("TAXPO",
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
				if (lkupTaxListDetailsPost != null)
					lkupTaxListDetailsPost.onLkupTaxListDetailsPost(result);
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
