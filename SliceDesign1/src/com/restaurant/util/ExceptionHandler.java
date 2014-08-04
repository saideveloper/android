package com.restaurant.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.restaurant.app.CrashReportActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

public class ExceptionHandler implements
		java.lang.Thread.UncaughtExceptionHandler {
	private final Activity myContext;

	public ExceptionHandler(Activity context) {
		myContext = context;
	}

	public void uncaughtException(Thread thread, Throwable exception) {
		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));
		final StringBuilder errorReport = new StringBuilder();
		errorReport.append("************ CAUSE OF ERROR ************\n\n");
		errorReport.append(stackTrace.toString());
		errorReport.append("************ End OF ERROR **************\n\n");

		System.err.println(errorReport.toString());

		Intent intent = new Intent(myContext, CrashReportActivity.class);
		intent.putExtra("error", errorReport.toString());
		myContext.startActivity(intent);

		android.os.Process.killProcess(android.os.Process.myPid());
		//System.exit(10);

		// try {
		// AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		// myContext);
		//
		// // set title
		// alertDialogBuilder.setTitle("Exception");
		//
		// // set dialog message
		// alertDialogBuilder.setMessage("" + errorReport.toString())
		// .setCancelable(false)
		// .setNeutralButton("Thanks", new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.cancel();
		// }
		// });
		//
		// // create alert dialog
		// AlertDialog alertDialog = alertDialogBuilder.create();
		//
		// // show it
		// alertDialog.show();
		// } catch (Exception e) {
		// System.err.println(e);
		// try {
		// IConstants.mContext.runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		// AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		// IConstants.mContext);
		//
		// // set title
		// alertDialogBuilder.setTitle("Exception");
		//
		// // set dialog message
		// alertDialogBuilder
		// .setMessage("" + errorReport.toString())
		// .setCancelable(false)
		// .setNeutralButton("Thanks",
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(
		// DialogInterface dialog,
		// int which) {
		// dialog.cancel();
		// }
		// });
		//
		// // create alert dialog
		// AlertDialog alertDialog = alertDialogBuilder.create();
		//
		// // show it
		// alertDialog.show();
		// }
		// });
		// } catch (Exception e2) {
		// System.err.println(e2);
		// }
		// } finally {
		// thread.stop();
		//
		// // Intent intent = new Intent(myContext, CrashReportActivity.class);
		// // intent.putExtra("error", errorReport.toString());
		// // myContext.startActivity(intent);
		// //
		// android.os.Process.killProcess(android.os.Process.myPid());
		// // System.exit(10);
		// }
	}
}