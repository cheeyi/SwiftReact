// handles Control-Button Bluetooth communication for DefaultActivity
package com.design.senior.realmaster;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class DefaultButtonThread extends Thread {

    /**
     * Bluetooth adapter from the default activity
     */
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothButtonCommunication bc;
    /**
     * Context from the default activity
     */
    private Context context;
    private Activity activity;
    private BluetoothDevice master;
    private boolean dangerWillRobinson = false; // Boolean for system level ERRORs

    /**
     * This is the constructor method for the default thread.
     *
     * @param bc The Bluetooth adapter from the default activity.
     * @param context          The context from the default activity.
     */
    public DefaultButtonThread(BluetoothButtonCommunication bc, Context context, Activity activity) {
        this.bc = bc;
        this.bluetoothAdapter = bc.bluetoothAdapter;
        this.context = context;
        this.activity = activity;
    }

    /**
     * This method is called when the default thread is started.
     */
    public void run() {

        // initializes the Bluetooth connection
        initializeConnection();

        // instantiates and starts the connect activity
        if(dangerWillRobinson != true) { // if no error
            Intent intent = new Intent(context, ConnectedButtonActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * This method initializes the Bluetooth connection.
     */
    private void initializeConnection() {

        // checks to see if the device supports Bluetooth
        checkBluetoothSupport(bluetoothAdapter);

        // checks to see if the device's Bluetooth is enabled
        checkBluetoothEnabled(bluetoothAdapter);

        // gets the set of paired Bluetooth devices
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        // initializes a Bluetooth socket
        BluetoothSocket bluetoothSocket = createBluetoothSocket(pairedDevices);
        //connection made to Master, discovery no longer needed
        bluetoothAdapter.cancelDiscovery();

        // loops until the Bluetooth socket is connected
        Boolean connected = false;
        while (!connected) {
            try {
                // attempts to connect to the master application's Bluetooth server
                if(bluetoothSocket != null) {
                    bluetoothSocket.connect();
                    connected = true;
                }
                else{
                    Log.v("Default Thread","Bluetooth socket was null");
                    dangerWillRobinson = true;
                    return;
                }
            } catch (IOException e) {
                // sleeps for 10 ms
                Log.v("Connect Exception", e.getMessage());
                SystemClock.sleep(10);
                if(e.getMessage().equals("File descriptor in bad state"))
                {
                    activity.finish();
                    Intent intent = new Intent(context, DefaultButtonActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    dangerWillRobinson = true;
                    return;
                }
                if(e.getMessage().equals("Unable to start Service Discovery")){
                    unpairDevice(master);
                    while(master.EXTRA_BOND_STATE.equals(master.BOND_BONDED));
                    pairDevice(master);
                    while(master.EXTRA_BOND_STATE.equals(master.BOND_NONE) || master.EXTRA_BOND_STATE.equals(master.BOND_BONDING));
                }
            }
        }

        try {
            // gets the input stream from the Bluetooth socket
            InputStream inputStream = bluetoothSocket.getInputStream();
            // sets the static input stream
            bc.inputStream = inputStream;
            // gets the output stream from the Bluetooth socket
            OutputStream outputStream = bluetoothSocket.getOutputStream();
            // sets the static output stream
            bc.outputStream = outputStream;
            bc.socket = bluetoothSocket;
        } catch (IOException e) {
            // prints out the exception's stack trace
            e.printStackTrace();
        }
    }

    /**
     * This method checks to see if the device supports Bluetooth.
     *
     * @param bluetoothAdapter The Bluetooth adapter to use.
     */
    private void checkBluetoothSupport(BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter == null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence text = "Phone is incapable of bluetooth communication. Please use different device.";
                    Toast.makeText(activity, text, duration).show();
                }
            });
            SystemClock.sleep(3000);
            ((Activity) (context)).finish();
        }
    }

    /**
     * This method checks to see if the device's Bluetooth is enabled.
     *
     * @param bluetoothAdapter The Bluetooth adapter to use.
     */
    private void checkBluetoothEnabled(BluetoothAdapter bluetoothAdapter) {
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
            while(bluetoothAdapter.getState() != BluetoothAdapter.STATE_ON);
        }
    }

    /**
     * This method creates and initializes a Bluetooth socket.
     *
     * @param pairedDevices Set of Bluetooth devices paired with the phone.
     * @return It returns the initialised Bluetooth socket.
     */
    private BluetoothSocket createBluetoothSocket(Set<BluetoothDevice> pairedDevices) {

        // gets a common UUID for both the Control and Buttons
        UUID uuid = UUID.fromString("23ea856c-49da-11e4-9e35-164230d1df67");

        // initialises an empty Bluetooth socket
        BluetoothSocket bluetoothSocket = null;

        // checks to see if there are any paired devices
        if (pairedDevices.size() > 0) {
            // loops through each paired device
            for (BluetoothDevice device : pairedDevices) {
                // checks to see if the name of the paired device is MASTER
                if (device.getName().equals("MASTER")) {
                    try {
                        master = device;
                        // creates a Bluetooth socket using a common UUID
                        //bluetoothSocket = master.createRfcommSocketToServiceRecord(uuid);
                        //Method m = master.getClass().getMethod("createRfcommSocketToServiceRecord", new Class[] {int.class});
                        //bluetoothSocket = (BluetoothSocket) m.invoke(master, 1);
                        bluetoothSocket = master.createInsecureRfcommSocketToServiceRecord(uuid);
                    } catch(Exception e){
                        Log.v("Connect Exception", e.getMessage());
                    }
                }
            }
        }
        //check if we paired successfully to a master, if not, prompt user to do so.
        if (master == null){
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence text = "Please pair to master device";
                    Toast.makeText(activity, text, duration).show();
                }
            });
            SystemClock.sleep(3000);
            activity.finish();
        }

        return bluetoothSocket;
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Log.d("unpairDevice", "Start unpairing...");
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);

            m.invoke(device, (Object[]) null);
            Log.d("unpairDevice", "Unpairing complete");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Log.d("pairDevice", "Start pairing...");
            Method m = device.getClass().getMethod("createBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
            Log.d("pairDevice", "Pairing complete");
            SystemClock.sleep(1000);
            UUID uuid = UUID.fromString("23ea856c-49da-11e4-9e35-164230d1df67");
            bc.socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
        }
        catch(Exception e){
            Log.d("pairDevice", e.getMessage());
        }
    }
}
