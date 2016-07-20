package com.design.senior.realmaster;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Arrays;

public class ConnectedButtonThread extends Thread {

    /** Context from the default activity */
    private Context context;

    /**
     * This is the constructor method for the connected thread.
     * @param context The context from the connected activity.
     */
    public ConnectedButtonThread(Context context) {
        this.context = context;
    }

    /**
     * This method is called when the connected thread is started.
     */
    public void run() {
        String[] settings;
        // reads the settings over Bluetooth
        do {
            settings = DefaultButtonActivity.bc.readMessageFromInputStream(DefaultButtonActivity.bc.getInputStream());
        }
        while(settings[1].equals("invalid"));
        if (settings[1].equals("MASTER CRASH"))
        {
            Intent intent = new Intent(context, DefaultButtonActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return;
        }
        else if(settings[1].equals("EXIT APP"))
        {
            Intent intent = new Intent(context, DefaultButtonActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT APP",true);
            context.startActivity(intent);
            return;
        }
        // parses the settings
        parseSettings(settings);
    }

    /**
     * Parses all of the settings into the session activity intent
     * @param settings The string array of settings
     */
    private void parseSettings(String[] settings) {

        // the first element will always be the mode (string)
        String mode = settings[2];
        // the second element will always be the timed setting (boolean)
        String timed = settings[3];
        // the third element will always be the time limit (float)
        String timeLimit = settings[4];

        // add additional settings here

        // logs the settings array
        Log.d("SETTINGS", Arrays.toString(settings));

        // instantiates the session activity
        Intent intent = new Intent(context, SessionButtonActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // adds the settings to the session activity
        intent.putExtra("MODE", mode);
        intent.putExtra("TIMED", timed);
        intent.putExtra("TIME_LIMIT", timeLimit);

        // starts the session activity
        context.startActivity(intent);
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
