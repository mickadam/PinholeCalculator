package com.madam.PinHoleCalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.madam.PinHoleCalculator2.R;


public class NouveauStenope extends Activity implements OnClickListener{
	
	private DBAdapter4Stenope db;
	EditText EdTxtNom;
	EditText EdTxtDesc;
	EditText EdTxtOuverture;
	EditText EdTxtFocale;
	Boolean ModeModifier = false;
	File imageFile;
	
	private int dbItemId;
	private String chemin;
	private ImageView photoSteno;
	private Button btnSave;
	private Bitmap image;


	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.nouveaustenope);

		//((Button)findViewById(R.nvxStenope.btnSave)).setOnClickListener(this);		
		
		EdTxtNom = (EditText) this.findViewById(R.nvxStenope.EdTxtNom);
		EdTxtDesc = (EditText) this.findViewById(R.nvxStenope.EdTxtDesc);
		EdTxtOuverture = (EditText) this.findViewById(R.nvxStenope.EdTxtOuverture);
		EdTxtFocale = (EditText) this.findViewById(R.nvxStenope.EdTxtFocale);
		btnSave = (Button)findViewById(R.nvxStenope.btnSave);
		photoSteno = (ImageView) this.findViewById(R.nvxStenope.ImageView01);
		
		btnSave.setOnClickListener(this);
		photoSteno.setOnClickListener(this);
		
		db = new DBAdapter4Stenope(this);
	    db.open();
	    
		if(this.getIntent().getExtras()!=null){
			
			// Si on arrive de DiamFoc.java
			if (this.getIntent().getExtras().get("dbItemId")==null){
				EdTxtFocale.setText(""+this.getIntent().getExtras().get("Focale"));
				EdTxtOuverture.setText(""+this.getIntent().getExtras().get("Diaphragme"));
				
				//Pas d'angle de calculé.
				if (!this.getIntent().getExtras().get("Angle").equals("---")){
					EdTxtDesc.setText("Angle: "+this.getIntent().getExtras().get("Angle"));
				}
				
			}else{
				ModeModifier = true;
			    dbItemId =  this.getIntent().getExtras().getInt("dbItemId");
			    EdTxtNom.setText(""+db.get("nom", dbItemId));
			    EdTxtDesc.setText(""+db.get("description", dbItemId));
			    EdTxtOuverture.setText(""+db.get("diaphragme", dbItemId));
			    EdTxtFocale.setText(""+db.get("focale", dbItemId));
			    chemin = ""+NouveauStenope.this.getFilesDir()+"/"+dbItemId+".png";
			    afficherPhoto(chemin);
			}
		}
		
	}
	
//	@Override
//	protected void onResume() {
//		afficherPhoto(chemin);
//		super.onResume();
//	}
	
	@Override
	protected void onDestroy() {
		//Supression de l'ancien fichier /image.jpg
		chemin = NouveauStenope.this.getFilesDir()+"/0.png";
	    imageFile = new File(chemin);
	    imageFile.delete();
	    db.close();
	    super.onDestroy();
	}
	
	
	public void onClick(View v) {
		
		if(v == btnSave){
			
			if (!ModeModifier){	
				dbItemId = (int) db.insererUnStenope(""+EdTxtNom.getText(),
						""+EdTxtDesc.getText(),
						""+EdTxtOuverture.getText(),
						""+EdTxtFocale.getText(), "");
				//Sauver l'URL en ID.png
				db.set("URLimg",""+NouveauStenope.this.getFilesDir()+"/"+dbItemId+".png",dbItemId);

				//Modifier le nom du fichier
				if (chemin!=null) {
					imageFile = new File(chemin);
					File newPath = new File(""+NouveauStenope.this.getFilesDir()+"/"+dbItemId+".png");
					imageFile.renameTo(newPath);
					chemin = ""+NouveauStenope.this.getFilesDir()+"/"+dbItemId+".png";
					//chemin = ""+dbItemId+".png";
					//afficherPhoto(chemin);
				}
				
			}else{
				chemin = ""+NouveauStenope.this.getFilesDir()+"/"+dbItemId+".png";
				db.modifierUnStenope((Integer)dbItemId,
						""+EdTxtNom.getText(),
						""+EdTxtDesc.getText(),
						""+EdTxtOuverture.getText(),
						""+EdTxtFocale.getText(),
						chemin);
			}
			
			Toast.makeText(this, ""+EdTxtNom.getText()+" sauvegardé", Toast.LENGTH_SHORT).show(); //TODO translate
			finish();
		}else{
			if(v == photoSteno){
				takePhoto();
			}
		}
	}
	
	
	private void afficherPhoto(String URL){
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap bm = BitmapFactory.decodeFile(URL, options);
		if(bm!=null){
			photoSteno.setBackgroundResource(0); 	// remove the background
			photoSteno.setImageBitmap(bm);			//set the bmp	
		}
	}
	
	private static final int TAKE_PHOTO_CODE = 1;

	private void takePhoto(){
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, TAKE_PHOTO_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch(requestCode){
			case TAKE_PHOTO_CODE:
				// Toast.makeText(this,"-1-",Toast.LENGTH_SHORT);
				if (data != null) {
					//  Toast.makeText(this,"-2-",Toast.LENGTH_SHORT);
					image = (Bitmap) data.getExtras().get("data");

					// Je sérialize...  MODE_PRIVATE correspond à la mémoire interne / Le format de compression de mon bitmap est PNG
					FileOutputStream fos = null;
					try {
						fos = openFileOutput(""+dbItemId+".png", Context.MODE_PRIVATE); //this.MODE_PRIVATE
						chemin = ""+NouveauStenope.this.getFilesDir()+"/"+dbItemId+".png";
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					image.compress(Bitmap.CompressFormat.PNG, 90, fos);

					try {
						fos.flush();
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					photoSteno.setBackgroundResource(0); 	// remove the background
					photoSteno.setImageBitmap(image);		//set the bmp	
				}

				break;
			}
		}
	}
}


