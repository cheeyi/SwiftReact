// handles Control-Button Bluetooth communication for CountdownActivity
package com.design.senior.realmaster;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CountdownThread extends Thread {

    private Context context;

    public CountdownThread(Context countdownContext) {
        context = countdownContext;
    }

    public void run() {
        // broadcast settings to Button devices
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        String prefix = "SETTINGS";
        String mode = settings.getString("mode", "Error");
        Boolean timed = settings.getBoolean("timed", false);
        String timeLimit = settings.getString("timeLimit", "Error");

        if(timeLimit.equals("")) { // give a relatively large number so activated Button does not disappear
            timeLimit="232";
        }
        else if(!timeLimit.equals("Error")) { // cap at 232
            if (Integer.parseInt(timeLimit) > 232) {
                timeLimit = "232";
            }
        }

        // this broadcasts current settings to Button devices
        String settingsMessage = prefix + "," + mode + "," + timed + "," + timeLimit;
        for (int index = 0; index < DefaultActivity.bc.getConnectionsCount(); index++) {
            DefaultActivity.bc.writeMessageToOutputStream(
                    settingsMessage, DefaultActivity.bc.getOutputStream(index));
        }
    }
}
