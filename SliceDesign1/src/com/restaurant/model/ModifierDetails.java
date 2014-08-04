package com.restaurant.model;

import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ModifierDetails {

	@DatabaseField
	private Integer index;

	@DatabaseField
	private Integer itemId;

	@DatabaseField
	private String modifierId;

	@DatabaseField
	private String modifierCategory;

	@DatabaseField
	private String subCategory;

	@DatabaseField
	private Integer whichQuestion;

	@DatabaseField
	private String ItemName;

	@DatabaseField
	private Double price;

	@DatabaseField
	private Double parcelPrice;

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
	private Boolean checked;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getItemName() {
		return ItemName;
	}

	public void setItemName(String itemName) {
		ItemName = itemName;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getModifierId() {
		return modifierId;
	}

	public void setModifierId(String string) {
		this.modifierId = string;
	}

	public String getModifierCategory() {
		return modifierCategory;
	}

	public void setModifierCategory(String modifierCategory) {
		this.modifierCategory = modifierCategory;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public Integer getWhichQuestion() {
		return whichQuestion;
	}

	public void setWhichQuestion(Integer whichQuestion) {
		this.whichQuestion = whichQuestion;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Boolean isChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Double getParcelPrice() {
		return parcelPrice;
	}

	public void setParcelPrice(Double parcelPrice) {
		this.parcelPrice = parcelPrice;
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
}
