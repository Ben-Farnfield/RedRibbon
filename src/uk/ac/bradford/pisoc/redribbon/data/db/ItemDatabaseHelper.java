package uk.ac.bradford.pisoc.redribbon.data.db;

import java.util.ArrayList;
import java.util.List;

import uk.ac.bradford.pisoc.redribbon.data.model.Item;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite helper class for accessing the items database.
 * 
 * <pre>
 * <code>
 * | _id           | title | body  | event_date | update_created |
 * +---------------+-------+-------+------------+----------------+
 * | INTEGER       | TEXT  | TEXT  | DATETIME   | DATETIME       |
 * | PRIMARY KEY   |       |       |            |                |
 * | AUTOINCREMENT |       |       |            |                |
 * </code>
 * </pre>
 * 
 * @author 	Ben Farnfield
 */
public class ItemDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "items.sqlite";
	private static final int VERSION = 1;
	
	private static final String TABLE_ITEM = "item";
	private static final String COLUMN_TITLE = "title";
	private static final String COLUMN_BODY = "body";
	private static final String COLUMN_EVENT_DATE = "event_date";
	private static final String COLUMN_UPDATE_CREATED = "update_created";
	
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

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_ITEM + " (" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, " +
				"body TEXT, event_date DATETIME, update_created DATETIME)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// do nothing
	}
	
	public void insertItems(List<Item> items) {
		ContentValues cv = new ContentValues();
		for (Item item : items) {
			cv.put(COLUMN_TITLE, item.getTitle());
			cv.put(COLUMN_BODY, item.getBody());
			cv.put(COLUMN_EVENT_DATE, item.getEventDate().toString());
			cv.put(COLUMN_UPDATE_CREATED, item.getUpdateCreated().toString());
			getWritableDatabase().insert(TABLE_ITEM, null, cv);
		}
	}
	
	public List<Item> getItems() {
		List<Item> items = new ArrayList<Item>();
		
		return items;
	}
}
