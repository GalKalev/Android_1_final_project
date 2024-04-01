package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.ShopInfoActivity;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.MyShopAndInfoPagerAdapter;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.example.finalprojectandroid1.shop.WeekdayWorkTime;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OwnedShopStats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OwnedShopStats extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OwnedShopStats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OwnedShopStats.
     */
    // TODO: Rename and change types and number of parameters
    public static OwnedShopStats newInstance(String param1, String param2) {
        OwnedShopStats fragment = new OwnedShopStats();
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

    String TAG = "OwnedShopStats";
    ShopModel shop;
    HashMap<String, List<WeekdayWorkTime>> shopDefaultAvailableTime;
    HashMap<String, Integer> shopAppointsTypes = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.d(TAG, "inside");

        View view = inflater.inflate(R.layout.fragment_owned_shop_stats, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager2 viewPager2 = view.findViewById(R.id.view_pager);
        MyShopAndInfoPagerAdapter myViewPagerAdapter = new MyShopAndInfoPagerAdapter(getActivity(), 1,2);
        viewPager2.setAdapter(myViewPagerAdapter);

        ShopInfoActivity shopInfoActivity = (ShopInfoActivity)getActivity();

        shop = shopInfoActivity.getShop();
        shopDefaultAvailableTime = shopInfoActivity.getShopDefaultAvailableTime();
        shopAppointsTypes = shopInfoActivity.getShopAppointsTypes();


        Button updateShop = view.findViewById(R.id.updateShopInfoButton);
        Button deleteShop = view.findViewById(R.id.deleteShopButton);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        updateShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle toUpdateShop = new Bundle();
                toUpdateShop.putString("userUid", shop.getShopOwnerId());
                toUpdateShop.putParcelable("shop",shop);
                toUpdateShop.putSerializable("shopDefaultAvailableTime", shopDefaultAvailableTime);
                toUpdateShop.putSerializable("shopAppointsTypes", shopAppointsTypes);
                toUpdateShop.putInt("shopPosition", shopInfoActivity.getShopPosition());
                Navigation.findNavController(view).navigate(R.id.action_ownedShopStats_to_updateShopActivity2,toUpdateShop);
            }
        });


        return view;
    }
}