package com.restaurant.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class TrxCodeDetails {

	@DatabaseField
	private String trxCode;

	@DatabaseField
	private String apiName;

	@DatabaseField
	private String trxMethod;

	@DatabaseField
	private String apiUrl;

	@DatabaseField
	private String trxPriority;
	
	@DatabaseField
	private String eTag;

	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getTrxMethod() {
		return trxMethod;
	}

	public void setTrxMethod(String trxMethod) {
		this.trxMethod = trxMethod;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getTrxPriority() {
		return trxPriority;
	}

	public void setTrxPriority(String trxPriority) {
		this.trxPriority = trxPriority;
	}

	public String getETag() {
		return eTag;
	}

	public void setETag(String eTag) {
		this.eTag = eTag;
	}
}
