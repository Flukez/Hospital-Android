package com.android.hospitalapplication.UtilityAndNetworkingClasses;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.content.ContentValues.TAG;

/**
 * Created by Gaurav on 31-12-2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    public void sendRegistrationToServer(String token){

        DatabaseReference dbrefUsers = FirebaseDatabase.getInstance().getReference("Users");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(uid!=null) {
            dbrefUsers.child(uid).child("device_token").setValue(token);
        }

    }
}
