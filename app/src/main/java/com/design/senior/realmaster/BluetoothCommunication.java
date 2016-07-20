package com.design.senior.realmaster;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class BluetoothCommunication {

    /** Maximum number of Bluetooth connections */
    public final int MAX_STREAMS = 200;

    /** Bluetooth input streams */
    private InputStream[] inputStreams = new InputStream[MAX_STREAMS];
    /** Bluetooth output streams */
    private OutputStream[] outputStreams = new OutputStream[MAX_STREAMS];
    /** Number of Bluetooth connections */
    private int connectionsCount;

    // gets the device's default Bluetooth adapter
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private BluetoothSocket[] sockets = new BluetoothSocket[MAX_STREAMS];

    private BluetoothServerSocket serverSocket;



    /**
     * This method gets a Bluetooth input stream.
     * @param index The index into the Bluetooth input streams array.
     * @return It returns a Bluetooth input stream.
     */
    public InputStream getInputStream(int index) {
        return inputStreams[index];
    }

    /**
     * This method sets a Bluetooth input stream.
     * @param stream The input stream to set.
     * @param index The index into the Bluetooth input streams array.
     */
    public void setInputStream(InputStream stream, int index) {
        inputStreams[index] = stream;
    }

    /**
     * This method gets a Bluetooth output stream.
     * @param index The index into the Bluetooth output streams array.
     * @return It returns a Bluetooth output stream.
     */
    public OutputStream getOutputStream(int index) {
        return outputStreams[index];
    }

    /**
     * This method sets a Bluetooth output stream.
     * @param stream The output stream to set.
     * @param index The index into the Bluetooth output streams array.
     */
    public void setOutputStream(OutputStream stream, int index) {
        outputStreams[index] = stream;
    }

    /**
     * This method gets the number of Bluetooth connections.
     * @return It returns the number of Bluetooth connections.
     */
    public int getConnectionsCount() {
        return connectionsCount;
    }

    /**
     * This method increments the number of Bluetooth connections by one.
     */
    public void incrementConnectionsCount() {
        connectionsCount++;
    }

    public void setSocket(BluetoothSocket socket, int index) {
        sockets[index] = socket;
    }

    public BluetoothSocket getSocket(int index) {
        return sockets[index];
    }
    /**
     * This method resets both the Bluetooth input and Bluetooth output streams.
     */
    public void closeBluetoothCommunication() {
        for(int i = 0; i < inputStreams.length; i++)
        {
            if(inputStreams[i] != null)
            {
                try {
                    inputStreams[i].close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                inputStreams[i] = null;
            }
        }

        for(int i = 0; i < outputStreams.length; i++)
        {
            if(outputStreams[i] != null)
            {
                try {
                    outputStreams[i].close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                outputStreams[i] = null;
            }
        }

        for(int i = 0; i < sockets.length; i++)
        {
            if(sockets[i] != null)
            {
                try {
                    sockets[i].close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                sockets[i] = null;
            }
        }
        if(serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        serverSocket = null;
        /* Don't force BT disable
        bluetoothAdapter.disable();
        while(bluetoothAdapter.getState() != BluetoothAdapter.STATE_OFF);
        */
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
                Log.v("IOException","Slave Crash!");
                splitMessage[1] = "SLAVE CRASH";
                // prints out the exception's stack trace
                e.printStackTrace();
                return splitMessage;
            }
            if(!tryParseInt(splitMessage[0]))
            {
                splitMessage[1] = "invalid";
                return splitMessage;
            }
        }
        while(Integer.parseInt(splitMessage[0]) > readBytes || readBytes == 0);
        Log.v("Read", "Read:" + totalMessage);
        return splitMessage;
    }


    /**
     * This method writes a message into a Bluetooth output stream.
     * @param message The message to write.
     * @param outputStream The Bluetooth output stream to use.
     */
    public void writeMessageToOutputStream (String message, OutputStream outputStream) {

        // converts the message into a byte array
        int size = message.getBytes().length;
        if(size > 255)
        {
            Log.v("Writing Message","Size of message greater than 255");
        }
        String write = Integer.toString(size) + "," + message;
        Log.v("Write", "Writing message: " + write);
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

    /**
     * This method creates and initializes a Bluetooth server socket.
     * @return It returns the initialised Bluetooth server socket.
     */
    public BluetoothServerSocket createBluetoothServerSocket() {

        // gets the name of the application
        String name = "PVCED";
        // gets a common UUID for both the master and slave applications
        UUID uuid = UUID.fromString("23ea856c-49da-11e4-9e35-164230d1df67");

        // initializes an empty Bluetooth server socket
        serverSocket = null;

        try {
            // creates a Bluetooth socket using a common UUID
            serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(name, uuid);
        } catch (IOException e) {
            // prints out the exception's stack trace
            e.printStackTrace();
        }

        return serverSocket;
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
}
