package com.pena.noah.cargestureapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Noah on 7/9/2015.
 */
public class Options extends PreferenceActivity
{
    private CheckBoxPreference spotifyEnabled;
    private CheckBoxPreference googlePlayEnabled;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle bundledInstance)
    {
        super.onCreate(bundledInstance);
        addPreferencesFromResource(R.xml.options);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = prefs.edit();

        spotifyEnabled = (CheckBoxPreference)findPreference("enable_spotify");
        googlePlayEnabled = (CheckBoxPreference)findPreference("enable_google_play");

        /*googlePlayEnabled.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                Log.d("Debug", "Google Play Player: " + newValue.toString());
               if(!(boolean)newValue)
               {
                   spotifyEnabled.setEnabled(true);
                   googlePlayEnabled.setEnabled(false);

                   editor.putBoolean("enable_spotify", true);
                   editor.putBoolean("enable_google_play", false);
                   editor.commit();
               }
                return false;
            }
        });

        spotifyEnabled.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                Log.d("Debug", "Spotify Player: " + newValue.toString());
                if(!(boolean)newValue)
                {
                    googlePlayEnabled.setEnabled(true);
                    spotifyEnabled.setEnabled(false);

                    editor.putBoolean("enable_spotify", false);
                    editor.putBoolean("enable_google_play", true);
                    editor.commit();
                }
                return false;
            }
        });
        */
    }

}
