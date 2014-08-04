package com.restaurant.model;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.restaurant.util.IConstants;

@DatabaseTable
public class OrderOfferDetails {
	
	public OrderOfferDetails() {
		userId = IConstants.appEmpId;
		trxSource = IConstants.appTrxSource;
	}
	
	@Expose
	@DatabaseField
	private Integer orderOfferId;
	
	@Expose
	@DatabaseField
	private Integer orderNo;

	@DatabaseField
	private Integer offerId;

	@DatabaseField
	private Integer quantity;

	@DatabaseField
	private Double price;

	@DatabaseField
	private String course;

	@DatabaseField
	private Integer cookTime;

	@DatabaseField
	private String orderDate;

	@DatabaseField
	private String noteToChef;

	@DatabaseField
	private String orderType;

	@DatabaseField
	private String allergy;
	
	@Expose
	@DatabaseField
	private String userId;
	
	@Expose
	@DatabaseField
	private String trxCode;

	@DatabaseField
	private String trxSource;

	@DatabaseField
	private String offerStatus;
	
	@Expose
	@DatabaseField
	private String lastUpdate;

	public Integer getOrderOfferId() {
		return orderOfferId;
	}

	public void setOrderOfferId(Integer orderOfferId) {
		this.orderOfferId = orderOfferId;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getOfferId() {
		return offerId;
	}

	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public Integer getCookTime() {
		return cookTime;
	}

	public void setCookTime(Integer cookTime) {
		this.cookTime = cookTime;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getNoteToChef() {
		return noteToChef;
	}

	public void setNoteToChef(String noteToChef) {
		this.noteToChef = noteToChef;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getAllergy() {
		return allergy;
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getOfferStatus() {
		return offerStatus;
	}

	public void setOfferStatus(String offerStatus) {
		this.offerStatus = offerStatus;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
