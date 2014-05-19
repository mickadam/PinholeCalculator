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

//Pour le simulateur de capteur:
//import org.openintents.sensorsimulator.hardware.Sensor;
//import org.openintents.sensorsimulator.hardware.SensorEvent;
//import org.openintents.sensorsimulator.hardware.SensorEventListener;
//import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

public class Exposition extends Activity implements OnClickListener,
		OnItemSelectedListener, SensorEventListener {

	private Button btnMesur;

	// private Camera camera;
	// private SurfaceView view;

	private EditText EdTxtEV;
	private EditText EdTxtDiaphIn;
	private EditText EdTxtVitesseIn;
	private EditText EdTxtIsoIn;
	private EditText EdTxtDiaphSteno;
	private EditText EdTxtIsoSteno;
	private TextView txtVitesse;
	private String[] TypesPapierArray;
	private Spinner spinTypesPapier;
	private Integer IdPapier = 0;

	// /** * Le sensor manager */
	private SensorManager mySensorManager;
	// Pour le simulateur de capteur:
	// private SensorManagerSimulator mySensorManager;
	private Sensor lightSensor;
	private float currentLight = 0;
	private float EV;

	private Button btnCalcEV;
	private Button btnCalcVit;
	private Button btnCompteur;
	private boolean bRunTimer = false;
	private CountDownTimer tTimer;
	protected long tempsRestant = 0;

	private Button btnTest;
	private LinearLayout layoutTest;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setVolumeControlStream(AudioManager.STREAM_SYSTEM);

		setContentView(R.layout.exposition);

		// --gestion du capteur de luminausité--//
		mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Pour le simulateur de capteur
		// mySensorManager = SensorManagerSimulator.getSystemService(this,
		// SENSOR_SERVICE);
		// mySensorManager.connectSimulator();

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

			// try {
			// if (camera == null){
			// camera = Camera.open();
			// }
			// if (camera != null){
			// camera.setParameters(camera.getParameters());
			// Parameters param = camera.getParameters();
			//
			// int ExpComp = camera.getParameters().getExposureCompensation();
			// float ExpCompStep =
			// camera.getParameters().getExposureCompensationStep();
			// param.setExposureCompensation(6);
			//
			// camera.setParameters(param);
			//
			// if (ExpComp*ExpCompStep==0){
			// Toast.makeText(this, R.string.nonSupportee,
			// Toast.LENGTH_SHORT).show();
			// }else{
			// EV = (float) (ExpComp*ExpCompStep);
			// EdTxtEV.setText(""+EV);
			// }
			// }
			//
			// } catch (Exception e) {
			// Toast.makeText(this, R.string.nonSupportee,
			// Toast.LENGTH_SHORT).show();
			// e.printStackTrace();
			// }
		}
		if (view == btnCalcEV) {
			EdTxtEV.setText("" + df2.format(CalculeEV()));
		}
		if (view == btnCalcVit) {

			double vitesse = CalculeVitesse();
			afficherVitesse(vitesse);
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

					public void onTick(long millisUntilFinished) {
						// txtVitesse.setText(""+millisUntilFinished / 100);
						afficherVitesse(((double) millisUntilFinished) / 1000);
						tempsRestant = millisUntilFinished;
						btnCompteur.setText(R.string.btnCompteurStop);
						bRunTimer = true;
					}

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

	protected void joueSon(boolean b) {
		new MediaPlayer();
		MediaPlayer mp = MediaPlayer.create(this, (R.raw.buzz));
		mp.setLooping(false);
		mp.start();
	}

	@Override
	protected void onPause() {
		// désenregistrer notre écoute du capteur
		mySensorManager.unregisterListener(this, lightSensor);
		// Pour le simulateur de capteur:
		// mySensorManager.unregisterListener(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		/* * enregistrer notre écoute du capteur */
		mySensorManager.registerListener(this, lightSensor,
				SensorManager.SENSOR_DELAY_GAME);
		// Pour le simulateur de capteur:
		// mySensorManager.registerListener(this,
		// mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
		// SensorManager.SENSOR_DELAY_FASTEST);
		super.onResume();
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {

		// selection of the paper type
		Toast.makeText(
				parent.getContext(),
				this.getString(R.string.papier) + ": "
						+ parent.getItemAtPosition(pos).toString(),
				Toast.LENGTH_SHORT).show();

		IdPapier = pos;

		String sT = parent.getItemAtPosition(pos).toString();
		// un iso dans le nom du papier
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

	// Show the speed
	private void afficherVitesse(double vitesse) {
		DecimalFormat df = new DecimalFormat("#######");

		if (vitesse != 0) {

			String strVitesse = "";

			if (vitesse < 1) {
				vitesse = 1 / vitesse;
				strVitesse = "1/" + df.format(vitesse) + " s";
			} else {
				int heures = (int) vitesse / 3600;
				if (heures >= 1)
					strVitesse = "" + df.format(heures) + "h ";
				int minutes = (int) ((vitesse - 3600 * heures) / 60);
				if (minutes >= 1)
					strVitesse = strVitesse + df.format(minutes) + "m ";
				double seconde = vitesse - 3600 * heures - 60 * minutes;
				df.applyPattern("#######.#");
				if (seconde >= 1)
					strVitesse = strVitesse + df.format(seconde) + "s";
			}
			txtVitesse.setText(strVitesse);
		} else {
			txtVitesse.setText("---");
		}
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// Do nothing
	}

	public static float log2(float num) {
		return (float) (Math.log(num) / Math.log(2));
	}

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

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_LIGHT)
			currentLight = event.values[0];
	}

}
