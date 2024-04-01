package com.example.finalprojectandroid1.shop;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalprojectandroid1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder>{

    String TAG = "ShopAdapter";
    private final ShopResInterface shopResInterface;
    private ArrayList<ShopModel> dataset;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ;
    Context context;


    public ShopAdapter(Context context,ArrayList<ShopModel> dataset, ShopResInterface shopResInterface) {
        this.context = context;
        this.dataset = dataset;
//        this.resType = resType;
        this.shopResInterface = shopResInterface;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        String TAG = "ShopAdapter_MyViewHolder";

        TextView shopName;
        TextView shopAddress;
        ImageView shopImage;
        TextView shopTags;


        public MyViewHolder(@NonNull View itemView, ShopResInterface shopResInterface, ArrayList<ShopModel> dataset) {
            super(itemView);
            shopName = itemView.findViewById(R.id.shopNameCard);
            shopAddress = itemView.findViewById(R.id.shopAddressCard);
            shopImage = itemView.findViewById(R.id.shopImageCard);
            shopTags = itemView.findViewById(R.id.shopTags);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if(shopResInterface != null){
                            int pos = getAdapterPosition();
                            Log.d(TAG, "pos: " + pos);
                            if(pos != RecyclerView.NO_POSITION){
                                shopResInterface.onItemClick(pos,dataset);
                            }
                        }
                    }catch(Exception e ){
                        Log.e(TAG, e.getMessage());
                    }

                }
            });


        }
    }

    @NonNull
    @Override
    public ShopAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_shop_res, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view, shopResInterface, dataset);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.MyViewHolder holder, int position) {
//        ShopModel shop = dataset.get(position);
        TextView shopName = holder.shopName;
        TextView shopAddress = holder.shopAddress;
        ImageView shopImage = holder.shopImage;
        String imageUrl;
        TextView shopTags = holder.shopTags;

        shopName.setText(dataset.get(position).getShopName());
        Log.d(TAG, "name: " + shopName.getText().toString());
        shopAddress.setText(dataset.get(position).getShopAddress());
        imageUrl = dataset.get(position).getShopImage();
        Glide.with(context).load(imageUrl).into(shopImage);

        Log.d(TAG,"tags list: " + dataset.get(position).getShopTags());
        shopTags.setText("");
        for(String tag :dataset.get(position).getShopTags()){
            Log.d(TAG, tag.toString());
            shopTags.setText(shopTags.getText() + " #" + tag);
        }


    }
    public void onClick(View v) {

    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void searchDataList(ArrayList<ShopModel> searchList){
        dataset = searchList;
        notifyDataSetChanged();

    }




}
