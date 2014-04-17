package com.madam.PinHoleCalculator;

import java.text.DecimalFormat;

import com.madam.PinHoleCalculator2.R;

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

public class DiamFoc  extends Activity implements OnKeyListener {
	
	LinearLayout layoutMain;
	LinearLayout layoutDiam;
	LinearLayout layoutFoc;
		
	EditText edTxtDiam;
	Button btnDiamMM;
	TextView OptimalFoc;
	
	EditText edTxtFoc;
	Button btnFocMM;
	private TextView OptimalDiam;
	
	private TextView txtDiaph;
	private TextView txtAngle;
	
	private EditText edTxtX;
	private EditText edTxtY;

	@Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.diamfoc);
       
       //txtDiam = (TextView) this.findViewById(R.id.TextViewDiam);
       OptimalFoc = (TextView) this.findViewById(R.id.TextViewFocOpt);
       edTxtDiam = (EditText) this.findViewById(R.id.EdTxtDiam);

       //txtFoc = (TextView) this.findViewById(R.id.TextViewFocal);
       OptimalDiam = (TextView) this.findViewById(R.id.TextViewDiamOpt);
       edTxtFoc = (EditText) this.findViewById(R.id.EdTxtFoc);
       
       txtDiaph = (TextView) this.findViewById(R.id.TextViewFNum);
       
       txtAngle = (TextView) this.findViewById(R.id.TextViewAngle);
       edTxtX = (EditText) this.findViewById(R.id.EdTxtX);
       edTxtY = (EditText) this.findViewById(R.id.EdTxtY);

       edTxtDiam.setOnKeyListener(this);
       edTxtFoc.setOnKeyListener(this);
       edTxtX.setOnKeyListener(this);
       edTxtY.setOnKeyListener(this);

    }
	
    @Override // Création du menu principal
    public boolean onCreateOptionsMenu(Menu menu) {    	
    	menu.add(0,100,0,R.string.sauver);
    	return true;
    }
    
    @Override // Selection d'un item du menu
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case 100: 
    		Intent intent = new Intent(this, NouveauStenope.class);
    		intent.putExtra("Focale",edTxtFoc.getText().toString());
    		intent.putExtra("Diaphragme", txtDiaph.getText().toString());
    		intent.putExtra("Angle", txtAngle.getText().toString());
    		this.startActivity(intent);
    		break;    	
    	}
    	return true;
    }
	
	public boolean onKey(View view, int arg1, KeyEvent arg2) {
		
		DecimalFormat df = new DecimalFormat("@@@"); 
		float value;
		
		if ( view == edTxtDiam){
			value = calculFocal(edTxtDiam.getText());
			if (value != -1) {
				OptimalFoc.setText(df.format(value));
			}
		}else{
		if(( view == edTxtFoc)){
			value = calculDiametre(edTxtFoc.getText());
			if (value != -1) {
				OptimalDiam.setText(df.format(value));
			}
			value = calculAngle(edTxtX.getText(),edTxtX.getText(),edTxtFoc.getText());
			if (value != -1) {
				txtAngle.setText(df.format(value) + "°");
			}
		}else{
			if( view == edTxtX || view == edTxtY){
				value = calculAngle(edTxtX.getText(),edTxtY.getText(),edTxtFoc.getText());
				if (value != -1) {
					txtAngle.setText(df.format(value) + "°");
				}
			}
		}
		}
		
		value = calculFNumber(edTxtFoc.getText(),edTxtDiam.getText());
		if (value != -1) {
			txtDiaph.setText(df.format(value));
		}
		return false;
	}
	

	public float calculFocal(Editable diametre){
		String str = new String();
		str = diametre.toString();
		
		String str2Comp = new String();
		str2Comp = "";
				
		if (!str.equals(str2Comp)){
			try{
				Float fDiam = Float.valueOf(str);
				Float lightWaveLgth = Float.valueOf(getString(R.attr.lightWaveLength));
				Float cst = Float.valueOf(getString(R.attr.cstLordRayLeigh));
				return (float) (((fDiam/cst)*(fDiam/cst))/lightWaveLgth);
			}catch(NumberFormatException ex){
				return (float) -1;
			}
		}
		else{
			return (float) -1;
		}
	}
	
	private float calculDiametre(Editable focal){
		
		String strFoc = new String(focal.toString());	
		String str2Comp = new String("");
						
		if (!strFoc.equals(str2Comp)){
			try{
				Float fFoc = Float.valueOf(strFoc);
				Float lightWaveLgth = Float.valueOf(getString(R.attr.lightWaveLength));
				Float cst = Float.valueOf(getString(R.attr.cstLordRayLeigh));
				return (float) ((float) cst*FloatMath.sqrt(fFoc*lightWaveLgth));
			}catch(NumberFormatException ex){
				return (float) -1;
			}
		}
		else{
			return (float) -1;
		}
	}

	
	//Calcul de l'ouverture
	public float calculFNumber(Editable focale, Editable diametre){
		String str = new String();
		str = diametre.toString();
		
		String str2 = new String();
		str2 = focale.toString();	
		
		String str4Comp = new String();
		str4Comp = "";
				
		if (!str.equals(str4Comp) && !str2.equals(str4Comp)){
			try{
				Float fDiam = Float.valueOf(str);
				Float fFoc = Float.valueOf(str2);
							
				return (float) (fFoc/fDiam);
			}catch(NumberFormatException ex){
				return (float) -1;
			}
		}
		else{
			return (float) -1;
		}
	}

	private float calculAngle(Editable X, Editable Y, Editable D) {
		//angle=2*ATAN(H/2D)
		String strX = new String(X.toString());	
		String strY = new String(Y.toString());	
		String strD = new String(D.toString());	
		String str2Comp = new String("");
						
		if (!strX.equals(str2Comp) && !strY.equals(str2Comp) && !strD.equals(str2Comp)){
			Float fX = Float.valueOf(strX);
			Float fY = Float.valueOf(strY);
			Float fD = Float.valueOf(strD);
			Float fH = Float.valueOf(FloatMath.sqrt((fX*fX)+(fY*fY)));
			return (float) ((float) 2*Math.atan(fH/(2*fD)) * (180/Math.PI));
		}else{
			return -1;	
		}
	}
}
