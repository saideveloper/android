package com.restaurant.model;

import java.util.ArrayList;

import android.graphics.Bitmap;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class MenuDetails {

	@DatabaseField
	private Integer itemId;

	@DatabaseField
	private String itemName;

	@DatabaseField
	private String ingId;

	@DatabaseField
	private String itemCategory;

	@DatabaseField
	private String itemType;

	@DatabaseField
	private String prepType;

	@DatabaseField
	private String subCategory;

	@DatabaseField
	private Double price;

	@DatabaseField
	private Integer availability;

	@DatabaseField
	private String course;

	@DatabaseField
	private Integer calories;

	@DatabaseField
	private Integer printerId;

	@DatabaseField
	private Double parcelPrice;

	@DatabaseField
	private String description;

	@DatabaseField
	private String regDate;

	@DatabaseField
	private Integer prepTime;

	@DatabaseField
	private String itemStatus;
	
	@DatabaseField
	private String modifierId;
	
	@DatabaseField
	private String modifierCategory;
	
	@DatabaseField
	private String modifierStatus;
	
	@DatabaseField
	private String question1;
	
	@DatabaseField
	private String answer1;
	
	@DatabaseField
	private String question2;
	
	@DatabaseField
	private String answer2;
	
	@DatabaseField
	private String question3;
	
	@DatabaseField
	private String answer3;
	
	@DatabaseField
	private String question4;
	
	@DatabaseField
	private String answer4;
	
	@DatabaseField
	private String question5;
	
	@DatabaseField
	private String answer5;
	
	@DatabaseField
	private String offerPrice;
	
	@DatabaseField
	private String offerParcelPrice;

	@DatabaseField
	private Integer totalLikes;

	@DatabaseField
	private Double avgRatings;

	@DatabaseField
	private Integer totalReviews;

	@DatabaseField
	private String imgUrl;

	@DatabaseField
	private String lastUpdate;

	private boolean yetToView, toggleView;
	private Bitmap itemImageCircle, itemImageHoverCircle;
	private int firstView, quantity, itemImageHover, offerQuantity, orderedItemId;

	private OrderMasterDetails orderMasterDetails;
	private ModifierQuestionAnswer modifierQuestionAnswer;

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

	public String getIngId() {
		return ingId;
	}

	public void setIngId(String ingId) {
		this.ingId = ingId;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getPrepType() {
		return prepType;
	}

	public void setPrepType(String prepType) {
		this.prepType = prepType;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getAvailability() {
		return availability;
	}

	public void setAvailability(Integer availability) {
		this.availability = availability;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public Integer getCalories() {
		return calories;
	}

	public void setCalories(Integer calories) {
		this.calories = calories;
	}

	public Integer getPrinterId() {
		return printerId;
	}

	public void setPrinterId(Integer printerId) {
		this.printerId = printerId;
	}

	public Double getParcelPrice() {
		return parcelPrice;
	}

	public void setParcelPrice(Double parcelPrice) {
		this.parcelPrice = parcelPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public Integer getPrepTime() {
		return prepTime;
	}

	public void setPrepTime(Integer prepTime) {
		this.prepTime = prepTime;
	}

	public String getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
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

	public String getModifierStatus() {
		return modifierStatus;
	}

	public void setModifierStatus(String modifierStatus) {
		this.modifierStatus = modifierStatus;
	}

	public String getQuestion1() {
		return question1;
	}

	public void setQuestion1(String question1) {
		this.question1 = question1;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getQuestion2() {
		return question2;
	}

	public void setQuestion2(String question2) {
		this.question2 = question2;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getQuestion3() {
		return question3;
	}

	public void setQuestion3(String question3) {
		this.question3 = question3;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getQuestion4() {
		return question4;
	}

	public void setQuestion4(String question4) {
		this.question4 = question4;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public String getQuestion5() {
		return question5;
	}

	public void setQuestion5(String question5) {
		this.question5 = question5;
	}

	public String getAnswer5() {
		return answer5;
	}

	public void setAnswer5(String answer5) {
		this.answer5 = answer5;
	}
	
	public String getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(String offerPrice) {
		this.offerPrice = offerPrice;
	}

	public String getOfferParcelPrice() {
		return offerParcelPrice;
	}

	public void setOfferParcelPrice(String offerParcelPrice) {
		this.offerParcelPrice = offerParcelPrice;
	}

	public Integer getTotalLikes() {
		return totalLikes;
	}

	public void setTotalLikes(Integer totalLikes) {
		this.totalLikes = totalLikes;
	}

	public Double getAvgRatings() {
		return avgRatings;
	}

	public void setAvgRatings(Double avgRatings) {
		this.avgRatings = avgRatings;
	}

	public Integer getTotalReviews() {
		return totalReviews;
	}

	public void setTotalReviews(Integer totalReviews) {
		this.totalReviews = totalReviews;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public boolean isYetToView() {
		return yetToView;
	}

	public void setYetToView(boolean yetToView) {
		this.yetToView = yetToView;
	}

	public boolean isToggleView() {
		return toggleView;
	}

	public void setToggleView(boolean toggleView) {
		this.toggleView = toggleView;
	}

	public int getItemImageHover() {
		return itemImageHover;
	}

	public void setItemImageHover(int itemImageHover) {
		this.itemImageHover = itemImageHover;
	}

	public Bitmap getItemImageCircle() {
		return itemImageCircle;
	}

	public void setItemImageCircle(Bitmap itemImageCircle) {
		this.itemImageCircle = itemImageCircle;
	}

	public Bitmap getItemImageHoverCircle() {
		return itemImageHoverCircle;
	}

	public void setItemImageHoverCircle(Bitmap itemImageHoverCircle) {
		this.itemImageHoverCircle = itemImageHoverCircle;
	}

	public int getFirstView() {
		return firstView;
	}

	public void setFirstView(int firstView) {
		this.firstView = firstView;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getOfferQuantity() {
		return offerQuantity;
	}

	public void setOfferQuantity(int offerQuantity) {
		this.offerQuantity = offerQuantity;
	}

	public int getOrderedItemId() {
		return orderedItemId;
	}

	public void setOrderedItemId(int orderedItemId) {
		this.orderedItemId = orderedItemId;
	}

	public OrderMasterDetails getOrderMasterDetails() {
		return orderMasterDetails;
	}

	public void setOrderMasterDetails(OrderMasterDetails orderMasterDetails) {
		this.orderMasterDetails = orderMasterDetails;
	}

	public ModifierQuestionAnswer getModifierQuestionAnswer() {
		return modifierQuestionAnswer;
	}

	public void setModifierQuestionAnswer(
			ModifierQuestionAnswer modifierQuestionAnswer) {
		this.modifierQuestionAnswer = modifierQuestionAnswer;
	}
}
