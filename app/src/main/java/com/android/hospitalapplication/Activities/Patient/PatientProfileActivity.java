package com.android.hospitalapplication.Activities.Patient;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

/**
 * Created by hp on 24-12-2017.
 */
public class PatientProfileActivity extends AppCompatActivity {
    TextView gender,bg;
    EditText FnameValue,GenderValue,bloodgrpValue,AddressValue,phoneValue,profileDob;
    ImageButton EditProfile;
    DatabaseReference dbrefUsers = FirebaseDatabase.getInstance().getReference("Users");
    Button Update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_profile);

        Toolbar toolbar =  findViewById(R.id.profileAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View profile");

        FnameValue=findViewById(R.id.FnameValue);
        EditProfile=findViewById(R.id.EditProfile);
        GenderValue=findViewById(R.id.genderValue);
        bloodgrpValue=findViewById(R.id.bloodgrpValue);
        AddressValue=findViewById(R.id.AddressValue);
        phoneValue=findViewById(R.id.MobileValue);
        Update=findViewById(R.id.Update);
        gender=findViewById(R.id.Gender);
        bg=findViewById(R.id.Bloodgrp);
        profileDob=findViewById(R.id.profileDob);
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        fetchData(uid);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profile,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.EditProfile :
                Update.setVisibility(View.VISIBLE);
                FnameValue.setEnabled(true);
                FnameValue.setCursorVisible(true);
                AddressValue.setEnabled(true);
                phoneValue.setEnabled(true);
                profileDob.setEnabled(true);
                gender.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
                bg.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
                GenderValue.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
                GenderValue.setTextColor(Color.GRAY);
                bloodgrpValue.setTextColor(Color.GRAY);
                bloodgrpValue.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
                Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference dbrefUser = FirebaseDatabase.getInstance().getReference("Users").child(uid);

                        String name=FnameValue.getText().toString();
                        String address=AddressValue.getText().toString();
                        String phone=phoneValue.getText().toString();
                        String dob=profileDob.getText().toString();
                        Map info = new HashMap();
                        info.put("name",name);
                        info.put("address",address);
                        info.put("phone",phone);
                        info.put("d_o_b",dob);
                        dbrefUser.updateChildren(info, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError==null)
                                {
                                    Toast.makeText(PatientProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    Update.setVisibility(View.GONE);
                                    FnameValue.setEnabled(false);
                                    FnameValue.setCursorVisible(false);
                                    AddressValue.setEnabled(false);
                                    phoneValue.setEnabled(false);
                                    profileDob.setEnabled(false);
                                    gender.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                                    bg.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                                    GenderValue.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                                    GenderValue.setTextColor(Color.BLACK);
                                    bloodgrpValue.setTextColor(Color.BLACK);
                                    bloodgrpValue.setBackgroundColor(getResources().getColor(R.color.md_white_1000));

                                }
                                else
                                {
                                    Toast.makeText(PatientProfileActivity.this, "Error Updating Profile", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });


        }
        return true;
    }


    private void fetchData(String u_id)
    {
        dbrefUsers.child(u_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String fname=dataSnapshot.child("name").getValue().toString();
                String gender=dataSnapshot.child("gender").getValue().toString();
                String bloodgrp=dataSnapshot.child("blood_group").getValue().toString();
                String phone=dataSnapshot.child("phone").getValue().toString();
                String dob=dataSnapshot.child("d_o_b").getValue().toString();
                String Address=dataSnapshot.child("address").getValue().toString();

                if(gender.equals("M"))
                {
                    GenderValue.setText("Male");

                }
                else
                {
                    GenderValue.setText("Female");
                }

                FnameValue.setText(fname);
                profileDob.setText(dob);
                phoneValue.setText(phone);
                bloodgrpValue.setText(bloodgrp);
                AddressValue.setText(Address);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
