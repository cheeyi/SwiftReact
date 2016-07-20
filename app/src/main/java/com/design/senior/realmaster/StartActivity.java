package com.design.senior.realmaster;

// Android imports
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends Activity {
    /**
     * Called when the app is first launched.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        String name = null;

        while(name == null || name.equals("")) {
            name = BluetoothAdapter.getDefaultAdapter().getName();
        }

        name = name.toLowerCase();
        if (name.equals("master")) { // launches Control app
            Intent intent = new Intent(this, DefaultActivity.class);
            startActivity(intent);
            finish();
        }
        else { // launches Button app for non-Master phones
            Intent intent = new Intent(this, DefaultButtonActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void onBackPressed() {
        // disables Back button
    }
}
