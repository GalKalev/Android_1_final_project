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
import android.widget.Toast;

import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.ShopInfoActivity;
import com.example.finalprojectandroid1.appointment.AppointmentAdapter;
import com.example.finalprojectandroid1.appointment.AppointmentModel;
import com.example.finalprojectandroid1.shop.ShopModel;
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

    // Customers can see the shop info here, can add to subscription and
    // setting an appointment

    String TAG = "NotOwnedShopStats";
    String userUid;
    String shopUid;
    UserInfo user;
    Button subscribeBtn;
    ShopInfoActivity shopInfoActivity;
    boolean isSub;

    DatabaseReference subRef ;

    ArrayList<String[]> shopBlockedDates;
    HashMap<String, ArrayList<String[]>> shopUnavailableAppointments;
//    ArrayList<int[]> shopBlockedTime;
    HashMap<String, ArrayList<String[]>> userUnavailableAppoints;

    ArrayList<AppointmentModel> myAppointmentsList;
    RecyclerView closestAppointInShopRes;
    Bundle fromShopActivity;

    int fetchingCounter = 0;

    LinearLayout loadingLayout;
    TextView noAppointSetText;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_not_owned_shop_stats, container, false);

        shopInfoActivity = (ShopInfoActivity) getActivity();

        fromShopActivity = getArguments();

        TextView shopTags = view.findViewById(R.id.notOwnedTags);
        TextView shopDes = view.findViewById(R.id.notOwnedDes);
        noAppointSetText = view.findViewById(R.id.noAppointSetText);
        loadingLayout = view.findViewById(R.id.loadingBarLayoutNotOwnedStats);

        // Showing the customer the closest appointment for this shop, it there is one
        closestAppointInShopRes = view.findViewById(R.id.closestAppointOfUserInShopRes);
        LinearLayout linksLayout = view.findViewById(R.id.linksButtonsLayoutNotOwned);


        shopInfoActivity.setDesLinksTags(shopDes, linksLayout,shopTags);

        userUid = shopInfoActivity.getUserUid();
        shopUid = shopInfoActivity.getShop().getShopUid();

        shopUnavailableAppointments = new HashMap<>();
        myAppointmentsList = new ArrayList<>();
        userUnavailableAppoints = new HashMap<>();
        shopBlockedDates = new ArrayList<>();
        subRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userUid).child("subscribedShops")
                .child(shopInfoActivity.getShop().getShopUid());

        subscribeBtn = view.findViewById(R.id.subscribedButton);
        Button setAppoint = view.findViewById(R.id.setAppointButton);

        checkSub();
        getUnavailableTimes();

        // Adding shop to subscription
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

        // Starting the steps for setting an appointment
        setAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToSteps();
            }
        });

        return view;
    }

    private void setToSteps(){
        Bundle toSetAppoint = new Bundle();
        toSetAppoint.putSerializable("shopUnavailableAppointments", shopUnavailableAppointments);
        toSetAppoint.putSerializable("shopBlockedDates", shopBlockedDates);
        toSetAppoint.putSerializable("userUnavailableAppoints", userUnavailableAppoints);
        toSetAppoint.putBoolean("isAppointChange",fromShopActivity.getBoolean("isAppointChange"));
        toSetAppoint.putString("appointChangeDate",fromShopActivity.getString("appointChangeDate"));
        toSetAppoint.putString("appointChangeStartTime",fromShopActivity.getString("appointChangeStartTime"));

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
                    subscribeBtn.setText("הוספה למועדפים");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fetching the user appointments, shop unavailable time (set appointments and blocked dates)
    public void getUnavailableTimes(){

        try{


            FirebaseDatabase.getInstance().getReference("users").child(userUid).child("userAppointments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{
                        boolean isClosestAppointSelected = false;
                        for(DataSnapshot appointSnap : snapshot.getChildren()){
                            String date = appointSnap.getKey();
                            ArrayList<String[]> timeAndShopUid = new ArrayList<>();
                            for(DataSnapshot appointValsSnap : appointSnap.getChildren()){
                                TimeRange time  = appointValsSnap.child("time").getValue(TimeRange.class);
                                String startTime = time.getStartTime();
                                String endTime = time.getEndTime();
                                String shopUid = appointValsSnap.child("shopUid").getValue(String.class);
                                if(!isClosestAppointSelected && shopUid.equals(shopInfoActivity.getShop().getShopUid()) ){
                                    myAppointmentsList.add(appointValsSnap.getValue(AppointmentModel.class));
                                    isClosestAppointSelected = true;
                                }
                                String[] dateAndTime = {startTime, endTime,shopUid};
                                timeAndShopUid.add(dateAndTime);

                            }
                            userUnavailableAppoints.put(date,timeAndShopUid);

                        }

                        if(isClosestAppointSelected){
                            noAppointSetText.setVisibility(View.GONE);
                        }

                        AppointmentAdapter closestAppointAdapter = new AppointmentAdapter(myAppointmentsList,shopInfoActivity,1,false,userUid);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        closestAppointInShopRes.setLayoutManager(layoutManager);
                        closestAppointInShopRes.setAdapter(closestAppointAdapter);

                        // Counter check if all the the unavailable times for both user and shop
                        // has finished
                        fetchingCounter++;
                        checkFetchingCounter();


                    }catch(Exception e){
                        Log.e(TAG,"getUserAppoints onDataChange: " + e.getMessage());
                        Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "error fetching userUnavailableAppoints: " + error.getMessage());
                    Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Log.e(TAG, "error fetching userUnavailableAppoints: " + e.getMessage());
            Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
        }

        try{

            FirebaseDatabase.getInstance().getReference("shops").child(shopUid).child("shopInfo").child("shopAppointments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot appointSnap : snapshot.getChildren()){
                        String date = appointSnap.getKey();
                        ArrayList<String[]> takenTime = new ArrayList<>();
                        for(DataSnapshot appointValsSnap : appointSnap.getChildren()){
                            TimeRange time  = appointValsSnap.child("time").getValue(TimeRange.class);
                            String startTime = time.getStartTime();
                            String endTime = time.getEndTime();
                            String[] dateAndTime = {startTime, endTime};
                            takenTime.add(dateAndTime);
                        }
                        shopUnavailableAppointments.put(date,takenTime);

                    }
                    // Counter check if all the the unavailable times for both user and shop
                    // has finished
                    fetchingCounter++;
                    checkFetchingCounter();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "error fetching shopUnavailableAppointments appoints: " + error.getMessage());
                    Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();

                }
            });


        }catch (Exception e){
            Log.e(TAG, "error fetching shopUnavailableAppointments appoints: " + e.getMessage());
            Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
        }


        try {
            FirebaseDatabase.getInstance().getReference("shops").child(shopUid).child("blockedDates").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG,"block snapshot.getKey(): " + snapshot.getKey());


                    for (DataSnapshot appointSnap : snapshot.getChildren()){
                        String dateStart = String.valueOf(appointSnap.getKey());
                        String dateEnd = String.valueOf(appointSnap.child("endDate").getValue(Integer.class));
                        if(Integer.parseInt(dateEnd) >= GlobalMembers.todayDate()){
                            TimeRange time  = appointSnap.child("time").getValue(TimeRange.class);

                            String startTime = time.getStartTime();
                            String endTime = time.getEndTime();


                            String[] dateAndTime = {dateStart,dateEnd, startTime, endTime};
                            shopBlockedDates.add(dateAndTime);
                        }

                    }
                    // Counter check if all the the unavailable times for both user and shop
                    // has finished
                    fetchingCounter++;
                    checkFetchingCounter();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "error fetching user taken appointments: " + error.getMessage());
                    Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();

                }
            });

            }catch(Exception e){
            Log.e(TAG, "error fetching user taken appointments: " + e.getMessage());
            Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
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