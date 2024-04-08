package com.example.finalprojectandroid1.fragments.myAppointments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.ShopInfoActivity;
import com.example.finalprojectandroid1.appointment.AppointmentAdapter;
import com.example.finalprojectandroid1.appointment.AppointmentModel;
import com.example.finalprojectandroid1.shop.TimeRange;
import com.example.finalprojectandroid1.user.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotOwnedShopStats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotOwnedShopStats extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotOwnedShopStats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotOwnedShopStats.
     */
    // TODO: Rename and change types and number of parameters
    public static NotOwnedShopStats newInstance(String param1, String param2) {
        NotOwnedShopStats fragment = new NotOwnedShopStats();
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

    String TAG = "NotOwnedShopStats";
    String userUid;
    String shopUid;
    UserInfo user;
    Button subscribeBtn;
    ShopInfoActivity shopInfoActivity;
    boolean isSub;

    DatabaseReference subRef ;

    ArrayList<String[]> shopUnavailableTime;
//    ArrayList<int[]> shopBlockedTime;
    HashMap<String[], String> userUnavailableAppoints;

    ArrayList<AppointmentModel> myAppointmentsList;
    RecyclerView closestAppointInShopRes;
    Bundle fromShopActivity;

    int fetchingCounter = 0;

    LinearLayout loadingLayout;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_not_owned_shop_stats, container, false);

        shopInfoActivity = (ShopInfoActivity) getActivity();

        fromShopActivity = getArguments();

        TextView shopTags = view.findViewById(R.id.notOwnedTags);
        TextView shopDes = view.findViewById(R.id.notOwnedDes);
        TextView shopLinks = view.findViewById(R.id.notOwnedLinks);
        loadingLayout = view.findViewById(R.id.loadingBarLayoutNotOwnedStats);
//        myAppointmentsList = shopInfoActivity.getMyAppointmentsList();

//        for(AppointmentModel appointmentModel : myAppointmentsList){
//            Log.d(TAG, "appoint date : " + appointmentModel.getDate());
//            Log.d(TAG, "appoint time : " + appointmentModel.getTime().getStartTime());
//            Log.d(TAG, "appoint shopUid : " + appointmentModel.getShopUid());
//        }

        closestAppointInShopRes = view.findViewById(R.id.closestAppointOfUserInShopRes);


        shopInfoActivity.setDesLinksTags(shopDes, shopLinks,shopTags);

        userUid = shopInfoActivity.getUserUid();
        shopUid = shopInfoActivity.getShop().getShopUid();
//        user = shopInfoActivity.getUser();
//        Log.d(TAG, user.toString());
        shopUnavailableTime = new ArrayList<>();
        myAppointmentsList = new ArrayList<>();
        userUnavailableAppoints = new HashMap<>();
        subRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userUid).child("subscribedShops")
                .child(shopInfoActivity.getShop().getShopUid());

        subscribeBtn = view.findViewById(R.id.subscribedButton);
        Button setAppoint = view.findViewById(R.id.setAppointButton);

        checkSub();
//        getUserAppoints();
        getUnavailableTimes();



        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(isSub){
                  subRef.removeValue();
                  subscribeBtn.setText("הוספה למועדפים");
              }else{
                  subRef.setValue(true);
                  subscribeBtn.setText("במועדפים");
              }
              checkSub();
            }
        });

        setAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle toSetAppoint = new Bundle();
//                toSetAppoint.putSerializable("shopUnavailableTime", shopUnavailableTime);
//                toSetAppoint.putSerializable("userUnavailableAppoints", userUnavailableAppoints);
//                Navigation.findNavController(view).navigate(R.id.action_notOwnedShopStats_to_setShopAppointmentStep1, toSetAppoint);
                setToSteps();
            }
        });

        return view;
    }

    private void setToSteps(){
        Bundle toSetAppoint = new Bundle();
        toSetAppoint.putSerializable("shopUnavailableTime", shopUnavailableTime);
        toSetAppoint.putSerializable("userUnavailableAppoints", userUnavailableAppoints);
        toSetAppoint.putBoolean("isAppointChange",fromShopActivity.getBoolean("isAppointChange"));
        toSetAppoint.putString("appointChangeDate",fromShopActivity.getString("appointChangeDate"));
        toSetAppoint.putString("appointChangeStartTime",fromShopActivity.getString("appointChangeStartTime"));
//        toSetAppoint.putInt("timeToChange",);

        Navigation.findNavController(getView()).navigate(R.id.action_notOwnedShopStats_to_setShopAppointmentStep1, toSetAppoint);
    }

    private void checkSub() {
        subRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isSub = snapshot.exists();
                if(isSub){
                    subscribeBtn.setText("במועדפים");
                }else{
                    subscribeBtn.setText("הוספה במועדפים");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getUnavailableTimes(){

        try{


            FirebaseDatabase.getInstance().getReference("users").child(userUid).child("userAppointments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{
                        boolean firstIn = false;
                        Log.d(TAG,"user snapshot.getKey(): " + snapshot.getKey());
                        for(DataSnapshot appointSnap : snapshot.getChildren()){
//                            Log.d(TAG, "user appointSnap.getkey(): " + appointSnap.getKey());
//                            Log.d(TAG, "user appointSnap.getValue(): " + appointSnap.getValue());
                            String date = appointSnap.getKey();
                            for(DataSnapshot appointValsSnap : appointSnap.getChildren()){
                                if(!firstIn && appointValsSnap.child("shopUid").getValue(String.class).equals(shopUid)  )
                                {
                                    myAppointmentsList.add(appointValsSnap.getValue(AppointmentModel.class));
                                    firstIn = true;
                                }

                                TimeRange time  = appointValsSnap.child("time").getValue(TimeRange.class);
                                String startTime = String.valueOf(time.getStartTime());
                                String endTime = time.getEndTime();
                                String[] dateAndTime = {date, startTime, endTime};
                                String shopUid = appointValsSnap.child("shopUid").getValue(String.class);
                                userUnavailableAppoints.put(dateAndTime,shopUid);

                            }

                        }

                        AppointmentAdapter closestAppointAdapter = new AppointmentAdapter(myAppointmentsList,shopInfoActivity,1,false,userUid);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        closestAppointInShopRes.setLayoutManager(layoutManager);
                        closestAppointInShopRes.setAdapter(closestAppointAdapter);

                        fetchingCounter++;
                        checkFetchingCounter();


                    }catch(Exception e){
                        Log.e(TAG,"getUserAppoints onDataChange: " + e.getMessage());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){

        }

        try{

            FirebaseDatabase.getInstance().getReference("shops").child(shopUid).child("shopAppointments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG,"shop snapshot.getKey(): " + snapshot.getKey());

                    for(DataSnapshot appointSnap : snapshot.getChildren()){
//                        Log.d(TAG,"shop appointSnap.getkey(): " + appointSnap.getKey());
//                        Log.d(TAG,"shop appointSnap.getValue(): " + appointSnap.getValue());
                        String date = appointSnap.getKey();
                        for(DataSnapshot appointValsSnap : appointSnap.getChildren()){
                            TimeRange time  = appointValsSnap.child("time").getValue(TimeRange.class);
                            String startTime = time.getStartTime();
                            String endTime = time.getEndTime();
                            String[] dateAndTime = {date, startTime, endTime};
                            shopUnavailableTime.add(dateAndTime);
                        }

                    }
                    fetchingCounter++;
                    checkFetchingCounter();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }catch (Exception e){

        }


        try {
            FirebaseDatabase.getInstance().getReference("shops").child(shopUid).child("blockedDates").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG,"block snapshot.getKey(): " + snapshot.getKey());


                    for (DataSnapshot appointSnap : snapshot.getChildren()){
//                        Log.d(TAG,"block shop appointSnap.getkey(): " + appointSnap.getKey());
//                        Log.d(TAG,"block shop appointSnap.getValue(): " + appointSnap.getValue());
                        String dateStart = appointSnap.getKey();

                        TimeRange time  = appointSnap.child("time").getValue(TimeRange.class);

                        String startTime = time.getStartTime();
                        String endTime = time.getEndTime();
                        String dateEnd = appointSnap.child("endDate").getValue(String.class);

                        String[] dateAndTime = {dateStart,dateEnd, startTime, endTime};
                        shopUnavailableTime.add(dateAndTime);
//                        for(DataSnapshot appointValsSnap : appointSnap.getChildren()){
//
//                        }
                    }
                    fetchingCounter++;
                    checkFetchingCounter();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            }catch(Exception e){

        }
    }

    private void checkFetchingCounter(){
        if(fetchingCounter == 3){
            if(fromShopActivity.getBoolean("isAppointChange")){
                setToSteps();
            }
            loadingLayout.setVisibility(View.GONE);
        }
    }
}