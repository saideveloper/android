package com.restaurant.model.lkup;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class LkupPrepTypeDetails {

	@DatabaseField
	private Integer typeId;

	@DatabaseField
	private String prepType;

	@DatabaseField
	private Integer cloudUpdate;

	@DatabaseField
	private String lastUpdate;

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getPrepType() {
		return prepType;
	}

	public void setPrepType(String prepType) {
		this.prepType = prepType;
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
