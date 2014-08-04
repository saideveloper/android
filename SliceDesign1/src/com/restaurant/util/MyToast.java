package com.restaurant.util;

import android.app.Activity;
import android.widget.Toast;

public class MyToast {
	private Activity mContext;
	public MyToast(Activity context) {
		mContext = context;
	}
	
	public MyToast(Activity context, String string) {
		Toast.makeText(context, "" + string, Toast.LENGTH_SHORT).show();
	}
	
	public void toast(String string) {
		Toast.makeText(mContext, "" + string, Toast.LENGTH_SHORT).show();
	}
}
