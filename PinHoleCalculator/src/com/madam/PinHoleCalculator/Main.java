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

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.madam.PinHoleCalculator2.R;

/**
 * The Class Main.
 */
public class Main extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	LinearLayout Mainlayout;
	TextView txtVHead;

	Button calculateDiameterFocalButton;
	Button expositionButton;
	Button myPinholeCollectionButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setVolumeControlStream(AudioManager.STREAM_SYSTEM);
		setContentView(R.layout.main);

		calculateDiameterFocalButton = (Button) this
				.findViewById(R.id.btnDiamFoc);
		expositionButton = (Button) this.findViewById(R.id.btnMesExp);
		myPinholeCollectionButton = (Button) this
				.findViewById(R.id.btnMesStenop);

		calculateDiameterFocalButton.setOnClickListener(this);
		expositionButton.setOnClickListener(this);
		myPinholeCollectionButton.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		if (v == calculateDiameterFocalButton) {
			Intent intent = new Intent(Main.this, DiamFoc.class);
			startActivity(intent);
		} else {
			if (v == expositionButton) {
				Intent intent = new Intent(Main.this, Exposition.class);
				startActivity(intent);
			}
			if (v == myPinholeCollectionButton) {
				Intent intent = new Intent(Main.this, MesStenopes.class);
				startActivity(intent);
			}
		}
	}

	/**
	 * Image toast.
	 *
	 * @param view
	 *            the view
	 */
	public void imageToast(View view) {
		Toast.makeText(this, R.string.imageText, Toast.LENGTH_LONG).show();
	}

}