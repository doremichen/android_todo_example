package com.adam.app.todo;


import com.adam.app.todo.contentprovider.MyTodoContentProvider;
import com.adam.app.todo.database.TodoTable;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TodosOverviewActivity extends ListActivity implements 
                                     LoaderCallbacks<Cursor>{

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int DELETED_ID = Menu.FIRST + 1;
	
	private SimpleCursorAdapter adapter = null;
	
	
	private void fillData() {
		String[] from = new String[]{TodoTable.COLUMN_SUMMARY};
		int[] to = new int[]{R.id.label};
		
		this.getLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(this, R.layout.todo_raw, null, from, to, 0);
		
		this.setListAdapter(adapter);
	}
	
	private void createTodo() {
		 Intent i = new Intent(this, TodoDetailActivity.class);
		 startActivity(i);
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETED_ID, 0, R.string.str_menu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case DELETED_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			
			Uri uri = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/" + info.id);
			
			this.getContentResolver().delete(uri, null, null);
			fillData();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.todo_list);
		this.getListView().setDividerHeight(2);
		fillData();
		this.registerForContextMenu(getListView());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case R.id.insert:
			createTodo();
			return true;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, TodoDetailActivity.class);
		Uri todoUri = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/" + id);
		i.putExtra(MyTodoContentProvider.CONTENT_ITEM_TYPE, todoUri);
		
		startActivity(i);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		String[] projection = {TodoTable.COLUMN_ID, TodoTable.COLUMN_SUMMARY};
		CursorLoader cursorLoader = new CursorLoader(this, MyTodoContentProvider.CONTENT_URI, projection, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		adapter.swapCursor(null);
	}

}
