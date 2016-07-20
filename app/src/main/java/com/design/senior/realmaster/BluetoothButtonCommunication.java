package com.design.senior.realmaster;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothButtonCommunication {

    /** Bluetooth input stream */
    public InputStream inputStream;
    /** Bluetooth output stream */
    public OutputStream outputStream;

    // gets the device's default Bluetooth adapter
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    /* Bluetooth socket*/
    public BluetoothSocket socket;

    /**
     * This method gets the Bluetooth input stream.
     * @return It returns the Bluetooth input stream.
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * This method sets the Bluetooth input stream.
     * @param stream The input stream to set.
     */
    public void setInputStream(InputStream stream) {
        inputStream = stream;
    }

    /**
     * This method gets the Bluetooth output stream.
     * @return It returns the Bluetooth output stream.
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * This method sets the Bluetooth output stream.
     * @param stream The output stream to set.
     */
    public void setOutputStream(OutputStream stream) {
        outputStream = stream;
    }

    /**
     * This method resets both the Bluetooth input and Bluetooth output streams.
     */
    public void closeBluetoothButtonCommunication() {
        if(inputStream != null)
        {
            try {
                inputStream.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            inputStream = null;
        }
        if(outputStream != null)
        {
            try {
                outputStream.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            outputStream = null;
        }
        if(socket != null)
        {
            try {
                socket.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            socket = null;
        }
        /* Try to not force BT turn-off every time
        bluetoothAdapter.disable();
        while(bluetoothAdapter.getState() != bluetoothAdapter.STATE_OFF) {
            Log.v("Blue","BT is turning off...");
        }*/
    }

    /**
     * This method reads a message from a Bluetooth input stream.
     * @param inputStream The Bluetooth input stream to use.
     * @return It returns a string array of the comma delimited message.
     */
    public String[] readMessageFromInputStream (InputStream inputStream) {

        // number of bytes read from the Bluetooth input stream
        int readBytes = 0;

        String[] splitMessage = new String[2];
        String totalMessage = "";
        // byte array to store the message from the Bluetooth input stream
        do {
            try {
                byte[] readBuffer = new byte[1024];
                // reads a byte array from the Bluetooth input stream
                readBytes = inputStream.read(readBuffer);
                //converts the byte array into a string
                String message = new String(readBuffer, 0, readBytes);
                totalMessage += message;
                // splits the message into a string array, delimited by commas
                splitMessage = totalMessage.split(",");
            }
            catch (IOException e){
                splitMessage[1] = "MASTER CRASH";
                // prints out the exception's stack trace
                e.printStackTrace();
                return splitMessage;
            }
            if(!tryParseInt(splitMessage[0]))
            {
                splitMessage = new String[2];
                splitMessage[1] = "invalid";
                return splitMessage;
            }
            if(splitMessage.length > 1)
            {
                if(!checkValid(splitMessage[1]))
                {
                    splitMessage[1] = "invalid";
                    return splitMessage;
                }
            }

        }
        while(Integer.parseInt(splitMessage[0]) > readBytes || readBytes == 0);
        Log.v("Read", "Read: " + totalMessage);
        return splitMessage;
    }

    /**
     * This method writes a message into a Bluetooth output stream.
     * @param message The message to write.
     * @param outputStream The Bluetooth output stream to use.
     */
    public void writeMessageToOutputStream (String message, OutputStream outputStream) {

        int size = message.getBytes().length;
        if(size > 255)
        {
            Log.v("Writing Message", "Size of message greater than 255");
        }
        String write = Integer.toString(size) + "," + message;
        Log.v("Write", "Write: " + write);
        byte[] writeBuffer = write.getBytes();

        try {
            // writes the byte array into the Bluetooth output stream
            outputStream.write(writeBuffer);
            // flushes the Bluetooth output stream
            outputStream.flush();
        } catch (IOException e) {
            // prints out the exception's stack trace
            e.printStackTrace();
        }
    }

    boolean tryParseInt(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException nfe)
        {
            return false;
        }
    }

    boolean checkValid(String message)
    {
        boolean retval = false;
        if(message.equals("SETTINGS"))
            retval = true;
        else if(message.equals("TURN ON"))
            retval = true;
        else if(message.equals("COMPLETE"))
            retval = true;
        else if(message.equals("EXIT APP"))
            retval = true;
        else if(message.equals("AGAIN"))
            retval = true;

        return retval;
    }
}
