package uk.ac.bradford.pisoc.redribbon.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * SQLite helper class for accessing and creating the items database.
 * 
 * <pre>
 * <code>
 * | _id           | title  | body  | event_date  | update_created  |
 * +---------------+--------+-------+-------------+-----------------+
 * | INTEGER       | TEXT   | TEXT  | INTEGER     | INTEGER         |
 * | PRIMARY KEY   |        |       |             |                 |
 * | AUTOINCREMENT |        |       |             |                 |
 * </code>
 * </pre>
 * 
 * @author 	Ben Farnfield
 */
public class ItemDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "ItemDatabaseHelper";
	
	private static final String DB_NAME = "items.sqlite";
	private static final int VERSION = 1;
	
	/**
	 * Item table name.
	 */
	public static final String TABLE_ITEM = "item";
	/**
	 * ID column name.
	 */
	public static final String COLUMN_ID = "_id";
	/**
	 * Title column name.
	 */
	public static final String COLUMN_TITLE = "title";
	/**
	 * Body column name.
	 */
	public static final String COLUMN_BODY = "body";
	/**
	 * Event date column name.
	 */
	public static final String COLUMN_EVENT_DATE = "event_date";
	/**
	 * Update created column name.
	 */
	public static final String COLUMN_UPDATE_CREATED = "update_created";
	
	private static final String DB_CREATE = "CREATE TABLE " + TABLE_ITEM + 
			" (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				   COLUMN_TITLE + " TEXT, " +
				   COLUMN_BODY + " TEXT, " +
				   COLUMN_EVENT_DATE + " INTEGER, " +
				   COLUMN_UPDATE_CREATED + " INTEGER);";
	
	/**
	 * Database name and version are passed to the SQLiteOpenHelper 
	 * constructor.
	 * 
	 * @param	context Context of the caller.
	 * @see		SQLiteOpenHelper
	 */
	public ItemDatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	/**
	 * Creates the database if it doesn't currently exist.
	 * 
	 * @see	SQLiteOpenHelper
	 * @see	Item
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_CREATE);
	}

	/**
	 * Drops and re-builds the database.
	 * 
	 * @see	SQLiteOpenHelper
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database ... this destroys old data.");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
	    onCreate(db);
	}
}
