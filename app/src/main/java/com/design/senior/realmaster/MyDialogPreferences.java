// makes a NumberPicker object for display in the Settings page (prefs.xml instantiates this)
package com.design.senior.realmaster;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.content.Context;
import android.util.Log;
import android.widget.NumberPicker;
import android.view.View;
import java.lang.reflect.Field;
import android.widget.EditText;
import android.graphics.Paint;
import android.os.Bundle;

/**
 * Created by CheeYi on 4/18/15.
 */

public class MyDialogPreferences extends DialogPreference {
    private NumberPicker minutesPicker = null;

    //private NumberPicker minutesPicker = null;
    private int minutes = 0;
    public int seconds = 0;
    //private int minutes = 1;


    // CONSTRUCTORS
    public MyDialogPreferences(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    // OVERRIDES
    @Override
    protected View onCreateDialogView() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        minutesPicker=new NumberPicker(getContext().getApplicationContext());
        minutesPicker.setMaxValue(15);
        minutesPicker.setMinValue(1);
        minutesPicker.setValue(Integer.parseInt(prefs.getString("sessionLength", "60"))/60);
        minutesPicker.setWrapSelectorWheel(true);
        setNumberPickerTextColor(minutesPicker,0xFF000000);
        return(minutesPicker);
    }

    @Override // if user hits OK
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            Log.v("Dialog response", "OK");
            minutesPicker.clearFocus();
            minutes = minutesPicker.getValue();
            seconds = minutesToSeconds(minutes);
            Log.v("Seconds calculated: ", Integer.toString(seconds));

            // write user-specified session length to SharedPreferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("sessionLength",Integer.toString(seconds));
            editor.commit(); // save changes
        }

    }

    public static int minutesToSeconds(int minutes) {
        return minutes*60;
    }

    // makes NumberPicker text color visible
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                    Log.v("numberPicker", "No field exception");
                }
                catch(IllegalAccessException e){
                    Log.v("numberPicker", "Illegal access");
                }
                catch(IllegalArgumentException e){
                    Log.v("numberPicker", "Illegal argument");
                }
            }
        }
        return false;
    }
}
