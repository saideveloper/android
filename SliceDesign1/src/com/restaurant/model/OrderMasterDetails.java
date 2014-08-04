package com.restaurant.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.restaurant.util.IConstants;

@DatabaseTable
public class OrderMasterDetails {

	public OrderMasterDetails() {
		cusId = IConstants.appCusId;
		empId = IConstants.appEmpId;
		layoutId = IConstants.appLayoutId;
		orderDesc = "";
		orderType = "";
		location = "";
		printerId = 0;
		orderStatus = IConstants.appActiveStatus;
		userId = IConstants.appEmpId;
		trxCode = "ORDPO";
		trxSource = IConstants.appTrxSource;
	}

	@Expose
	@DatabaseField
	private Integer orderNo;

	@Expose
	@DatabaseField
	private Integer itemId;

	@Expose
	@DatabaseField
	private String cusId;

	@Expose
	@DatabaseField
	private String empId;

	@Expose
	@DatabaseField
	private String layoutId;

	@Expose
	@DatabaseField
	private String orderDesc;

	@Expose
	@DatabaseField
	private String orderType;
	
	@Expose
	@DatabaseField
	private String chefNotes;

	@Expose
	@DatabaseField
	private String orderDate;

	@Expose
	@DatabaseField
	private String sessionCd;

	@Expose
	@DatabaseField
	private int printerId;

	@Expose
	@DatabaseField
	private String orderStatus;

	@Expose
	@DatabaseField
	private String location;

	@Expose
	@DatabaseField
	private String trxCode;

	@Expose
	@DatabaseField
	private String trxSource;

	@Expose
	@DatabaseField
	private String userId;

	@DatabaseField
	private String lastUpdate;
	
	@Expose
	private List<OrderMenuDetails> itemList;
	
	@Expose
	private List<OrderOfferDetails> offerList;

	private int mQuantity, mCourse, mAllergy;

	private float mAlpha;

	private double mPrice, mTotalPrice;

	private String mOrderTitle, mChefnotes;

	private boolean mCanEdit;

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

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getChefNotes() {
		return chefNotes;
	}

	public void setChefNotes(String chefNotes) {
		this.chefNotes = chefNotes;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getSessionCd() {
		return sessionCd;
	}

	public void setSessionCd(String sessionCd) {
		this.sessionCd = sessionCd;
	}

	public int getPrinterId() {
		return printerId;
	}

	public void setPrinterId(int printerId) {
		this.printerId = printerId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int getQuantity() {
		return mQuantity;
	}

	public void setQuantity(int quantity) {
		this.mQuantity = quantity;
	}

	public int getCourse() {
		return mCourse;
	}

	public void setCourse(int course) {
		this.mCourse = course;
	}

	public int getAllergy() {
		return mAllergy;
	}

	public void setAllergy(int allergy) {
		this.mAllergy = allergy;
	}

	public float getAlpha() {
		return mAlpha;
	}

	public void setAlpha(float alpha) {
		this.mAlpha = alpha;
	}

	public double getPrice() {
		return mPrice;
	}

	public void setPrice(double price) {
		this.mPrice = price;
	}

	public double getTotalPrice() {
		return mTotalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.mTotalPrice = totalPrice;
	}

	public String getOrderTitle() {
		return mOrderTitle;
	}

	public void setOrderTitle(String orderTitle) {
		this.mOrderTitle = orderTitle;
	}

	public String getChefnotes() {
		return mChefnotes;
	}

	public void setChefnotes(String chefnotes) {
		this.mChefnotes = chefnotes;
	}

	public boolean isCanEdit() {
		return mCanEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.mCanEdit = canEdit;
	}

	public List<OrderMenuDetails> getOrderMenuDetails() {
		return itemList;
	}

	public void setOrderMenuDetails(List<OrderMenuDetails> orderMenuDetails) {
		this.itemList = orderMenuDetails;
	}

	public List<OrderOfferDetails> getOrderOfferDetails() {
		return offerList;
	}

	public void setOrderOfferDetails(List<OrderOfferDetails> orderOfferDetails) {
		this.offerList = orderOfferDetails;
	}
}
