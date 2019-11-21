package com.android.hospitalapplication.Activities.Doctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.hospitalapplication.Activities.Patient.ViewPrescriptionActivity;
import com.android.hospitalapplication.ModelClasses.Report;
import com.android.hospitalapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportsActivity extends AppCompatActivity {

    TextView nameText;
    RecyclerView reportList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Toolbar toolbar =  findViewById(R.id.report_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient Reports");

        nameText = findViewById(R.id.tv1);
        reportList = findViewById(R.id.report_list);
        reportList.setLayoutManager(new LinearLayoutManager(this));
        String patId = getIntent().getStringExtra("pat_id");

        DatabaseReference dbrefUsers  = FirebaseDatabase.getInstance().getReference("Users").child(patId);

        dbrefUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String s = name+"\'s Reports :-";
                nameText.setText(s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        getReports(patId);
    }

    public void getReports(String uid){

        final DatabaseReference dbrefReports = FirebaseDatabase.getInstance().getReference("Reports").child(uid);
        FirebaseRecyclerAdapter<Report,ReportHolder> adapter = new FirebaseRecyclerAdapter<Report, ReportHolder>(Report.class,R.layout.report_list_layout,ReportHolder.class,dbrefReports) {
            @Override
            protected void populateViewHolder(final ReportHolder viewHolder, Report model, final int position) {
                String pushId= getRef(position).getKey();
                Log.d("img push id :",pushId);
                dbrefReports.child(pushId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = (position+1)+". "+dataSnapshot.child("image_name").getValue().toString();
                        final String downloadUrl = dataSnapshot.child("image_url").getValue().toString();
                        Log.d("img name & url:",name+" "+downloadUrl);
                        viewHolder.setName(name);
                        viewHolder.v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i  = new Intent(ReportsActivity.this, ViewReportActivity.class);
                                i.putExtra("img_url",downloadUrl);
                                startActivity(i);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        reportList.setAdapter(adapter);

    }

    public static class ReportHolder extends RecyclerView.ViewHolder{
        public View v;
        public ReportHolder(View itemView) {

            super(itemView);
            v=itemView;

        }

        public void setName(String imgName){
            TextView name = v.findViewById(R.id.report_name);
            name.setText(imgName);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
