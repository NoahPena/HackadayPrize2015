package com.pena.noah.cargestureapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.util.Log;
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
import com.spotify.sdk.android.player.PlayConfig;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Noah on 6/23/2015.
 */
public class GlobalVariables
{
    //Android Stuff
    static Context mContext = null;
    static Activity mActivity = null;

    //Spotify Android SDK Stuff
    static boolean isPlaying = false;
    static boolean useSpotify = false;
    static Player mPlayer = null;

    //Spotify Web API Stuff
    static SpotifyService spotifyService = null;
    static Pager<PlaylistSimple> playlists = null;
    static List<String> currentPlaylist = null;
    static UserPrivate user = null;
    static int counter = 0;

    public static void nextPlaylist()
    {
        if(counter >= playlists.items.size() - 1)
        {
            counter = 0;
        }
        else
        {
            counter++;
        }

        Log.d("Debug", "Current Counter: " + counter);
      //  Toast.makeText(mContext, "Current Counter: " + counter + "\nSize: " + playlists.items.size(), Toast.LENGTH_SHORT).show();
       // Log.d("Debug", "Size: " + playlists.items.size());

    }

    public static void getUserPlaylists()
    {
        spotifyService.getPlaylists(user.id, new Callback<Pager<PlaylistSimple>>() {
            @Override
            public void success(Pager<PlaylistSimple> playlistSimplePager, Response response)
            {
                playlists = playlistSimplePager;

                mPlayer.play(playlists.items.get(counter).uri);

                Log.d("Debug", "it twerks");
            }

            @Override
            public void failure(RetrofitError error)
            {
                Log.d("Debug", "it doesn't twerk");
            }
        });
    }

    //Bluetooth Stuff
    static BluetoothAdapter mBluetoothAdapter = null;
    static BluetoothDevice mBluetoothDevice = null;
    static BluetoothSocket mBluetoothSocket = null;

    //Phone Stuff
    static TelephonyManager telephoneManager = null;
    static boolean inCall = false;
    static boolean incomingCall = false;
    static TextToSpeech textToSpeech = null;
}
