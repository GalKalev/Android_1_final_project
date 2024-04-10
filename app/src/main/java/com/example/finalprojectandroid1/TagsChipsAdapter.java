package com.example.finalprojectandroid1;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectandroid1.shop.ShopAdapter;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.example.finalprojectandroid1.shop.ShopResInterface;

import java.util.ArrayList;

public class TagsChipsAdapter extends RecyclerView.Adapter<TagsChipsAdapter.MyViewHolder>{
    String TAG = "TagsChipsAdapter";
    ArrayList<String> tagsList;

    public TagsChipsAdapter(ArrayList<String> tagsList) {
        this.tagsList = tagsList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tagName;
        LinearLayout tagLayout;
        ImageView deleteIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           tagName = itemView.findViewById(R.id.tagName);
           tagLayout = itemView.findViewById(R.id.tagLayout);
           deleteIcon = itemView.findViewById(R.id.deleteIcon);


        }
    }


    @NonNull
    @Override
    public TagsChipsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tags_chips, parent, false);
        TagsChipsAdapter.MyViewHolder myViewHolder = new TagsChipsAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TagsChipsAdapter.MyViewHolder holder, int position) {

        TextView tagName = holder.tagName;
        LinearLayout tagLayout = holder.tagLayout;
        ImageView deleteIcon = holder.deleteIcon;

        tagName.setText(tagsList.get(position));
        tagLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagsList.remove(position);

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return tagsList.size();
    }


}
