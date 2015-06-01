package com.example.cargestureproject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	private static final String DEVICE_NAME = "Car-Gesture";

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
		searchDevice();
		pairDevice(mDevice);
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
	
	public void pairDevice(BluetoothDevice device)
	{
	    String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
	    Intent intent = new Intent(ACTION_PAIRING_REQUEST);
	    String EXTRA_DEVICE = "android.bluetooth.device.extra.DEVICE";
	    intent.putExtra(EXTRA_DEVICE, device);
	    String EXTRA_PAIRING_VARIANT = "android.bluetooth.device.extra.PAIRING_VARIANT";
	    int PAIRING_VARIANT_PIN = 1234;
	    intent.putExtra(EXTRA_PAIRING_VARIANT, PAIRING_VARIANT_PIN);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivity(intent);
	}
	
	public void searchDevice()
	{
		mBluetoothAdapter.startDiscovery();
		
		//mBluetoothAdapter.getProfileProxy(getApplicationContext(), listener, profile)
		//mBluetoothAdapter.getProfileProxy(getApplicationContext(), listener, profile);
		Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
		String names = null;
		
		if (devices != null)
		{
			for (BluetoothDevice device : devices)
			{
				names += "\n" + device.getName();
				if(DEVICE_NAME.equals(device.getName()))
				{
					mDevice = device;
					Toast.makeText(getApplicationContext(), device.getName() + " connected!", Toast.LENGTH_LONG).show();
					break;
				}
			}
		}
		
		//Toast.makeText(getApplicationContext(), names, Toast.LENGTH_LONG).show();
		
		mBluetoothAdapter.cancelDiscovery();
	}
	
	
}
