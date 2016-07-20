// Compliant to Android 4.4+, use FragmentManager instead of Pref object
// Redirects app control flow to prefs.xml in SettingsFragment
package com.design.senior.realmaster;

import android.app.Activity;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.support.v4.app.DialogFragment;
import android.app.Activity;

import java.util.List;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment (prefs.xml) as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }
}
