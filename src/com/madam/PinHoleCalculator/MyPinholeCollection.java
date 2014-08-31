/***************************************************************
 * 
 * Licensed under the Creative Commons.
 * Attribution-NonCommercial-ShareAlike 4.0 International License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/
 * 
 * Contributors:
 *  Mickaël ADAM - mickael.adam29@gmail.com - Initial API and implementation
 * 
 ***************************************************************/
package com.madam.PinHoleCalculator;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.madam.PinHoleCalculator.R;

// TODO: Auto-generated Javadoc
/**
 * The Class MyPinholeCollection.
 */
public class MyPinholeCollection extends ListActivity implements OnClickListener {
	/** Called when the activity is first created. */

	DBAdapter4Pinhole db;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.messtenopes);
		getListView().setOnCreateContextMenuListener(this);
		((Button) findViewById(R.id.buttonAdd)).setOnClickListener(this);

		db = new DBAdapter4Pinhole(this);
		db.open();
		DataBind();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		db.open();
		DataBind();
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	// Création du menu principal
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 200, 0, R.string.toutEffacer);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	// Selection d'un item du menu
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 200:
			db.Truncate();
			DataBind();
			break;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	@Override
	// Selection d'un item de la liste
	protected void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("unused")
		Cursor cursor = (Cursor) l.getAdapter().getItem(position);
		// String titre = cursor.getString(cursor.getColumnIndex("titre"));
		// Toast.makeText(this,"Item id "+id+" : ", Toast.LENGTH_SHORT).show();

		Intent intent = new Intent(this, Exposition.class);
		intent.putExtra("Diaphragme", db.get("diaphragme", (int) id));
		this.startActivity(intent);

		super.onListItemClick(l, v, position, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu,
	 * android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	// Creation du menu contextuel
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.action);
		menu.add(0, 200, 0, R.string.editer);
		menu.add(0, 100, 1, R.string.supprimer);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	// Selection d'un item du menu contextuel
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case 100:
			db.supprimerStenope(info.id);
			DataBind();
			break;
		case 200:
			// Edit
			Intent intent = new Intent(this, NewPinhole.class);
			int id = (int) info.id;
			intent.putExtra("dbItemId", id);
			this.startActivity(intent);
			break;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}

	/**
	 * Data bind.
	 */
	@SuppressWarnings("deprecation")
	public void DataBind() {
		Cursor cursor = db.recupererLaListeDesStenopes();
		startManagingCursor(cursor);

		String[] columns = new String[] { "nom", "description", "focale",
				"diaphragme", "URLimg" };
		int[] to = new int[] { R.id.stenope_Item_textNom,
				R.id.stenope_Item_TextDescription,
				R.id.stenope_Item_TextFocale, R.id.stenope_Item_TextDiaphragme,
				R.id.stenope_Item_ImageView };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.stenope_item, cursor, columns, to);
		// SimpleCursorAdapter adapter = new
		// CursorAdapter(this,R.layout.stenope_item,cursor,columns,to);

		setListAdapter(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		Intent intent = new Intent(MyPinholeCollection.this, NewPinhole.class);
		startActivity(intent);
	}

}