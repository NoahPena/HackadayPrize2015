package com.example.cargestureproject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.widget.Toast;

public class BluetoothShit extends Activity
{
	private static final String DEVICE_NAME = "Car-Gesture";
	
	BluetoothAdapter mBluetoothAdapter;
	BluetoothDevice mBluetoothDevice;
	BluetoothSocket mBluetoothSocket;
	
	InputStream mInputStream;
	Thread workerThread;
	byte[] readBuffer;
	int readBufferPosition;
	int counter;
	volatile boolean stopWorker;
	
	boolean bluetoothEnabled = false;
	
	public BluetoothShit()
	{
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		bluetoothEnabled = false;
	}

	public boolean bluetoothOn()
	{
		//mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		bluetoothEnabled = true;
		
		if(mBluetoothAdapter == null)
		{
			return false;
		}
		else
		{
			if(!mBluetoothAdapter.isEnabled())
			{
				mBluetoothAdapter.enable();
				Toast.makeText(getApplicationContext(), "Bluetooth Enabled", Toast.LENGTH_SHORT).show();
			}
		}
		
		return true;
	}
	
	public boolean bluetoothOff()
	{
		if((mBluetoothAdapter == null) || (!bluetoothEnabled))
		{
			return false;
		}
		else
		{
			if(!mBluetoothAdapter.isEnabled())
			{
				mBluetoothAdapter.disable();
				Toast.makeText(getApplicationContext(), "Bluetooth Disabled", Toast.LENGTH_SHORT).show();
			}
		}
			
		return true;
	}
	
	void openBT() throws IOException
	{
	    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
	    mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);        
	    mBluetoothSocket.connect();
	    
	    mInputStream = mBluetoothSocket.getInputStream();

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
	                                    	Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
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
	
	public void searchDevice()
	{
		mBluetoothAdapter.startDiscovery();
		
		Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
		//String names = null;
		
		if (devices != null)
		{
			for (BluetoothDevice device : devices)
			{
				//names += "\n" + device.getName();
				if(DEVICE_NAME.equals(device.getName()))
				{
					mBluetoothDevice = device;
					Toast.makeText(getApplicationContext(), device.getName() + " connected!", Toast.LENGTH_LONG).show();
					break;
				}
			}
		}
				
		mBluetoothAdapter.cancelDiscovery();
	}
	
	
}
