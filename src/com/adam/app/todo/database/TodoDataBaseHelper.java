package com.adam.app.todo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDataBaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "todo.db";
	private static final int DATABASE_VERSION = 1;
	
	public TodoDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		TodoTable.onCreat(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		TodoTable.onUpgrade(db, oldVersion, newVersion);
	}

}
