package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShops;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.MainActivity;
import com.example.finalprojectandroid1.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyShopsAndInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyShopsAndInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyShopsAndInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyShopsAndInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static MyShopsAndInfo newInstance(String param1, String param2) {
        MyShopsAndInfo fragment = new MyShopsAndInfo();
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

    private String TAG = "MyShopsAndInfo";
    ActivityMainBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_shops_and_info, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();

        BottomNavigationView topNavBar = view.findViewById(R.id.topNavBarMyShopsAndInfo);

        Log.d(TAG,"inside");

        replaceFragment( new SubscibedPersonalShops());
        topNavBar.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            Log.d(TAG, item.getTitle().toString());
            // Handle item selection
            if(item.getItemId() == R.id.subscribed_top_nav_pesonal){
                fragment = new SubscibedPersonalShops();
            } else if (item.getItemId() == R.id.ownedShops_top_nav_pesonal) {
                fragment = new MyOwnedShops();
            } else if (item.getItemId() == R.id.settings_top_nav_pesonal ) {
                fragment = new AccountSettings();
            }
            // Replace the current fragment with the selected one
            if (fragment != null) {
                replaceFragment(fragment);
                return true; // Return true to indicate that the item selection event has been handled
            }
            return false; // Return false if no fragment is selected
        });
        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView2, fragment);
        transaction.addToBackStack(null);  // Optional: Add the transaction to the back stack
        transaction.commit();
    }
}