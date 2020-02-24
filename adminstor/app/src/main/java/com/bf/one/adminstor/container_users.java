package com.bf.one.adminstor;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class container_users extends RecyclerView.Adapter<container_users.ItemViewHolder> {
    private ArrayList<user_slot> mItems;

    container_users(ArrayList<user_slot> items) {
        mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_cell, parent, false);

//        final ItemViewHolder tempholder = new ItemViewHolder(view);
//        tempholder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        return holder;
//        return holder;
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.mContext, search.class);
////                int itemPosition = parent.getChildPosition(v);
//                String item = mList.get(itemPosition);
//                Toast.makeText(MainActivity.mContext, item, Toast.LENGTH_LONG).show();
//
//                MainActivity.mContext.startActivity(intent);
//            }
//        });
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        user_slot item = mItems.get(position);
        holder.nameTv.setText(item.getName());
        holder.callTv.setText(item.getCallnumber());
        holder.user_shell_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < send_page.search_data_bases.size(); i++) {
                    if (send_page.search_data_bases.get(i).getName().equals(holder.nameTv.getText()) && send_page.search_data_bases.get(i).getCallnumber().equals(holder.callTv.getText())) {
                        if (send_page.search_data_bases.get(i).getSelected()) {  //체크 없으면
                            Log.w("1차플", send_page.search_data_bases.get(i).getSelected() + "//" + "false");
                            send_page.search_data_bases.get(i).setSelected(false);
                            holder.user_shell_layout.setBackgroundResource(R.drawable.bg_nonselect);
                        } else { //체크 있으면
                            Log.w("2차플", send_page.search_data_bases.get(i).getSelected() + "//" + "true");
                            send_page.search_data_bases.get(i).setSelected(true);
                            holder.user_shell_layout.setBackgroundResource(R.drawable.bg_selected);
                        }
                    } else continue;
                }
            }
        });
        for (int i = 0; i < send_page.search_data_bases.size(); i++) {
            if (send_page.search_data_bases.get(i).getName() == holder.nameTv.getText() && send_page.search_data_bases.get(i).getCallnumber() == holder.callTv.getText()) {
                if (send_page.search_data_bases.get(i).getSelected()) {  //체크 없으면
                    holder.user_shell_layout.setBackgroundResource(R.drawable.bg_selected);
                } else { //체크 있으면
                    holder.user_shell_layout.setBackgroundResource(R.drawable.bg_nonselect);
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv;
        private TextView callTv;
//        private ImageView iconIv;
        private ViewGroup user_shell_layout;

        ItemViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.textView_user_name);
            callTv = itemView.findViewById(R.id.textView_callnumber);
            user_shell_layout = itemView.findViewById(R.id.user_cell);
//            iconIv = itemView.findViewById(R.id.cell_onoff);
//            iconIv.setBackgroundResource(R.drawable.selected_off);
        }
    }
}
