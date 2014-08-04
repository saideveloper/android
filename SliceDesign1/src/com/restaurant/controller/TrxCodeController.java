package com.restaurant.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.TableUtils;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.OrderMasterDetails;
import com.restaurant.model.TrxCodeDetails;
import com.restaurant.util.JSONParser;

public class TrxCodeController extends AsyncTask<String, Void, String> {

	private Context mContext;
	private TrxCodeDetailsFetch trxCodeDetailsFetch;

	public interface TrxCodeDetailsFetch {
		void onTrxCodeDetailsFetch(String result);
	}

	public TrxCodeController(Context context, TrxCodeDetailsFetch trxCodeDetailsFetch) {
		mContext = context;
		this.trxCodeDetailsFetch = trxCodeDetailsFetch;
	}

	public void deleteTrxCodeDetails() {
		try {
//			DeleteBuilder<TrxCodeDetails, Integer> deleteBuilder = DatabaseManager
//					.getInstance().getTrxCodeDetailsDao().deleteBuilder();
//			deleteBuilder.delete();
			
			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), TrxCodeDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), TrxCodeDetails.class);
		} catch (Exception e) {
		}
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected String doInBackground(String... params) {
		String result = "";
		// List<TrxCodeDetails> trxCodeDetails = new
		// ArrayList<TrxCodeDetails>();

		try {
			List<TrxCodeDetails> trxCodeDetailsList = DatabaseManager
					.getInstance().getTrxCodeDetailsDao().queryForAll();

			if (trxCodeDetailsList.size() == 0) {
				JSONParser jParser = new JSONParser();
				JSONObject jsonResponse = jParser.restSpecialApiCall("TrxCode");

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:

					JSONObject jsonTrxCodeInsert = jsonResponse
							.getJSONObject("jsonData");

					// trxCodeDetailsList =
					// organiseTrxCodeDetailsInsert(jsonTrxCodeInsert);

					deleteTrxCodeDetails();

					// Getting JSON Array node
					JSONArray trxCodeDetailsArray = jsonTrxCodeInsert
							.getJSONArray("trxCodeDetails");

					Gson gson = new Gson();

					// looping through All Data
					for (int i = 0; i < trxCodeDetailsArray.length(); i++) {
						JSONObject jsonObject = trxCodeDetailsArray
								.getJSONObject(i);

						TrxCodeDetails trxCodeDetail = gson.fromJson(
								jsonObject.toString(), TrxCodeDetails.class);
						
						trxCodeDetail.setETag("xyz");

						DatabaseManager.getInstance().getTrxCodeDetailsDao()
								.create(trxCodeDetail);
					}

					// for (TrxCodeDetails trxCodeDetail : trxCodeDetailsList) {
					// DatabaseManager.getInstance().getTrxCodeDetailsDao().create(trxCodeDetail);
					// }

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
			} else {
				result = "success";
			}

			// trxCodeDetails =
			// DatabaseManager.getInstance().getTrxCodeDetailsDao().queryForAll();
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
			// List<TrxCodeDetails> trxCodeDetails =
			// DatabaseManager.getInstance().getTrxCodeDetailsDao().queryForAll();

			if (trxCodeDetailsFetch != null)
				trxCodeDetailsFetch.onTrxCodeDetailsFetch(result);
			// } else {
			// System.out.println("onPostExecute");
			// // TODO Toast needed to display the response
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public List<TrxCodeDetails> organiseTrxCodeDetailsInsert(
	// JSONObject jsonMenuInsert) throws ParseException, IOException {
	// List<TrxCodeDetails> trxCodeDetailsList = new
	// ArrayList<TrxCodeDetails>();
	//
	// if (jsonMenuInsert != null) {
	// try {
	//
	// // Getting JSON Array node
	// JSONArray trxCodeDetailsArray = jsonMenuInsert
	// .getJSONArray("trxCodeDetails");
	//
	// // looping through All Data
	// for (int i = 0; i < trxCodeDetailsArray.length(); i++) {
	// JSONObject mu = trxCodeDetailsArray.getJSONObject(i);
	//
	// TrxCodeDetails trxCodeDetails = new TrxCodeDetails();
	//
	// if ((mu.has("trxCode"))) {
	// trxCodeDetails.setTrxCode(mu.getString("trxCode"));
	// }
	//
	// if ((mu.has("apiName"))) {
	// trxCodeDetails.setApiName(mu.getString("apiName"));
	// }
	//
	// if ((mu.has("trxMethod"))) {
	// trxCodeDetails.setTrxMethod(mu.getString("trxMethod"));
	// }
	//
	// if ((mu.has("apiUrl"))) {
	// trxCodeDetails.setApiUrl(mu.getString("apiUrl"));
	// }
	//
	// if ((mu.has("trxPriority"))) {
	// // trxCodeDetails.setTrxPriority(Integer.parseInt(mu
	// // .getString("trxPriority")));
	// trxCodeDetails.setTrxPriority(0);
	// }
	//
	// // adding contact to TrxCodeDetails list
	// trxCodeDetailsList.add(trxCodeDetails);
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// return trxCodeDetailsList;
	// }
}
