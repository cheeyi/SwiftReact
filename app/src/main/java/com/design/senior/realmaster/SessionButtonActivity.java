package com.design.senior.realmaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class SessionButtonActivity extends Activity {

    /** UI variables */
    private RelativeLayout relativeLayout;
    private Vibrator vibrator;

    /** Button variables */
    private Boolean buttonEnabled = false;
    private Handler handler;
    private long activateTime;

    /** Settings variables */
    public String mode;
    public Boolean timed;
    public Float timeLimit;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_session);
    }

    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // gets the session activity's relative layout
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        // gets access to the device's vibration control
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);


        // creates a listener for when the fullscreen button is pressed
        View.OnTouchListener relativeLayoutListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // checks to see if the button is being pressed
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // checks to see if the button is enabled
                    if (buttonEnabled) {
                        // causes the phone to vibrate for 250 milliseconds
                        vibrator.vibrate(250);
                        try {
                            // gets a ringtone from the ringtone manager
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
                            // plays the ringtone
                            ringtone.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        };

        // connects the listener to the fullscreen button
        relativeLayout.setOnTouchListener(relativeLayoutListener);

        // gets the current intent
        Intent intent = getIntent();

        // checks to make sure the timed string exists
        if(!intent.getStringExtra("MODE").equals("Error")) {
            mode = intent.getStringExtra("MODE");
        // otherwise mode defaults to button mode
        } else {
            mode = getString(R.string.button);
        }

        // timed is a boolean and will default to false
        timed = Boolean.parseBoolean(intent.getStringExtra("TIMED"));

        // checks to make sure the time limit string exists
        if(!intent.getStringExtra("TIME_LIMIT").equals("Error")) {
            timeLimit = Float.parseFloat(intent.getStringExtra("TIME_LIMIT"));
        // otherwise time limit default to an arbitrarily large value is set
        } else {
            timeLimit = (float) 1000;
        }

        // instantiates and starts the session thread
        final SessionButtonThread sessionThread = new SessionButtonThread(this);
        sessionThread.start();
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        // prevents the back button from changing activities
    }

    /**
     * Called when the fullscreen button is pressed.
     * @param view The view responsible for handling the onClick event
     */
    public void buttonPressed(View view) {

        // checks to see if the button is enabled
        if (buttonEnabled) {

            long elapsedTime;

            // acquire the time (in milliseconds) at which the button was pressed
            elapsedTime = (SystemClock.elapsedRealtime() - activateTime);

            // disables the button
            disableButton();

            // constructs the message to send to the master application (pressed,reactionTime)
            String writeMessage = getString(R.string.pressed) + "," + elapsedTime;
            // sends the message to the master application
            DefaultButtonActivity.bc.writeMessageToOutputStream(writeMessage, DefaultButtonActivity.bc.getOutputStream());
        }
    }

    /**
     * Enables the fullscreen button.
     * @param pictureSelect The string indicating which picture to display
     */
    public void enableButton(String pictureSelect) {
        // PICTURE mode. Currently not fully implemented 5/2/2015
        // checks to see if it's a picture session
        if (mode.equals(getString(R.string.picture))) {
            // checks to see if this is the correct picture
            if (pictureSelect.equals(getString(R.string.picture_correct))) {
                // sets the background to the checkmark picture
                relativeLayout.setBackgroundResource(R.drawable.checkmark);
                // checks to see if this is the incorrect picture
            } else if (pictureSelect.equals(getString(R.string.picture_incorrect))) {
                // sets the background to the cross picture
                relativeLayout.setBackgroundResource(R.drawable.cross);
                // prevents the button from being enabled
                return;
            } else {
                Log.d("ERROR", "The picture was neither correct or incorrect");
            }
            // checks to see if it's a button session
        } else if (mode.equals(getString(R.string.button))) {
            // sets the background to red
            relativeLayout.setBackgroundColor(Color.WHITE);
        } else {
            Log.d("ERROR", "The mode is not defined");
        }

        // acquire the time at which the button was enabled
        activateTime = SystemClock.elapsedRealtime();
        // sets the button as enabled
        buttonEnabled = true;

        // checks to see if the session is timed
        if (timed) {

            // gets the time limit in milliseconds
            long timeLimitInMilliseconds = (long) Math.round(timeLimit * 1000);

            // instantiates and starts a missed button handler to run after the time limit expires
            handler = new Handler();
            handler.postDelayed(missedButton, timeLimitInMilliseconds);
        }
    }

    /**
     * Called when a timed session's time limit expires.
     */
    private Runnable missedButton = new Runnable() {
        @Override
        public void run() {
            // disables the button
            disableButton();

            // constructs the message to send to the master application (missed)
            String writeMessage = getString(R.string.missed);
            // sends the message to the master application
            DefaultButtonActivity.bc.writeMessageToOutputStream(writeMessage, DefaultButtonActivity.bc.getOutputStream());
        }
    };

    /**
     * Disables the fullscreen button.
     */
    public void disableButton() {

        // sets the button as disabled
        buttonEnabled = false;

        // checks to see if the session is timed
        if (timed) {
            // stops the missed button callback from triggering
            if(handler != null){
                handler.removeCallbacks(missedButton);
            }
        }

        // sets the background to black
        relativeLayout.setBackgroundColor(Color.BLACK);
    }

    /**
     * This method is used to start the complete activity.
     */
    public void startCompleteButtonActivity() {
        // instantiates and starts the complete activity
        Intent intent = new Intent(this, CompleteButtonActivity.class);
        startActivity(intent);
    }

    public void startDefaultButtonActivity() {
        // instantiates and starts the default activity
        Intent intent = new Intent(this, DefaultButtonActivity.class);
        startActivity(intent);
    }
}
