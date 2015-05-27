package com.example.cargestureproject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends Activity 
{

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
		try {
			openBT();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void bluetoothOn()
	{
		if(mBluetoothAdapter == null)
		{
			return;
		}
		else
		{
			if(!mBluetoothAdapter.isEnabled())
			{
				mBluetoothAdapter.enable();
				Toast.makeText(getApplicationContext(), "Bluetooth Enabled", Toast.LENGTH_LONG).show();
			}
		}
		
		 	Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		    if(pairedDevices.size() > 0)
		    {
		        for(BluetoothDevice device : pairedDevices)
		        {
		            if(device.getName().equals("Nigga"))			//Please Change 
		            {
		                mDevice = device;
		                break;
		            }
		        }
		    }
		    Toast.makeText(getApplicationContext(), "Bluetooth Device Found", Toast.LENGTH_LONG).show();
	}
	
	public void bluetoothOff()
	{
		if(mBluetoothAdapter == null)
		{
			return;
		}
		else
		{
			if(mBluetoothAdapter.isEnabled())
			{
				mBluetoothAdapter.disable();
				Toast.makeText(getApplicationContext(), "Bluetooth Disabled", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	void openBT() throws IOException
	{
	    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
	    mSocket = mDevice.createRfcommSocketToServiceRecord(uuid);        
	    mSocket.connect();
	    //mOutputStream = mmSocket.getOutputStream();
	    mInputStream = mSocket.getInputStream();

	    beginListenForData();

	    Toast.makeText(getApplicationContext(), "Bluetooth Opened", Toast.LENGTH_LONG).show();
	    
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
	                                    	Toast.makeText(getApplicationContext(), "Data: " + data, Toast.LENGTH_LONG).show();
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

	void closeBT() throws IOException
	{
	    stopWorker = true;
	    //mmOutputStream.close();
	    mInputStream.close();
	    mSocket.close();
	    
	    //myLabel.setText("Bluetooth Closed");
	}
}
