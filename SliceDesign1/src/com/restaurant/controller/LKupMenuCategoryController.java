package com.restaurant.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.lkup.LkupMenuCategory;
import com.restaurant.util.JSONParser;

public class LKupMenuCategoryController extends
		AsyncTask<String, Void, List<LkupMenuCategory>> {

	private Context mContext;

	public interface LKupMenuCategoryControllerTC {
		void onTaskCompleted(List<LkupMenuCategory> lkupMenuCategories);
	}

	private LKupMenuCategoryControllerTC listener;

	public LKupMenuCategoryController(Context context,
			LKupMenuCategoryControllerTC listener) {
		mContext = context;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected List<LkupMenuCategory> doInBackground(String... params) {

		// String result = "";
		List<LkupMenuCategory> lkupMenuCategories = new ArrayList<LkupMenuCategory>();

		try {
			JSONParser jParser = new JSONParser();
			JSONObject jsonResponse = jParser.restApiCall("MUCGT");
			// System.out.println("json Response data " +
			// jsonResponse.toString());
			switch (Integer.parseInt(jsonResponse.getString("status"))) {
			case 200:
				System.out.println("response 200 ");
				JSONObject jsonMenuCategoryInsert = jsonResponse
						.getJSONObject("jsonData");

				List<LkupMenuCategory> lkmenucategoryList = organiseMenuCategoryInsert(jsonMenuCategoryInsert);

				try {
					DeleteBuilder<LkupMenuCategory, Integer> deleteBuilder = DatabaseManager
							.getInstance().getLkupMenuCategoryDao()
							.deleteBuilder();
					deleteBuilder.delete();
				} catch (Exception e) {
				}

				for (LkupMenuCategory lkupmenuCategoryDetail : lkmenucategoryList) {
					System.out.println("lkupmenuCategoryDetail "
							+ lkupmenuCategoryDetail.getCategory());
					DatabaseManager.getInstance().getLkupMenuCategoryDao()
							.create(lkupmenuCategoryDetail);
				}
				// result = "success";
				// menuDetails =
				// DatabaseManager.getInstance().getMenuDetailsDao().queryForAll();

				break;
			case 304:
				System.out.println("response 304 ");
				// result = "success";
				break;
			case 0:
				// result = "success";
				break;
			default:
				// result = "failure";
				break;
			}

			lkupMenuCategories = DatabaseManager.getInstance()
					.getLkupMenuCategoryDao().queryForAll();
			System.err.println("lkupMenuCategories Data "
					+ lkupMenuCategories.size());
		} catch (SQLException e) {
			System.err.println("SQLException " + e.getMessage());
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return lkupMenuCategories;
	}

	@Override
	protected void onPostExecute(List<LkupMenuCategory> lkupMenuCategories) {
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

			if (listener != null)
				listener.onTaskCompleted(lkupMenuCategories);
			// } else {
			// System.out.println("onPostExecute");
			// // TODO Toast needed to display the response
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<LkupMenuCategory> organiseMenuCategoryInsert(
			JSONObject jsonMenuCategoryInsert) throws ParseException {
		System.out.println("organiseMenuCategoryInsert called ..");
		List<LkupMenuCategory> menuCategoryList = new ArrayList<LkupMenuCategory>();

		if (jsonMenuCategoryInsert != null) {
			try {
				// Getting JSON Array node
				JSONArray menuCategoryArray = jsonMenuCategoryInsert
						.getJSONArray("menuCategory");

				// looping through All Contacts
				for (int i = 0; i < menuCategoryArray.length(); i++) {
					JSONObject mu = menuCategoryArray.getJSONObject(i);
					LkupMenuCategory menuCategory = new LkupMenuCategory();

					if (mu.has("categoryId")) {
						menuCategory.setCategoryId(mu.getInt("categoryId"));
						if ((mu.has("category"))) {
							menuCategory.setCategory(mu.getString("category"));
						}if(mu.has("lastUpdate")){
							menuCategory.setLastUpdate(mu.getString("lastUpdate"));
						}

						// if ((mu.has("categoryImage"))) {
						// if (!(mu.isNull("categoryImage"))) {
						// ImgEncoderDecoder imgEncoderDecoder = new
						// ImgEncoderDecoder();
						// String realPath = request.getSession()
						// .getServletContext()
						// .getRealPath("/Images/Category");
						// realPath = realPath + "/"
						// + mu.getString("category") + ".png";
						// String path = "/Category/"
						// + mu.getString("category") + ".png";
						// BufferedImage newImg;
						// File file = new File(realPath);
						// if (file.exists()) {
						// file.delete();
						// System.err.println("realPath :" + realPath);
						// newImg = imgEncoderDecoder.decodeToImage(mu
						// .getString("categoryImage"));
						// try {
						// ImageIO.write(newImg, "png", new File(
						// realPath));
						// } catch (IOException e) {
						// e.printStackTrace();
						// }
						// newImg.flush();
						// menuCategory.setImgUrl(path);
						// } else {
						// System.out.println(mu
						// .getString("categoryImage"));
						// newImg = imgEncoderDecoder.decodeToImage(mu
						// .getString("categoryImage"));
						// try {
						// ImageIO.write(newImg, "png", file);
						// } catch (IOException e) {
						// e.printStackTrace();
						// }
						// menuCategory.setImgUrl(path);
						// }
						//
						// }
						// }

						// adding contact to MenuDetails list
						menuCategoryList.add(menuCategory);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return menuCategoryList;
	}
}
