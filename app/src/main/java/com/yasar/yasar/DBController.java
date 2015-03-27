/**
 * DBController - object for interacting with SQLite DB
 */

package com.yasar.yasar;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBController extends SQLiteOpenHelper implements IdbController {

    private final String TAG = ((Object) this).getClass().getSimpleName();

    private static final int DB_VERSION = 1;

	private static final String TABLE_NAME = "contacts";
	private static final String FIELD_ID = "id";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_NUMBER = "number";

	private DBController(Context applicationcontext) {
		super(applicationcontext, "androidsqlite.db", null, DB_VERSION);
	}

	// Make DBController a singleton instance
	private static DBController instance = null;

	public static DBController getInstance(Context applicationcontext) {
		if (instance == null)
			instance = new DBController(applicationcontext);
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE " + TABLE_NAME + " ( " + FIELD_ID
				+ " INTEGER PRIMARY KEY, " + FIELD_NAME + " TEXT, "
				+ FIELD_NUMBER + " TEXT)";
		database.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old,
			int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS " + TABLE_NAME;
		database.execSQL(query);
		onCreate(database);
	}

	// Inserting a new entry
	public long insertContact(Contact contact) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FIELD_ID, contact.getId());
		values.put(FIELD_NAME, contact.getName());
		values.put(FIELD_NUMBER, contact.getNumber());

		long insertRowId = database.insert(TABLE_NAME, null, values);

		database.close();

        return insertRowId;
	}

	// Updating an existing entry
	public int updateContact(Contact contact) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FIELD_ID, contact.getId());
		values.put(FIELD_NAME, contact.getName());
		values.put(FIELD_NUMBER, contact.getNumber());

		int status = database.update(TABLE_NAME, values, FIELD_ID + " = ?",
				new String[] { "1" });
		database.close();
		return status;
	}

	// Delete a single entry
	public void deleteContact(int id) {
		SQLiteDatabase database = this.getWritableDatabase();
		String deleteQuery = "DELETE FROM " + TABLE_NAME + " where " + FIELD_ID
				+ "='" + id + "'";
		Log.d(TAG, deleteQuery);
		database.execSQL(deleteQuery);
		database.close();
	}

	// Delete all entries
	public void deleteAll() {
		SQLiteDatabase database = this.getWritableDatabase();
		String deleteQuery = "DELETE * FROM " + TABLE_NAME;
		Log.d(TAG, deleteQuery);
		database.execSQL(deleteQuery);
		database.close();
	}

	// Return all entries in DB
	public ArrayList<Contact> getAllContacts() {
		ArrayList<Contact> list = new ArrayList<Contact>();
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Contact c = new Contact();
				c.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));
				c.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
				c.setNumber(cursor.getString(cursor.getColumnIndex(FIELD_NUMBER)));
				list.add(c);
			} while (cursor.moveToNext());
		}
		database.close();
		return list;
	}

	// Return a single entry given its unique ID
	public Contact getContactInfo(int id) {
		Contact c = new Contact();
		c.setId(id);
		SQLiteDatabase database = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " where "
				+ FIELD_ID + "='" + id + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			c.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
			c.setNumber(cursor.getString(cursor.getColumnIndex(FIELD_NUMBER)));
		} else {
			return null;
		}
		database.close();
		return c;
	}
}