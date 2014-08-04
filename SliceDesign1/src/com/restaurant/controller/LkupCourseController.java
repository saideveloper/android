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
import com.restaurant.model.lkup.LkupCourseDetails;
import com.restaurant.util.JSONParser;

public class LkupCourseController extends
		AsyncTask<String, Void, List<LkupCourseDetails>> {
	
	private Context mContext;

	public interface LkupCourseControllerTC {
		void onTaskCompleted(List<LkupCourseDetails> lkupCourseDetails);
	}

	private LkupCourseControllerTC listener;

	public LkupCourseController(Context context, LkupCourseControllerTC listener) {
		mContext = context;
		this.listener = listener;
	}
	
	@Override
	protected void onPreExecute() {
	}

	@Override
	protected List<LkupCourseDetails> doInBackground(String... params) {

		// String result = "";
		List<LkupCourseDetails> lkupCourseDetails = new ArrayList<LkupCourseDetails>();

		try {
			JSONParser jParser = new JSONParser();
			JSONObject jsonResponse = jParser.restApiCall("MNCGT");

			switch (Integer.parseInt(jsonResponse.getString("status"))) {
			case 200:

				JSONObject jsonCourseDetailsInsert = jsonResponse
						.getJSONObject("jsonData");

				List<LkupCourseDetails> lkupCourseDetailList = organiseCourseDetailsInsert(jsonCourseDetailsInsert);

				try {
					DeleteBuilder<LkupCourseDetails, Integer> deleteBuilder = DatabaseManager
							.getInstance().getLkupCourseDetailsDao()
							.deleteBuilder();
					deleteBuilder.delete();
				} catch (Exception e) {
				}

				for (LkupCourseDetails lkCourseDetail : lkupCourseDetailList) {
					DatabaseManager.getInstance().getLkupCourseDetailsDao()
							.create(lkCourseDetail);
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

			lkupCourseDetails = DatabaseManager.getInstance()
					.getLkupCourseDetailsDao().queryForAll();
		} catch (SQLException e) {
			System.err.println("SQLException " + e);

			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return lkupCourseDetails;
	}

	@Override
	protected void onPostExecute(List<LkupCourseDetails> lkupCourseDetails) {
		// System.out.println("onPostExecute " + result);
		// Toast.makeText(mContext, "onPostExecute " + result,
		// Toast.LENGTH_SHORT)
		// .show();
		try {
			// if (result.equals("success")) {
			// List<MenuDetails> menuDetails =
			// DatabaseManager.getInstance().getMenuDetailsDao().queryForAll();

			if (listener != null)
				listener.onTaskCompleted(lkupCourseDetails);
			
			// } else {
			// System.out.println("onPostExecute");
			// // TODO Toast needed to display the response
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<LkupCourseDetails> organiseCourseDetailsInsert(
			JSONObject jsonCourseDetailsArrayInsert) throws ParseException {
		List<LkupCourseDetails> courseDetailsList = new ArrayList<LkupCourseDetails>();

		if (jsonCourseDetailsArrayInsert != null) {
			try {
				// Getting JSON Array node
				JSONArray courseDetailsArray = jsonCourseDetailsArrayInsert
						.getJSONArray("courseDetails");

				// looping through All Contacts
				for (int i = 0; i < courseDetailsArray.length(); i++) {
					JSONObject mu = courseDetailsArray.getJSONObject(i);
					LkupCourseDetails courseDetails = new LkupCourseDetails();
					
					if ((mu.has("courseId"))) {
						courseDetails.setCourseId(Integer.parseInt(mu.getString("courseId")));
					}
					
					if ((mu.has("course"))) {
						courseDetails.setCourse(mu.getString("course"));
					}
					
					if ((mu.has("lastUpdate"))) {
						courseDetails.setLastUpdate(mu.getString("lastUpdate"));
					}
					
					courseDetailsList.add(courseDetails);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return courseDetailsList;
	}
}
