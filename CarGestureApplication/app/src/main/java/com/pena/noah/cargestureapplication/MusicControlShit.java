package com.pena.noah.cargestureapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import android.app.Activity;

import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import java.security.Key;


public class MusicControlShit
{
	Context mContext;
	Activity mActivity;

	//Spotify Crap
	boolean useSpotify = false;
	Player mPlayer = null;

	
	boolean currentPlaying = false;
	
	public MusicControlShit(Context context, Activity activity)
	{
		mContext = context;
		mActivity = activity;

		useSpotify = false;
	}

	public MusicControlShit(Context context, Activity activity, Player player)
	{
		mContext = context;
		mActivity = activity;

		mPlayer = player;

	}


	public void nextPlaylist()
	{
		if(GlobalVariables.useSpotify)
		{
			GlobalVariables.nextPlaylist();
			GlobalVariables.mPlayer.play(GlobalVariables.playlists.items.get(GlobalVariables.counter).uri);
		}
	}

	public void pausePlayTrack()
	{
		if(GlobalVariables.useSpotify)
		{
			if(GlobalVariables.isPlaying)
			{
				GlobalVariables.isPlaying = false;
				GlobalVariables.mPlayer.pause();
			}
			else
			{
				GlobalVariables.isPlaying = true;
				GlobalVariables.mPlayer.resume();
			}
		}
		else
		{
			Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);

			synchronized (this) {
				i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
				mContext.sendOrderedBroadcast(i, null);

				i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
				mContext.sendOrderedBroadcast(i, null);
			}
		}
	}

	public void resumeTrack()
	{
		if(GlobalVariables.useSpotify)
		{
			GlobalVariables.isPlaying = true;
			GlobalVariables.mPlayer.resume();
		}
		else
		{
			Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);

			synchronized (this)
			{
				i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY));
				mContext.sendOrderedBroadcast(i, null);

				i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY));
				mContext.sendOrderedBroadcast(i, null);
			}
		}
	}
	
	public void pauseTrack()
	{
		if(GlobalVariables.useSpotify)
		{
			GlobalVariables.isPlaying = false;
			GlobalVariables.mPlayer.pause();
		}
		else
		{
			Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);

			synchronized (this) {
				i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE));
				mContext.sendOrderedBroadcast(i, null);

				i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PAUSE));
				mContext.sendOrderedBroadcast(i, null);
			}
		}
	}
	
	public void skipTrack()
	{
		//Toast.makeText(mContext, "We tried", Toast.LENGTH_SHORT).show();
		if(GlobalVariables.useSpotify)
		{
			GlobalVariables.mPlayer.skipToNext();
		}
		else
		{
			Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);

			synchronized (this) {
				i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT));
				mContext.sendOrderedBroadcast(i, null);

				i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT));
				mContext.sendOrderedBroadcast(i, null);
			}
		}
	}
	
	public void lastTrack()
	{
		//Toast.makeText(context, text, duration)
		if(GlobalVariables.useSpotify)
		{
			GlobalVariables.mPlayer.skipToPrevious();
		}
		else
		{
			Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);

			synchronized (this) {
				i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS));
				mContext.sendOrderedBroadcast(i, null);

				i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS));
				mContext.sendOrderedBroadcast(i, null);
			}
		}
	}

}
