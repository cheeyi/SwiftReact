// handles Control-Button Bluetooth commmunication for DefaultActivity (main menu)
package com.design.senior.realmaster;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class DefaultThread extends Thread {

    /** Bluetooth adapter from the default activity */
    private BluetoothAdapter bluetoothAdapter;
    /** Context from the default activity */
    private Context context;
    /** Bluetooth server socket */
    private BluetoothServerSocket bluetoothServerSocket;

    private BluetoothCommunication bc;

    /**
     * This is the constructor method for the default thread.
     * @param bc The Bluetooth adapter from the default activity.
     * @param defaultContext The context from the default activity.
     */
    public DefaultThread(BluetoothCommunication bc, Context defaultContext) {
        this.bc = bc;
        bluetoothAdapter=  bc.bluetoothAdapter;
        context = defaultContext;
    }

    /**
     * This method is called when the Bluetooth connection thread is started.
     */
    public void run() {
        // accepts incoming Bluetooth connections
        acceptConnection(); // maybe blocking two other phones from not being to connect
    }

    /**
     * This method accepts Bluetooth connections.
     */
    public void acceptConnection() {

        // checks to see if the device supports Bluetooth
        checkBluetoothSupport(bluetoothAdapter);
        // checks to see if the device's Bluetooth is enabled
        checkBluetoothEnabled(bluetoothAdapter);

        // initializes a Bluetooth server socket
        bluetoothServerSocket = bc.createBluetoothServerSocket();
        //connection made to Master, discovery no longer needed
        bluetoothAdapter.cancelDiscovery();

        BluetoothSocket bluetoothSocket;

        // loops until the thread is interrupted or an exception occurs
        while (!isInterrupted()) {

            try {
                // attempts to accept the slave application's connection
                bluetoothSocket = bluetoothServerSocket.accept();
            } catch (IOException e) {
                // prints out the exception's stack trace
                e.printStackTrace();
                Log.v("Default Thread", "Connection to slave failed.");
                // breaks out of the while loop
                return;
            }

            try {
                // gets the input stream from the Bluetooth socket
                InputStream inputStream = bluetoothSocket.getInputStream();
                // sets the static input stream
                bc.setInputStream(inputStream, bc.getConnectionsCount());
                // gets the output stream from the Bluetooth socket
                OutputStream outputStream = bluetoothSocket.getOutputStream();
                // sets the static output stream
                bc.setOutputStream(outputStream, bc.getConnectionsCount());
                // sets the static socket array
                bc.setSocket(bluetoothSocket, bc.getConnectionsCount());
                // increments the Bluetooth connections counter
                bc.incrementConnectionsCount();
            } catch (IOException e) {
                // prints out the exception's stack trace
                e.printStackTrace();
                Log.v("Default Thread", "Failed to get stream info.");
            }
        }
    }

    /**
     * This method closes the Bluetooth server socket.
     */
    public void close() {
        try {
            // closes the server socket
            bluetoothServerSocket.close();
        }
        catch (NullPointerException bSS) {
            Log.v("Default Thread close()","NullPointerException");
        }
        catch (IOException e) {
            // prints out the exception's stack trace
            e.printStackTrace();
        }
    }

    /**
     * This method checks to see if the device supports Bluetooth.
     * @param bluetoothAdapter The Bluetooth adapter to use.
     */
    public void checkBluetoothSupport(BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter == null) {
            ((Activity)(context)).finish();
        }
    }

    /**
     * This method checks to see if the device's Bluetooth is enabled.
     * @param bluetoothAdapter The Bluetooth adapter to use.
     */
    public void checkBluetoothEnabled(BluetoothAdapter bluetoothAdapter) {
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
            while(bluetoothAdapter.getState() != BluetoothAdapter.STATE_ON);
        }
    }
}
