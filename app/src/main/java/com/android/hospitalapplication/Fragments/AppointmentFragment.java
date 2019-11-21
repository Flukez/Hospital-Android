package com.android.hospitalapplication.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.hospitalapplication.Activities.AppointmentReceiptActivity;
import com.android.hospitalapplication.Activities.Doctor.AppointmentDetailsActivity;
import com.android.hospitalapplication.ModelClasses.User;
import com.android.hospitalapplication.UtilityAndNetworkingClasses.OnSwipeListener;
import com.android.hospitalapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AppointmentFragment extends Fragment {

    View v;
    ImageView rightNav,leftNav;
    TextView date;
    RecyclerView appointments;
    String docId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference dbrefApt = FirebaseDatabase.getInstance().getReference("Appointments").child(docId);
    DatabaseReference dbrefUsers = FirebaseDatabase.getInstance().getReference("Users");


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentFragment newInstance(String param1, String param2) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_appointment, container, false);
        rightNav=v.findViewById(R.id.right_nav);
        leftNav=v.findViewById(R.id.left_nav);
        date=v.findViewById(R.id.date_cal);
        appointments=v.findViewById(R.id.appointment_list);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(appointments.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.divider_line));
        appointments.setLayoutManager(new LinearLayoutManager(getContext()));
        appointments.addItemDecoration(itemDecoration);

        String currDate = getCurrentDate();
        date.setText(currDate);

        appointments.setOnTouchListener(new OnSwipeListener(getContext()){
            @Override
            public void onSwipeRight() {
                String prevDate = decreaseDate();
                date.setText(prevDate);
                getAppointments(prevDate);
            }

            @Override
            public void onSwipeLeft() {
                String nextDate = increaseDate();
                date.setText(nextDate);
                getAppointments(nextDate);
            }
        });
        getAppointments(currDate);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nextDate = increaseDate();
                date.setText(nextDate);
                getAppointments(nextDate);
            }
        });

        leftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prevDate = decreaseDate();
                date.setText(prevDate);
                getAppointments(prevDate);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                DatePickerDialog dp = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        date.setText(i2 + "/" + (i1 + 1) + "/" + i);
                        String currDate = date.getText().toString();
                        Log.d("new Date:",currDate);
                        getAppointments(currDate);
                    }
                },year,month,dayOfMonth);
                dp.show();
            }

        });



    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
          //  throw new RuntimeException(context.toString()
              //      + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String getCurrentDate(){
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(d);
    }

    public String increaseDate(){
        String currDate = date.getText().toString();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = df.parse(currDate);
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH,1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return df.format(c.getTime());
    }

    public String decreaseDate(){
        String currDate = date.getText().toString();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = df.parse(currDate);
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH,-1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return df.format(c.getTime());
    }

    public void getAppointments(final String date){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Calendar c = Calendar.getInstance();

            Date d = df.parse(date);
            c.setTime(d);
            final String newDate = df.format(c.getTime());

            Query q = dbrefApt.orderByChild("apt_date").equalTo(newDate);
            FirebaseRecyclerAdapter<User,PatientViewHolder> adapter = new FirebaseRecyclerAdapter<User, PatientViewHolder>(User.class,R.layout.user_appointment_list,PatientViewHolder.class,q) {
                @Override
                protected void populateViewHolder(final PatientViewHolder viewHolder, User model, int position) {
                    final String pat_id = getRef(position).getKey();

                    dbrefApt.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(pat_id)){
                                final String time = dataSnapshot.child(pat_id).child("apt_time").getValue().toString();
                                Log.d("pat id :",""+pat_id);
                                dbrefUsers.child(pat_id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String name = dataSnapshot.child("name").getValue().toString();
                                            String gender = dataSnapshot.child("gender").getValue().toString();

                                            if(gender.equals("F")){
                                                viewHolder.setImage(R.drawable.avatar_fm);
                                            }
                                            viewHolder.setName(name);
                                            viewHolder.setTime(time);
                                            viewHolder.v.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent i = new Intent(getActivity(), AppointmentDetailsActivity.class);
                                                    i.putExtra("doc_id",docId);
                                                    i.putExtra("pat_id",pat_id);
                                                    startActivity(i);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            };
            appointments.setAdapter(adapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder{

        private View v;
        private TextView pName,time;
        private ImageView photo;
        public PatientViewHolder(View itemView) {
            super(itemView);
            v=itemView;
        }

        public void setName(String name){
            pName=v.findViewById(R.id.name);
            pName.setText(name);
        }

        public void setTime(String time){
            this.time=v.findViewById(R.id.appointment_time);
            this.time.setText(time);
        }

        public void setImage(int resId){
            photo=v.findViewById(R.id.pat_photo);
            photo.setImageResource(resId);
        }
    }
}
