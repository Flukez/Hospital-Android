package com.android.hospitalapplication.Activities.Doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.hospitalapplication.Activities.LoginActivity;
import com.android.hospitalapplication.Fragments.AppointmentFragment;
import com.android.hospitalapplication.Fragments.ProfileInfoFragment;
import com.android.hospitalapplication.Fragments.RequestAppointmentFragment;
import com.android.hospitalapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    BottomNavigationView btmNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        Toolbar toolbar =  findViewById(R.id.doc_app_bar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Doctor");


        btmNav=findViewById(R.id.navigation_doc);

        String notifFragment = getIntent().getStringExtra("notif_frag");
        if(notifFragment!=null){
            if(notifFragment.equals("new_req")) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.content_doc, new RequestAppointmentFragment()).commit();
                btmNav.setSelectedItemId(R.id.action_new_requests);
            }
        }else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content_doc, new AppointmentFragment()).commit();
        }

        btmNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fm  = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
               switch(item.getItemId()){
                   case R.id.action_appointments :
                       ft.replace(R.id.content_doc,new AppointmentFragment()).commit();
                       return true;
                   case R.id.action_new_requests :
                       ft.replace(R.id.content_doc,new RequestAppointmentFragment()).commit();
                       return true;
                   case R.id.action_profile :
                       ft.replace(R.id.content_doc,new ProfileInfoFragment()).commit();
                       return true;
                       default:
                           Toast.makeText(DoctorActivity.this,"No Fragment Found",Toast.LENGTH_SHORT).show();
                           return false;
               }

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doc_main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch(item.getItemId()){
           case R.id.sign_out :
               auth.signOut();
               startActivity(new Intent(this,LoginActivity.class));
               finish();
       }
       return true;
    }


}
