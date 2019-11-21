package com.android.hospitalapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.hospitalapplication.Activities.Patient.ViewPrescriptionActivity;
import com.android.hospitalapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppointmentReceiptActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView aptId,docName,patName,aptDate,aptRemarks;
    private Button viewPresc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_receipt);
        mToolbar=findViewById(R.id.apt_receipt_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Appointment Details");

        aptId=findViewById(R.id.apt_id);
        aptDate=findViewById(R.id.date_time);
        aptRemarks=findViewById(R.id.remarks);
        docName=findViewById(R.id.doc_name);
        patName=findViewById(R.id.pat_name);
        viewPresc=findViewById(R.id.view_prescriptions);


        String docId = getIntent().getStringExtra("doc_id");
        String patId = getIntent().getStringExtra("pat_id");

        getDetails(docId,patId);
    }

    public void getDetails(final String docId, final String patID){
        final DatabaseReference dbrefRoot = FirebaseDatabase.getInstance().getReference();

        dbrefRoot.child("Appointments").child(docId).child(patID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String date = dataSnapshot.child("apt_date").getValue().toString();
                final String time = dataSnapshot.child("apt_time").getValue().toString();
                final String id = dataSnapshot.child("apt_id").getValue().toString();
                final String remark = dataSnapshot.child("apt_remarks").getValue().toString();

                dbrefRoot.child("Users").child(docId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String doctorName = dataSnapshot.child("name").getValue().toString();
                        dbrefRoot.child("Users").child(patID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String patientName = dataSnapshot.child("name").getValue().toString();

                                aptDate.setText(date+" "+time);
                                aptId.setText(id);
                                aptRemarks.setText(remark);
                                docName.setText(doctorName);
                                patName.setText(patientName);

                                String currDate = getCurrentDate();
                                try {
                                    if(compareDates(date,currDate)){
                                        viewPresc.setVisibility(View.VISIBLE);
                                        viewPresc.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent i = new Intent(AppointmentReceiptActivity.this, ViewPrescriptionActivity.class);
                                                i.putExtra("pat_id",patID);
                                                i.putExtra("doc_id",docId);
                                                startActivity(i);
                                            }
                                        });
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getCurrentDate(){
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(d);
    }

    public boolean compareDates(String dateBefore,String dateAfter) throws ParseException{
           SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        return (df.parse(dateBefore).before(df.parse(dateAfter)));
    }
}
