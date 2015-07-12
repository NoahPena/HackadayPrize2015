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
public class Options extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private CheckBoxPreference spotifyEnabled;
    private CheckBoxPreference googlePlayEnabled;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public void onSharedPreferenceChanged(SharedPreferences arg0, String key)
    {

    }

    protected void onCreate(Bundle bundledInstance)
    {
        super.onCreate(bundledInstance);
        addPreferencesFromResource(R.xml.options);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = prefs.edit();

        spotifyEnabled = (CheckBoxPreference)findPreference("enable_spotify");
        googlePlayEnabled = (CheckBoxPreference)findPreference("enable_google_play");

        if(prefs.getBoolean("enable_spotify", false))
        {
            spotifyEnabled.setChecked(true);
            spotifyEnabled.setEnabled(true);

            googlePlayEnabled.setChecked(false);
            googlePlayEnabled.setEnabled(false);
        }
        else if(prefs.getBoolean("enable_google_play", false))
        {
            googlePlayEnabled.setChecked(true);
            googlePlayEnabled.setEnabled(true);

            spotifyEnabled.setChecked(false);
            spotifyEnabled.setEnabled(false);
        }

        googlePlayEnabled.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                Log.d("Debug", "Google Play Player: " + newValue.toString());
                if(!(boolean)newValue)
                {
                    spotifyEnabled.setEnabled(true);
                    spotifyEnabled.setChecked(true);
                    googlePlayEnabled.setEnabled(false);
                    googlePlayEnabled.setChecked(false);

                    editor.putBoolean("enable_spotify", true);
                    editor.putBoolean("enable_google_play", false);
                    editor.commit();
                }

                //setPreferenceScreen(null);
                //addPreferencesFromResource(R.xml.options);
                return true;
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
                    googlePlayEnabled.setChecked(true);
                    spotifyEnabled.setEnabled(false);
                    spotifyEnabled.setChecked(false);

                    editor.putBoolean("enable_spotify", false);
                    editor.putBoolean("enable_google_play", true);
                    editor.commit();
                }

                //setPreferenceScreen(null);
                //addPreferencesFromResource(R.xml.options);
                return true;
            }
        });

        prefs.registerOnSharedPreferenceChangeListener(this);
    }

}
