package com.pena.noah.cargestureapplication;

import java.lang.reflect.Method;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class PhoneControlShit
{
	public String currentGesture = "NONE";

	Context mContext;
	Activity mActivity;

	MusicControlShit musicControl;

	String number;

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
		GlobalVariables.incomingCall = false;
		GlobalVariables.inCall = false;
		musicControl.resumeTrack();

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

		}
		catch (Exception ex)
		{
			// Many things can go wrong with reflection calls
			Log.d("DEBUG","PhoneStateReceiver **" + ex.toString());
			return false;
		}

		return true;
	}

	public boolean answerCall(Context context)
	{

		Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
		buttonUp.putExtra(Intent.EXTRA_KEY_EVENT,
				new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
		context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");

		GlobalVariables.inCall = true;
		GlobalVariables.incomingCall = false;

		return true;
	}

	public String getContactDisplayNameByNumber(String number) {
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		String name = "?";

		ContentResolver contentResolver = mContext.getContentResolver();
		Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID,
				ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);

		try {
			if (contactLookup != null && contactLookup.getCount() > 0) {
				contactLookup.moveToNext();
				name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
				//String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
			}
		} finally {
			if (contactLookup != null) {
				contactLookup.close();
			}
		}

		return name;
	}

	public String spacePhoneNumber(String number)
	{
		String spacedOut = "";

		for(int i = 0; i < number.length(); i++)
		{
			spacedOut += number.charAt(i) + " ";
		}

		return spacedOut;
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

					number = getContactDisplayNameByNumber(incomingNumber);

					if(number == null)
					{
						number = spacePhoneNumber(incomingNumber);
					}


					GlobalVariables.textToSpeech.speak(number + " is calling", TextToSpeech.QUEUE_FLUSH, null);

					GlobalVariables.incomingCall = true;
					break;

				default:

					break;
			}
		}
	}


}
