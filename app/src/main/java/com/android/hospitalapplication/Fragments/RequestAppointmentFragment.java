package com.android.hospitalapplication.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.hospitalapplication.Activities.Doctor.SelectAppointmentActivity;
import com.android.hospitalapplication.Activities.Patient.AppointmentStatusActivity;
import com.android.hospitalapplication.ModelClasses.User;
import com.android.hospitalapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RequestAppointmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequestAppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestAppointmentFragment extends Fragment {

    private View mView;
    String doctorId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    DatabaseReference dbrefRequests = FirebaseDatabase.getInstance().getReference("Requests").child(doctorId);
    DatabaseReference dbrefUsers = FirebaseDatabase.getInstance().getReference("Users");

    RecyclerView requestList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RequestAppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestAppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestAppointmentFragment newInstance(String param1, String param2) {
        RequestAppointmentFragment fragment = new RequestAppointmentFragment();
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
       mView= inflater.inflate(R.layout.fragment_request_appointment, container, false);
       requestList=mView.findViewById(R.id.req_list);

        FirebaseRecyclerAdapter<User,AppointmentStatusActivity.RequestsViewHolder> adapter = new FirebaseRecyclerAdapter<User, AppointmentStatusActivity.RequestsViewHolder>(User.class, R.layout.user_apt_list, AppointmentStatusActivity.RequestsViewHolder.class,dbrefRequests) {
            @Override
            protected void populateViewHolder(final AppointmentStatusActivity.RequestsViewHolder viewHolder, User model, int position) {
                                 final String u_id = getRef(position).getKey();
                                 Log.d("pat id :",u_id);
                                 dbrefRequests.addValueEventListener(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                         if(dataSnapshot.hasChild(u_id)) {
                                             final String prefDate = dataSnapshot.child(u_id).child("req_date").getValue().toString();
                                             final String desc = dataSnapshot.child(u_id).child("req_desc").getValue().toString();
                                             final String reqType = dataSnapshot.child(u_id).child("req_status").getValue().toString();
                                             if (reqType.equals("requested")) {
                                                 dbrefUsers.child(u_id).addValueEventListener(new ValueEventListener() {
                                                     @Override
                                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                                         String name = dataSnapshot.child("name").getValue().toString();
                                                         final String bg = dataSnapshot.child("blood_group").getValue().toString();
                                                         String gender = dataSnapshot.child("gender").getValue().toString();
                                                         Log.d("name :", name + "\n" + bg);

                                                         if(gender.equals("M")) {
                                                             viewHolder.photo.setImageResource(R.drawable.avatar);
                                                         }
                                                         else{
                                                             viewHolder.photo.setImageResource(R.drawable.avatar_fm);
                                                         }
                                                         viewHolder.setStatus("");
                                                         viewHolder.setDate(prefDate);
                                                         viewHolder.setName(name);
                                                         viewHolder.dateOrDesc.setText("Pref. Date");
                                                         viewHolder.cancel.setVisibility(View.INVISIBLE);
                                                         viewHolder.view.setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View view) {
                                                                 Intent i = new Intent(getActivity(), SelectAppointmentActivity.class);
                                                                 i.putExtra("desc", desc);
                                                                 i.putExtra("pref_date", prefDate);
                                                                 i.putExtra("pat_id", u_id);
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
                                     }

                                     @Override
                                     public void onCancelled(DatabaseError databaseError) {

                                     }
                                 });
            }
        };
        requestList.setLayoutManager(new LinearLayoutManager(getActivity()));
        requestList.setAdapter(adapter);
        return mView;
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
        //    throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
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
}
