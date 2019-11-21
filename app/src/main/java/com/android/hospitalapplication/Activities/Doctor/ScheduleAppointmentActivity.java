package com.android.hospitalapplication.Activities.Doctor;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.hospitalapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleAppointmentActivity extends AppCompatActivity {

    Toolbar mToolbar;
    Button setDate, setTime, schApt;
    EditText remarks;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointment);

        mToolbar = findViewById(R.id.apt_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Schedule Appointment");

        setDate = findViewById(R.id.set_date);
        setTime = findViewById(R.id.set_time);
        schApt = findViewById(R.id.schedule_apt);
        remarks = findViewById(R.id.remarks);


        String prefDate = getIntent().getStringExtra("pref_date");
        final String pat_id = getIntent().getStringExtra("pat_id");
        type=getIntent().getStringExtra("type_apt");
        Log.d("type :",""+type);

        setDate.setText(prefDate);
        setDate.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(c.YEAR);
                int month = c.get(c.MONTH);
                int day = c.get(c.DAY_OF_MONTH);

                DatePickerDialog datepicker = new DatePickerDialog(ScheduleAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        setDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datepicker.getDatePicker().setMinDate(Calendar.getInstance().getTime().getTime());
                datepicker.show();
            }

        });

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int hourOfDay = c.get(c.HOUR_OF_DAY);
                int minutes = c.get(c.MINUTE);

               /* TimePickerDialog tp = new TimePickerDialog(ScheduleAppointmentActivity.this,new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String AM_PM ;
                        String min;
                        if(i < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
                        if(i1<10){
                            min="0"+i1;
                        }
                        else{
                            min=""+i1;
                        }

                        setTime.setText(""+(i%12)+":"+min+AM_PM);
                    }
                },hourOfDay,minutes,false);

                tp.show();*/
               /* CustomTimePicker customTimePicker = new CustomTimePicker(ScheduleAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String AM_PM;
                        String min, hrs;
                        if (i < 12) {
                            AM_PM = "AM";
                            if (i < 10) {
                                hrs = "0" + i;
                            } else {
                                hrs = "" + i;
                            }
                        } else {
                            AM_PM = "PM";
                            if (i > 12) {
                                i %= 12;
                                if (i < 10) {
                                    hrs = "0" + i;
                                } else {
                                    hrs = "" + i;
                                }
                            } else {
                                i = 12;
                                hrs = "" + i;
                            }
                        }

                        if (i1 < 10) {
                            min = "0" + i1;
                        } else {
                            min = "" + i1;
                        }
                        setTime.setText("" + (hrs) + ":" + min + AM_PM);
                    }
                }, hourOfDay, minutes, false);
                customTimePicker.show(); */

                AlertDialog.Builder b = new AlertDialog.Builder(ScheduleAppointmentActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.alert_dialog_time_picker,null);
                final NumberPicker hrs = v.findViewById(R.id.hours);
                final NumberPicker min = v.findViewById(R.id.minutes);

                min.setMinValue(0);
                min.setMaxValue(1);
                List<String> displayedValue = new ArrayList<>();
                displayedValue.add(String.format("%02d", 0));
                displayedValue.add(String.format("%02d", 30));
                min.setDisplayedValues(displayedValue.toArray(new String[displayedValue.size()]));

                Spinner amPm = v.findViewById(R.id.am_pm);
                amPm = initSpinner(amPm,R.array.time_zone);
                amPm.setBackgroundColor(getResources().getColor(R.color.md_blue_grey_300));

                final Spinner finalAmPm1 = amPm;
                hrs.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                    }
                });
                amPm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i){ //AM
                            case 0 : hrs.setMaxValue(11);
                                     hrs.setMinValue(9);

                                     break;
                            case 1 :  {
                                hrs.setMaxValue(7);
                                hrs.setMinValue(5);
                            }
                            break;

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                               adapterView.setSelection(0);
                    }
                });
                b.setView(v);
                b.setTitle("Choose Time");
                b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int hr = hrs.getValue();
                        String hour,minute;
                        if(hr<10){
                            hour = "0"+hr;
                        }
                        else{
                            hour=""+hr;
                        }
                        if(min.getValue()==0){
                            minute="00";
                        }
                        else{
                            minute="30";
                        }
                        String ampm = finalAmPm1.getSelectedItem().toString();
                        setTime.setText(hour+":"+minute+ampm);

                    }
                });
                b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = b.create();
                alertDialog.show();
            }
        });

        setTime.setText("Set Appointment time");

        schApt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = formatDate(setDate.getText().toString());

                String time = setTime.getText().toString();
                String remark = remarks.getText().toString();
                String doc_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if (!(date.equals(null)) && !(time.equals(null))) {
                    setAppointment(pat_id, doc_id, date, time, remark);
                    finish();
                } else {
                    Toast.makeText(ScheduleAppointmentActivity.this, "Please select date & time for the appointment", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public Spinner initSpinner(Spinner s, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), arrayId, R.layout.spinner_style);
        adapter.setDropDownViewResource(R.layout.spinner_style);
        s.setAdapter(adapter);
        return s;
    }

    public void setAppointment(final String patId, final String docId, String date, String time, String remarks) {
        final DatabaseReference dbrefRoot = FirebaseDatabase.getInstance().getReference();


        Map aptDetails = new HashMap();

        aptDetails.put("Appointments/" + patId + "/" + docId + "/" + "apt_date", date);
        aptDetails.put("Appointments/" + docId + "/" + patId + "/" + "apt_date", date);
        aptDetails.put("Appointments/" + patId + "/" + docId + "/" + "apt_time", time);
        aptDetails.put("Appointments/" + docId + "/" + patId + "/" + "apt_time", time);
        aptDetails.put("Appointments/" + patId + "/" + docId + "/" + "apt_remarks", remarks);
        aptDetails.put("Appointments/" + docId + "/" + patId + "/" + "apt_remarks", remarks);
        aptDetails.put("Appointments/" + patId + "/" + docId + "/" + "apt_id", ServerValue.TIMESTAMP);
        aptDetails.put("Appointments/" + docId + "/" + patId + "/" + "apt_id", ServerValue.TIMESTAMP);


        dbrefRoot.updateChildren(aptDetails, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Map remove = new HashMap();
                    remove.put("Requests/" + patId + "/" + docId, null);
                    remove.put("Requests/" + docId + "/" + patId, null);
                    dbrefRoot.updateChildren(remove, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(getApplicationContext(), "Appointment Set", Toast.LENGTH_LONG).show();
                                HashMap<String, String> notifDetails = new HashMap<>();
                                notifDetails.put("from", docId);

                                notifDetails.put("type", type);

                                dbrefRoot.child("Notifications").child(patId).push().setValue(notifDetails);
                                Intent i = new Intent(ScheduleAppointmentActivity.this, AppointmentDetailsActivity.class);
                                i.putExtra("doc_id", docId);
                                i.putExtra("pat_id", patId);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }
            }
        });

    }

    public String formatDate(String date) {
        Log.d("inc date:", date);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date d = df.parse(date);
            c.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("new date", df.format(c.getTime()));
        return df.format(c.getTime());

    }
}
