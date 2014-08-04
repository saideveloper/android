package com.restaurant.model;

import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ModifierQuestionAnswer {

	@DatabaseField(generatedId = true)
	private Integer modifierUniqueId;

	@DatabaseField
	private Integer itemId;

	@DatabaseField
	private String itemName;

	@DatabaseField
	private String modifierId;

	@DatabaseField
	private String modifierCategory;

	@DatabaseField
	private String subCategory;

	@DatabaseField
	private Integer whichQuestion;

	@DatabaseField
	private Double price;

	@DatabaseField
	private Double parcelPrice;

	@DatabaseField
	private String question;

	
	private JSONObject jsonAnswer;
	
	@DatabaseField
	private String addonId;
	
	@DatabaseField
	private String answer;

	@DatabaseField
	private Double modifierPrice;

	@DatabaseField
	private Integer quantity;

	private Double totalPrice;

	@DatabaseField
	private Boolean checked;

	public Integer getModifierUniqueId() {
		return modifierUniqueId;
	}

	public void setModifierUniqueId(Integer modifierUniqueId) {
		this.modifierUniqueId = modifierUniqueId;
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

	public void setItemName(String string) {
		this.itemName = string;
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

	public Double getParcelPrice() {
		return parcelPrice;
	}

	public void setParcelPrice(Double parcelPrice) {
		this.parcelPrice = parcelPrice;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public JSONObject getJsonAnswer() {
		return jsonAnswer;
	}

	public void setJsonAnswer(JSONObject jsonAnswer) {
		this.jsonAnswer = jsonAnswer;
	}

	public String getAddonId() {
		return addonId;
	}

	public void setAddonId(String addonId) {
		this.addonId = addonId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Double getModifierPrice() {
		return modifierPrice;
	}

	public void setModifierPrice(Double modifierPrice) {
		this.modifierPrice = modifierPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
}
