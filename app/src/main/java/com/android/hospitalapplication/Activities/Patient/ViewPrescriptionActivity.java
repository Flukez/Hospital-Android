package com.android.hospitalapplication.Activities.Patient;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.android.hospitalapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewPrescriptionActivity extends AppCompatActivity {

    ImageView presView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prescription);

        Toolbar toolbar =  findViewById(R.id.prescription_bar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Prescription");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.transparent)));


        presView = findViewById(R.id.prescription);
        final String docId = getIntent().getStringExtra("doc_id");
        String patId = getIntent().getStringExtra("pat_id");

        DatabaseReference dbrefPresc = FirebaseDatabase.getInstance().getReference("Prescriptions");
        dbrefPresc.child(docId).child(patId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String downloadUrl = dataSnapshot.child("image_url").getValue().toString();
                Log.d("image_url :",downloadUrl);

                Picasso.with(ViewPrescriptionActivity.this).load(downloadUrl).fit().into(presView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
