// builds a Settings page based on contents in prefs.xml
package com.design.senior.realmaster;

import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.os.Bundle;
/**
 * Created by CheeYi on 4/9/15.
 */
public class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.prefs);
        }

    }
