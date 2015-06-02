package com.example.cargestureproject;

import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneControlShit extends BroadcastReceiver
{
	public String currentGesture = "NONE";
	public boolean inCall = false;
	public boolean incomingCall = false;
	
	public interface ITelephony
	{
		boolean endCall();
		void answerRingingCall();
		void silenceRinger();
	}
	
	Context mContext;
	
	PhoneStateListener callStateListener;
	TelephonyManager telephonyManager;
	EndCallListener endCallListener;
	ITelephony telephonyService;
	
	String number;

	public PhoneControlShit(Context context)
	{
		mContext = context;
		
		endCallListener = new EndCallListener();
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(endCallListener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		incomingCall = true;
		
	       try {
	         Class<?> c = Class.forName(telephonyManager.getClass().getName());
	         Method m = c.getDeclaredMethod("getITelephony");
	         m.setAccessible(true);
	         telephonyService = (ITelephony) m.invoke(telephonyManager);
	         Bundle bundle = intent.getExtras();
	         String phoneNumber = bundle.getString("incoming_number");
	         Log.e("INCOMING", phoneNumber);
	         if ((phoneNumber != null) && currentGesture.equalsIgnoreCase("LEFT")) 
	         {
	        	incomingCall = false;
	        	inCall = false;
	            telephonyService.silenceRinger();
	            telephonyService.endCall();
	            Log.e("HANG UP", phoneNumber);
	         }
	         else if((phoneNumber != null) && currentGesture.equalsIgnoreCase("RIGHT"))
	         {
	        	 incomingCall = false;
	        	 inCall = true;
	        	 telephonyService.silenceRinger();
	        	 telephonyService.answerRingingCall();
	        	 Log.e("ANSWERED", phoneNumber);
	         }

	       } catch (Exception e) {
	         e.printStackTrace();
	       }
	}
	
	private class EndCallListener extends PhoneStateListener {
	    @Override
	    public void onCallStateChanged(int state, String incomingNumber) {
	    	incomingCall = false;
	    	inCall = false;
	    	currentGesture = "NONE";
	    }
	}
}
