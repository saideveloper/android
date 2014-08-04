package com.restaurant.adapter;

import java.util.ArrayList;
import java.util.List;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.restaurant.anim.ResizeAnimation;
import com.restaurant.app.HomeActivity;
import com.restaurant.app.MenuActivity;
import com.restaurant.app.R;
import com.restaurant.dao.DatabaseManager;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.OrderMenuDetails;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;

@SuppressLint("NewApi")
public class MenuNewAdapter extends BaseAdapter {
	private Activity context;
	private int swidth = 0;
	private boolean isImagePresent = true, isDescriptionPresent = true;
	private int listItemHeight = 0;

	private LayoutInflater inflater;

	Typeface font;

	private AnimateOrderIcon animateOrderIconListener;

	CharSequence[] modifiers;
	public static Double modifierPrc = 0.0, orgModifierPrc = 0.0;

	IDialog iDialog = new IDialog();

	public interface AnimateOrderIcon {
		void animate();
	}

	public MenuNewAdapter(Activity context, int swidth,
			List<MenuDetails> menuDetailsList,
			AnimateOrderIcon animateOrderIconListener) {
		this.context = context;
		HomeActivity.menuListArray = new ArrayList<MenuDetails>();

		this.swidth = swidth;

		this.animateOrderIconListener = animateOrderIconListener;

		font = Typeface.createFromAsset(context.getAssets(),
				"Roboto-Regular.ttf");

		boolean firstime = true;

		ArrayList<String> distinctItemName = new ArrayList<String>();

		for (MenuDetails menuDetail : menuDetailsList) {

			menuDetail.setOrderedItemId(0);
			menuDetail.setQuantity(1);
			menuDetail.setYetToView(true);
			menuDetail.setFirstView(0);
			menuDetail.setToggleView(true);

			Bitmap sourceBitmap = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.seafood);
			menuDetail.setItemImageCircle(getCroppedBitmap(sourceBitmap));

			sourceBitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.overlay_right_tick);
			menuDetail.setItemImageHoverCircle(getCroppedBitmap(sourceBitmap));

			menuDetail.setItemImageHover(View.GONE);

			boolean allow = true;

			if (firstime) {
				firstime = false;
				HomeActivity.menuListArray.add(menuDetail);
			} else {
				System.out
						.println("distinctItemName.contains(menuDetail.getItemName()) "
								+ distinctItemName.contains(menuDetail
										.getItemName()));
				if (distinctItemName.contains(menuDetail.getItemName())) {
					allow = false;
				}
			}

			if (allow) {
				HomeActivity.menuListArray.add(menuDetail);
				distinctItemName.add(menuDetail.getItemName());
			}
		}
	}

	@Override
	public int getCount() {
		return HomeActivity.menuListArray.size(); // total number of elements in
													// the list
	}

	@Override
	public Object getItem(int index) {
		return HomeActivity.menuListArray.get(index); // single item in the list
	}

	@Override
	public long getItemId(int index) {
		return index; // index number
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int index, View view, final ViewGroup parent) {

		try {
			final ViewHolder viewHolder;

			if (view == null) {
				if (inflater == null) {
					inflater = LayoutInflater.from(parent.getContext());
				}

				view = inflater.inflate(R.layout.menu_list_item, parent, false);

				viewHolder = new ViewHolder();

				viewHolder.layoutListItem = (RelativeLayout) view
						.findViewById(R.id.layoutListItem);

				viewHolder.imgBg = (ImageView) view.findViewById(R.id.imgBg);
				viewHolder.frameLayoutContent = (FrameLayout) view
						.findViewById(R.id.frameLayoutContent);
				viewHolder.layoutReview = (LinearLayout) view
						.findViewById(R.id.layoutReview);

				viewHolder.itemImage = (ImageView) view
						.findViewById(R.id.itemImage);
				viewHolder.itemImageHover = (ImageView) view
						.findViewById(R.id.itemImageHover);

				viewHolder.itemName = (TextView) view
						.findViewById(R.id.itemName);
				viewHolder.description = (TextView) view
						.findViewById(R.id.description);
				viewHolder.orderdetails = (TextView) view
						.findViewById(R.id.orderdetails);

				viewHolder.quantity = (TextView) view
						.findViewById(R.id.quantity);
				viewHolder.price = (TextView) view.findViewById(R.id.price);
				viewHolder.priceRight = (TextView) view
						.findViewById(R.id.priceRight);

				viewHolder.itemName.setTypeface(font);
				viewHolder.description.setTypeface(font);
				viewHolder.orderdetails.setTypeface(font);

				viewHolder.quantity.setTypeface(font);
				viewHolder.price.setTypeface(font);
				viewHolder.priceRight.setTypeface(font);

				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			final MenuDetails menuDetails = HomeActivity.menuListArray
					.get(index);

			viewHolder.itemName.setText("" + menuDetails.getItemName());

			if (menuDetails.getModifierStatus().equalsIgnoreCase("y")
					&& menuDetails.getOrderedItemId() == 0) {
				viewHolder.orderdetails.setVisibility(View.GONE);
			} else {
				viewHolder.orderdetails.setVisibility(View.VISIBLE);
			}

			viewHolder.description.setText("" + menuDetails.getDescription());

			viewHolder.ratings.setText("" + menuDetails.getAvgRatings());
			viewHolder.likes.setText("" + menuDetails.getTotalLikes());
			viewHolder.reviews.setText("" + menuDetails.getTotalReviews());

			viewHolder.imgBgLayout = (RelativeLayout) view
					.findViewById(R.id.imgBgLayout);

			viewHolder.itemImage.setImageBitmap(menuDetails
					.getItemImageCircle());
			viewHolder.itemImageHover.setImageBitmap(menuDetails
					.getItemImageHoverCircle());
			viewHolder.itemImageHover.setVisibility(menuDetails
					.getItemImageHover());

			if (!isImagePresent) {
				viewHolder.itemImage.setVisibility(View.GONE);
				viewHolder.layoutDescOrd.setTranslationX(0);
			}

			if (!isDescriptionPresent) {
				viewHolder.layoutDescOrd.setVisibility(View.GONE);
			}

			viewHolder.priceRight.setText("Rs " + menuDetails.getPrice() + "0");

			if (index == 0) {
				viewHolder.layoutListItem.getLayoutParams().height = MenuActivity.screenHeight;
				viewHolder.layoutListItem.setBackgroundColor(Color.TRANSPARENT);
				viewHolder.imgBgLayout.setVisibility(View.INVISIBLE);
				viewHolder.frameLayoutContent.setVisibility(View.GONE);
				viewHolder.layoutReview.setVisibility(View.GONE);

				viewHolder.imgBg.getLayoutParams().height = MenuActivity.screenHeight;
				viewHolder.imgBgLayout.getLayoutParams().height = MenuActivity.screenHeight;

				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});
			} else {
				viewHolder.layoutListItem.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;

				viewHolder.layoutListItem.setBackgroundColor(Color.WHITE);

				viewHolder.imgBgLayout.setVisibility(View.GONE);
				viewHolder.frameLayoutContent.setVisibility(View.VISIBLE);
				viewHolder.layoutReview.setVisibility(View.VISIBLE);
			}

			if (menuDetails.isYetToView())
				menuDetails.setYetToView(false);
		} catch (Exception e) {
			System.err.println(e);
		}

		return view;
	}

	private class ViewHolder {
		RelativeLayout imgBgLayout;
		ImageView itemImage, itemImageHover, imgBg;
		TextView itemName, description, orderdetails, quantity, price,
				priceRight, ratings, likes, reviews;
		LinearLayout layoutDescOrd, layoutQtyPrc, layoutReview,
				layoutChefnotes;
		RelativeLayout layoutListItem, layoutAddEditRemove, layoutAdd,
				layoutRemove;
		FrameLayout frameLayoutContent;
	}

	private Bitmap getCroppedBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
}
