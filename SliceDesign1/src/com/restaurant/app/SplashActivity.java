package com.restaurant.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

import com.restaurant.util.ExceptionHandler;
import com.restaurant.util.IConstants;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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

		IConstants.mContext = this;

		setContentView(R.layout.splash);

		try {
			final WifiManager wifi = (WifiManager) getApplicationContext()
					.getSystemService(Context.WIFI_SERVICE);

			if (!wifi.isWifiEnabled()) {
				wifi.setWifiEnabled(true);
			}
			
			//IConstants.showToast("android.os.Build.VERSION.SDK_INT " + android.os.Build.VERSION.SDK_INT);
			
			// METHOD 1

			/****** Create Thread that will sleep for 3 seconds *************/
			Thread background = new Thread() {
				public void run() {

					try {
						// Thread will sleep for 3 seconds
						sleep(1 * 1000);

						// After 3 seconds redirect to another intent

						System.out.println("SplashActivity " + IConstants.lol);

						SharedPreferences sharedPreferences = PreferenceManager
								.getDefaultSharedPreferences(SplashActivity.this);

						IConstants.appRestIp = sharedPreferences.getString(
								"appRestIp", null);
						IConstants.appRestPort = sharedPreferences.getString(
								"appRestPort", null);

						IConstants.appRestName = sharedPreferences.getString(
								"appRestName", null);
						IConstants.appRestLocation = sharedPreferences
								.getString("appRestLocation", null);
						IConstants.appRestAddress = sharedPreferences
								.getString("appRestAddress", null);

						System.out.println("SplashActivity "
								+ IConstants.appRestIp + ", "
								+ IConstants.appRestPort);
						System.out.println("SplashActivity "
								+ IConstants.appRestName + ", "
								+ IConstants.appRestLocation);

						Intent i = getIntent();
						if (IConstants.appRestIp == null
								&& IConstants.appRestPort == null) {
							i = new Intent(getBaseContext(),
									ServerConnectActivity.class);
						} else if (IConstants.appRestName == null
								&& IConstants.appRestLocation == null) {
							i = new Intent(getBaseContext(),
									RestuarantSetupActivity.class);
						} else {
							i = new Intent(getBaseContext(), HomeActivity.class);
						}

						startActivity(i);

						// Remove activity
						finish();
					} catch (Exception e) {
					}
				}
			};
			
			
			//IConstants.logcat();
			
			

			// Thread.setDefaultUncaughtExceptionHandler(new
			// Thread.UncaughtExceptionHandler() {
			// @Override
			// public void uncaughtException(Thread paramThread, Throwable
			// paramThrowable) {
			// Log.e("Alert","Lets See if it Works !!!");
			// IConstants.showToast("Lets See if it Works !!!");
			// }
			// });

			// start thread
			background.start();

			// METHOD 2

			/*
			 * new Handler().postDelayed(new Runnable() {
			 * 
			 * // Using handler with postDelayed called runnable run method
			 * 
			 * @Override public void run() { Intent i = new
			 * Intent(MainSplashScreen.this, FirstScreen.class);
			 * startActivity(i);
			 * 
			 * // close this activity finish(); } }, 5*1000); // wait for 5
			 * seconds
			 */
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
