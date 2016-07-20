package com.design.senior.realmaster;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ReactionTimeData {

    // not in use 5/2/2015
    public SharedPreferences sessionsNumber;
    private SharedPreferences.Editor edit, edit2;
    // variables in use
    private SharedPreferences reactionTimes;
    private long average;
    private long totalReactionTime;
    private int pressCount;
    private int session;
    private Context context;

    /* VARIABLE
    * sessionsNumber is a placeholder for a charting activity which has not yet been
    * implemented 5/2/2015. It contains the total number of sessions completed thus far.
    */

    public ReactionTimeData(Context context){ // main constructor for data instantiated
        this.context = context;
        reactionTimes = context.getSharedPreferences("reactionTimes", context.MODE_PRIVATE);
        sessionsNumber = context.getSharedPreferences("sessionsNumber", context.MODE_PRIVATE); //?
        edit = reactionTimes.edit();
        edit2 = sessionsNumber.edit();
        session = sessionsNumber.getInt("sessionNumber", 0);
        pressCount = 0;
        totalReactionTime = 0;
    }

    private void updateAverageReactionTime(String reactionTime)
    { // called whenever the Controller receives a successful tap message from Buttons
        totalReactionTime += Long.parseLong(reactionTime);
        average = (totalReactionTime)/pressCount;
    }

    /* get method for the average reaction time of a session */
    public long getAverageReactionTime()
    {
        return average;
    }

    /* This method is for external use after a button press, to increment the press count to calculate the average */
    public void incrementPressCount(String reactionTime)
    {
        pressCount++;
        updateAverageReactionTime(reactionTime);
    }

    /* Use this method after a session has completed */
    public void reset()
    {
        session++;
        String value = Integer.toString(session);
        edit.putLong(value, average);
        edit.commit();

        value = "" + "sessionNumber";

        edit2.putInt(value, session);
        edit2.commit();

        pressCount = 0;
        totalReactionTime = 0;
        average = 0;
    }

    public long getAverageReacitonTime(int session)
    { // naming to circumvent getAverageReactionTime naming clash
        String value;
        long average;

        /* acquiring the pressCount value for the specific session  */
        value = Integer.toString(session);
        average = reactionTimes.getLong(value, -1);

        return average;
    }

    public long[] getAllAverageReactionTimes()
    { // not in use 5/2/2015. For use with SessionsNumber to keep track of past sessions
        String value = "sessionNumber";
        int sessionCount = sessionsNumber.getInt(value, 1);
        sessionCount = 1; // temporarily ignore sessionsNumber
        long retval[] = new long[sessionCount];

        for(int i = 1; i <= sessionCount; i++)
        {
            retval[i-1] = getAverageReacitonTime(i);
            Log.v("DataBeast", Long.toString(retval[i-1]));
        }

        return retval;
    }
}