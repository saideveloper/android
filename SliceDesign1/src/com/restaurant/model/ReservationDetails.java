package com.restaurant.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ReservationDetails {

	@DatabaseField
	private int reservationId;

	@DatabaseField
	private Integer restId;

	@DatabaseField
	private String cusId;

	@DatabaseField
	private String tableNo;

	@DatabaseField
	private Integer noOfGuest;

	@DatabaseField
	private String reservedFor;

	@DatabaseField
	private String resDate;

	@DatabaseField
	private String resNote;

	@DatabaseField
	private Double noOfHours;

	@DatabaseField
	private String trxCode;

	@DatabaseField
	private String trxSource;

	@DatabaseField
	private String userId;

	@DatabaseField
	private String reservationStatus;

	@DatabaseField
	private String lastUpdate;

	public int getReservationId() {
		return reservationId;
	}

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	public Integer getRestId() {
		return restId;
	}

	public void setRestId(Integer restId) {
		this.restId = restId;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public Integer getNoOfGuest() {
		return noOfGuest;
	}

	public void setNoOfGuest(Integer noOfGuest) {
		this.noOfGuest = noOfGuest;
	}

	public String getReservedFor() {
		return reservedFor;
	}

	public void setReservedFor(String reservedFor) {
		this.reservedFor = reservedFor;
	}

	public String getResDate() {
		return resDate;
	}

	public void setResDate(String resDate) {
		this.resDate = resDate;
	}

	public String getResNote() {
		return resNote;
	}

	public void setResNote(String resNote) {
		this.resNote = resNote;
	}

	public Double getNoOfHours() {
		return noOfHours;
	}

	public void setNoOfHours(Double noOfHours) {
		this.noOfHours = noOfHours;
	}

	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}

	public String getTrxSource() {
		return trxSource;
	}

	public void setTrxSource(String trxSource) {
		this.trxSource = trxSource;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReservationStatus() {
		return reservationStatus;
	}

	public void setReservationStatus(String reservationStatus) {
		this.reservationStatus = reservationStatus;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
