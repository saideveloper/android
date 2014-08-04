package com.restaurant.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class RestaurantDetails {

	@DatabaseField
	private int restId;

	@DatabaseField
	private Integer propertyId;

	@DatabaseField
	private String restName;

	@DatabaseField
	private String location;

	@DatabaseField
	private String latLong;

	@DatabaseField
	private String address1;

	@DatabaseField
	private String address2;

	@DatabaseField
	private String address3;

	@DatabaseField
	private String address4;

	@DatabaseField
	private String webSite;

	@DatabaseField
	private String emailId;

	@DatabaseField
	private String fbPage;

	@DatabaseField
	private String twitterAccount;

	@DatabaseField
	private Long contactNo;

	@DatabaseField
	private Long landLine;

	@DatabaseField
	private String cityCd;

	@DatabaseField
	private String stateCd;

	@DatabaseField
	private String countryCd;

	@DatabaseField
	private String pinCode;

	@DatabaseField
	private String landMark;

	@DatabaseField
	private String restDescription;

	@DatabaseField
	private Integer totalLikes;

	@DatabaseField
	private Double restRatings;

	@DatabaseField
	private Double empRatings;

	@DatabaseField
	private Double foodRatings;

	@DatabaseField
	private Integer totalReviews;

	@DatabaseField
	private String regDate;

	@DatabaseField
	private String imageUrl;

	@DatabaseField
	private String lastUpdate;

	@DatabaseField
	private String validFrom;

	@DatabaseField
	private String validTo;

	@DatabaseField
	private String restStatus;

	public int getRestId() {
		return restId;
	}

	public void setRestId(int restId) {
		this.restId = restId;
	}

	public Integer getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}

	public String getRestName() {
		return restName;
	}

	public void setRestName(String restName) {
		this.restName = restName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getAddress4() {
		return address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getFbPage() {
		return fbPage;
	}

	public void setFbPage(String fbPage) {
		this.fbPage = fbPage;
	}

	public String getTwitterAccount() {
		return twitterAccount;
	}

	public void setTwitterAccount(String twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public Long getContactNo() {
		return contactNo;
	}

	public void setContactNo(Long contactNo) {
		this.contactNo = contactNo;
	}

	public Long getLandLine() {
		return landLine;
	}

	public void setLandLine(Long landLine) {
		this.landLine = landLine;
	}

	public String getCityCd() {
		return cityCd;
	}

	public void setCityCd(String cityCd) {
		this.cityCd = cityCd;
	}

	public String getStateCd() {
		return stateCd;
	}

	public void setStateCd(String stateCd) {
		this.stateCd = stateCd;
	}

	public String getCountryCd() {
		return countryCd;
	}

	public void setCountryCd(String countryCd) {
		this.countryCd = countryCd;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getLandMark() {
		return landMark;
	}

	public void setLandMark(String landMark) {
		this.landMark = landMark;
	}

	public String getRestDescription() {
		return restDescription;
	}

	public void setRestDescription(String restDescription) {
		this.restDescription = restDescription;
	}

	public Integer getTotalLikes() {
		return totalLikes;
	}

	public void setTotalLikes(Integer totalLikes) {
		this.totalLikes = totalLikes;
	}

	public Double getRestRatings() {
		return restRatings;
	}

	public void setRestRatings(Double restRatings) {
		this.restRatings = restRatings;
	}

	public Double getEmpRatings() {
		return empRatings;
	}

	public void setEmpRatings(Double empRatings) {
		this.empRatings = empRatings;
	}

	public Double getFoodRatings() {
		return foodRatings;
	}

	public void setFoodRatings(Double foodRatings) {
		this.foodRatings = foodRatings;
	}

	public Integer getTotalReviews() {
		return totalReviews;
	}

	public void setTotalReviews(Integer totalReviews) {
		this.totalReviews = totalReviews;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}

	public String getRestStatus() {
		return restStatus;
	}

	public void setRestStatus(String restStatus) {
		this.restStatus = restStatus;
	}
	
	

}
