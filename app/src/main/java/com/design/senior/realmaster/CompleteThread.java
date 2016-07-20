// handles Control-Button Bluetooth communication for CompleteActivity
package com.design.senior.realmaster;

import android.util.Log;

import java.util.Arrays;

public class CompleteThread extends Thread {

    private CompleteActivity activity;

    public CompleteThread(CompleteActivity activity) {
        this.activity = activity;
    }

    public void run() {
        String complete = "COMPLETE";
        // broadcasts the complete message to all Buttons so they show "Session Complete"
        for (int index = 0; index < DefaultActivity.bc.getConnectionsCount(); index++) {
            DefaultActivity.bc.writeMessageToOutputStream(
                    complete, DefaultActivity.bc.getOutputStream(index));
        }
    }

}
