package com.pena.noah.cargestureapplication;

import android.app.Service;
import android.content.Intent;
import android.media.Image;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Noah on 7/8/2015.
 */
public class WidgetService extends Service
{
    ImageButton imageButton;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);



        Log.d("Debug", "onStartCommand");

        Toast.makeText(getApplicationContext(), "Just a test", Toast.LENGTH_SHORT).show();

        if(GlobalVariables.mActivity == null)
        {
            Intent dialogIntent = new Intent(this, MainActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);
        }
        else
        {
            Log.d("activity", GlobalVariables.mActivity.toString());
           // GlobalVariables.mActivity.finish();
            sendBroadcast(new Intent("xyz"));
           /* LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                    .getInstance(GlobalVariables.mActivity);
            localBroadcastManager.sendBroadcast(new Intent(
                    "closeApplication"));*/
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
