package com.example.cargestureproject;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity 
{
	BluetoothShit bluetooth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		bluetooth = new BluetoothShit(getApplicationContext());
		
		if(bluetooth.bluetoothOn())
		{
			bluetooth.searchDevice();
			
			try 
			{
				bluetooth.openBT();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
}
