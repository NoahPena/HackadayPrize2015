package com.pena.noah.cargestureapplication;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.cargestureproject.R;

public class MainActivity extends Activity implements OnClickListener
{
	BluetoothShit bluetooth;
	
	Button quitButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		quitButton = (Button)findViewById(R.id.quitButton);
	
		
		//while(!startBluetoothShit());
		startBluetoothShit();
	}
	
	public void onClick(View v)
	{
		if(v.getId() == R.id.quitButton)
		{
			if(bluetooth.bluetoothOff())
			{
				finish();
				System.exit(0);
			}
		}
	}
	
	public boolean startBluetoothShit()
	{
		bluetooth = new BluetoothShit(getApplicationContext(), this);
		
		if(bluetooth.bluetoothOn())
		{
			if(bluetooth.searchDevice())
			{
			
				try 
				{
					bluetooth.openBT();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();

					return false;
				}
			}
		}
		
		return true;
	}
	
	
}
