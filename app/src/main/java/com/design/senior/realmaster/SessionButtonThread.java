package com.design.senior.realmaster;

import android.util.Log;

import java.util.Arrays;

public class SessionButtonThread extends Thread {

    private SessionButtonActivity activity;

    /**
     * This is the constructor method for the session thread.
     * @param activity The activity from the session activity.
     */
    public SessionButtonThread(SessionButtonActivity activity) {
        this.activity = activity;
    }

    /**
     * This method is called when the default thread is started.
     */
    public void run() {

        handleMessage();
    }

    /**
     * This method handles the incoming messages from the master application
     */
    public void handleMessage() {

        // keeps track of how long the session should last
        Boolean running = true;

        // loops until session is complete
        while(running) {

            // reads the session message over Bluetooth
            String[] message;
            do {
                message = DefaultButtonActivity.bc.readMessageFromInputStream(DefaultButtonActivity.bc.getInputStream());
            }while(message[1].equals("invalid"));
            
            final String[] messagefinal = message;
            Log.d("RECEIVED", Arrays.toString(message));

            // checks to see if the message is a turn on message
            if (message[1].equals(activity.getString(R.string.turn_on))) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (activity.mode.equals(activity.getString(R.string.picture))) {
                            activity.enableButton(messagefinal[1]);
                        } else {
                            activity.enableButton("IGNORE");
                        }
                    }
                });

            // checks to see if the message is a complete message
            } else if (message[1].equals("COMPLETE")) {
                running = false;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.disableButton();
                        activity.startCompleteButtonActivity();
                    }
                });
            }
            else if(message[1].equals("MASTER CRASH"))
            {
                running = false;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.disableButton();
                        activity.startDefaultButtonActivity();
                    }
                });
            }
        }
    }

}
