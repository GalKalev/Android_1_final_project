package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.ShopInfoActivity;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.PagerAdapter;
import com.example.finalprojectandroid1.shop.AppointmentsTimeAndPrice;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.example.finalprojectandroid1.shop.TimeRange;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

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

    // Mainly contains the tab for the shop information and shop appointments
    String TAG = "OwnedShopStats";
    ShopModel shop;
    HashMap<String, List<TimeRange>> shopDefaultAvailableTime;
    HashMap<String, AppointmentsTimeAndPrice> shopAppointsTypes = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_owned_shop_stats, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager2 viewPager2 = view.findViewById(R.id.view_pager);
        PagerAdapter myViewPagerAdapter = new PagerAdapter(getActivity(), 1,2);
        viewPager2.setAdapter(myViewPagerAdapter);

        ShopInfoActivity shopInfoActivity = (ShopInfoActivity)getActivity();

        shop = shopInfoActivity.getShop();
        shopDefaultAvailableTime = shopInfoActivity.getShopDefaultAvailableTime();
        shopAppointsTypes = shopInfoActivity.getShopAppointsTypes();


        Button updateShop = view.findViewById(R.id.updateShopInfoButton);
        Button deleteShop = view.findViewById(R.id.deleteShopButton);

        // Tabs
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

        // Owner can update and change shop information
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
                shopInfoActivity.finish();
            }
        });

        DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("shops").child(shop.getShopUid());

        // Deleteing the shop from the database
        deleteShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.card_cancel_confirmation_dialog);
                dialog.show();
                TextView cancelText = dialog.findViewById(R.id.showCancelTextCancelConfirmDialog);
                cancelText.setText("מחיקת החנות " + shop.getShopName() + ", תמחק לצמיתות את כל המידע על החנות ותבטל את כל התורים הקיימים");
                Button confirmShopDeletion = dialog.findViewById(R.id.confirmCancellationButtonDialog);

                confirmShopDeletion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shopRef.child("shopAppointments").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot appointDateSnap : snapshot.getChildren()){
                                    String date = String.valueOf(appointDateSnap.getKey());
                                    for (DataSnapshot appointTimeSnap : appointDateSnap.getChildren()){
                                        String time = appointTimeSnap.getKey();
                                        String userUid = appointDateSnap.child("userUid").getValue(String.class);
                                        FirebaseDatabase.getInstance().getReference("users").child(userUid)
                                                .child("userAppointments").child(date).child(time).removeValue();
                                    }
                                }
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                                Query removeShopSubscription = userRef.orderByChild("subscribedShops").equalTo(shop.getShopUid());

                                removeShopSubscription.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot userSnap : snapshot.getChildren()){
                                            userSnap.child("subscribedShops").child(shop.getShopUid()).getRef().removeValue();
                                        }
                                        String imageUrl = shop.getShopImage();
                                        imageUrl = imageUrl.replace("https://firebasestorage.googleapis.com/v0/b/finalprojectandroid1-29f41.appspot.com/o/shops%2Fimages%2F","");
                                        int questionMarkLocationInUrl = imageUrl.indexOf("?");
                                        String imageStorageLocation = imageUrl.substring(0,questionMarkLocationInUrl);

                                        Log.d(TAG, "image url: " + imageUrl);
                                        Log.d(TAG, "imageStorageLocation: " + imageStorageLocation);
                                        
                                        FirebaseStorage.getInstance().getReference("shops").child("images").child(imageStorageLocation).delete();
                                        shopRef.removeValue();

                                        dialog.dismiss();
                                        shopInfoActivity.finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e(TAG, "error deleting shop: " + error.getMessage());
                                        Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
                                    }
                                });




                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "error deleting shop: " + error.getMessage());
                                Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                Button dismissButton = dialog.findViewById(R.id.backButtonCancelDialog);
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });


        return view;
    }
}