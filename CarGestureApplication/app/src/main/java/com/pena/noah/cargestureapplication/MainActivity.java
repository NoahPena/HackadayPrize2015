package com.pena.noah.cargestureapplication;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

//import com.example.cargestureproject.R;
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

public class MainActivity extends Activity implements OnClickListener, PlayerNotificationCallback, ConnectionStateCallback
{
    BluetoothShit bluetooth;

    Button quitButton;

    private static final String CLIENT_ID = "ea003cf31ff442b09e9be3534cb76499";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";

    private static final int REQUEST_CODE = 1337;
    private Player mPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quitButton = (Button)findViewById(R.id.quitButton);

        try
        {
            AuthenticationRequest.Builder builder =
                    new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
            builder.setScopes(new String[]{"user-read-private", "streaming"});
            AuthenticationRequest request = builder.build();

            AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        } catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("DEBUG", e.getMessage());
        }

        //while(!startBluetoothShit());
       // while(mPlayer == null);
        startBluetoothShit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player)
                    {
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                        mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                    }

                    @Override
                    public void onError(Throwable throwable)
                    {
                        Toast.makeText(getApplicationContext(), "Could not initialize player: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Spotify", "Could not initialize player: " + throwable.getMessage());
                        //Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void onLoggedIn()
    {
        Toast.makeText(getApplicationContext(), "User Logged In", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoggedOut()
    {
        Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoginFailed(Throwable error)
    {
        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTemporaryError()
    {
        Toast.makeText(getApplicationContext(), "Temporary Error Occured", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionMessage(String message)
    {
        Toast.makeText(getApplicationContext(), "Received connection message: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState)
    {
        Toast.makeText(getApplicationContext(), "Playback event received: " + eventType.name(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails)
    {
        Toast.makeText(getApplicationContext(), "Playback error received: " + errorType.name(), Toast.LENGTH_LONG).show();
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
        bluetooth = new BluetoothShit(getApplicationContext(), this, mPlayer);

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
                    Log.d("DEBUG", e.toString());
                    return false;
                }
            }
        }

        return true;
    }


}
