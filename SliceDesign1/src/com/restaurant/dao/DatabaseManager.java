package com.restaurant.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.restaurant.model.MenuDetails;
import com.restaurant.model.ModifierDetails;
import com.restaurant.model.ModifierQuestionAnswer;
import com.restaurant.model.OfferDetails;
import com.restaurant.model.OrderMasterDetails;
import com.restaurant.model.OrderMenuDetails;
import com.restaurant.model.OrderOfferDetails;
import com.restaurant.model.ReservationDetails;
import com.restaurant.model.RestAddonItemsDetails;
import com.restaurant.model.RestItemTypeDetails;
import com.restaurant.model.RestTimingDetails;
import com.restaurant.model.RestaurantDetails;
import com.restaurant.model.RestaurantInfoDetails;
import com.restaurant.model.SalesDetails;
import com.restaurant.model.TrxCodeDetails;
import com.restaurant.model.lkup.LkupCourseDetails;
import com.restaurant.model.lkup.LkupMenuCategory;
import com.restaurant.model.lkup.LkupOrderStatusDetails;
import com.restaurant.model.lkup.LkupPayTypeDetails;
import com.restaurant.model.lkup.LkupPrepTypeDetails;
import com.restaurant.model.lkup.LkupRestTaxDetails;
import com.restaurant.model.lkup.LkupTaxListDetails;

public class DatabaseManager {

	static private DatabaseManager instance;

	static public void init(Context ctx) {
		if (null == instance) {
			instance = new DatabaseManager(ctx);
		}
	}

	static public DatabaseManager getInstance() {
		return instance;
	}

	private static DatabaseHelper helper;

	private DatabaseManager(Context ctx) {
		helper = new DatabaseHelper(ctx);
	}

	public static DatabaseHelper getHelper() {
		return helper;
	}

	public void closeHelper() {
		try {
			// helper.close();
			// helper = null;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: DatabaseManager handle exception
		}
	}

	public Dao<MenuDetails, Integer> getMenuDetailsDao() {
		return getHelper().getMenuDetailsDao();
	}

	public Dao<TrxCodeDetails, Integer> getTrxCodeDetailsDao() {
		return getHelper().getTrxCodeDetailsDao();
	}

	public Dao<OrderMasterDetails, Integer> getOrderMasterDetailsDao() {
		return getHelper().getOrderMasterDetailsDao();
	}

	public Dao<OrderMenuDetails, Integer> getOrderMenuDetailsDao() {
		return getHelper().getOrderMenuDetailsDao();
	}

	public Dao<OrderOfferDetails, Integer> getOrderOfferDetailsDao() {
		return getHelper().getOrderOfferDetailsDao();
	}
	
	public Dao<LkupCourseDetails, Integer> getLkupCourseDetailsDao() {
		return getHelper().getLkupCourseDetailsDao();
	}
	
	public Dao<RestaurantDetails, Integer> getRestaurantDetailsDao() {
		return getHelper().getRestaurantDetailsDao();
	}
	
	public Dao<ReservationDetails, Integer> getReservationDetailsDao() {
		return getHelper().getReservationDetailsDao();
	}
	
	public Dao<LkupMenuCategory, Integer> getLkupMenuCategoryDao() {
		return getHelper().getLkupMenuCategoryDao();
	}
	
	public Dao<OfferDetails, Integer> getofferDetailsDao() {
		return getHelper().getOfferDetailsDao();
	}
	
	public Dao<RestAddonItemsDetails, Integer> getRestAddonItemsDetailsDao() {
		return getHelper().getRestAddonItemsDetailsDao();
	}
	
	public Dao<RestItemTypeDetails, Integer> getRestItemTypeDetailsDao() {
		return getHelper().getRestItemTypeDetailsDao();
	}
	
	public Dao<RestaurantInfoDetails, Integer> getRestaurantInfoDetailsDao() {
		return getHelper().getRestaurantInfoDetailsDao();
	}
	
	public Dao<RestTimingDetails, Integer> getRestTimingDetailsDao() {
		return getHelper().getRestTimingDetailsDao();
	}
	
	public Dao<LkupPayTypeDetails, Integer> getLkupPayTypeDetailsDao() {
		return getHelper().getLkupPayTypeDetailsDao();
	}
	
	public Dao<LkupPrepTypeDetails, Integer> getLkupPrepTypeDetailsDao() {
		return getHelper().getLkupPrepTypeDetailsDao();
	}
	
	public Dao<ModifierDetails, Integer> getModifierDetailsDao() {
		return getHelper().getModifierDetailsDao();
	}
	
	public Dao<ModifierQuestionAnswer, Integer> getModifierQuestionAnswerDao() {
		return getHelper().getModifierQuestionAnswerDao();
	}
	
	public Dao<SalesDetails, Integer> getSalesDetailsDao() {
		return getHelper().getSalesDetailsDao();
	}
	
	public Dao<LkupTaxListDetails, Integer> getLkupTaxListDetailsDao() {
		return getHelper().getLkupTaxListDetailsDao();
	}
	
	public Dao<LkupRestTaxDetails, Integer> getLkupRestTaxDetailsDao() {
		return getHelper().getLkupRestTaxDetailsDao();
	}
	
	public Dao<LkupOrderStatusDetails, Integer> getLkupOrderStatusDetailsDao() {
		return getHelper().getLkupOrderStatusDetailsDao();
	}
}