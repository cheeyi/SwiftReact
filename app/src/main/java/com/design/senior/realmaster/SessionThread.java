// IMPORTANT: handles Control-Button Bluetooth communication during a running session
package com.design.senior.realmaster;

import android.content.Context;
import android.content.SharedPreferences.*;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

public class SessionThread extends Thread {
    Context context;
    public boolean interrupted;
    public SessionThread(Context context) {
        this.context = context;
    }
    private int completeIndex = 1000000;

    public void run() {
        interrupted = false;
        while (!interrupted) {
            sendNext();
        }
        readComplete();
    }

    public void readComplete(){
        // reads message from Buttons
        String[] readMessage;
        for(int i = 0; i < DefaultActivity.bc.getConnectionsCount(); i++){
            if(i != completeIndex){
                do {
                    readMessage = DefaultActivity.bc.readMessageFromInputStream(DefaultActivity.bc.getInputStream(i));
                } while(readMessage[1].equals("invalid"));
                if (readMessage[1].equals("COMPLETE")){

                }
                else{
                    Log.d("readComplete()", "Error reading complete");
                }
            }
        }
    }
    @Override
    public void interrupt()
    {
        interrupted = true;
    }

    private void writeMessageToOutputStream (OutputStream outputStream, String message) {

        byte[] writeBuffer = message.getBytes();
        Log.d("WRITE", message);
        try {
            outputStream.write(writeBuffer);
        } catch (IOException e) {

        }

    }

    private String[] readMessageFromInputStream (InputStream inputStream) {

        byte[] readBuffer = new byte[1024];  // buffer store for the stream
        int readBytes; // bytes returned from read()

        String message = "";
        String[] decodedMessage;

        try {

            readBytes = inputStream.read(readBuffer);
            message = new String(readBuffer, 0, readBytes);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        Log.d("READ",message);
        decodedMessage = message.split(",");

        return decodedMessage;
    }

    public void sendNext() {
        // send signal to activate a random Button device
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = settings.getString("mode", "60");

        Random random = new Random(); // randomize next Button to be activated
        int buttonIndex = random.nextInt(DefaultActivity.bc.getConnectionsCount());

        if (DefaultActivity.bc.getOutputStream(buttonIndex) != null) {

            if (mode.equals("PICTURE")) { // currently not in use 5/2/2015

                for (int index = 0; index < DefaultActivity.bc.getConnectionsCount(); index++) {

                    if (index == buttonIndex) {
                        DefaultActivity.bc.writeMessageToOutputStream("TURN ON,CORRECT", DefaultActivity.bc.getOutputStream(index));
                    } else {
                        DefaultActivity.bc.writeMessageToOutputStream("TURN ON,INCORRECT", DefaultActivity.bc.getOutputStream(index));
                    }
                }

            } else { // default button mode
                DefaultActivity.bc.writeMessageToOutputStream("TURN ON", DefaultActivity.bc.getOutputStream(buttonIndex));

            }

            String[] readMessage;
            do {
                readMessage = DefaultActivity.bc.readMessageFromInputStream(DefaultActivity.bc.getInputStream(buttonIndex));
            } while(readMessage[1].equals("invalid"));

            if (readMessage[1].equals("PRESSED")) {
                DefaultActivity.data.incrementPressCount(readMessage[2]); // readMessage[2] is reactionTime
            }

            if(readMessage[1].equals("COMPLETE")){
                completeIndex = buttonIndex;
            }
        }
    }
}
