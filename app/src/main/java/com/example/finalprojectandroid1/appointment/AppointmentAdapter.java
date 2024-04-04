package com.example.finalprojectandroid1.appointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectandroid1.R;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder>{
    String TAG = "AppointmentAdapter";
    private ArrayList<AppointmentModel> appointsDataset;

    Context context;
    boolean isOwner;


    public AppointmentAdapter(ArrayList<AppointmentModel> appointsDataset, Context context, boolean isOwner) {
        this.appointsDataset = appointsDataset;
        this.context = context;
        this.isOwner = isOwner;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shopOrUserName;
        TextView shopAddressOrUserAppearance;
        TextView appointTime;
        TextView appointDate;
        TextView appointTypes;
        TextView appointPrice;
        CheckBox deleteCheckBox;
        Button changeAppointBtn;
//        Button waze;
//        Button googleMaps;

        public MyViewHolder(@NonNull View itemView, ArrayList<AppointmentModel> dataset) {
            super(itemView);
            shopOrUserName = itemView.findViewById(R.id.shopOrUserName);
            shopAddressOrUserAppearance = itemView.findViewById(R.id.addressOrAppearances);
            appointTime = itemView.findViewById(R.id.appointTime);
            appointDate = itemView.findViewById(R.id.appointDate);
            appointPrice = itemView.findViewById(R.id.appointPrice);
            appointTypes = itemView.findViewById(R.id.appointTypes);
            deleteCheckBox = itemView.findViewById(R.id.deleteButtonCheckBox);
            changeAppointBtn = itemView.findViewById(R.id.changeAppointButton);



            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(deleteCheckBox.isChecked()){
                        deleteCheckBox.setChecked(false);
                    }else{
                        deleteCheckBox.setChecked(true);
                    }

                    return true;
                }
            });
        }
    }

    @NonNull
    @Override
    public AppointmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_appointments_res, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view, appointsDataset);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.MyViewHolder holder, int position) {

        TextView shopOrUserName = holder.shopOrUserName;
        TextView shopAddressOrUserAppearance = holder.shopAddressOrUserAppearance;
        TextView appointTime = holder.appointTime;
        TextView appointDate = holder.appointDate;
        TextView appointTypes = holder.appointTypes;
        TextView appointPrice = holder.appointPrice;
        CheckBox deleteCheckBox = holder.deleteCheckBox;
        Button changeAppointBtn = holder.changeAppointBtn;

        if(isOwner){
            shopOrUserName.setText(appointsDataset.get(position).getUserUid());
//            shopAddressOrUserAppearance.setText(appointsDataset.get(position).getUserAppearanceNum());
        }else{
            shopOrUserName.setText(appointsDataset.get(position).getShopName());
            shopAddressOrUserAppearance.setText(appointsDataset.get(position).getShopAddress());
            appointPrice.setText(appointsDataset.get(position).getPrice());
        }
        appointTime.setText(appointsDataset.get(position).getTime().getStartTime());
        appointDate.setText(appointsDataset.get(position).getDate());
        appointTypes.setText(appointsDataset.get(position).getAppointmentTypes().toString());


        changeAppointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void getUserAppearanceNum(int position){
//        FirebaseDatabase.getInstance().getReference("shops").child(appointsDataset.get(position).getShopName())
//                .child("userAppearanceNum").child()
    }

    @Override
    public int getItemCount() {
        return appointsDataset.size();
    }


}
