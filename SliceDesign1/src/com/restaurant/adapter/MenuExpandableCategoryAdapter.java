package com.restaurant.adapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.j256.ormlite.stmt.QueryBuilder;
import com.restaurant.adapter.MenuExpandableCourseAdapter.CourseAnimateOrderIcon;
import com.restaurant.app.ExpandableMenuActivity;
import com.restaurant.app.MainMenuActivity;
import com.restaurant.app.R;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.OrderMenuDetails;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;

public class MenuExpandableCategoryAdapter extends BaseExpandableListAdapter {

	private Activity activity;
	private ArrayList<Object> childern;
	private LayoutInflater inflater;
	private ArrayList<String> parents;
	// ArrayList<MenuDetails> child;
	private ExpandableListView expandableListView;
	public int lastExpandedGroupPosition;

	Typeface font;

	// private DebugListView listViewCache[];

	ArrayAdapter<String> dataAdapter;

	Spinner itemCategorySpinner = null;

	IDialog iDialog = new IDialog();

	// int selectedItemCategoryPosition = 0;
	// String selectedItemCategory = "All Categories";

	public interface SelectedItemCategory {
		void selectedItemCategory(String selectedItemCategory);
	}

	SelectedItemCategory selectedItemCategoryListener;

	public interface CategoryAnimateOrderIcon {
		void animate();
	}

	CategoryAnimateOrderIcon categoryAnimateOrderIconListener;

	public MenuExpandableCategoryAdapter(Activity activity,
			ArrayList<String> parents, ArrayList<Object> childern,
			ExpandableListView expandableListView,
			CategoryAnimateOrderIcon categoryAnimateOrderIconListener,
			SelectedItemCategory selectedItemCategoryListener) {
		this.activity = activity;
		this.parents = parents;
		this.childern = childern;
		this.expandableListView = expandableListView;
		this.categoryAnimateOrderIconListener = categoryAnimateOrderIconListener;
		this.selectedItemCategoryListener = selectedItemCategoryListener;

		font = Typeface.createFromAsset(activity.getAssets(),
				"Roboto-Regular.ttf");

		// listViewCache = new DebugListView[ parents.size() ];

		List<String> itemCategorys = new ArrayList<String>();
		itemCategorys.add("All Categories");

		try {
			ArrayList<MenuDetails> itemCategory = (ArrayList<MenuDetails>) DatabaseManager
					.getInstance().getMenuDetailsDao().queryBuilder()
					.distinct().selectColumns("itemCategory").query();
			for (MenuDetails menuDetail : itemCategory) {
				itemCategorys.add("" + menuDetail.getItemCategory());
			}

			dataAdapter = new ArrayAdapter<String>(activity,
					R.layout.spinner_list, itemCategorys);

			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setInflater(LayoutInflater inflater, Activity activity) {
		this.inflater = inflater;
		this.activity = activity;
	}

	// @Override
	// public void registerDataSetObserver(DataSetObserver observer) {
	// super.registerDataSetObserver(observer);
	// }

	public interface AnimateOrderIcon {
		void animate();
	}

	private class ChildViewHolder {
		ImageView itemImage, itemImageHover, layoutAdd, layoutRemove;
		View layoutAddRemoveVBar, layoutAddRemoveDivider;
		TextView itemName, description, orderdetails, quantity, price;
		LinearLayout layoutContent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final ArrayList<MenuDetails> child = (ArrayList<MenuDetails>) childern
				.get(groupPosition);

		final MenuDetails menuDetail = child.get(childPosition);

		final ChildViewHolder childViewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.child_menu_list_item, null);

			childViewHolder = new ChildViewHolder();

			childViewHolder.itemName = (TextView) convertView
					.findViewById(R.id.itemName);
			childViewHolder.description = (TextView) convertView
					.findViewById(R.id.description);
			childViewHolder.orderdetails = (TextView) convertView
					.findViewById(R.id.orderdetails);

			childViewHolder.quantity = (TextView) convertView
					.findViewById(R.id.quantity);
			childViewHolder.price = (TextView) convertView
					.findViewById(R.id.price);

			childViewHolder.itemName.setTypeface(font);
			childViewHolder.description.setTypeface(font);
			childViewHolder.orderdetails.setTypeface(font);

			childViewHolder.quantity.setTypeface(font);
			childViewHolder.price.setTypeface(font);

			childViewHolder.itemImage = (ImageView) convertView
					.findViewById(R.id.itemImage);
			childViewHolder.itemImageHover = (ImageView) convertView
					.findViewById(R.id.itemImageHover);

			childViewHolder.layoutAdd = (ImageView) convertView
					.findViewById(R.id.layoutAdd);

			childViewHolder.layoutAddRemoveVBar = (View) convertView
					.findViewById(R.id.layoutAddRemoveVBar);

			childViewHolder.layoutAddRemoveDivider = (View) convertView
					.findViewById(R.id.layoutAddRemoveDivider);

			childViewHolder.layoutRemove = (ImageView) convertView
					.findViewById(R.id.layoutRemove);

			childViewHolder.layoutContent = (LinearLayout) convertView
					.findViewById(R.id.layoutContent);

			convertView.setTag(childViewHolder);
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}

		childViewHolder.itemName.setText(menuDetail.getItemName());

		childViewHolder.itemImage.setImageBitmap(menuDetail
				.getItemImageCircle());
		childViewHolder.itemImageHover.setImageBitmap(menuDetail
				.getItemImageHoverCircle());
		// childViewHolder.itemImageHover.setVisibility(menuDetail
		// .getItemImageHover());

		if (menuDetail.getModifierStatus().equalsIgnoreCase("y")) {
			childViewHolder.description.setText("Having modifiers!");
			childViewHolder.orderdetails
					.setText("Click item to choose modifier");
		} else {
			childViewHolder.description.setText(""
					+ menuDetail.getDescription());
			childViewHolder.orderdetails.setText("Rs " + menuDetail.getPrice()
					+ "0");
		}

		// if (menuDetail.getQuantity() > 0) {
		// childViewHolder.layoutAddRemoveDivider.setVisibility(View.VISIBLE);
		// childViewHolder.layoutRemove.setVisibility(View.VISIBLE);
		// } else {
		// childViewHolder.layoutAddRemoveDivider.setVisibility(View.GONE);
		// childViewHolder.layoutRemove.setVisibility(View.GONE);
		// }

		viewOrder(menuDetail, childViewHolder);

		if (menuDetail.getModifierStatus().equalsIgnoreCase("y")) {
			childViewHolder.layoutAddRemoveDivider.setVisibility(View.GONE);
			childViewHolder.layoutRemove.setVisibility(View.GONE);
		} else {
			childViewHolder.layoutAddRemoveDivider.setVisibility(View.VISIBLE);
			childViewHolder.layoutRemove.setVisibility(View.VISIBLE);
		}

		childViewHolder.layoutAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (menuDetail.getModifierStatus().equalsIgnoreCase("n")) {
					childViewHolder.layoutAddRemoveDivider
							.setVisibility(View.VISIBLE);
					childViewHolder.layoutRemove.setVisibility(View.VISIBLE);

					menuDetail.setToggleView(false);
					menuDetail.setFirstView(1);

					// childViewHolder.itemImageHover.setAlpha(0);
					// menuDetail.setItemImageHover(View.VISIBLE);
					// childViewHolder.itemImageHover.setVisibility(menuDetail
					// .getItemImageHover());

					addToOrder(menuDetail, childViewHolder);

					categoryAnimateOrderIconListener.animate();

					// changeColor(childViewHolder.layoutContent);
				} else {

					childViewHolder.layoutAddRemoveDivider
							.setVisibility(View.GONE);
					childViewHolder.layoutRemove.setVisibility(View.GONE);

					iDialog.newmodifier(menuDetail);
				}
			}
		});

		childViewHolder.layoutRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (menuDetail.getModifierStatus().equalsIgnoreCase("n")) {
					menuDetail.setToggleView(false);
					menuDetail.setFirstView(1);

					// menuDetail.setItemImageHover(View.VISIBLE);
					// childViewHolder.itemImageHover
					// .setVisibility(menuDetail
					// .getItemImageHover());

					removeOrder(menuDetail, childViewHolder);

					categoryAnimateOrderIconListener.animate();
				} else {
					// iDialog.newmodifier(menuDetail);
				}
			}
		});
		// childViewHolder.layoutAdd.setVisibility(View.GONE);
		// childViewHolder.layoutAddRemoveVBar.setVisibility(View.GONE);
		// childViewHolder.layoutAddRemoveDivider.setVisibility(View.GONE);
		// childViewHolder.layoutRemove.setVisibility(View.GONE);

		// convertView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View view) {
		// if (menuDetail.getModifierStatus().equalsIgnoreCase("y")) {
		// iDialog.newmodifier(menuDetail);
		// }
		// }
		// });

		// onTouchChangeColor(childViewHolder.layoutAdd,
		// childViewHolder.layoutContent);
		//
		// onTouchChangeColor(childViewHolder.layoutRemove,
		// childViewHolder.layoutContent);
		//
		// onTouchChangeColor(convertView,
		// childViewHolder.layoutContent);

		// convertView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View view) {
		// Toast.makeText(activity, child.get(childPosition),
		// Toast.LENGTH_SHORT).show();
		// }
		// });

		return convertView;

		// View v = null;
		// if( listViewCache[groupPosition] != null )
		// v = listViewCache[groupPosition];
		// else {
		// DebugListView dev = new DebugListView( activity );
		//
		// dev.setRows( 2 );
		//
		// // Defined Array values to show in ListView
		// String[] values = new String[] { "Item 1",
		// "Item 2",
		// "Item 3",
		// "Item 4",
		// "Item 5",
		// "Item 1",
		// "Item 2",
		// "Item 3",
		// "Item 4",
		// "Item 5",
		// "Item 1",
		// "Item 2",
		// "Item 3",
		// "Item 4",
		// "Item 5",
		// "Item 1",
		// "Item 2",
		// "Item 3",
		// "Item 4",
		// "Item 5"
		// };
		// dev.setAdapter(new ArrayAdapter<String>(activity,
		// android.R.layout.simple_list_item_1, android.R.id.text1, values));
		// dev.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // disallow the onTouch for your scrollable parent view
		// v.getParent().requestDisallowInterceptTouchEvent(true);
		// return false;
		// }
		// });
		//
		// //dev.setRows( getTotalHeightofListView(dev) );
		//
		// listViewCache[groupPosition] = dev;
		// v = dev;
		// }
		//
		// return v;
	}

	private class GroupViewHolder {
		ImageView imgBg, groupIndicator;
		View divider;
		TextView course, itemCount;
		LinearLayout layoutListItem, spinner1Layout;
		RelativeLayout imgBgLayout, content, innerContent;
		FrameLayout frameLayoutContent;
	}

	// public int getTotalHeightofListView(ListView listView) {
	//
	// ListAdapter mAdapter = listView.getAdapter();
	//
	// int totalHeight = 0;
	//
	// for (int i = 0; i < mAdapter.getCount(); i++) {
	// View mView = mAdapter.getView(i, null, listView);
	//
	// mView.measure(
	// MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
	//
	// MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	//
	// totalHeight += mView.getMeasuredHeight();
	// Log.w("HEIGHT" + i, String.valueOf(totalHeight));
	//
	// i = mAdapter.getCount();
	// }
	//
	// // ViewGroup.LayoutParams params = listView.getLayoutParams();
	// // params.height = totalHeight
	// // + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
	// // listView.setLayoutParams(params);
	// // listView.requestLayout();
	// return totalHeight;
	// }

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		final GroupViewHolder groupViewHolder;

		// if (convertView == null) {
		// convertView = inflater.inflate(R.layout.parent_menu_list_item, null);
		// }
		//
		// ((CheckedTextView)
		// convertView).setText(parents.get(groupPosition));
		// ((CheckedTextView) convertView).setChecked(isExpanded);
		//
		// return convertView;

		// System.out.println("groupPosition " + groupPosition + ", isExpanded "
		// + isExpanded);

		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.parent_menu_list_item, null);

			groupViewHolder = new GroupViewHolder();

			groupViewHolder.course = (TextView) convertView
					.findViewById(R.id.course);
			groupViewHolder.itemCount = (TextView) convertView
					.findViewById(R.id.itemCount);

			groupViewHolder.imgBgLayout = (RelativeLayout) convertView
					.findViewById(R.id.imgBgLayout);

			groupViewHolder.imgBg = (ImageView) convertView
					.findViewById(R.id.imgBg);

			groupViewHolder.layoutListItem = (LinearLayout) convertView
					.findViewById(R.id.layoutListItem);
			groupViewHolder.layoutListItem
					.setBackgroundColor(Color.TRANSPARENT);

			groupViewHolder.content = (RelativeLayout) convertView
					.findViewById(R.id.content);

			groupViewHolder.innerContent = (RelativeLayout) convertView
					.findViewById(R.id.innerContent);

			groupViewHolder.spinner1Layout = (LinearLayout) convertView
					.findViewById(R.id.spinner1Layout);

			groupViewHolder.divider = (View) convertView
					.findViewById(R.id.divider);

			groupViewHolder.groupIndicator = (ImageView) convertView
					.findViewById(R.id.groupIndicator);

			convertView.setTag(groupViewHolder);
		} else {
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}

		groupViewHolder.course.setText(parents.get(groupPosition));

		groupViewHolder.itemCount.setText("" + getChildrenCount(groupPosition));

		groupViewHolder.course.setTypeface(font);
		groupViewHolder.itemCount.setTypeface(font);

		// int imageResourceId = isExpanded ? R.drawable.down : R.drawable.next;
		// int imageResourceId = isExpanded ? android.R.drawable.arrow_up_float
		// : android.R.drawable.arrow_down_float;
		int imageResourceId = isExpanded ? R.drawable.expander_close_holo_light
				: R.drawable.expander_open_holo_light;
		groupViewHolder.groupIndicator.setImageResource(imageResourceId);

		if (groupPosition == 0) {

			groupViewHolder.imgBgLayout.setVisibility(View.INVISIBLE);

			groupViewHolder.content.setVisibility(View.GONE);

			groupViewHolder.spinner1Layout.setVisibility(View.GONE);

			// convertView.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			//
			// }
			// });

			try {
				android.widget.AbsListView.LayoutParams layoutParams = new android.widget.AbsListView.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				groupViewHolder.layoutListItem.setLayoutParams(layoutParams);
				// System.out.println("layoutListItem " + layoutListItem);
				// System.out.println("layoutListItem.getLayoutParams().height "
				// + layoutListItem.getLayoutParams().height);
				// System.out.println("ExpandableMenuActivity.screenHeight "
				// + ExpandableMenuActivity.screenHeight);
				groupViewHolder.layoutListItem.getLayoutParams().height = ExpandableMenuActivity.screenHeight;
				groupViewHolder.imgBgLayout.getLayoutParams().height = ExpandableMenuActivity.screenHeight;
				groupViewHolder.imgBg.getLayoutParams().height = ExpandableMenuActivity.screenHeight;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (groupPosition == 1) {
			try {
				groupViewHolder.layoutListItem.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
			} catch (Exception e) {
			}

			groupViewHolder.imgBgLayout.setVisibility(View.GONE);

			groupViewHolder.content.setVisibility(View.GONE);

			groupViewHolder.spinner1Layout.setVisibility(View.VISIBLE);

			ExpandableMenuActivity.firsttime = true;

			itemCategorySpinner = (Spinner) convertView
					.findViewById(R.id.spinner1);
			itemCategorySpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int pos, long id) {
							// selectedItemCategoryPosition = pos;
							// selectedItemCategory =
							// parent.getItemAtPosition(pos).toString();
							if (ExpandableMenuActivity.firsttime) {
								ExpandableMenuActivity.firsttime = false;
							} else {
								ExpandableMenuActivity.selectedItemCategoryPosition = pos;
								ExpandableMenuActivity.selectedItemCategory = parent
										.getItemAtPosition(pos).toString();
								ExpandableMenuActivity.firsttime = true;
								selectedItemCategoryListener
										.selectedItemCategory(ExpandableMenuActivity.selectedItemCategory);
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

			// if (itemCategorySpinner == null) {
			// itemCategorySpinner.setAdapter(dataAdapter);
			// } else {
			itemCategorySpinner.setAdapter(dataAdapter);
			itemCategorySpinner
					.setSelection(ExpandableMenuActivity.selectedItemCategoryPosition);
			// }

			// List<String> itemCategorys = new ArrayList<String>();
			// itemCategorys.add("All Categories");
			//
			// try {
			// ArrayList<MenuDetails> itemCategory = (ArrayList<MenuDetails>)
			// DatabaseManager.getInstance()
			// .getMenuDetailsDao().queryBuilder().distinct()
			// .selectColumns("itemCategory").query();
			// for (MenuDetails menuDetail : itemCategory) {
			// itemCategorys.add("" + menuDetail.getItemCategory());
			// }
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
			//
			// list.add("All Categories");
			// list.add("Chicken");
			// list.add("Mutton");
			// list.add("Mushroom");
			// list.add("Soup");
			// list.add("Curries");
			//
			// ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
			// activity, R.layout.spinner_list, itemCategorys);

			// dataAdapter
			// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// itemCategorySpinner.setAdapter(dataAdapter);

			// convertView.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			//
			// }
			// });
		} else {

			try {
				groupViewHolder.layoutListItem.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
			} catch (Exception e) {
			}

			groupViewHolder.imgBgLayout.setVisibility(View.GONE);
			groupViewHolder.spinner1Layout.setVisibility(View.GONE);
			groupViewHolder.content.setVisibility(View.VISIBLE);

			if (groupPosition == 2) {
				groupViewHolder.content.setPadding(5, 0, 5, 0);
				groupViewHolder.innerContent
						.setBackgroundResource(R.drawable.corner_black);

				groupViewHolder.divider.setVisibility(View.VISIBLE);
			} else {
				groupViewHolder.content.setPadding(5, 0, 5, 0);
				groupViewHolder.innerContent
						.setBackgroundResource(R.drawable.nocorner_black);

				if (groupPosition == getGroupCount() - 1 && !isExpanded) {
					// groupViewHolder.content.setPadding(5, 0, 5, 5);
					groupViewHolder.innerContent
							.setBackgroundResource(R.drawable.corner_black_bottom);
					groupViewHolder.divider.setVisibility(View.GONE);
				} else {
					groupViewHolder.divider.setVisibility(View.VISIBLE);
				}
			}
		}

		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<String>) childern.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return parents.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return parents.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);

		if (groupPosition != lastExpandedGroupPosition) {
			expandableListView.collapseGroup(lastExpandedGroupPosition);
		}

		lastExpandedGroupPosition = groupPosition;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public void viewOrder(MenuDetails menuDetail, ChildViewHolder viewHolder) {
		try {
			List<OrderMenuDetails> orderMenuDetailsList = DatabaseManager
					.getInstance().getOrderMenuDetailsDao().queryBuilder()
					.distinct().where().eq("itemId", menuDetail.getItemId())
					.query();
			System.out.println("menuDetail itemId" + menuDetail.getItemId());
			OrderMenuDetails orderMenuDetail = new OrderMenuDetails();

			if (orderMenuDetailsList.size() > 0) {
				orderMenuDetail = orderMenuDetailsList.get(0);

				viewHolder.quantity.setText("" + orderMenuDetail.getQuantity());
				viewHolder.price.setText("" + orderMenuDetail.getPrice() + "0");

				if (menuDetail.getModifierStatus().equalsIgnoreCase("n")) {
					viewHolder.orderdetails.setText("Quantity - "
							+ orderMenuDetail.getQuantity() + " | Rs "
							+ orderMenuDetail.getPrice() + "0");

					viewHolder.layoutAddRemoveDivider.setVisibility(View.GONE);
					viewHolder.layoutRemove.setVisibility(View.GONE);
				} else {
					viewHolder.layoutAddRemoveDivider
							.setVisibility(View.VISIBLE);
					viewHolder.layoutRemove.setVisibility(View.VISIBLE);
				}
				// menuDetail.setItemImageHover(View.VISIBLE);
				// viewHolder.itemImageHover.setVisibility(menuDetail
				// .getItemImageHover());

				ObjectAnimator animatorItemImageHovera = ObjectAnimator
						.ofFloat(viewHolder.itemImageHover, "scaleX", 1);
				ObjectAnimator animatorItemImageHoverb = ObjectAnimator
						.ofFloat(viewHolder.itemImageHover, "scaleY", 1);
				AnimatorSet set = new AnimatorSet();
				// set.play(animator1a).with(animator1b);
				set.play(animatorItemImageHovera).with(animatorItemImageHoverb);
				// if (duration == 0) {
				set.setDuration(0);
				// }
				set.start();

				menuDetail.setToggleView(false);
				menuDetail.setFirstView(1);

				// viewHolder.layoutAddRemoveDivider.setVisibility(View.VISIBLE);
				// viewHolder.layoutRemove.setVisibility(View.VISIBLE);
			} else {
				viewHolder.quantity.setText("1");
				viewHolder.price.setText("" + menuDetail.getPrice() + "0");
				// viewHolder.orderdetails
				// .setText("None of this has been placed");

				if (menuDetail.getModifierStatus().equalsIgnoreCase("n")) {
					viewHolder.orderdetails.setText("Rs "
							+ menuDetail.getPrice() + "0");
				} else {

				}

				viewHolder.layoutAddRemoveDivider.setVisibility(View.GONE);
				viewHolder.layoutRemove.setVisibility(View.GONE);

				// menuDetail.setItemImageHover(View.GONE);
				// viewHolder.itemImageHover.setVisibility(menuDetail
				// .getItemImageHover());

				ObjectAnimator animatorItemImageHovera = ObjectAnimator
						.ofFloat(viewHolder.itemImageHover, "scaleX", 0);
				ObjectAnimator animatorItemImageHoverb = ObjectAnimator
						.ofFloat(viewHolder.itemImageHover, "scaleY", 0);
				AnimatorSet set = new AnimatorSet();
				// set.play(animator1a).with(animator1b);
				set.play(animatorItemImageHovera).with(animatorItemImageHoverb);
				// if (duration == 0) {
				set.setDuration(0);
				// }
				set.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void addToOrder(MenuDetails menuDetail, ChildViewHolder viewHolder) {
		try {

			// QueryBuilder<OrderMenuDetails, Integer> qBuilder =
			// DatabaseManager
			// .getInstance().getOrderMenuDetailsDao().queryBuilder();
			// qBuilder.orderBy("sequenceNo", false); // false for descending
			// order
			// qBuilder.limit(1);
			// List<OrderMenuDetails> orderMenuDetailsListOfOne =
			// qBuilder.query();

			List<OrderMenuDetails> orderMenuDetailsList = DatabaseManager
					.getInstance().getOrderMenuDetailsDao().queryBuilder()
					.distinct().where().eq("itemId", menuDetail.getItemId())
					.query();
			System.out.println("menuDetail itemId" + menuDetail.getItemId());
			OrderMenuDetails orderMenuDetail = new OrderMenuDetails();

			if (orderMenuDetailsList.size() > 0) {
				orderMenuDetail = orderMenuDetailsList.get(0);

				orderMenuDetail.setQuantity(orderMenuDetail.getQuantity() + 1);
				orderMenuDetail.setPrice(menuDetail.getPrice()
						* orderMenuDetail.getQuantity());
				DatabaseManager.getInstance().getOrderMenuDetailsDao()
						.update(orderMenuDetail);

				System.out.println("orderMenuDetail getOrderMenuId "
						+ orderMenuDetail.getOrderMenuId());
				System.out.println("orderMenuDetail getQuantity "
						+ orderMenuDetail.getQuantity());
				// System.out.println("orderMenuDetail getSequenceNo "
				// + orderMenuDetail.getSequenceNo());
			} else {
				System.out.println("New item added!");
				// orderMenuDetail.setOrderMenuId(DatabaseManager
				// .getInstance().getOrderMenuDetailsDao().queryForAll().size()+1);
				orderMenuDetail.setItemId(menuDetail.getItemId());
				orderMenuDetail.setModifierStatus("n");
				orderMenuDetail.setItemName(menuDetail.getItemName());
				orderMenuDetail.setQuantity(1);
				orderMenuDetail.setPrice(menuDetail.getPrice()
						* orderMenuDetail.getQuantity());
				orderMenuDetail.setItemPrice(menuDetail.getPrice());
				orderMenuDetail.setPrinterId(menuDetail.getPrinterId());
				orderMenuDetail.setChefNote("");
				orderMenuDetail.setAllergy("");
				orderMenuDetail.setCookTime(menuDetail.getPrepTime());
				orderMenuDetail.setCourse(menuDetail.getCourse());
				orderMenuDetail.setOrderType("");
				orderMenuDetail.setEditable(true);
				orderMenuDetail.setHavingAnswer("");

				// orderMenuDetail
				// .setSequenceNo(orderMenuDetailsListOfOne.size() == 0 ? 1
				// : orderMenuDetailsListOfOne.get(0)
				// .getSequenceNo() + 1);

				// Gson gson = new Gson();
				// String json = gson.toJson(orderMenuDetail);
				//
				// System.out.println("orderMenuDetail " + json);

				DatabaseManager.getInstance().getOrderMenuDetailsDao()
						.create(orderMenuDetail);
			}

			viewHolder.quantity.setText("" + orderMenuDetail.getQuantity());
			viewHolder.price.setText("" + orderMenuDetail.getPrice() + "0");

			viewHolder.orderdetails.setText("Quantity - "
					+ orderMenuDetail.getQuantity() + " | Rs "
					+ orderMenuDetail.getPrice() + "0");

			ObjectAnimator animatorItemImageHovera = ObjectAnimator.ofFloat(
					viewHolder.itemImageHover, "scaleX", 1);
			ObjectAnimator animatorItemImageHoverb = ObjectAnimator.ofFloat(
					viewHolder.itemImageHover, "scaleY", 1);
			AnimatorSet set = new AnimatorSet();
			// set.play(animator1a).with(animator1b);
			set.play(animatorItemImageHovera).with(animatorItemImageHoverb);
			// if (duration == 0) {
			// set.setDuration(duration);
			// }
			set.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeOrder(MenuDetails menuDetail, ChildViewHolder viewHolder) {
		try {
			List<OrderMenuDetails> orderMenuDetailsList = DatabaseManager
					.getInstance().getOrderMenuDetailsDao().queryBuilder()
					.distinct().where().eq("itemId", menuDetail.getItemId())
					.query();
			System.out.println("menuDetail itemId" + menuDetail.getItemId());
			if (orderMenuDetailsList.size() > 0) {
				OrderMenuDetails orderMenuDetail = orderMenuDetailsList.get(0);

				if (orderMenuDetail.getQuantity() > 1) {
					orderMenuDetail
							.setQuantity(orderMenuDetail.getQuantity() - 1);
					orderMenuDetail.setPrice(menuDetail.getPrice()
							* orderMenuDetail.getQuantity());
					DatabaseManager.getInstance().getOrderMenuDetailsDao()
							.update(orderMenuDetail);

					System.out.println("orderMenuDetail getOrderMenuId "
							+ orderMenuDetail.getOrderMenuId());
					System.out.println("orderMenuDetail getQuantity "
							+ orderMenuDetail.getQuantity());

					viewHolder.quantity.setText(""
							+ orderMenuDetail.getQuantity());
					viewHolder.price.setText("" + orderMenuDetail.getPrice()
							+ "0");

					viewHolder.orderdetails.setText("Quantity - "
							+ orderMenuDetail.getQuantity() + " | Rs "
							+ orderMenuDetail.getPrice() + "0");
				} else {
					System.out.println("item deleted!");
					DatabaseManager.getInstance().getOrderMenuDetailsDao()
							.delete(orderMenuDetail);

					viewHolder.quantity.setText("1");
					viewHolder.price.setText("" + menuDetail.getPrice() + "0");
					// viewHolder.orderdetails
					// .setText("None of this has been placed");
					viewHolder.orderdetails.setText("Rs: "
							+ menuDetail.getPrice() + "0");

					menuDetail.setToggleView(true);
					menuDetail.setFirstView(2);

					// firstView(viewHolder, 1);

					// menuDetail.setItemImageHover(View.GONE);
					// viewHolder.itemImageHover.setVisibility(menuDetail
					// .getItemImageHover());

					ObjectAnimator animatorItemImageHovera = ObjectAnimator
							.ofFloat(viewHolder.itemImageHover, "scaleX", 0);
					ObjectAnimator animatorItemImageHoverb = ObjectAnimator
							.ofFloat(viewHolder.itemImageHover, "scaleY", 0);
					AnimatorSet set = new AnimatorSet();
					// set.play(animator1a).with(animator1b);
					set.play(animatorItemImageHovera).with(
							animatorItemImageHoverb);
					// if (duration == 0) {
					// set.setDuration(duration);
					// }
					set.start();

					viewHolder.layoutAddRemoveDivider.setVisibility(View.GONE);
					viewHolder.layoutRemove.setVisibility(View.GONE);
				}
			} else {
				System.out.println("No item to delete!");

				viewHolder.quantity.setText("1");
				viewHolder.price.setText("" + menuDetail.getPrice() + "0");
				// viewHolder.orderdetails
				// .setText("None of this has been placed");
				viewHolder.orderdetails.setText("Rs: " + menuDetail.getPrice()
						+ "0");

				menuDetail.setToggleView(true);
				menuDetail.setFirstView(2);

				// firstView(viewHolder, 1);

				// menuDetail.setItemImageHover(View.GONE);
				// viewHolder.itemImageHover.setVisibility(menuDetail
				// .getItemImageHover());

				ObjectAnimator animatorItemImageHovera = ObjectAnimator
						.ofFloat(viewHolder.itemImageHover, "scaleX", 0);
				ObjectAnimator animatorItemImageHoverb = ObjectAnimator
						.ofFloat(viewHolder.itemImageHover, "scaleY", 0);
				AnimatorSet set = new AnimatorSet();
				// set.play(animator1a).with(animator1b);
				set.play(animatorItemImageHovera).with(animatorItemImageHoverb);
				// if (duration == 0) {
				// set.setDuration(duration);
				// }
				set.start();

				viewHolder.layoutAddRemoveDivider.setVisibility(View.GONE);
				viewHolder.layoutRemove.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private Bitmap getCroppedBitmap(Bitmap bitmap) {
	// Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	// bitmap.getHeight(), Config.ARGB_8888);
	// Canvas canvas = new Canvas(output);
	//
	// final int color = 0xff424242;
	// Paint paint = new Paint();
	// final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	// // final RectF sOuterRect = new RectF(0, 0, bitmap.getWidth(),
	// // bitmap.getHeight());
	//
	// paint.setAntiAlias(true);
	// paint.setFilterBitmap(true);
	//
	// canvas.drawARGB(0, 0, 0, 0);
	// paint.setColor(color);
	// // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	// canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
	// bitmap.getWidth() / 2, paint);
	// paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	//
	// canvas.drawBitmap(bitmap, rect, rect, paint);
	//
	// return output;
	// }

	public void onTouchChangeColor(final View view,
			final LinearLayout layoutContent) {
		view.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					layoutContent.setBackgroundColor(Color
							.parseColor("#dfdfe1"));
					break;

				case MotionEvent.ACTION_MOVE:
					layoutContent.setBackgroundColor(Color.WHITE);
					break;

				case MotionEvent.ACTION_UP:
					layoutContent.setBackgroundColor(Color.WHITE);
					break;

				case MotionEvent.ACTION_CANCEL:
					layoutContent.setBackgroundColor(Color.WHITE);
					break;

				default:
					layoutContent.setBackgroundColor(Color.WHITE);
					break;
				}
				return false;
			}
		});
	}

	// public void changeColor(final View view) {
	// final float[] curr = new float[3], from = new float[3], to = new
	// float[3];
	//
	// Color.colorToHSV(Color.parseColor("#FFFFFFFF"), from); // from white
	// Color.colorToHSV(Color.GREEN, to); // to red
	//
	// final ValueAnimator anim = ValueAnimator.ofFloat(0, 1); // values are
	// // arbitrary
	// anim.setDuration(10); // duration of animation
	//
	// anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	// @Override
	// public void onAnimationUpdate(ValueAnimator animation) {
	// curr[0] = from[0] + (to[0] - from[0])
	// * animation.getAnimatedFraction();
	// curr[1] = from[1] + (to[1] - from[1])
	// * animation.getAnimatedFraction();
	// curr[2] = from[2] + (to[2] - from[2])
	// * animation.getAnimatedFraction();
	//
	// view.setBackgroundColor(Color.HSVToColor(curr));
	// }
	// });
	//
	// anim.addListener(new AnimatorListener() {
	//
	// @Override
	// public void onAnimationStart(Animator animation) {
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animator animation) {
	// }
	//
	// @Override
	// public void onAnimationEnd(Animator animation) {
	// backToOrgColor(view);
	// }
	//
	// @Override
	// public void onAnimationCancel(Animator animation) {
	// }
	// });
	//
	// anim.start();
	// }
	//
	// public void backToOrgColor(final View view) {
	// final float[] curr = new float[3], from = new float[3], to = new
	// float[3];
	//
	// Color.colorToHSV(Color.GREEN, from); // from white
	// Color.colorToHSV(Color.parseColor("#FFFFFFFF"), to); // to red
	//
	// final ValueAnimator anim = ValueAnimator.ofFloat(0, 1); // values are
	// // arbitrary
	// anim.setDuration(10); // duration of animation
	//
	// anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	// @Override
	// public void onAnimationUpdate(ValueAnimator animation) {
	// curr[0] = from[0] + (to[0] - from[0])
	// * animation.getAnimatedFraction();
	// curr[1] = from[1] + (to[1] - from[1])
	// * animation.getAnimatedFraction();
	// curr[2] = from[2] + (to[2] - from[2])
	// * animation.getAnimatedFraction();
	//
	// view.setBackgroundColor(Color.HSVToColor(curr));
	// }
	// });
	//
	// anim.addListener(new AnimatorListener() {
	//
	// @Override
	// public void onAnimationStart(Animator animation) {
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animator animation) {
	// }
	//
	// @Override
	// public void onAnimationEnd(Animator animation) {
	// // try {
	// // Thread.sleep(1000);
	// // //view.setBackgroundColor(Color.WHITE);
	// // } catch (InterruptedException e) {
	// // e.printStackTrace();
	// // }
	// }
	//
	// @Override
	// public void onAnimationCancel(Animator animation) {
	// }
	// });
	//
	// anim.start();
	// }
}
