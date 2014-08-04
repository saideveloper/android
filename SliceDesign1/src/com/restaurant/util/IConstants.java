package com.restaurant.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.Toast;

import com.restaurant.model.TrxCodeDetails;

public class IConstants {
	public static int lol = 100;

	public static Activity mContext;

	// public static boolean firstTime = true;

	// public static int appTheme = ThemeManager.DARK;
	public static String appThemeColor = "#464646";

	public static String appName = "Slice";
	public static String appRestIp = null;
	public static String appRestPort = null;
	public static String appRestURL = "/Business/V1/Restaurant/";
	public static String appRestName = null;
	public static String appRestLocation = null;
	public static String appRestAddress = null;
	public static int appRestTimeout = 5000;

	public static final String appTrxSource = "a";
	public static final String appCusId = "CU";
	public static final String appEmpId = "EMP";
	public static final String appLayoutId = "FT";
	public static final String appActiveStatus = "a";
	public static final String appInActiveStatus = "i";
	public static final String appDeleteStatus = "x";
	
	//public static String appItemCategory = "";

	public static HashMap<String, TrxCodeDetails> trxCode = new HashMap<String, TrxCodeDetails>();

	public static boolean checkServer() {
		Socket sock = new Socket();
		InetSocketAddress addr = new InetSocketAddress(appRestIp,
				Integer.parseInt(appRestPort));
		try {
			sock.connect(addr, appRestTimeout);
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				sock.close();
			} catch (IOException e) {
			}
		}
	}

	public static boolean checkServer(String ip, String port) {
		Socket sock = new Socket();
		InetSocketAddress addr = new InetSocketAddress(ip,
				Integer.parseInt(port));
		try {
			sock.connect(addr, appRestTimeout);
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				sock.close();
			} catch (IOException e) {
			}
		}
	}

	private static final String PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static boolean validateIP(final String ip) {
		Pattern pattern = Pattern.compile(PATTERN);
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	public static int dpToPx(int dp) {
		return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}

	public static int pxToDp(int px) {
		return (int) (px / Resources.getSystem().getDisplayMetrics().density);
	}

	public static void showToast(String string) {
		Toast.makeText(mContext, "" + string, Toast.LENGTH_SHORT).show();
	}

	public static void logcat() {
		try {
//			Process process = Runtime.getRuntime().exec("logcat");
//			BufferedReader bufferedReader = new BufferedReader(
//					new InputStreamReader(process.getInputStream()));
//
//			StringBuilder log = new StringBuilder();
//			String line = "";
//			while ((line = bufferedReader.readLine()) != null) {
//				log.append(line);
//			}
//
//			FileOperations fileOperations = new FileOperations();
//
//			//String fileName = "logcat_" + System.currentTimeMillis() + ".txt";
//			// File outputFile = new File(mContext.getExternalCacheDir(),
//			// fileName);
//			// @SuppressWarnings("unused")
//			// Process process = Runtime.getRuntime().exec(
//			// "logcat -f " + outputFile.getAbsolutePath());
//			fileOperations.write("logcat_" + System.currentTimeMillis(), log.toString());
		} catch (Exception e) {
		}
	}
}
