package com.android.hospitalapplication.UtilityAndNetworkingClasses;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.icu.text.NumberFormat;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.android.hospitalapplication.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaurav on 07-01-2018.
 */

public class CustomTimePicker extends TimePickerDialog {
    private final static int TIME_PICKER_INTERVAL = 30;
    private TimePicker mTimePicker;
    private final OnTimeSetListener mTimeSetListener;
    private String amPm;
    int lastHour;


    public CustomTimePicker(Context context, OnTimeSetListener listener,
                            int hourOfDay, int minute, boolean is24HourView) {
        super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, hourOfDay,
                minute / TIME_PICKER_INTERVAL, is24HourView);
        mTimeSetListener = listener;
    }

    @Override
    public void updateTime(int hourOfDay, int minuteOfHour) {


    //    mTimePicker.setCurrentHour(hourOfDay);
    //    mTimePicker.setCurrentMinute(minuteOfHour / TIME_PICKER_INTERVAL);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mTimeSetListener != null) {
                    if(lastHour<10){
                        lastHour+=12;
                    }
                    mTimeSetListener.onTimeSet(mTimePicker,lastHour,
                            mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
                    Log.d("Time :",""+mTimePicker.getCurrentHour()+":"+mTimePicker.getCurrentMinute());
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        super.onTimeChanged(view, hourOfDay, minute);

        if(lastHour!=hourOfDay ){
            view.setCurrentHour(lastHour);
            view.setCurrentMinute(minute/TIME_PICKER_INTERVAL);
        }
        Log.d("Time :",""+lastHour+":"+mTimePicker.getCurrentMinute()+" "+hourOfDay+":"+minute);

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            mTimePicker = (TimePicker) findViewById(timePickerField.getInt(null));
            Field field = classForid.getField("minute");
            Field field1 = classForid.getField("hour");
            Field field2 = classForid.getField("amPm");

            NumberPicker minuteSpinner = (NumberPicker) mTimePicker
                    .findViewById(field.getInt(null));
            final NumberPicker hourSpinner = mTimePicker.findViewById(field1.getInt(null));
            final NumberPicker amPmSpinner = mTimePicker.findViewById(field2.getInt(null));

            minuteSpinner.setMinValue(0);
            minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minuteSpinner.setDisplayedValues(displayedValues
                    .toArray(new String[displayedValues.size()]));

            Log.d("last hour :",""+lastHour);

            if(amPmSpinner.getValue()==0){
                hourSpinner.setMinValue(10);
                hourSpinner.setMaxValue(12);
                amPm="AM";
                lastHour=hourSpinner.getValue();

            }
            else{
                amPm="PM";
                hourSpinner.setMinValue(5);
                hourSpinner.setMaxValue(7);
                lastHour=hourSpinner.getValue();

            }
            hourSpinner.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                    lastHour=i1;
                    if(i1==12){
                        amPmSpinner.setValue(1);
                    }
                }
            });
            amPmSpinner.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    if(i1 == 0){ //AM
                        hourSpinner.setMinValue(10);
                        hourSpinner.setMaxValue(12);
                        amPm="AM";
                        lastHour=hourSpinner.getValue();


                    }
                    else{ //Pm
                        amPm="PM";
                        hourSpinner.setMinValue(5);
                        hourSpinner.setMaxValue(7);
                        lastHour=hourSpinner.getValue();

                    }
                    Log.d("AM/PM ",amPm+" "+i1);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
