package com.restaurant.model;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class RestItemTypeDetails {

	@DatabaseField
	private String itemTypeCd;

	@DatabaseField
	private String itemType;

	@DatabaseField
	private String itemTypeStatus;

	@DatabaseField
	private Integer cloudUpdate;
	
	@Expose
	@DatabaseField
	private String lastUpdate;

	public String getItemTypeCd() {
		return itemTypeCd;
	}

	public void setItemTypeCd(String itemTypeCd) {
		this.itemTypeCd = itemTypeCd;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemTypeStatus() {
		return itemTypeStatus;
	}

	public void setItemTypeStatus(String itemTypeStatus) {
		this.itemTypeStatus = itemTypeStatus;
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
