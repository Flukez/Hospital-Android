package com.android.hospitalapplication.UtilityAndNetworkingClasses;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.hospitalapplication.Activities.AppointmentReceiptActivity;
import com.android.hospitalapplication.Activities.Doctor.DoctorActivity;
import com.android.hospitalapplication.Activities.Patient.AppointmentStatusActivity;
import com.android.hospitalapplication.Activities.Patient.PatientActivity;
import com.android.hospitalapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gaurav on 31-12-2017.
 */

public class FirebaseNotificationsService extends FirebaseMessagingService {

    public static final String CHANNEL_ID = "HospitalApp Notification Channel" ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("payload :", "" + remoteMessage.getData());

        String titleNotif = remoteMessage.getData().get("title_notif");
        String contentNotif = remoteMessage.getData().get("body_notif");
        String from = remoteMessage.getData().get("from_user_id");
        String typeNotif = remoteMessage.getData().get("type");

        if(typeNotif.equals("request")){
        Intent i  = new Intent(this, DoctorActivity.class);
        i.putExtra("notif_frag","new_req");
        PendingIntent resultingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        createNotification(titleNotif,contentNotif,resultingIntent);

        }
        else if(typeNotif.equals("confirmed")){
            Intent i = new Intent(this, AppointmentReceiptActivity.class);
            String docId = from;
            String patId = remoteMessage.getData().get("to_user_id");
            i.putExtra("doc_id",docId);
            i.putExtra("pat_id",patId);
            PendingIntent resultingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            createNotification(titleNotif,contentNotif,resultingIntent);
        }
        else if(typeNotif.equals("rescheduled")){
            Intent i = new Intent(this, AppointmentReceiptActivity.class);
            String docId = from;
            String patId = remoteMessage.getData().get("to_user_id");
            i.putExtra("doc_id",docId);
            i.putExtra("pat_id",patId);
            PendingIntent resultingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            createNotification(titleNotif,contentNotif,resultingIntent);
        }
        else if(typeNotif.equals("follow")){
            Intent i = new Intent(this, AppointmentReceiptActivity.class);
            String docId = from;
            String patId = remoteMessage.getData().get("to_user_id");
            i.putExtra("doc_id",docId);
            i.putExtra("pat_id",patId);
            PendingIntent resultingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            createNotification(titleNotif,contentNotif,resultingIntent);
        }
        else if(typeNotif.equals("declined")){
            Intent i = new Intent(this, AppointmentStatusActivity.class);
            PendingIntent resultingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            createNotification(titleNotif,contentNotif,resultingIntent);
        }



    }

    private void createNotification(String titleNotif, String contentNotif, PendingIntent resultPendingIntent) {

        Notification notification = new Notification();
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notification.defaults |= Notification.DEFAULT_SOUND;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = "Channel 1";
            String description = "Channel for notifications";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            // Register the channel with the system
            mNotifyMgr.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,CHANNEL_ID);
        mBuilder.setDefaults(notification.defaults);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titleNotif)
                .setContentText(contentNotif)
                .setAutoCancel(true)
                .setVibrate(new long[]{50, 350, 200, 350, 200})
                .setLights(Color.RED, 3000, 3000)
                .setContentIntent(resultPendingIntent);

        int mNotificationId = (int) System.currentTimeMillis();

        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

    public void addReminder(final String docID, final String patId){


        DatabaseReference dbrefUsers = FirebaseDatabase.getInstance().getReference("Users").child(docID);
        dbrefUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String name = dataSnapshot.child("name").getValue().toString();

                DatabaseReference dbrefApt = FirebaseDatabase.getInstance().getReference("Appointments").child(docID).child(patId);

                dbrefApt.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String date = dataSnapshot.child("apt_date").getValue().toString();
                        String time = dataSnapshot.child("apt_time").getValue().toString();
                        long eventTime = 0;
                        try {
                            eventTime = getTimeInMillis(date+" "+time);
                            Log.d("time in mils :",""+eventTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                        time =df.format(time);
                        String s[] = time.split(":");
                        int hr = Integer.parseInt(s[0]);
                        int min = Integer.parseInt(s[1]);
                        NotificationScheduler.setReminder(getApplicationContext(), PatientActivity.class,hr,min);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//this means reminder is added

    }

    public long getTimeInMillis(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        long millis;

        Date d = sdf.parse(date);
        millis= d.getTime();
        Log.d("date , millis:",date+" "+millis);
        return millis;
    }
}