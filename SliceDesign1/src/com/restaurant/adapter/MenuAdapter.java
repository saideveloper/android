package com.restaurant.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.restaurant.model.ModifierQuestionAnswer;
import com.restaurant.model.OrderMenuDetails;
import com.restaurant.util.IConstants;
import com.restaurant.util.IDialog;

@SuppressLint("NewApi")
public class MenuAdapter extends BaseAdapter {
	private Activity context;
	private int swidth = 0;
	private boolean isImagePresent = true, isDescriptionPresent = true;
	private int listItemHeight = 0;

	private LayoutInflater inflater;
	// private ArrayList<MenuDetails> listArray;

	Typeface font;

	private AnimateOrderIcon animateOrderIconListener;

	CharSequence[] modifiers;
	public static Double modifierPrc = 0.0, orgModifierPrc = 0.0;

	// public static Map<String, ModifierQuestionAnswer> modifierAddedItems =
	// new HashMap<String, ModifierQuestionAnswer>();
	// public static HashMap<String, ModifierQuestionAnswer>
	// modifierAddedItemsStack = new HashMap<String, ModifierQuestionAnswer>();
	// // public static Map<String, Map<String, ModifierQuestionAnswer>>
	// // modifierAddedItemsStack = new HashMap<String, Map<String,
	// // ModifierQuestionAnswer>>();
	//
	// //public static ArrayList<ModifierQuestionAnswer>
	// modifierQuestionAnswersChoosedItems = new
	// ArrayList<ModifierQuestionAnswer>();

	IDialog iDialog = new IDialog();

	// // prepare a gray filter setting saturation to 0, or ...
	// ColorMatrix cm = new ColorMatrix();
	//
	// Paint paint = new Paint();
	// ColorFilter colorFilter;

	// List<String> groupList;
	// List<String> childList;
	// Map<String, List<String>> laptopCollection;
	// ExpandableListView expListView;

	public interface AnimateOrderIcon {
		void animate();
	}

	public MenuAdapter(Activity context, int swidth,
			List<MenuDetails> menuDetailsList,
			AnimateOrderIcon animateOrderIconListener) {
		this.context = context;
		HomeActivity.menuListArray = new ArrayList<MenuDetails>();

		this.swidth = swidth;

		this.animateOrderIconListener = animateOrderIconListener;

		font = Typeface.createFromAsset(context.getAssets(),
				"Roboto-Regular.ttf");

		// IConstants.showToast("IConstants.appItemCategory " +
		// IConstants.appItemCategory);
		// System.out.println("IConstants.appItemCategory " +
		// IConstants.appItemCategory);

		// cm.setSaturation(0);
		//
		// colorFilter = new ColorMatrixColorFilter(cm);
		//
		// // ... prepare a color filter
		// // ColorFilter filter = new PorterDuffColorFilter(Color.rgb(34, 136,
		// // 201), PorterDuff.Mode.OVERLAY);
		// colorFilter = new PorterDuffColorFilter(Color.parseColor("#3399cc"),
		// PorterDuff.Mode.OVERLAY);
		//
		// // create paint
		// paint.setColorFilter(colorFilter);

		boolean firstime = true;

		ArrayList<String> distinctItemName = new ArrayList<String>();

		for (MenuDetails menuDetail : menuDetailsList) {

			// if
			// (!menuDetail.getItemCategory().contains(IConstants.appItemCategory))
			// {
			// continue;
			// }

			System.out.println("itemName " + menuDetail.getItemName());
			System.out.println("ItemId " + menuDetail.getItemId());
			System.out.println("ItemCategory " + menuDetail.getItemCategory());
			// System.out.println("menuDetail.getItemCategory().contains(itemCategory) "
			// +
			// menuDetail.getItemCategory().contains(IConstants.appItemCategory));

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

			// System.out.println(MenuActivity.menuListArray.size());
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

				// viewHolder.nameUnderLine = (View) view
				// .findViewById(R.id.nameUnderLine);

				// Bitmap sourceBitmap = BitmapFactory.decodeResource(
				// context.getResources(), R.drawable.seafood);
				// sourceBitmap = getResizedBitmap(sourceBitmap, 100, 100);
				// sourceBitmap = getCroppedBitmap(sourceBitmap);
				// viewHolder.itemImage.setImageBitmap(sourceBitmap);

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

				viewHolder.ratings = (TextView) view.findViewById(R.id.ratings);
				viewHolder.likes = (TextView) view.findViewById(R.id.likes);
				viewHolder.reviews = (TextView) view.findViewById(R.id.reviews);

				viewHolder.layoutDescOrd = (LinearLayout) view
						.findViewById(R.id.layoutDescOrd);
				viewHolder.layoutQtyPrc = (LinearLayout) view
						.findViewById(R.id.layoutQtyPrc);

				viewHolder.layoutAddEditRemove = (RelativeLayout) view
						.findViewById(R.id.layoutAddEditRemove);

				viewHolder.layoutAdd = (RelativeLayout) view
						.findViewById(R.id.layoutAdd);

				viewHolder.layoutRemove = (RelativeLayout) view
						.findViewById(R.id.layoutRemove);

				viewHolder.layoutChefnotes = (LinearLayout) view
						.findViewById(R.id.layoutChefnotes);

				viewHolder.itemName.setTypeface(font);
				viewHolder.description.setTypeface(font);
				viewHolder.orderdetails.setTypeface(font);

				viewHolder.quantity.setTypeface(font);
				viewHolder.price.setTypeface(font);
				viewHolder.priceRight.setTypeface(font);

				viewHolder.ratings.setTypeface(font);
				viewHolder.likes.setTypeface(font);
				viewHolder.reviews.setTypeface(font);

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

			// viewHolder.itemImage.setImageBitmap(menuDetails
			// .getItemImageCircle());
			
			viewHolder.imgBgLayout = (RelativeLayout) view
					.findViewById(R.id.imgBgLayout);

			viewHolder.itemImage.setImageBitmap(menuDetails
					.getItemImageCircle());
			viewHolder.itemImageHover.setImageBitmap(menuDetails
					.getItemImageHoverCircle());
			viewHolder.itemImageHover.setVisibility(menuDetails
					.getItemImageHover());

			// viewHolder.itemImage.setColorFilter(colorFilter);

			// try {
			// viewHolder.itemImage.setImageBitmap(putOverlay(menuDetails
			// .getItemImageCircle()));
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

			if (!isImagePresent) {
				viewHolder.itemImage.setVisibility(View.GONE);
				viewHolder.layoutDescOrd.setTranslationX(0);
			}

			if (!isDescriptionPresent) {
				viewHolder.layoutDescOrd.setVisibility(View.GONE);
			}

			// if (menuDetails.getOrderedItemId() != 0) {
			// viewHolder.quantity.setText(""
			// + menuDetails.getOrderMasterDetails().getQuantity());
			// viewHolder.price.setText(""
			// + menuDetails.getOrderMasterDetails().getPrice() + "0");
			//
			// viewHolder.orderdetails.setText("placed - "
			// + menuDetails.getOrderMasterDetails().getQuantity()
			// + " | "
			// + menuDetails.getOrderMasterDetails().getPrice() + "0");
			// } else {
			// viewHolder.quantity.setText("1");
			// viewHolder.price.setText("" + menuDetails.getPrice() + "0");
			// viewHolder.orderdetails.setText("Rs: " + menuDetails.getPrice()
			// + "0");
			// }

			viewOrder(menuDetails, viewHolder);

			viewHolder.priceRight.setText("Rs " + menuDetails.getPrice() + "0");

			// viewHolder.itemImage.getLayoutParams().width = 90;
			// viewHolder.itemImage.getLayoutParams().height = 90;
			//
			// viewHolder.layoutAddEditRemove.getLayoutParams().width = 90;
			// viewHolder.layoutAddEditRemove.getLayoutParams().height = 90;
			//
			// viewHolder.layoutQtyPrc.getLayoutParams().height = 60;

			if (menuDetails.getFirstView() == 1) {
				secondView(viewHolder, 0);
			} else {
				firstView(viewHolder, 0);
			}

			// if (menuDetails.getFirstView() == 0) {
			// if (swidth <= 480) {
			// ObjectAnimator animator = ObjectAnimator.ofFloat(
			// viewHolder.itemName, "translationX", 145);
			// animator.setDuration(0);
			// animator.start();
			// } else {
			// ObjectAnimator animator = ObjectAnimator.ofFloat(
			// viewHolder.itemName, "translationX", 95);
			// animator.setDuration(0);
			// animator.start();
			// }
			// }

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
				// if (swidth <= 480) {
				// viewHolder.frameLayoutContent.getLayoutParams().height = 200;
				// viewHolder.layoutListItem.getLayoutParams().height = 210;
				// viewHolder.itemImage.getLayoutParams().width = 130;
				// viewHolder.itemImage.getLayoutParams().height = 130;
				// viewHolder.layoutQtyPrc.getLayoutParams().height = 100;
				// viewHolder.layoutAddEditRemove.getLayoutParams().width = 138;
				// viewHolder.layoutAddEditRemove.getLayoutParams().height =
				// 138;
				// } else {
				// viewHolder.frameLayoutContent.getLayoutParams().height = 130;
				// viewHolder.layoutListItem.getLayoutParams().height = 140;
				// }

				viewHolder.layoutListItem.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;

				// viewHolder.layoutListItem.setBackgroundColor(Color.BLACK);
				viewHolder.layoutListItem.setBackgroundColor(Color.WHITE);

				viewHolder.imgBgLayout.setVisibility(View.GONE);
				viewHolder.frameLayoutContent.setVisibility(View.VISIBLE);
				viewHolder.layoutReview.setVisibility(View.VISIBLE);
				// }

				if (menuDetails.getModifierStatus().equalsIgnoreCase("n")) {

					viewHolder.layoutAdd
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// toast("layoutAdd");
									if (menuDetails.isToggleView()) {
										menuDetails.setToggleView(false);
										menuDetails.setFirstView(1);

										secondView(viewHolder, 1);

										menuDetails
												.setItemImageHover(View.VISIBLE);
										viewHolder.itemImageHover
												.setVisibility(menuDetails
														.getItemImageHover());

										addToOrder(menuDetails, viewHolder);
									} else {
										animateOrderIconListener.animate();
										blink((View) v);

										addToOrder(menuDetails, viewHolder);

										// if (menuDetails.getOrderedItemId() ==
										// 0) {
										// menuDetails
										// .setOrderedItemId(menuDetails
										// .getItemId());
										//
										// OrderMasterDetails orderMasterDetail
										// = new OrderMasterDetails();
										// orderMasterDetail
										// .setItemId(menuDetails
										// .getItemId());
										// orderMasterDetail
										// .setOrderTitle(menuDetails
										// .getItemName());
										//
										// orderMasterDetail.setQuantity(2);
										//
										// orderMasterDetail.setPrice(orderMasterDetail
										// .getQuantity()
										// * menuDetails.getPrice());
										//
										// orderMasterDetail.setChefnotes("");
										//
										// menuDetails
										// .setOrderMasterDetails(orderMasterDetail);
										//
										// viewHolder.quantity
										// .setText(""
										// + menuDetails
										// .getOrderMasterDetails()
										// .getQuantity());
										// viewHolder.price
										// .setText(""
										// + menuDetails
										// .getOrderMasterDetails()
										// .getPrice()
										// + "0");
										// viewHolder.orderdetails
										// .setText("placed - "
										// + menuDetails
										// .getOrderMasterDetails()
										// .getQuantity()
										// + " | "
										// + menuDetails
										// .getOrderMasterDetails()
										// .getPrice()
										// + "0");
										// } else {
										// OrderMasterDetails orderMasterDetail
										// = menuDetails
										// .getOrderMasterDetails();
										// orderMasterDetail
										// .setQuantity(orderMasterDetail
										// .getQuantity() + 1);
										// orderMasterDetail.setPrice(orderMasterDetail
										// .getQuantity()
										// * menuDetails.getPrice());
										//
										// menuDetails
										// .setOrderMasterDetails(orderMasterDetail);
										//
										// viewHolder.quantity
										// .setText(""
										// + menuDetails
										// .getOrderMasterDetails()
										// .getQuantity());
										// viewHolder.price
										// .setText(""
										// + menuDetails
										// .getOrderMasterDetails()
										// .getPrice()
										// + "0");
										//
										// viewHolder.orderdetails
										// .setText("placed - "
										// + menuDetails
										// .getOrderMasterDetails()
										// .getQuantity()
										// + " | "
										// + menuDetails
										// .getOrderMasterDetails()
										// .getPrice()
										// + "0");
										// }
									}
								}
							});

					viewHolder.layoutRemove
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// toast("layoutRemove");
									if (menuDetails.isToggleView()) {
										menuDetails.setToggleView(false);
										menuDetails.setFirstView(1);

										secondView(viewHolder, 1);

										menuDetails
												.setItemImageHover(View.VISIBLE);
										viewHolder.itemImageHover
												.setVisibility(menuDetails
														.getItemImageHover());
									} else {
										animateOrderIconListener.animate();
										blink((View) v);

										removeOrder(menuDetails, viewHolder);

										// if (menuDetails.getOrderedItemId() !=
										// 0) {
										// OrderMasterDetails orderMasterDetail
										// = menuDetails
										// .getOrderMasterDetails();
										// if (orderMasterDetail.getQuantity() >
										// 1) {
										// orderMasterDetail
										// .setQuantity(orderMasterDetail
										// .getQuantity() - 1);
										// orderMasterDetail.setPrice(orderMasterDetail
										// .getQuantity()
										// * menuDetails
										// .getPrice());
										//
										// if (orderMasterDetail
										// .getQuantity() > 0) {
										// menuDetails
										// .setOrderMasterDetails(orderMasterDetail);
										//
										// viewHolder.quantity
										// .setText(""
										// + menuDetails
										// .getOrderMasterDetails()
										// .getQuantity());
										// viewHolder.price
										// .setText(""
										// + menuDetails
										// .getOrderMasterDetails()
										// .getPrice()
										// + "0");
										// viewHolder.orderdetails
										// .setText("placed - "
										// + menuDetails
										// .getOrderMasterDetails()
										// .getQuantity()
										// + " | "
										// + menuDetails
										// .getOrderMasterDetails()
										// .getPrice()
										// + "0");
										// } else {
										// menuDetails
										// .setOrderedItemId(0);
										// orderMasterDetail
										// .setQuantity(1);
										// orderMasterDetail
										// .setPrice(0);
										//
										// viewHolder.quantity
										// .setText("1");
										// viewHolder.price.setText(""
										// + menuDetails
										// .getPrice()
										// + "0");
										// // viewHolder.orderdetails
										// //
										// .setText("None of this has been placed");
										// viewHolder.orderdetails.setText("Rs: "
										// + menuDetails
										// .getPrice()
										// + "0");
										//
										// menuDetails
										// .setToggleView(true);
										// menuDetails.setFirstView(2);
										//
										// firstView(viewHolder, 1);
										//
										// menuDetails
										// .setItemImageHover(View.GONE);
										// viewHolder.itemImageHover
										// .setVisibility(menuDetails
										// .getItemImageHover());
										// }
										// } else {
										// menuDetails.setOrderedItemId(0);
										// orderMasterDetail
										// .setQuantity(1);
										// orderMasterDetail.setPrice(0);
										//
										// viewHolder.quantity
										// .setText("1");
										// viewHolder.price.setText(""
										// + menuDetails
										// .getPrice()
										// + "0");
										// // viewHolder.orderdetails
										// //
										// .setText("None of this has been placed");
										// viewHolder.orderdetails.setText("Rs: "
										// + menuDetails
										// .getPrice()
										// + "0");
										//
										// menuDetails.setToggleView(true);
										// menuDetails.setFirstView(2);
										//
										// firstView(viewHolder, 1);
										//
										// menuDetails
										// .setItemImageHover(View.GONE);
										// viewHolder.itemImageHover
										// .setVisibility(menuDetails
										// .getItemImageHover());
										// }
										// } else {
										// menuDetails.setToggleView(true);
										// menuDetails.setFirstView(2);
										//
										// firstView(viewHolder, 1);
										//
										// menuDetails
										// .setItemImageHover(View.GONE);
										// viewHolder.itemImageHover.setVisibility(menuDetails
										// .getItemImageHover());
										// }
									}
								}
							});

					viewHolder.layoutChefnotes
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// toast("layoutChefnotes");
									if (menuDetails.isToggleView()) {
										menuDetails.setToggleView(false);
										menuDetails.setFirstView(1);

										secondView(viewHolder, 1);

										menuDetails
												.setItemImageHover(View.VISIBLE);
										viewHolder.itemImageHover
												.setVisibility(menuDetails
														.getItemImageHover());

										addToOrder(menuDetails, viewHolder);
									} else {
										blink((View) v);

										// iDialog.chefNote(
										// context,
										// menuDetails
										// .getOrderMasterDetails(),
										// animateOrderIconListener,
										// menuDetails);

										try {
											OrderMenuDetails orderMenuDetail = DatabaseManager
													.getInstance()
													.getOrderMenuDetailsDao()
													.queryBuilder()
													.distinct()
													.where()
													.eq("itemId",
															menuDetails
																	.getItemId())
													.query().get(0);
											iDialog.chefNote(context,
													orderMenuDetail,
													animateOrderIconListener);
										} catch (Exception e) {
											// TODO: handle exception
										}

										// System.out.println("menuDetail itemId"
										// + menuDetail.getItemId());

										// if (menuDetails.getOrderedItemId() !=
										// 0) {
										// OrderMasterDetails orderMasterDetail
										// = menuDetails
										// .getOrderMasterDetails();
										//
										// if (orderMasterDetail.getQuantity() >
										// 0) {
										//
										// iDialog.chefNote(
										// context,
										// menuDetails
										// .getOrderMasterDetails(),
										// animateOrderIconListener,
										// menuDetails);
										//
										// } else {
										// new MyToast(context,
										// "Please add some quantity first!");
										// }
										// } else {
										// new MyToast(context,
										// "Please add some quantity first!");
										// }
									}
								}
							});

					viewHolder.layoutAddEditRemove
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// toast("layoutAddEditRemove");
									if (menuDetails.isToggleView()) {
										menuDetails.setToggleView(false);
										menuDetails.setFirstView(1);

										secondView(viewHolder, 1);

										menuDetails
												.setItemImageHover(View.VISIBLE);
										viewHolder.itemImageHover
												.setVisibility(menuDetails
														.getItemImageHover());

										addToOrder(menuDetails, viewHolder);
									} else {
										// menuDetails.setToggleView(true);
										// menuDetails.setFirstView(2);
										//
										// firstView(viewHolder, 1);
									}
								}
							});

					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// toast("view");
							if (menuDetails.isToggleView()) {
								menuDetails.setToggleView(false);
								menuDetails.setFirstView(1);

								secondView(viewHolder, 1);

								menuDetails.setItemImageHover(View.VISIBLE);
								viewHolder.itemImageHover
										.setVisibility(menuDetails
												.getItemImageHover());

								addToOrder(menuDetails, viewHolder);
							} else {
								// menuDetails.setToggleView(true);
								// menuDetails.setFirstView(2);
								//
								// firstView(viewHolder, 1);
								//
								// menuDetails.setItemImageHover(View.GONE);
								// viewHolder.itemImageHover
								// .setVisibility(menuDetails
								// .getItemImageHover());
							}
						}
					});

				} else {
					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							try {

								ArrayList<String> hasItemsAlready = new ArrayList<String>();
								final ArrayList<ModifierQuestionAnswer> hasModifierItemsAlready = new ArrayList<ModifierQuestionAnswer>();

								// ---------------------------------------------

								System.err
										.println("view modifierAddedItemsStack");
								// ---------------------------------------------
								for (Entry<String, ModifierQuestionAnswer> entry : HomeActivity.modifierAddedItemsStack
										.entrySet()) {
									String key = entry.getKey();
									ModifierQuestionAnswer modifierQuestionAnswer = entry
											.getValue();
									System.out.println("Key = " + key);
									System.out.println("Values = "
											+ modifierQuestionAnswer
													.getAnswer() + "n");

									String item = "";

									item += ""
											+ modifierQuestionAnswer
													.getItemName();

									item += "("
											+ modifierQuestionAnswer
													.getModifierCategory();

									item += ")\nQuantity : "
											+ modifierQuestionAnswer
													.getQuantity();

									item += "\nRs : "
											+ modifierQuestionAnswer
													.getTotalPrice();

									Boolean allowToAddAnother = true;

									// System.out
									// .println("modifierQuestionAnswer.getAnswer() "
									// + modifierQuestionAnswer
									// .getAnswer());

									if (null != modifierQuestionAnswer
											.getAnswer()
											&& modifierQuestionAnswer
													.getAnswer() != "null") {

										// if(hasItemsAlready.contains(item)){
										// item = "";
										// }

										System.err
												.println("------------------------------------------");
										for (int i = 0; i < hasItemsAlready
												.size(); i++) {
											// System.out.println("item " +
											// item);
											// System.out
											// .println("hasItemsAlready "
											// + hasItemsAlready
											// .get(i));

											if (hasItemsAlready.get(i)
													.contains(item)) {
												hasItemsAlready.set(
														i,
														hasItemsAlready.get(i)
																+ "\n"
																+ modifierQuestionAnswer
																		.getAnswer());
												allowToAddAnother = false;
											}
										}
										System.err
												.println("------------------------------------------");

										// System.out.println("hasItemsAlready.indexOf(item) "
										// + hasItemsAlready.indexOf(item));

										if (allowToAddAnother)
											item += "\n"
													+ modifierQuestionAnswer
															.getAnswer();
									}

									// System.out.println(item);

									if (allowToAddAnother)
										hasItemsAlready.add(item);

									hasModifierItemsAlready
											.add(modifierQuestionAnswer);
								}
								// ---------------------------------------------

								// Iterator myVeryOwnIterator =
								// MenuAdapter.modifierAddedItemsStack
								// .keySet().iterator();
								// while (myVeryOwnIterator.hasNext()) {
								// String key = (String) myVeryOwnIterator
								// .next();
								//
								// //System.out.println("key " + key);
								//
								// ModifierQuestionAnswer modifierQuestionAnswer
								// = MenuAdapter.modifierAddedItemsStack
								// .get(key);
								//
								// String item = "";
								//
								// item += ""
								// + modifierQuestionAnswer
								// .getItemName();
								//
								// item += "("
								// + modifierQuestionAnswer
								// .getModifierCategory();
								//
								// item += ")\nRs : "
								// + modifierQuestionAnswer
								// .getQuantity();
								//
								// item += "\nQuantity : "
								// + modifierQuestionAnswer
								// .getTotalPrice();
								//
								// Boolean allowToAddAnother = true;
								//
								// if(null!=modifierQuestionAnswer.getAnswer()
								// &&
								// modifierQuestionAnswer.getAnswer()!="null"){
								//
								// // if(hasItemsAlready.contains(item)){
								// // item = "";
								// // }
								//
								//
								// System.err.println("------------------------------------------");
								// for (int i = 0; i < hasItemsAlready.size();
								// i++) {
								// System.out.println("item " + item);
								// System.out.println("hasItemsAlready " +
								// hasItemsAlready.get(i));
								//
								// if(hasItemsAlready.get(i).contains(item)){
								// hasItemsAlready.set(i, item + "\n" +
								// modifierQuestionAnswer
								// .getAnswer());
								// allowToAddAnother = false;
								// }
								// }
								// System.err.println("------------------------------------------");
								//
								// //System.out.println("hasItemsAlready.indexOf(item) "
								// + hasItemsAlready.indexOf(item));
								//
								// if(allowToAddAnother)
								// item += "\n"
								// + modifierQuestionAnswer
								// .getAnswer();
								// }
								//
								//
								// //System.out.println(item);
								//
								// if(allowToAddAnother)
								// hasItemsAlready.add(item);
								//
								// hasModifierItemsAlready.add(modifierQuestionAnswer);
								// }
								//
								// //
								// ---------------------------------------------

								if (hasItemsAlready.size() > 0) {

									final CharSequence[] items = new CharSequence[hasItemsAlready
											.size() + 1];

									for (int i = 0; i < hasItemsAlready.size(); i++) {
										items[i] = hasItemsAlready.get(i);

										if (i == hasItemsAlready.size() - 1) {
											items[i + 1] = "Add new";
										}
									}

									AlertDialog.Builder builder = new AlertDialog.Builder(
											IConstants.mContext);
									builder.setTitle("Make your selection");
									builder.setItems(
											items,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int item) {
													// // Do something with the
													// // selection
													// mDoneButton
													// .setText(items[item]);
													if (items[item] == "Add new") {
														iDialog.modifier(menuDetails);
													} else {
														ArrayList<String> hasItemsAlready = new ArrayList<String>();
														for (ModifierQuestionAnswer modifierQuestionAnswer : hasModifierItemsAlready) {

															String itemString = "";

															itemString += ""
																	+ modifierQuestionAnswer
																			.getItemName();

															itemString += "("
																	+ modifierQuestionAnswer
																			.getModifierCategory();

															itemString += ")\nQuantity : "
																	+ modifierQuestionAnswer
																			.getQuantity();

															itemString += "\nRs : "
																	+ modifierQuestionAnswer
																			.getTotalPrice();

															Boolean allowToAddAnother = true;

															// System.out
															// .println("modifierQuestionAnswer.getAnswer() "
															// +
															// modifierQuestionAnswer
															// .getAnswer());

															if (null != modifierQuestionAnswer
																	.getAnswer()
																	&& modifierQuestionAnswer
																			.getAnswer() != "null") {

																// if(hasItemsAlready.contains(item)){
																// item = "";
																// }

																System.err
																		.println("------------------------------------------");
																for (int i = 0; i < hasItemsAlready
																		.size(); i++) {
																	// System.out.println("item "
																	// + item);
																	// System.out
																	// .println("hasItemsAlready "
																	// +
																	// hasItemsAlready
																	// .get(i));

																	if (hasItemsAlready
																			.get(i)
																			.contains(
																					itemString)) {
																		hasItemsAlready
																				.set(i,
																						hasItemsAlready
																								.get(i)
																								+ "\n"
																								+ modifierQuestionAnswer
																										.getAnswer());

																		itemString = hasItemsAlready
																				.get(i);
																		allowToAddAnother = false;
																	}
																}
																System.err
																		.println("------------------------------------------");

																// System.out.println("hasItemsAlready.indexOf(item) "
																// +
																// hasItemsAlready.indexOf(item));

																if (allowToAddAnother)
																	itemString += "\n"
																			+ modifierQuestionAnswer
																					.getAnswer();
															}

															// System.out.println(item);

															if (allowToAddAnother)
																hasItemsAlready
																		.add(itemString);

															// if (null !=
															// modifierQuestionAnswer
															// .getAnswer()) {
															//
															// //
															// if(hasItemsAlready.contains(item)){
															// // item = "";
															// // }
															//
															// //
															// System.out.println("hasItemsAlready.indexOf(item) "
															// // +
															// //
															// hasItemsAlready.indexOf(item));
															//
															// itemString +=
															// "\n"
															// +
															// modifierQuestionAnswer
															// .getAnswer();
															// }

															// System.out
															// .println("items[item] "
															// + items[item]);
															// System.out
															// .println("itemString "
															// + itemString);

															if (itemString
																	.equalsIgnoreCase(items[item]
																			.toString())) {
																iDialog.modifierEdit(
																		menuDetails
																				.getItemId(),
																		modifierQuestionAnswer);
																break;
															}
															// else
															// if(itemString.contains(items[item].toString())){
															// iDialog.modifierEdit(menuDetails.getItemId(),
															// modifierQuestionAnswer);
															// break;
															// }
														}
													}
												}
											});
									AlertDialog alert = builder.create();
									alert.show();
								} else {
									iDialog.modifier(menuDetails);
								}

								// iDialog.modifier(menuDetails.getItemId());

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
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
		// View nameUnderLine;
		TextView itemName, description, orderdetails, quantity, price,
				priceRight, ratings, likes, reviews;
		LinearLayout layoutDescOrd, layoutQtyPrc, layoutReview,
				layoutChefnotes;
		RelativeLayout layoutListItem, layoutAddEditRemove, layoutAdd,
				layoutRemove;
		FrameLayout frameLayoutContent;
	}

	public void viewOrder(MenuDetails menuDetail, ViewHolder viewHolder) {
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

				viewHolder.orderdetails.setText("placed - "
						+ orderMenuDetail.getQuantity() + " | "
						+ orderMenuDetail.getPrice() + "0");

				menuDetail.setItemImageHover(View.VISIBLE);
				viewHolder.itemImageHover.setVisibility(menuDetail
						.getItemImageHover());
				
				menuDetail.setToggleView(false);
				menuDetail.setFirstView(1);
			} else {
				viewHolder.quantity.setText("1");
				viewHolder.price.setText("" + menuDetail.getPrice() + "0");
				// viewHolder.orderdetails
				// .setText("None of this has been placed");
				viewHolder.orderdetails.setText("Rs: " + menuDetail.getPrice()
						+ "0");
				
//				menuDetail.setItemImageHover(View.GONE);
//				viewHolder.itemImageHover.setVisibility(menuDetail
//						.getItemImageHover());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addToOrder(MenuDetails menuDetail, ViewHolder viewHolder) {
		try {
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
				orderMenuDetail.setChefNote("");
				DatabaseManager.getInstance().getOrderMenuDetailsDao()
						.create(orderMenuDetail);
			}

			viewHolder.quantity.setText("" + orderMenuDetail.getQuantity());
			viewHolder.price.setText("" + orderMenuDetail.getPrice() + "0");

			viewHolder.orderdetails.setText("placed - "
					+ orderMenuDetail.getQuantity() + " | "
					+ orderMenuDetail.getPrice() + "0");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeOrder(MenuDetails menuDetail, ViewHolder viewHolder) {
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

					viewHolder.orderdetails.setText("placed - "
							+ orderMenuDetail.getQuantity() + " | "
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

					firstView(viewHolder, 1);

					menuDetail.setItemImageHover(View.GONE);
					viewHolder.itemImageHover.setVisibility(menuDetail
							.getItemImageHover());
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

				firstView(viewHolder, 1);

				menuDetail.setItemImageHover(View.GONE);
				viewHolder.itemImageHover.setVisibility(menuDetail
						.getItemImageHover());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void fadeIn(View imageView) {
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.fade_in);
		imageView.startAnimation(animation);
		imageView.setVisibility(View.VISIBLE);
	}

	private void blink(final View imageView) {
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.fade_out);
		imageView.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				imageView.setVisibility(View.GONE);
				fadeIn(imageView);
			}
		});
	}

	private void firstView(ViewHolder viewHolder, int duration) {
		// ObjectAnimator animator1a = ObjectAnimator.ofFloat(
		// viewHolder.itemImage, "alpha", 1);
		//
		// ObjectAnimator animator1b = ObjectAnimator.ofFloat(
		// viewHolder.itemImage, "translationY", 0);
		//
		// ObjectAnimator animator2;
		//
		// // if (swidth <= 480) {
		// // animator2 = ObjectAnimator.ofFloat(viewHolder.itemName,
		// // "translationX", 145);
		// // } else {
		// // animator2 = ObjectAnimator.ofFloat(viewHolder.itemName,
		// // "translationX", 95);
		// // }
		//
		// if (!isImagePresent) {
		// animator2 = ObjectAnimator.ofFloat(viewHolder.itemName,
		// "translationX", 0);
		// } else {
		// if (swidth < 400) {
		// animator2 = ObjectAnimator.ofFloat(viewHolder.itemName,
		// "translationX", 65);
		// } else {
		// animator2 = ObjectAnimator.ofFloat(viewHolder.itemName,
		// "translationX", 95);
		// }
		// }

		if (!isDescriptionPresent) {
			if (listItemHeight == 0) {
				if (swidth < 500) {
					listItemHeight = 320;
				} else {
					listItemHeight = viewHolder.frameLayoutContent
							.getLayoutParams().height;
				}
			}

			if (swidth < 500) {
				ResizeAnimation animation = new ResizeAnimation(
						viewHolder.frameLayoutContent, listItemHeight,
						IConstants.pxToDp(160), 300);
				viewHolder.frameLayoutContent.startAnimation(animation);
			} else {
				ResizeAnimation animation = new ResizeAnimation(
						viewHolder.frameLayoutContent, listItemHeight,
						IConstants.pxToDp(70), 300);
				viewHolder.frameLayoutContent.startAnimation(animation);
			}

			// viewHolder.frameLayoutContent.getLayoutParams().height = 70;
			// viewHolder.frameLayoutContent.requestLayout();
		}

		// viewHolder.itemName.setPaintFlags(0);

		ObjectAnimator animator3a = ObjectAnimator.ofFloat(
				viewHolder.layoutDescOrd, "alpha", 1);

		ObjectAnimator animator3b = ObjectAnimator.ofFloat(
				viewHolder.layoutDescOrd, "translationY", 0);

		ObjectAnimator animator4a = ObjectAnimator.ofFloat(
				viewHolder.layoutQtyPrc, "alpha", 0);

		ObjectAnimator animator4b = ObjectAnimator.ofFloat(
				viewHolder.layoutQtyPrc, "translationY",
				viewHolder.layoutQtyPrc.getMeasuredHeight());

		ObjectAnimator animator5a = ObjectAnimator.ofFloat(
				viewHolder.layoutAddEditRemove, "scaleX", 0);

		ObjectAnimator animator5b = ObjectAnimator.ofFloat(
				viewHolder.layoutAddEditRemove, "scaleY", 0);

		ObjectAnimator animator6 = ObjectAnimator.ofFloat(
				viewHolder.priceRight, "alpha", 1);

		AnimatorSet set = new AnimatorSet();
		// set.play(animator1a).with(animator1b);
		// set.play(animator2);
		set.play(animator3a).with(animator3b);
		set.play(animator4a).with(animator4b);
		set.play(animator5a).with(animator5b);
		set.play(animator6);
		if (duration == 0) {
			set.setDuration(duration);
		}
		set.start();
	}

	private void secondView(ViewHolder viewHolder, int duration) {
		// ObjectAnimator animator1a = ObjectAnimator.ofFloat(
		// viewHolder.itemImage, "alpha", 0);
		//
		// ObjectAnimator animator1b = ObjectAnimator.ofFloat(
		// viewHolder.itemImage, "translationY",
		// viewHolder.itemImage.getMeasuredHeight());
		//
		// ObjectAnimator animator2 =
		// ObjectAnimator.ofFloat(viewHolder.itemName,
		// "translationX", 0);

		if (!isDescriptionPresent) {

			if (swidth < 500) {
				ResizeAnimation animation = new ResizeAnimation(
						viewHolder.frameLayoutContent, IConstants.pxToDp(160),
						IConstants.pxToDp(listItemHeight), 300);
				viewHolder.frameLayoutContent.startAnimation(animation);
			} else {
				ResizeAnimation animation = new ResizeAnimation(
						viewHolder.frameLayoutContent, IConstants.pxToDp(70),
						IConstants.pxToDp(listItemHeight), 300);
				viewHolder.frameLayoutContent.startAnimation(animation);
			}

			// viewHolder.frameLayoutContent.getLayoutParams().height =
			// IConstants
			// .pxToDp(listItemHeight);
			// viewHolder.frameLayoutContent.requestLayout();
		}

		// viewHolder.itemName.setPaintFlags(viewHolder.itemName.getPaintFlags()
		// | Paint.UNDERLINE_TEXT_FLAG);

		ObjectAnimator animator3a = ObjectAnimator.ofFloat(
				viewHolder.layoutDescOrd, "alpha", 0);

		ObjectAnimator animator3b = ObjectAnimator.ofFloat(
				viewHolder.layoutDescOrd, "translationY",
				viewHolder.layoutDescOrd.getMeasuredHeight());

		ObjectAnimator animator4a = ObjectAnimator.ofFloat(
				viewHolder.layoutQtyPrc, "alpha", 1);

		ObjectAnimator animator4b = ObjectAnimator.ofFloat(
				viewHolder.layoutQtyPrc, "translationY", 0);

		ObjectAnimator animator5a = ObjectAnimator.ofFloat(
				viewHolder.layoutAddEditRemove, "scaleX", 1);

		ObjectAnimator animator5b = ObjectAnimator.ofFloat(
				viewHolder.layoutAddEditRemove, "scaleY", 1);

		ObjectAnimator animator6 = ObjectAnimator.ofFloat(
				viewHolder.priceRight, "alpha", 0);

		AnimatorSet set = new AnimatorSet();
		// set.play(animator1a).with(animator1b);
		// set.play(animator2);
		set.play(animator3a).with(animator3b);
		set.play(animator4a).with(animator4b);
		set.play(animator5a).with(animator5b);
		set.play(animator6);
		if (duration == 0) {
			set.setDuration(duration);
		}
		set.start();
	}

	// private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	//
	// int width = bm.getWidth();
	//
	// int height = bm.getHeight();
	//
	// float scaleWidth = ((float) newWidth) / width;
	//
	// float scaleHeight = ((float) newHeight) / height;
	//
	// // CREATE A MATRIX FOR THE MANIPULATION
	//
	// Matrix matrix = new Matrix();
	//
	// // RESIZE THE BIT MAP
	//
	// matrix.postScale(scaleWidth, scaleHeight);
	//
	// // RECREATE THE NEW BITMAP
	//
	// Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
	// matrix, false);
	//
	// return resizedBitmap;
	//
	// }

	private Bitmap getCroppedBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		// final RectF sOuterRect = new RectF(0, 0, bitmap.getWidth(),
		// bitmap.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		canvas.drawBitmap(bitmap, rect, rect, paint);

		// //
		// -----------------------------------------------------------------------------------------------------
		// colorFilter = new PorterDuffColorFilter(Color.parseColor("#3399cc"),
		// PorterDuff.Mode.OVERLAY);
		// paint.setColorFilter(colorFilter);
		//
		// Bitmap overlay = BitmapFactory.decodeResource(
		// IConstants.mContext.getResources(), R.drawable.ic_action_done);
		// //canvas = new Canvas(overlay);
		// //paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		// canvas.drawBitmap(overlay, 0, 0, paint);
		// //
		// -----------------------------------------------------------------------------------------------------

		// canvas.saveLayer(sOuterRect, paint, Canvas.ALL_SAVE_FLAG);

		// Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
		// return _bmp;
		return output;
	}

	// private Bitmap createCircleBitMap(Bitmap imgBitmap) {
	// // Create a mutable bitmap
	// Bitmap bitMap = Bitmap.createBitmap(imgBitmap.getWidth(),
	// imgBitmap.getHeight(), Bitmap.Config.ARGB_8888);
	//
	// bitMap = bitMap.copy(bitMap.getConfig(), true);
	// // Construct a canvas with the specified bitmap to draw into
	// Canvas canvas = new Canvas(bitMap);
	// // Create a new paint with default settings.
	// Paint paint = new Paint();
	// // smooths out the edges of what is being drawn
	// paint.setAntiAlias(true);
	// // set color
	// paint.setColor(Color.BLACK);
	// // set style
	// paint.setStyle(Paint.Style.STROKE);
	// // set stroke
	// paint.setStrokeWidth(4.5f);
	// // draw circle with radius imgBitmap.getWidth() / 2
	// canvas.drawCircle(imgBitmap.getWidth() / 2, imgBitmap.getHeight() / 2,
	// imgBitmap.getWidth() / 2, paint);
	//
	// paint = new Paint(Paint.FILTER_BITMAP_FLAG);
	// canvas.drawBitmap(bitMap, 0, 0, paint);
	//
	// return bitMap;
	// }

	// public Bitmap putOverlay(Bitmap bitmap) {
	// // Canvas canvas = new Canvas(bitmap);
	// // Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
	// // canvas.drawBitmap(overlay, 0, 0, paint);
	//
	// Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(),
	// bitmap.getHeight(), Bitmap.Config.ARGB_8888);
	//
	// Canvas c = new Canvas(resultBitmap);
	//
	// c.drawBitmap(bitmap, 0, 0, null);
	//
	// Paint p = new Paint();
	// p.setAlpha(127);
	//
	// Bitmap overlay = BitmapFactory.decodeResource(
	// IConstants.mContext.getResources(),
	// R.drawable.overlay_right_tick);
	//
	// overlay = Bitmap.createScaledBitmap(overlay, bitmap.getWidth(),
	// bitmap.getHeight(), false);
	//
	// // overlay = getCroppedBitmap(overlay);
	//
	// // Paint paint = new Paint();
	// //
	// // Canvas canvas = new Canvas(overlay);
	// //
	// // canvas.drawCircle(overlay.getWidth() / 2, overlay.getHeight() / 2,
	// // overlay.getWidth() / 2, paint);
	// //
	// // paint = new Paint(Paint.FILTER_BITMAP_FLAG);
	//
	// c.drawBitmap(overlay, 0, 0, p);
	//
	// return resultBitmap;
	// }
}
