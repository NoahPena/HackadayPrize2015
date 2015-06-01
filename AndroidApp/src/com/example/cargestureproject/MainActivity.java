package com.example.cargestureproject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
		try {
			openBT();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//pairDevice(mDevice);
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
	
	void openBT() throws IOException
	{
	    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
	    mSocket = mDevice.createRfcommSocketToServiceRecord(uuid);        
	    mSocket.connect();
	    //mOutputStream = mSocket.getOutputStream();
	    mInputStream = mSocket.getInputStream();

	    beginListenForData();

	    Toast.makeText(getApplicationContext(), "Bluetooth Opened", Toast.LENGTH_LONG).show();
	    //myLabel.setText("Bluetooth Opened");
	}
	
	void beginListenForData()
	{
	    final Handler handler = new Handler(); 
	    final byte delimiter = 10; //This is the ASCII code for a newline character

	    stopWorker = false;
	    readBufferPosition = 0;
	    readBuffer = new byte[1024];
	    workerThread = new Thread(new Runnable()
	    {
	        public void run()
	        {                
	           while(!Thread.currentThread().isInterrupted() && !stopWorker)
	           {
	                try 
	                {
	                    int bytesAvailable = mInputStream.available();                        
	                    if(bytesAvailable > 0)
	                    {
	                        byte[] packetBytes = new byte[bytesAvailable];
	                        mInputStream.read(packetBytes);
	                        for(int i=0;i<bytesAvailable;i++)
	                        {
	                            byte b = packetBytes[i];
	                            if(b == delimiter)
	                            {
	     byte[] encodedBytes = new byte[readBufferPosition];
	     System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
	     final String data = new String(encodedBytes, "US-ASCII");
	     readBufferPosition = 0;

	                                handler.post(new Runnable()
	                                {
	                                    public void run()
	                                    {
	                                    	Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
	                                        //myLabel.setText(data);
	                                    }
	                                });
	                            }
	                            else
	                            {
	                                readBuffer[readBufferPosition++] = b;
	                            }
	                        }
	                    }
	                } 
	                catch (IOException ex) 
	                {
	                    stopWorker = true;
	                }
	           }
	        }
	    });

	    workerThread.start();
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
