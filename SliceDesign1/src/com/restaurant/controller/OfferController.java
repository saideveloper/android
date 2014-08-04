package com.restaurant.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ParseException;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.TableUtils;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.OfferDetails;
import com.restaurant.model.lkup.LkupTaxListDetails;
import com.restaurant.util.JSONBind;
import com.restaurant.util.JSONParser;

public class OfferController {

	private Context mContext;
	private OfferDetailsFetch offerDetailsFetchListener;
	private OfferDetailsPost offerDetailsPostListener;
	private FetchAsynchTask fetchAsynchTask;
	private PostAsynchTask postAsynchTask;

	public OfferController(Context context) {
		mContext = context;
	}

	public OfferController(Context context,
			OfferDetailsPost offerDetailsPostListener) {
		mContext = context;
		this.offerDetailsPostListener = offerDetailsPostListener;
	}

	public OfferController(Context context,
			OfferDetailsFetch offerDetailsFetchListener) {
		mContext = context;
		this.offerDetailsFetchListener = offerDetailsFetchListener;
	}

	public interface OfferDetailsFetch {
		void onOfferDetailsFetch(String result);
	}

	public interface OfferDetailsPost {
		void onOfferDetailsPost(String result);
	}

	public void getOfferDetails() {
		fetchAsynchTask = new FetchAsynchTask();
		fetchAsynchTask.execute();
	}

	public void postOfferDetails(OfferDetails offerDetails, File fileName) {
		postAsynchTask = new PostAsynchTask(offerDetails, fileName);
		postAsynchTask.execute();
	}

	public void deleteOfferDetails() {
		try {
//			DeleteBuilder<OfferDetails, Integer> deleteBuilder = DatabaseManager
//					.getInstance().getofferDetailsDao().deleteBuilder();
//			deleteBuilder.delete();
			
			TableUtils.dropTable(DatabaseManager.getHelper()
					.getConnectionSource(), OfferDetails.class, true);
			TableUtils.createTableIfNotExists(DatabaseManager.getHelper()
					.getConnectionSource(), OfferDetails.class);
		} catch (Exception e) {
		}
	}

	public class FetchAsynchTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params)
				throws NumberFormatException {
			String result = "";
			try {

				JSONParser jParser = new JSONParser();
				JSONObject jsonResponse = jParser.restApiCall("ADVGT");
				System.out.println("offerDetailsArray jsonResponse "
						+ jsonResponse.toString());
				switch (Integer.parseInt(jsonResponse.getString("status"))) {
				case 200:
					JSONObject jsonOfferInsert = jsonResponse
							.getJSONObject("jsonData");

					// List<OfferDetails> offerDetailList =
					// organiseOfferDetailsInsert(jsonOfferInsert);

					deleteOfferDetails();

					// for (OfferDetails offerDetails : offerDetailList) {
					// DatabaseManager.getInstance().getofferDetailsDao()
					// .create(offerDetails);
					// }

					// Getting JSON Array node
					JSONArray offerDetailsArray = jsonOfferInsert
							.getJSONArray("offerDetails");

					Gson gson = new Gson();

					// looping through All Data
					for (int i = 0; i < offerDetailsArray.length(); i++) {
						
						JSONObject jsonObject = offerDetailsArray
								.getJSONObject(i);
						
						System.out.println("offerdetail "
								+ jsonObject.toString());

						OfferDetails offerDetail = gson.fromJson(
								jsonObject.toString(), OfferDetails.class);
						
						DatabaseManager.getInstance().getofferDetailsDao()
								.create(offerDetail);
					}

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

			} catch (NumberFormatException e) {
				System.out.println("JSOn error " + e.getMessage());
				result = "" + e;
			} catch (JsonSyntaxException e) {
				result = "" + e;
				e.printStackTrace();
			} catch (JSONException e) {
				result = "" + e;
				e.printStackTrace();
			} catch (SQLException e) {
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

				if (offerDetailsFetchListener != null)
					offerDetailsFetchListener.onOfferDetailsFetch(result);
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
		private OfferDetails offerDetails;
		private File fileName;

		public PostAsynchTask() {
			// TODO Auto-generated constructor stub
		}

		public PostAsynchTask(OfferDetails offerDetails, File fileName) {
			this.offerDetails = offerDetails;
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
				String json = gson.toJson(offerDetails);
				System.out.println(json);

				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				try {
					jsonArray.put(new JSONObject(json));
					jsonObject.put("OfferDetails", jsonArray);
					jsonObject = new JSONObject(jsonObject.toString());

					// System.out.println(jsonObject.getJSONArray("MenuDetails").getJSONObject(0).getString("itemName"));

					JSONBind jsonPack = new JSONBind();
					JSONObject jsonResponse = jsonPack.restMultiPartApiCall(
							"ADVPO", jsonObject.toString(), fileName);

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
				if (offerDetailsPostListener != null)
					offerDetailsPostListener.onOfferDetailsPost(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// public List<OfferDetails> organiseOfferDetailsInsert(
	// JSONObject jsonOfferInsert) throws ParseException, IOException {
	// List<OfferDetails> offerDetailsList = new ArrayList<OfferDetails>();
	//
	// if (jsonOfferInsert != null) {
	// try {
	//
	// // Getting JSON Array node
	// JSONArray offerDetailsArray = jsonOfferInsert
	// .getJSONArray("offerDetails");
	//
	// // looping through All Contacts
	// for (int i = 0; i < offerDetailsArray.length(); i++) {
	// JSONObject mu = offerDetailsArray.getJSONObject(i);
	//
	// OfferDetails offerDetails = new OfferDetails();
	//
	// if ((mu.has("title"))) {
	// offerDetails.setTitle(mu.getString("title"));
	// }
	//
	// if ((mu.has("subTitle"))) {
	// offerDetails.setSubTitle(mu.getString("subTitle"));
	// }
	//
	// if ((mu.has("offerDesc"))) {
	// offerDetails.setOfferDesc(mu.getString("offerDesc"));
	// }
	//
	// if ((mu.has("couponCode"))) {
	// offerDetails.setCouponCode(mu.getString("couponCode"));
	// }
	//
	// if ((mu.has("terms"))) {
	// offerDetails.setTerms(mu.getString("terms"));
	// }
	//
	// if ((mu.has("partner"))) {
	// offerDetails.setPartner(mu.getString("partner"));
	// }
	//
	// if ((mu.has("category"))) {
	// offerDetails.setCategory(mu.getString("category"));
	// }
	//
	// if ((mu.has("discountType"))) {
	// offerDetails.setDiscountType(mu
	// .getString("discountType"));
	// }
	//
	// if ((mu.has("validFrom"))) {
	// offerDetails.setValidFrom(mu.getString("validFrom"));
	// }
	//
	// if ((mu.has("validUpto"))) {
	// offerDetails.setValidUpto(mu.getString("validUpto"));
	// }
	//
	// if ((mu.has("prepTime"))) {
	// offerDetails.setPrepTime(Integer.parseInt(mu
	// .getString("prepTime")));
	// }
	//
	// if ((mu.has("discount"))) {
	// offerDetails.setDiscount(Double.parseDouble(mu
	// .getString("discount")));
	// }
	//
	// if ((mu.has("template"))) {
	// offerDetails.setTemplate(mu.getString("template"));
	// }
	//
	// if ((mu.has("itemId"))) {
	// offerDetails.setItemId(mu.getString("itemId"));
	// }
	//
	// if ((mu.has("totalLikes"))) {
	// offerDetails.setTotalLikes(Integer.parseInt(mu
	// .getString("totalLikes")));
	// }
	//
	// if ((mu.has("avgRatings"))) {
	// offerDetails.setAvgRatings(Integer.parseInt(mu
	// .getString("avgRatings")));
	// }
	//
	// if ((mu.has("totalReviews"))) {
	// offerDetails.setTotalReviews(Integer.parseInt(mu
	// .getString("totalReviews")));
	// }
	//
	// if ((mu.has("userId"))) {
	// offerDetails.setUserId(mu.getString("userId"));
	// }
	//
	// if ((mu.has("trxCode"))) {
	// offerDetails.setTrxCode(mu.getString("trxCode"));
	// }
	//
	// if ((mu.has("trxSource"))) {
	// offerDetails.setTrxSource(mu.getString("trxSource"));
	// }
	//
	// // // if ((mu.has("offerImage"))) {
	// // // if (!(mu.isNull("offerImage"))) {
	// // // ImgEncoderDecoder imgEncoderDecoder = new
	// // // ImgEncoderDecoder();
	// // // String realPath = request.getSession()
	// // // .getServletContext()
	// // // .getRealPath("/Images/Offer");
	// // // realPath = realPath + "/" + mu.getString("title")
	// // // + ".png";
	// // // String path = "/Offer/" + mu.getString("title")
	// // // + ".png";
	// // // BufferedImage newImg;
	// // //
	// // // newImg = imgEncoderDecoder.decodeToImage(mu
	// // // .getString("offerImage"));
	// // // ImageIO.write(newImg, "png", new File(realPath));
	// // //
	// // // offerDetails.setImgUrl(path);
	// // // }
	// // // }
	// // // offerDetails.setOfferStatus("a");
	// //
	// // // adding contact to OfferDetails list
	// offerDetailsList.add(offerDetails);
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// return offerDetailsList;
	// }

	public void cancelTasks() {
		if (fetchAsynchTask != null) {
			fetchAsynchTask.cancel(true);
		}
		if (postAsynchTask != null) {
			postAsynchTask.cancel(true);
		}
	}
}
