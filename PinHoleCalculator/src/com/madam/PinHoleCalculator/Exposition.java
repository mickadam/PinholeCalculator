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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madam.PinHoleCalculator2.R;

// TODO: Auto-generated Javadoc
/**
 * The Class Exposition.
 */
public class Exposition extends Activity implements OnClickListener,
		OnItemSelectedListener, SensorEventListener {

	/** The btn mesur. */
	private Button btnMesur;

	/** The Ed txt ev. */
	private EditText EdTxtEV;

	/** The Ed txt diaph in. */
	private EditText EdTxtDiaphIn;

	/** The Ed txt vitesse in. */
	private EditText EdTxtVitesseIn;

	/** The Ed txt iso in. */
	private EditText EdTxtIsoIn;

	/** The Ed txt diaph steno. */
	private EditText EdTxtDiaphSteno;

	/** The Ed txt iso steno. */
	private EditText EdTxtIsoSteno;

	/** The txt vitesse. */
	private TextView txtVitesse;

	/** The Types papier array. */
	private String[] TypesPapierArray;

	/** The spin types papier. */
	private Spinner spinTypesPapier;

	/** The Id papier. */
	private Integer IdPapier = 0;

	/** The my sensor manager. */
	private SensorManager mySensorManager;

	/** The light sensor. */
	private Sensor lightSensor;

	/** The current light. */
	private float currentLight = 0;

	/** The ev. */
	private float EV;

	/** The btn calc ev. */
	private Button btnCalcEV;

	/** The btn calc vit. */
	private Button btnCalcVit;

	/** The btn compteur. */
	private Button btnCompteur;

	/** The b run timer. */
	private boolean bRunTimer = false;

	/** The t timer. */
	private CountDownTimer tTimer;

	/** The temps restant. */
	protected long tempsRestant = 0;

	/** The btn test. */
	private Button btnTest;

	/** The layout test. */
	private LinearLayout layoutTest;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setVolumeControlStream(AudioManager.STREAM_SYSTEM);

		setContentView(R.layout.exposition);

		// --gestion du capteur de luminausité--//
		mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		// -------------------------------------//

		// en entrée
		btnMesur = (Button) this.findViewById(R.id.exposition_btnMesurer);
		btnCalcEV = (Button) this.findViewById(R.id.exposition_btnCalcEV);
		btnCalcVit = (Button) this.findViewById(R.id.bntCalcVit);
		btnCompteur = (Button) this.findViewById(R.id.bntCompteur);

		EdTxtEV = (EditText) this.findViewById(R.id.exposition_EdTxtEV);
		EdTxtDiaphIn = (EditText) this.findViewById(R.id.exposition_EdTxtfNIn);
		EdTxtVitesseIn = (EditText) this
				.findViewById(R.id.exposition_EdTxtVitesseIn);
		EdTxtIsoIn = (EditText) this.findViewById(R.id.exposition_EdTxtIsoIn);

		// valeurs du stenopé
		EdTxtDiaphSteno = (EditText) this.findViewById(R.id.exposition_EdTxtfN);
		EdTxtIsoSteno = (EditText) this.findViewById(R.id.exposition_EdTxtIso);
		txtVitesse = (TextView) this.findViewById(R.id.exposition_txtVitesse);

		// Le Spinner du papier
		TypesPapierArray = new String[11];
		TypesPapierArray[0] = "Aucun";
		TypesPapierArray[1] = "Ilford Delta Pro";
		TypesPapierArray[2] = "Ilford FP4 Plus";
		TypesPapierArray[3] = "Ilford HP5 Plus";
		TypesPapierArray[4] = "Ilford Pan F Plus";
		TypesPapierArray[5] = "Ilford XP2 Super";
		TypesPapierArray[6] = "Adox CHS Art 25";
		TypesPapierArray[7] = "Kodak 400TX";
		TypesPapierArray[8] = "Kodak TMY";
		TypesPapierArray[9] = "Kodak TMX";
		TypesPapierArray[10] = "Kodak E100S";

		spinTypesPapier = (Spinner) findViewById(R.id.exposition_TypePapier);
		// Pour les petit doitg :)
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, TypesPapierArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinTypesPapier.setAdapter(adapter);
		spinTypesPapier.setOnItemSelectedListener(this);
		// fin spinner
		btnCompteur.setOnClickListener(this);
		btnMesur.setOnClickListener(this);
		btnCalcEV.setOnClickListener(this);
		btnCalcVit.setOnClickListener(this);

		if (this.getIntent().getExtras() != null) {
			// if (this.getIntent().getExtras().get("Diaphragme")!=null) {
			EdTxtDiaphSteno.setText(""
					+ this.getIntent().getExtras().get("Diaphragme"));
			// }
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View view) {
		DecimalFormat df2 = new DecimalFormat("@@@@");

		if (view == btnMesur) {
			if (currentLight != 0) {
				EV = (float) (Math.log(currentLight / 2.5) / Math.log(2));
				EdTxtEV.setText("" + EV);
			} else {
				Toast.makeText(this, R.string.nonSupportee, Toast.LENGTH_SHORT)
						.show();
			}

		}
		if (view == btnCalcEV) {
			EdTxtEV.setText("" + df2.format(CalculeEV()));
		}
		if (view == btnCalcVit) {

			double vitesse = CalculeVitesse();
			showTimeLeft(vitesse);
			tempsRestant = 0;
		}
		if (view == btnCompteur) {
			GestionCompteur();
		}
		if (view == btnTest) {

			layoutTest.setVisibility(Math.abs(layoutTest.getVisibility()
					- View.GONE));

		}
	}

	/**
	 * Gestion compteur.
	 */
	@SuppressLint("UseValueOf")
	private void GestionCompteur() {

		long lVitesse;

		if (!bRunTimer) {
			if (tempsRestant == 0) {
				float fVitesse = new Float(CalculeVitesse());
				lVitesse = new Long((long) (fVitesse * 1000));
			} else {
				lVitesse = tempsRestant;
			}
			if (CalculeVitesse() != 0) {
				tTimer = new CountDownTimer(lVitesse, 100) {

					@Override
					public void onTick(long millisUntilFinished) {
						// txtVitesse.setText(""+millisUntilFinished / 100);
						showTimeLeft(((double) millisUntilFinished) / 1000);
						tempsRestant = millisUntilFinished;
						btnCompteur.setText(R.string.btnCompteurStop);
						bRunTimer = true;
					}

					@Override
					public void onFinish() {
						txtVitesse.setText(R.string.CompteurFini);
						btnCompteur.setText(R.string.btnCompteurStart);
						tempsRestant = 0;
						bRunTimer = false;
						joueSon(true);
					}
				}.start();
			}

		} else {
			tTimer.cancel();
			btnCompteur.setText(R.string.btnCompteurStart);
			bRunTimer = false;
		}

	}

	/**
	 * Joue son.
	 *
	 * @param b
	 *            the b
	 */
	protected void joueSon(boolean b) {
		new MediaPlayer();
		MediaPlayer mp = MediaPlayer.create(this, (R.raw.buzz));
		mp.setLooping(false);
		mp.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// Unregister the sensor listener
		mySensorManager.unregisterListener(this, lightSensor);
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// Register the sensor listener
		mySensorManager.registerListener(this, lightSensor,
				SensorManager.SENSOR_DELAY_GAME);
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {

		// Selection of the paper type
		Toast.makeText(
				parent.getContext(),
				this.getString(R.string.papier) + ": "
						+ parent.getItemAtPosition(pos).toString(),
				Toast.LENGTH_SHORT).show();

		IdPapier = pos;

		String sT = parent.getItemAtPosition(pos).toString();
		// Set the iso according to the name of the paper
		// TODO have good data structure of paper information
		if (sT.contains("400")) {
			EdTxtIsoSteno.setText("400");
		} else if (sT.contains("100")) {
			EdTxtIsoSteno.setText("100");
		} else if (sT.contains("3200")) {
			EdTxtIsoSteno.setText("3200");
		} else if (sT.contains("200")) {
			EdTxtIsoSteno.setText("200");
		}

	}

	/**
	 * Show the time left for the exposure.
	 *
	 * @param TimeLeft
	 *            the time left for the current exposure
	 */
	private void showTimeLeft(double TimeLeft) {
		DecimalFormat df = new DecimalFormat("#######");

		if (TimeLeft != 0) {

			String strTimeLeft = "";

			int hours = (int) TimeLeft / 3600;
			if (hours >= 1)
				strTimeLeft = "" + df.format(hours) + "h ";
			int minutes = (int) ((TimeLeft - 3600 * hours) / 60);
			if (minutes >= 1)
				strTimeLeft = strTimeLeft + df.format(minutes) + "m ";
			double seconds = TimeLeft - 3600 * hours - 60 * minutes;
			df.applyPattern("#######");
			if (seconds >= 0)
				strTimeLeft = strTimeLeft + df.format(seconds) + "s";

			txtVitesse.setText(strTimeLeft);
		} else {
			txtVitesse.setText("---");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android
	 * .widget.AdapterView)
	 */
	public void onNothingSelected(AdapterView<?> arg0) {
		// Do nothing
	}

	/**
	 * Log2.
	 *
	 * @param num
	 *            the num
	 * @return the float
	 */
	public static float log2(float num) {
		return (float) (Math.log(num) / Math.log(2));
	}

	/**
	 * Calculate the Exposition Value from .
	 *
	 * @return the double
	 */
	private double CalculeEV() {
		float t = 1;
		float Iso;
		float N;

		String str4Comp = new String();
		str4Comp = "";

		try {

			N = Float.valueOf(EdTxtDiaphIn.getText().toString());
			Iso = Float.valueOf(EdTxtIsoIn.getText().toString());

			String sT = EdTxtVitesseIn.getText().toString();

			// test du format de T
			boolean testDiv = sT.contains("/");

			if (!testDiv) {
				if (!sT.equals(str4Comp)) {
					t = Float.valueOf(sT);
				}
			} else {
				int i = sT.indexOf('/');
				sT = sT.substring(i + 1);

				if (!sT.equals(str4Comp))
					t = 1 / Float.valueOf(sT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

		return EV = log2((N * N) / (t)) - log2(Iso / 100);
	}

	/**
	 * Calcule vitesse.
	 *
	 * @return the double
	 */
	private double CalculeVitesse() {
		double calc = 0;

		String str2Comp = new String();
		str2Comp = "";
		double Vitesse = 0;

		if (!str2Comp.equals(EdTxtDiaphSteno.getText().toString())
				&& !str2Comp.equals(EdTxtIsoSteno.getText().toString())
				&& !str2Comp.equals(EdTxtEV.getText().toString())) {

			try {
				float N = Float.valueOf(EdTxtDiaphSteno.getText().toString());
				float Iso = Float.valueOf(EdTxtIsoSteno.getText().toString());

				float Ev = Float.valueOf(new String(EdTxtEV.getText()
						.toString().replace(",", ".")));

				calc = -(Ev + log2(Iso / 100) - log2(N * N));

				Vitesse = (float) Math.pow(2, calc);

			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}

			// Prise en compte de la réciprocité
			if (Vitesse > 1) {
				double a;
				double b;
				// TypesPapierArray[0]="Aucun";
				// TypesPapierArray[1]="Ilford Delta Pro";
				// TypesPapierArray[2]="Ilford FP4 Plus";
				// TypesPapierArray[3]="Ilford HP5 Plus";
				// TypesPapierArray[4]="Ilford Pan F Plus";
				// TypesPapierArray[5]="Ilford XP2 Super";
				// TypesPapierArray[6]="Adox CHS Art 25";
				// TypesPapierArray[7]="Kodak 400TX";
				// TypesPapierArray[8]="Kodak TMY";
				// TypesPapierArray[9]="Kodak TMX";
				// TypesPapierArray[10]="Kodak E100S";
				switch (IdPapier) {
				case 0:
					// Aucun papier
					break;
				case 1:// Ilford Delta Pro
					a = 0.046; // 100Dela
					b = 1.62;
					Vitesse = (float) (a * Math.pow(Vitesse, b) + Vitesse); // (tadj
																			// -
																			// tmeas)
																			// =
																			// a
																			// *
																			// (tmeas
																			// ^
																			// b)
					break;
				case 2:// Ilford FP4 Plus
					b = 1.48;
					Vitesse = (float) (Math.pow(Vitesse, b)); // tcorrigé = t
																// ^1.48
					break;
				case 3:// Ilford HP5 Plus
					b = 1.48;
					Vitesse = (float) (Math.pow(Vitesse, b)); // tcorrigé = t
																// ^1.48
					break;
				case 4:// Ilford Pan F Plus
					b = 1.48;
					Vitesse = (float) (Math.pow(Vitesse, b)); // tcorrigé = t
																// ^1.48
					break;
				case 5:// Ilford XP2 Super
					b = 1.48;
					Vitesse = (float) (Math.pow(Vitesse, b)); // tcorrigé = t
																// ^1.48
					break;
				case 6:// "Adox CHS Art 25
					Vitesse = (float) (0.134353883987194 * Math.pow(Vitesse,
							1.34968890243641) + Vitesse);
					break;
				case 7:// Kodak 400TX
					a = 0.169;
					b = 1.62;
					Vitesse = (float) (a * Math.pow(Vitesse, b) + Vitesse);
					break;
				case 8:// "Kodak TMY
					a = 0.061;
					b = 1.62;
					Vitesse = (float) (a * Math.pow(Vitesse, b) + Vitesse);
					break;
				case 9:// Kodak TMX
					a = 0.069;
					b = 1.62;
					Vitesse = (float) (a * Math.pow(Vitesse, b) + Vitesse);
					break;
				case 10:// Kodak E100S
					a = 0.046;
					b = 1.62;
					Vitesse = (float) (Math.pow(Vitesse + 1.0, 1.0 / 0.96) - 1.0);
					break;

				default:
					break;
				}
			}
		}
		return Vitesse;
	}

	// ==Source==//
	// ---------------------------------------------------------------------------------------------
	// Les ingénieurs d'ILford auraient calculé la formule suivante pour la
	// réciprocité du FP4 et du HP5
	// tcorrigé = t ^1.48 (cf
	// http://www.greenspun.com/bboard/q-and-a-fetch-msg.tcl?msg_id=006PxQ ): OK
	// ---------------------------------------------------------------------------------------------
	// http://www.apug.org/forums/forum37/11566-reciprocity-misbehavior.html
	// formule : tc = tc,1 *(tm^1.62) + tm
	// avec
	// tm : temps mesuré
	// tc : correction (pas le temps corrigé, mais la correction a apporter en
	// secondes)
	// tc,1 = le temps corrigé pour 1 seconde
	// le facteur 1.62 est précis pour les films suivants
	// 400TX(0.169)
	// TMY(0.061),
	// TMX(0.069),
	// HP5+(0.101) // ou pas
	// 100Delta(0.046). : OK
	// ---------------------------------------------------------------------------------------------
	// Réciprocité du E100S
	// http://www.apug.org/forums/archive/index.php/t-23351.html
	// tcorrigé (secondes) = Math.pow(t+1.0, 1.0/0.96) -1.0
	// ---------------------------------------------------------------------------------------------

	// Old
	// Calcul de reciprocité pour les papier Ilford
	/**
	 * Reprocite ilford1.
	 * 
	 * @param vit
	 *            the vit
	 * @return the float
	 */
	@SuppressWarnings("unused")
	private float reprociteIlford1(double vit) {

		// tableau des temps corrigées suivant la valeur d'exposition
		// String[][] sTempsCorrigees =
		// {{"13.88","12.83","11.83","10.83","9.919","8.919","7.919","6.919","","","","","","",""},{"","","","","","","","","","","","","","",""}};

		double a0 = -0.0845647951367544;
		double a1 = 0.998757436078246;
		double a2 = 0.28579724392706;
		double a3 = -0.0117160339953655;
		double a4 = 0.000427778205482486;
		double a5 = -0.0000103079474477344;// e-05;
		double a6 = 0.000000155435790397744;// e-07;
		double a7 = -0.00000000140225408340445;// e-09;
		double a8 = 0.00000000000688970964188516;// e-12;
		double a9 = -0.0000000000000141355556089969;// e-14;

		float Vitesse = (float) (a0 + a1 * vit + a2 * vit * vit + a3 * vit
				* vit * vit + a4 * vit * vit * vit * vit + a5 * vit * vit * vit
				* vit * vit + a6 * vit * vit * vit * vit * vit * vit + a7 * vit
				* vit * vit * vit * vit * vit * vit + a8 * vit * vit * vit
				* vit * vit * vit * vit * vit + a9 * vit * vit * vit * vit
				* vit * vit * vit * vit * vit);
		return Vitesse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.hardware.SensorEventListener#onAccuracyChanged(android.hardware
	 * .Sensor, int)
	 */
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.hardware.SensorEventListener#onSensorChanged(android.hardware
	 * .SensorEvent)
	 */
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_LIGHT)
			currentLight = event.values[0];
	}

}
