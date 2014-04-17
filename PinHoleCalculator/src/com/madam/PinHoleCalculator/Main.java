package com.madam.PinHoleCalculator;

import java.util.HashSet;
import java.util.Set;
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
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.madam.PinHoleCalculator2.R;

public class Main extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	
	LinearLayout Mainlayout;
	TextView txtVHead;
	
	Button btnDiamFoc;
	Button btnExpo;
	Button btnMesStenop;
	
	//DiamFoc diamFoc;
	//Exposition ExpoActivity;

	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        this.setVolumeControlStream(AudioManager.STREAM_SYSTEM);  
        setContentView(R.layout.main);
        
        btnDiamFoc = (Button) this.findViewById(R.id.btnDiamFoc);
        btnExpo = (Button) this.findViewById(R.id.btnMesExp);
        btnMesStenop = (Button) this.findViewById(R.id.btnMesStenop);
        
//       Mainlayout = new LinearLayout(this);   
//       txtVHead = new TextView(this);
//       btnDiamFoc = new Button(this);
//       btnExpo = new Button(this);
//       btnMesStenop = new Button(this);
              
//       Mainlayout.setOrientation(LinearLayout.VERTICAL);
//       Mainlayout.addView(txtVHead);
//       Mainlayout.addView(btnDiamFoc);
//       Mainlayout.addView(btnExpo);
//       Mainlayout.addView(btnMesStenop);
       
//       txtVHead.setText("PinHoleCalculator by Mickael ADAM");
//       btnDiamFoc.setText("Diamètre & Focal");
//       btnExpo.setText("Mesure d'Exposition");
//       btnMesStenop.setText("Mes Sténopés");
       
//       setContentView(Mainlayout);
       
       btnDiamFoc.setOnClickListener(this);
       btnExpo.setOnClickListener(this);
       btnMesStenop.setOnClickListener(this);
       
       AdView adview = (AdView)findViewById(R.id.adView);
       AdRequest re = new AdRequest();
       //adview.loadAd(new AdRequest());
       
       Set<String> keywordSet=new HashSet<String>(); // on crée notre Set
       keywordSet.add(new String("Camera")); 
       keywordSet.add(new String("photo")); 
       keywordSet.add(new String("pinhole")); 
       keywordSet.add(new String("paper")); 
       
       re.setKeywords(keywordSet);
       adview.loadAd(re);
    }

	public void onClick(View v) 
	{
		if (v == btnDiamFoc) {
				Intent intent = new Intent(Main.this, DiamFoc.class);
				startActivity(intent);
			}else{
			if (v == btnExpo){
				Intent intent = new Intent(Main.this, Exposition.class);
				startActivity(intent);
			}
			if (v == btnMesStenop){
				Intent intent = new Intent(Main.this, MesStenopes.class);
				startActivity(intent);
			}
		}
	}
	
	public void ImageTaost(View view)
	{
		Toast.makeText(this,R.string.imageText, Toast.LENGTH_LONG).show();
	}

}