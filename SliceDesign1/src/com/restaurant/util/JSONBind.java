package com.restaurant.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class JSONBind {

	int TIMEOUT_MILLISEC = 60000; // = 60 seconds
	static InputStream is = null;
	static JSONObject jsonObject = null;
	static String json = "";
	static int status = 0;

	public JSONBind() {
	}

	public JSONObject restApiCall(String trxCode, String jsonData) {

		jsonObject = new JSONObject();

		// Making HTTP request
		try {

			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

			System.out.println(trxCode);
			System.out.println(IConstants.appRestIp);
			System.out.println(IConstants.appRestPort);
			System.out.println(IConstants.trxCode.get(trxCode).getApiUrl());

			trxCode = "http://" + IConstants.appRestIp + ":"
					+ IConstants.appRestPort + IConstants.appRestURL
					+ IConstants.appRestName + "/" + IConstants.appRestLocation
					+ "/" + IConstants.trxCode.get(trxCode).getApiUrl();

			System.out.println(replaceUrlSpaces(trxCode));

			HttpPost request = new HttpPost(replaceUrlSpaces(trxCode));

			StringEntity stringEntity = new StringEntity(jsonData);

			request.setEntity(stringEntity);

			request.addHeader("Content-Type", "application/json");
			request.addHeader("Accept", "application/Phoebuz.rest-v.1.0");

			HttpResponse httpResponse = httpClient.execute(request);

			System.out.println("Response Code : "
					+ httpResponse.getStatusLine().getStatusCode());
			status = httpResponse.getStatusLine().getStatusCode();

			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
				Log.e("Buffer json", "Result " + json);

			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			// try parse the string to a JSON object
			jsonObject = getJsonPack(json);

		} catch (UnsupportedEncodingException e) {
			System.out.println("UnsupportedEncodingException " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		} catch (ClientProtocolException e) {

			System.out.println("ClientProtocolException " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		} catch (ConnectException e) {

			System.out.println("ConnectException " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		} catch (IOException e) {

			System.out.println("IOException " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("Exception " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		}

		// return JSON String
		return jsonObject;
	}
	
	
	
	public JSONObject restApiCallWithExtras(String trxCode, String appendUrl, String jsonData) {

		jsonObject = new JSONObject();

		// Making HTTP request
		try {

			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

			System.out.println(trxCode);
			System.out.println(IConstants.appRestIp);
			System.out.println(IConstants.appRestPort);
			System.out.println(IConstants.trxCode.get(trxCode).getApiUrl());

			trxCode = "http://" + IConstants.appRestIp + ":"
					+ IConstants.appRestPort + IConstants.appRestURL
					+ IConstants.appRestName + "/" + IConstants.appRestLocation
					+ "/" + IConstants.trxCode.get(trxCode).getApiUrl() + "/" + appendUrl;

			System.out.println(replaceUrlSpaces(trxCode));

			HttpPost request = new HttpPost(replaceUrlSpaces(trxCode));

			StringEntity stringEntity = new StringEntity(jsonData);

			request.setEntity(stringEntity);

			request.addHeader("Content-Type", "application/json");
			request.addHeader("Accept", "application/Phoebuz.rest-v.1.0");

			HttpResponse httpResponse = httpClient.execute(request);

			System.out.println("Response Code : "
					+ httpResponse.getStatusLine().getStatusCode());
			status = httpResponse.getStatusLine().getStatusCode();

			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
				Log.e("Buffer json", "Result " + json);

			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			// try parse the string to a JSON object
			jsonObject = getJsonPack(json);

		} catch (UnsupportedEncodingException e) {
			System.out.println("UnsupportedEncodingException " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		} catch (ClientProtocolException e) {

			System.out.println("ClientProtocolException " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		} catch (ConnectException e) {

			System.out.println("ConnectException " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		} catch (IOException e) {

			System.out.println("IOException " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("Exception " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		}

		// return JSON String
		return jsonObject;
	}
	
	

	public JSONObject restMultiPartApiCall(String trxCode, String jsonData,
			File fileName) {

		jsonObject = new JSONObject();

		// Making HTTP request
		try {

			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

			// System.out.println(trxCode);
			// System.out.println(IConstants.appRestIp);
			// System.out.println(IConstants.appRestPort);
			// System.out.println(IConstants.trxCode.get(trxCode).getApiUrl());
			//
			// trxCode = "http://" + IConstants.appRestIp + ":"
			// + IConstants.appRestPort
			// + IConstants.appRestURL + IConstants.appRestName + "/" +
			// IConstants.appRestLocation + "/"
			// + IConstants.trxCode.get(trxCode).getApiUrl();
			//
			// System.out.println(replaceUrlSpaces(trxCode));

			// HttpPost request = new HttpPost(replaceUrlSpaces(trxCode));
			HttpPost request = new HttpPost(
					"http://192.168.1.2:8080/Business/V1/Restaurant/Heritage/Coimbatore/Menu/MenuDetails");

			StringEntity stringEntity = new StringEntity(jsonData);

			request.setEntity(stringEntity);

			FileBody bin = new FileBody(fileName);
			StringBody name = new StringBody("itemName.png");
			// StringBody jsonData = new
			// StringBody("{\"menuDetails\":[{\"trxSource\":\"a\",\"trxCode\":\"MNUPO\",\"userId\":\"EMP3\",\"itemName\":\"zozo\",\"itemType\":\"v\",\"itemCategory\":\"Mushroom\",\"subCategory\":\"\",\"prepType\":\"South Indian\",\"course\":\"Starter\",\"description\":\"dfg adfgdsfgsdfg sdg \",\"price\":\"12\",\"parcelPrice\":\"12\",\"calories\":\"12\",\"prepTime\":\"12\",\"printerId\":\"2\",\"ingId\":null}]}");
			jsonData = "{\"menuDetails\":[{\"trxSource\":\"a\",\"trxCode\":\"MNUPO\",\"userId\":\"EMP3\",\"itemName\":\"zozo\",\"itemType\":\"v\",\"itemCategory\":\"Mushroom\",\"subCategory\":\"\",\"prepType\":\"South Indian\",\"course\":\"Starter\",\"description\":\"dfg adfgdsfgsdfg sdg \",\"price\":\"12\",\"parcelPrice\":\"12\",\"calories\":\"12\",\"prepTime\":\"12\",\"printerId\":\"2\",\"ingId\":null}]}";
			StringBody jsonDataStringBody = new StringBody(jsonData);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("file", bin);
			reqEntity.addPart("name", name);
			reqEntity.addPart("jsonData", jsonDataStringBody);
			request.setEntity(reqEntity);

			// request.addHeader("Content-Type", "application/json");
			// request.addHeader("Accept", "application/Phoebuz.rest-v.1.0");

			HttpResponse httpResponse = httpClient.execute(request);

			System.out.println("Response Code : "
					+ httpResponse.getStatusLine().getStatusCode());
			status = httpResponse.getStatusLine().getStatusCode();

			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
				Log.e("Buffer json", "Result " + json);
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			// try parse the string to a JSON object
			jsonObject = getJsonPack(json);

		} catch (UnsupportedEncodingException e) {

			System.out.println("UnsupportedEncodingException " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		} catch (IllegalArgumentException e) {

			System.out.println("IllegalArgumentException " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		} catch (ClientProtocolException e) {

			System.out.println("ClientProtocolException " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		} catch (ConnectException e) {

			System.out.println("ConnectException " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		} catch (IOException e) {

			System.out.println("IOException " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("Exception " + e);

			jsonObject = getJsonPack(json, e);

			e.printStackTrace();
		}

		// return JSON String
		return jsonObject;
	}

	private JSONObject getJsonPack(final String jsonString) {

		JSONObject jsonObject = new JSONObject();
		try {
			try {
				jsonObject.put("status", status);
				jsonObject.put("jsonData", new JSONObject(jsonString));

			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
				jsonObject.put("status", status);
				jsonObject.put("jsonData", jsonString);
			}
			
			IConstants.mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {
//					IConstants.showToast("status " + status + ", jsonString " + jsonString);
				}
			});
		} catch (final Exception ex) {
			IConstants.mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					IConstants.showToast("getJsonPack " + ex);
				}
			});
		}

		return jsonObject;
	}

	private JSONObject getJsonPack(final String jsonString, final Exception exception) {

		JSONObject jsonObject = new JSONObject();
		try {
			try {
				jsonObject.put("status", status);
				jsonObject.put("jsonData", new JSONObject(jsonString));
			} catch (JSONException ex) {
				Log.e("JSON Parser", "Error parsing data " + ex.toString());
				jsonObject.put("status", status);
				jsonObject.put("jsonData", jsonString);
			}

			IConstants.mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					IConstants.showToast("status " + status + ", jsonString " + jsonString);
					if (exception != null) {
						IConstants.showToast(exception.getCause() + " "
								+ exception.getMessage());
					}
				}
			});
		} catch (final Exception ex) {
			IConstants.mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					IConstants.showToast("getJsonPack " + ex);
				}
			});
		}

		return jsonObject;
	}

	public String replaceUrlSpaces(String url) {
		return url.replaceAll(" ", "%20");
	}
}
