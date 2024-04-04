package com.example.finalprojectandroid1.fragments.myAppointments;

import android.app.Dialog;
import android.content.Intent;
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

import java.util.ArrayList;

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

    String TAG = "SetShopAppointmentStep3";
    Bundle fromStep2;
    int userAppearancesNum;
    String chosenStartTime;
    String chosenEndTime;
    ArrayList<String> appointsNameList;
    String chosenDate;
    int priceSum;
    ShopInfoActivity shopInfoActivity;
    int userUnavailableStartTime;
    String userUnavailableShopUid;
    String shopName;
    String userName;
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
        appointsNameList = fromStep2.getStringArrayList("chosenAppointName");
        priceSum = fromStep2.getInt("priceSum");

        dateAndTime.setText(chosenDate + " שעה:" + chosenStartTime);

        for(String appointName: appointsNameList){
            TextView name = new TextView(getContext());
            name.setText(appointName);

            appointmentListLayout.addView(name);
        }

        priceSumText.setText( "סה\"כ: " + " ₪" + priceSum );
//        sumPrice.setText( sumPrice + "סה\"כ: ");

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
                if(fromStep2.getBoolean("chosenTakenUserAppoint")){
                    userUnavailableStartTime = fromStep2.getInt("userUnavailableStartTime");
                    userUnavailableShopUid = fromStep2.getString("userUnavailableShopUid");
                    Dialog cancelOtherAppointDialog = new Dialog(getContext());
                    cancelOtherAppointDialog.show();
                    cancelOtherAppointDialog.setContentView(R.layout.card_cancel_appointment_dialog);

                    TextView showCancelShop = cancelOtherAppointDialog.findViewById(R.id.showCancelTextCancelAppointDialog);
                    Button confirmAppointCancellation = cancelOtherAppointDialog.findViewById(R.id.confirmCancelAppointDialog);
                    Button cancelAppointCancellation = cancelOtherAppointDialog.findViewById(R.id.cancelCancelAppointDialog);


                    DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("shops").child(userUnavailableShopUid);
                    shopRef.child("shopName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            shopName = snapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    Query findAppointInShop = shopRef.child("shopAppointments")
                            .orderByChild("date").equalTo(chosenDate);

                    findAppointInShop.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                           for(DataSnapshot shopAppointsSnap : snapshot.getChildren()){
//                               Log.d(TAG, "snapshot1 COUNT: " + shopAppointsSnap.getChildrenCount());
//                               Log.d(TAG, "snapshot1 val: " + shopAppointsSnap.getValue());
//                               Log.d(TAG,"startTime: " + shopAppointsSnap.child("time").child("startTime").getValue(Integer.class));
                               int shopStartTime = shopAppointsSnap.child("time").child("startTime").getValue(Integer.class);
//                               Log.d(TAG,"userUid: " +  shopAppointsSnap.child("userUid").getValue(String.class));
                               String shopUserUid = shopAppointsSnap.child("userUid").getValue(String.class);
//                               Log.d(TAG,"userUnavailableStartTime: " +  userUnavailableStartTime);
//                               Log.d(TAG,"shopInfoActivity.getUserUid(): " +  shopInfoActivity.getUserUid());

                               if(shopStartTime == userUnavailableStartTime && shopUserUid.equals(shopInfoActivity.getUserUid())){
                                   showCancelShop.setText("לבטל את התור בחנות " + shopName + " בשעה " + userUnavailableStartTime);
                                   confirmAppointCancellation.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                            snapshot.getRef().removeValue();
                                           Query userOldAppoint = FirebaseDatabase.getInstance().getReference("users").child(shopInfoActivity.getUserUid())
                                                   .child("userAppointments").orderByChild("date").equalTo(chosenDate);

                                           userOldAppoint.addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                   for (DataSnapshot userAppointSnap : snapshot.getChildren()){
                                                       Log.d(TAG, "userAppointSnap COUNT: " + userAppointSnap.getChildrenCount());
                                                       Log.d(TAG, "userAppointSnap val: " + userAppointSnap.getValue());
                                                       int userStartTime = userAppointSnap.child("time").child("startTime").getValue(Integer.class);
                                                       if(userStartTime == userUnavailableStartTime ){
                                                           snapshot.getRef().removeValue();
                                                           setTheAppointInDatabase();

                                                           Intent i = new Intent(shopInfoActivity, MainActivity.class);
                                                           i.putExtra("userUid", shopInfoActivity.getUserUid());
                                                           startActivity(i);

                                                       }
                                                   }
                                               }

                                               @Override
                                               public void onCancelled(@NonNull DatabaseError error) {

                                               }
                                           });

                                       }
                                   });
                               }


                           }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    cancelAppointCancellation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelOtherAppointDialog.dismiss();
                        }
                    });



                }else{
                    setTheAppointInDatabase();

                    Intent i = new Intent(shopInfoActivity, MainActivity.class);
                    i.putExtra("userUid", shopInfoActivity.getUserUid());
                    startActivity(i);

                }


            }
        });




        return view;
    }

    public void setTheAppointInDatabase(){
        String shopUid = shopInfoActivity.getShop().getShopUid();
        String userUid = shopInfoActivity.getUserUid();

        DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("shops");

        shopRef.child(shopUid).child("usersAppearances").child(userUid).setValue(ServerValue.increment(1));


        shopRef.child(shopUid).child("usersAppearances").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAppearancesNum = snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        int startTime = Integer.parseInt(chosenStartTime.replace(":",""));
        int endTime = Integer.parseInt(chosenEndTime.replace(":",""));

        TimeRange time = new TimeRange(startTime,endTime);


        FirebaseDatabase.getInstance().getReference("users").child(userUid).child("userName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AppointmentModel appointmentForShop = new AppointmentModel(userUid,userName,time, chosenDate,appointsNameList);
        AppointmentModel appointmentForUser = new AppointmentModel(shopInfoActivity.getShop().getShopName(),
                shopInfoActivity.getShop().getShopAddress().presentAddress(),shopInfoActivity.getShop().getShopUid(),
                time,chosenDate,appointsNameList,String.valueOf(priceSum));



        FirebaseDatabase.getInstance().getReference("shops").child(shopUid).
                child("shopAppointments").push().setValue(appointmentForShop);

        FirebaseDatabase.getInstance().getReference("users").child(userUid).
                child("userAppointments").push().setValue(appointmentForUser);
    }

    public void goBack(View v){
        Navigation.findNavController(v).navigate(R.id.action_setShopAppointmentStep3_to_setShopAppointmentStep2, fromStep2);
    }
}