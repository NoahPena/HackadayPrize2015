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

public class MusicControlShit implements PlayerNotificationCallback, ConnectionStateCallback
{
	Context mContext;
	Activity mActivity;

	//Spotify Crap
	boolean useSpotify = false;
	private static final String CLIENT_ID = "";
	private static final String REDIRECT_URI = "";

	private static final int REQUEST_CODE = 1337;
	private Player mPlayer;
	
	boolean currentPlaying = false;
	
	public MusicControlShit(Context context, Activity activity)
	{
		mContext = context;
		mActivity = activity;

		if(useSpotify)
		{
			AuthenticationRequest.Builder builder =
					new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
			builder.setScopes(new String[]{"user-read-private", "streaming"});
			AuthenticationRequest request = builder.build();

			AuthenticationClient.openLoginActivity(mActivity, REQUEST_CODE, request);

			//AuthenticationResponse = AuthenticationClient.getResponse(REQUEST_CODE)


		}
	}

	@Override
	public void onLoggedIn()
	{
		Toast.makeText(mContext, "User Logged In", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onLoggedOut()
	{
		Toast.makeText(mContext, "User Logged Out", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onLoginFailed(Throwable error)
	{
		Toast.makeText(mContext, "Login Failed", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onTemporaryError()
	{
		Toast.makeText(mContext, "Temporary Error Occured", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onConnectionMessage(String message)
	{
		Toast.makeText(mContext, "Received connection message: " + message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onPlaybackEvent(EventType eventType, PlayerState playerState)
	{
		Toast.makeText(mContext, "Playback event received: " + eventType.name(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onPlaybackError(ErrorType errorType, String errorDetails)
	{
		Toast.makeText(mContext, "Playback error received: " + errorType.name(), Toast.LENGTH_LONG).show();
	}

	public void pausePlayTrack()
	{
		if(useSpotify)
		{

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
	
	public void pauseTrack()
	{
		if(useSpotify)
		{

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
		if(useSpotify)
		{

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
		if(useSpotify)
		{

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
