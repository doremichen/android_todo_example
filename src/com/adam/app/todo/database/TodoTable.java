package com.adam.app.todo.database;

import android.database.sqlite.SQLiteDatabase;

public class TodoTable {
	public static final String TABLE_TODO = "todo";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_SUMMARY = "summary";
	public static final String COLUMN_DESCRIPTION = "description";
	
	private static final String DATABASE_CREATE = "CREATE TABLE " +
			TABLE_TODO +
			"(" +
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_CATEGORY + " text not null, " +
			COLUMN_SUMMARY + " text not null, " +
			COLUMN_DESCRIPTION + " text not null" +
			")";
	
	public static void onCreat(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
		onCreat(db);
	}
}
