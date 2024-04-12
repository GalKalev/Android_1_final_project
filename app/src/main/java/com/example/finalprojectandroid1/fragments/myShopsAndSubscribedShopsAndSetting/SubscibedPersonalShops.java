package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.MainActivity;
import com.example.finalprojectandroid1.shop.ShopAdapter;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscibedPersonalShops#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscibedPersonalShops extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubscibedPersonalShops() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscibedPesonalShops.
     */
    // TODO: Rename and change types and number of parameters
    public static SubscibedPersonalShops newInstance(String param1, String param2) {
        SubscibedPersonalShops fragment = new SubscibedPersonalShops();
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

    private String TAG = "SubscibedPesonalShops";
    ArrayList<ShopModel> subShopList;
    ShopAdapter subShopAdapter;
    String userUid;
    MainActivity mainActivity;
    RecyclerView subscribedShopsRes;
    String uid = null;
    ProgressBar progressBar;
    TextView noSubs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_subscibed_personal_shops, container, false);

        Log.d(TAG, "inside");
        mainActivity = (MainActivity) getActivity();
        subShopList = new ArrayList<>();
        progressBar = view.findViewById(R.id.progressBarSub);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refreshLayout);

        userUid = mainActivity.getUserUid();
        noSubs = view.findViewById(R.id.noSubsText);

//        mainActivity.setSubShopList();
//        subShopList = mainActivity.getSubShopList();
        subscribedShopsRes = view.findViewById(R.id.resSubscribedShops);
//        subShopAdapter = mainActivity.getSubShopAdapter();

        subShopAdapter = new ShopAdapter(mainActivity,subShopList,mainActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        subscribedShopsRes.setLayoutManager(linearLayoutManager);
        subscribedShopsRes.setItemAnimator(new DefaultItemAnimator());
        subscribedShopsRes.setAdapter(subShopAdapter);

        setSubShopList();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                subShopList.clear();
                subShopAdapter.notifyDataSetChanged();
                setSubShopList();


                swipeRefreshLayout.setRefreshing(false);
            }
        });



        Log.d(TAG, "subShopList.size(): " + subShopList.size() );
//
//        database = FirebaseDatabase.getInstance();
//
//        myRef = database.getReference("users").child(uid).child("SubscribedShops");
//
//        dataset = new ArrayList<>();
//        adapter = new ShopAdapter(dataset, uid);
//        subscribedShopsRes.setAdapter(adapter);



        return view;
    }

    public void setSubShopList() {
//        subShopList.clear();
        progressBar.setVisibility(View.VISIBLE);
        try{
            DatabaseReference subShopsRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userUid).child("subscribedShops");
            subShopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(!snapshot.exists()){
                        noSubs.setVisibility(View.VISIBLE);


                    }else{

                        noSubs.setVisibility(View.GONE);
                        ArrayList<String> subKeyList = new ArrayList<>();
                        for(DataSnapshot shopSnapshot : snapshot.getChildren()){
                            String shopKey = shopSnapshot.getKey();
                            subKeyList.add(shopKey);
                        }

                        Log.d(TAG, "subKeyList size: " + subKeyList.size());

                        DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("shops");
                        for(String shopKey : subKeyList){

                            shopRef.child(shopKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                //                            int count = 0;
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

//                                for(DataSnapshot shopSnap : snapshot.getChildren()){
//
//                                    Log.d(TAG, "shopSnap count: " + shopSnap.getChildrenCount());
//                                    Log.d(TAG, "shopSnap: " + shopSnap.getValue());
//                                }
                                    Log.d(TAG, "snapshot: " + snapshot.getValue());
                                    subShopList.add(snapshot.getValue(ShopModel.class));
                                    noSubs.setVisibility(View.GONE);
//                                 count++;
//                                Log.d(TAG, "count: " + count);
                                    if(subShopList.size() == subKeyList.size()){
                                        Log.d(TAG, "subShopList.size(): " + subShopList.size());
                                        subShopAdapter.notifyDataSetChanged();
//                                     Log.d(TAG, "count: " + count);
//                                        subShopAdapter = new ShopAdapter(mainActivity,subShopList,mainActivity);
//                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                                        subscribedShopsRes.setLayoutManager(linearLayoutManager);
//                                        subscribedShopsRes.setItemAnimator(new DefaultItemAnimator());
//                                        subscribedShopsRes.setAdapter(subShopAdapter);
                                        progressBar.setVisibility(View.GONE);



                                    }


//                                mainActivity.setShopListData(subShopList,subShopAdapter,snapshot);
//                                subShopList = mainActivity.getSubShopList();
//
//                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                                subscribedShopsRes.setLayoutManager(linearLayoutManager);
////
//                                subscribedShopsRes.setItemAnimator(new DefaultItemAnimator());
//
//                                subscribedShopsRes.setAdapter(subShopAdapter);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(mainActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }


                    Log.d(TAG, "END FOR");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(mainActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();

                }
            });
            progressBar.setVisibility(View.GONE);

        }catch(Exception e){
            Log.e(TAG, "setSubShopList: " + e.getMessage());
            progressBar.setVisibility(View.GONE);
            Toast.makeText(mainActivity, "שגיאת מערכת. יש לנסות שוב במועד אחר", Toast.LENGTH_SHORT).show();
        }

    }

}