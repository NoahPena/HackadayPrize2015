package com.pena.noah.cargestureapplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;


public class BluetoothShit// extends Activity
{
	private static final String DEVICE_NAME = "Car-Gesture";
	
	BluetoothAdapter mBluetoothAdapter;
	BluetoothDevice mBluetoothDevice;
	BluetoothSocket mBluetoothSocket;
	
	Context mContext;
	Activity mActivity;
	Player mPlayer = null;
	
	MusicControlShit musicControl;
	PhoneControlShit phoneControl;
	
	InputStream mInputStream;
	Thread workerThread;
	byte[] readBuffer;
	int readBufferPosition;
	int counter;
	volatile boolean stopWorker;
	
	boolean bluetoothEnabled = false;
	
	public BluetoothShit(Context context, Activity activity)
	{
		GlobalVariables.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		mContext = context;
		mActivity = activity;
		
		bluetoothEnabled = false;
	}

	public BluetoothShit(Context context, Activity activity, Player player)
	{
		GlobalVariables.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		mContext = context;
		mActivity = activity;

		bluetoothEnabled = false;

		mPlayer = player;
	}

	public boolean bluetoothOn()
	{
		//GlobalVariables.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if(GlobalVariables.mBluetoothAdapter == null)
		{
			Toast.makeText(mContext, "Bluetooth Not Supported", Toast.LENGTH_LONG).show();
			return false;
		}

		if(GlobalVariables.mBluetoothAdapter.isEnabled())
		{
			bluetoothEnabled = true;
			return true;
		}
		else
		{
			Toast.makeText(mContext, "Please Turn Bluetooth On", Toast.LENGTH_LONG).show();
			return false;
		}

		/*
		bluetoothEnabled = true;
		
		if(GlobalVariables.mBluetoothAdapter == null)
		{
			return false;
		}
		else
		{
			if(!GlobalVariables.mBluetoothAdapter.isEnabled())
			{
				GlobalVariables.mBluetoothAdapter.enable();
				while(!GlobalVariables.mBluetoothAdapter.isEnabled());
				Toast.makeText(mContext, "Bluetooth Enabled", Toast.LENGTH_SHORT).show();
			}
		}
		
		return true;
		*/
	}
	
	public boolean bluetoothOff()
	{
		//if((GlobalVariables.mBluetoothAdapter == null) || (!bluetoothEnabled))
		//{
		//	return false;
		//}
		//else
		//{
		//	if(!GlobalVariables.mBluetoothAdapter.isEnabled())
		//	{
				GlobalVariables.mBluetoothAdapter.disable();
				Toast.makeText(mContext, "Bluetooth Disabled", Toast.LENGTH_SHORT).show();
		//	}
		//}
			
		return true;
	}
	
	void openBT() throws IOException
	{
	    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
	    GlobalVariables.mBluetoothSocket = GlobalVariables.mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
		if(GlobalVariables.mBluetoothSocket.isConnected())
		{
			GlobalVariables.mBluetoothSocket.close();
		}
		//GlobalVariables.mBluetoothSocket.close();
	    GlobalVariables.mBluetoothSocket.connect();
	    
	    mInputStream = GlobalVariables.mBluetoothSocket.getInputStream();

	    beginListenForData();

	    Toast.makeText(mContext, "Bluetooth Opened", Toast.LENGTH_LONG).show();
	}
	
	void beginListenForData()
	{
	    final Handler handler = new Handler(); 
	    final byte delimiter = 10; //This is the ASCII code for a newline character

	    musicControl = new MusicControlShit(mContext, mActivity, mPlayer);
	    phoneControl = new PhoneControlShit(mContext, mActivity);
	    
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
	                                    	switch(data.trim())
	                                    	{
	                                    	case "RIGHT":
	                                    			if(GlobalVariables.inCall && GlobalVariables.callReceiving)
	                                    			{
	                                    				//nothing
	                                    			}
	                                    			else if(GlobalVariables.incomingCall && GlobalVariables.callReceiving)
	                                    			{
														phoneControl.answerCall(mContext);
	                                    				//phoneControl.currentGesture = "RIGHT";
	                                    			}
	                                    			else
	                                    			{
	                                    				musicControl.skipTrack();
														Toast.makeText(mContext, "Skip Track", Toast.LENGTH_SHORT).show();
	                                    			}
	                                    		break;
	                                    		
	                                    	case "LEFT":
	                                    			if(GlobalVariables.inCall && GlobalVariables.callReceiving)
	                                    			{
	                                    				phoneControl.killCall(mContext);
	                                    			}
	                                    			else if(GlobalVariables.incomingCall && GlobalVariables.callReceiving)
	                                    			{
														phoneControl.killCall(mContext);
	                                    				//phoneControl.currentGesture = "LEFT";
	                                    			}
	                                    			else
	                                    			{
	                                    				musicControl.lastTrack();
														Toast.makeText(mContext, "Last Track", Toast.LENGTH_SHORT).show();
	                                    			}
	                                    		break;
	                                    		
	                                    	case "NEAR":
	                                    			if(GlobalVariables.inCall && GlobalVariables.callReceiving)
	                                    			{
	                                    				//nothing
	                                    			}
	                                    			else if(GlobalVariables.incomingCall && GlobalVariables.callReceiving)
	                                    			{
	                                    				//nothing
	                                    			}
	                                    			else
	                                    			{
	                                    				musicControl.pausePlayTrack();
														Toast.makeText(mContext, "Pause/Play Track", Toast.LENGTH_SHORT).show();

	                                    			}
	                                    		break;
	                                    		
	                                    	case "DOWN":
	                                    			if(GlobalVariables.inCall && GlobalVariables.callReceiving)
	                                    			{
	                                    				if(GlobalVariables.telephoneManager != null)
	                                    				{
	                                    					phoneControl.killCall(mContext);
	                                    				}
	                                    				else
	                                    				{
	                                    					Toast.makeText(mContext, "ERROR", Toast.LENGTH_LONG).show();
	                                    				}
	                                    			}
	                                    			else if(GlobalVariables.incomingCall && GlobalVariables.callReceiving)
	                                    			{
	                                    				//nothing
	                                    			}
	                                    			else
	                                    			{
	                                    				//nothing
	                                    			}
											break;

											case "UP":
												if(GlobalVariables.inCall && GlobalVariables.callReceiving)
												{

												}
												else if(GlobalVariables.incomingCall && GlobalVariables.callReceiving)
												{

												}
												else if(GlobalVariables.useSpotify)
												{
													musicControl.nextPlaylist();
													Toast.makeText(mContext, "Next Playlist", Toast.LENGTH_SHORT).show();
												}
												else
												{
													//nothing
												}
												break;

											case "FAR":
												if(GlobalVariables.inCall && GlobalVariables.callReceiving)
												{

												}
												else if(GlobalVariables.incomingCall && GlobalVariables.callReceiving)
												{

												}
												else
												{

												}

												break;


	                                    	default:
	                                    		Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show();
	                                    		break;
	                                    	}
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
	
	public boolean searchDevice()
	{
		GlobalVariables.mBluetoothAdapter.startDiscovery();
		
		int counter = 1000;
		
		Set<BluetoothDevice> devices = GlobalVariables.mBluetoothAdapter.getBondedDevices();
		//String names = null;
		
		while(GlobalVariables.mBluetoothDevice == null || !GlobalVariables.mBluetoothDevice.getName().equals(DEVICE_NAME))
		{
			counter--;
			if(counter <= 0)
			{
				Toast.makeText(mContext, "Can't find " + DEVICE_NAME, Toast.LENGTH_LONG).show();
				return false;
			}
			if (devices != null)
			{
				for (BluetoothDevice device : devices)
				{
					//names += "\n" + device.getName();
					if(DEVICE_NAME.equals(device.getName()))
					{
						GlobalVariables.mBluetoothDevice = device;
						Toast.makeText(mContext, device.getName() + " connected!", Toast.LENGTH_LONG).show();
						break;
					}
				}
			}
		}
				
		GlobalVariables.mBluetoothAdapter.cancelDiscovery();
		return true;
	}
	
	
}
