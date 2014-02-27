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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDAO {

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
	 */
	public void insertItems(List<Item> items) {
		ContentValues cv = new ContentValues();
		for (Item item : items) {
			cv.put(ItemDatabaseHelper.COLUMN_TITLE, item.getTitle());
			cv.put(ItemDatabaseHelper.COLUMN_BODY, item.getBody());
			cv.put(ItemDatabaseHelper.COLUMN_EVENT_DATE, 
					item.getEventDate().getTime());
			cv.put(ItemDatabaseHelper.COLUMN_UPDATE_CREATED, 
					item.getUpdateCreated().getTime());
			mDB.insert(
					ItemDatabaseHelper.TABLE_ITEM, null, cv);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Item> getItems() {
		Cursor cursor = mDB.query(ItemDatabaseHelper.TABLE_ITEM, 
				null, null, null, null, null, 
					ItemDatabaseHelper.COLUMN_UPDATE_CREATED + " asc");
		ItemCursor itemCursor = new ItemCursor(cursor);
		List<Item> items = itemCursor.getItems();
		itemCursor.close();
		return items;
	}
	
	/*
	 * 
	 */
	private static class ItemCursor extends CursorWrapper {
		
		public ItemCursor(Cursor cursor) {
			super(cursor);
		}
		
		public List<Item> getItems() {
			List<Item> items = new ArrayList<Item>();
			
			moveToFirst();
			while (! isAfterLast()) {
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
				items.add(item);
				moveToNext();
			}
			return items;
		}
	}
}
