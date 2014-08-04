package com.restaurant.adapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.restaurant.adapter.OrderNewAdapter.ViewHolder;
import com.restaurant.app.ExpandableMenuActivity;
import com.restaurant.app.HomeActivity;
import com.restaurant.app.OrderNewActivity;
import com.restaurant.app.OrderNewActivity3;
import com.restaurant.app.R;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.ModifierQuestionAnswer;
import com.restaurant.model.OrderMenuDetails;
import com.restaurant.util.IDialog;

public class OrderNewAdapter3 extends BaseAdapter {
	private Activity context;
	private LayoutInflater inflater;

	private List<OrderMenuDetails> listArray;

	Typeface font;

	private ShowConfirmOrderOrPayBill showConfirmOrderOrPayBill;
	
	IDialog iDialog = new IDialog();

	public interface ShowConfirmOrderOrPayBill {
		void showConfirmOrderOrPayBill(Boolean b);
	}

	public OrderNewAdapter3(Activity context,
			ShowConfirmOrderOrPayBill showConfirmOrderOrPayBill) {
		this.context = context;
		this.showConfirmOrderOrPayBill = showConfirmOrderOrPayBill;

		font = Typeface.createFromAsset(context.getAssets(),
				"Roboto-Regular.ttf");

		updateAdapter();
	}

	@Override
	public int getCount() {
		return listArray.size();
	}

	@Override
	public Object getItem(int i) {
		return listArray.get(i);
	}

	private void updateAdapter() {
		try {
			listArray = new ArrayList<OrderMenuDetails>();

			List<OrderMenuDetails> orderMenuDetailsList = DatabaseManager
					.getInstance().getOrderMenuDetailsDao().queryBuilder()
					.distinct().orderBy("orderMenuId", true).query();

			if (orderMenuDetailsList.size() > 0) {
				listArray.add(orderMenuDetailsList.get(0));
				listArray.add(orderMenuDetailsList.get(0));
			}

			for (int i = 0; i < orderMenuDetailsList.size(); i++) {
				listArray.add(orderMenuDetailsList.get(i));
			}

			if (orderMenuDetailsList.size() > 0) {
				listArray.add(orderMenuDetailsList.get(0));
			}

			// if(orderMenuDetailsList.size()>0){
			// showConfirmOrderOrPayBill.showConfirmOrderOrPayBill(false);
			// } else {
			showConfirmOrderOrPayBill.showConfirmOrderOrPayBill(true);
			// }

			ExpandableMenuActivity.updateADapter();
			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refreshList() {
		updateAdapter();
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void updateItem(OrderMenuDetails orderMenuDetail) {
		try {
			DatabaseManager.getInstance().getOrderMenuDetailsDao()
					.update(orderMenuDetail);
			updateAdapter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeItem(OrderMenuDetails orderMenuDetail) {
		try {
			DatabaseManager.getInstance().getOrderMenuDetailsDao()
					.delete(orderMenuDetail);
			updateAdapter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeAllItems() {
		updateAdapter();

		if (getCount() > 0) {
			showConfirmOrderOrPayBill.showConfirmOrderOrPayBill(false);
		} else {
			showConfirmOrderOrPayBill.showConfirmOrderOrPayBill(true);
		}
	}

	class ViewHolder {
		ImageView imgBg, layoutAdd, layoutRemove, layoutCancel;
		View layoutAddRemoveDivider;
		TextView itemName, modifiers, quantity, quantityTxt, pipe, price, priceTxt, orderTitle;
		TextView chefnotes;
		LinearLayout layoutListItem, layoutContent, spinner1Layout,
				myInnContent;
		RelativeLayout imgBgLayout, content, mytitleContent;
		// FrameLayout frameLayoutContent;
	}

	@Override
	public View getView(final int index, View convertView,
			final ViewGroup parent) {

		try {
			final ViewHolder viewHolder;

			if (convertView == null) {

				if (inflater == null) {
					inflater = LayoutInflater.from(parent.getContext());
				}

				convertView = inflater.inflate(R.layout.order_list_new_item,
						parent, false);

				viewHolder = new ViewHolder();

				// viewHolder.frameLayoutContent = (FrameLayout) convertView
				// .findViewById(R.id.frameLayoutContent);

				viewHolder.layoutListItem = (LinearLayout) convertView
						.findViewById(R.id.layoutListItems);
				viewHolder.content = (RelativeLayout) convertView
						.findViewById(R.id.content);

				viewHolder.imgBgLayout = (RelativeLayout) convertView
						.findViewById(R.id.imgBgLayout);
				viewHolder.imgBg = (ImageView) convertView
						.findViewById(R.id.imgBg);

				viewHolder.layoutAdd = (ImageView) convertView
						.findViewById(R.id.layoutAdd);

				viewHolder.layoutAddRemoveDivider = (View) convertView
						.findViewById(R.id.layoutAddRemoveDivider);

				viewHolder.layoutRemove = (ImageView) convertView
						.findViewById(R.id.layoutRemove);

				viewHolder.layoutCancel = (ImageView) convertView
						.findViewById(R.id.layoutCancel);

				viewHolder.content = (RelativeLayout) convertView
						.findViewById(R.id.content);

				viewHolder.spinner1Layout = (LinearLayout) convertView
						.findViewById(R.id.spinner1Layout);

				viewHolder.myInnContent = (LinearLayout) convertView
						.findViewById(R.id.myInnContent);

				viewHolder.mytitleContent = (RelativeLayout) convertView
						.findViewById(R.id.mytitleContent);

				viewHolder.orderTitle = (TextView) convertView
						.findViewById(R.id.orderTitle);

				viewHolder.itemName = (TextView) convertView
						.findViewById(R.id.itemName);
				viewHolder.modifiers = (TextView) convertView
						.findViewById(R.id.modifiers);
				viewHolder.quantity = (TextView) convertView
						.findViewById(R.id.quantity);
				viewHolder.price = (TextView) convertView
						.findViewById(R.id.price);
				
				
				viewHolder.quantityTxt = (TextView) convertView
						.findViewById(R.id.quantityTxt);
				viewHolder.pipe = (TextView) convertView
						.findViewById(R.id.pipe);
				viewHolder.priceTxt = (TextView) convertView
						.findViewById(R.id.priceTxt);

				viewHolder.chefnotes = (TextView) convertView
						.findViewById(R.id.chefnotes);
				
//				viewHolder.divider = (View) convertView
//						.findViewById(R.id.divider);

				viewHolder.orderTitle.setTypeface(font);

				viewHolder.itemName.setTypeface(font);
				viewHolder.modifiers.setTypeface(font);
				viewHolder.quantity.setTypeface(font);
				viewHolder.price.setTypeface(font);

				viewHolder.chefnotes.setTypeface(font);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final OrderMenuDetails orderMenuDetail = listArray.get(index);

			if (index == 0) {
				android.widget.AbsListView.LayoutParams layoutParams = new android.widget.AbsListView.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				viewHolder.layoutListItem.setLayoutParams(layoutParams);
				viewHolder.layoutListItem.getLayoutParams().height = OrderNewActivity3.screenHeight;
				viewHolder.layoutListItem.setBackgroundColor(Color.TRANSPARENT);
				viewHolder.imgBgLayout.setVisibility(View.INVISIBLE);
				viewHolder.content.setVisibility(View.GONE);

				viewHolder.imgBgLayout.getLayoutParams().height = OrderNewActivity3.screenHeight;
				viewHolder.imgBg.getLayoutParams().height = OrderNewActivity3.screenHeight;

			} else {
				android.widget.AbsListView.LayoutParams layoutParams = new android.widget.AbsListView.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				viewHolder.layoutListItem.setLayoutParams(layoutParams);
				// layoutListItem.getLayoutParams().height =
				// android.widget.AbsListView.LayoutParams.WRAP_CONTENT;

				// viewHolder.layoutListItem.setBackgroundColor(Color.BLACK);
				viewHolder.layoutListItem.setBackgroundColor(Color.WHITE);
				// viewHolder.layoutListItem.setBackgroundColor(Color
				// .parseColor(itemsColor[index]));
				viewHolder.imgBgLayout.setVisibility(View.GONE);
				viewHolder.content.setVisibility(View.VISIBLE);

				if (index == 1) {
					viewHolder.content.setVisibility(View.GONE);
					viewHolder.spinner1Layout.setVisibility(View.VISIBLE);
					
					viewHolder.spinner1Layout.setPadding(5, 5, 5, 0);

					viewHolder.mytitleContent
							.setBackgroundResource(R.drawable.corner_black);

					viewHolder.orderTitle.setVisibility(View.VISIBLE);
					viewHolder.chefnotes.setVisibility(View.GONE);

//					viewHolder.divider.setVisibility(View.GONE);

					// viewHolder.layoutListItem.getLayoutParams().height = 50;
				} else if (index > 1) {
					viewHolder.content.setVisibility(View.VISIBLE);
					viewHolder.spinner1Layout.setVisibility(View.GONE);

					viewHolder.myInnContent
							.setBackgroundResource(R.drawable.corner_white_bottom_border);
					viewHolder.myInnContent.setPadding(10, 0, 5, 0);

//					viewHolder.divider.setVisibility(View.VISIBLE);

					if (index == getCount() - 1) {
						viewHolder.content.setVisibility(View.GONE);
						viewHolder.spinner1Layout.setVisibility(View.VISIBLE);
						
						viewHolder.spinner1Layout.setPadding(5, 0, 5, 0);

//						viewHolder.divider.setVisibility(View.GONE);

						viewHolder.mytitleContent
								.setBackgroundResource(R.drawable.corner_black_bottom);

						viewHolder.orderTitle.setVisibility(View.GONE);
						viewHolder.chefnotes.setVisibility(View.VISIBLE);
					}
				}

				// viewHolder.item.setTextColor(Color.parseColor(itemsColor[index]));
			}
			
			
			orderMenuDetail.setModifierStatus(orderMenuDetail
					.getModifierStatus() == null ? "n" : orderMenuDetail
					.getModifierStatus());

			if (orderMenuDetail.getModifierStatus().equalsIgnoreCase("y")) {
				viewHolder.itemName.setText("" + orderMenuDetail.getItemName()
						+ " (" + orderMenuDetail.getModifierCategory() + ")");
				try {
					viewHolder.modifiers.setVisibility(View.VISIBLE);
					viewHolder.itemName.getLayoutParams().height = 40;
					viewHolder.itemName.setPadding(0, 0, 0, 0);
					viewHolder.modifiers.setTextSize(14);

					if (!orderMenuDetail.isHavingAnswer().equalsIgnoreCase("")) {
						viewHolder.modifiers.setText(""
								+ orderMenuDetail.isHavingAnswer());
					} else {
						viewHolder.modifiers.setText("");
						viewHolder.modifiers.setVisibility(View.GONE);
						viewHolder.itemName.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
						viewHolder.itemName.setPadding(0, 10, 0, 0);
					}
				} catch (Exception ex1) {
				}
			} else {
				try {
					viewHolder.modifiers.setText("");
					viewHolder.modifiers.setVisibility(View.GONE);
					viewHolder.itemName.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
					viewHolder.itemName.setPadding(0, 10, 0, 0);
				} catch (Exception ex1) {
				}
				viewHolder.itemName.setText("" + orderMenuDetail.getItemName());
			}

			viewHolder.price.setText("" + orderMenuDetail.getPrice() + "0");

			// if (orderMenuDetail.getChefNote().equalsIgnoreCase("")) {
			// viewHolder.chefnotes.setText("Click to add chef notes!");
			// } else {
			// viewHolder.chefnotes
			// .setText("" + orderMenuDetail.getChefNote());
			// }

			if (orderMenuDetail.getQuantity() > 0) {
				viewHolder.quantity.setText("" + orderMenuDetail.getQuantity());
			} else {
				// viewHolder.orderQuantity.setText("(0)");
				// HomeActivity.orderedMenuDetails.remove(menuDetails);
				// notifyDataSetChanged();
			}

			if (orderMenuDetail.isEditable()) {
				viewHolder.layoutAdd.setVisibility(View.VISIBLE);
				viewHolder.layoutAddRemoveDivider.setVisibility(View.VISIBLE);
				viewHolder.layoutRemove.setVisibility(View.VISIBLE);
				viewHolder.layoutCancel.setVisibility(View.GONE);
			} else {
				viewHolder.layoutAdd.setVisibility(View.GONE);
				viewHolder.layoutAddRemoveDivider.setVisibility(View.GONE);
				viewHolder.layoutRemove.setVisibility(View.GONE);
				viewHolder.layoutCancel.setVisibility(View.VISIBLE);

				viewHolder.itemName.setAlpha(0.5f);
				viewHolder.quantity.setAlpha(0.5f);
				viewHolder.quantityTxt.setAlpha(0.5f);
				viewHolder.pipe.setAlpha(0.5f);
				viewHolder.price.setAlpha(0.5f);
				viewHolder.priceTxt.setAlpha(0.5f);
			}

			viewHolder.layoutAdd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// blink((ImageView) v);
					orderMenuDetail.setQuantity(orderMenuDetail.getQuantity() + 1);
					orderMenuDetail.setPrice(orderMenuDetail.getQuantity()
							* orderMenuDetail.getItemPrice());

					if (orderMenuDetail.getQuantity() > 1) {
						viewHolder.quantity.setText(""
								+ orderMenuDetail.getQuantity());
						viewHolder.price.setText(""
								+ orderMenuDetail.getPrice() + "0");
					} else {
						// viewHolder.orderQuantity.setText("(0)");
					}

					// System.out.println(orderMenuDetail.getOrderMenuId());

					updateItem(orderMenuDetail);

					// HomeActivity.menuAdapter.notifyDataSetChanged();
				}
			});

			viewHolder.layoutRemove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// blink((ImageView) v);
					if (orderMenuDetail.getQuantity() > 1) {
						orderMenuDetail.setQuantity(orderMenuDetail
								.getQuantity() - 1);
						if (orderMenuDetail.getQuantity() > 0) {

							orderMenuDetail.setPrice(orderMenuDetail
									.getQuantity()
									* orderMenuDetail.getItemPrice());

							viewHolder.quantity.setText(""
									+ orderMenuDetail.getQuantity());
							viewHolder.price.setText(""
									+ orderMenuDetail.getPrice() + "0");

							updateItem(orderMenuDetail);
						} else {
							// menuDetails.setOrderedItemId(0);
							// HomeActivity.orderedMenuDetails.remove(menuDetails);
							// notifyDataSetChanged();

							removeItem(orderMenuDetail);
						}
					} else {
						// menuDetails.setOrderedItemId(0);
						// HomeActivity.orderedMenuDetails.remove(menuDetails);
						// notifyDataSetChanged();

						removeItem(orderMenuDetail);
					}

					// menuDetails.setOrderMasterDetails(orderMasterDetails);

					// MenuActivity.menuListArray.set(MenuActivity.menuListArray.indexOf(menuDetails),
					// menuDetails);

					// HomeActivity.menuAdapter.notifyDataSetChanged();
				}
			});

			viewHolder.layoutCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// removeAllItems();
					iDialog.voidItem(context, orderMenuDetail);
				}
			});
			
			viewHolder.chefnotes.setText(OrderNewActivity3.chefnotes);
			
			viewHolder.chefnotes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					iDialog.chefNote(context, viewHolder.chefnotes);
				}
			});

			// convertView.setOnClickListener(new View.OnClickListener() {
			// @SuppressLint("NewApi")
			// @Override
			// public void onClick(final View layoutContent) {
			//
			// }
			// });

		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;

		// if (convertView == null) {
		//
		// if (inflater == null) {
		// inflater = LayoutInflater.from(parent.getContext());
		// }
		//
		// convertView = inflater.inflate(R.layout.order_list_new_item, null);
		// }
		//
		// LinearLayout layoutListItem = (LinearLayout) convertView
		// .findViewById(R.id.layoutListItems);
		// RelativeLayout content = (RelativeLayout) convertView
		// .findViewById(R.id.content);
		//
		// RelativeLayout imgBgLayout = (RelativeLayout) convertView
		// .findViewById(R.id.imgBgLayout);
		// ImageView imgBg = (ImageView) convertView.findViewById(R.id.imgBg);
		//
		// if (index == 0) {
		// android.widget.AbsListView.LayoutParams layoutParams = new
		// android.widget.AbsListView.LayoutParams(
		// LinearLayout.LayoutParams.MATCH_PARENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT);
		// layoutListItem.setLayoutParams(layoutParams);
		// layoutListItem.getLayoutParams().height =
		// OrderNewActivity3.screenHeight;
		// layoutListItem.setBackgroundColor(Color.TRANSPARENT);
		// imgBgLayout.setVisibility(View.INVISIBLE);
		// content.setVisibility(View.GONE);
		//
		// imgBgLayout.getLayoutParams().height =
		// OrderNewActivity3.screenHeight;
		// imgBg.getLayoutParams().height = OrderNewActivity3.screenHeight;
		//
		// } else {
		// android.widget.AbsListView.LayoutParams layoutParams = new
		// android.widget.AbsListView.LayoutParams(
		// LinearLayout.LayoutParams.MATCH_PARENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT);
		// layoutListItem.setLayoutParams(layoutParams);
		// //layoutListItem.getLayoutParams().height =
		// android.widget.AbsListView.LayoutParams.WRAP_CONTENT;
		//
		// // viewHolder.layoutListItem.setBackgroundColor(Color.BLACK);
		// layoutListItem.setBackgroundColor(Color.WHITE);
		// // viewHolder.layoutListItem.setBackgroundColor(Color
		// // .parseColor(itemsColor[index]));
		// imgBgLayout.setVisibility(View.GONE);
		// content.setVisibility(View.VISIBLE);
		//
		// // viewHolder.item.setTextColor(Color.parseColor(itemsColor[index]));
		// }
		//
		//
		// final OrderMenuDetails orderMenuDetail = listArray.get(index);
		//
		// TextView itemName = (TextView)
		// convertView.findViewById(R.id.itemName);
		// itemName.setText("" + orderMenuDetail.getItemName());
		//
		// itemName.setTypeface(font);
		//
		//
		// TextView quantity = (TextView)
		// convertView.findViewById(R.id.quantity);
		// TextView price = (TextView) convertView.findViewById(R.id.price);
		//
		//
		// TextView desc = (TextView) convertView.findViewById(R.id.desc);
		//
		//
		// final ImageView layoutAdd = (ImageView) convertView
		// .findViewById(R.id.layoutAdd);
		//
		// final View layoutAddRemoveDivider = (View) convertView
		// .findViewById(R.id.layoutAddRemoveDivider);
		//
		// final ImageView layoutRemove = (ImageView) convertView
		// .findViewById(R.id.layoutRemove);
		//
		// //final EditText chefNotes = (EditText)
		// convertView.findViewById(R.id.chefNotes);
		//
		// LinearLayout chefNotesLayout = (LinearLayout)
		// convertView.findViewById(R.id.chefNotesLayout);
		//
		// //if(isLastChild){
		// // chefNotesLayout.setVisibility(View.VISIBLE);
		// //} else {
		// chefNotesLayout.setVisibility(View.GONE);
		// //}
		//
		// layoutAdd.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// layoutAddRemoveDivider.setVisibility(View.VISIBLE);
		// layoutRemove.setVisibility(View.VISIBLE);
		// }
		// });
		//
		// layoutRemove.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// layoutAddRemoveDivider.setVisibility(View.GONE);
		// layoutRemove.setVisibility(View.GONE);
		// }
		// });
		//
		// convertView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View view) {
		// Toast.makeText(activity, child.get(childPosition),
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		//
		// return convertView;

		// try {
		// final ViewHolder viewHolder;
		//
		// if (view == null) {
		//
		// if (inflater == null) {
		// inflater = LayoutInflater.from(parent.getContext());
		// }
		//
		// view = inflater
		// .inflate(R.layout.order_list_item, parent, false);
		//
		// viewHolder = new ViewHolder();
		// viewHolder.imgremove = (ImageView) view
		// .findViewById(R.id.imgremove);
		// viewHolder.imgadd = (ImageView) view.findViewById(R.id.imgadd);
		//
		// viewHolder.itemName = (TextView) view
		// .findViewById(R.id.itemName);
		// viewHolder.quantity = (TextView) view
		// .findViewById(R.id.quantity);
		// viewHolder.price = (TextView) view.findViewById(R.id.price);
		// viewHolder.chefnotes = (TextView) view
		// .findViewById(R.id.chefnotes);
		//
		// viewHolder.itemName.setTypeface(font);
		// viewHolder.quantity.setTypeface(font);
		// viewHolder.price.setTypeface(font);
		// viewHolder.chefnotes.setTypeface(font);
		//
		// view.setTag(viewHolder);
		// } else {
		// viewHolder = (ViewHolder) view.getTag();
		// }
		//
		// final OrderMenuDetails orderMenuDetail = listArray.get(index);
		//
		// viewHolder.itemName.setText("" + orderMenuDetail.getItemName());
		// viewHolder.price.setText("" + orderMenuDetail.getPrice());
		//
		// viewHolder.chefnotes.setText("" + orderMenuDetail.getChefNote());
		//
		// if (orderMenuDetail.getChefNote() != "") {
		// viewHolder.chefnotes.setVisibility(View.VISIBLE);
		// } else {
		// viewHolder.chefnotes.setVisibility(View.GONE);
		// }
		//
		// if (orderMenuDetail.getQuantity() > 0) {
		// viewHolder.quantity.setText("(" + orderMenuDetail.getQuantity()
		// + ")");
		// } else {
		// // viewHolder.orderQuantity.setText("(0)");
		// // HomeActivity.orderedMenuDetails.remove(menuDetails);
		// // notifyDataSetChanged();
		// }
		//
		//
		//
		//
		// viewHolder.imgadd.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// blink((ImageView) v);
		// orderMenuDetail.setQuantity(orderMenuDetail.getQuantity() + 1);
		// orderMenuDetail.setPrice(orderMenuDetail.getQuantity()
		// * orderMenuDetail.getItemPrice());
		//
		// if (orderMenuDetail.getQuantity() > 1) {
		// viewHolder.quantity.setText("("
		// + orderMenuDetail.getQuantity() + ")");
		// viewHolder.price.setText(""
		// + orderMenuDetail.getPrice() + "0");
		// } else {
		// // viewHolder.orderQuantity.setText("(0)");
		// }
		//
		// updateItem(orderMenuDetail);
		//
		// HomeActivity.menuAdapter.notifyDataSetChanged();
		// }
		// });
		//
		// viewHolder.imgremove.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// blink((ImageView) v);
		// if (orderMenuDetail.getQuantity() > 1) {
		// orderMenuDetail.setQuantity(orderMenuDetail
		// .getQuantity() - 1);
		// if (orderMenuDetail.getQuantity() > 1) {
		//
		// orderMenuDetail.setPrice(orderMenuDetail
		// .getQuantity()
		// * orderMenuDetail.getItemPrice());
		//
		// viewHolder.quantity.setText("("
		// + orderMenuDetail.getQuantity() + ")");
		// viewHolder.price.setText(""
		// + orderMenuDetail.getPrice() + "0");
		//
		// updateItem(orderMenuDetail);
		// } else {
		// // menuDetails.setOrderedItemId(0);
		// // HomeActivity.orderedMenuDetails.remove(menuDetails);
		// // notifyDataSetChanged();
		//
		// removeItem(orderMenuDetail);
		// }
		// } else {
		// // menuDetails.setOrderedItemId(0);
		// // HomeActivity.orderedMenuDetails.remove(menuDetails);
		// // notifyDataSetChanged();
		//
		// removeItem(orderMenuDetail);
		// }
		//
		// // menuDetails.setOrderMasterDetails(orderMasterDetails);
		//
		// //
		// MenuActivity.menuListArray.set(MenuActivity.menuListArray.indexOf(menuDetails),
		// // menuDetails);
		//
		// HomeActivity.menuAdapter.notifyDataSetChanged();
		// }
		// });
		//
		// view.setOnClickListener(new View.OnClickListener() {
		// @SuppressLint("NewApi")
		// @Override
		// public void onClick(final View layoutContent) {
		// IDialog iDialog = new IDialog();
		// iDialog.chefNote(context, orderMenuDetail,
		// viewHolder.chefnotes);
		// }
		// });
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// return view;
	}

	// private void fadeIn(ImageView imageView) {
	// Animation animation = AnimationUtils.loadAnimation(context,
	// R.anim.fade_in);
	// imageView.startAnimation(animation);
	// imageView.setVisibility(View.VISIBLE);
	// }
	//
	// private void blink(final ImageView imageView) {
	// Animation animation = AnimationUtils.loadAnimation(context,
	// R.anim.fade_out);
	// imageView.startAnimation(animation);
	// animation.setAnimationListener(new AnimationListener() {
	//
	// @Override
	// public void onAnimationStart(Animation animation) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	// // TODO Auto-generated method stub
	// imageView.setVisibility(View.GONE);
	// fadeIn(imageView);
	// }
	// });
	// }
}
