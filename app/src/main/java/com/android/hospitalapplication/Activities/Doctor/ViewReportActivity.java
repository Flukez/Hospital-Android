package com.android.hospitalapplication.Activities.Doctor;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.android.hospitalapplication.R;
import com.squareup.picasso.Picasso;

public class ViewReportActivity extends AppCompatActivity {

    private ImageView showReport ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        final Toolbar toolbar =  findViewById(R.id.view_report_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Report");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.transparent)));

        showReport=findViewById(R.id.view_report);

        showReport.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    if(getSupportActionBar().isShowing()){
                        getSupportActionBar().hide();
                    }
                    else{
                        getSupportActionBar().show();
                    }
                    return  true;
                }
                else {
                    return false;
                }
            }
        });
        String url = getIntent().getStringExtra("img_url");

        Picasso.with(this).setLoggingEnabled(true);

        Picasso.with(this).load(url).fit().into(showReport);
    }
}
