package com.design.senior.realmaster;

import android.os.SystemClock;
import android.util.Log;

import java.util.Arrays;

public class CompleteButtonThread extends Thread {

    private CompleteButtonActivity activity;

    /**
     * This is the constructor method for the complete thread.
     * @param activity The activity from the complete activity.
     */
    public CompleteButtonThread(CompleteButtonActivity activity) {
        this.activity = activity;
    }

    /**
     * This method is called when the default thread is started.
     */
    public void run() {

        /* send message to master that the session has completed */
        String[] message;
        String complete = "COMPLETE";
        SystemClock.sleep(100);
        DefaultButtonActivity.bc.writeMessageToOutputStream(complete, DefaultButtonActivity.bc.getOutputStream());

        // reads the complete message over Bluetooth
        do {
            message = DefaultButtonActivity.bc.readMessageFromInputStream(DefaultButtonActivity.bc.getInputStream());
        }
        while(message[1].equals("invalid"));

        Log.d("RECEIVED", Arrays.toString(message));

        // checks to see if the message is the repeat session message
        if (message[1].equals(activity.getString(R.string.again))) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.startConnectedButtonActivity();
                }
            });

        // checks to see if the message is the exit to main menu message
        } else if (message[1].equals(activity.getString(R.string.btexit))) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.startDefaultButtonActivity();
                }
            });
        } else if (message[1].equals("MASTER CRASH"))
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.startDefaultButtonActivity();
                }
            });
        }


    }

    private boolean isEmpty(String[] buffer)
    {
        boolean retval = false;

        for(int i = 0; i < buffer.length; i++)
        {
            if(buffer[i] == null || buffer[i] == "")
            {
                retval = true;
            }
        }
        return retval;
    }

}
