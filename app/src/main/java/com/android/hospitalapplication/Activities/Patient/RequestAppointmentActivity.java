package com.android.hospitalapplication.Activities.Patient;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hospitalapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestAppointmentActivity extends AppCompatActivity {

    Spinner typeofproblem , doctor_list;
    View bottomSheet;
    LinearLayout doctordetails;
    Button request_Appointment,  preferred_appointment_date;
    ImageButton calldoctor;
    FrameLayout info;
    String docName;
    ImageView bPhoto;
    TextView doctorcontactnumber, doctoraddress,bsName,bsQualify,bsExperience,bsSpecialize;
    EditText describe_problem;
    private BottomSheetBehavior mBottomSheetBehavior1;


    private int requestStatus =-1;                     //-1=no request 0=sent 1=received(pending)


    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_appointment);
        bottomSheet = findViewById(R.id.bottom_sheet1);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setHideable(true);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

        doctordetails = findViewById(R.id.doctordetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.pat_app_bar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Request Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        doctor_list = findViewById(R.id.doctor_list);
        typeofproblem = findViewById(R.id.typeofproblem);
        request_Appointment = findViewById(R.id.Request_Appointment);
        preferred_appointment_date = findViewById(R.id.preferred_appointment_date);
        doctoraddress = findViewById(R.id.doctoraddress);
        doctorcontactnumber = findViewById(R.id.doctorcontactnumber);
        calldoctor = findViewById(R.id.calldoctor);
        info=findViewById(R.id.more_info);
        describe_problem = findViewById(R.id.describe);
        bsName=findViewById(R.id.bsName);
        bsQualify=findViewById(R.id.bsQualify);
        bsExperience=findViewById(R.id.bsExperience);
        bsSpecialize=findViewById(R.id.bsSpecialize);
        bPhoto=findViewById(R.id.user_profile_photo);

        request_Appointment.setEnabled(false);
        final Calendar cal = Calendar.getInstance();
        final int year = cal.get(cal.YEAR);
        final int month = cal.get(cal.MONTH);
        final int day = cal.get(cal.DAY_OF_MONTH);

        typeofproblem = initSpinner(typeofproblem, R.array.problem);

        //date picker is set
        preferred_appointment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datepicker = new DatePickerDialog(RequestAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        preferred_appointment_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datepicker.getDatePicker().setMinDate(Calendar.getInstance().getTime().getTime());
                datepicker.show();
            }
        });

        typeofproblem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Position", "" + position);
                if (!(position == 0)) {
                    doctordetails.setVisibility(View.VISIBLE);
                    request_Appointment.setEnabled(true);
                }
                else {
                    doctordetails.setVisibility(View.GONE);
                    request_Appointment.setEnabled(false);
                }
                switch (position) {
                    case 1:
                    case 2:
                    case 3:
                        fetchDoctor("General Physician");
                        break;
                    case 4:
                        fetchDoctor("ENT");
                        break;
                    case 5:
                         fetchDoctor("Gynecology");
                        break;
                    case 6:
                         fetchDoctor("Pediatrics");
                        break;
                    case 7:
                         fetchDoctor("Ophthalmology");
                        break;
                    case 8:
                         fetchDoctor("Dermatology");
                        break;
                    case 9:
                        fetchDoctor("Cardiology");
                        break;
                    case 10:
                        fetchDoctor("Neurology");
                        break;
                    case 11:
                         fetchDoctor("Dentistry");
                        break;
                    case 12:
                        fetchDoctor("Gastroenterology");
                        break;
                    case 13:
                        fetchDoctor("Urology");
                        break;
                    case 14:
                        fetchDoctor("Orthopedics");
                        break;
                    default:
                        Toast.makeText(RequestAppointmentActivity.this, "No Doctor Found", Toast.LENGTH_SHORT);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //doc spinner
        doctor_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                DatabaseReference dbrefUser = FirebaseDatabase.getInstance().getReference("Users");
                String docName = adapterView.getItemAtPosition(i).toString().trim();
                Log.d("doc name :",docName+" "+i);
                dbrefUser.orderByChild("name").equalTo(docName).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String contact = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("room_no").getValue().toString();
                        String bQualify=dataSnapshot.child("qualification").getValue().toString();
                        String bSpeciality=dataSnapshot.child("speciality").getValue().toString();
                        String bExp=dataSnapshot.child("experience").getValue().toString();
                        String gender = dataSnapshot.child("gender").getValue().toString();

                        if(gender.equals("F")){
                            bPhoto.setImageResource(R.drawable.f_doctor_avatar);
                        }
                        bsName.setText(name);
                        bsExperience.setText(bExp);
                        bsQualify.setText(bQualify);
                        bsSpecialize.setText(bSpeciality);
                        doctoraddress.setText(address);
                        doctorcontactnumber.setText(contact);

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //call buuton deatils
        calldoctor.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                String doctorNumber = doctorcontactnumber.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", doctorNumber, null));
                startActivity(intent);
            }
        });

        //doc profile
         info.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mBottomSheetBehavior1.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            else if(mBottomSheetBehavior1.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
          }
    });

        //appointment button is set
        request_Appointment.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                String preferred_appointmentdate = preferred_appointment_date.getText().toString();
                String doctorcontactno = doctorcontactnumber.getText().toString();
                String doctorsaddress = doctoraddress.getText().toString();
                String typesofproblem = typeofproblem.getSelectedItem().toString();
                String doctorname = doctor_list.getSelectedItem().toString();
                String describe = describe_problem.getText().toString();

                if (preferred_appointmentdate.equals("") || doctorcontactno.equals("") || doctorsaddress.equals("") || typesofproblem.equals("Type of problem") || doctorname.equals("Doctor name")) {
                    Toast.makeText(RequestAppointmentActivity.this, "Enter All The fields", Toast.LENGTH_SHORT).show();

                } else if (typesofproblem.equals("Type Of Problem")) {
                    typeofproblem.setFocusable(true);
                } else if (preferred_appointmentdate.equals("")) {
                    preferred_appointment_date.setError("Set Preferred Date");
                    preferred_appointment_date.setFocusable(true);
                } else if (doctorname.equals("Doctor Name")) {
                    doctor_list.setFocusable(true);
                } else if(requestStatus==-1){
                        sendAppointmentRequest(doctorname,preferred_appointmentdate,describe);
                        startActivity(new Intent(RequestAppointmentActivity.this, PatientActivity.class));
                        finish();
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

    public void fetchDoctor(final String spec) {
        final DatabaseReference dbrefUsers = FirebaseDatabase.getInstance().getReference("Users");
        final ArrayList<String> doctors = new ArrayList<>();

        dbrefUsers.orderByChild("speciality").equalTo(spec).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                doctors.add(dataSnapshot.child("name").getValue().toString());
                Log.d("No. of Docs :",""+doctors.size());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_style,doctors);
                adapter.setDropDownViewResource(R.layout.spinner_style);
                doctor_list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void sendAppointmentRequest(String name, final String preferredDate, final String description){

        final String patient_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference dbrefUser = FirebaseDatabase.getInstance().getReference("Users");
        final DatabaseReference dbrefRoot = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference dbrefNotif = FirebaseDatabase.getInstance().getReference("Notifications");

        dbrefUser.orderByChild("name").equalTo(name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String doctorId = dataSnapshot.getKey();
                Log.d("Doctor id",doctorId);
                dbrefUser.child(doctorId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String type = dataSnapshot.child("type").getValue().toString();
                        if(type.equals("Doctor")){
                            Map requestDetails = new HashMap();
                            requestDetails.put("Requests/"+patient_id+"/"+doctorId+"/"+"req_date",preferredDate);
                            requestDetails.put("Requests/"+doctorId+"/"+patient_id+"/"+"req_date",preferredDate);
                            requestDetails.put("Requests/"+patient_id+"/"+doctorId+"/"+"req_status","sent");
                            requestDetails.put("Requests/"+doctorId+"/"+patient_id+"/"+"req_status","requested");
                            requestDetails.put("Requests/"+patient_id+"/"+doctorId+"/"+"req_desc",description);
                            requestDetails.put("Requests/"+doctorId+"/"+patient_id+"/"+"req_desc",description);
                            dbrefRoot.updateChildren(requestDetails, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError==null){
                                        requestStatus=0;
                                        HashMap<String,String> notificationDetails = new HashMap<>();
                                        notificationDetails.put("from",patient_id);
                                        notificationDetails.put("type","request");

                                        dbrefNotif.child(doctorId).push().setValue(notificationDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(RequestAppointmentActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(RequestAppointmentActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        if(mBottomSheetBehavior1.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        else {
            finish();
        }
    }
}
