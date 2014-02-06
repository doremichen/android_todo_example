package com.adam.app.todo;

import com.adam.app.todo.contentprovider.MyTodoContentProvider;
import com.adam.app.todo.database.TodoTable;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TodoDetailActivity extends Activity {

	private Spinner mCategory = null;
	private EditText mTitleText = null;
	private EditText mBodyText = null;

	private Uri todoUri = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.todo_edit);
		
		mCategory = (Spinner) findViewById(R.id.category);
	    mTitleText = (EditText) findViewById(R.id.todo_edit_summary);
	    mBodyText = (EditText) findViewById(R.id.todo_edit_description);
	    Button confirmButton = (Button) findViewById(R.id.todo_edit_button);
	    
	    Bundle extras = getIntent().getExtras();
	    
	    // Check from the saved Instance
	    todoUri = (savedInstanceState == null) ? null : (Uri)savedInstanceState
	        .getParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE);
	    
	    if(extras != null) {
	    	todoUri = extras.getParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE);
	    	
	    	fillData(todoUri);
	    }
	    
	    
	    confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(mTitleText.getText().toString())) {
					makeToast();
				} else {
					setResult(RESULT_OK);
			        finish();
				}
			}
	    	
	    });
	    
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		saveState();
	}
	
	private void fillData( Uri uri) {
		String[] projection = {TodoTable.COLUMN_CATEGORY, TodoTable.COLUMN_DESCRIPTION, TodoTable.COLUMN_SUMMARY};
		
		Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
		
		if(cursor != null) {
			cursor.moveToFirst();
			String category = cursor.getString(cursor.getColumnIndexOrThrow(TodoTable.COLUMN_CATEGORY));
			
			for(int i = 0; i < mCategory.getCount(); i++) {
				String s = (String) mCategory.getItemAtPosition(i);
				if(s.equalsIgnoreCase(category)) {
					mCategory.setSelection(i);
				}
				
			}
			
			mTitleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(TodoTable.COLUMN_SUMMARY)));
			mBodyText.setText(cursor.getString(cursor.getColumnIndexOrThrow(TodoTable.COLUMN_DESCRIPTION)));
			
		}
		
		cursor.close();
		
	}
	
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	    saveState();
	    outState.putParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE, todoUri);
	}
	
	private void saveState() {
		 String category = (String) mCategory.getSelectedItem();
		 String summary = mTitleText.getText().toString();
		 String description = mBodyText.getText().toString();
		 
		 if(summary.length() == 0 && description.length() == 0) {
			 return;
		 }
		 
		 ContentValues values = new ContentValues();
		 values.put(TodoTable.COLUMN_CATEGORY, category);
		 values.put(TodoTable.COLUMN_SUMMARY, summary);
		 values.put(TodoTable.COLUMN_DESCRIPTION, description);
		 
		 if(todoUri == null) {
			 todoUri = this.getContentResolver().insert(MyTodoContentProvider.CONTENT_URI, values);
		 } else {
			 this.getContentResolver().update(MyTodoContentProvider.CONTENT_URI, values, null, null);
		 }
		 		 
	}
	
	private void makeToast() {
		Toast.makeText(this, "Please maintain summary", Toast.LENGTH_SHORT).show();
	}

}