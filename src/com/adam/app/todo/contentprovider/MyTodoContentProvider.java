package com.adam.app.todo.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import com.adam.app.todo.database.TodoDataBaseHelper;
import com.adam.app.todo.database.TodoTable;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class MyTodoContentProvider extends ContentProvider {

	//database
	private TodoDataBaseHelper database = null;
	
	//used for URiMatcher
	private static final int TODOS = 10;
	private static final int TODOS_ID = 20;
	
	private static final String AUTHORITY = "com.adam.app.todo.contentprovider";
	
	private static final String BASE_PATH = "todos";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/todos";
	public static final String CONTENT_ITEM_TYPE =  ContentResolver.CURSOR_ITEM_BASE_TYPE + "/todo";
	
	public static final UriMatcher SURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		SURIMatcher.addURI(AUTHORITY, BASE_PATH, TODOS);
		SURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TODOS_ID);
	}
	
	
	private void checkColumns(String[] projection) {
		String[] avaliable = {
				TodoTable.COLUMN_CATEGORY,
				TodoTable.COLUMN_DESCRIPTION,
				TodoTable.COLUMN_SUMMARY,
				TodoTable.COLUMN_ID
		};
		
		if(projection != null) {
			HashSet<String> requestColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availbleColumns = new HashSet<String>(Arrays.asList(avaliable));
			
			if(!availbleColumns.containsAll(requestColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int uriType = SURIMatcher.match(uri);
		SQLiteDatabase db = database.getWritableDatabase();
		int rowDeleted = 0;
		
		switch(uriType) {
		case TODOS:
			rowDeleted = db.delete(TodoTable.TABLE_TODO, selection, selectionArgs);
			break;
		case TODOS_ID:
			String id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowDeleted = db.delete(TodoTable.TABLE_TODO, TodoTable.COLUMN_ID + "=" + id, null);
			} else {
				rowDeleted = db.delete(TodoTable.TABLE_TODO, TodoTable.COLUMN_ID + "=" + id +
						" and " + selection, selectionArgs);
			}
						
			break;
		default:
			break;
		}
		
		this.getContext().getContentResolver().notifyChange(uri, null);
		return rowDeleted;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		int uriType = SURIMatcher.match(uri);
		SQLiteDatabase db = database.getWritableDatabase();
		long id = 0;
		
		switch(uriType) {
		case TODOS:
			id = db.insert(TodoTable.TABLE_TODO, null, values);
			break;
		case TODOS_ID:
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		this.getContext().getContentResolver().notifyChange(uri, null);
		
		
		return Uri.parse(this.BASE_PATH + " /" + id);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		database = new TodoDataBaseHelper(this.getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		//use SQLiteQueryBuilder
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
		checkColumns(projection);
		
		queryBuilder.setTables(TodoTable.TABLE_TODO);
		
		int uriType = SURIMatcher.match(uri);
		
		switch(uriType) {
		case TODOS:			
			break;
		case TODOS_ID:
			//Adding Id
			queryBuilder.appendWhere(TodoTable.COLUMN_ID + " = " + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(this.getContext().getContentResolver(), uri);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int uriType = SURIMatcher.match(uri);
		SQLiteDatabase db = database.getWritableDatabase();
		int rowUpdated = 0;
		switch(uriType) {
		case TODOS:
			rowUpdated = db.update(TodoTable.TABLE_TODO, values, selection, selectionArgs);			
			break;
		case TODOS_ID:
			String id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowUpdated = db.update(TodoTable.TABLE_TODO, values, TodoTable.COLUMN_ID + "=" + id, null);
			} else {
				rowUpdated = db.update(TodoTable.TABLE_TODO, values, TodoTable.COLUMN_ID + "=" + id +
						" and " + selection, selectionArgs);
			}
			break;
		default:
			break;
		}
		
		this.getContext().getContentResolver().notifyChange(uri, null);
		
		return rowUpdated;
	}

}
