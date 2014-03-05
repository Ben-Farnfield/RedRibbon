package uk.ac.bradford.pisoc.redribbon.data.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.ac.bradford.pisoc.redribbon.data.model.Item;
import uk.ac.bradford.pisoc.redribbon.util.NullDate;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ItemDAO {
	
	private static final String TAG = "ItemDAO";
	
	private static final int MAX_NUM_ITEMS = 8;

	private SQLiteDatabase mDB;
	private SQLiteOpenHelper mDBHelper;
	
	/**
	 * 
	 * @param context
	 */
	public ItemDAO(Context context) {
		mDBHelper = new ItemDatabaseHelper(context);
	}
	
	/**
	 * 
	 */
	public void open() {
		mDB = mDBHelper.getWritableDatabase();
	}
	
	/**
	 * 
	 */
	public void close() {
		mDBHelper.close();
	}
	
	/**
	 * 
	 * @param items
	 * @return
	 */
	public int insertItems(List<Item> items) {
		
		int maxIdBeforeInsert = getItemMaxId();
		
		ContentValues cv = new ContentValues();
		for (Item item : items) {
			cv.put(ItemDatabaseHelper.COLUMN_TITLE, item.getTitle());
			cv.put(ItemDatabaseHelper.COLUMN_BODY, item.getBody());
			cv.put(ItemDatabaseHelper.COLUMN_EVENT_DATE, 
					item.getEventDate().getTime());
			cv.put(ItemDatabaseHelper.COLUMN_UPDATE_CREATED, 
					item.getUpdateCreated().getTime());
			try {
				long key = mDB.insertOrThrow(
						ItemDatabaseHelper.TABLE_ITEM, null, cv);
				Log.d(TAG, "Item key: " + String.valueOf(key));
			} catch (SQLException e) {
				// Thrown if the item being inserted already exists 
				// within the database. Do nothing.
			}
		}
		
		int maxIdAfterInsert = getItemMaxId();
		
		purgeOldItems();

		return maxIdAfterInsert - maxIdBeforeInsert;
	}
	
	/**
	 * 
	 * @return
	 */
	public Cursor getItems() {
//		Cursor cursor = mDB.rawQuery("SELECT * FROM item", null);
//		ItemCursor itemCursor = new ItemCursor(cursor);
//		List<Item> items = itemCursor.getItems();
//		itemCursor.close();
//		return items;
		return mDB.rawQuery("SELECT * FROM item", null);
	}
	
	/**
	 * 
	 * @return
	 */
	public Item getLatestItem() {
		Cursor cursor = mDB.rawQuery("SELECT * FROM item WHERE _id=?", 
				new String[] {String.valueOf(getItemMaxId())});
		ItemCursor itemCursor = new ItemCursor(cursor);
		Item item = itemCursor.getItem();
		itemCursor.close();
		return item;
	}
	
	/* Returns the highest ID currently in use within the database. */
	private int getItemMaxId() {
		Cursor cursor = mDB.rawQuery("SELECT MAX(_id) AS id FROM " + 
				ItemDatabaseHelper.TABLE_ITEM, null);
		ItemCursor itemCursor = new ItemCursor(cursor);
		int maxId = itemCursor.getId();
		itemCursor.close();
		return maxId;
	}
	
	/* Returns the lowest ID currently in use within the database. */
	private int getItemMinId() {
		Cursor cursor = mDB.rawQuery("SELECT MIN(_id) AS id FROM " + 
				ItemDatabaseHelper.TABLE_ITEM, null);
		ItemCursor itemCursor = new ItemCursor(cursor);
		int minId = itemCursor.getId();
		itemCursor.close();
		return minId;
	}
	
	/* Purges the oldest Items from the database.
	 * 
	 * When the total number of Items is greater than the MAX_NUM_ITEMS 
	 * then the oldest Items are deleted.
	 */
	private void purgeOldItems() {
		int numItemsToPurge = getTotalNumItems() - MAX_NUM_ITEMS;
		
		if (numItemsToPurge > 0) {
			Log.d(TAG, "Purge " + String.valueOf(numItemsToPurge) + " items.");
			for (int i=0; i < numItemsToPurge; i++) {
				deleteOldestItem();
			}
		}
	}
	
	/* Returns the total number of Items stored within the database. */
	private int getTotalNumItems() {
		Cursor cursor = mDB.rawQuery("SELECT COUNT(*) AS numItems FROM " +
				ItemDatabaseHelper.TABLE_ITEM, null);
		ItemCursor itemCursor = new ItemCursor(cursor);
		int numItems = itemCursor.getNumItems();
		itemCursor.close();
		return numItems;
	}
	
	/* Deletes the oldest Item from the database.
	 * 
	 * The items age is determined using its ID. The smallest ID is 
	 * presumed to be associated with the oldest Item.
	 */
	private void deleteOldestItem() {
		mDB.delete(ItemDatabaseHelper.TABLE_ITEM, "_id=?", 
				new String[] {String.valueOf(getItemMinId())});
	}
	
	/* Helper class which wraps a cursor object. 
	 * 
	 * Within the wrapper the cursors methods are exposed allowing you to 
	 * call them without referencing the cursor object itself.
	 */
	private static class ItemCursor extends CursorWrapper {

		public ItemCursor(Cursor cursor) {
			super(cursor);
		}
		
		/* Returns a list of all Item objects stored within the database. */
		public List<Item> getItems() {
			List<Item> items = new ArrayList<Item>();
			
			moveToFirst();
			while (! isAfterLast()) {
				items.add(getCurrentItem());
				moveToNext();
			}
			return items;
		}
		
		/* Returns the Item currently pointed to by the cursor. */
		public Item getItem() {
			moveToFirst();
			return getCurrentItem();
		}
		
		/* Helper for ItemCursor#getItem() and ItemCursor#getItems(). Builds 
		 * and then returns the Item currently pointed to by the cursor.
		 */
		private Item getCurrentItem() {
			Item item = new Item();
			item.setTitle(getString(
					getColumnIndex(ItemDatabaseHelper.COLUMN_TITLE)));
			item.setBody(getString(
					getColumnIndex(ItemDatabaseHelper.COLUMN_BODY)));
			Date eventDate = NullDate.parseDate(getLong(
					getColumnIndex(ItemDatabaseHelper.COLUMN_EVENT_DATE)));
			item.setEventDate(eventDate);
			Date updateCreated = NullDate.parseDate(getLong(getColumnIndex(
					ItemDatabaseHelper.COLUMN_UPDATE_CREATED)));
			item.setUpdateCreated(updateCreated);
			return item;
		}
		
		/* Helper for ItemDAO#getItemMaxId() and ItemDAO#getItemMinId(). 
		 * Returns an int representing the MIN or MAX id.
		 */
		public int getId() {
			moveToFirst();
			return getInt(getColumnIndex("id"));
		}
		
		/* Helper for ItemDAO#getTotalNumItems(). Returns an int representing 
		 * the number of Items within the database. 
		 */
		public int getNumItems() {
			moveToFirst();
			return getInt(getColumnIndex("numItems"));
		}
	}
}
