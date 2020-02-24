package com.bf.one.myapplication2;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class container  extends RecyclerView.Adapter<container.ItemViewHolder> {
    private ArrayList<slot> mItems;
    container(ArrayList<slot> items){
        mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        slot item = mItems.get(position);
        holder.conutTV.setText(""+(position+1));
        holder.nameTv.setText(item.getName());
        holder.ageTv.setText(String.valueOf(item.getAge()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTv;
        private TextView ageTv;
        private TextView conutTV;
        ItemViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.TV_name);
            ageTv = itemView.findViewById(R.id.TV_age);
            conutTV = itemView.findViewById(R.id.TV_count);
        }
    }
}
