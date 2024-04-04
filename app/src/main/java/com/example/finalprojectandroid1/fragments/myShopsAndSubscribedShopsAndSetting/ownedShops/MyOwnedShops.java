package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.MainActivity;
import com.example.finalprojectandroid1.shop.ShopAdapter;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOwnedShops#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOwnedShops extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyOwnedShops() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOwnedShops.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOwnedShops newInstance(String param1, String param2) {
        MyOwnedShops fragment = new MyOwnedShops();
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

    String TAG = "MyOwnedShops";
    ArrayList<ShopModel> ownedShopList;
    ShopAdapter ownedShopAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String userUid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_owned_shops, container, false);
        Button addOwnedShopButton = view.findViewById(R.id.addOwnedShopButton);
        TextView noOwnedShopsText = view.findViewById(R.id.noOwnedShopsText);
        RecyclerView ownedShopRes = view.findViewById(R.id.ownedRes);
        ProgressBar progressBar = view.findViewById(R.id.progressBar2);

        Log.d(TAG, "in");
        MainActivity mainActivity = (MainActivity) getActivity();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ownedShopRes.setLayoutManager(layoutManager);
//        ownedShopRes.setItemAnimator(new DefaultItemAnimator());

        // Access MainActivity to get ownedShopList and shopAdapter


        ownedShopList = mainActivity.getOwnedShopList();
        ownedShopAdapter = mainActivity.getOwnedShopAdapter();
        // Set the adapter to the RecyclerView
        ownedShopRes.setAdapter(ownedShopAdapter);
        // Hide the progress bar
        progressBar.setVisibility(View.GONE);


        addOwnedShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bundle toUpdateShopActivity = new Bundle();
                    String userUid = mainActivity.getUserUid();
                    toUpdateShopActivity.putString("userUid", userUid);
//                    Navigation.findNavController(view).navigate(R.id.action_myOwnedShops_to_addOwnedShop);
                    Navigation.findNavController(view).navigate(R.id.action_myOwnedShops_to_updateShopActivity,toUpdateShopActivity);
//                    Intent toUpdateShop = new Intent(mainActivity, UpdateShopActivity.class);
//                    startActivity(toUpdateShop);
                    Log.d(TAG,"after");
                } catch (Exception e) {
                    Log.e(TAG, "Error navigating to addOwnedShop fragment: " + e.getMessage());
                }
            }
        });



        return view;
    }



}