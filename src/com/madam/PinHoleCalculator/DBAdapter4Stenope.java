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

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * The Class DBAdapter4Stenope.
 */
public class DBAdapter4Stenope {

	/** The DB helper. */
	DatabaseHelper DBHelper;

	/** The context. */
	Context context;

	/** The SQ ldata base. */
	SQLiteDatabase SQLdataBase;

	/**
	 * Instantiates a new DB adapter4 stenope.
	 * 
	 * @param context
	 *            the context
	 */
	public DBAdapter4Stenope(Context context) {
		this.context = context;
		DBHelper = new DatabaseHelper(context);
	}

	/**
	 * The Class DatabaseHelper.
	 */
	public class DatabaseHelper extends SQLiteOpenHelper {

		/** The context. */
		Context context;

		/**
		 * Instantiates a new database helper.
		 * 
		 * @param context
		 *            the context
		 */
		public DatabaseHelper(Context context) {
			super(context, "stenopes", null, 1);
			this.context = context;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database
		 * .sqlite.SQLiteDatabase)
		 */
		@Override
		public void onCreate(SQLiteDatabase SQLdataBase) {
			SQLdataBase
					.execSQL("create table if not exists stenopes (_id integer primary key, "
							+ "nom text not null,"
							+ "description text not null,"
							+ "focale text not null,"
							+ "diaphragme text not null,"
							+ "URLimg text"
							+ ");");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database
		 * .sqlite.SQLiteDatabase, int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase SQLdataBase, int oldVersion,
				int newVersion) {
			Toast.makeText(
					context,
					"Mise à jour de la Base de données version " + oldVersion
							+ " vers " + newVersion, Toast.LENGTH_SHORT).show();
			SQLdataBase.execSQL("DROP TABLE IF EXISTS stenopes");
			onCreate(SQLdataBase);
		}

	}

	/**
	 * Open.
	 * 
	 * @return the DB adapter4 stenope
	 */
	public DBAdapter4Stenope open() {
		SQLdataBase = DBHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Close.
	 */
	public void close() {
		SQLdataBase.close();
	}

	/**
	 * Truncate.
	 */
	public void Truncate() {

		// suppression de la photo
		String chemin;
		Cursor curs = recupererLaListeDesStenopes();
		int nbItem = curs.getCount();
		for (int i = 1; i <= nbItem; i++) {
			Cursor c = recupererUnStenope(i);
			c.moveToFirst();
			chemin = c.getString(c.getColumnIndex("URLimg"));
			File imageFile = new File(chemin);
			imageFile.delete();
		}

		SQLdataBase.execSQL("DELETE FROM stenopes");
	}

	/**
	 * Inserer un stenope.
	 * 
	 * @param nom
	 *            the nom
	 * @param description
	 *            the description
	 * @param diametre
	 *            the diametre
	 * @param focale
	 *            the focale
	 * @param URLimg
	 *            the UR limg
	 * @return the long
	 */
	public long insererUnStenope(String nom, String description,
			String diametre, String focale, String URLimg) {
		ContentValues values = new ContentValues();
		values.put("nom", nom);
		values.put("description", description);
		values.put("focale", focale);
		values.put("diaphragme", diametre);

		return SQLdataBase.insert("stenopes", null, values);
	}

	/**
	 * Modifier un stenope.
	 * 
	 * @param id
	 *            the id
	 * @param nom
	 *            the nom
	 * @param description
	 *            the description
	 * @param diametre
	 *            the diametre
	 * @param focale
	 *            the focale
	 * @param URLimg
	 *            the UR limg
	 * @return the int
	 */
	public int modifierUnStenope(int id, String nom, String description,
			String diametre, String focale, String URLimg) {
		ContentValues values = new ContentValues();
		values.put("nom", nom);
		values.put("description", description);
		values.put("focale", focale);
		values.put("diaphragme", diametre);
		values.put("URLimg", URLimg);

		return SQLdataBase.update("stenopes", values, "_id= " + id, null);
	}

	/**
	 * Supprimer stenope.
	 * 
	 * @param id
	 *            the id
	 * @return true, if successful
	 */
	public boolean supprimerStenope(long id) {

		// suppression de la photo
		String chemin;
		Cursor c = recupererUnStenope((int) id);
		c.moveToFirst();
		chemin = c.getString(c.getColumnIndex("URLimg"));
		c.close();
		File imageFile = new File(chemin);
		imageFile.delete();

		return SQLdataBase.delete("stenopes", "_id=" + id, null) > 0;
	}

	/**
	 * Recuperer la liste des stenopes.
	 * 
	 * @return the cursor
	 */
	public Cursor recupererLaListeDesStenopes() {
		return SQLdataBase.query("stenopes", new String[] { "_id", "nom",
				"description", "focale", "diaphragme", "URLimg" }, null, null,
				null, null, null);
	}

	/**
	 * Recuperer un stenope.
	 * 
	 * @param id
	 *            the id
	 * @return the cursor
	 */
	public Cursor recupererUnStenope(int id) {
		return SQLdataBase.query("stenopes", new String[] { "_id", "nom",
				"description", "focale", "diaphragme", "URLimg" }, "_id=" + id,
				null, null, null, null);
	}

	/**
	 * Gets the value of a field thanks is id.
	 * 
	 * @param field
	 *            the field
	 * @param id
	 *            the id
	 * @return the string
	 */
	public String get(String field, int id) {
		if (id == 0)
			return null;

		Cursor cursor = recupererUnStenope(id);
		cursor.moveToFirst();

		String strGet = cursor.getString(cursor.getColumnIndex(field));
		cursor.close();
		return strGet;
	}

	/**
	 * Sets the value of a field thanks is id.
	 * 
	 * @param field
	 *            the field
	 * @param value
	 *            the value
	 * @param id
	 *            the id
	 * @return the int
	 */
	public int set(String field, String value, int id) {
		if (id == 0)
			return -1;

		ContentValues values = new ContentValues();
		Cursor c = recupererUnStenope(id);

		c.moveToFirst();
		values.put(field, value);

		return SQLdataBase.update("stenopes", values, "_id= " + id, null);

	}
}
