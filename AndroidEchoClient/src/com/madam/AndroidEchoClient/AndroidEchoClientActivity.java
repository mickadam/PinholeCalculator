package com.madam.AndroidEchoClient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AndroidEchoClientActivity extends Activity implements OnClickListener {
    private Button buttonConnect;
	private Button buttonSend;
	private EditText editTextIP;
	private EditText editTextPort;
	private EditText editTextText2Send;
	private EditText editTextResponse;
	private AndroidEchoClient client;
	private Boolean bConnected = false;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        buttonConnect = (Button) this.findViewById(R.id.buttonConnect);
        buttonSend = (Button) this.findViewById(R.id.buttonSend);
        
        editTextIP = (EditText) this.findViewById(R.id.editTextIP);
        editTextPort = (EditText) this.findViewById(R.id.editTextPort);
        editTextText2Send = (EditText) this.findViewById(R.id.editTextText2Send);
        editTextResponse = (EditText) this.findViewById(R.id.editTextResponse);
        
        buttonConnect.setOnClickListener(this);
        buttonSend.setOnClickListener(this);
        
        editTextIP.setText("192.168.56.1");
        editTextPort.setText("80");
        editTextText2Send.setText("test");
        
        client = new AndroidEchoClient(this);
    }

	public void onClick(View view) 
	{
		if ( view == buttonConnect)
		{
			if(!bConnected)
			{
				Boolean res = client.connect(editTextIP.getText().toString(),editTextPort.getText().toString());
				if(res)
				{
					buttonConnect.setText("Déconnection du serveur");
					bConnected = true;
				}
			}else
			{
				client.close();
				buttonConnect.setText("Connection au Serveur");
				bConnected = false;
			}
		}
		if ( view == buttonSend)
		{
			editTextResponse.setText(editTextResponse.getText().toString()+"\n"+client.send(editTextText2Send.getText().toString()));
		}
	}
	@Override
	public void finish() 
	{
		client.close();
		super.finish();
	}
}
