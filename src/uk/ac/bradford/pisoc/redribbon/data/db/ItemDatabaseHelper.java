package uk.ac.bradford.pisoc.redribbon.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * SQLite helper class for accessing the items database.
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
	 * 
	 */
	public static final String TABLE_ITEM = "item";
	/**
	 * 
	 */
	public static final String COLUMN_ID = "_id";
	/**
	 * 
	 */
	public static final String COLUMN_TITLE = "title";
	/**
	 * 
	 */
	public static final String COLUMN_BODY = "body";
	/**
	 * 
	 */
	public static final String COLUMN_EVENT_DATE = "event_date";
	/**
	 * 
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
	 * 
	 * 
	 * @see	SQLiteOpenHelper
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_CREATE);
	}

	/**
	 * 
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
