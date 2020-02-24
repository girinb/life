package com.bf.one.sell_project.shell_adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bf.one.sell_project.R;

import java.util.ArrayList;

/**
 * Created by charlie on 2017. 12. 7
 */

class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private ArrayList<Item> mItems;
    ItemAdapter(ArrayList<Item> items){
        mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shell,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = mItems.get(position);
        holder.nameTv.setText(item.getName());
//        holder.ageIv.setimage(item.getImgpath());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTv;
        private ImageView ageIv;
        ItemViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.textView_name);
            ageIv = itemView.findViewById(R.id.imageView_icon);
        }
    }
}
