package com.restaurant.adapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.restaurant.app.HomeActivity;
import com.restaurant.app.MenuActivity;
import com.restaurant.app.R;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.ModifierQuestionAnswer;
import com.restaurant.model.RestAddonItemsDetails;
import com.restaurant.util.IConstants;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity context;
	private Map<String, List<ModifierQuestionAnswer>> modifierCollection;
	private List<String> questions;
	private TextView modifierPrice, modifierQuantity;
	private int modifierIndex = -1;

	private String modifier = "", itemName = "";

	public ExpandableListAdapter(Activity context, List<String> questions,
			Map<String, List<ModifierQuestionAnswer>> modifierCollection,
			String modifier, String itemName, TextView modifierPrice,
			TextView modifierQuantity) {
		this.context = context;
		this.modifierCollection = modifierCollection;
		this.questions = questions;
		this.modifier = modifier;
		this.itemName = itemName;
		this.modifierPrice = modifierPrice;
		this.modifierQuantity = modifierQuantity;
	}

	public void updateAdapter(Activity context, List<String> questions,
			Map<String, List<ModifierQuestionAnswer>> modifierCollection,
			String modifier, String itemName, TextView modifierPrice,
			TextView modifierQuantity) {
		this.context = context;
		this.modifierCollection = modifierCollection;
		this.questions = questions;
		this.modifier = modifier;
		this.itemName = itemName;
		this.modifierPrice = modifierPrice;
		this.modifierQuantity = modifierQuantity;
		notifyDataSetChanged();
	}

	public Object getChild(int groupPosition, int childPosition) {
		return modifierCollection.get(questions.get(groupPosition)).get(
				childPosition);
	}

	public Object getSpecialChild(int groupPosition, int childPosition) {
		return modifierCollection.get(questions.get(groupPosition)).get(
				childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final ModifierQuestionAnswer modifierQuestionAnswer = (ModifierQuestionAnswer) getChild(
				groupPosition, childPosition);
		final String question = (String) getGroup(groupPosition);

		LayoutInflater inflater = context.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.child_item, null);
		}

		try {

			final TextView answer = (TextView) convertView
					.findViewById(R.id.answer);
			answer.setText("" + modifierQuestionAnswer.getAnswer());

			final TextView rupees = (TextView) convertView
					.findViewById(R.id.rupees);
			rupees.setText("" + modifierQuestionAnswer.getModifierPrice());

			final CheckBox checkToAdd = (CheckBox) convertView
					.findViewById(R.id.checkToAdd);
			
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					checkToAdd.setChecked(!checkToAdd.isChecked());
					
					System.out.println("onCheckedChanged "
							+ answer.getText().toString());

					modifierQuestionAnswer.setChecked(checkToAdd.isChecked());

					Double price = Double.parseDouble(modifierPrice
							.getText().toString());

					Integer integer = Integer.parseInt(modifierQuantity
							.getText().toString());
					
					if (integer <= 0) {
						integer = 1;
					}
					System.out.println("price " + price);
					
					if(checkToAdd.isChecked()){
						price = price
								+ (modifierQuestionAnswer
										.getModifierPrice() * integer);
					} else {
						price = price
								- (modifierQuestionAnswer
										.getModifierPrice() * integer);
					}
					
					System.out.println("price " + price);
					
					if(price > modifierQuestionAnswer.getPrice()){
						modifierPrice.setText("" + price);
					} else {
						modifierPrice.setText("" + modifierQuestionAnswer.getPrice());
					}

					HomeActivity.modifierAddedItems.put(itemName + "_"
							+ modifier + "_" + question + "_"
							+ modifierQuestionAnswer.getAnswer(),
							modifierQuestionAnswer);
				}
			});

//			checkToAdd
//					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//						@Override
//						public void onCheckedChanged(CompoundButton buttonView,
//								boolean isChecked) {
//
//							System.out.println("onCheckedChanged "
//									+ answer.getText().toString());
//
//							modifierQuestionAnswer.setChecked(isChecked);
//
//							Double price = Double.parseDouble(modifierPrice
//									.getText().toString());
//
//							Integer integer = Integer.parseInt(modifierQuantity
//									.getText().toString());
//							
//							if (integer <= 0) {
//								integer = 1;
//							}
//							System.out.println("price " + price);
//
//							if (isChecked) {
//								price = price
//										+ (modifierQuestionAnswer
//												.getModifierPrice() * integer);
//							} else {
//								price = price
//										- (modifierQuestionAnswer
//												.getModifierPrice() * integer);
//							}
//
//							System.out.println("price " + price);
//
//							modifierPrice.setText("" + price);
//
//							MenuAdapter.modifierAddedItems.put(itemName + "_"
//									+ modifier + "_" + question + "_"
//									+ modifierQuestionAnswer.getAnswer(),
//									modifierQuestionAnswer);
//						}
//					});

			if (null != HomeActivity.modifierAddedItems.get(itemName + "_"
					+ modifier + "_" + question + "_"
					+ modifierQuestionAnswer.getAnswer())) {
				checkToAdd.setChecked(HomeActivity.modifierAddedItems.get(
						itemName + "_" + modifier + "_" + question + "_"
								+ modifierQuestionAnswer.getAnswer())
						.getChecked());
				System.out.println(itemName + "_" + modifier + "_" + question
						+ "_" + modifierQuestionAnswer.getAnswer());
			} else {
				modifierQuestionAnswer.setChecked(false);
				HomeActivity.modifierAddedItems.put(
						itemName + "_" + modifier + "_" + question + "_"
								+ modifierQuestionAnswer.getAnswer(),
						modifierQuestionAnswer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return modifierCollection.get(questions.get(groupPosition)).size();
	}

	public Object getGroup(int groupPosition) {
		return questions.get(groupPosition);
	}

	public int getGroupCount() {
		return questions.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String laptopName = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.group_item, null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.laptop);
		item.setTypeface(null, Typeface.BOLD);
		item.setText(laptopName);
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}