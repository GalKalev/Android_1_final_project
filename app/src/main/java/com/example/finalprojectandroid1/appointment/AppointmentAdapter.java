package com.example.finalprojectandroid1.appointment;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.String;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.MainActivity;
import com.example.finalprojectandroid1.activities.ShopInfoActivity;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder>{
    String TAG = "AppointmentAdapter";
    ArrayList<AppointmentModel> appointsDataset;
    ArrayList<AppointmentModel> selectAppointsDataset = new ArrayList<>();
    Activity activity;

//    Context context;
    boolean isOwner;
    boolean isEnable = false;
    boolean isSelectAll = false;
    MainViewModel mainViewModel;
    String shopOrUserUid;
    String formattedStartTime;
    DatabaseReference userDatabase;
    DatabaseReference shopDatabase;
    int activityNum;


    public AppointmentAdapter( ArrayList<AppointmentModel> appointsDataset, Activity activity,int activityNum, boolean isOwner, String shopOrUserUid) {
        this.appointsDataset = appointsDataset;
        this.shopOrUserUid = shopOrUserUid;
        this.isOwner = isOwner;
        this.activity = activity;
        this.activityNum = activityNum;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shopNameUserOrUserNameAndAppearancesNum;
        TextView addressUserOrAppointTypesShop;
        TextView appointTime;
        TextView appointDate;
        TextView appointTypes;
        TextView appointPrice;
        TextView noAppoints;
        ImageView deleteCheckBoxImage;
        Button changeAppointBtn;
        LinearLayout ExtendedInfoLayoutForCustomer;
        LinearLayout basicInfoLayout;
        LinearLayout appointmentLayout;



//        Button waze;
//        Button googleMaps;

        public MyViewHolder(@NonNull View itemView, boolean isOwner) {
            super(itemView);

            String TAG = "AppointmentAdapter MyViewHolder";

            shopNameUserOrUserNameAndAppearancesNum = itemView.findViewById(R.id.shopNameUserOrUserNameAndApearancesNum);
            addressUserOrAppointTypesShop = itemView.findViewById(R.id.addressUserOrAppointTypesShop);
            appointTime = itemView.findViewById(R.id.appointTime);
            appointDate = itemView.findViewById(R.id.appointDate);
            appointPrice = itemView.findViewById(R.id.appointPrice);
            appointTypes = itemView.findViewById(R.id.appointTypes);
            deleteCheckBoxImage = itemView.findViewById(R.id.deleteCheckBoxImage);
            changeAppointBtn = itemView.findViewById(R.id.changeAppointButton);
            ExtendedInfoLayoutForCustomer = itemView.findViewById(R.id.ExtendedInfoLayoutForCustomer);
            basicInfoLayout = itemView.findViewById(R.id.basicInfoLayoutAppointment);
            noAppoints = itemView.findViewById(R.id.noAppointsText);
            appointmentLayout = itemView.findViewById(R.id.appointmentLayout);



//            if(!isOwner){
//                basicInfoLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.d(TAG, "click");
//                        if(ExtendedInfoLayoutForCustomer.getVisibility() == View.VISIBLE){
//
//                            ExtendedInfoLayoutForCustomer.setVisibility(View.GONE);
//                        }else{
//                            ExtendedInfoLayoutForCustomer.setVisibility(View.VISIBLE);
//                        }
//                        Log.d(TAG,"ExtendedInfoLayoutForCustomer.getVisibility(): " + ExtendedInfoLayoutForCustomer.getVisibility());
//                    }
//                });
//            }
        }
    }

    @NonNull
    @Override
    public AppointmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_appointments_res, parent, false);
//        MyViewHolder myViewHolder = new MyViewHolder(view, appointsDataset,isOwner);
//        return myViewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_appointments_res, parent, false);

        // Initialize view model using ViewModelProvider
        mainViewModel = new ViewModelProvider((FragmentActivity) activity).get(MainViewModel.class);

        return new MyViewHolder(view, isOwner);

    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.MyViewHolder holder, int position) {

        TextView shopNameUserOrUserNameAndAppearancesNum = holder.shopNameUserOrUserNameAndAppearancesNum;
        TextView addressUserOrAppointTypesShop = holder.addressUserOrAppointTypesShop;
        TextView appointTime = holder.appointTime;
        TextView appointDate = holder.appointDate;
        TextView appointTypes = holder.appointTypes;
        TextView appointPrice = holder.appointPrice;
        TextView noAppoints = holder.noAppoints;
        ImageView deleteCheckBoxImage = holder.deleteCheckBoxImage;
        Button changeAppointBtn = holder.changeAppointBtn;
        LinearLayout infoLayoutForCustomer = holder.ExtendedInfoLayoutForCustomer;
        LinearLayout basicInfoLayout = holder.basicInfoLayout;
        LinearLayout appointmentLayout = holder.appointmentLayout;


         userDatabase = FirebaseDatabase.getInstance().getReference("users");
         shopDatabase = FirebaseDatabase.getInstance().getReference("shops");
//         String dateCompare = GlobalMembers.convertDateFromShowToCompare(appointmentModel.getDate());
//            String timeToRemove = GlobalMembers.formattingTimeToString(appointmentModel.getTime().getStartTime());

//        formattedStartTime = GlobalMembers.formattingTimeToString(appointsDataset.get(position).getTime().getStartTime());
        formattedStartTime = appointsDataset.get(position).getTime().getStartTime().substring(0,2) + ":" + appointsDataset.get(position).getTime().getStartTime().substring(2);
        Log.d(TAG, "formattedStartTime: " + formattedStartTime + " appointsDataset.get(position).getTime().getStartTime(): " + appointsDataset.get(position).getTime().getStartTime());

        if(isOwner){
            shopNameUserOrUserNameAndAppearancesNum.setText(appointsDataset.get(position).getUserName());
            for(String appointTypeString : appointsDataset.get(position).getAppointmentTypes()){
                addressUserOrAppointTypesShop.setText(appointTypes.getText().toString()  + appointTypeString + ", ");
            }
        }else{
            shopNameUserOrUserNameAndAppearancesNum.setText(appointsDataset.get(position).getShopName());
            addressUserOrAppointTypesShop.setText(appointsDataset.get(position).getShopAddress());
            appointPrice.setText(appointsDataset.get(position).getPrice() + " ש\"ח ");
            for(String appointTypeString : appointsDataset.get(position).getAppointmentTypes()){
                appointTypes.setText(appointTypes.getText().toString()  + appointTypeString + ", ");
            }


        }

        appointTime.setText(formattedStartTime);
        appointDate.setText(appointsDataset.get(position).getDate());




        Log.d(TAG, "appointTime: " + appointTime.getText().toString());
        Log.d(TAG, "appointDate: " + appointDate.getText().toString());
        Log.d(TAG, "appointTypes: " + appointTypes.getText().toString());


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (!isEnable)
                {
                    // when action mode is not enable
                    // initialize action mode
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            // initialize menu inflater
                            MenuInflater menuInflater= mode.getMenuInflater();
                            // inflate menu
                            menuInflater.inflate(R.menu.delete_menu,menu);
                            // return true
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            // when action mode is prepare
                            // set isEnable true
                            isEnable = true;
                            // create method
                            ClickItem(holder);
                            // set observer on getText method
                            mainViewModel.getText().observe((LifecycleOwner) activity
                                    , new Observer<String>() {
                                        @Override
                                        public void onChanged(String s) {
                                            // when text change
                                            // set text on action mode title
                                            mode.setTitle(String.format("%s Selected",s));
                                        }
                                    });
                            // return true
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            // when click on action mode item
                            // get item  id
                            int id = item.getItemId();
                            // use switch condition
                            if(id == R.id.menu_delete ){

                                try {

                                    for(AppointmentModel appointmentModel : selectAppointsDataset){
//                                        SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy");
//                                        Date date = sdfInput.parse(appointmentModel.getDate());
//
//                                        SimpleDateFormat sdfCompare = new SimpleDateFormat("yyyyMMdd");
//                                        String dateCompare = sdfCompare.format(date);
                                        String dateCompare = GlobalMembers.convertDateFromShowToCompare(appointmentModel.getDate());
                                        String timeToRemove = appointmentModel.getTime().getStartTime().substring(0, 2) + ":" + appointmentModel.getTime().getStartTime().substring(2);

                                        Log.d(TAG,"dateCompare: " + dateCompare + " timeToRemove " + timeToRemove);
                                        deleteFromDatabase(appointmentModel,dateCompare,timeToRemove);

                                    }
                                    if(appointsDataset.size() == 0){
                                        noAppoints.setVisibility(View.VISIBLE);
                                    }
                                    mode.finish();


                                }catch(Exception e){
                                    Log.e(TAG,e.getMessage());
                                }



                            }else if(id == R.id.menu_select_all){
                                // when click on select all
                                // check condition
                                if(selectAppointsDataset.size() == appointsDataset.size())
                                {
                                    // when all item selected
                                    // set isselectall false
                                    isSelectAll = false;
                                    // create select array list
                                    selectAppointsDataset.clear();
                                }
                                else
                                {
                                    // when  all item unselected
                                    // set isSelectALL true
                                    isSelectAll=true;
                                    // clear select array list
                                    selectAppointsDataset.clear();
                                    // add value in select array list
                                    selectAppointsDataset.addAll(appointsDataset);
                                }
                                // set text on view model
                                mainViewModel.setText(String .valueOf(selectAppointsDataset.size()));
                                // notify adapter
                                notifyDataSetChanged();
                            }

                            // return true
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            // when action mode is destroy
                            // set isEnable false
                            isEnable = false;
                            // set isSelectAll false
                            isSelectAll = false;
                            // clear select array list
                            selectAppointsDataset.clear();
                            // notify adapter
                            notifyDataSetChanged();
                        }
                    };

                    ((AppCompatActivity) v.getContext()).startActionMode(callback);

                    // start action mode

                }
                else
                {
                    // when action mode is already enable
                    // call method
                    ClickItem(holder);
                }
                // return true
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check condition
                Log.d(TAG, "isEnable: " + isEnable);
                if(isEnable)
                {
                    // when action mode is enable
                    // call method
                    ClickItem(holder);
                }
                else
                {
                    // when action mode is not enable
                    // display toast
                    if(!isOwner){



                        Log.d(TAG, "click");
                        if(infoLayoutForCustomer.getVisibility() == View.VISIBLE){

                            infoLayoutForCustomer.setVisibility(View.GONE);
                        }else{
                            infoLayoutForCustomer.setVisibility(View.VISIBLE);
                        }
//                                Log.d(TAG,"ExtendedInfoLayoutForCustomer.getVisibility(): " + infoLayoutForCustomer.getVisibility());}

                    }
                }
            }
        });

        if(isSelectAll)
        {
            // when value selected
            // visible all check boc image
            holder.deleteCheckBoxImage.setVisibility(View.VISIBLE);
            //set background color
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        else
        {
            // when all value unselected
            // hide all check box image
            holder.deleteCheckBoxImage.setVisibility(View.GONE);
            // set background color
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        changeAppointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityNum == 0){
                    Log.d(TAG, "main activity: " + activity.getClass());
                    MainActivity mainActivity = (MainActivity) activity;
                    Log.d(TAG, "main activity get user: " + mainActivity.getUserUid());
                    shopDatabase.child(appointsDataset.get(position).getShopUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d(TAG, "snapshot.getKey(): " + snapshot.getKey());
                            Log.d(TAG, "snapshot.getChildrenCount(): " + snapshot.getChildrenCount());
                            ShopModel shop = snapshot.getValue(ShopModel.class);
                            Log.d(TAG, "shop uid check: " + shop.getShopUid());

                            mainActivity.goToShopPage(shop, true, appointsDataset.get(position).date,appointsDataset.get(position).time.getStartTime());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }else{
                    Log.d(TAG, "shop info activity: " + activity.getClass());
                    ShopInfoActivity shopInfoActivity = (ShopInfoActivity) activity;
                    Log.d(TAG, "shop info get user: " + shopInfoActivity.getUserUid());
                    shopInfoActivity.changeAppoint(true,appointsDataset.get(position).date,appointsDataset.get(position).time.getStartTime());


                }
            }
        });

    }

    private void deleteFromDatabase(AppointmentModel appointmentModel,String date, String time){
        if(isOwner){
            userDatabase.child(appointmentModel.getUserUid()).child("userAppointments").
                    child(date).child(time).removeValue();

            shopDatabase.child(shopOrUserUid).child("shopAppointments").
                    child(date).child(time).removeValue();
            shopDatabase.child(shopOrUserUid).child("usersAppearances").child(appointmentModel.getUserUid()).setValue(ServerValue.increment(-1));

//                                            userDatabase.child(appointmentModel.getUserUid()).child("userAppointments").
//                                                    child(dateCompare).child(timeToRemove).removeValue();
//                                            shopDatabase.child(shopOrUserUid).child("shopAppointments").
//                                                    child(dateCompare).child(timeToRemove).removeValue();
        }else{
            shopDatabase.child(appointmentModel.getShopUid()).child("shopAppointments").
                    child(date).child(time).removeValue();
            shopDatabase.child(appointmentModel.getShopUid()).child("usersAppearances").child(shopOrUserUid).child("appointmentsOrdered").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int appearanceNum = snapshot.getValue(Integer.class);
                    if(appearanceNum == 1){
                        snapshot.getRef().getParent().removeValue();
                    }else{
                        appearanceNum--;
                        snapshot.getRef().setValue(appearanceNum);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            userDatabase.child(shopOrUserUid).child("userAppointments").
                    child(date).child(time).removeValue();
        }

        appointsDataset.remove(appointmentModel);

    }


    private void ClickItem(AppointmentAdapter.MyViewHolder holder) {

        // get selected item value
        AppointmentModel a = appointsDataset.get(holder.getAdapterPosition());
        Log.d(TAG,"a: " + a.getDate());
        // check condition
        if(holder.deleteCheckBoxImage.getVisibility()==View.GONE)
        {
            // when item not selected
            // visible check box image
            holder.deleteCheckBoxImage.setVisibility(View.VISIBLE);
            // set background color
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            // add value in select array list
            selectAppointsDataset.add(a);
        }
        else
        {
            // when item selected
            // hide check box image
            holder.deleteCheckBoxImage.setVisibility(View.GONE);
            // set background color
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            // remove value from select arrayList
            selectAppointsDataset.remove(a);

        }
        // set text on view model
        String selectSize = String.valueOf(selectAppointsDataset.size());
        mainViewModel.setText(selectSize);
    }

//    private String formattingTime(int time){
//        String timeString;
//        if(time < 1000){
//            timeString = "0" + time;
//        }else{
//            timeString = String.valueOf(time);
//        }
//        timeString = timeString.substring(0, 2) + ":" + timeString.substring(2);
//
//        return timeString;
//    }


    @Override
    public int getItemCount() {
        return appointsDataset.size();
    }


}
