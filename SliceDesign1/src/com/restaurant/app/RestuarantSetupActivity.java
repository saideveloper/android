package com.restaurant.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.restaurant.dao.DatabaseManager;
import com.restaurant.util.ExceptionHandler;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;
import com.restaurant.util.JSONParser;

public class RestuarantSetupActivity extends Activity implements
		OnClickListener {

	private EditText restName;
	private Button btnSingIn;
	private EditText restLocation;
	private EditText restNumber;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		DatabaseManager.init(this);

		IConstants.mContext = this;

		setContentView(R.layout.restuarant_setup);

		restName = (EditText) findViewById(R.id.restName);
		btnSingIn = (Button) findViewById(R.id.btnSingIn);
		btnSingIn.setOnClickListener(this);
		restNumber = (EditText) findViewById(R.id.restNumber);
		restLocation = (EditText) findViewById(R.id.restLocation);
	}

	@Override
	public void onClick(View view) {

		if (view.getId() == R.id.btnSingIn) {

			if ((restName.getText().toString().trim() + "") == "") {
				restName.setError("please enter Restaurant Name!");
			} else if ((restNumber.getText().toString().trim() + "") == "") {
				restNumber.setError("please enter Phone Number!");
			} else if ((restNumber.getText().toString().trim() + "").length() < 10) {
				restNumber.setError("please enter correct Phone Number!");
			} else if ((restLocation.getText().toString().trim() + "") == "") {
				restLocation.setError("please enter Location!");
			} else {

				IDialog iDialog = new IDialog();

				// restName.setText(restName.getText().toString().trim().replaceAll(" ","%20"));
				// restLocation.setText(restLocation.getText().toString().trim().replaceAll(" ","%20"));

				try {

					iDialog.showProgress(RestuarantSetupActivity.this);

					if (IConstants.checkServer()) {
						JSONParser jParser = new JSONParser();

						JSONObject jsonResponse = jParser
								.resturantDetails("RestaurantDetails/rest?restName='"
										+ restName.getText().toString().trim()
										+ "'&contactNo="
										+ restNumber.getText().toString()
												.trim()
										+ "&location='"
										+ restLocation.getText().toString()
												.trim() + "'");

						if (Integer.parseInt(jsonResponse.getString("status")) == 200) {
							System.out.println("okay cool!");

							JSONObject jsonRestaurant = jsonResponse
									.getJSONObject("jsonData");

							// Getting JSON Array node
							JSONArray restaurantDetailsArray = jsonRestaurant
									.getJSONArray("restaurantDetails");

							if (restaurantDetailsArray.length() > 0) {

								JSONObject rest = restaurantDetailsArray
										.getJSONObject(0);

								SharedPreferences sharedPreferences = PreferenceManager
										.getDefaultSharedPreferences(this);
								Editor editor = sharedPreferences.edit();
								editor.putString("appRestName",
										rest.getString("restName"));
								editor.putString("appRestNumber",
										rest.getString("contactNo"));
								editor.putString("appRestLocation",
										rest.getString("location"));
								editor.putString("appRestAddress",
										rest.getString("address1"));
								editor.commit();

								IConstants.appRestName = sharedPreferences
										.getString("appRestName", null);
								IConstants.appRestLocation = sharedPreferences
										.getString("appRestLocation", null);
								IConstants.appRestAddress = sharedPreferences
										.getString("appRestAddress", null);
								
								iDialog.dismissProgress();

								startActivity(new Intent(this,
										HomeActivity.class));
								finish();

								overridePendingTransition(
										R.anim.slide_in_right,
										R.anim.slide_out_left);
							} else {
								IConstants.showToast("Contact Adminstrator!");
								iDialog.dismissProgress();
							}
						} else {
							IConstants.showToast("Contact Adminstrator! "
									+ Integer.parseInt(jsonResponse
											.getString("status")));
							iDialog.dismissProgress();
						}
					} else {
						System.err.println("Server is un-reachable!");
						IConstants.showToast("Server is un-reachable!");
						iDialog.dismissProgress();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					IConstants.showToast("" + e);
					iDialog.dismissProgress();
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
