package com.android.hospitalapplication.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View v;
    TextView time,time1,id;
    EditText user_profile_name,user_profile_short_bio,exp_value,room,registration_id_doc,morning,evening,mobile,specalize;
    ImageButton editProfile;
    Button update;
    ImageView photo;
    DatabaseReference dbrefUsers = FirebaseDatabase.getInstance().getReference("Users");
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileInfoFragment() {
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
    public static ProfileInfoFragment newInstance(String param1, String param2) {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
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
        v = inflater.inflate(R.layout.fragment_view_profile, container, false);
        user_profile_name=v.findViewById(R.id.user_profile_name);
        user_profile_short_bio=v.findViewById(R.id.user_profile_short_bio);
        exp_value=v.findViewById(R.id.exp_value);
        room=v.findViewById(R.id.room);
        registration_id_doc=v.findViewById(R.id.registration_id_doc);
        mobile=v.findViewById(R.id.mobile);
        specalize=v.findViewById(R.id.specalize);
        update=v.findViewById(R.id.update);
        editProfile=v.findViewById(R.id.editProfile);
        id=v.findViewById(R.id.id);
        photo=v.findViewById(R.id.user_profile_photo);
        time=v.findViewById(R.id.time);
        time1=v.findViewById(R.id.time1);
        morning=v.findViewById(R.id.morning);
        evening=v.findViewById(R.id.evening);
        String u_id= FirebaseAuth.getInstance().getCurrentUser().getUid();
        fetchData(u_id);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update.setVisibility(View.VISIBLE);
                user_profile_name.setCursorVisible(true);
                user_profile_name.setEnabled(true);
                user_profile_short_bio.setEnabled(true);
                exp_value.setEnabled(true);
                mobile.setEnabled(true);
                room.setEnabled(true);
                specalize.setEnabled(true);
                registration_id_doc.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
                id.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
                evening.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
                morning.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
                time.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
                time1.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
                evening.setTextColor(Color.GRAY);
                morning.setTextColor(Color.GRAY);
                registration_id_doc.setTextColor(Color.GRAY);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference dbrefUser = FirebaseDatabase.getInstance().getReference("Users").child(uid);

                        String name=user_profile_name.getText().toString();
                        String Qualify=user_profile_short_bio.getText().toString();
                        String experience=exp_value.getText().toString();
                        String number=mobile.getText().toString();
                        String room1=room.getText().toString();

                        String specalizisation=specalize.getText().toString();
                        Map info = new HashMap();
                        info.put("name",name);
                        info.put("qualification",Qualify);
                        info.put("phone",number);
                        info.put("experience",experience);
                        info.put("room_no",room1);
                        info.put("speciality",specalizisation);
                        dbrefUser.updateChildren(info, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError==null)
                                {
                                    Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();

                                    user_profile_name.setEnabled(false);
                                    user_profile_short_bio.setEnabled(false);
                                    exp_value.setEnabled(false);
                                    mobile.setEnabled(false);
                                    room.setEnabled(false);
                                    specalize.setEnabled(false);
                                    update.setVisibility(View.INVISIBLE);
                                    registration_id_doc.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                                    id.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                                    evening.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                                    morning.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                                    time.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                                    time1.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                                    evening.setTextColor(Color.BLACK);
                                    morning.setTextColor(Color.BLACK);
                                    registration_id_doc.setTextColor(Color.BLACK);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Error Updating Profile", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

            }
        });

        return v;
    }
    public void fetchData(String u_id){
        dbrefUsers.child(u_id).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String Qualify = dataSnapshot.child("qualification").getValue().toString();
                String expe = dataSnapshot.child("experience").getValue().toString();
                String room1 = dataSnapshot.child("room_no").getValue().toString();
                String regNo = dataSnapshot.child("doctor_reg_id").getValue().toString();
                String speciality = dataSnapshot.child("speciality").getValue().toString();
                String gender = dataSnapshot.child("gender").getValue().toString();

                if(gender.equals("F")){
                    photo.setImageResource(R.drawable.f_doctor_avatar);
                }
                user_profile_name.setText(name);
                user_profile_short_bio.setText(Qualify);
                mobile.setText(phone);
                exp_value.setText(expe);
                room.setText(room1);
                registration_id_doc.setText(regNo);
                specalize.setText(speciality);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
