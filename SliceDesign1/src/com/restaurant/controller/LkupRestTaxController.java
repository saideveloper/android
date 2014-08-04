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
import com.restaurant.model.lkup.LkupPrepTypeDetails;
import com.restaurant.model.lkup.LkupRestTaxDetails;
import com.restaurant.util.JSONBind;
import com.restaurant.util.JSONParser;

public class LkupRestTaxController {
	private Context mContext;
	private LkupRestTaxDetailsFetch lkupRestTaxDetailsFetch;
	private LkupRestTaxDetailsPost lkupRestTaxDetailsPost;
	private FetchAsynchTask fetchAsynchTask;
	private PostAsynchTask postAsynchTask;

	public LkupRestTaxController(Context context) {
		mContext = context;
	}

	public LkupRestTaxController(Context context,
			LkupRestTaxDetailsPost lkupRestTaxDetailsPost) {
		mContext = context;
		this.lkupRestTaxDetailsPost = lkupRestTaxDetailsPost;
	}

	public LkupRestTaxController(Context context,
			LkupRestTaxDetailsFetch lkupRestTaxDetailsFetch) {
		mContext = context;
		this.lkupRestTaxDetailsFetch = lkupRestTaxDetailsFetch;
	}

	public interface LkupRestTaxDetailsFetch {
		void onLkupRestTaxDetailsFetch(String result);
	}

	public interface LkupRestTaxDetailsPost {
		void onLkupRestTaxDetailsPost(String result);
	}

	public void getLkupRestTaxDetails() {
		fetchAsynchTask = new FetchAsynchTask();
		fetchAsynchTask.execute();
	}

	public void postLkupRestTaxDetails(LkupRestTaxDetails lkupRestTaxDetails) {
		postAsynchTask = new PostAsynchTask(lkupRestTaxDetails);
		postAsynchTask.execute();
	}

	public void deleteLkupRestTaxDetails() {
		try {
//			DeleteBuilder<LkupRestTaxDetails, Integer> deleteBuilder = DatabaseManager
//					.getInstance().getLkupRestTaxDetailsDao().deleteBuilder();
//			deleteBuilder.delete();
			
			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), LkupRestTaxDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), LkupRestTaxDetails.class);
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
				JSONObject jsonResponse = jParser.restApiCall("RTXGT");

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:

					JSONObject jsonParse = jsonResponse
							.getJSONObject("jsonData");

					deleteLkupRestTaxDetails();

					JSONArray lkupRestTaxDetailsArray = jsonParse
							.getJSONArray("restTaxDetails");

					Gson gson = new Gson();

					System.err
							.println("-------------------------------------------------------------");

					for (int i = 0; i < lkupRestTaxDetailsArray.length(); i++) {
						JSONObject jsonObject = lkupRestTaxDetailsArray
								.getJSONObject(i);

						System.err.println(jsonObject.toString());

						LkupRestTaxDetails lkupRestTaxDetail = gson
								.fromJson(jsonObject.toString(),
										LkupRestTaxDetails.class);

						DatabaseManager.getInstance()
								.getLkupRestTaxDetailsDao()
								.create(lkupRestTaxDetail);
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
				if (lkupRestTaxDetailsFetch != null)
					lkupRestTaxDetailsFetch.onLkupRestTaxDetailsFetch(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class PostAsynchTask extends AsyncTask<String, Void, String> {
		private LkupRestTaxDetails lkupRestTaxDetails;

		public PostAsynchTask() {
		}

		public PostAsynchTask(LkupRestTaxDetails lkupRestTaxDetails) {
			this.lkupRestTaxDetails = lkupRestTaxDetails;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			try {

				Gson gson = new GsonBuilder().serializeNulls().create();
				String json = gson.toJson(lkupRestTaxDetails);
				System.out.println(json);

				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				try {
					jsonArray.put(new JSONObject(json));
					jsonObject.put("restTaxDetails", jsonArray);
					jsonObject = new JSONObject(jsonObject.toString());

					JSONBind jsonBind = new JSONBind();
					JSONObject jsonResponse = jsonBind.restApiCall("RTXPO",
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
				if (lkupRestTaxDetailsPost != null)
					lkupRestTaxDetailsPost.onLkupRestTaxDetailsPost(result);
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
