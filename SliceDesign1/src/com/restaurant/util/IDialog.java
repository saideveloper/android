package com.restaurant.util;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.restaurant.adapter.ExpandableListAdapter;
import com.restaurant.adapter.MenuAdapter.AnimateOrderIcon;
import com.restaurant.adapter.ModifierExpandableListAdapter;
import com.restaurant.app.ExpandableMenuActivity;
import com.restaurant.app.HomeActivity;
import com.restaurant.app.MenuActivity;
import com.restaurant.app.OrderNewActivity3;
import com.restaurant.app.R;
import com.restaurant.controller.SalesController;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.ModifierQuestionAnswer;
import com.restaurant.model.OrderMasterDetails;
import com.restaurant.model.OrderMenuDetails;
import com.restaurant.model.SalesDetails;
import com.restaurant.model.lkup.LkupOrderStatusDetails;
import com.restaurant.model.lkup.LkupPayTypeDetails;
import com.restaurant.model.lkup.LkupRestTaxDetails;
import com.restaurant.model.lkup.LkupTaxListDetails;

public class IDialog {

	ProgressDialog progressDialog = null;

	public static Double modifierPriceValue = 0.0, orgModifierPriceValue = 0.0;
	String choosedModifier = "";
	String choosedModifierId = "";

	Double price = 0.0, taxAmount = 0.0;
	String payType = "CS", payDesc = "";

	public interface TryAgain {
		void tryAgain();
	}

	public IDialog() {
		// TODO IDialog constructor
	}

	public void chefNote(final Activity activity,
			final OrderMenuDetails orderMenuDetail,
			final AnimateOrderIcon animateOrderIconListener) {

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(false).setTitle("Add Chef Notes");

		builder.setPositiveButton("Add", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.setNeutralButton("No thanks", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		final AlertDialog dialog = builder.create();
		LayoutInflater inflater = activity.getLayoutInflater();
		final View serverConnectDialog = inflater.inflate(R.layout.chef_notes,
				null);

		final EditText chefnote = (EditText) serverConnectDialog
				.findViewById(R.id.chefnote);
		chefnote.setText("" + orderMenuDetail.getChefNote());
		chefnote.setSelection(chefnote.getText().length());

		dialog.setView(serverConnectDialog);
		dialog.show();
		// Overriding the handler immediately after show is probably a better
		// approach than OnShowListener as described below
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if ((chefnote.getText().toString().trim() + "") == "") {
							chefnote.setError("please enter some chefnote!");
						} else {
							orderMenuDetail.setChefNote(""
									+ chefnote.getText().toString().trim());
							animateOrderIconListener.animate();
							dialog.dismiss();

							try {
								// OrderMenuDetails orderMenuDetail =
								// DatabaseManager
								// .getInstance().getOrderMenuDetailsDao().queryBuilder()
								// .distinct().where().eq("itemId",
								// menuDetail.getItemId())
								// .query().get(0);
								// System.out.println("orderMenuDetail itemId" +
								// orderMenuDetail.getItemId());
								orderMenuDetail.setChefNote(""
										+ chefnote.getText().toString().trim());
								DatabaseManager.getInstance()
										.getOrderMenuDetailsDao()
										.update(orderMenuDetail);
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
				});

		dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						animateOrderIconListener.animate();
						ExpandableMenuActivity.updateADapter();
					}
				});
		dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							orderMenuDetail.setChefNote("");
							DatabaseManager.getInstance()
									.getOrderMenuDetailsDao()
									.update(orderMenuDetail);
						} catch (Exception e) {
							// TODO: handle exception
						}
						animateOrderIconListener.animate();
						dialog.dismiss();
						ExpandableMenuActivity.updateADapter();
					}
				});
	}

	public void chefNote(final Activity activity,
			final TextView chefNoteTextView) {

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(false).setTitle("Add Chef Notes");

		builder.setPositiveButton("Add", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.setNeutralButton("No thanks", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		final AlertDialog dialog = builder.create();
		LayoutInflater inflater = activity.getLayoutInflater();
		final View serverConnectDialog = inflater.inflate(R.layout.chef_notes,
				null);

		final EditText chefnote = (EditText) serverConnectDialog
				.findViewById(R.id.chefnote);
		chefnote.setText("" + OrderNewActivity3.chefnotes);
		chefnote.setSelection(chefnote.getText().length());

		dialog.setView(serverConnectDialog);
		dialog.show();
		// Overriding the handler immediately after show is probably a better
		// approach than OnShowListener as described below
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if ((chefnote.getText().toString().trim() + "") == "") {
							chefnote.setError("please enter some chefnote!");
						} else {
							// orderMenuDetail.setChefNote(""
							// + chefnote.getText().toString().trim());
							chefNoteTextView.setText(""
									+ chefnote.getText().toString().trim());

							OrderNewActivity3.chefnotes = ""
									+ chefnote.getText().toString().trim();

							// if (orderMenuDetail.getChefNote() != "") {
							// chefNoteTextView.setVisibility(View.VISIBLE);
							// } else {
							// chefNoteTextView.setVisibility(View.GONE);
							// }

							// try {
							// DatabaseManager.getInstance()
							// .getOrderMenuDetailsDao()
							// .update(orderMenuDetail);
							//
							// ExpandableMenuActivity.updateADapter();
							// } catch (Exception e) {
							// // TODO: handle exception
							// }

							dialog.dismiss();
						}
					}
				});
		dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						ExpandableMenuActivity.updateADapter();
					}
				});
		dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// orderMenuDetail.setChefNote("");
						chefNoteTextView.setText("");
						OrderNewActivity3.chefnotes = "";

						// if (orderMenuDetail.getChefNote() != "") {
						// chefNoteTextView.setVisibility(View.VISIBLE);
						// } else {
						// chefNoteTextView.setVisibility(View.GONE);
						// }

						// try {
						// DatabaseManager.getInstance()
						// .getOrderMenuDetailsDao()
						// .update(orderMenuDetail);
						// ExpandableMenuActivity.updateADapter();
						// } catch (Exception e) {
						// // TODO: handle exception
						// }

						dialog.dismiss();
					}
				});
	}

	public void voidItem(final Activity activity, OrderMenuDetails orderMenuDetail) {

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(false).setTitle("Void " + orderMenuDetail.getItemName());

		builder.setPositiveButton("Void", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		final AlertDialog dialog = builder.create();
		LayoutInflater inflater = activity.getLayoutInflater();
		final View voidItemDialog = inflater.inflate(R.layout.void_item_dialog,
				null);

		final Spinner voidType = (Spinner) voidItemDialog
				.findViewById(R.id.voidType);

		final EditText voidDesc = (EditText) voidItemDialog
				.findViewById(R.id.voidDesc);

		ArrayAdapter<String> dataAdapter;
		List<String> orderStatusList = new ArrayList<String>();
		HashMap<String, String> orderStatusHashMap = new HashMap<String, String>();

		try {
			List<LkupOrderStatusDetails> lkupOrderStatusDetails = DatabaseManager
					.getInstance().getLkupOrderStatusDetailsDao()
					.queryBuilder().orderBy("orderStatusDesc", true)
					.query();

			for (LkupOrderStatusDetails lkupOrderStatusDetail : lkupOrderStatusDetails) {
				orderStatusList.add(""
						+ lkupOrderStatusDetail.getOrderStatusDesc());
				orderStatusHashMap.put(
						lkupOrderStatusDetail.getOrderStatusDesc(),
						lkupOrderStatusDetail.getOrderStatusCode());
			}
			
			dataAdapter = new ArrayAdapter<String>(activity,
					R.layout.spinner_list, orderStatusList);

			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			voidType.setAdapter(dataAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		dialog.setView(voidItemDialog);
		dialog.show();
		// Overriding the handler immediately after show is probably a better
		// approach than OnShowListener as described below
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						dialog.dismiss();
					}
				});
		dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						dialog.dismiss();
					}
				});
	}

	// ----------------------------------------------------------------

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void newmodifier(final MenuDetails menuDetail) {

		try {
			modifierPriceValue = 0.0;
			orgModifierPriceValue = 0.0;

			final List<String> modifierCategoryList = new ArrayList<String>();
			List<String> modifierQuestionsList = new ArrayList<String>();
			List<ModifierQuestionAnswer> modifierAnswersList = new ArrayList<ModifierQuestionAnswer>();

			final HashMap<String, ModifierQuestionAnswer> modifierQuestionAnswerHashMap = new HashMap<String, ModifierQuestionAnswer>();
			Map<String, List<ModifierQuestionAnswer>> modifierCollection = new LinkedHashMap<String, List<ModifierQuestionAnswer>>();

			final List<ModifierQuestionAnswer> modifierQuestionAnswerList = DatabaseManager
					.getInstance().getModifierQuestionAnswerDao()
					.queryBuilder().distinct().where()
					.in("itemId", menuDetail.getItemId()).query();

			// for (ModifierQuestionAnswer modifierQuestionAnswer :
			// modifierQuestionAnswerList) {
			// System.out
			// .println("modifierQuestionAnswer.getModifierUniqueId() "
			// + modifierQuestionAnswer.getModifierUniqueId());
			// }

			for (ModifierQuestionAnswer modifierQuestionAnswer : modifierQuestionAnswerList) {
				modifierQuestionAnswer.setChecked(false);
				try {
					DatabaseManager.getInstance()
							.getModifierQuestionAnswerDao()
							.update(modifierQuestionAnswer);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (ModifierQuestionAnswer modifierQuestionAnswer : modifierQuestionAnswerList) {
				modifierCategoryList.add(modifierQuestionAnswer
						.getModifierCategory());
				modifierQuestionAnswerHashMap.put(
						modifierQuestionAnswer.getModifierCategory(),
						modifierQuestionAnswer);
			}

			HashSet hashSet = new HashSet();
			hashSet.addAll(modifierCategoryList);
			modifierCategoryList.clear();
			modifierCategoryList.addAll(hashSet);

			modifierCategoryList.removeAll(Arrays.asList(new Object[] { null,
					"null" }));

			List<ModifierQuestionAnswer> modifierQuestionAnswerListTemp = DatabaseManager
					.getInstance().getModifierQuestionAnswerDao()
					.queryBuilder().distinct().where()
					.eq("modifierCategory", modifierCategoryList.get(0)).and()
					.eq("itemId", menuDetail.getItemId()).query();

			for (int i = 0; i < modifierQuestionAnswerListTemp.size(); i++) {
				modifierQuestionsList.add(modifierQuestionAnswerListTemp.get(i)
						.getQuestion());
			}

			hashSet = new HashSet();
			hashSet.addAll(modifierQuestionsList);
			modifierQuestionsList.clear();
			modifierQuestionsList.addAll(hashSet);

			modifierQuestionsList.removeAll(Arrays.asList(new Object[] { null,
					"null" }));

			for (int i = 0; i < modifierQuestionsList.size(); i++) {
				modifierAnswersList = new ArrayList<ModifierQuestionAnswer>();
				modifierQuestionAnswerListTemp = DatabaseManager.getInstance()
						.getModifierQuestionAnswerDao().queryBuilder()
						.distinct().where()
						.eq("modifierCategory", modifierCategoryList.get(0))
						.and().eq("question", modifierQuestionsList.get(i))
						.and().eq("itemId", menuDetail.getItemId()).query();

				List<String> modifierAnswerDistinctList = new ArrayList<String>();
				for (int j = 0; j < modifierQuestionAnswerListTemp.size(); j++) {
					if (modifierAnswerDistinctList
							.contains(modifierQuestionAnswerListTemp.get(j)
									.getAnswer()))
						continue;

					modifierAnswerDistinctList
							.add(modifierQuestionAnswerListTemp.get(j)
									.getAnswer());
					modifierAnswersList.add(modifierQuestionAnswerListTemp
							.get(j));
				}

				modifierCollection.put(modifierQuestionsList.get(i),
						modifierAnswersList);
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(
					IConstants.mContext);
			builder.setCancelable(false);

			builder.setPositiveButton("Add", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			builder.setNegativeButton("Cancel", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});

			final AlertDialog dialog = builder.create();
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LayoutInflater inflater = IConstants.mContext.getLayoutInflater();
			final View modifierDialog = inflater.inflate(R.layout.modifier,
					null);

			TextView modifierTitle = (TextView) modifierDialog
					.findViewById(R.id.modifierTitle);
			modifierTitle.setText(menuDetail.getItemName());

			final TextView modifierQuantity = (TextView) modifierDialog
					.findViewById(R.id.modifierQuantity);
			modifierQuantity.setText("1");

			modifierPriceValue = modifierQuestionAnswerHashMap.get(
					modifierCategoryList.get(0)).getPrice();
			orgModifierPriceValue = modifierPriceValue;

			choosedModifierId = modifierQuestionAnswerHashMap.get(
					modifierCategoryList.get(0)).getModifierId();

			final TextView modifierPrice = (TextView) modifierDialog
					.findViewById(R.id.modifierPrice);
			modifierPrice.setText("" + modifierPriceValue);

			final ImageView modifierAdd = (ImageView) modifierDialog
					.findViewById(R.id.layoutAdd);
			final View modifierAddRemoveDivider = (View) modifierDialog
					.findViewById(R.id.layoutAddRemoveDivider);
			final ImageView modifierRemove = (ImageView) modifierDialog
					.findViewById(R.id.layoutRemove);
			// final LinearLayout modifierChefnotes = (LinearLayout)
			// modifierDialog
			// .findViewById(R.id.layoutChefnotes);

			modifierAdd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					modifierAddRemoveDivider.setVisibility(View.VISIBLE);
					modifierRemove.setVisibility(View.VISIBLE);

					Double tempPrice = 0.0, temoModifierPrice = 0.0;
					Integer modifierQuantityTemp = Integer
							.parseInt(modifierQuantity.getText().toString());
					modifierQuantityTemp++;
					modifierQuantity.setText("" + modifierQuantityTemp);

					tempPrice = orgModifierPriceValue * modifierQuantityTemp;

					List<ModifierQuestionAnswer> modifierQuestionAnswerListTemp;
					try {
						modifierQuestionAnswerListTemp = DatabaseManager
								.getInstance().getModifierQuestionAnswerDao()
								.queryBuilder().distinct().where()
								.eq("modifierCategory", choosedModifier).and()
								.eq("itemId", menuDetail.getItemId()).and()
								.eq("checked", true).query();

						for (int i = 0; i < modifierQuestionAnswerListTemp
								.size(); i++) {
							temoModifierPrice = temoModifierPrice
									+ (modifierQuestionAnswerListTemp.get(i)
											.getModifierPrice() * modifierQuantityTemp);
						}

						modifierPrice.setText(""
								+ (tempPrice + temoModifierPrice));

						modifierPriceValue = tempPrice + temoModifierPrice;
					} catch (SQLException e) {
						e.printStackTrace();
					}

					System.out.println("add modifierPriceValue "
							+ modifierPriceValue);
				}
			});

			modifierRemove.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Integer modifierQuantityTemp = Integer
							.parseInt(modifierQuantity.getText().toString());
					if (modifierQuantityTemp > 1) {
						modifierQuantityTemp--;
						modifierQuantity.setText("" + modifierQuantityTemp);

						// modifierPrice.setText(""
						// + (modifierPriceValue * modifierQuantityTemp));

						Double tempPrice = 0.0, temoModifierPrice = 0.0;

						tempPrice = orgModifierPriceValue
								* modifierQuantityTemp;

						List<ModifierQuestionAnswer> modifierQuestionAnswerListTemp;
						try {
							modifierQuestionAnswerListTemp = DatabaseManager
									.getInstance()
									.getModifierQuestionAnswerDao()
									.queryBuilder().distinct().where()
									.eq("modifierCategory", choosedModifier)
									.and().eq("itemId", menuDetail.getItemId())
									.and().eq("checked", true).query();

							for (int i = 0; i < modifierQuestionAnswerListTemp
									.size(); i++) {
								temoModifierPrice = temoModifierPrice
										+ (modifierQuestionAnswerListTemp
												.get(i).getModifierPrice() * modifierQuantityTemp);
							}

							modifierPrice.setText(""
									+ (tempPrice + temoModifierPrice));

							modifierPriceValue = tempPrice + temoModifierPrice;
						} catch (SQLException e) {
							e.printStackTrace();
						}

						if (modifierQuantityTemp > 1) {
						} else {
							modifierAddRemoveDivider.setVisibility(View.GONE);
							modifierRemove.setVisibility(View.GONE);

							modifierPriceValue = orgModifierPriceValue;

							modifierPrice.setText("" + (modifierPriceValue));
						}
					} else {
						modifierAddRemoveDivider.setVisibility(View.GONE);
						modifierRemove.setVisibility(View.GONE);

						modifierPriceValue = orgModifierPriceValue;

						modifierPrice.setText("" + (modifierPriceValue));

						// dialog.dismiss();
					}

					System.out.println("rem modifierPriceValue "
							+ modifierPriceValue);
				}
			});

			// modifierChefnotes.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			//
			// }
			// });

			final LinearLayout hscrollbar = (LinearLayout) modifierDialog
					.findViewById(R.id.hscrollbar);

			final ExpandableListView expandableListView = (ExpandableListView) modifierDialog
					.findViewById(R.id.laptop_list);

			final ModifierExpandableListAdapter modifierExpandableListAdapter = new ModifierExpandableListAdapter(
					IConstants.mContext, modifierQuestionsList,
					modifierCollection, modifierCategoryList.get(0),
					menuDetail.getItemName(), modifierPriceValue,
					modifierPrice, modifierQuantity);
			expandableListView.setAdapter(modifierExpandableListAdapter);

			choosedModifier = "";

			int len = modifierCategoryList.size();
			for (int i = 0; i < len; i++) {
				Button button = new Button(IConstants.mContext);
				button.setText("" + modifierCategoryList.get(i));
				button.setTextColor(Color.WHITE);
				button.setBackgroundColor(Color.parseColor("#99000000"));
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.MATCH_PARENT);

				layoutParams.setMargins(1, 1, 1, 1);

				if (len < 5)
					layoutParams.weight = 1;

				button.setLayoutParams(layoutParams);
				button.setGravity(Gravity.CENTER);
				button.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						for (ModifierQuestionAnswer modifierQuestionAnswer : modifierQuestionAnswerList) {
							modifierQuestionAnswer.setChecked(false);
							try {
								DatabaseManager.getInstance()
										.getModifierQuestionAnswerDao()
										.update(modifierQuestionAnswer);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						modifierAddRemoveDivider.setVisibility(View.GONE);
						modifierRemove.setVisibility(View.GONE);

						choosedModifier = ((TextView) v).getText().toString();

						System.out.println("onClick " + choosedModifier);

						modifierQuantity.setText("1");

						for (int j = 0; j < hscrollbar.getChildCount(); j++) {
							hscrollbar.getChildAt(j).setBackgroundColor(
									Color.parseColor("#99000000"));
						}

						v.setBackgroundResource(R.drawable.green_btn_gradient);
						System.out.println(choosedModifier);

						modifierPriceValue = modifierQuestionAnswerHashMap.get(
								choosedModifier).getPrice();

						orgModifierPriceValue = modifierPriceValue;

						choosedModifierId = modifierQuestionAnswerHashMap.get(
								choosedModifier).getModifierId();

						Integer integer = Integer.parseInt(modifierQuantity
								.getText().toString());
						modifierPrice.setText(""
								+ (modifierPriceValue * integer));

						try {
							List<String> modifierQuestionsList = new ArrayList<String>();
							List<ModifierQuestionAnswer> modifierAnswersList = new ArrayList<ModifierQuestionAnswer>();
							Map<String, List<ModifierQuestionAnswer>> modifierCollection = new LinkedHashMap<String, List<ModifierQuestionAnswer>>();

							List<ModifierQuestionAnswer> modifierQuestionAnswerListTemp;
							modifierQuestionAnswerListTemp = DatabaseManager
									.getInstance()
									.getModifierQuestionAnswerDao()
									.queryBuilder().distinct().where()
									.eq("modifierCategory", choosedModifier)
									.and().eq("itemId", menuDetail.getItemId())
									.query();

							for (int i = 0; i < modifierQuestionAnswerListTemp
									.size(); i++) {
								modifierQuestionsList
										.add(modifierQuestionAnswerListTemp
												.get(i).getQuestion());
							}

							HashSet hashSet = new HashSet();
							hashSet.addAll(modifierQuestionsList);
							modifierQuestionsList.clear();
							modifierQuestionsList.addAll(hashSet);

							modifierQuestionsList.removeAll(Arrays
									.asList(new Object[] { null, "null" }));

							for (int i = 0; i < modifierQuestionsList.size(); i++) {
								modifierAnswersList = new ArrayList<ModifierQuestionAnswer>();
								modifierQuestionAnswerListTemp = DatabaseManager
										.getInstance()
										.getModifierQuestionAnswerDao()
										.queryBuilder()
										.distinct()
										.where()
										.eq("question",
												modifierQuestionsList.get(i))
										.and()
										.eq("itemId", menuDetail.getItemId())
										.query();

								List<String> modifierAnswerDistinctList = new ArrayList<String>();
								for (int j = 0; j < modifierQuestionAnswerListTemp
										.size(); j++) {
									if (modifierAnswerDistinctList
											.contains(modifierQuestionAnswerListTemp
													.get(j).getAnswer()))
										continue;

									modifierAnswerDistinctList
											.add(modifierQuestionAnswerListTemp
													.get(j).getAnswer());
									modifierAnswersList
											.add(modifierQuestionAnswerListTemp
													.get(j));
								}

								modifierCollection.put(
										modifierQuestionsList.get(i),
										modifierAnswersList);
							}

							int count = modifierExpandableListAdapter
									.getGroupCount();
							for (int i = 0; i < count; i++)
								expandableListView.collapseGroup(i);

							modifierExpandableListAdapter.updateAdapter(
									IConstants.mContext, modifierQuestionsList,
									modifierCollection, "" + choosedModifier,
									menuDetail.getItemName(),
									modifierPriceValue, modifierPrice,
									modifierQuantity);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				if (i == 0) {
					button.setBackgroundResource(R.drawable.green_btn_gradient);

					choosedModifier = modifierCategoryList.get(i);

					modifierQuantity.setText("1");
				}

				hscrollbar.addView(button);
			}

			dialog.setView(modifierDialog);
			dialog.show();
			// Overriding the handler immediately after show is probably a
			// better
			// approach than OnShowListener as described below
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							try {

								JSONObject jsonObject = new JSONObject();
								JSONArray jsonArray = new JSONArray();

								List<ModifierQuestionAnswer> modifierQuestionAnswerListTemp;

								modifierQuestionAnswerListTemp = DatabaseManager
										.getInstance()
										.getModifierQuestionAnswerDao()
										.queryBuilder()
										.distinct()
										.where()
										.eq("modifierCategory", choosedModifier)
										.and()
										.eq("itemId", menuDetail.getItemId())
										.and().eq("checked", true).query();

								// System.out.println("modifierQuestionAnswerListTemp "
								// + modifierQuestionAnswerListTemp.size());

								String havingAnswer = "";

								for (int i = 0; i < modifierQuestionAnswerListTemp
										.size(); i++) {

									JSONObject j = new JSONObject();
									j.put("ItemName",
											modifierQuestionAnswerListTemp.get(
													i).getAddonId());
									j.put("price",
											modifierQuestionAnswerListTemp.get(
													i).getModifierPrice());
									jsonArray.put(j);

									if (i == 0) {
										havingAnswer += modifierQuestionAnswerListTemp
												.get(i).getAnswer() + ", ";
									} else if (i == modifierQuestionAnswerListTemp
											.size() - 1) {
										havingAnswer += modifierQuestionAnswerListTemp
												.get(i).getAnswer() + "";
									}

									// System.out.println("jsonArray.toString() "
									// + jsonArray.toString());
									// temoModifierPrice = temoModifierPrice
									// + (modifierQuestionAnswerListTemp.get(i)
									// .getModifierPrice() *
									// modifierQuantityTemp);
								}

								jsonObject.put("m", jsonArray);

								Integer quantity = Integer
										.parseInt(modifierQuantity.getText()
												.toString());
								Double totalPrice = Double
										.parseDouble(modifierPrice.getText()
												.toString());

								// QueryBuilder<OrderMenuDetails, Integer>
								// qBuilder = DatabaseManager
								// .getInstance().getOrderMenuDetailsDao()
								// .queryBuilder();
								// qBuilder.orderBy("sequenceNo", false); //
								// false
								// // for
								// // descending
								// // order
								// qBuilder.limit(1);
								// List<OrderMenuDetails>
								// orderMenuDetailsListOfOne = qBuilder
								// .query();

								OrderMenuDetails orderMenuDetail = new OrderMenuDetails();

								if (modifierQuestionAnswerListTemp.size() == 0) {
									List<OrderMenuDetails> orderMenuDetailsList = DatabaseManager
											.getInstance()
											.getOrderMenuDetailsDao()
											.queryBuilder()
											.distinct()
											.where()
											.eq("itemId",
													menuDetail.getItemId())
											.and()
											.eq("modifierId", choosedModifierId)
											.and().eq("editable", true).query();

									if (orderMenuDetailsList.size() > 0) {
										orderMenuDetail = orderMenuDetailsList
												.get(0);

										orderMenuDetail
												.setQuantity(orderMenuDetail
														.getQuantity()
														+ quantity);
										orderMenuDetail
												.setPrice(orderMenuDetail
														.getPrice()
														+ totalPrice);

										DatabaseManager.getInstance()
												.getOrderMenuDetailsDao()
												.update(orderMenuDetail);
									} else {
										orderMenuDetail.setItemId(Integer
												.parseInt(choosedModifierId));
										orderMenuDetail
												.setModifierId(choosedModifierId);
										orderMenuDetail.setModifierStatus("y");
										orderMenuDetail
												.setModifierCategory(choosedModifier);
										orderMenuDetail.setModifierDesc(""
												+ jsonObject.toString());
										orderMenuDetail.setItemName(menuDetail
												.getItemName());
										orderMenuDetail.setQuantity(quantity);
										orderMenuDetail.setPrice(totalPrice);
										orderMenuDetail.setItemPrice(menuDetail
												.getPrice());
										orderMenuDetail.setPrinterId(menuDetail
												.getPrinterId());
										orderMenuDetail.setChefNote("");
										orderMenuDetail.setAllergy("");

										orderMenuDetail.setCookTime(menuDetail
												.getPrepTime());
										orderMenuDetail.setCourse(menuDetail
												.getCourse());
										orderMenuDetail.setOrderType("");
										orderMenuDetail.setEditable(true);
										orderMenuDetail
												.setHavingAnswer(havingAnswer);

										// orderMenuDetail
										// .setSequenceNo(orderMenuDetailsListOfOne
										// .size() == 0 ? 1
										// : orderMenuDetailsListOfOne
										// .get(0).getSequenceNo() + 1);
										DatabaseManager.getInstance()
												.getOrderMenuDetailsDao()
												.create(orderMenuDetail);
									}

								} else {
									orderMenuDetail.setItemId(Integer
											.parseInt(choosedModifierId));
									orderMenuDetail.setModifierStatus("y");
									orderMenuDetail
											.setModifierCategory(choosedModifier);
									orderMenuDetail.setModifierDesc(""
											+ jsonObject.toString());
									orderMenuDetail.setItemName(menuDetail
											.getItemName());
									orderMenuDetail.setQuantity(quantity);
									orderMenuDetail.setPrice(totalPrice);
									orderMenuDetail.setItemPrice(menuDetail
											.getPrice());
									orderMenuDetail.setPrinterId(menuDetail
											.getPrinterId());
									orderMenuDetail.setChefNote("");
									orderMenuDetail.setAllergy("");

									orderMenuDetail.setCookTime(menuDetail
											.getPrepTime());
									orderMenuDetail.setCourse(menuDetail
											.getCourse());
									orderMenuDetail.setOrderType("");
									orderMenuDetail.setEditable(true);
									orderMenuDetail
											.setHavingAnswer(havingAnswer);

									// orderMenuDetail
									// .setSequenceNo(orderMenuDetailsListOfOne
									// .size() == 0 ? 1
									// : orderMenuDetailsListOfOne
									// .get(0).getSequenceNo() + 1);
									DatabaseManager.getInstance()
											.getOrderMenuDetailsDao()
											.create(orderMenuDetail);
								}

								ExpandableMenuActivity.updateADapter();
							} catch (Exception e) {
								e.printStackTrace();
							}

							// boolean hasNotQA = true;
							//
							// Iterator myVeryOwnIterator =
							// HomeActivity.modifierAddedItems
							// .keySet().iterator();
							// while (myVeryOwnIterator.hasNext()) {
							// String key = (String) myVeryOwnIterator.next();
							// // Boolean check = (Boolean)
							// // HomeActivity.modifierAddedItems
							// // .get(key).getChecked();
							// if (HomeActivity.modifierAddedItems.get(key)
							// .getChecked()) {
							//
							// hasNotQA = false;
							//
							// ModifierQuestionAnswer modifierQuestionAnswer =
							// HomeActivity.modifierAddedItems
							// .get(key);
							//
							// modifierQuestionAnswer
							// .setModifierCategory(choosedModifier);
							// modifierQuestionAnswer
							// .setItemName(menuDetail
							// .getItemName());
							//
							// modifierQuestionAnswer
							// .setQuantity(quantity);
							// modifierQuestionAnswer
							// .setTotalPrice(totalPrice);
							// modifierQuestionAnswer
							// .setPrice(modifierPriceValue);
							//
							// HomeActivity.modifierAddedItemsStack.put(
							// key, modifierQuestionAnswer);
							//
							// System.err
							// .println("HomeActivity.modifierAddedItemsStack.size() "
							// + HomeActivity.modifierAddedItemsStack
							// .size());
							// System.err
							// .println("Add modifierAddedItemsStack put "
							// + modifierQuestionAnswer
							// .getAnswer());
							// System.err
							// .println("Add modifierAddedItemsStack key "
							// + key);
							// System.err
							// .println("Add modifierAddedItemsStack getModifierId "
							// + modifierQuestionAnswer
							// .getModifierId());
							// }
							// }
							//
							// if (hasNotQA) {
							// System.err.println("hasNotQA " + hasNotQA);
							// ModifierQuestionAnswer modifierQuestionAnswer =
							// new ModifierQuestionAnswer();
							//
							// modifierQuestionAnswer
							// .setModifierCategory(choosedModifier);
							// modifierQuestionAnswer.setItemName(menuDetail
							// .getItemName());
							// modifierQuestionAnswer.setQuantity(quantity);
							// modifierQuestionAnswer
							// .setTotalPrice(totalPrice);
							// modifierQuestionAnswer
							// .setPrice(modifierPriceValue);
							//
							// String key = menuDetail.getItemName() + "_"
							// + choosedModifier;
							//
							// HomeActivity.modifierAddedItemsStack.put(key,
							// modifierQuestionAnswer);
							//
							// System.err
							// .println("Add modifierAddedItemsStack getModifierId "
							// + modifierQuestionAnswer
							// .getModifierId());
							// }
							//
							// System.err.println("Add modifierAddedItemsStack");
							// // ---------------------------------------------
							// for (Entry<String, ModifierQuestionAnswer> entry
							// : HomeActivity.modifierAddedItemsStack
							// .entrySet()) {
							// String key = entry.getKey();
							// ModifierQuestionAnswer modifierQuestionAnswer =
							// entry
							// .getValue();
							// System.out.println("Key = " + key);
							// System.out.println("Values = "
							// + modifierQuestionAnswer.getAnswer()
							// + "n");
							// }
							// // ---------------------------------------------

							dialog.dismiss();
						}
					});

			dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void modifier(final MenuDetails menuDetail) {

		try {

			// System.out.println("modifier itemId " + itemId);

			modifierPriceValue = 0.0;

			// final MenuDetails menuDetail = (DatabaseManager.getInstance()
			// .getMenuDetailsDao().queryBuilder().distinct().where()
			// .in("itemId", itemId).query()).get(0);

			List<String> modifierCategoryList = new ArrayList<String>();
			List<String> modifierQuestionsList = new ArrayList<String>();
			List<ModifierQuestionAnswer> modifierAnswersList = new ArrayList<ModifierQuestionAnswer>();

			final HashMap<String, ModifierQuestionAnswer> modifierQuestionAnswerHashMap = new HashMap<String, ModifierQuestionAnswer>();
			Map<String, List<ModifierQuestionAnswer>> modifierCollection = new LinkedHashMap<String, List<ModifierQuestionAnswer>>();

			final List<ModifierQuestionAnswer> modifierQuestionAnswerList = DatabaseManager
					.getInstance().getModifierQuestionAnswerDao()
					.queryBuilder().distinct().where()
					.in("itemId", menuDetail.getItemId()).query();

			for (ModifierQuestionAnswer modifierQuestionAnswer : modifierQuestionAnswerList) {
				modifierCategoryList.add(modifierQuestionAnswer
						.getModifierCategory());
				// modifierQuestionsList.add(modifierQuestionAnswer.getQuestion());
				// modifierAnswersList.add(modifierQuestionAnswer.getAnswer());
				modifierQuestionAnswerHashMap.put(
						modifierQuestionAnswer.getModifierCategory(),
						modifierQuestionAnswer);
			}

			HashSet hashSet = new HashSet();
			hashSet.addAll(modifierCategoryList);
			modifierCategoryList.clear();
			modifierCategoryList.addAll(hashSet);

			modifierCategoryList.removeAll(Arrays.asList(new Object[] { null,
					"null" }));

			List<ModifierQuestionAnswer> modifierQuestionAnswerListTemp = DatabaseManager
					.getInstance().getModifierQuestionAnswerDao()
					.queryBuilder().distinct().where()
					.in("modifierCategory", modifierCategoryList.get(0))
					.query();

			for (int i = 0; i < modifierQuestionAnswerListTemp.size(); i++) {
				modifierQuestionsList.add(modifierQuestionAnswerListTemp.get(i)
						.getQuestion());
			}

			hashSet = new HashSet();
			hashSet.addAll(modifierQuestionsList);
			modifierQuestionsList.clear();
			modifierQuestionsList.addAll(hashSet);

			modifierQuestionsList.removeAll(Arrays.asList(new Object[] { null,
					"null" }));

			for (int i = 0; i < modifierQuestionsList.size(); i++) {
				modifierAnswersList = new ArrayList<ModifierQuestionAnswer>();
				modifierQuestionAnswerListTemp = DatabaseManager.getInstance()
						.getModifierQuestionAnswerDao().queryBuilder()
						.distinct().where()
						.in("question", modifierQuestionsList.get(i)).query();

				List<String> modifierAnswerDistinctList = new ArrayList<String>();
				for (int j = 0; j < modifierQuestionAnswerListTemp.size(); j++) {
					if (modifierAnswerDistinctList
							.contains(modifierQuestionAnswerListTemp.get(j)
									.getAnswer()))
						continue;

					modifierAnswerDistinctList
							.add(modifierQuestionAnswerListTemp.get(j)
									.getAnswer());
					modifierAnswersList.add(modifierQuestionAnswerListTemp
							.get(j));

					// System.out
					// .println("modifierQuestionAnswerListTemp.get(j).getAnswer() "
					// + modifierQuestionAnswerListTemp.get(j)
					// .getAnswer());
				}

				// hashSet = new HashSet();
				// hashSet.addAll(modifierAnswersList);
				// modifierAnswersList.clear();
				// modifierAnswersList.addAll(hashSet);
				//
				// modifierAnswersList.removeAll(Arrays.asList(new Object[] {
				// null,
				// "null" }));

				modifierCollection.put(modifierQuestionsList.get(i),
						modifierAnswersList);
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(
					IConstants.mContext);
			builder.setCancelable(false);

			builder.setPositiveButton("Add", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			// builder.setNeutralButton("No thanks", new OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// }
			// });
			builder.setNegativeButton("Cancel", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});

			final AlertDialog dialog = builder.create();
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LayoutInflater inflater = IConstants.mContext.getLayoutInflater();
			final View modifierDialog = inflater.inflate(R.layout.modifier,
					null);

			TextView modifierTitle = (TextView) modifierDialog
					.findViewById(R.id.modifierTitle);
			modifierTitle.setText(menuDetail.getItemName());

			final TextView modifierQuantity = (TextView) modifierDialog
					.findViewById(R.id.modifierQuantity);
			modifierQuantity.setText("1");

			modifierPriceValue = modifierQuestionAnswerHashMap.get(
					modifierCategoryList.get(0)).getPrice();

			final TextView modifierPrice = (TextView) modifierDialog
					.findViewById(R.id.modifierPrice);
			modifierPrice.setText("" + modifierPriceValue);

			final RelativeLayout modifierAdd = (RelativeLayout) modifierDialog
					.findViewById(R.id.layoutAdd);
			final RelativeLayout modifierRemove = (RelativeLayout) modifierDialog
					.findViewById(R.id.layoutRemove);
			final LinearLayout modifierChefnotes = (LinearLayout) modifierDialog
					.findViewById(R.id.layoutChefnotes);

			modifierAdd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Integer integer = Integer.parseInt(modifierQuantity
							.getText().toString());
					integer++;
					modifierQuantity.setText("" + integer);
					modifierPrice.setText("" + (modifierPriceValue * integer));
				}
			});

			modifierRemove.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Integer integer = Integer.parseInt(modifierQuantity
							.getText().toString());
					if (integer > 1) {
						integer--;
						modifierQuantity.setText("" + integer);
						modifierPrice.setText(""
								+ (modifierPriceValue * integer));
					}
				}
			});

			modifierChefnotes.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			});

			final LinearLayout hscrollbar = (LinearLayout) modifierDialog
					.findViewById(R.id.hscrollbar);

			final ExpandableListView expandableListView = (ExpandableListView) modifierDialog
					.findViewById(R.id.laptop_list);

			final ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(
					IConstants.mContext, modifierQuestionsList,
					modifierCollection, modifierCategoryList.get(0),
					menuDetail.getItemName(), modifierPrice, modifierQuantity);
			expandableListView.setAdapter(expandableListAdapter);

			choosedModifier = "";

			int len = modifierCategoryList.size();
			for (int i = 0; i < len; i++) {
				Button button = new Button(IConstants.mContext);
				button.setText("" + modifierCategoryList.get(i));
				button.setTextColor(Color.WHITE);
				button.setBackgroundColor(Color.parseColor("#99000000"));
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				layoutParams.setMargins(1, 1, 1, 1);
				if (len < 5)
					layoutParams.weight = 1;
				button.setLayoutParams(layoutParams);
				button.setGravity(Gravity.CENTER);
				button.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						choosedModifier = ((TextView) v).getText().toString();

						System.out.println("onClick " + choosedModifier);

						modifierQuantity.setText("1");

						// // ---------------------------------------------
						//
						// Iterator myVeryOwnIterator =
						// HomeActivity.modifierAddedItemsStack
						// .keySet().iterator();
						// while (myVeryOwnIterator.hasNext()) {
						// String key = (String) myVeryOwnIterator.next();
						//
						// System.out.println("choosedModifier");
						// System.out.println("key " + key);
						// System.out.println("choosedModifier "
						// + choosedModifier);
						//
						// if (key.contains(choosedModifier)) {
						// ModifierQuestionAnswer modifierQuestionAnswer =
						// HomeActivity.modifierAddedItemsStack
						// .get(key);
						//
						//
						//
						// modifierPrice.setText(""
						// + modifierQuestionAnswer
						// .getTotalPrice());
						//
						// modifierQuantity.setText(""
						// + modifierQuestionAnswer.getQuantity());
						//
						// System.out.println(""
						// + modifierQuestionAnswer
						// .getTotalPrice());
						//
						// System.out.println(""
						// + modifierQuestionAnswer.getQuantity());
						//
						// break;
						// }
						// }
						//
						// // ---------------------------------------------

						HomeActivity.modifierAddedItems = new HashMap<String, ModifierQuestionAnswer>();

						for (int j = 0; j < hscrollbar.getChildCount(); j++) {
							hscrollbar.getChildAt(j).setBackgroundColor(
									Color.parseColor("#99000000"));
						}
						v.setBackgroundResource(R.drawable.green_btn_gradient);
						System.out.println(choosedModifier);

						modifierPriceValue = modifierQuestionAnswerHashMap.get(
								choosedModifier).getPrice();

						Integer integer = Integer.parseInt(modifierQuantity
								.getText().toString());
						modifierPrice.setText(""
								+ (modifierPriceValue * integer));

						try {
							List<String> modifierQuestionsList = new ArrayList<String>();
							List<ModifierQuestionAnswer> modifierAnswersList = new ArrayList<ModifierQuestionAnswer>();
							Map<String, List<ModifierQuestionAnswer>> modifierCollection = new LinkedHashMap<String, List<ModifierQuestionAnswer>>();

							List<ModifierQuestionAnswer> modifierQuestionAnswerListTemp;
							modifierQuestionAnswerListTemp = DatabaseManager
									.getInstance()
									.getModifierQuestionAnswerDao()
									.queryBuilder().distinct().where()
									.in("modifierCategory", choosedModifier)
									.query();

							for (int i = 0; i < modifierQuestionAnswerListTemp
									.size(); i++) {
								modifierQuestionsList
										.add(modifierQuestionAnswerListTemp
												.get(i).getQuestion());
							}

							HashSet hashSet = new HashSet();
							hashSet.addAll(modifierQuestionsList);
							modifierQuestionsList.clear();
							modifierQuestionsList.addAll(hashSet);

							modifierQuestionsList.removeAll(Arrays
									.asList(new Object[] { null, "null" }));

							for (int i = 0; i < modifierQuestionsList.size(); i++) {
								modifierAnswersList = new ArrayList<ModifierQuestionAnswer>();
								modifierQuestionAnswerListTemp = DatabaseManager
										.getInstance()
										.getModifierQuestionAnswerDao()
										.queryBuilder()
										.distinct()
										.where()
										.in("question",
												modifierQuestionsList.get(i))
										.query();

								List<String> modifierAnswerDistinctList = new ArrayList<String>();
								for (int j = 0; j < modifierQuestionAnswerListTemp
										.size(); j++) {
									if (modifierAnswerDistinctList
											.contains(modifierQuestionAnswerListTemp
													.get(j).getAnswer()))
										continue;

									modifierAnswerDistinctList
											.add(modifierQuestionAnswerListTemp
													.get(j).getAnswer());
									modifierAnswersList
											.add(modifierQuestionAnswerListTemp
													.get(j));

									// System.out
									// .println("modifierQuestionAnswerListTemp.get(j).getAnswer() "
									// + modifierQuestionAnswerListTemp
									// .get(j).getAnswer());
									// System.out
									// .println("modifierQuestionAnswerListTemp.get(j).getModifierPrice() "
									// + modifierQuestionAnswerListTemp
									// .get(j)
									// .getModifierPrice());
								}

								// hashSet = new HashSet();
								// hashSet.addAll(modifierAnswersList);
								// modifierAnswersList.clear();
								// modifierAnswersList.addAll(hashSet);
								//
								// modifierAnswersList.removeAll(Arrays.asList(new
								// Object[] {
								// null,
								// "null" }));

								modifierCollection.put(
										modifierQuestionsList.get(i),
										modifierAnswersList);
							}

							int count = expandableListAdapter.getGroupCount();
							for (int i = 0; i < count; i++)
								expandableListView.collapseGroup(i);

							expandableListAdapter.updateAdapter(
									IConstants.mContext, modifierQuestionsList,
									modifierCollection, "" + choosedModifier,
									menuDetail.getItemName(), modifierPrice,
									modifierQuantity);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				if (i == 0) {
					button.setBackgroundResource(R.drawable.green_btn_gradient);

					choosedModifier = modifierCategoryList.get(i);

					modifierQuantity.setText("1");

					// // ---------------------------------------------
					//
					// Iterator myVeryOwnIterator =
					// HomeActivity.modifierAddedItemsStack
					// .keySet().iterator();
					// while (myVeryOwnIterator.hasNext()) {
					// String key = (String) myVeryOwnIterator.next();
					//
					// System.out.println("choosedModifier");
					// System.out.println("key " + key);
					// System.out
					// .println("choosedModifier " + choosedModifier);
					//
					// if (key.contains(choosedModifier)) {
					// ModifierQuestionAnswer modifierQuestionAnswer =
					// HomeActivity.modifierAddedItemsStack
					// .get(key);
					//
					// modifierPrice.setText(""
					// + modifierQuestionAnswer.getTotalPrice());
					// modifierQuantity.setText(""
					// + modifierQuestionAnswer.getQuantity());
					//
					// System.out.println(""
					// + modifierQuestionAnswer.getTotalPrice());
					//
					// System.out.println(""
					// + modifierQuestionAnswer.getQuantity());
					//
					// break;
					// }
					// }
					//
					// // ---------------------------------------------
				}

				hscrollbar.addView(button);
			}

			dialog.setView(modifierDialog);
			dialog.show();
			// Overriding the handler immediately after show is probably a
			// better
			// approach than OnShowListener as described below
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							Integer quantity = Integer
									.parseInt(modifierQuantity.getText()
											.toString());
							Double totalPrice = Double
									.parseDouble(modifierPrice.getText()
											.toString());

							// if (quantity > 0) {
							// integer--;
							// modifierQuantity.setText("" + integer);
							// modifierPrice.setText(""
							// + (modifierPriceValue * integer));
							// }

							boolean hasNotQA = true;

							Iterator myVeryOwnIterator = HomeActivity.modifierAddedItems
									.keySet().iterator();
							while (myVeryOwnIterator.hasNext()) {
								String key = (String) myVeryOwnIterator.next();
								// Boolean check = (Boolean)
								// HomeActivity.modifierAddedItems
								// .get(key).getChecked();
								if (HomeActivity.modifierAddedItems.get(key)
										.getChecked()) {

									hasNotQA = false;

									ModifierQuestionAnswer modifierQuestionAnswer = HomeActivity.modifierAddedItems
											.get(key);

									// System.out.println(modifierQuestionAnswer
									// .getModifierCategory());
									// System.out.println(modifierQuestionAnswer
									// .getAnswer()
									// + " - "
									// + modifierQuestionAnswer
									// .getModifierPrice());

									modifierQuestionAnswer
											.setModifierCategory(choosedModifier);
									modifierQuestionAnswer
											.setItemName(menuDetail
													.getItemName());

									modifierQuestionAnswer
											.setQuantity(quantity);
									modifierQuestionAnswer
											.setTotalPrice(totalPrice);
									modifierQuestionAnswer
											.setPrice(modifierPriceValue);

									HomeActivity.modifierAddedItemsStack.put(
											key, modifierQuestionAnswer);

									System.err
											.println("HomeActivity.modifierAddedItemsStack.size() "
													+ HomeActivity.modifierAddedItemsStack
															.size());
									System.err
											.println("Add modifierAddedItemsStack put "
													+ modifierQuestionAnswer
															.getAnswer());
									System.err
											.println("Add modifierAddedItemsStack key "
													+ key);
									System.err
											.println("Add modifierAddedItemsStack getModifierId "
													+ modifierQuestionAnswer
															.getModifierId());
								}
							}

							if (hasNotQA) {
								System.err.println("hasNotQA " + hasNotQA);
								ModifierQuestionAnswer modifierQuestionAnswer = new ModifierQuestionAnswer();

								modifierQuestionAnswer
										.setModifierCategory(choosedModifier);
								modifierQuestionAnswer.setItemName(menuDetail
										.getItemName());
								modifierQuestionAnswer.setQuantity(quantity);
								modifierQuestionAnswer
										.setTotalPrice(totalPrice);
								modifierQuestionAnswer
										.setPrice(modifierPriceValue);

								String key = menuDetail.getItemName() + "_"
										+ choosedModifier;

								HomeActivity.modifierAddedItemsStack.put(key,
										modifierQuestionAnswer);

								System.err
										.println("Add modifierAddedItemsStack getModifierId "
												+ modifierQuestionAnswer
														.getModifierId());
							}

							System.err.println("Add modifierAddedItemsStack");
							// ---------------------------------------------
							for (Entry<String, ModifierQuestionAnswer> entry : HomeActivity.modifierAddedItemsStack
									.entrySet()) {
								String key = entry.getKey();
								ModifierQuestionAnswer modifierQuestionAnswer = entry
										.getValue();
								System.out.println("Key = " + key);
								System.out.println("Values = "
										+ modifierQuestionAnswer.getAnswer()
										+ "n");
							}
							// ---------------------------------------------

							// // ---------------------------------------------
							//
							// ModifierQuestionAnswer modifierQuestionAnswer =
							// new ModifierQuestionAnswer();
							//
							// myVeryOwnIterator =
							// HomeActivity.modifierAddedItemsStack
							// .keySet().iterator();
							// while (myVeryOwnIterator.hasNext()) {
							// String key = (String) myVeryOwnIterator.next();
							//
							// System.out.println("modifierAddedItemsStack");
							// System.out.println("key " + key);
							// System.out.println("choosedModifier "
							// + choosedModifier);
							//
							// if (key.contains(choosedModifier)) {
							// modifierQuestionAnswer =
							// HomeActivity.modifierAddedItemsStack
							// .get(key);
							//
							// System.out.println(""
							// + modifierQuestionAnswer
							// .getTotalPrice());
							//
							// System.out.println(""
							// + modifierQuestionAnswer
							// .getQuantity());
							//
							// break;
							// }
							// }
							//
							// // ---------------------------------------------

							// }

							dialog.dismiss();
						}
					});

			// dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(
			// new View.OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// dialog.dismiss();
			// }
			// });
			dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void modifierEdit(Integer itemId,
			ModifierQuestionAnswer modifierQuestionAnswerEdit) {

		try {

			modifierPriceValue = 0.0;

			final MenuDetails menuDetail = (DatabaseManager.getInstance()
					.getMenuDetailsDao().queryBuilder().distinct().where()
					.in("itemId", itemId).query()).get(0);

			// List<String> modifierCategoryList = new ArrayList<String>();
			List<String> modifierQuestionsList = new ArrayList<String>();
			List<ModifierQuestionAnswer> modifierAnswersList = new ArrayList<ModifierQuestionAnswer>();

			// final HashMap<String, ModifierQuestionAnswer>
			// modifierQuestionAnswerHashMap = new HashMap<String,
			// ModifierQuestionAnswer>();
			Map<String, List<ModifierQuestionAnswer>> modifierCollection = new LinkedHashMap<String, List<ModifierQuestionAnswer>>();

			// final List<ModifierQuestionAnswer> modifierQuestionAnswerList =
			// DatabaseManager
			// .getInstance().getModifierQuestionAnswerDao()
			// .queryBuilder().distinct().where().in("itemId", itemId)
			// .query();

			List<ModifierQuestionAnswer> modifierQuestionAnswerListTemp = DatabaseManager
					.getInstance()
					.getModifierQuestionAnswerDao()
					.queryBuilder()
					.distinct()
					.where()
					.in("modifierCategory",
							modifierQuestionAnswerEdit.getModifierCategory())
					.query();

			for (int i = 0; i < modifierQuestionAnswerListTemp.size(); i++) {
				modifierQuestionsList.add(modifierQuestionAnswerListTemp.get(i)
						.getQuestion());
			}

			HashSet hashSet = new HashSet();
			hashSet.addAll(modifierQuestionsList);
			modifierQuestionsList.clear();
			modifierQuestionsList.addAll(hashSet);

			modifierQuestionsList.removeAll(Arrays.asList(new Object[] { null,
					"null" }));

			for (int i = 0; i < modifierQuestionsList.size(); i++) {
				modifierAnswersList = new ArrayList<ModifierQuestionAnswer>();
				modifierQuestionAnswerListTemp = DatabaseManager.getInstance()
						.getModifierQuestionAnswerDao().queryBuilder()
						.distinct().where()
						.in("question", modifierQuestionsList.get(i)).query();

				List<String> modifierAnswerDistinctList = new ArrayList<String>();
				for (int j = 0; j < modifierQuestionAnswerListTemp.size(); j++) {
					if (modifierAnswerDistinctList
							.contains(modifierQuestionAnswerListTemp.get(j)
									.getAnswer()))
						continue;

					modifierAnswerDistinctList
							.add(modifierQuestionAnswerListTemp.get(j)
									.getAnswer());
					modifierAnswersList.add(modifierQuestionAnswerListTemp
							.get(j));

				}

				modifierCollection.put(modifierQuestionsList.get(i),
						modifierAnswersList);
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(
					IConstants.mContext);
			builder.setCancelable(false);

			builder.setPositiveButton("Edit", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			builder.setNegativeButton("Cancel", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});

			final AlertDialog dialog = builder.create();
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LayoutInflater inflater = IConstants.mContext.getLayoutInflater();
			final View modifierDialog = inflater.inflate(R.layout.modifier,
					null);

			TextView modifierTitle = (TextView) modifierDialog
					.findViewById(R.id.modifierTitle);
			modifierTitle.setText(menuDetail.getItemName());

			final TextView modifierQuantity = (TextView) modifierDialog
					.findViewById(R.id.modifierQuantity);
			modifierQuantity.setText(""
					+ modifierQuestionAnswerEdit.getQuantity());

			modifierPriceValue = modifierQuestionAnswerEdit.getPrice();

			final TextView modifierPrice = (TextView) modifierDialog
					.findViewById(R.id.modifierPrice);
			modifierPrice.setText(""
					+ modifierQuestionAnswerEdit.getTotalPrice());

			final RelativeLayout modifierAdd = (RelativeLayout) modifierDialog
					.findViewById(R.id.layoutAdd);
			final RelativeLayout modifierRemove = (RelativeLayout) modifierDialog
					.findViewById(R.id.layoutRemove);
			final LinearLayout modifierChefnotes = (LinearLayout) modifierDialog
					.findViewById(R.id.layoutChefnotes);

			modifierAdd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Integer integer = Integer.parseInt(modifierQuantity
							.getText().toString());
					integer++;
					modifierQuantity.setText("" + integer);
					modifierPrice.setText("" + (modifierPriceValue * integer));
				}
			});

			modifierRemove.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Integer integer = Integer.parseInt(modifierQuantity
							.getText().toString());
					if (integer > 1) {
						integer--;
						modifierQuantity.setText("" + integer);
						modifierPrice.setText(""
								+ (modifierPriceValue * integer));
					}
				}
			});

			modifierChefnotes.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			});

			final LinearLayout hscrollbar = (LinearLayout) modifierDialog
					.findViewById(R.id.hscrollbar);

			choosedModifier = "";

			Button button = new Button(IConstants.mContext);
			button.setText(""
					+ modifierQuestionAnswerEdit.getModifierCategory());
			button.setTextColor(Color.WHITE);
			button.setBackgroundResource(R.drawable.green_btn_gradient);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			layoutParams.setMargins(1, 1, 1, 1);
			layoutParams.weight = 1;
			button.setLayoutParams(layoutParams);
			button.setGravity(Gravity.CENTER);

			choosedModifier = modifierQuestionAnswerEdit.getModifierCategory();

			HomeActivity.modifierAddedItems = new HashMap<String, ModifierQuestionAnswer>();

			// ---------------------------------------------
			for (Entry<String, ModifierQuestionAnswer> entry : HomeActivity.modifierAddedItemsStack
					.entrySet()) {
				String key = entry.getKey();
				ModifierQuestionAnswer modifierQuestionAnswer = entry
						.getValue();
				// System.out.println("Key = " + key);
				// System.out.println("Values = " +
				// modifierQuestionAnswer.getAnswer() + "n");
				if (key.contains(menuDetail.getItemName() + "_"
						+ choosedModifier))
					HomeActivity.modifierAddedItems.put(key,
							modifierQuestionAnswer);
			}
			// ---------------------------------------------

			button.setBackgroundResource(R.drawable.green_btn_gradient);

			hscrollbar.addView(button);

			final ExpandableListView expandableListView = (ExpandableListView) modifierDialog
					.findViewById(R.id.laptop_list);

			final ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(
					IConstants.mContext, modifierQuestionsList,
					modifierCollection,
					modifierQuestionAnswerEdit.getModifierCategory(),
					menuDetail.getItemName(), modifierPrice, modifierQuantity);
			expandableListView.setAdapter(expandableListAdapter);

			dialog.setView(modifierDialog);
			dialog.show();
			// Overriding the handler immediately after show is probably a
			// better
			// approach than OnShowListener as described below
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							Integer quantity = Integer
									.parseInt(modifierQuantity.getText()
											.toString());
							Double totalPrice = Double
									.parseDouble(modifierPrice.getText()
											.toString());

							boolean hasNotQA = true;

							Iterator myVeryOwnIterator = HomeActivity.modifierAddedItems
									.keySet().iterator();
							while (myVeryOwnIterator.hasNext()) {
								String key = (String) myVeryOwnIterator.next();
								if (HomeActivity.modifierAddedItems.get(key)
										.getChecked()) {

									hasNotQA = false;

									ModifierQuestionAnswer modifierQuestionAnswer = HomeActivity.modifierAddedItems
											.get(key);

									modifierQuestionAnswer
											.setModifierCategory(choosedModifier);
									modifierQuestionAnswer
											.setItemName(menuDetail
													.getItemName());
									modifierQuestionAnswer
											.setQuantity(quantity);
									modifierQuestionAnswer
											.setTotalPrice(totalPrice);
									modifierQuestionAnswer
											.setPrice(modifierPriceValue);

									HomeActivity.modifierAddedItemsStack.put(
											key, modifierQuestionAnswer);

									System.err
											.println("HomeActivity.modifierAddedItemsStack.size() "
													+ HomeActivity.modifierAddedItemsStack
															.size());
									System.err
											.println("Edit modifierAddedItemsStack put "
													+ modifierQuestionAnswer
															.getAnswer());
									System.err
											.println("Edit modifierAddedItemsStack key "
													+ key);
								}
							}

							if (hasNotQA) {
								ModifierQuestionAnswer modifierQuestionAnswer = new ModifierQuestionAnswer();

								modifierQuestionAnswer
										.setModifierCategory(choosedModifier);
								modifierQuestionAnswer.setItemName(menuDetail
										.getItemName());
								modifierQuestionAnswer.setQuantity(quantity);
								modifierQuestionAnswer
										.setTotalPrice(totalPrice);
								modifierQuestionAnswer
										.setPrice(modifierPriceValue);

								String key = menuDetail.getItemName() + "_"
										+ choosedModifier;

								HomeActivity.modifierAddedItemsStack.put(key,
										modifierQuestionAnswer);
							}

							System.err.println("Edit modifierAddedItemsStack");
							// ---------------------------------------------
							for (Entry<String, ModifierQuestionAnswer> entry : HomeActivity.modifierAddedItemsStack
									.entrySet()) {
								String key = entry.getKey();
								ModifierQuestionAnswer modifierQuestionAnswer = entry
										.getValue();
								System.out.println("Key = " + key);
								System.out.println("Values = "
										+ modifierQuestionAnswer.getAnswer()
										+ "n");
							}
							// ---------------------------------------------

							// // ---------------------------------------------
							//
							// ModifierQuestionAnswer modifierQuestionAnswer =
							// new ModifierQuestionAnswer();
							//
							// myVeryOwnIterator =
							// HomeActivity.modifierAddedItemsStack
							// .keySet().iterator();
							// while (myVeryOwnIterator.hasNext()) {
							// String key = (String) myVeryOwnIterator.next();
							//
							// System.out.println("modifierAddedItemsStack");
							// System.out.println("key " + key);
							// System.out.println("choosedModifier "
							// + choosedModifier);
							//
							// if (key.contains(choosedModifier)) {
							// modifierQuestionAnswer =
							// HomeActivity.modifierAddedItemsStack
							// .get(key);
							//
							// System.out.println(""
							// + modifierQuestionAnswer
							// .getTotalPrice());
							//
							// System.out.println(""
							// + modifierQuestionAnswer
							// .getQuantity());
							//
							// break;
							// }
							// }
							//
							// // ---------------------------------------------
							//
							// // }

							dialog.dismiss();
						}
					});

			dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void paybill(final Activity activity,
			final SalesController salesController) {

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(false).setTitle("Pay Now");

		builder.setPositiveButton("Pay", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		final AlertDialog dialog = builder.create();
		LayoutInflater inflater = activity.getLayoutInflater();
		final View payBillDialog = inflater.inflate(R.layout.paybill_dialog,
				null);

		TableLayout payBillTable = (TableLayout) payBillDialog
				.findViewById(R.id.payBillTable);

		final TextView billAmount = (TextView) payBillDialog
				.findViewById(R.id.billAmount);
		final TextView totalAmount = (TextView) payBillDialog
				.findViewById(R.id.totalAmount);
		final TextView tipAmount = (TextView) payBillDialog
				.findViewById(R.id.getTipAmount);

		price = taxAmount = 0.0;

		payType = "CS";
		payDesc = "";

		final SalesDetails salesDetails = new SalesDetails();

		List<LkupPayTypeDetails> lkupPayTypeDetails = new ArrayList<LkupPayTypeDetails>();

		try {
			// find out how many orders account-id #10 has
			GenericRawResults<String[]> rawResults = DatabaseManager
					.getInstance().getOrderMenuDetailsDao()
					.queryRaw("select sum(price) from ordermenudetails");
			// there should be 1 result
			List<String[]> results = rawResults.getResults();
			// the results array should have 1 value
			String[] resultArray = results.get(0);
			// this should print the number of orders that have this account-id
			System.out.println("Account-id 10 has " + resultArray[0]
					+ " orders");
			price = Double.parseDouble(resultArray[0]);
			billAmount.setText("" + round(price));
			totalAmount.setText("" + round(price));

			lkupPayTypeDetails = DatabaseManager.getInstance()
					.getLkupPayTypeDetailsDao().queryForAll();

			HashMap<String, String> taxDesc = new HashMap<String, String>();
			List<LkupTaxListDetails> lkupTaxListDetails = DatabaseManager
					.getInstance().getLkupTaxListDetailsDao().queryForAll();

			for (LkupTaxListDetails lkupTaxListDetail : lkupTaxListDetails) {
				taxDesc.put(lkupTaxListDetail.getTaxCd(),
						lkupTaxListDetail.getTaxDesc());
			}

			List<LkupRestTaxDetails> lkupRestTaxDetails = DatabaseManager
					.getInstance().getLkupRestTaxDetailsDao().queryForAll();

			for (int i = 0; i < lkupRestTaxDetails.size(); i++) {
				TableRow row = new TableRow(IConstants.mContext);
				TableRow.LayoutParams lp = new TableRow.LayoutParams(
						TableRow.LayoutParams.WRAP_CONTENT);
				row.setLayoutParams(lp);

				TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
						TableRow.LayoutParams.WRAP_CONTENT,
						TableRow.LayoutParams.WRAP_CONTENT);
				TextView textView1 = new TextView(IConstants.mContext);
				textView1.setPadding(10, 10, 0, 10);
				layoutParams.weight = 1;
				textView1.setLayoutParams(layoutParams);

				textView1.setText(taxDesc.get(lkupRestTaxDetails.get(i)
						.getTaxCd())
						+ " ("
						+ lkupRestTaxDetails.get(i).getTaxPer() + "%)");

				textView1.setTypeface(Typeface.DEFAULT_BOLD);

				layoutParams = new TableRow.LayoutParams(
						TableRow.LayoutParams.WRAP_CONTENT,
						TableRow.LayoutParams.WRAP_CONTENT);
				TextView textView2 = new TextView(IConstants.mContext);
				textView2.setPadding(0, 10, 10, 10);
				layoutParams.gravity = Gravity.RIGHT;
				textView2.setLayoutParams(layoutParams);

				taxAmount += (Double) (price / 100 * lkupRestTaxDetails.get(i)
						.getTaxPer());

				textView2.setText(""
						+ round((Double) (price / 100 * lkupRestTaxDetails.get(
								i).getTaxPer())));
				// textView2.setCompoundDrawablesWithIntrinsicBounds(
				// R.drawable.rupees_48_48, 0, 0, 0);

				if (taxDesc.get(lkupRestTaxDetails.get(i).getTaxCd())
						.equalsIgnoreCase("Service Charge")) {
					salesDetails
							.setServiceCharge((Double) (price / 100 * lkupRestTaxDetails
									.get(i).getTaxPer()));
					salesDetails.setServiceChargePercentage(""
							+ lkupRestTaxDetails.get(i).getTaxPer());
				} else if (taxDesc.get(lkupRestTaxDetails.get(i).getTaxCd())
						.equalsIgnoreCase("Service Tax")) {
					salesDetails
							.setServiceTax((Double) (price / 100 * lkupRestTaxDetails
									.get(i).getTaxPer()));
					salesDetails.setServiceTaxPercentage(""
							+ lkupRestTaxDetails.get(i).getTaxPer());
				} else if (taxDesc.get(lkupRestTaxDetails.get(i).getTaxCd())
						.equalsIgnoreCase("VAT")) {
					salesDetails
							.setVatTax((Double) (price / 100 * lkupRestTaxDetails
									.get(i).getTaxPer()));
					salesDetails.setVatTaxPercentage(""
							+ lkupRestTaxDetails.get(i).getTaxPer());
				} else {
					salesDetails
							.setOtherTax((Double) (price / 100 * lkupRestTaxDetails
									.get(i).getTaxPer()));
					salesDetails.setOtherTaxPercentage(""
							+ lkupRestTaxDetails.get(i).getTaxPer());
				}

				row.addView(textView1);
				row.addView(textView2);
				payBillTable.addView(row, 3 + i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		totalAmount.setText("" + round(price + taxAmount));

		tipAmount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if ((tipAmount.getText().toString().trim() + "") == "") {
					// salesDetails.setTipAmt(0.0);
					totalAmount.setText("" + round(price + taxAmount));
				} else {
					totalAmount.setText(""
							+ round(price
									+ taxAmount
									+ Double.parseDouble(tipAmount.getText()
											.toString().trim())));
				}
			}
		});

		final LinearLayout hscrollbar = (LinearLayout) payBillDialog
				.findViewById(R.id.hscrollbar);

		for (int i = 0; i < lkupPayTypeDetails.size(); i++) {
			Button button = new Button(IConstants.mContext);
			button.setText("" + lkupPayTypeDetails.get(i).getPayDesc());
			button.setTextColor(Color.WHITE);
			button.setBackgroundColor(Color.parseColor("#99000000"));
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.MATCH_PARENT);

			layoutParams.setMargins(1, 1, 1, 1);

			if (lkupPayTypeDetails.size() < 5)
				layoutParams.weight = 1;

			button.setLayoutParams(layoutParams);
			button.setGravity(Gravity.CENTER);
			button.setTag(lkupPayTypeDetails.get(i));
			button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					for (int j = 0; j < hscrollbar.getChildCount(); j++) {
						hscrollbar.getChildAt(j).setBackgroundColor(
								Color.parseColor("#99000000"));
					}

					LkupPayTypeDetails lkupPayTypeDetail = (LkupPayTypeDetails) v
							.getTag();

					payType = lkupPayTypeDetail.getPayType();
					payDesc = lkupPayTypeDetail.getPayDesc();

					v.setBackgroundResource(R.drawable.green_btn_gradient);
				}
			});

			if (lkupPayTypeDetails.get(i).getPayType().equalsIgnoreCase("CS")) {
				button.setBackgroundResource(R.drawable.green_btn_gradient);
				payType = lkupPayTypeDetails.get(i).getPayType();
				payDesc = lkupPayTypeDetails.get(i).getPayDesc();
			}

			hscrollbar.addView(button);
		}

		dialog.setView(payBillDialog);
		dialog.show();
		// Overriding the handler immediately after show is probably a better
		// approach than OnShowListener as described below
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();

						salesDetails.setOrderNo(OrderNewActivity3.orderNo);
						salesDetails.setBillAmt(price);
						salesDetails.setTotalAmt(price);

						salesDetails.setPayType(payType);
						salesDetails.setPayDesc(payDesc);

						if ((tipAmount.getText().toString().trim() + "") == "") {
							salesDetails.setTipAmt(0.0);
						} else {
							try {
								salesDetails.setTipAmt(Double
										.parseDouble(tipAmount.getText()
												.toString().trim()));
								salesDetails.setTotalAmt(price
										+ taxAmount
										+ Double.parseDouble(tipAmount
												.getText().toString().trim()));
							} catch (Exception e) {
								salesDetails.setTipAmt(0.0);
							}
						}
						try {
							// Gson gson = new
							// GsonBuilder().excludeFieldsWithModifiers(
							// Modifier.TRANSIENT).create();
							Gson gson = new GsonBuilder()
									.excludeFieldsWithoutExposeAnnotation()
									.create();

							String json = gson.toJson(salesDetails);
							JSONObject jsonObject = new JSONObject();
							JSONArray jsonArray = new JSONArray();

							jsonArray.put(new JSONObject(json));
							jsonObject.put("salesDetails", jsonArray);
							jsonObject = new JSONObject(jsonObject.toString());
							System.out.println(jsonObject.toString());

							salesController.postsalesDetails(jsonObject,
									salesDetails);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

		dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
	}

	public void datePicker(final Activity activity) {

		// Process to get Current Date
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		// Launch Date Picker Dialog
		DatePickerDialog dpd = new DatePickerDialog(activity,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// Display Selected date in textbox
						// txtDate.setText(dayOfMonth + "-"+ (monthOfYear + 1) +
						// "-" + year);

						System.out.println(dayOfMonth + "-" + (monthOfYear + 1)
								+ "-" + year);
					}
				}, mYear, mMonth, mDay);
		dpd.show();
	}

	public void timePicker(final Activity activity) {

		// Process to get Current Time
		final Calendar c = Calendar.getInstance();
		int mHour = c.get(Calendar.HOUR_OF_DAY);
		int mMinute = c.get(Calendar.MINUTE);

		// Launch Time Picker Dialog
		TimePickerDialog tpd = new TimePickerDialog(activity,
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						// TODO Auto-generated method stub
						// txtTime.setText(hourOfDay + ":" + minute);
						System.out.println(hourOfDay + ":" + minute);
					}
				}, mHour, mMinute, false);
		tpd.show();
	}

	public void tryagain(final Activity activity,
			final TryAgain tryAgainListener) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		alert.setCancelable(false);
		alert.setTitle("Server is un-reachable!");

		alert.setPositiveButton("Try Again",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						tryAgainListener.tryAgain();
					}
				});

		alert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		alert.show();
	}

	public String round(Double d) {
		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMinimumFractionDigits(2);
		formatter.setMaximumFractionDigits(2);
		return formatter.format(d);
	}

	public void showProgress(final Activity activity) {
		progressDialog = new ProgressDialog(activity);
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Please wait...");
		progressDialog.show();
	}

	public void showProgress(final Activity activity, String content) {
		progressDialog = new ProgressDialog(activity);
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(content);
		progressDialog.show();
	}

	public void dismissProgress() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}
}
