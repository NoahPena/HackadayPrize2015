package com.example.cargestureproject;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

public class MusicControlShit
{
	Context mContext;
	
	boolean currentPlaying = false;
	
	public MusicControlShit(Context context)
	{
		mContext = context;
		
	}
	
	public void pausePlayTrack()
	{
		Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
		
		synchronized (this)
		{
			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
			mContext.sendOrderedBroadcast(i, null);
			
			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
			mContext.sendOrderedBroadcast(i, null);
		}
	}
	
	public void pauseTrack()
	{
		Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
		
		synchronized (this)
		{
			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE));
			mContext.sendOrderedBroadcast(i, null);
			
			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PAUSE));
			mContext.sendOrderedBroadcast(i, null);
		}
		
	}
	
	public void skipTrack()
	{
		//Toast.makeText(mContext, "We tried", Toast.LENGTH_SHORT).show();
		
		Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
		
		synchronized (this) 
		{
            i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT));
            mContext.sendOrderedBroadcast(i, null);

            i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT));
            mContext.sendOrderedBroadcast(i, null);
		}
	}
	
	public void lastTrack()
	{
		//Toast.makeText(context, text, duration)
		
		Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
		
		synchronized (this)
		{
			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS));
			mContext.sendOrderedBroadcast(i, null);
			
			i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS));
			mContext.sendOrderedBroadcast(i, null);
		}
	}

}
