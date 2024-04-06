package com.example.finalprojectandroid1.fragments.myAppointments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.MainActivity;
import com.example.finalprojectandroid1.appointment.AppointmentAdapter;
import com.example.finalprojectandroid1.appointment.AppointmentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyUpcomingAppointments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyUpcomingAppointments extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyUpcomingAppointments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalCalendar.
     */
    // TODO: Rename and change types and number of parameters
    public static MyUpcomingAppointments newInstance(String param1, String param2) {
        MyUpcomingAppointments fragment = new MyUpcomingAppointments();
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

    String TAG = "MyUpcomingAppointments";

    ArrayList<AppointmentModel> myAppointmentsList;
    AppointmentAdapter myAppointmentsAdapter;
    String userUid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_upcoming_appointments, container, false);

        MainActivity mainActivity = (MainActivity)getActivity();
        userUid = mainActivity.getUserUid();


        RecyclerView appointmentsRes = view.findViewById(R.id.myAppointmetsRes);
        myAppointmentsList = new ArrayList<>();
        try{
            DatabaseReference userAppoints = FirebaseDatabase.getInstance().getReference("users").child(userUid).child("userAppointments");

            userAppoints.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count = 0;
                    for(DataSnapshot dateSnap: snapshot.getChildren()){
                        Log.d(TAG, "setMyAppointmentsList dateSnap count: " + dateSnap.getChildrenCount());
                        Log.d(TAG, "setMyAppointmentsList dateSnap value: " + dateSnap.getValue());
                        for (DataSnapshot appointSnap: dateSnap.getChildren()){
                            Log.d(TAG, "setMyAppointmentsList appointSnap count: " + appointSnap.getChildrenCount());
                            Log.d(TAG, "setMyAppointmentsList appointSnap value: " + appointSnap.getValue());
                            AppointmentModel newAppoint = appointSnap.getValue(AppointmentModel.class);
                            myAppointmentsList.add(newAppoint);
                            count++;
                            if(count == snapshot.getChildrenCount()){
//                                Collections.sort(myAppointmentsList, new Comparator<AppointmentModel>() {
//                                    @Override
//                                    public int compare(AppointmentModel o1, AppointmentModel o2) {
//                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
//                                        Date date1, date2;
//                                        try{
//                                            date1 = simpleDateFormat.parse(o1.getDate());
//                                            date2 = simpleDateFormat.parse(o2.getDate());
//                                        }catch(Exception e){
//                                            Log.e(TAG,"Error comparing dates: " + e.getMessage());
//                                            return 0;
//                                        }
//                                        int dateCompare = date1.compareTo(date2);
//                                        if(dateCompare != 0){
//                                            return dateCompare;
//                                        }else{
//                                            return Integer.compare(o1.getTime().getStartTime(), o2.getTime().getStartTime());
//                                        }
//                                    }
//                                });
                                myAppointmentsAdapter = new AppointmentAdapter(myAppointmentsList,mainActivity,false, userUid);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                appointmentsRes.setLayoutManager(layoutManager);


                                appointmentsRes.setAdapter(myAppointmentsAdapter);

                            }
                        }
//
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch(Exception e){
            Log.e(TAG, "Error fetching user appointments: " + e.getMessage());
        }

//        Log.d(TAG, "myAppointmentsList size: " + myAppointmentsList.size());






        return view;
    }

    public void setMyAppointmentsList() {

    }

}