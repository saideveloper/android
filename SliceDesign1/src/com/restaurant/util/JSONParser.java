package com.restaurant.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.TrxCodeDetails;

import android.util.Log;

public class JSONParser {

	int TIMEOUT_MILLISEC = 60000; // = 60 seconds
	static InputStream is = null;
	static JSONObject jsonObject = null;
	static String json = "";
	static int status = 0;

	// constructor
	public JSONParser() {
		
	}

	public JSONObject restApiCall(String trxCode) {

		jsonObject = new JSONObject();

		// Making HTTP request
		try {

			String orgTrxCode = trxCode;

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

			HttpGet request = new HttpGet(replaceUrlSpaces(trxCode));

			request.addHeader("Content-Type", "application/Phoebuz.rest-v.1.0");
			request.addHeader("Accept", "application/json");

			try {
				request.addHeader("If-None-Match",
						IConstants.trxCode.get(orgTrxCode).getETag());
				System.out.println("IConstants.trxCode.get(orgTrxCode).getETag() " + IConstants.trxCode.get(orgTrxCode).getETag());
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}

			HttpResponse httpResponse = httpClient.execute(request);
			
			String etag = httpResponse.getFirstHeader("etag")
					.toString().split(":")[1].trim();

			Log.d("Http Response:", etag);

			try {
				// IConstants.trxCode.get(orgTrxCode).setETag(
				// httpResponse.getFirstHeader("etag").toString());
				
				
				TrxCodeDetails trxCodeDetail = IConstants.trxCode
						.get(orgTrxCode);
				trxCodeDetail.setETag(etag);
				
				
				UpdateBuilder<TrxCodeDetails, Integer> updateBuilder = DatabaseManager.getInstance().getTrxCodeDetailsDao().updateBuilder();
				updateBuilder.where().eq("trxCode", orgTrxCode);
				updateBuilder.updateColumnValue("eTag", etag);
				updateBuilder.update();
				
				
				IConstants.trxCode.get(orgTrxCode).setETag(etag);

			} catch (Exception ex1) {
				ex1.printStackTrace();
			}

			System.out.println("Response Code : "
					+ httpResponse.getStatusLine().getStatusCode());
			status = httpResponse.getStatusLine().getStatusCode();

			HttpEntity httpEntity = httpResponse.getEntity();
			if(null!=httpEntity){
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
	
	
	
	public JSONObject restApiCallWithArgs(String trxCode, String args) {

		jsonObject = new JSONObject();

		// Making HTTP request
		try {

			String orgTrxCode = trxCode;

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
					+ "/" + IConstants.trxCode.get(trxCode).getApiUrl() + "/rest?" + args;

			System.out.println(replaceUrlSpaces(trxCode));

			HttpGet request = new HttpGet(replaceUrlSpaces(trxCode));

			request.addHeader("Content-Type", "application/Phoebuz.rest-v.1.0");
			request.addHeader("Accept", "application/json");

			try {
				request.addHeader("If-None-Match",
						IConstants.trxCode.get(orgTrxCode).getETag());
				System.out.println("IConstants.trxCode.get(orgTrxCode).getETag() " + IConstants.trxCode.get(orgTrxCode).getETag());
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}

			HttpResponse httpResponse = httpClient.execute(request);
			
			String etag = httpResponse.getFirstHeader("etag")
					.toString().split(":")[1].trim();

			Log.d("Http Response:", etag);

			try {
				// IConstants.trxCode.get(orgTrxCode).setETag(
				// httpResponse.getFirstHeader("etag").toString());
				
				
				TrxCodeDetails trxCodeDetail = IConstants.trxCode
						.get(orgTrxCode);
				trxCodeDetail.setETag(etag);
				
				
				UpdateBuilder<TrxCodeDetails, Integer> updateBuilder = DatabaseManager.getInstance().getTrxCodeDetailsDao().updateBuilder();
				updateBuilder.where().eq("trxCode", orgTrxCode);
				updateBuilder.updateColumnValue("eTag", etag);
				updateBuilder.update();
				
				
				IConstants.trxCode.get(orgTrxCode).setETag(etag);

			} catch (Exception ex1) {
				ex1.printStackTrace();
			}

			System.out.println("Response Code : "
					+ httpResponse.getStatusLine().getStatusCode());
			status = httpResponse.getStatusLine().getStatusCode();

			HttpEntity httpEntity = httpResponse.getEntity();
			if(null!=httpEntity){
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
	

	public JSONObject restOtherApiCall(String trxCode) {

		jsonObject = new JSONObject();

		// Making HTTP request
		try {
			
			String orgTrxCode = trxCode;

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
					+ "/" + IConstants.trxCode.get(trxCode).getApiUrl();

			System.out.println(replaceUrlSpaces(trxCode));

			HttpGet request = new HttpGet(replaceUrlSpaces(trxCode));

			request.addHeader("Content-Type", "application/Phoebuz.rest-v.1.0");
			request.addHeader("Accept", "application/json");

			try {
				request.addHeader("If-None-Match",
						IConstants.trxCode.get(orgTrxCode).getETag());
				System.out.println("IConstants.trxCode.get(orgTrxCode).getETag() " + IConstants.trxCode.get(orgTrxCode).getETag());
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}

			HttpResponse httpResponse = httpClient.execute(request);
			
			String etag = httpResponse.getFirstHeader("etag")
					.toString().split(":")[1].trim();

			Log.d("Http Response:", etag);

			try {
				// IConstants.trxCode.get(orgTrxCode).setETag(
				// httpResponse.getFirstHeader("etag").toString());
				
				
				TrxCodeDetails trxCodeDetail = IConstants.trxCode
						.get(orgTrxCode);
				trxCodeDetail.setETag(etag);
				
				
				UpdateBuilder<TrxCodeDetails, Integer> updateBuilder = DatabaseManager.getInstance().getTrxCodeDetailsDao().updateBuilder();
				updateBuilder.where().eq("trxCode", orgTrxCode);
				updateBuilder.updateColumnValue("eTag", etag);
				updateBuilder.update();
				
				
				IConstants.trxCode.get(orgTrxCode).setETag(etag);

			} catch (Exception ex1) {
				ex1.printStackTrace();
			}

			System.out.println("Response Code : "
					+ httpResponse.getStatusLine().getStatusCode());
			status = httpResponse.getStatusLine().getStatusCode();

//			HttpParams httpParams = new BasicHttpParams();
//			HttpConnectionParams.setConnectionTimeout(httpParams,
//					TIMEOUT_MILLISEC);
//			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
//
//			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
//
//			System.out.println(trxCode);
//			System.out.println(IConstants.appRestIp);
//			System.out.println(IConstants.appRestPort);
//
//			trxCode = "http://" + IConstants.appRestIp + ":"
//					+ IConstants.appRestPort + IConstants.appRestURL + trxCode;
//
//			System.out.println(replaceUrlSpaces(trxCode));
//
//			HttpGet request = new HttpGet(replaceUrlSpaces(trxCode));
//
//			// HttpGet request = new HttpGet(url);
//			request.addHeader("Content-Type", "application/Phoebuz.rest-v.1.0");
//			request.addHeader("Accept", "application/json");
//
//			HttpResponse httpResponse = httpClient.execute(request);
//
//			System.out.println("Response Code : "
//					+ httpResponse.getStatusLine().getStatusCode());
//			status = httpResponse.getStatusLine().getStatusCode();

			HttpEntity httpEntity = httpResponse.getEntity();
			if(null!=httpEntity){
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
			}

			// // try parse the string to a JSON object
			jsonObject = getJsonPack(json);

		} catch (UnsupportedEncodingException e) {
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
	
	
	
	
	public JSONObject restOtherApiCallWithArgs(String trxCode, String args) {

		jsonObject = new JSONObject();

		// Making HTTP request
		try {
			
			String orgTrxCode = trxCode;

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
					+ "/" + IConstants.trxCode.get(trxCode).getApiUrl() + "/rest?" + args;

			System.out.println(replaceUrlSpaces(trxCode));

			HttpGet request = new HttpGet(replaceUrlSpaces(trxCode));

			request.addHeader("Content-Type", "application/Phoebuz.rest-v.1.0");
			request.addHeader("Accept", "application/json");

			try {
				request.addHeader("If-None-Match",
						IConstants.trxCode.get(orgTrxCode).getETag());
				System.out.println("IConstants.trxCode.get(orgTrxCode).getETag() " + IConstants.trxCode.get(orgTrxCode).getETag());
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}

			HttpResponse httpResponse = httpClient.execute(request);
			
			String etag = httpResponse.getFirstHeader("etag")
					.toString().split(":")[1].trim();

			Log.d("Http Response:", etag);

			try {
				// IConstants.trxCode.get(orgTrxCode).setETag(
				// httpResponse.getFirstHeader("etag").toString());
				
				
				TrxCodeDetails trxCodeDetail = IConstants.trxCode
						.get(orgTrxCode);
				trxCodeDetail.setETag(etag);
				
				
				UpdateBuilder<TrxCodeDetails, Integer> updateBuilder = DatabaseManager.getInstance().getTrxCodeDetailsDao().updateBuilder();
				updateBuilder.where().eq("trxCode", orgTrxCode);
				updateBuilder.updateColumnValue("eTag", etag);
				updateBuilder.update();
				
				
				IConstants.trxCode.get(orgTrxCode).setETag(etag);

			} catch (Exception ex1) {
				ex1.printStackTrace();
			}

			System.out.println("Response Code : "
					+ httpResponse.getStatusLine().getStatusCode());
			status = httpResponse.getStatusLine().getStatusCode();

//			HttpParams httpParams = new BasicHttpParams();
//			HttpConnectionParams.setConnectionTimeout(httpParams,
//					TIMEOUT_MILLISEC);
//			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
//
//			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
//
//			System.out.println(trxCode);
//			System.out.println(IConstants.appRestIp);
//			System.out.println(IConstants.appRestPort);
//
//			trxCode = "http://" + IConstants.appRestIp + ":"
//					+ IConstants.appRestPort + IConstants.appRestURL + trxCode;
//
//			System.out.println(replaceUrlSpaces(trxCode));
//
//			HttpGet request = new HttpGet(replaceUrlSpaces(trxCode));
//
//			// HttpGet request = new HttpGet(url);
//			request.addHeader("Content-Type", "application/Phoebuz.rest-v.1.0");
//			request.addHeader("Accept", "application/json");
//
//			HttpResponse httpResponse = httpClient.execute(request);
//
//			System.out.println("Response Code : "
//					+ httpResponse.getStatusLine().getStatusCode());
//			status = httpResponse.getStatusLine().getStatusCode();

			HttpEntity httpEntity = httpResponse.getEntity();
			if(null!=httpEntity){
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
			}

			// // try parse the string to a JSON object
			jsonObject = getJsonPack(json);

		} catch (UnsupportedEncodingException e) {
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
	
	
	
	public JSONObject restSpecialApiCall(String trxCode) {

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

			trxCode = "http://" + IConstants.appRestIp + ":"
					+ IConstants.appRestPort + IConstants.appRestURL + trxCode;

			System.out.println(replaceUrlSpaces(trxCode));

			HttpGet request = new HttpGet(replaceUrlSpaces(trxCode));

			// HttpGet request = new HttpGet(url);
			request.addHeader("Content-Type", "application/Phoebuz.rest-v.1.0");
			request.addHeader("Accept", "application/json");

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

			// // try parse the string to a JSON object
			jsonObject = getJsonPack(json);

		} catch (UnsupportedEncodingException e) {
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


	public int restCheckServer(String ip, String port) {

		try {

			status = 0;

			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

			System.out.println("http://" + ip + ":" + port
					+ IConstants.appRestURL + "CheckServer");

			HttpGet request = new HttpGet("http://" + ip + ":" + port
					+ IConstants.appRestURL + "CheckServer");

			request.addHeader("Content-Type", "application/Phoebuz.rest-v.1.0");
			request.addHeader("Accept", "application/json");

			HttpResponse httpResponse = httpClient.execute(request);

			System.out.println("Response Code : "
					+ httpResponse.getStatusLine().getStatusCode());
			status = httpResponse.getStatusLine().getStatusCode();

			IConstants.mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {
//					IConstants.showToast("status " + status);
				}
			});
		} catch (final UnsupportedEncodingException e) {
			System.out.println("UnsupportedEncodingException " + e);
			IConstants.mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					IConstants.showToast("status " + status
							+ ", UnsupportedEncodingException " + e);
				}
			});
			e.printStackTrace();
		} catch (final ClientProtocolException e) {
			System.out.println("ClientProtocolException " + e);
			IConstants.mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					IConstants.showToast("status " + status
							+ ", ClientProtocolException " + e);
				}
			});
			e.printStackTrace();
		} catch (final ConnectException e) {
			System.out.println("ConnectException " + e);
			IConstants.mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					IConstants.showToast("status " + status
							+ ", ConnectException " + e);
				}
			});
			e.printStackTrace();
		} catch (final IOException e) {
			System.out.println("IOException " + e);
			IConstants.mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					IConstants.showToast("status " + status + ", IOException "
							+ e);
				}
			});
			e.printStackTrace();
		} catch (final Exception e) {
			System.out.println("Exception " + e);
			IConstants.mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					IConstants.showToast("status " + status + ", Exception "
							+ e);
				}
			});
			e.printStackTrace();
		}

		return status;
	}

	public JSONObject resturantDetails(String restDetails) {

		jsonObject = new JSONObject();

		// Making HTTP request
		try {
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

			System.out.println(restDetails);
			System.out.println(IConstants.appRestIp);
			System.out.println(IConstants.appRestPort);
			System.out.println(IConstants.appRestURL);
			System.out.println(restDetails);

			restDetails = "http://" + IConstants.appRestIp + ":"
					+ IConstants.appRestPort + IConstants.appRestURL
					+ restDetails;

			System.out.println(replaceUrlSpaces(restDetails));

			HttpGet request = new HttpGet(replaceUrlSpaces(restDetails));

			request.addHeader("Content-Type", "application/Phoebuz.rest-v.1.0");
			request.addHeader("Accept", "application/json");

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

			IConstants.mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {
//					IConstants.showToast("status " + status + ", json " + json);
				}
			});

			// // try parse the string to a JSON object
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
//					 IConstants.showToast("status " + status + ", jsonString "
//					 + jsonString);
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

	private JSONObject getJsonPack(final String jsonString,
			final Exception exception) {

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
//					 IConstants.showToast("status " + status + ", jsonString "
//					 + jsonString);
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