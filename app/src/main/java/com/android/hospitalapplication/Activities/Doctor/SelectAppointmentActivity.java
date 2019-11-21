package com.android.hospitalapplication.Activities.Doctor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hospitalapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SelectAppointmentActivity extends AppCompatActivity {

    TextView namePat,genderPat,phonePat,descPat,datePat,bgPat;
    Button declineReq,acceptReq;
    Toolbar mToolbar;
    String docId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference dbrefRoot = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbrefUsers = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_appointment);

        mToolbar=findViewById(R.id.apt_app_bar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Set Appointment");

        namePat=findViewById(R.id.name);
        genderPat=findViewById(R.id.gender);
        phonePat=findViewById(R.id.phone);
        datePat=findViewById(R.id.date);
        descPat=findViewById(R.id.desc);
        bgPat=findViewById(R.id.bg);

        declineReq=findViewById(R.id.decline);
        acceptReq=findViewById(R.id.set);

        final String u_id = getIntent().getStringExtra("pat_id");

        fetchData(u_id);

        declineReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder bd = new AlertDialog.Builder(SelectAppointmentActivity.this);
                bd.setTitle("Reject Appointment Request ?").setMessage("Enter A Reason");

                final EditText reason = new EditText(SelectAppointmentActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                reason.setLayoutParams(lp);
                reason.setMaxLines(2);


                bd.setView(reason);
                bd.setPositiveButton("Decline Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String r = reason.getText().toString();
                        if(!r.equals(null)) {
                            Map remove = new HashMap();
                            remove.put("Requests/" + u_id + "/" + docId + "/reason", r);
                            remove.put("Requests/" + u_id + "/" + docId + "/req_status", "declined");
                            remove.put("Requests/" + docId + "/" + u_id, null);
                            dbrefRoot.updateChildren(remove, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        Toast.makeText(getApplicationContext(), "Request Declined", Toast.LENGTH_LONG).show();
                                        HashMap<String,String> notifDetails = new HashMap<>();
                                        notifDetails.put("from",docId);
                                        notifDetails.put("type","declined");
                                        dbrefRoot.child("Notifications").child(u_id).push().setValue(notifDetails);
                                        startActivity(new Intent(SelectAppointmentActivity.this, DoctorActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }


                            });
                        }
                        else{
                            reason.setError("Empty Field");
                        }

                    }
                });

                bd.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog ad = bd.create();
                ad.show();


            }
        });

        acceptReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectAppointmentActivity.this,ScheduleAppointmentActivity.class);
                i.putExtra("pat_id",u_id);
                i.putExtra("pref_date",getIntent().getStringExtra("pref_date"));
                i.putExtra("type_apt","confirmed");
                startActivity(i);

            }
        });
    }


    public void fetchData(String u_id){
        dbrefUsers.child(u_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String gender = dataSnapshot.child("gender").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String bg = dataSnapshot.child("blood_group").getValue().toString();
                String desc = getIntent().getStringExtra("desc");
                String prefDate = getIntent().getStringExtra("pref_date");

                namePat.setText(name);
                genderPat.setText(gender);
                phonePat.setText(phone);
                bgPat.setText(bg);
                descPat.setText(desc);
                datePat.setText(prefDate);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
