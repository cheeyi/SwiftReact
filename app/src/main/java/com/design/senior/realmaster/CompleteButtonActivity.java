package com.design.senior.realmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CompleteButtonActivity extends Activity {

    private CompleteButtonThread completeThread;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_complete);
    }

    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // instantiates and starts the connected thread
        completeThread = new CompleteButtonThread(this);
        completeThread.start();
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        // prevents the back button from changing activities
    }

    /**
     * This method is used to start the default activity.
     */
    public void startDefaultButtonActivity() {
        // instantiates and starts the default activity
        Intent intent = new Intent(this, DefaultButtonActivity.class);
        startActivity(intent);
    }

    /**
     * This method is used to start the connected activity.
     */
    public void startConnectedButtonActivity() {
        // instantiates and starts the connected activity
        Intent intent = new Intent(this, ConnectedButtonActivity.class);
        startActivity(intent);
    }

}
