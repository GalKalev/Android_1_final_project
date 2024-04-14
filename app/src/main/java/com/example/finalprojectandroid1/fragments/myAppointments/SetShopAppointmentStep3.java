package com.example.finalprojectandroid1.fragments.myAppointments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.MainActivity;
import com.example.finalprojectandroid1.activities.ShopInfoActivity;
import com.example.finalprojectandroid1.appointment.AppointmentModel;
import com.example.finalprojectandroid1.shop.TimeRange;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetShopAppointmentStep3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetShopAppointmentStep3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SetShopAppointmentStep3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetShopAppointmentStep3.
     */
    // TODO: Rename and change types and number of parameters
    public static SetShopAppointmentStep3 newInstance(String param1, String param2) {
        SetShopAppointmentStep3 fragment = new SetShopAppointmentStep3();
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


    // Final step to confirm the appointment date and time

    String TAG = "SetShopAppointmentStep3";
    Bundle fromStep2;

    String chosenStartTime;
    String chosenEndTime;
    ArrayList<String> chosenAppointsName;
    String chosenDate;
    int priceSum;
    ShopInfoActivity shopInfoActivity;
    int userUnavailableStartTime;
    String userUnavailableShopUid;
    String shopName;
    String userName;
    String savedDateText;
    int dateKeyNum;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_shop_appointment_step3, container, false);
        TextView dateAndTime = view.findViewById(R.id.dateAndTimeStep3);
        TextView priceSumText = view.findViewById(R.id.sumPriceStep3);
        LinearLayout appointmentListLayout = view.findViewById(R.id.appointsListLayoutStep3);
        Button backBtn = view.findViewById(R.id.backButtonStep3);
        Button confirmAppointBtn = view.findViewById(R.id.confirmAppointmentStep3);

        shopInfoActivity = (ShopInfoActivity)getActivity();

        fromStep2 = getArguments();
        chosenDate = fromStep2.getString("chosenDate");
        chosenStartTime = fromStep2.getString("chosenStartTime");
        chosenEndTime = fromStep2.getString("chosenEndTime");
        chosenAppointsName = fromStep2.getStringArrayList("chosenAppointsName");
        priceSum = fromStep2.getInt("priceSum");

        // Presenting the chosen appointment summery
        dateAndTime.setText(GlobalMembers.convertDateFromCompareToShow(chosenDate) + " שעה:" + chosenStartTime);

        for(String appointName: chosenAppointsName){
            TextView name = new TextView(getContext());
            name.setText(appointName);
            name.setTextColor(Color.BLACK);
            name.setTextSize(18);
            appointmentListLayout.addView(name);
        }
        priceSumText.setText( "סה\"כ: " + " ₪" + priceSum );
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goBack(view);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "back pressed");
                goBack(view);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        confirmAppointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If the costumer chooses to cancel a different appoinmetnt
                // the method will cancel the other appointment
                if(fromStep2.getBoolean("chosenTakenUserAppoint")){
                    Dialog cancelOtherAppointDialog = new Dialog(getContext());
                    cancelOtherAppointDialog.setContentView(R.layout.card_cancel_confirmation_dialog);


                    TextView showCancelShop = cancelOtherAppointDialog.findViewById(R.id.showCancelTextCancelConfirmDialog);
                    Button confirmAppointCancellation = cancelOtherAppointDialog.findViewById(R.id.confirmCancellationButtonDialog);
                    Button cancelAppointCancellation = cancelOtherAppointDialog.findViewById(R.id.backButtonCancelDialog);
                    userUnavailableStartTime = fromStep2.getInt("userUnavailableStartTime");
                    userUnavailableShopUid = fromStep2.getString("userUnavailableShopUid");


                    DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("shops").child(userUnavailableShopUid);
                    shopRef.child("shopInfo").child("shopName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            shopName = snapshot.getValue(String.class);

                            Query findAppointInShop = shopRef.child("shopAppointments").orderByKey().equalTo(chosenDate);

                            findAppointInShop.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Log.d(TAG,"snapshot.getKey(): " + snapshot.getKey());
                                    for(DataSnapshot shopAppointsSnap : snapshot.getChildren()){
                                        for(DataSnapshot shopDateAppointsSnap : shopAppointsSnap.getChildren()){
                                            int shopStartTime = Integer.parseInt(shopDateAppointsSnap.child("time").child("startTime").getValue(String.class));
                                            String shopUserUid = shopDateAppointsSnap.child("userUid").getValue(String.class);
                                            if(shopStartTime == userUnavailableStartTime && shopUserUid.equals(shopInfoActivity.getUserUid())){
                                                String userUnavailableStartTimeStr = String.valueOf(userUnavailableStartTime).substring(0,2) + ":" + String.valueOf(userUnavailableStartTime).substring(2);
                                                showCancelShop.setText("לבטל את התור בחנות " + shopName + " בשעה " + userUnavailableStartTimeStr + "?");
                                                cancelOtherAppointDialog.show();
                                                confirmAppointCancellation.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        snapshot.getRef().removeValue();
                                                        Query userOldAppoint = FirebaseDatabase.getInstance().getReference("users").child(shopInfoActivity.getUserUid())
                                                                .child("userAppointments").orderByKey().equalTo(chosenDate);

                                                        userOldAppoint.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                for (DataSnapshot userAppointSnap : snapshot.getChildren()){
                                                                    for(DataSnapshot userDateAppointSnap : userAppointSnap.getChildren()){
                                                                        int userStartTime = Integer.parseInt(userDateAppointSnap.child("time").child("startTime").getValue(String.class));
                                                                        if(userStartTime == userUnavailableStartTime ){
                                                                            snapshot.getRef().removeValue();
                                                                            setTheAppointInDatabase();

                                                                        }
                                                                    }

                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                                Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                    }
                                                });
                                            }
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e(TAG, error.getMessage());
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    try {

                        cancelAppointCancellation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelOtherAppointDialog.dismiss();
                            }
                        });



                    }catch(Exception e){
                        Log.e(TAG, e.getMessage());
                    }


                }else{
                    setTheAppointInDatabase();

                }


            }
        });


        return view;
    }

    // Setting the new appointment in the database
    public void setTheAppointInDatabase(){
        String shopUid = shopInfoActivity.getShop().getShopUid();
        String userUid = shopInfoActivity.getUserUid();

        DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("shops");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        SimpleDateFormat sdfOriginalFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfTargetFormat = new SimpleDateFormat("dd/MM/yyyy");

        dateKeyNum = Integer.valueOf(chosenDate);
        try{
            Date dateKey = sdfOriginalFormat.parse(chosenDate);
            savedDateText = sdfTargetFormat.format(dateKey);
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }

        TimeRange time = new TimeRange(chosenStartTime.replace(":",""),chosenEndTime.replace(":",""));

        try{
            userRef.child(userUid).child("userAuth").child("userName").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userName = snapshot.getValue(String.class);
                    Log.d(TAG,"userName: " + userName);
                    AppointmentModel appointmentForShop = new AppointmentModel(userUid,userName,time, savedDateText, chosenAppointsName);
                    AppointmentModel appointmentForUser = new AppointmentModel(shopInfoActivity.getShop().getShopName(),
                            shopInfoActivity.getShop().getShopAddress().presentAddress(),shopInfoActivity.getShop().getShopUid(),
                            time,savedDateText, chosenAppointsName,String.valueOf(priceSum));
                    try{
                        FirebaseDatabase.getInstance().getReference("shops").child(shopUid)
                               .child("shopAppointments").child(chosenDate).child(chosenStartTime).setValue(appointmentForShop);
                        FirebaseDatabase.getInstance().getReference("users").child(userUid).
                                child("userAppointments").child(chosenDate).child(chosenStartTime).setValue(appointmentForUser);
                    }catch(Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Checking to see the user process wad to change an appointment
                    if(fromStep2.getBoolean("isAppointChange")){

                        String dateToChange = fromStep2.getString("appointChangeDate");
                        String StartTimeToChange = fromStep2.getString("appointChangeStartTime");
                        Log.d(TAG, "dateToChange: " + dateToChange + " StartTimeToChange: " + StartTimeToChange);

                        dateToChange = GlobalMembers.convertDateFromShowToCompare(dateToChange);
                        StartTimeToChange = StartTimeToChange.substring(0,2) + ":" + StartTimeToChange.substring(2);

                        userRef.child(userUid).child("userAppointments").child(dateToChange).child(StartTimeToChange).removeValue();
                        shopRef.child(shopUid).child("shopAppointments").child(dateToChange).child(StartTimeToChange).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().removeValue();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
                            }
                        });



                    }

                    Intent i = new Intent(shopInfoActivity, MainActivity.class);
                    i.putExtra("userUid", shopInfoActivity.getUserUid());
                    i.putExtra("user", shopInfoActivity.getUser());
                    startActivity(i);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: " + error.getMessage());
                }
            });
        }catch (Exception e){
            Log.e(TAG, "Error setting data: " + e.getMessage());
        }







    }

    public void goBack(View v){
        Bundle backToStep2 = new Bundle();
        backToStep2.putString("chosenDate",chosenDate);
        backToStep2.putString("chosenStartTime",chosenStartTime);
        backToStep2.putString("chosenEndTime",chosenEndTime);
        backToStep2.putStringArrayList("chosenAppointsName",chosenAppointsName);
        backToStep2.putInt("priceSum",priceSum);
        backToStep2.putInt("timeSum",fromStep2.getInt("timeSum"));
        backToStep2.putSerializable("userUnavailableAppoints",getArguments().getSerializable("userUnavailableAppoints"));
        backToStep2.putSerializable("shopBlockedDates",getArguments().getSerializable("shopBlockedDates"));
        backToStep2.putSerializable("shopUnavailableAppointments",getArguments().getSerializable("shopUnavailableAppointments"));
        backToStep2.putBoolean("chosenTakenUserAppoint",fromStep2.getBoolean("chosenTakenUserAppoint"));
        backToStep2.putInt("userUnavailableStartTime",userUnavailableStartTime);
        backToStep2.putString("userUnavailableShopUid",userUnavailableShopUid);
        backToStep2.putBoolean("isAppointChange",fromStep2.getBoolean("isAppointChange"));
        backToStep2.putString("appointChangeDate",fromStep2.getString("appointChangeDate"));
        backToStep2.putString("appointChangeStartTime",fromStep2.getString("appointChangeStartTime"));

        Navigation.findNavController(v).navigate(R.id.action_setShopAppointmentStep3_to_setShopAppointmentStep2, backToStep2);
    }
}