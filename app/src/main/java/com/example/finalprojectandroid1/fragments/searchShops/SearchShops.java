package com.example.finalprojectandroid1.fragments.searchShops;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.MainActivity;
import com.example.finalprojectandroid1.shop.ShopAdapter;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.example.finalprojectandroid1.shop.ShopResInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchShops#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchShops extends Fragment implements ShopResInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchShops() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchShops.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchShops newInstance(String param1, String param2) {
        SearchShops fragment = new SearchShops();
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

    String TAG = "SearchShops";

    private RecyclerView recyclerView;
    private ShopAdapter shopAdapter;
    private DatabaseReference shopsRef;
    private ArrayList<ShopModel> shopList;
    private ArrayList<ShopModel> filteredShopList;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_shops, container, false);
        SearchView searchView = view.findViewById(R.id.searchShops);
        recyclerView = view.findViewById(R.id.searchShopsRes);
        shopsRef = FirebaseDatabase.getInstance().getReference("shops");

        mainActivity = (MainActivity)getActivity();

        shopList = new ArrayList<>();
        filteredShopList = new ArrayList<>();
        shopAdapter = new ShopAdapter(getContext(), filteredShopList, this);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(shopAdapter);

        // Fetch shop data from Firebase
        fetchShopData();

        // Set up SearchView

        searchView.setQueryHint("חפש שם של חנות...");

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter shop list based on search query
                filterShopList(newText);
                return true;
            }
        });

        return view;
    }

    // Method to fetch shop data from Firebase
    private void fetchShopData() {
        shopsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopList.clear();
                Log.d(TAG, "dataSnapshot.getKey(): " + dataSnapshot.getKey());


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataSnapshot shopInfoSnap = snapshot.child("shopInfo");
                    Log.d(TAG, "snapshot.getKey(): " + snapshot.getKey());
                    Log.d(TAG, "shopInfoSnap.getKey(): " + shopInfoSnap.getKey());
                    ShopModel shop = shopInfoSnap.getValue(ShopModel.class);
                    shopList.add(shop);
                }
                // Update the filtered shop list
                filteredShopList.clear();
                filteredShopList.addAll(shopList);
                shopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error fetching shop data: " + databaseError.getMessage());
            }
        });
    }

    // Method to filter the shop list based on search query
    private void filterShopList(String query) {
        filteredShopList.clear();
        for (ShopModel shop : shopList) {
            if (shop.getShopName().toLowerCase().contains(query.toLowerCase())) {
                filteredShopList.add(shop);
            }
        }
        shopAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position, ArrayList<ShopModel> shopList) {
        mainActivity.onItemClick(position,shopList);
    }
}