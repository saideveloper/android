package com.restaurant.model;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class RestAddonItemsDetails {

	@DatabaseField
	private Integer addonId;

	@DatabaseField
	private String addonName;

	@DatabaseField
	private String type;

	@DatabaseField
	private String addonStatus;

	@DatabaseField
	private Integer cloudUpdate;

	@Expose
	@DatabaseField
	private String lastUpdate;

	public Integer getAddonId() {
		return addonId;
	}

	public void setAddonId(Integer addonId) {
		this.addonId = addonId;
	}

	public String getAddonName() {
		return addonName;
	}

	public void setAddonName(String addonName) {
		this.addonName = addonName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddonStatus() {
		return addonStatus;
	}

	public void setAddonStatus(String addonStatus) {
		this.addonStatus = addonStatus;
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
