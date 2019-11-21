package com.android.hospitalapplication.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.hospitalapplication.R;

/**
 * Created by Kunnu on 1/10/2018.
 */

public class AboutUs extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        mToolbar=findViewById(R.id.about_us_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Our Team");
    }
}
