package com.example.adk_test_led;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
 
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ToggleButton;
 
import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;

public class MainActivity extends Activity implements OnTouchListener{
 
	// TAG is used to debug in Android logcat console
	private static final String TAG = "ArduinoAccessory";
 
	private static final String ACTION_USB_PERMISSION = "com.example.adk_test_led.action.USB_PERMISSION";
 
	private UsbManager mUsbManager;
	private PendingIntent mPermissionIntent;
	private boolean mPermissionRequestPending;
	private View ViewForward;
 	private View ViewBackward;

	private Button buttonLeft;

	private Button buttonRight;
	
	UsbAccessory mAccessory;
	ParcelFileDescriptor mFileDescriptor;
	FileInputStream mInputStream;
	FileOutputStream mOutputStream;
 
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbAccessory accessory = UsbManager.getAccessory(intent);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						openAccessory(accessory);
					} else {
						Log.d(TAG, "permission denied for accessory "
								+ accessory);
					}
					mPermissionRequestPending = false;
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				UsbAccessory accessory = UsbManager.getAccessory(intent);
				if (accessory != null && accessory.equals(mAccessory)) {
					closeAccessory();
				}
			}
		}
	};

	private Button buttonLed;


 
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
		mUsbManager = UsbManager.getInstance(this);
		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		registerReceiver(mUsbReceiver, filter);
 
		if (getLastNonConfigurationInstance() != null) {
			mAccessory = (UsbAccessory) getLastNonConfigurationInstance();
			openAccessory(mAccessory);
		}
 
		setContentView(R.layout.activity_main);
		
		ViewForward = findViewById(R.id.Forward);
		ViewForward.setOnTouchListener(this);
		
		ViewBackward = findViewById(R.id.Backward);
		ViewBackward.setOnTouchListener(this);
		
		buttonLeft = (Button) findViewById(R.id.Left);
		buttonLeft.setOnTouchListener(this);
		
		buttonRight = (Button) findViewById(R.id.Right);
		buttonRight.setOnTouchListener(this);
		
		buttonLed = (Button) findViewById(R.id.Led);
		buttonLed.setOnTouchListener(this);
		
//		buttonForward.setOnTouchListener(new OnTouchListener() {
//		    @Override
//		    public boolean onTouch(View v, MotionEvent event) {
//		        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//		            Send((byte) 1);
//		        }
//		        else if (event.getAction() == MotionEvent.ACTION_UP) {
//		        	Send((byte) 0);
//		        }
//				return false;
//		    }
//		});
		
		
		
		
	}
 
	@Override
	public Object onRetainNonConfigurationInstance() {
		if (mAccessory != null) {
			return mAccessory;
		} else {
			return super.onRetainNonConfigurationInstance();
		}
	}
 
	@Override
	public void onResume() {
		super.onResume();
 
		if (mInputStream != null && mOutputStream != null) {
			return;
		}
 
		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (mUsbManager.hasPermission(accessory)) {
				openAccessory(accessory);
			} else {
				synchronized (mUsbReceiver) {
					if (!mPermissionRequestPending) {
						mUsbManager.requestPermission(accessory,mPermissionIntent);
						mPermissionRequestPending = true;
					}
				}
			}
		} else {
			Log.d(TAG, "mAccessory is null");
		}
	}
 
	@Override
	public void onPause() {
		super.onPause();
		closeAccessory();
	}
 
	@Override
	public void onDestroy() {
		unregisterReceiver(mUsbReceiver);
		super.onDestroy();
	}
 
	private void openAccessory(UsbAccessory accessory) {
		mFileDescriptor = mUsbManager.openAccessory(accessory);
		if (mFileDescriptor != null) {
			mAccessory = accessory;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();
			mInputStream = new FileInputStream(fd);
			mOutputStream = new FileOutputStream(fd);
			Log.d(TAG, "accessory opened");
		} else {
			Log.d(TAG, "accessory open fail");
		}
	}
 
 
	private void closeAccessory() {
		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}
		} catch (IOException e) {
		} finally {
			mFileDescriptor = null;
			mAccessory = null;
		}
	}
 
//	public void Forward(View v){
// 
//		byte[] buffer = new byte[1];
//		
//		buffer[0]=(byte)1; //button says off, light is on
//// 
////		if(buttonLED.isChecked())
////			buffer[0]=(byte)0; // button says on, light is off
////		else
////			buffer[0]=(byte)1; // button says off, light is on
//// 
//		if (mOutputStream != null) {
//			try {
//				mOutputStream.write(buffer);
//			} catch (IOException e) {
//				Log.e(TAG, "write failed", e);
//			}
//		}
//	}
//	
	public void Send(byte fontion, byte state){
		 
		byte[] buffer = new byte[2];
		
		buffer[0]= fontion; 
		buffer[1]= state;

		if (mOutputStream != null) {
			try {
				mOutputStream.write(buffer);
			} catch (IOException e) {
				Log.e(TAG, "write failed", e);
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		byte fonction = 0;
		byte state = 0;
			
		if (v== ViewForward)
			fonction = 1;
		else if (v== ViewBackward)
			fonction = 2;
		else if (v== buttonLeft)
			fonction = 3;
		else if (v== buttonRight)
			fonction = 4;
		else if (v== buttonLed)
			fonction = 5;
			
			
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			state = 1;
		}
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			state = 2;
		}
		else if (event.getAction() == MotionEvent.ACTION_HOVER_MOVE) {
			state = 3;
		}
		
		Send(fonction,state);
		
		return false;
	}
 
}