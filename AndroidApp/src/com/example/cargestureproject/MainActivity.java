package com.example.cargestureproject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	private static final String TAG = "MyActivity";

	BluetoothAdapter mBluetoothAdapter;
	BluetoothDevice mDevice;
	BluetoothSocket mSocket;
	
	InputStream mInputStream;
	Thread workerThread;
	byte[] readBuffer;
	int readBufferPosition;
	int counter;
	volatile boolean stopWorker;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		bluetoothOn();
		
		/*try {
			openBT();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	public boolean bluetoothOn()
	{
		if(mBluetoothAdapter == null)
		{
			return false;
		}
		else
		{
			if(!mBluetoothAdapter.isEnabled())
			{
				mBluetoothAdapter.enable();
				Toast.makeText(getApplicationContext(), "Bluetooth Enabled", Toast.LENGTH_LONG).show();
			}
		}
		
		return true;
	}
	
	public boolean bluetoothOff()
	{
		if(mBluetoothAdapter == null)
		{
			return false;
		}
		else
		{
			if(mBluetoothAdapter.isEnabled())
			{
				mBluetoothAdapter.disable();
				Toast.makeText(getApplicationContext(), "Bluetooth Disabled", Toast.LENGTH_LONG).show();
			}
		}
		
		return true;
	}
}
