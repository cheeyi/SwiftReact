// called by CountdownActivity. This is the main session activity that decides which phone to turn on
// and receives successful or missed taps from the Buttons. It also updates 'data', which holds the
// average reaction time information.
package com.design.senior.realmaster;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;


public class SessionActivity extends Activity {
    public SessionThread sessionThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_session);
        sessionThread = new SessionThread(this);
        sessionThread.start();

        // TextView element for the countdown timer
        final TextView textView = (TextView) findViewById(R.id.textView2);

        // Obtains session length from Preferences, set to 60 by default
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sessionLength = settings.getString("sessionLength", "60");
        Log.v("Session length",sessionLength);

        // all time associated with reaction time is in milliseconds
        long sessionLengthInMilliseconds;

        if (sessionLength == null || sessionLength.isEmpty() ) { // if empty or erased session settings
            sessionLengthInMilliseconds = Long.parseLong("60") * 1000; // default to 60
        }
        else { // grab user-specified session length
            sessionLengthInMilliseconds = Long.parseLong(sessionLength) * 1000;
            // check for negative values or less than 10, and default to 60s
            if (sessionLengthInMilliseconds < 10000 || sessionLengthInMilliseconds == 0) {
                sessionLengthInMilliseconds = 60000;
            }
        }

        new CountDownTimer(sessionLengthInMilliseconds, 100) {
            public void onTick(long millisUntilFinished) {
                // shows remaining session duration
                textView.setText(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                // call CompleteActivity
                if (sessionThread != null) {
                // sessionThread should not be null, but we were getting NullPointerException(s) periodically
                    sessionThread.interrupt();
                    sessionThread = null;
                    Intent intent = new Intent();
                    intent.setClassName("com.design.senior.realmaster", "com.design.senior.realmaster.CompleteActivity");
                    startActivity(intent);
                }
            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        DefaultActivity.data.reset(); // reset ReactionTimeData upon session start
        Log.v("RTD","Data reset");
    }

    @Override
    public void onBackPressed() {
    }

    public void onFinish(View view) { // called by the Stop button to stop a running session
        sessionThread.interrupt();
        sessionThread = null;
        Intent intent = new Intent();
        // goes to CompleteActivity
        intent.setClassName("com.design.senior.realmaster", "com.design.senior.realmaster.CompleteActivity");
        Log.v("RTD","Finish");
        startActivity(intent);
    }
}
