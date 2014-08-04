package com.restaurant.adapter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
import com.restaurant.app.MenuActivity;
import com.restaurant.app.R;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.OrderMasterDetails;
import com.restaurant.util.IDialog;

public class OrderAdapter extends BaseAdapter {
	private Activity context;
	private LayoutInflater inflater;

	// private ArrayList<MenuDetails> listArray;

	Typeface font;

	public OrderAdapter(Activity context) {
		this.context = context;

		font = Typeface.createFromAsset(context.getAssets(),
				"Roboto-Regular.ttf");

		// String[] bob = { "this", "is", "a", "really", "silly", "list" };
		// Set<String> silly = new HashSet<String>(Arrays.asList(bob));
		// boolean isSilly = silly.contains("silly");
	}

	@Override
	public int getCount() {
		return HomeActivity.orderedMenuDetails.size();
	}

	@Override
	public Object getItem(int i) {
		return HomeActivity.orderedMenuDetails.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void removeItem(int i) {
		try {
			System.out.println(HomeActivity.orderedMenuDetails.size());
			final MenuDetails menuDetails = HomeActivity.orderedMenuDetails
					.get(i);
			menuDetails.setOrderedItemId(0);
			HomeActivity.orderedMenuDetails
					.remove(HomeActivity.orderedMenuDetails.get(i));
			notifyDataSetChanged();
			HomeActivity.menuAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			System.err.println(e);
		}
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

			final MenuDetails menuDetails = HomeActivity.orderedMenuDetails
					.get(index);

			final OrderMasterDetails orderMasterDetails = menuDetails
					.getOrderMasterDetails();

			viewHolder.itemName.setText("" + menuDetails.getItemName());
			viewHolder.price.setText("" + orderMasterDetails.getQuantity()
					* menuDetails.getPrice() + "0");

			try {
				System.err.println("chefnotes "
						+ orderMasterDetails.getChefnotes());
				viewHolder.chefnotes.setText(""
						+ orderMasterDetails.getChefnotes());
			} catch (Exception ex) {
				System.err.println("chefnotes error");
				ex.printStackTrace();
			}

			if (orderMasterDetails.getChefnotes() != "") {
				viewHolder.chefnotes.setVisibility(View.VISIBLE);
			} else {
				viewHolder.chefnotes.setVisibility(View.GONE);
			}

			if (orderMasterDetails.getQuantity() > 0) {
				viewHolder.quantity.setText("("
						+ orderMasterDetails.getQuantity() + ")");
			} else {
				// viewHolder.orderQuantity.setText("(0)");
				HomeActivity.orderedMenuDetails.remove(menuDetails);
				notifyDataSetChanged();
			}

			viewHolder.imgadd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					blink((ImageView) v);
					orderMasterDetails.setQuantity(orderMasterDetails
							.getQuantity() + 1);
					orderMasterDetails.setPrice(orderMasterDetails
							.getQuantity() * menuDetails.getPrice());

					if (orderMasterDetails.getQuantity() > 0) {
						viewHolder.quantity.setText("("
								+ orderMasterDetails.getQuantity() + ")");
						viewHolder.price.setText(""
								+ orderMasterDetails.getPrice() + "0");
					} else {
						// viewHolder.orderQuantity.setText("(0)");
					}

					// menuDetails.setOrderMasterDetails(orderMasterDetails);

					// MenuActivity.menuListArray.set(MenuActivity.menuListArray.indexOf(menuDetails),
					// menuDetails);

					HomeActivity.menuAdapter.notifyDataSetChanged();
				}
			});

			viewHolder.imgremove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					blink((ImageView) v);
					if (orderMasterDetails.getQuantity() > 0) {
						orderMasterDetails.setQuantity(orderMasterDetails
								.getQuantity() - 1);
						if (orderMasterDetails.getQuantity() > 0) {

							orderMasterDetails.setPrice(orderMasterDetails
									.getQuantity() * menuDetails.getPrice());

							viewHolder.quantity.setText("("
									+ orderMasterDetails.getQuantity() + ")");
							viewHolder.price.setText(""
									+ orderMasterDetails.getPrice() + "0");
						} else {
							// viewHolder.orderQuantity.setText("(0)");
							// fadeOut(viewHolder.imgremove);
							// fadeOut(viewHolder.imgadd);
							menuDetails.setOrderedItemId(0);
							HomeActivity.orderedMenuDetails.remove(menuDetails);
							notifyDataSetChanged();
						}
					} else {
						// fadeOut(viewHolder.imgremove);
						// fadeOut(viewHolder.imgadd);
						menuDetails.setOrderedItemId(0);
						HomeActivity.orderedMenuDetails.remove(menuDetails);
						notifyDataSetChanged();
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
//					iDialog.chefNote(context,
//							menuDetails.getOrderMasterDetails(),
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
