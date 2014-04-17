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
import com.madam.PinHoleCalculator2.R;

public class MesStenopes extends ListActivity implements OnClickListener {
    /** Called when the activity is first created. */
	
	DBAdapter4Stenope db;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.messtenopes);
        getListView().setOnCreateContextMenuListener(this);
        ((Button)findViewById(R.id.buttonAdd)).setOnClickListener(this);
        
        db = new DBAdapter4Stenope(this);
        db.open();
        DataBind();
    }
    
    @Override
    protected void onResume() {
    	db.open();
        DataBind();
    	super.onResume();
    }
    
    @Override // Création du menu principal
    public boolean onCreateOptionsMenu(Menu menu) {    	
    	menu.add(0,200,0,R.string.toutEffacer);
    	return true;
    }
    
    @Override // Selection d'un item du menu
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case 200:
    		db.Truncate();
    		DataBind();
    		break;    	
    	}
    	return true;
    }
    
	@Override // Selection d'un item de la liste
	protected void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("unused")
		Cursor cursor = (Cursor)l.getAdapter().getItem(position);
		//String titre  = cursor.getString(cursor.getColumnIndex("titre"));
		//Toast.makeText(this,"Item id "+id+" : ", Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(this, Exposition.class);
		intent.putExtra("Diaphragme",db.get("diaphragme",(int) id));
		this.startActivity(intent);
		
		super.onListItemClick(l, v, position, id);
	}
	
	@Override // Creation du menu contextuel
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.action);
		menu.add(0,200,0,R.string.editer);
		menu.add(0,100,1,R.string.supprimer);
		
	}
    
	@Override // Selection d'un item du menu contextuel
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()){
		case 100:
			db.supprimerStenope(info.id);
			DataBind();
			break;
		case 200:
			//Edit
			Intent intent = new Intent(this, NouveauStenope.class);
    		int id = (int) info.id;
    		intent.putExtra("dbItemId",id);
    		this.startActivity(intent);
			break;			
		}
		return true;
	}
	
    @Override
    protected void onDestroy() {
    	db.close();
    	super.onDestroy();
    }
    
    @SuppressWarnings("deprecation")
	public void DataBind(){
    	Cursor cursor = db.recupererLaListeDesStenopes();
    	startManagingCursor(cursor);
    	
    	String[] columns = new String[]{"nom","description","focale","diaphragme","URLimg"};
    	int[] to = new int[] {
    			R.stenope_Item.textNom,
    			R.stenope_Item.TextDescription,
    			R.stenope_Item.TextFocale,
    			R.stenope_Item.TextDiaphragme,
    			R.stenope_Item.ImageView};
    	
    	SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.stenope_item,cursor,columns,to);
    	//SimpleCursorAdapter adapter = new CursorAdapter(this,R.layout.stenope_item,cursor,columns,to);
    	
    	setListAdapter(adapter);
    }

	public void onClick(View v) {
		Intent intent = new Intent(MesStenopes.this, NouveauStenope.class);
		startActivity(intent);
	}
        
}