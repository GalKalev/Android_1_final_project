package com.example.finalprojectandroid1.shop;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectandroid1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder>{

    String TAG = "ShopAdapter";
    private ArrayList<ShopModel> dataset;
    String uid;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ;


    public ShopAdapter(ArrayList<ShopModel> dataset, String uid) {
        this.dataset = dataset;
        this.uid = uid;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView shopName;
        TextView shopAddress;
        TextView shopDes;
        ImageView shopImage;
        TextView shopTags;
        TextView shopLinks;
        CalendarView shopCalendar;
        RadioGroup shopTime;
        Button submitAppointmentButton;
        Button setAppointmentButton;
        Button subscribeButton;
        Button infoButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            shopName = itemView.findViewById(R.id.shopNameCard);
            shopAddress = itemView.findViewById(R.id.shopAddressCard);
            shopDes = itemView.findViewById(R.id.shopDescriptionCard);
            shopImage = itemView.findViewById(R.id.shopImageCard);
            shopTags = itemView.findViewById(R.id.shopTagsCard);
            shopLinks = itemView.findViewById(R.id.shopLinksCard);
            shopCalendar = itemView.findViewById(R.id.shopCalendarCard);
            shopTime = itemView.findViewById(R.id.shopTimeCard);
            submitAppointmentButton = itemView.findViewById(R.id.submitAppointmentButtonCard);
            setAppointmentButton = itemView.findViewById(R.id.setAppointmentButtonCard);
            subscribeButton = itemView.findViewById(R.id.subscribeButtonCard);
            infoButton = itemView.findViewById(R.id.infoButtonCard);

        }
    }

    @NonNull
    @Override
    public ShopAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_shop, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.MyViewHolder holder, int position) {
        TextView shopName = holder.shopName;
        TextView shopAddress = holder.shopAddress;
        TextView shopDes = holder.shopDes;
        ImageView shopImage = holder.shopImage;
        TextView shopTags = holder.shopTags;
        TextView shopLinks = holder.shopLinks;
        CalendarView shopCalendar = holder.shopCalendar;
        RadioGroup shopTime = holder.shopTime;
        Button submitAppointmentButton = holder.submitAppointmentButton;
        Button setAppointmentButton = holder.setAppointmentButton;
        Button subscribeButton = holder.subscribeButton;
        Button infoButton = holder.infoButton;


        shopName.setText(dataset.get(position).getShopName());
        shopAddress.setText(dataset.get(position).getShopAddress());
        shopDes.setText(dataset.get(position).getShopDes());
//        shopImage.setImageResource(dataset.get(position).getShopImage());
//        shopTags.setText(dataset.get(position).getShopTags());
//        shopLinks.setText(dataset.get(position).getShopLinks());

        shopCalendar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG,"focus");
            }
        });

//        itemAmount.setText(dataset.get(position).getItemAmount());
//        myRef = database.getReference("users").child(uid).child("shoppingList");
//        updateDatabase();

//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int currentPosition = holder.getAdapterPosition();
////                AddItemDialog addItemDialog = new AddItemDialog(v.getContext(),itemName.getText().toString()
////                        ,itemAmount.getText().toString(),ShopAdapter.this,currentPosition);
////                addItemDialog.show();
////                updateDatabase();
////                notifyDataSetChanged();
////
//            }
//        });

//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int currentPosition = holder.getAdapterPosition();
//                dataset.remove(currentPosition);
//                notifyItemRemoved(currentPosition);
//                notifyItemRangeChanged(currentPosition, dataset.size());
//                updateDatabase();
//            }
//        });


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    public void updateAdapterWithData(String name, String amount, int position) {

//        dataset.get(position).setItemName(name);
//        dataset.get(position).setItemAmount(amount);
//        notifyDataSetChanged();
    }

    public void updateDatabase(){
//
//
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Clear existing data in the database node (optional)
//                myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            // Iterate over the dataset and add each item to the database
//                            for (ManagerListModel item : dataset) {
//                                // Prepare data to be written to the database
//                                String itemName = item.getItemName();
//                                String itemAmount = item.getItemAmount();
//                                String itemId = String.valueOf(dataset.indexOf(item)); // Generate a unique key for each item
//                                myRef.child(itemId).child("itemName").setValue(itemName);
//                                myRef.child(itemId).child("itemAmount").setValue(itemAmount);
//                                // Write other item details as needed
//                            }
//                        } else {
//                            // Handle error
//                            Log.e("Firebase", "Failed to clear data: " + task.getException().getMessage());
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                throw error.toException();
//            }
//        });
    }
}
