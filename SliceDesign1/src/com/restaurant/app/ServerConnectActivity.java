package com.restaurant.app;

import com.restaurant.dao.DatabaseManager;
import com.restaurant.util.ExceptionHandler;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;
import com.restaurant.util.JSONParser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ServerConnectActivity extends Activity implements OnClickListener {

	private Button btnSingIn;
	private EditText portNo;
	private EditText ipAddress;

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

		setContentView(R.layout.server_connect);

		btnSingIn = (Button) findViewById(R.id.btnSingIn);
		btnSingIn.setOnClickListener(this);
		ipAddress = (EditText) findViewById(R.id.ipAddress);
		portNo = (EditText) findViewById(R.id.portNo);

		// IConstants.showToast("testing");
	}

	@Override
	public void onClick(View view) {

		IDialog iDialog = new IDialog();

		if (view.getId() == R.id.btnSingIn) {

			try {

				if ((ipAddress.getText().toString().trim() + "") == "") {
					ipAddress.setError("Please enter IP Address!");
				} else if (!IConstants.validateIP(ipAddress.getText()
						.toString().trim()
						+ "")) {
					ipAddress.setError("Please enter correct IP Address!");
				} else if ((portNo.getText().toString().trim() + "") == "") {
					portNo.setError("Please enter Port Number!");
				} else if ((portNo.getText().toString().trim() + "").length() < 4) {
					portNo.setError("Please enter correct Port Number!");
				} else {

					iDialog.showProgress(ServerConnectActivity.this);

					if (IConstants.checkServer(ipAddress.getText().toString()
							.trim(), portNo.getText().toString().trim())) {
						JSONParser jParser = new JSONParser();
						int response = jParser.restCheckServer(ipAddress
								.getText().toString().trim(), portNo.getText()
								.toString().trim());

						if (response == 200) {
							System.out.println("okay cool!");

							SharedPreferences sharedPreferences = PreferenceManager
									.getDefaultSharedPreferences(this);
							Editor editor = sharedPreferences.edit();
							editor.putString("appRestIp", (ipAddress.getText()
									.toString().trim() + ""));
							editor.putString("appRestPort", (portNo.getText()
									.toString().trim() + ""));
							editor.commit();

							IConstants.appRestIp = sharedPreferences.getString(
									"appRestIp", null);
							IConstants.appRestPort = sharedPreferences
									.getString("appRestPort", null);

							iDialog.dismissProgress();

							startActivity(new Intent(this,
									RestuarantSetupActivity.class));
							finish();

							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
						} else {
							IConstants.showToast("Unable to connect Server");
							iDialog.dismissProgress();
						}
					} else {
						System.err.println("Server is un-reachable!");
						IConstants.showToast("Server is un-reachable!");
						iDialog.dismissProgress();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				IConstants.showToast("" + e);
				iDialog.dismissProgress();
			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
