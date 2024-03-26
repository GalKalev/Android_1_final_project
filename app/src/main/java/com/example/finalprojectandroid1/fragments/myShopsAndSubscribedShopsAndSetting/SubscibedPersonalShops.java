package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.shop.ShopAdapter;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    ArrayList<ShopModel> dataset;
    ShopAdapter adapter;
    DatabaseReference myRef;
    FirebaseDatabase database;
    String uid = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_subscibed_personal_shops, container, false);

        //the code below is crashing

//        RecyclerView resSubscribed = view.findViewById(R.id.resSubscribedShops);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        resSubscribed.setLayoutManager(linearLayoutManager);
//
//        resSubscribed.setItemAnimator(new DefaultItemAnimator());
//
//        database = FirebaseDatabase.getInstance();
//
//        myRef = database.getReference("users").child(uid).child("SubscribedShops");
//
//        dataset = new ArrayList<>();
//        adapter = new ShopAdapter(dataset, uid);
//        resSubscribed.setAdapter(adapter);



        return view;
    }
}