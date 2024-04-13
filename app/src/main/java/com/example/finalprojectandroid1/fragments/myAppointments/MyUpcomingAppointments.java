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
import android.widget.Toast;

import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.MainActivity;
import com.example.finalprojectandroid1.appointment.AppointmentAdapter;
import com.example.finalprojectandroid1.appointment.AppointmentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

    // Shows the user appointments as a customer
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

        // Fetching the user appointments from the database
        DatabaseReference userAppoints = FirebaseDatabase.getInstance().getReference("users").child(userUid).child("userAppointments");

        userAppoints.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for(DataSnapshot dateSnap: snapshot.getChildren()){
                    // Checking to see if the appointment time has passed and deleting
                    // and deleting it if so
                    int dateNum = Integer.parseInt(dateSnap.getKey());
                    for (DataSnapshot appointSnap: dateSnap.getChildren()){
                        try {
                            if (dateNum < GlobalMembers.todayDate() ||
                                    (dateNum == GlobalMembers.todayDate() && Integer.parseInt(appointSnap.child("time").child("startTime").getValue(String.class)) <= GlobalMembers.timeRightNowInt())) {
                                FirebaseDatabase.getInstance().getReference("shops").child(appointSnap.child("shopUid").getValue(String.class)).child("shopAppointments").
                                        child(dateSnap.getKey()).child(appointSnap.getKey()).removeValue();
                                appointSnap.getRef().removeValue();
                                continue;
//
                            }

                            AppointmentModel newAppoint = appointSnap.getValue(AppointmentModel.class);
                            myAppointmentsList.add(newAppoint);

                            count++;
                            if (count == snapshot.getChildrenCount()) {

                                // Showing the appointments in the recycler view
                                myAppointmentsAdapter = new AppointmentAdapter(myAppointmentsList, mainActivity, 0, false, userUid);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                appointmentsRes.setLayoutManager(layoutManager);

                                appointmentsRes.setAdapter(myAppointmentsAdapter);
                                mainActivity.setMyAppointmentsList(myAppointmentsList);

                            }
                        }catch(Exception e){
                            Log.e(TAG, "Error fetching the user appointments: " + e.getMessage());
                            Toast.makeText(mainActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
                        }

                    }
//
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error fetching user appointments: " + error.getMessage());
                Toast.makeText(mainActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }



}