package com.restaurant.model;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class RestTimingDetails {

	@DatabaseField
	private Integer timeId;

	@DatabaseField
	private String timingCategory;

	@DatabaseField
	private String ids;

	@DatabaseField
	private String fromTime;

	@DatabaseField
	private String toTime;

	@DatabaseField
	private Integer sunday;

	@DatabaseField
	private Integer monday;

	@DatabaseField
	private Integer tuesday;

	@DatabaseField
	private Integer wednesday;

	@DatabaseField
	private Integer thursday;

	@DatabaseField
	private Integer friday;

	@DatabaseField
	private Integer saturday;

	@DatabaseField
	private String timeStatus;

	@DatabaseField
	private Integer cloudUpdate;
	
	@Expose
	@DatabaseField
	private String lastUpdate;

	public Integer getTimeId() {
		return timeId;
	}

	public void setTimeId(Integer timeId) {
		this.timeId = timeId;
	}

	public String getTimingCategory() {
		return timingCategory;
	}

	public void setTimingCategory(String timingCategory) {
		this.timingCategory = timingCategory;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public Integer getSunday() {
		return sunday;
	}

	public void setSunday(Integer sunday) {
		this.sunday = sunday;
	}

	public Integer getMonday() {
		return monday;
	}

	public void setMonday(Integer monday) {
		this.monday = monday;
	}

	public Integer getTuesday() {
		return tuesday;
	}

	public void setTuesday(Integer tuesday) {
		this.tuesday = tuesday;
	}

	public Integer getWednesday() {
		return wednesday;
	}

	public void setWednesday(Integer wednesday) {
		this.wednesday = wednesday;
	}

	public Integer getThursday() {
		return thursday;
	}

	public void setThursday(Integer thursday) {
		this.thursday = thursday;
	}

	public Integer getFriday() {
		return friday;
	}

	public void setFriday(Integer friday) {
		this.friday = friday;
	}

	public Integer getSaturday() {
		return saturday;
	}

	public void setSaturday(Integer saturday) {
		this.saturday = saturday;
	}

	public String getTimeStatus() {
		return timeStatus;
	}

	public void setTimeStatus(String timeStatus) {
		this.timeStatus = timeStatus;
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
