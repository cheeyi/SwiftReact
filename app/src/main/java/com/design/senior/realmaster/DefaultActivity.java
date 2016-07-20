/*
This is the first Activity called for the Controller.
 */
package com.design.senior.realmaster;

// Android imports
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class DefaultActivity extends Activity {

    /** Static Bluetooth communication object */
    public static BluetoothCommunication bc = new BluetoothCommunication();

    /** Holds the default thread */
    private DefaultThread defaultThread;
    public static ReactionTimeData data;


    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        // resets both the Bluetooth input and Bluetooth output streams
        bc.closeBluetoothCommunication(); // flushes BT sockets and stream

        // data holds all the variables for computation of average reaction time
        // it is referenced by CompleteActivity and SessionActivity
        data = new ReactionTimeData(this);
        Log.v("RTD","Data created");
    }

    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // instantiates and starts the default thread
        defaultThread = new DefaultThread(bc, getApplicationContext());
        defaultThread.start();
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        // pressing the Back button will exit app and return to home screen
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    /**
     * This method is used to start the Countdown Activity.
     * @param view The view that was clicked.
     */
    public void startCountdownActivity(View view) {
        // called when Start button is pressed from menu (DefaultActivity)

        // checks to make sure at least Bluetooth connection exists
        if (bc.getConnectionsCount() > 0) {

            // interrupts the default tread and closes the server socket
            defaultThread.interrupt();
            defaultThread.close();

            try {
                // waits for the default thread to join back up with the UI thread
                defaultThread.join(); // wait for defaultThread to terminate
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(this, CountdownActivity.class);
            startActivity(intent);
        }
    }

    /**
     * This method is used to start the Progress Activity.
     * @param view The view that was clicked.
     */
    public void startProgressActivity(View view) {
        // currently not in use 5/2/2015

        // interrupts the default tread and closes the server socket
        defaultThread.interrupt();
        defaultThread.close();

        try {
            // waits for the default thread to join back up with the UI thread
            defaultThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //ChartMaker maker = new ChartMaker();
        //Log.v("Test", "Test");
        //Intent intent = maker.execute(this);
        //startActivity(intent);
    }

    /**
     * This method is used to start the Settings Activity.
     * @param view The view that was clicked.
     */
    public void startSettingsActivity(View view) {
        // called when the gear (Settings) icon is pressed

        // interrupts the default tread and closes the server socket
        defaultThread.interrupt();
        defaultThread.close();

        try {
            // waits for the default thread to join back up with the UI thread
            defaultThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // brings user to Settings page
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void quit(View view) {
        // not being used 5/2/2015. User can exit app by pressing Home or Back button

        defaultThread.interrupt();
        String exit = "EXIT APP";
        for(int i = 0; i < bc.getConnectionsCount(); i++)
        {
            bc.writeMessageToOutputStream(exit, bc.getOutputStream(i));
        }
        bc.closeBluetoothCommunication();
        finish();
    }
}
