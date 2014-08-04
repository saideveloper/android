package com.restaurant.model.lkup;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class LkupPayTypeDetails {

	@DatabaseField
	private String payType;

	@DatabaseField
	private String payDesc;

	@DatabaseField
	private String payStatus;

	@DatabaseField
	private Integer cloudUpdate;

	@Expose
	@DatabaseField
	private String lastUpdate;

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayDesc() {
		return payDesc;
	}

	public void setPayDesc(String payDesc) {
		this.payDesc = payDesc;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getCloudUpdate() {
		return cloudUpdate;
	}

	public void setCloudUpdate(Integer cloudUpdate) {
		this.cloudUpdate = cloudUpdate;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
