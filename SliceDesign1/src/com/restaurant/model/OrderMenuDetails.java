package com.restaurant.model;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.restaurant.util.IConstants;

@DatabaseTable
public class OrderMenuDetails {

	public OrderMenuDetails() {
		//sequenceNo = 0;
		itemName = "";
		modifierId = "";
		printerId = 0;
		modifierCategory = "";
		havingAnswer = "";
		userId = IConstants.appEmpId;
		trxSource = IConstants.appTrxSource;
	}

	@DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
	private Integer orderMenuId;

//	@DatabaseField
//	private Integer sequenceNo;

	@DatabaseField
	private Integer orderNo;

	@Expose
	@DatabaseField
	private Integer itemId;

	@Expose
	@DatabaseField
	private String itemName;

	@Expose
	@DatabaseField
	private String modifierId;

	@Expose
	@DatabaseField
	private String modifierCategory;
	
	@Expose
	@DatabaseField
	private String modifierDesc;

	@Expose
	@DatabaseField
	private String modifierStatus;

	@Expose
	@DatabaseField
	private Integer quantity;

	@Expose
	@DatabaseField
	private String course;

	@Expose
	@DatabaseField
	private Integer cookTime;

	@Expose
	@DatabaseField
	private String orderDate;

	@Expose
	@DatabaseField
	private Double price;

	@Expose
	@DatabaseField
	private Double itemPrice;
	
	@Expose
	@DatabaseField
	private String noteToChef;

	@Expose
	@DatabaseField
	private String orderType;

	@Expose
	@DatabaseField
	private String allergy;
	
	@Expose
	@DatabaseField
	private Integer printerId;
	
	@DatabaseField
	private boolean editable;
	
	@DatabaseField
	private String havingAnswer;

	@Expose
	@DatabaseField
	private String userId;

	@Expose
	@DatabaseField
	private String trxCode;

	@Expose
	@DatabaseField
	private String trxSource;

	@Expose
	@DatabaseField
	private String itemStatus;

	@DatabaseField
	private String lastUpdate;

	public Integer getOrderMenuId() {
		return orderMenuId;
	}

	public void setOrderMenuId(Integer orderMenuId) {
		this.orderMenuId = orderMenuId;
	}

//	public Integer getSequenceNo() {
//		return sequenceNo;
//	}
//
//	public void setSequenceNo(Integer sequenceNo) {
//		this.sequenceNo = sequenceNo;
//	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getModifierId() {
		return modifierId;
	}

	public void setModifierId(String modifierId) {
		this.modifierId = modifierId;
	}

	public String getModifierCategory() {
		return modifierCategory;
	}

	public void setModifierCategory(String modifierCategory) {
		this.modifierCategory = modifierCategory;
	}

	public String getModifierDesc() {
		return modifierDesc;
	}

	public void setModifierDesc(String modifierDesc) {
		this.modifierDesc = modifierDesc;
	}

	public String getModifierStatus() {
		return modifierStatus;
	}

	public void setModifierStatus(String modifierStatus) {
		this.modifierStatus = modifierStatus;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getChefNote() {
		return noteToChef;
	}

	public void setChefNote(String chefNote) {
		this.noteToChef = chefNote;
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

	public Integer getPrinterId() {
		return printerId;
	}

	public void setPrinterId(Integer printerId) {
		this.printerId = printerId;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean edit) {
		this.editable = edit;
	}

	public String isHavingAnswer() {
		return havingAnswer;
	}

	public void setHavingAnswer(String havingAnswer) {
		this.havingAnswer = havingAnswer;
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

	public String getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
