package com.pena.noah.cargestureapplication;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class PhoneControlShit
{
	public String currentGesture = "NONE";

	Context mContext;
	Activity mActivity;

	MusicControlShit musicControl;

	public PhoneControlShit(Context context, Activity activity)
	{
		mContext = context;
		mActivity = activity;

		musicControl = new MusicControlShit(mContext, mActivity);

		GlobalVariables.telephoneManager = (TelephonyManager)mActivity.getSystemService(Context.TELEPHONY_SERVICE);
		GlobalVariables.telephoneManager.listen(new TeleListener(), PhoneStateListener.LISTEN_CALL_STATE);

	}

	public boolean killCall(Context context)
	{
		try
		{
			// Get the boring old TelephonyManager
			TelephonyManager telephonyManager =
					(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

			// Get the getITelephony() method
			Class classTelephony = Class.forName(telephonyManager.getClass().getName());
			Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

			// Ignore that the method is supposed to be private
			methodGetITelephony.setAccessible(true);

			// Invoke getITelephony() to get the ITelephony interface
			Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

			// Get the endCall method from ITelephony
			Class telephonyInterfaceClass =
					Class.forName(telephonyInterface.getClass().getName());
			Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

			// Invoke endCall()
			methodEndCall.invoke(telephonyInterface);

			GlobalVariables.incomingCall = false;
			GlobalVariables.inCall = false;

		}
		catch (Exception ex)
		{
			// Many things can go wrong with reflection calls
			Log.d("DEBUG","PhoneStateReceiver **" + ex.toString());
			return false;
		}
		musicControl.pausePlayTrack();

		return true;
	}

	public boolean answerCall(Context context)
	{
		//musicControl.pausePlayTrack();
		try
		{
			// Get the boring old TelephonyManager
			TelephonyManager telephonyManager =
					(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

			// Get the getITelephony() method
			Class classTelephony = Class.forName(telephonyManager.getClass().getName());
			Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

			// Ignore that the method is supposed to be private
			methodGetITelephony.setAccessible(true);

			// Invoke getITelephony() to get the ITelephony interface
			Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

			// Get the endCall method from ITelephony
			Class telephonyInterfaceClass =
					Class.forName(telephonyInterface.getClass().getName());
			Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("answerRingingCall");

			// Invoke endCall()
			methodEndCall.invoke(telephonyInterface);

			GlobalVariables.inCall = true;
			GlobalVariables.incomingCall = false;

		}
		catch(Exception e)
		{
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
			return false;
		}


		return true;
	}


	class TeleListener extends PhoneStateListener
	{
		public void onCallStateChanged(int state, String incomingNumber)
		{
			super.onCallStateChanged(state, incomingNumber);

			switch(state)
			{
				case TelephonyManager.CALL_STATE_IDLE:
					Toast.makeText(mContext, "CALL_STATE_IDLE", Toast.LENGTH_LONG).show();
					break;

				case TelephonyManager.CALL_STATE_OFFHOOK:
					Toast.makeText(mContext, "CALL_STATE_OFFHOOK", Toast.LENGTH_LONG).show();
					break;

				case TelephonyManager.CALL_STATE_RINGING:
					Toast.makeText(mContext, incomingNumber, Toast.LENGTH_LONG).show();
					//  Toast.makeText(getApplicationContext(), "CALL_STATE_RINGING", Toast.LENGTH_LONG).show();

					//musicControl.pausePlayTrack();
					musicControl.pauseTrack();

					GlobalVariables.incomingCall = true;
					break;

				default:

					break;
			}
		}
	}


}
