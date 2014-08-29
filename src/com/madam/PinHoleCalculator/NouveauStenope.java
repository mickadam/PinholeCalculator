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

import com.madam.PinHoleCalculator.R;

/**
 * The Class NouveauStenope.
 */
public class NouveauStenope extends Activity implements OnClickListener {

	/** The db. */
	private DBAdapter4Stenope db;

	/** The Ed txt nom. */
	EditText EdTxtNom;

	/** The Ed txt desc. */
	EditText EdTxtDesc;

	/** The Ed txt ouverture. */
	EditText EdTxtOuverture;

	/** The Ed txt focale. */
	EditText EdTxtFocale;

	/** The Mode modifier. */
	Boolean ModeModifier = false;

	/** The image file. */
	File imageFile;

	/** The db item id. */
	private int dbItemId;

	/** The chemin. */
	private String chemin;

	/** The photo steno. */
	private ImageView photoSteno;

	/** The btn save. */
	private Button btnSave;

	/** The image. */
	private Bitmap image;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.nouveaustenope);

		// ((Button)findViewById(R.nvxStenope.btnSave)).setOnClickListener(this);

		EdTxtNom = (EditText) this.findViewById(R.id.nvxStenope_EdTxtNom);
		EdTxtDesc = (EditText) this.findViewById(R.id.nvxStenope_EdTxtDesc);
		EdTxtOuverture = (EditText) this
				.findViewById(R.id.nvxStenope_EdTxtOuverture);
		EdTxtFocale = (EditText) this.findViewById(R.id.nvxStenope_EdTxtFocale);
		btnSave = (Button) findViewById(R.id.nvxStenope_btnSave);
		photoSteno = (ImageView) this.findViewById(R.id.nvxStenope_ImageView01);

		btnSave.setOnClickListener(this);
		photoSteno.setOnClickListener(this);

		db = new DBAdapter4Stenope(this);
		db.open();

		if (this.getIntent().getExtras() != null) {

			// Si on arrive de DiamFoc.java
			if (this.getIntent().getExtras().get("dbItemId") == null) {
				EdTxtFocale.setText(""
						+ this.getIntent().getExtras().get("Focale"));
				EdTxtOuverture.setText(""
						+ this.getIntent().getExtras().get("Diaphragme"));

				// Pas d'angle de calculé.
				if (!this.getIntent().getExtras().get("Angle").equals("---")) {
					EdTxtDesc.setText("Angle: "
							+ this.getIntent().getExtras().get("Angle"));
				}

			} else {
				ModeModifier = true;
				dbItemId = this.getIntent().getExtras().getInt("dbItemId");
				EdTxtNom.setText("" + db.get("nom", dbItemId));
				EdTxtDesc.setText("" + db.get("description", dbItemId));
				EdTxtOuverture.setText("" + db.get("diaphragme", dbItemId));
				EdTxtFocale.setText("" + db.get("focale", dbItemId));
				chemin = "" + NouveauStenope.this.getFilesDir() + "/"
						+ dbItemId + ".png";
				afficherPhoto(chemin);
			}
		}

	}

	// @Override
	// protected void onResume() {
	// afficherPhoto(chemin);
	// super.onResume();
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// Supression de l'ancien fichier /image.jpg
		chemin = NouveauStenope.this.getFilesDir() + "/0.png";
		imageFile = new File(chemin);
		imageFile.delete();
		db.close();
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {

		if (v == btnSave) {

			if (!ModeModifier) {
				dbItemId = (int) db.insererUnStenope("" + EdTxtNom.getText(),
						"" + EdTxtDesc.getText(),
						"" + EdTxtOuverture.getText(),
						"" + EdTxtFocale.getText(), "");
				// Sauver l'URL en ID.png
				db.set("URLimg", "" + NouveauStenope.this.getFilesDir() + "/"
						+ dbItemId + ".png", dbItemId);

				// Modifier le nom du fichier
				if (chemin != null) {
					imageFile = new File(chemin);
					File newPath = new File(""
							+ NouveauStenope.this.getFilesDir() + "/"
							+ dbItemId + ".png");
					imageFile.renameTo(newPath);
					chemin = "" + NouveauStenope.this.getFilesDir() + "/"
							+ dbItemId + ".png";
					// chemin = ""+dbItemId+".png";
					// afficherPhoto(chemin);
				}

			} else {
				chemin = "" + NouveauStenope.this.getFilesDir() + "/"
						+ dbItemId + ".png";
				db.modifierUnStenope(dbItemId, "" + EdTxtNom.getText(), ""
						+ EdTxtDesc.getText(), "" + EdTxtOuverture.getText(),
						"" + EdTxtFocale.getText(), chemin);
			}

			Toast.makeText(this, "" + EdTxtNom.getText() + " sauvegardé",
					Toast.LENGTH_SHORT).show(); // TODO translate
			finish();
		} else {
			if (v == photoSteno) {
				takePhoto();
			}
		}
	}

	/**
	 * Afficher photo.
	 * 
	 * @param URL
	 *            the url
	 */
	private void afficherPhoto(String URL) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap bm = BitmapFactory.decodeFile(URL, options);
		if (bm != null) {
			photoSteno.setBackgroundResource(0); // remove the background
			photoSteno.setImageBitmap(bm); // set the bmp
		}
	}

	/** The Constant TAKE_PHOTO_CODE. */
	private static final int TAKE_PHOTO_CODE = 1;

	/**
	 * Take photo.
	 */
	private void takePhoto() {
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, TAKE_PHOTO_CODE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PHOTO_CODE:
				// Toast.makeText(this,"-1-",Toast.LENGTH_SHORT);
				if (data != null) {
					// Toast.makeText(this,"-2-",Toast.LENGTH_SHORT);
					image = (Bitmap) data.getExtras().get("data");

					// Je sérialize... MODE_PRIVATE correspond à la mémoire
					// interne / Le format de compression de mon bitmap est PNG
					FileOutputStream fos = null;
					try {
						fos = openFileOutput("" + dbItemId + ".png",
								Context.MODE_PRIVATE); // this.MODE_PRIVATE
						chemin = "" + NouveauStenope.this.getFilesDir() + "/"
								+ dbItemId + ".png";
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

					photoSteno.setBackgroundResource(0); // remove the
															// background
					photoSteno.setImageBitmap(image); // set the bmp
				}

				break;
			}
		}
	}
}
