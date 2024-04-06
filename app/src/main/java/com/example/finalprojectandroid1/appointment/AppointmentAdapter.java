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

import com.example.finalprojectandroid1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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


    public AppointmentAdapter( ArrayList<AppointmentModel> appointsDataset, Activity activity, boolean isOwner, String shopOrUserUid) {
        this.appointsDataset = appointsDataset;
        this.shopOrUserUid = shopOrUserUid;
        this.isOwner = isOwner;
        this.activity = activity;
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



//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if(deleteCheckBox.isChecked()){
//                        deleteCheckBox.setVisibility(View.GONE);
//                        deleteCheckBox.setChecked(false);
//                    }else{
//                        deleteCheckBox.setVisibility(View.VISIBLE);
//                        deleteCheckBox.setChecked(true);
//                    }
//
//                    return true;
//                }
//            });

            if(!isOwner){
                basicInfoLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "click");
                        if(ExtendedInfoLayoutForCustomer.getVisibility() == View.VISIBLE){

                            ExtendedInfoLayoutForCustomer.setVisibility(View.GONE);
                        }else{
                            ExtendedInfoLayoutForCustomer.setVisibility(View.VISIBLE);
                        }
                        Log.d(TAG,"ExtendedInfoLayoutForCustomer.getVisibility(): " + ExtendedInfoLayoutForCustomer.getVisibility());
                    }
                });
            }
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

        if(isOwner){
            shopNameUserOrUserNameAndAppearancesNum.setText(appointsDataset.get(position).getUserName() +
                    " (" + appointsDataset.get(position).getUserAppearancesNum() +") תורים של הלקוח סה\"כ");
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





//        Log.d(TAG, "time num: " + appointsDataset.get(position).getTime());
//        Log.d(TAG, "start time num: " + appointsDataset.get(position).getTime().getStartTime());

        formattedStartTime = formattingTime(appointsDataset.get(position).getTime().getStartTime());
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
                                DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
                                DatabaseReference shopDatabase = FirebaseDatabase.getInstance().getReference("shops");

                                try {

                                    for(AppointmentModel appointmentModel : selectAppointsDataset){
                                        SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy");
                                        Date date = sdfInput.parse(appointmentModel.getDate());

                                        SimpleDateFormat sdfCompare = new SimpleDateFormat("yyyyMMdd");
                                        String dateCompare = sdfCompare.format(date);
                                        String timeToRemove = formattingTime(appointmentModel.getTime().getStartTime());

                                        Log.d(TAG,"dateCompare: " + dateCompare + " timeToRemove " + timeToRemove);
                                        if(isOwner){
                                            userDatabase.child(appointmentModel.getUserUid()).child("userAppointments").
                                                    child(dateCompare).child(timeToRemove).removeValue();

                                            shopDatabase.child(shopOrUserUid).child("shopAppointments").
                                                    child(dateCompare).child(timeToRemove).removeValue();
//                                            userDatabase.child(appointmentModel.getUserUid()).child("userAppointments").
//                                                    child(dateCompare).child(timeToRemove).removeValue();
//                                            shopDatabase.child(shopOrUserUid).child("shopAppointments").
//                                                    child(dateCompare).child(timeToRemove).removeValue();
                                        }else{
                                            shopDatabase.child(appointmentModel.getShopUid()).child("shopAppointments").
                                                    child(dateCompare).child(timeToRemove).removeValue();
                                            userDatabase.child(shopOrUserUid).child("userAppointments").
                                                    child(dateCompare).child(timeToRemove).removeValue();
                                        }
                                        appointsDataset.remove(appointmentModel);

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
                        basicInfoLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "click");
                                if(infoLayoutForCustomer.getVisibility() == View.VISIBLE){

                                    infoLayoutForCustomer.setVisibility(View.GONE);
                                }else{
                                    infoLayoutForCustomer.setVisibility(View.VISIBLE);
                                }
//                                Log.d(TAG,"ExtendedInfoLayoutForCustomer.getVisibility(): " + infoLayoutForCustomer.getVisibility());
                            }
                        });
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

            }
        });




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

    private String formattingTime(int time){
        String timeString;
        if(time < 1000){
            timeString = "0" + time;
        }else{
            timeString = String.valueOf(time);
        }
        timeString = timeString.substring(0, 2) + ":" + timeString.substring(2);

        return timeString;
    }

    @Override
    public int getItemCount() {
        return appointsDataset.size();
    }


}
