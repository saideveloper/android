package com.restaurant.adapter;

import java.sql.SQLException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.restaurant.app.HomeActivity;
import com.restaurant.app.R;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.OrderMenuDetails;
import com.restaurant.util.IDialog;

public class OrderNewAdapter extends BaseAdapter {
	private Activity context;
	private LayoutInflater inflater;

	private List<OrderMenuDetails> listArray;

	Typeface font;

	public OrderNewAdapter(Activity context) {
		this.context = context;

		font = Typeface.createFromAsset(context.getAssets(),
				"Roboto-Regular.ttf");

		// listArray = new ArrayList<OrderMenuDetails>();

		try {
			listArray = DatabaseManager.getInstance().getOrderMenuDetailsDao()
					.queryBuilder().distinct().query();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// String[] bob = { "this", "is", "a", "really", "silly", "list" };
		// Set<String> silly = new HashSet<String>(Arrays.asList(bob));
		// boolean isSilly = silly.contains("silly");
	}

	@Override
	public int getCount() {
		return listArray.size();
	}

	@Override
	public Object getItem(int i) {
		return listArray.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void updateItem(OrderMenuDetails orderMenuDetail) {
		try {
			DatabaseManager.getInstance().getOrderMenuDetailsDao()
					.update(orderMenuDetail);
			listArray = DatabaseManager.getInstance().getOrderMenuDetailsDao()
					.queryBuilder().distinct().query();
			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeItem(OrderMenuDetails orderMenuDetail) {
		try {
			DatabaseManager.getInstance().getOrderMenuDetailsDao()
					.delete(orderMenuDetail);
			listArray = DatabaseManager.getInstance().getOrderMenuDetailsDao()
					.queryBuilder().distinct().query();
			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// try {
		// System.out.println(HomeActivity.orderedMenuDetails.size());
		// final MenuDetails menuDetails = HomeActivity.orderedMenuDetails
		// .get(i);
		// menuDetails.setOrderedItemId(0);
		// HomeActivity.orderedMenuDetails
		// .remove(HomeActivity.orderedMenuDetails.get(i));
		// notifyDataSetChanged();
		// HomeActivity.menuAdapter.notifyDataSetChanged();
		// } catch (Exception e) {
		// System.err.println(e);
		// }
	}

	@Override
	public View getView(final int index, View view, final ViewGroup parent) {

		try {
			final ViewHolder viewHolder;

			if (view == null) {

				if (inflater == null) {
					inflater = LayoutInflater.from(parent.getContext());
				}

				view = inflater
						.inflate(R.layout.order_list_item, parent, false);

				viewHolder = new ViewHolder();
				viewHolder.imgremove = (ImageView) view
						.findViewById(R.id.imgremove);
				viewHolder.imgadd = (ImageView) view.findViewById(R.id.imgadd);

				viewHolder.itemName = (TextView) view
						.findViewById(R.id.itemName);
				viewHolder.quantity = (TextView) view
						.findViewById(R.id.quantity);
				viewHolder.price = (TextView) view.findViewById(R.id.price);
				viewHolder.chefnotes = (TextView) view
						.findViewById(R.id.chefnotes);

				viewHolder.itemName.setTypeface(font);
				viewHolder.quantity.setTypeface(font);
				viewHolder.price.setTypeface(font);
				viewHolder.chefnotes.setTypeface(font);

				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			final OrderMenuDetails orderMenuDetail = listArray.get(index);

			viewHolder.itemName.setText("" + orderMenuDetail.getItemName());
			viewHolder.price.setText("" + orderMenuDetail.getPrice());

			viewHolder.chefnotes.setText("" + orderMenuDetail.getChefNote());

			if (orderMenuDetail.getChefNote() != "") {
				viewHolder.chefnotes.setVisibility(View.VISIBLE);
			} else {
				viewHolder.chefnotes.setVisibility(View.GONE);
			}

			if (orderMenuDetail.getQuantity() > 0) {
				viewHolder.quantity.setText("(" + orderMenuDetail.getQuantity()
						+ ")");
			} else {
				// viewHolder.orderQuantity.setText("(0)");
				// HomeActivity.orderedMenuDetails.remove(menuDetails);
				// notifyDataSetChanged();
			}

			viewHolder.imgadd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					blink((ImageView) v);
					orderMenuDetail.setQuantity(orderMenuDetail.getQuantity() + 1);
					orderMenuDetail.setPrice(orderMenuDetail.getQuantity()
							* orderMenuDetail.getItemPrice());

					if (orderMenuDetail.getQuantity() > 1) {
						viewHolder.quantity.setText("("
								+ orderMenuDetail.getQuantity() + ")");
						viewHolder.price.setText(""
								+ orderMenuDetail.getPrice() + "0");
					} else {
						// viewHolder.orderQuantity.setText("(0)");
					}

					updateItem(orderMenuDetail);

					HomeActivity.menuAdapter.notifyDataSetChanged();
				}
			});

			viewHolder.imgremove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					blink((ImageView) v);
					if (orderMenuDetail.getQuantity() > 1) {
						orderMenuDetail.setQuantity(orderMenuDetail
								.getQuantity() - 1);
						if (orderMenuDetail.getQuantity() > 1) {

							orderMenuDetail.setPrice(orderMenuDetail
									.getQuantity()
									* orderMenuDetail.getItemPrice());

							viewHolder.quantity.setText("("
									+ orderMenuDetail.getQuantity() + ")");
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

					HomeActivity.menuAdapter.notifyDataSetChanged();
				}
			});

			view.setOnClickListener(new View.OnClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onClick(final View layoutContent) {
//					IDialog iDialog = new IDialog();
//					iDialog.chefNote(context, orderMenuDetail,
//							viewHolder.chefnotes);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		return view;
	}

	class ViewHolder {
		// View ycut;
		ImageView imgremove, imgadd;
		TextView itemName, chefnotes, quantity, price;
	}

	private void fadeIn(ImageView imageView) {
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.fade_in);
		imageView.startAnimation(animation);
		imageView.setVisibility(View.VISIBLE);
	}

	private void blink(final ImageView imageView) {
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.fade_out);
		imageView.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				imageView.setVisibility(View.GONE);
				fadeIn(imageView);
			}
		});
	}
}
