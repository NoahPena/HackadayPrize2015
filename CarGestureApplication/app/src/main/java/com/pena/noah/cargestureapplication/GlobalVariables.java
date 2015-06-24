package com.pena.noah.cargestureapplication;

import android.app.Activity;
import android.content.Context;

import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.PlayConfig;

/**
 * Created by Noah on 6/23/2015.
 */
public class GlobalVariables
{
    static Player mPlayer = null;
    static Context mContext = null;
    static Activity mActivity = null;

    static boolean isPlaying = false;
    static boolean useSpotify = false;

}
