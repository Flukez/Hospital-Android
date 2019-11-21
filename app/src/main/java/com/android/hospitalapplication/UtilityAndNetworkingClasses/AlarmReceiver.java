package com.android.hospitalapplication.UtilityAndNetworkingClasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.hospitalapplication.Activities.Patient.PatientActivity;

/**
 * Created by User on 11-01-2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationScheduler.showNotification(context, PatientActivity.class,
                "Appointment Approaching", "You Have An Appointment Soon");
    }
}
