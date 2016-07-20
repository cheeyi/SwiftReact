// called by DefaultActivity when Start is pressed
// enters 5-second countdown before session begins to broadcast settings to Buttons
package com.design.senior.realmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CountdownActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
    }

    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // instantiates and starts the countdown thread
        CountdownThread connectedThread = new CountdownThread(getApplicationContext());
        connectedThread.start();

        // the TextView element for the countdown timer
        final TextView textView = (TextView) findViewById(R.id.textView2);

        new CountDownTimer(5000, 1000) { // 5 seconds

            public void onTick(long millisUntilFinished) {
                textView.setText(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() { // after 5 seconds, call SessionActivity
                Intent intent = new Intent();
                intent.setClassName("com.design.senior.realmaster", "com.design.senior.realmaster.SessionActivity");
                startActivity(intent);
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
    }
}
