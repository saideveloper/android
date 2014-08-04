package com.restaurant.dao;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
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

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something
	// appropriate for your app
	private static final String DATABASE_NAME = "restaurant.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access the MenuDetails table
	private Dao<MenuDetails, Integer> menuDao = null;
	private Dao<TrxCodeDetails, Integer> trxDao = null;
	private Dao<OrderMasterDetails, Integer> orderMasterDao = null;
	private Dao<OrderMenuDetails, Integer> orderMenuDao = null;
	private Dao<OrderOfferDetails, Integer> orderOfferDao = null;
	private Dao<LkupCourseDetails, Integer> lkupcourseDao = null;
	private Dao<RestaurantDetails, Integer> restaurantDao = null;
	private Dao<ReservationDetails, Integer> reservationDao = null;
	private Dao<LkupMenuCategory, Integer> lkupMenuCategoryDao = null;
	private Dao<OfferDetails, Integer> offerDetailsDao = null;
	private Dao<RestAddonItemsDetails, Integer> restAddonItemsDetailsDao = null;
	private Dao<RestItemTypeDetails, Integer> restItemTypeDetailsDao = null;
	private Dao<RestaurantInfoDetails, Integer> restaurantInfoDetailsDao = null;
	private Dao<RestTimingDetails, Integer> restTimingDetailsDao = null;
	private Dao<LkupPayTypeDetails, Integer> lkupPayTypeDetailsDao = null;
	private Dao<LkupPrepTypeDetails, Integer> lkupPrepTypeDetailsDao = null;
	private Dao<ModifierDetails, Integer> modifierDetailsDao = null;
	private Dao<ModifierQuestionAnswer, Integer> modifierQuestionAnswerDao = null;
	private Dao<SalesDetails, Integer> salesDetailsDao = null;
	private Dao<LkupTaxListDetails, Integer> lkupTaxListDetailsDao = null;
	private Dao<LkupRestTaxDetails, Integer> lkupRestTaxDetailsDao = null;
	private Dao<LkupOrderStatusDetails, Integer> lkupOrderStatusDetailsDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTableIfNotExists(connectionSource,
					MenuDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					TrxCodeDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					OrderMasterDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					OrderMenuDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					OrderOfferDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					LkupCourseDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					RestaurantDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					ReservationDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					LkupMenuCategory.class);
			TableUtils.createTableIfNotExists(connectionSource,
					OfferDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					RestAddonItemsDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					RestItemTypeDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					RestaurantInfoDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					RestTimingDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					LkupPayTypeDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					LkupPrepTypeDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					ModifierDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					ModifierQuestionAnswer.class);
			TableUtils.createTableIfNotExists(connectionSource,
					SalesDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					LkupTaxListDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					LkupRestTaxDetails.class);
			TableUtils.createTableIfNotExists(connectionSource,
					LkupOrderStatusDetails.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, MenuDetails.class, true);
			TableUtils.dropTable(connectionSource, TrxCodeDetails.class, true);
			TableUtils.dropTable(connectionSource, OrderMasterDetails.class,
					true);
			TableUtils
					.dropTable(connectionSource, OrderMenuDetails.class, true);
			TableUtils.dropTable(connectionSource, OrderOfferDetails.class,
					true);
			TableUtils.dropTable(connectionSource, LkupCourseDetails.class,
					true);
			TableUtils.dropTable(connectionSource, RestaurantDetails.class,
					true);
			TableUtils.dropTable(connectionSource, ReservationDetails.class,
					true);
			TableUtils
					.dropTable(connectionSource, LkupMenuCategory.class, true);
			TableUtils.dropTable(connectionSource, OfferDetails.class, true);
			TableUtils.dropTable(connectionSource, RestAddonItemsDetails.class,
					true);
			TableUtils.dropTable(connectionSource, RestItemTypeDetails.class,
					true);
			TableUtils.dropTable(connectionSource, RestaurantInfoDetails.class,
					true);
			TableUtils.dropTable(connectionSource, RestTimingDetails.class,
					true);
			TableUtils.dropTable(connectionSource, LkupPayTypeDetails.class,
					true);
			TableUtils.dropTable(connectionSource, LkupPrepTypeDetails.class,
					true);
			TableUtils.dropTable(connectionSource, ModifierDetails.class,
					true);
			TableUtils.dropTable(connectionSource, ModifierQuestionAnswer.class,
					true);
			TableUtils.dropTable(connectionSource, SalesDetails.class,
					true);
			TableUtils.dropTable(connectionSource, LkupTaxListDetails.class,
					true);
			TableUtils.dropTable(connectionSource, LkupRestTaxDetails.class,
					true);
			TableUtils.dropTable(connectionSource, LkupOrderStatusDetails.class,
					true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the Database Access Object (DAO) for our MenuDetails class. It
	 * will create it or just give the cached value.
	 */
	public Dao<MenuDetails, Integer> getMenuDetailsDao() {
		if (menuDao == null) {
			try {
				menuDao = getDao(MenuDetails.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return menuDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our TrxCodeDetails class. It
	 * will create it or just give the cached value.
	 */
	public Dao<TrxCodeDetails, Integer> getTrxCodeDetailsDao() {
		if (trxDao == null) {
			try {
				trxDao = getDao(TrxCodeDetails.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return trxDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our OrderMasterDetails
	 * class. It will create it or just give the cached value.
	 */
	public Dao<OrderMasterDetails, Integer> getOrderMasterDetailsDao() {
		if (orderMasterDao == null) {
			try {
				orderMasterDao = getDao(OrderMasterDetails.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return orderMasterDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our OrderMenuDetails class.
	 * It will create it or just give the cached value.
	 */
	public Dao<OrderMenuDetails, Integer> getOrderMenuDetailsDao() {
		if (orderMenuDao == null) {
			try {
				orderMenuDao = getDao(OrderMenuDetails.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return orderMenuDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our OrderOfferDetails class.
	 * It will create it or just give the cached value.
	 */
	public Dao<OrderOfferDetails, Integer> getOrderOfferDetailsDao() {
		if (orderOfferDao == null) {
			try {
				orderOfferDao = getDao(OrderOfferDetails.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return orderOfferDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our LkupCourseDetails class.
	 * It will create it or just give the cached value.
	 */
	public Dao<LkupCourseDetails, Integer> getLkupCourseDetailsDao() {
		if (lkupcourseDao == null) {
			try {
				lkupcourseDao = getDao(LkupCourseDetails.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return lkupcourseDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our RestaurantDetails class.
	 * It will create it or just give the cached value.
	 */
	public Dao<RestaurantDetails, Integer> getRestaurantDetailsDao() {
		if (restaurantDao == null) {
			try {
				restaurantDao = getDao(RestaurantDetails.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return restaurantDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our ReservationDetails
	 * class. It will create it or just give the cached value.
	 */
	public Dao<ReservationDetails, Integer> getReservationDetailsDao() {
		if (reservationDao == null) {
			try {
				reservationDao = getDao(ReservationDetails.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return reservationDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our LKupMenuCategory class.
	 * It will create it or just give the cached value.
	 */
	public Dao<LkupMenuCategory, Integer> getLkupMenuCategoryDao() {
		if (lkupMenuCategoryDao == null) {
			try {
				lkupMenuCategoryDao = getDao(LkupMenuCategory.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return lkupMenuCategoryDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our offerDetails class. It
	 * will create it or just give the cached value.
	 */
	public Dao<OfferDetails, Integer> getOfferDetailsDao() {
		if (offerDetailsDao == null) {
			try {
				offerDetailsDao = getDao(OfferDetails.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return offerDetailsDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our RestAddonItemsDetails
	 * class. It will create it or just give the cached value.
	 */
	public Dao<RestAddonItemsDetails, Integer> getRestAddonItemsDetailsDao() {
		if (restAddonItemsDetailsDao == null) {
			try {
				restAddonItemsDetailsDao = getDao(RestAddonItemsDetails.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return restAddonItemsDetailsDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our RestItemTypeDetails
	 * class. It will create it or just give the cached value.
	 */
	public Dao<RestItemTypeDetails, Integer> getRestItemTypeDetailsDao() {
		if (restItemTypeDetailsDao == null) {
			try {
				restItemTypeDetailsDao = getDao(RestItemTypeDetails.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return restItemTypeDetailsDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our RestaurantInfoDetails
	 * class. It will create it or just give the cached value.
	 */
	public Dao<RestaurantInfoDetails, Integer> getRestaurantInfoDetailsDao() {
		if (restaurantInfoDetailsDao == null) {
			try {
				restaurantInfoDetailsDao = getDao(RestaurantInfoDetails.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return restaurantInfoDetailsDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our RestTimingDetails class.
	 * It will create it or just give the cached value.
	 */
	public Dao<RestTimingDetails, Integer> getRestTimingDetailsDao() {
		if (restTimingDetailsDao == null) {
			try {
				restTimingDetailsDao = getDao(RestTimingDetails.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return restTimingDetailsDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our LkupPayTypeDetails
	 * class. It will create it or just give the cached value.
	 */
	public Dao<LkupPayTypeDetails, Integer> getLkupPayTypeDetailsDao() {
		if (lkupPayTypeDetailsDao == null) {
			try {
				lkupPayTypeDetailsDao = getDao(LkupPayTypeDetails.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lkupPayTypeDetailsDao;
	}
	
	/**
	 * Returns the Database Access Object (DAO) for our LkupPayTypeDetails
	 * class. It will create it or just give the cached value.
	 */
	public Dao<LkupPrepTypeDetails, Integer> getLkupPrepTypeDetailsDao() {
		if (lkupPrepTypeDetailsDao == null) {
			try {
				lkupPrepTypeDetailsDao = getDao(LkupPrepTypeDetails.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lkupPrepTypeDetailsDao;
	}
	
	/**
	 * Returns the Database Access Object (DAO) for our ModifierDetails
	 * class. It will create it or just give the cached value.
	 */
	public Dao<ModifierDetails, Integer> getModifierDetailsDao() {
		if (modifierDetailsDao == null) {
			try {
				modifierDetailsDao = getDao(ModifierDetails.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return modifierDetailsDao;
	}
	
	
	/**
	 * Returns the Database Access Object (DAO) for our ModifierQuestionAnswer
	 * class. It will create it or just give the cached value.
	 */
	public Dao<ModifierQuestionAnswer, Integer> getModifierQuestionAnswerDao() {
		if (modifierQuestionAnswerDao == null) {
			try {
				modifierQuestionAnswerDao = getDao(ModifierQuestionAnswer.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return modifierQuestionAnswerDao;
	}
	
	
	/**
	 * Returns the Database Access Object (DAO) for our SalesDetails
	 * class. It will create it or just give the cached value.
	 */
	public Dao<SalesDetails, Integer> getSalesDetailsDao() {
		if (salesDetailsDao == null) {
			try {
				salesDetailsDao = getDao(SalesDetails.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return salesDetailsDao;
	}
	
	
	/**
	 * Returns the Database Access Object (DAO) for our LkupTaxListDetails
	 * class. It will create it or just give the cached value.
	 */
	public Dao<LkupTaxListDetails, Integer> getLkupTaxListDetailsDao() {
		if (lkupTaxListDetailsDao == null) {
			try {
				lkupTaxListDetailsDao = getDao(LkupTaxListDetails.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lkupTaxListDetailsDao;
	}
	
	
	/**
	 * Returns the Database Access Object (DAO) for our LkupRestTaxDetails
	 * class. It will create it or just give the cached value.
	 */
	public Dao<LkupRestTaxDetails, Integer> getLkupRestTaxDetailsDao() {
		if (lkupRestTaxDetailsDao == null) {
			try {
				lkupRestTaxDetailsDao = getDao(LkupRestTaxDetails.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lkupRestTaxDetailsDao;
	}

	
	/**
	 * Returns the Database Access Object (DAO) for our LkupOrderStatusDetails
	 * class. It will create it or just give the cached value.
	 */
	public Dao<LkupOrderStatusDetails, Integer> getLkupOrderStatusDetailsDao() {
		if (lkupOrderStatusDetailsDao == null) {
			try {
				lkupOrderStatusDetailsDao = getDao(LkupOrderStatusDetails.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lkupOrderStatusDetailsDao;
	}


	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		menuDao = null;
		trxDao = null;
		orderMasterDao = null;
		orderMenuDao = null;
		orderOfferDao = null;
		lkupcourseDao = null;
		restaurantDao = null;
		reservationDao = null;
		lkupMenuCategoryDao = null;
		offerDetailsDao = null;
		restAddonItemsDetailsDao = null;
		restItemTypeDetailsDao = null;
		restaurantInfoDetailsDao = null;
		restTimingDetailsDao = null;
		lkupPayTypeDetailsDao = null;
		modifierDetailsDao = null;
		modifierQuestionAnswerDao = null;
		salesDetailsDao = null;
		lkupTaxListDetailsDao = null;
		lkupRestTaxDetailsDao = null;
		lkupOrderStatusDetailsDao = null;
	}
}
