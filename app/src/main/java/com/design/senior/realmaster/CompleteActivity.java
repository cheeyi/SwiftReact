// called by SessionActivity. This is the "Session Complete" screen, providing objective feedback
// to users in the form of their average reaction time. Currently does not penalize missed taps.
package com.design.senior.realmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CompleteActivity extends Activity {

    private CompleteThread completeThread;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
    }

    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();

        TextView textView2 = (TextView) findViewById(R.id.textView2);
        /* bypass charting functionality that was not implemented completely
        List<long[]> reactionTimes = new ArrayList<long[]>();
        reactionTimes.add(DefaultActivity.data.getAllAverageReactionTimes());
        int index = reactionTimes.get(0).length; // get size/length of first element in reactionTimes list
        long times = reactionTimes.get(0)[index-1]; // get first list and last element in that list
        */
        long times = DefaultActivity.data.getAverageReactionTime();
        textView2.setText(Double.toString(times / 1000.0)); // in ms, converted to s

        // instantiates and starts the connected thread
        completeThread = new CompleteThread(this);
        completeThread.start();
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        // pressing Back button brings users back to main menu
        startDefaultActivity(this.findViewById(android.R.id.content));
    }

    public void startCountdownActivity (View v) {
        // called when "Repeat Session" is pressed
        if(DefaultActivity.bc.getConnectionsCount() > 0) {
            // broadcasts the 'again' message to all Buttons which brings them back to the ConnectedButtonActivity
            for (int index = 0; index < DefaultActivity.bc.getConnectionsCount(); index++) {
                DefaultActivity.bc.writeMessageToOutputStream("AGAIN", DefaultActivity.bc.getOutputStream(index));
            }

            //TODO: This fixes a timing issue with the Buttons, look into solutions
            SystemClock.sleep(1000);

            Intent intent = new Intent(this, CountdownActivity.class);
            startActivity(intent);
        }
    }

    public void startDefaultActivity (View v) {
        // called when "Main Menu" is pressed
        // broadcasts the 'again' message to all Buttons which brings them back to the ConnectedButtonActivity
        for (int index = 0; index < DefaultActivity.bc.getConnectionsCount(); index++) {
            DefaultActivity.bc.writeMessageToOutputStream("AGAIN", DefaultActivity.bc.getOutputStream(index));
        }

        Intent intent = new Intent(this, DefaultActivity.class);
        startActivity(intent);
    }

}
