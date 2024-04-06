package com.example.finalprojectandroid1.fragments.myAppointments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.ShopInfoActivity;
import com.example.finalprojectandroid1.shop.TimeRange;
import com.example.finalprojectandroid1.user.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    HashMap<String, TimeRange> shopUnavailableTime;
    HashMap<String, HashMap<TimeRange,String>> userUnavailableAppoints;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_not_owned_shop_stats, container, false);

        shopInfoActivity = (ShopInfoActivity) getActivity();

        TextView shopTags = view.findViewById(R.id.notOwnedTags);
        TextView shopDes = view.findViewById(R.id.notOwnedDes);
        TextView shopLinks = view.findViewById(R.id.notOwnedLinks);

        shopInfoActivity.setDesLinksTags(shopDes, shopLinks,shopTags);

        userUid = shopInfoActivity.getUserUid();
        shopUid = shopInfoActivity.getShop().getShopUid();
//        user = shopInfoActivity.getUser();
//        Log.d(TAG, user.toString());
        shopUnavailableTime = new HashMap<>();
        userUnavailableAppoints = new HashMap<>();
        subRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userUid).child("subscribedShops")
                .child(shopInfoActivity.getShop().getShopUid());

        subscribeBtn = view.findViewById(R.id.subscribedButton);
        Button setAppoint = view.findViewById(R.id.setAppointButton);

        checkSub();
        getUserAppoints();
        getShopUnavailableTime();

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
                Bundle toSetAppoint = new Bundle();
                toSetAppoint.putSerializable("shopUnavailableTime", shopUnavailableTime);
                toSetAppoint.putSerializable("userUnavailableAppoints", userUnavailableAppoints);
                Navigation.findNavController(view).navigate(R.id.action_notOwnedShopStats_to_setShopAppointmentStep1, toSetAppoint);
            }
        });

        return view;
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

    public void getUserAppoints(){
        try{
            FirebaseDatabase.getInstance().getReference("users").child(userUid).child("userAppointments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{
                        for(DataSnapshot appointSnap : snapshot.getChildren()){
                            HashMap<TimeRange,String> timeAndName = new HashMap<>();
                            Log.d(TAG, "user appointSnap.getValue(): " + appointSnap.getValue());


                        }

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

    }

    public void getShopUnavailableTime(){
        try{
            FirebaseDatabase.getInstance().getReference("shops").child(shopUid).child("shopAppointments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot appointSnap : snapshot.getChildren()){
                        Log.d(TAG,"shop appointSnap.getValue()" + appointSnap.getValue());
                    }
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

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            }catch(Exception e){

        }
    }
}