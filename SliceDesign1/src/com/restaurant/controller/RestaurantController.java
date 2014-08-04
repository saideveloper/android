package com.restaurant.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.RestaurantDetails;
import com.restaurant.util.JSONParser;

public class RestaurantController extends
		AsyncTask<String, Void, List<RestaurantDetails>> {

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected List<RestaurantDetails> doInBackground(String... params) {

		// String result = "";
		List<RestaurantDetails> restaurantDetails = new ArrayList<RestaurantDetails>();

		try {
			JSONParser jParser = new JSONParser();
			JSONObject jsonResponse = jParser.restApiCall("RSTGT");

			switch (Integer.parseInt(jsonResponse.getString("status"))) {
			case 200:

				JSONObject jsonRestaurantInsert = jsonResponse
						.getJSONObject("jsonData");

				List<RestaurantDetails> restaurantDetailList = organiseRestaurantDetailsInsert(jsonRestaurantInsert);

				try {
					DeleteBuilder<RestaurantDetails, Integer> deleteBuilder = DatabaseManager
							.getInstance().getRestaurantDetailsDao()
							.deleteBuilder();
					deleteBuilder.delete();
				} catch (Exception e) {
				}

				for (RestaurantDetails restaurantDetail : restaurantDetailList) {
					DatabaseManager.getInstance().getRestaurantDetailsDao()
							.create(restaurantDetail);
				}
				// result = "success";

				// menuDetails =
				// DatabaseManager.getInstance().getMenuDetailsDao().queryForAll();

				break;
			case 304:
				// result = "success";
				break;
			case 0:
				// result = "success";
				break;
			default:
				// result = "failure";
				break;
			}

			restaurantDetails = DatabaseManager.getInstance()
					.getRestaurantDetailsDao().queryForAll();
		} catch (SQLException e) {
			System.err.println("SQLException " + e);

			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return restaurantDetails;
	}

	@Override
	protected void onPostExecute(List<RestaurantDetails> restaurantDetails) {
		// System.out.println("onPostExecute " + result);
		// Toast.makeText(mContext, "onPostExecute " + result,
		// Toast.LENGTH_SHORT)
		// .show();
		try {
			// if (result.equals("success")) {
			// List<MenuDetails> menuDetails =
			// DatabaseManager.getInstance().getMenuDetailsDao().queryForAll();

			// if (listener != null)
			// listener.onTaskCompleted(menuDetails);
			// } else {
			// System.out.println("onPostExecute");
			// // TODO Toast needed to display the response
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<RestaurantDetails> organiseRestaurantDetailsInsert(
			JSONObject jsonRestaurantInsert) throws ParseException, IOException {
		List<RestaurantDetails> restaurantDetailsList = new ArrayList<RestaurantDetails>();

		if (jsonRestaurantInsert != null) {
			try {

				// Getting JSON Array node
				JSONArray restaurantDetailsArray = jsonRestaurantInsert
						.getJSONArray("restaurantDetails");

				// looping through All Contacts
				for (int i = 0; i < restaurantDetailsArray.length(); i++) {
					JSONObject c = restaurantDetailsArray.getJSONObject(i);

					RestaurantDetails restaurantDetails = new RestaurantDetails();

					// if ((c.has("propertyId"))) {
					// restaurantDetails.setPropertyId(Integer.parseInt(c
					// .getString("propertyId")));
					// }

					if ((c.has("restName"))) {
						restaurantDetails.setRestName(c.getString("restName"));
					}

					if ((c.has("location"))) {
						restaurantDetails.setLocation(c.getString("location"));
					}

					if ((c.has("latLong"))) {
						restaurantDetails.setLatLong(c.getString("latLong"));
					}

					if ((c.has("address1"))) {
						restaurantDetails.setAddress1(c.getString("address1"));
					}

					if ((c.has("address2"))) {
						restaurantDetails.setAddress2(c.getString("address2"));
					}

					if ((c.has("address3"))) {
						restaurantDetails.setAddress3(c.getString("address3"));
					}

					if ((c.has("address4"))) {
						restaurantDetails.setAddress4(c.getString("address4"));
					}

					if ((c.has("webSite"))) {
						restaurantDetails.setWebSite(c.getString("webSite"));
					}

					if ((c.has("fbPage"))) {
						restaurantDetails.setFbPage(c.getString("fbPage"));
					}

					if ((c.has("emailId"))) {
						restaurantDetails.setEmailId(c.getString("emailId"));
					}

					if ((c.has("twitterAccount"))) {
						restaurantDetails.setTwitterAccount(c
								.getString("twitterAccount"));
					}

					if ((c.has("contactNo"))) {
						restaurantDetails.setContactNo(Long.parseLong(c
								.getString("contactNo")));
					}

					if ((c.has("landLine"))) {
						if (c.getString("landLine").length() > 0) {
							restaurantDetails.setLandLine(Long.parseLong(c
									.getString("landLine")));
						}
					}

					if ((c.has("cityCd"))) {
						restaurantDetails.setCityCd(c.getString("cityCd"));
					}

					if ((c.has("stateCd"))) {
						restaurantDetails.setStateCd(c.getString("stateCd"));
					}

					if ((c.has("countryCd"))) {
						restaurantDetails
								.setCountryCd(c.getString("countryCd"));
					}

					if ((c.has("pinCode"))) {
						restaurantDetails.setPinCode(c.getString("pinCode"));
					}

					if ((c.has("landMark"))) {
						restaurantDetails.setLandMark(c.getString("landMark"));
					}

					if ((c.has("restDescription"))) {
						restaurantDetails.setRestDescription(c
								.getString("restDescription"));
					}

					if ((c.has("totalLikes"))) {
						restaurantDetails.setTotalLikes(Integer.parseInt(c
								.getString("totalLikes")));
					} else {
						restaurantDetails.setTotalLikes(0);
					}

					if ((c.has("restRatings"))) {
						restaurantDetails.setRestRatings(Double.parseDouble(c
								.getString("restRatings")));
					} else {
						restaurantDetails.setRestRatings(0.0);
					}

					if ((c.has("empRatings"))) {
						restaurantDetails.setEmpRatings(Double.parseDouble(c
								.getString("empRatings")));
					} else {
						restaurantDetails.setEmpRatings(0.0);
					}

					if ((c.has("foodRatings"))) {
						restaurantDetails.setFoodRatings(Double.parseDouble(c
								.getString("foodRatings")));
					} else {
						restaurantDetails.setFoodRatings(0.0);
					}

					if ((c.has("totalReviews"))) {
						restaurantDetails.setTotalReviews(Integer.parseInt(c
								.getString("totalReviews")));
					} else {
						restaurantDetails.setTotalReviews(0);
					}

					if ((c.has("restImage"))) {
						// if (!c.getString("restImage").equals("null")) {
						// String realPath = request.getSession()
						// .getServletContext()
						// .getRealPath("/Images/Restaurant");
						// realPath = realPath + "/" + c.getString("restName")
						// + ".png";
						// String path = "/Restaurant/"
						// + c.getString("restName") + ".png";
						// BufferedImage newImg;
						// ImgEncoderDecoder imgEncoderDecoder = new
						// ImgEncoderDecoder();
						// newImg = imgEncoderDecoder.decodeToImage(c
						// .getString("restImage"));
						// ImageIO.write(newImg, "png", new File(realPath));
						//
						// restaurantDetails.setImageUrl(path);
						// }
						restaurantDetails.setImageUrl(null);
					}

					if ((c.has("restStatus"))) {
						restaurantDetails.setRestStatus(c
								.getString("restStatus"));
					}

					if ((c.has("regDate"))) {
						restaurantDetails.setRegDate(c.getString("regDate"));
					}

					if ((c.has("validFrom"))) {
						restaurantDetails
								.setValidFrom(c.getString("validFrom"));
					}

					if ((c.has("validTo"))) {
						restaurantDetails.setValidTo(c.getString("validTo"));
					}

					// adding contact to RestaurantDetails list
					restaurantDetailsList.add(restaurantDetails);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {

		}
		return restaurantDetailsList;
	}
}
