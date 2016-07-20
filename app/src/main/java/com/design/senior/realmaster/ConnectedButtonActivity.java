package com.design.senior.realmaster;

import android.app.Activity;
import android.os.Bundle;

public class ConnectedButtonActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_connected);
    }

    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // instantiates and starts the connected thread
        ConnectedButtonThread connectedThread = new ConnectedButtonThread(getApplicationContext());
        connectedThread.start();
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        // prevents the back button from changing activities
    }
}
