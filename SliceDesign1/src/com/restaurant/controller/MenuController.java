package com.restaurant.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.TableUtils;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.ModifierDetails;
import com.restaurant.model.ModifierQuestionAnswer;
import com.restaurant.model.OrderMenuDetails;
import com.restaurant.model.RestAddonItemsDetails;
import com.restaurant.util.JSONBind;
import com.restaurant.util.JSONParser;

public class MenuController {
	private Context mContext;
	private MenuDetailsFetch menuDetailsFetchListener;
	private MenuDetailsPost menuDetailsPostListener;
	private FetchAsynchTask fetchAsynchTask;
	private PostAsynchTask postAsynchTask;

	public MenuController(Context context) {
		mContext = context;
	}

	public MenuController(Context context,
			MenuDetailsPost menuDetailsPostListener) {
		mContext = context;
		this.menuDetailsPostListener = menuDetailsPostListener;
	}

	public MenuController(Context context,
			MenuDetailsFetch menuDetailsFetchListener) {
		mContext = context;
		this.menuDetailsFetchListener = menuDetailsFetchListener;
	}

	public interface MenuDetailsFetch {
		void onMenuDetailsFetch(String result);
	}

	public interface MenuDetailsPost {
		void onMenuDetailsPost(String result);
	}

	public void getMenuDetails() {
		fetchAsynchTask = new FetchAsynchTask();
		fetchAsynchTask.execute();
	}

	public void postMenuDetails(MenuDetails menuDetails, File fileName) {
		postAsynchTask = new PostAsynchTask(menuDetails, fileName);
		postAsynchTask.execute();
	}

	public void deleteMenuDetails() {
		try {
//			DeleteBuilder<MenuDetails, Integer> deleteBuilder = DatabaseManager
//					.getInstance().getMenuDetailsDao().deleteBuilder();
//			deleteBuilder.delete();
			
			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), MenuDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), MenuDetails.class);
		} catch (Exception e) {
		}
	}

	public void deleteModifierQuestionAnswer() {
		try {
			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), ModifierQuestionAnswer.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), ModifierQuestionAnswer.class);
			// DeleteBuilder<ModifierQuestionAnswer, Integer> deleteBuilder =
			// DatabaseManager
			// .getInstance().getModifierQuestionAnswerDao()
			// .deleteBuilder();
			// deleteBuilder.delete();
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
				JSONObject jsonResponse = jParser.restApiCall("MNUGT");

				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:

					JSONObject jsonMenuInsert = jsonResponse
							.getJSONObject("jsonData");

					deleteModifierQuestionAnswer();

					deleteMenuDetails();

					// Getting JSON Array node
					JSONArray menuDetailsArray = jsonMenuInsert
							.getJSONArray("menuDetails");

					Gson gson = new Gson();

					System.err
							.println("-------------------------------------------------------------");

					// looping through All Data
					for (int i = 0; i < menuDetailsArray.length(); i++) {
						JSONObject jsonObject = menuDetailsArray
								.getJSONObject(i);

						System.err.println(jsonObject.toString());

						MenuDetails menuDetail = gson.fromJson(
								jsonObject.toString(), MenuDetails.class);

						DatabaseManager.getInstance().getMenuDetailsDao()
								.create(menuDetail);

						getModifierBind(menuDetail);
					}

					System.err
							.println("-------------------------------------------------------------");

					// organiseMenuDetailsToInsert(jsonMenuInsert);

					// List<MenuDetails> menuDetailList =
					// organiseMenuDetailsToInsert(jsonMenuInsert);
					//
					// for (MenuDetails menuDetail : menuDetailList) {
					// DatabaseManager.getInstance().getMenuDetailsDao()
					// .create(menuDetail);
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

				// menuDetails =
				// DatabaseManager.getInstance().getMenuDetailsDao()
				// .queryForAll();

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
				// List<MenuDetails> menuDetails =
				// DatabaseManager.getInstance().getMenuDetailsDao().queryForAll();

				// if(menuDetails.size()>0){
				//
				// } else {
				//
				// }

				if (menuDetailsFetchListener != null)
					menuDetailsFetchListener.onMenuDetailsFetch(result);
				// } else {
				// System.out.println("onPostExecute");
				// // TODO Toast needed to display the response
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class PostAsynchTask extends AsyncTask<String, Void, String> {
		private MenuDetails menuDetails;
		private File fileName;

		public PostAsynchTask() {
			// TODO Auto-generated constructor stub
		}

		public PostAsynchTask(MenuDetails menuDetails, File fileName) {
			this.menuDetails = menuDetails;
			this.fileName = fileName;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			try {

				Gson gson = new GsonBuilder().serializeNulls().create();
				String json = gson.toJson(menuDetails);
				System.out.println(json);

				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				try {
					jsonArray.put(new JSONObject(json));
					jsonObject.put("MenuDetails", jsonArray);
					jsonObject = new JSONObject(jsonObject.toString());

					// System.out.println(jsonObject.getJSONArray("MenuDetails").getJSONObject(0).getString("itemName"));

					JSONBind jsonPack = new JSONBind();
					JSONObject jsonResponse = jsonPack.restMultiPartApiCall(
							"MNUPO", jsonObject.toString(), fileName);

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
				if (menuDetailsPostListener != null)
					menuDetailsPostListener.onMenuDetailsPost(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void getModifierBind(MenuDetails menuDetail) {
		try {

			if (!menuDetail.getAnswer1().equalsIgnoreCase("null")) {
				insertModifierQuestionAnswer(menuDetail,
						menuDetail.getQuestion1(),
						new JSONObject(menuDetail.getAnswer1()), 1);
			}

			if (!menuDetail.getAnswer2().equalsIgnoreCase("null")) {
				insertModifierQuestionAnswer(menuDetail,
						menuDetail.getQuestion2(),
						new JSONObject(menuDetail.getAnswer2()), 2);
			}

			if (!menuDetail.getAnswer3().equalsIgnoreCase("null")) {
				insertModifierQuestionAnswer(menuDetail,
						menuDetail.getQuestion3(),
						new JSONObject(menuDetail.getAnswer3()), 3);
			}

			if (!menuDetail.getAnswer4().equalsIgnoreCase("null")) {
				insertModifierQuestionAnswer(menuDetail,
						menuDetail.getQuestion4(),
						new JSONObject(menuDetail.getAnswer4()), 4);
			}

			if (!menuDetail.getAnswer5().equalsIgnoreCase("null")) {
				insertModifierQuestionAnswer(menuDetail,
						menuDetail.getQuestion5(),
						new JSONObject(menuDetail.getAnswer5()), 5);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertModifierQuestionAnswer(MenuDetails menuDetail,
			String question, JSONObject jsonObject, int questionNo) {
		try {
			JSONArray jsonArray = jsonObject.getJSONArray("m");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject mu = jsonArray.getJSONObject(i);

				ModifierQuestionAnswer modifierQuestionAnswer = new ModifierQuestionAnswer();

				RestAddonItemsDetails restAddonItemsDetail = DatabaseManager
						.getInstance().getRestAddonItemsDetailsDao()
						.queryBuilder().distinct().where()
						.in("addonId", mu.getString("ItemName")).query().get(0);

				// modifierQuestionAnswer.setIndex(i);
				modifierQuestionAnswer.setItemId(menuDetail.getItemId());
				modifierQuestionAnswer.setItemName(menuDetail.getItemName());
				modifierQuestionAnswer
						.setModifierId(menuDetail.getModifierId());
				modifierQuestionAnswer.setModifierCategory(menuDetail
						.getModifierCategory());

				modifierQuestionAnswer.setPrice(menuDetail.getPrice());
				modifierQuestionAnswer.setTotalPrice(menuDetail.getPrice());
				modifierQuestionAnswer.setParcelPrice(menuDetail
						.getParcelPrice());

				modifierQuestionAnswer.setChecked(false);

				modifierQuestionAnswer.setWhichQuestion(questionNo);
				modifierQuestionAnswer.setQuestion(question);
				
				modifierQuestionAnswer.setJsonAnswer(jsonObject);
				
				modifierQuestionAnswer.setAddonId(mu.getString("ItemName"));
				
				modifierQuestionAnswer.setAnswer(restAddonItemsDetail
						.getAddonName());
				
				modifierQuestionAnswer.setQuantity(1);
				modifierQuestionAnswer.setModifierPrice(Double.parseDouble(mu
						.getString("price")));

				DatabaseManager.getInstance().getModifierQuestionAnswerDao()
						.create(modifierQuestionAnswer);
			}

//			final List<ModifierQuestionAnswer> modifierQuestionAnswerList = DatabaseManager
//					.getInstance().getModifierQuestionAnswerDao()
//					.queryBuilder().distinct().query();
//			System.err
//					.println("=============================================================");
//			for (ModifierQuestionAnswer modifierQuestionAnswer : modifierQuestionAnswerList) {
//				System.out.println(modifierQuestionAnswer.getModifierUniqueId()
//						+ ", " + modifierQuestionAnswer.getItemName()
//						+ ", " + modifierQuestionAnswer.getModifierCategory());
//			}
//			System.err
//					.println("=============================================================");
		} catch (Exception e) {
			e.printStackTrace();
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
