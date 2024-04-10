package com.example.finalprojectandroid1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.appointment.AppointmentModel;
import com.example.finalprojectandroid1.shop.AppointmentsTimeAndPrice;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.example.finalprojectandroid1.shop.TimeRange;
import com.example.finalprojectandroid1.user.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopInfoActivity extends AppCompatActivity {


    String TAG = "sShopInfoActivity";
    ShopModel shop;
    HashMap<String, List<TimeRange>> shopDefaultAvailableTime = new HashMap<>();
    HashMap<String, AppointmentsTimeAndPrice> shopAppointsTypes = new HashMap<>();
    int shopPosition;
    UserInfo user;
//    ArrayList<List<WeekdayWorkTime>> timeList = new ArrayList<>();
//    ArrayList<String> days = new ArrayList<>();

    String userUid;
    boolean isOwner;
    ArrayList<ShopModel> ownedShopList;
    ArrayList<AppointmentModel> myAppointmentsList;
    Bundle fromMainActivity;
    NavController navController;

    boolean isAppointChange;
    String appointChangeDate;
    String appointChangeStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);

        TextView shopName = findViewById(R.id.shopNameShopActivity);
        TextView shopAddress = findViewById(R.id.shopAddressShopActivity);
        ImageView shopImage = findViewById(R.id.shopImageShopActivity);

        fromMainActivity = getIntent().getExtras();
        if(!fromMainActivity.isEmpty()){
            isOwner = fromMainActivity.getBoolean("isOwned");

            shop = getIntent().getParcelableExtra("shop");
            shopDefaultAvailableTime = (HashMap<String, List<TimeRange>>) getIntent().getSerializableExtra("shopDefaultAvailableTime");
            shopAppointsTypes = (HashMap<String, AppointmentsTimeAndPrice>) getIntent().getSerializableExtra("shopSetAppointment");
            shopPosition = fromMainActivity.getInt("shopPosition");

            ownedShopList = getIntent().getParcelableArrayListExtra("ownedShopList");

            shopName.setText(shop.getShopName().toString());
            shopAddress.setText(shop.getShopAddress().presentAddress());
            String imageUrl = shop.getShopImage().toString();
            Glide.with(this).load(imageUrl).into(shopImage);

            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView4);
            navController = navHostFragment.getNavController();


            if(!isOwner){
                userUid = fromMainActivity.getString("userUid");
                user = fromMainActivity.getParcelable("user");
                myAppointmentsList = fromMainActivity.getParcelableArrayList("myAppointmentsList");

                changeAppoint(fromMainActivity.getBoolean("isAppointChange"),fromMainActivity.getString("appointChangeDate"),fromMainActivity.getString("appointChangeStartTime"));
            }

        }else{
            Log.e(TAG, "Problem with getting from main activity");

        }

//        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                Intent i = new Intent(ShopInfoActivity.this, MainActivity.class);
//                i.putExtra("updateShop",1);
//                i.putExtra("curUserUid", );
//
//                startActivity(i);
//            }
//        });

    }

    private void notOwnerSettings(){
        userUid = fromMainActivity.getString("userUid");
        user = fromMainActivity.getParcelable("user");
        myAppointmentsList = fromMainActivity.getParcelableArrayList("myAppointmentsList");
        Bundle isAppointChange = new Bundle();
        isAppointChange.putBoolean("isAppointChange",fromMainActivity.getBoolean("isAppointChange"));
        navController.navigate(R.id.notOwnedShopStats,isAppointChange);
    }

    public void setDesLinksTags(TextView des, LinearLayout linksLayout, TextView tags){
        des.setText(shop.getShopDes());
        linksLayout.setGravity(Gravity.END);

        for(String tag : shop.getShopTags()){
            tags.setText(tags.getText() + "#" + tag + " ");
        }
        Log.d(TAG, "LINKS: " + shop.getShopLinks());
        if(shop.getShopLinks() != null){
            Log.d(TAG, "LINKS not empty: " + shop.getShopLinks());
            for(String link : shop.getShopLinks()){
                int linkImage = GlobalMembers.detectSocialMedia(link);
                Log.d(TAG,"domain: " + linkImage);
                ImageButton linkIcon = new ImageButton(this);
                linkIcon.setBackgroundColor(Color.TRANSPARENT);
                linkIcon.setImageResource(linkImage);
                linkIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent toUrl = new Intent(Intent.ACTION_VIEW);
                        toUrl.setData(Uri.parse(link));
                        startActivity(toUrl);
                    }
                });

                linksLayout.addView(linkIcon);
            }
        }else{
            Log.d(TAG, "LINKS empty: " + shop.getShopLinks());
            TextView noLinks = new TextView(this);
            noLinks.setText("לא הוזנו לינקים");
            linksLayout.addView(noLinks);
        }

    }

    public ShopModel getShop() {
        return shop;
    }

    public void setShop(ShopModel shop) {
        this.shop = shop;
    }

    public HashMap<String, List<TimeRange>> getShopDefaultAvailableTime() {
        return shopDefaultAvailableTime;
    }

    public void setShopDefaultAvailableTime(HashMap<String, List<TimeRange>> shopDefaultAvailableTime) {
        this.shopDefaultAvailableTime = shopDefaultAvailableTime;
    }

    public HashMap<String, AppointmentsTimeAndPrice> getShopAppointsTypes() {
        return shopAppointsTypes;
    }

    public int getShopPosition() {
        return shopPosition;
    }

    public String getUserUid() {
        return userUid;
    }

    public ArrayList<ShopModel> getOwnedShopList() {
        return ownedShopList;
    }

    public UserInfo getUser() {
        return user;
    }

    public boolean getIsOwner() {
        return isOwner;
    }

    public ArrayList<AppointmentModel> getMyAppointmentsList() {
        return myAppointmentsList;
    }

    public void changeAppoint( boolean isAppointChange,String dateToChange, String timeToChange){
        Bundle isAppointChangeBundle = new Bundle();
        isAppointChangeBundle.putBoolean("isAppointChange",isAppointChange);
        isAppointChangeBundle.putString("appointChangeDate",dateToChange);
        isAppointChangeBundle.putString("appointChangeStartTime",timeToChange);
        navController.navigate(R.id.notOwnedShopStats,isAppointChangeBundle);
    }
}