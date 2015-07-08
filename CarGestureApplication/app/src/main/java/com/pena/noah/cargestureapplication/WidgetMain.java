package com.pena.noah.cargestureapplication;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by Noah on 7/7/2015.
 */
public class WidgetMain extends AppWidgetProvider
{
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("Debug", "onUpdate");

        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++)
        {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
