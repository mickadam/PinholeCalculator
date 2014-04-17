package com.madam.PinHoleCalculator;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBAdapter4Stenope {

	DatabaseHelper	DBHelper;
	Context			context;
	SQLiteDatabase	SQLdataBase;
	
	public DBAdapter4Stenope(Context context){
		this.context = context;
		DBHelper = new DatabaseHelper(context);
	}	
	
	public class DatabaseHelper extends SQLiteOpenHelper{

		Context	context;
		
		public DatabaseHelper(Context context) {
			super(context, "stenopes", null, 1);
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase SQLdataBase) {
			SQLdataBase.execSQL("create table if not exists stenopes (_id integer primary key, "
					+ "nom text not null," 
					+ "description text not null,"
					+ "focale text not null,"
					+ "diaphragme text not null,"
					+ "URLimg text"
					+ ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase SQLdataBase, int oldVersion, int newVersion) {
			Toast.makeText(context, "Mise à jour de la Base de données version "+oldVersion+" vers "+newVersion, Toast.LENGTH_SHORT).show();
			SQLdataBase.execSQL("DROP TABLE IF EXISTS stenopes");
			onCreate(SQLdataBase);
		}
		
	}
	
	public DBAdapter4Stenope open(){
		SQLdataBase = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		SQLdataBase.close();
	}

	public void Truncate(){
		
		// suppression de la photo
		String chemin;
		Cursor curs = recupererLaListeDesStenopes();
		int nbItem = curs.getCount();
		for(int i = 1;i<=nbItem;i++){
			Cursor c = recupererUnStenope(i);
			c.moveToFirst();
			chemin = c.getString(c.getColumnIndex("URLimg"));
			File imageFile = new File(chemin);
			imageFile.delete();
		}
		
		SQLdataBase.execSQL("DELETE FROM stenopes");
	}
	
	public long insererUnStenope(String nom, String description, String diametre, String focale,String URLimg){
		ContentValues values = new ContentValues();
		values.put("nom", nom);
		values.put("description", description);
		values.put("focale", focale);
		values.put("diaphragme", diametre);
				
		return SQLdataBase.insert("stenopes", null, values);
	}
	
	
	public int modifierUnStenope(int id, String nom, String description, String diametre, String focale, String URLimg){
		ContentValues values = new ContentValues();
		values.put("nom", nom);
		values.put("description", description);
		values.put("focale", focale);
		values.put("diaphragme", diametre);
		values.put("URLimg", URLimg);
		
		return SQLdataBase.update("stenopes", values, "_id= " + id, null);
	}
	
	
	public boolean supprimerStenope(long id){
		
		// suppression de la photo
		String chemin;
		Cursor c = recupererUnStenope((int)id);
		c.moveToFirst();
		chemin = c.getString(c.getColumnIndex("URLimg"));
		c.close();
		File imageFile = new File(chemin);
	    imageFile.delete();
	    
		return SQLdataBase.delete("stenopes", "_id="+id, null)>0;
	}
	
	public Cursor recupererLaListeDesStenopes(){
		return SQLdataBase.query("stenopes", new String[]{
				"_id",
				"nom",
				"description",
				"focale",
				"diaphragme",
				"URLimg"}, null, null, null, null, null);
	}
	public Cursor recupererUnStenope(int id){
		return SQLdataBase.query("stenopes", new String[]{
				"_id",
				"nom",
				"description",
				"focale",
				"diaphragme","URLimg"}, "_id="+id, null, null, null, null);
	}
	//récupérer la valeur d'un champ grâce à son id
	public String get(String champ, int id){
		if (id==0)
			return null;
		
		Cursor c = recupererUnStenope(id);
		c.moveToFirst();
		
		String strGet = c.getString(c.getColumnIndex(champ));
		c.close();
		return strGet;

	}
	
	public int set(String champ,String valeur, int id){
		if (id==0)
			return -1;
		
		ContentValues values = new ContentValues();
		Cursor c = recupererUnStenope(id);
		
		c.moveToFirst();
		values.put(champ, valeur);
		
		return SQLdataBase.update("stenopes", values, "_id= " + id, null);
		
	}
}


