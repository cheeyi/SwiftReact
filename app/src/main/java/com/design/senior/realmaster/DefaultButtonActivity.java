package com.design.senior.realmaster;

// android imports
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;

public class DefaultButtonActivity extends Activity {

    /** Static Bluetooth communication object */
    public static BluetoothButtonCommunication bc = new BluetoothButtonCommunication();

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_default);
    }

    @Override
    protected void onNewIntent(Intent intent){
        setIntent(intent);
    }
    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        boolean exitCheck = intent.getBooleanExtra("EXIT APP", false);

        // reset BT upon app start and exit

        if(exitCheck)
        {
            Log.v("BT","exitCheck is true");
            bc.closeBluetoothButtonCommunication();
            finish();
            return;
        }

        // resets both the Bluetooth input and Bluetooth output streams
        bc.closeBluetoothButtonCommunication(); // 4/24: test

        // instantiates and starts the default thread
        DefaultButtonThread defaultThread = new DefaultButtonThread(bc, getApplicationContext(), this);
        defaultThread.start();
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        // prevents the back button from changing activities
    }
}
