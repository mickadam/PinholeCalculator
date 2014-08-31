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

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madam.PinHoleCalculator.R;

/**
 * The Class DiamFoc.
 */
public class DiamFoc extends Activity implements OnKeyListener {

	/** The layout main. */
	LinearLayout layoutMain;

	/** The layout diameter. */
	LinearLayout layoutDiameter;

	/** The layout focal. */
	LinearLayout layoutFocal;

	/** The ed txt diameter. */
	EditText edTxtDiameter;

	/** The btn diam mm. */
	Button btnDiameterMM;

	/** The Optimal focal. */
	TextView optimalFocal;

	/** The ed txt focal. */
	EditText edTxtFocal;

	/** The btn focal mm. */
	Button btnFocalMM;

	/** The Optimal diameter. */
	private TextView optimalDiameter;

	/** The txt diaph. */
	private TextView txtDiaph;

	/** The txt angle. */
	private TextView txtAngle;

	/** The ed txt Angle x. */
	private EditText edTxtAngleX;

	/** The ed txt Angle y. */
	private EditText edTxtAngleY;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diamfoc);

		optimalFocal = (TextView) this.findViewById(R.id.TextViewFocOpt);
		edTxtDiameter = (EditText) this.findViewById(R.id.EdTxtDiam);

		optimalDiameter = (TextView) this.findViewById(R.id.TextViewDiamOpt);
		edTxtFocal = (EditText) this.findViewById(R.id.EdTxtFoc);

		txtDiaph = (TextView) this.findViewById(R.id.TextViewFNum);

		txtAngle = (TextView) this.findViewById(R.id.TextViewAngle);
		edTxtAngleX = (EditText) this.findViewById(R.id.EdTxtX);
		edTxtAngleY = (EditText) this.findViewById(R.id.EdTxtY);

		edTxtDiameter.setOnKeyListener(this);
		edTxtFocal.setOnKeyListener(this);
		edTxtAngleX.setOnKeyListener(this);
		edTxtAngleY.setOnKeyListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	// Create the main menu
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 100, 0, R.string.sauver);
		return true;
	}

	/*
	 * Selection of an menu item
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 100:
			Intent intent = new Intent(this, NewPinhole.class);
			intent.putExtra("Focale", edTxtFocal.getText().toString());
			intent.putExtra("Diaphragme", txtDiaph.getText().toString());
			intent.putExtra("Angle", txtAngle.getText().toString());
			this.startActivity(intent);
			break;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnKeyListener#onKey(android.view.View, int,
	 * android.view.KeyEvent)
	 */
	public boolean onKey(View view, int arg1, KeyEvent arg2) {
		// Define the decimal format to display diameter and focal
		DecimalFormat df = new DecimalFormat("@@@");
		// if edition of the diameter
		if (view == edTxtDiameter) {
			// Calculate the focal
			float calculatedFocal = calculFocal(edTxtDiameter.getText());
			if (calculatedFocal != -1) {
				// Set Optimal focal with good format
				optimalFocal.setText(df.format(calculatedFocal));
			}
		} else {
			if ((view == edTxtFocal)) {
				float calculatedDiametre = calculDiametre(edTxtFocal.getText());
				if (calculatedDiametre != -1) {
					optimalDiameter.setText(df.format(calculatedDiametre));
				}
				float calculatedAngle = calculAngle(edTxtAngleX.getText(),
						edTxtAngleX.getText(), edTxtFocal.getText());
				if (calculatedAngle != -1) {
					txtAngle.setText(df.format(calculatedAngle) + "°");
				}
			} else {
				if (view == edTxtAngleX || view == edTxtAngleY) {
					float calculatedAngle = calculAngle(edTxtAngleX.getText(),
							edTxtAngleY.getText(), edTxtFocal.getText());
					if (calculatedAngle != -1) {
						txtAngle.setText(df.format(calculatedAngle) + "°");
					}
				}
			}
		}

		float calculatedFNumber = calculFNumber(edTxtFocal.getText(),
				edTxtDiameter.getText());
		if (calculatedFNumber != -1) {
			txtDiaph.setText(df.format(calculatedFNumber));
		}
		return false;
	}

	/**
	 * Calculate focal.
	 * 
	 * @param diametre
	 *            the diametre
	 * @return the float
	 */
	public float calculFocal(Editable diametre) {
		String str = new String();
		str = diametre.toString();

		String str2Comp = new String();
		str2Comp = "";

		if (!str.equals(str2Comp)) {
			try {
				Float fDiam = Float.valueOf(str);
				Float lightWaveLgth = Float
						.valueOf(getString(R.attr.lightWaveLength));
				Float cst = Float.valueOf(getString(R.attr.cstLordRayLeigh));
				return ((fDiam / cst) * (fDiam / cst)) / lightWaveLgth;
			} catch (NumberFormatException ex) {
				return -1;
			}
		} else {
			return -1;
		}
	}

	/**
	 * Calcul diametre.
	 * 
	 * @param focal
	 *            the focal
	 * @return the float
	 */
	private float calculDiametre(Editable focal) {

		String strFoc = new String(focal.toString());
		String str2Comp = new String("");

		if (!strFoc.equals(str2Comp)) {
			try {
				Float fFoc = Float.valueOf(strFoc);
				Float lightWaveLgth = Float
						.valueOf(getString(R.attr.lightWaveLength));
				Float cst = Float.valueOf(getString(R.attr.cstLordRayLeigh));
				return cst * FloatMath.sqrt(fFoc * lightWaveLgth);
			} catch (NumberFormatException ex) {
				return -1;
			}
		} else {
			return -1;
		}
	}

	/**
	 * Calcul f number.
	 * 
	 * @param focale
	 *            the focale
	 * @param diametre
	 *            the diametre
	 * @return the float
	 */
	public float calculFNumber(Editable focale, Editable diametre) {
		String str = new String();
		str = diametre.toString();

		String str2 = new String();
		str2 = focale.toString();

		String str4Comp = new String();
		str4Comp = "";

		if (!str.equals(str4Comp) && !str2.equals(str4Comp)) {
			try {
				Float fDiam = Float.valueOf(str);
				Float fFoc = Float.valueOf(str2);

				return fFoc / fDiam;
			} catch (NumberFormatException ex) {
				return -1;
			}
		} else {
			return -1;
		}
	}

	/**
	 * Calcul angle.
	 * 
	 * @param X
	 *            the x
	 * @param Y
	 *            the y
	 * @param D
	 *            the d
	 * @return the float
	 */
	private float calculAngle(Editable X, Editable Y, Editable D) {
		// angle=2*ATAN(H/2D)
		String strX = new String(X.toString());
		String strY = new String(Y.toString());
		String strD = new String(D.toString());
		String str2Comp = new String("");

		if (!strX.equals(str2Comp) && !strY.equals(str2Comp)
				&& !strD.equals(str2Comp)) {
			Float fX = Float.valueOf(strX);
			Float fY = Float.valueOf(strY);
			Float fD = Float.valueOf(strD);
			Float fH = Float.valueOf(FloatMath.sqrt((fX * fX) + (fY * fY)));
			return (float) (2 * Math.atan(fH / (2 * fD)) * (180 / Math.PI));
		} else {
			return -1;
		}
	}
}
