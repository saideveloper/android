package com.restaurant.model;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.restaurant.util.IConstants;

@DatabaseTable
public class SalesDetails {

	public SalesDetails() {
		receiptNo = 0;
		orderNo = 0;
		billAmt = 0.0;
		totalAmt = 0.0;
		tipAmt = 0.0;
		discount = 0.0;
		paid = 0;
		payType = "CS";
		payDesc = "";
		serviceTax = 0.0;
		serviceCharge = 0.0;
		vatTax = 0.0;
		otherTax = 0.0;
		couponCode = "";
		sessionCd = "";
		serviceTaxPercentage = "";
		serviceChargePercentage = "";
		vatTaxPercentage = "";
		otherTaxPercentage = "";
		salesDesc = "";
		orderDesc = "";
		printerId = 0;
		cusId = IConstants.appEmpId;
		empName = IConstants.appEmpId;
		layoutId = IConstants.appLayoutId;
		salesStatus = "a";
		userId = IConstants.appEmpId;
		trxCode = "SLEPO";
		trxSource = IConstants.appTrxSource;
	}

	@Expose
	@DatabaseField
	private Integer receiptNo;

	@Expose
	@DatabaseField
	private Integer orderNo;

	@Expose
	@DatabaseField
	private String cusId;

	@Expose
	private String empName;
	
	@Expose
	private String layoutId;
	
	@Expose
	@DatabaseField
	private Double billAmt;

	@Expose
	@DatabaseField
	private Double tipAmt;

	@Expose
	@DatabaseField
	private Double totalAmt;

	@Expose
	@DatabaseField
	private Integer paid;

	@Expose
	@DatabaseField
	private String payType;

	@Expose
	private String payDesc;
	
	@Expose
	@DatabaseField
	private Double discount;

	@Expose
	@DatabaseField
	private String couponCode;

	@Expose
	@DatabaseField
	private String sessionCd;

	@Expose
	@DatabaseField
	private Double vatTax;

	@Expose
	@DatabaseField
	private Double serviceCharge;

	@Expose
	@DatabaseField
	private Double serviceTax;

	@Expose
	@DatabaseField
	private Double otherTax;

	@Expose
	private String serviceTaxPercentage;

	@Expose
	private String serviceChargePercentage;

	@Expose
	private String vatTaxPercentage;

	@Expose
	private String otherTaxPercentage;

	@Expose
	@DatabaseField
	private String salesDesc;
	
	@Expose
	private String orderDesc;
	
	@Expose
	@DatabaseField
	private String salesStatus;

	@Expose
	@DatabaseField
	private String salesDate;

	@Expose
	@DatabaseField
	private String trxCode;

	@Expose
	@DatabaseField
	private String trxSource;

	@Expose
	@DatabaseField
	private String userId;
	
	@Expose
	private Integer printerId;

	@DatabaseField
	private String lastUpdate;

	public Integer getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(Integer receiptNo) {
		this.receiptNo = receiptNo;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}

	public Double getBillAmt() {
		return billAmt;
	}

	public void setBillAmt(Double billAmt) {
		this.billAmt = billAmt;
	}

	public Double getTipAmt() {
		return tipAmt;
	}

	public void setTipAmt(Double tipAmt) {
		this.tipAmt = tipAmt;
	}

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public Integer getPaid() {
		return paid;
	}

	public void setPaid(Integer paid) {
		this.paid = paid;
	}

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

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getSessionCd() {
		return sessionCd;
	}

	public void setSessionCd(String sessionCd) {
		this.sessionCd = sessionCd;
	}

	public Double getVatTax() {
		return vatTax;
	}

	public void setVatTax(Double vatTax) {
		this.vatTax = vatTax;
	}

	public Double getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(Double serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public Double getServiceTax() {
		return serviceTax;
	}

	public void setServiceTax(Double serviceTax) {
		this.serviceTax = serviceTax;
	}

	public Double getOtherTax() {
		return otherTax;
	}

	public void setOtherTax(Double otherTax) {
		this.otherTax = otherTax;
	}

	public String getServiceTaxPercentage() {
		return serviceTaxPercentage;
	}

	public void setServiceTaxPercentage(String serviceTaxPercentage) {
		this.serviceTaxPercentage = serviceTaxPercentage;
	}

	public String getServiceChargePercentage() {
		return serviceChargePercentage;
	}

	public void setServiceChargePercentage(String serviceChargePercentage) {
		this.serviceChargePercentage = serviceChargePercentage;
	}

	public String getVatTaxPercentage() {
		return vatTaxPercentage;
	}

	public void setVatTaxPercentage(String vatTaxPercentage) {
		this.vatTaxPercentage = vatTaxPercentage;
	}

	public String getOtherTaxPercentage() {
		return otherTaxPercentage;
	}

	public void setOtherTaxPercentage(String otherTaxPercentage) {
		this.otherTaxPercentage = otherTaxPercentage;
	}

	public String getSalesDesc() {
		return salesDesc;
	}

	public void setSalesDesc(String salesDesc) {
		this.salesDesc = salesDesc;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public String getSalesStatus() {
		return salesStatus;
	}

	public void setSalesStatus(String salesStatus) {
		this.salesStatus = salesStatus;
	}

	public String getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(String salesDate) {
		this.salesDate = salesDate;
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

	public Integer getPrinterId() {
		return printerId;
	}

	public void setPrinterId(Integer printerId) {
		this.printerId = printerId;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
