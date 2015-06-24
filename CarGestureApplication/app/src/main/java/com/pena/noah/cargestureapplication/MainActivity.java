package com.pena.noah.cargestureapplication;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends Activity implements OnClickListener, PlayerNotificationCallback, ConnectionStateCallback
{
    BluetoothShit bluetooth;

    Button quitButton;
    Button switchButton;

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
        switchButton = (Button)findViewById(R.id.switchButton);

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
            final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                final Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player)
                    {
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                       // mPlayer.play("spotify:user:1255218042:playlist:3AFfiVdHxZoqVlS6x7KIgl");

                        //String userID = "1255218042";

                        //String authToken = "BQBxLmj2iP5kCOODeP28Ra0O1bdgUnGCR29hVDGGeijaSJ_7bZYANRa2fc3aTM3IfQXoguA1ezJqoyTZvuHjcxCm2hq3QIUc73Z89o_VA3NBcoMXzrW";

                        SpotifyApi api = new SpotifyApi();

                        api.setAccessToken(response.getAccessToken());

                        GlobalVariables.spotifyService = api.getService();


                        GlobalVariables.spotifyService.getMe(new Callback<UserPrivate>() {
                            @Override
                            public void success(UserPrivate userPrivate, Response response) {
                                GlobalVariables.user = userPrivate;
                                Toast.makeText(getApplicationContext(), GlobalVariables.user.id, Toast.LENGTH_LONG).show();

                                GlobalVariables.getUserPlaylists();

                                GlobalVariables.isPlaying = true;

                                Log.d("Debug", "jajajajajajaja");
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d("Debug", "nein nein nein nein nein");
                            }
                        });



                        GlobalVariables.mPlayer = mPlayer;

                        GlobalVariables.useSpotify = true;
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
        //Toast.makeText(getApplicationContext(), "User Logged In", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoggedOut()
    {
        //Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoginFailed(Throwable error)
    {
        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTemporaryError()
    {
        //Toast.makeText(getApplicationContext(), "Temporary Error Occured", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionMessage(String message)
    {
        //Toast.makeText(getApplicationContext(), "Received connection message: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState)
    {
        //Toast.makeText(getApplicationContext(), "Playback event received: " + eventType.name(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails)
    {
        //Toast.makeText(getApplicationContext(), "Playback error received: " + errorType.name(), Toast.LENGTH_LONG).show();
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
        else if(v.getId() == R.id.switchButton)
        {
            if(GlobalVariables.useSpotify)
            {
                GlobalVariables.mPlayer.pause();
                GlobalVariables.useSpotify = false;
            }
            else
            {
                GlobalVariables.mPlayer.resume();
                GlobalVariables.useSpotify = true;
            }
        }
    }



    public boolean startBluetoothShit()
    {
        GlobalVariables.mContext = getApplicationContext();
        GlobalVariables.mActivity = this;
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

    @Override
    protected void onDestroy()
    {
        if(GlobalVariables.mBluetoothSocket.isConnected())
        {
            try
            {
                GlobalVariables.mBluetoothSocket.close();
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }


}
