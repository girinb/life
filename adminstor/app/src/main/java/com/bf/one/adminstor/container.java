package com.bf.one.adminstor;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class container extends RecyclerView.Adapter<container.ItemViewHolder> {
    private ArrayList<slot> mItems;

    container(ArrayList<slot> items) {
        mItems = items;
    }



    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shell, parent, false);

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
        slot item = mItems.get(position);
        holder.nameTv.setText(item.getName());

//        holder.iconIv.setImageResource();
//        try {

//            Bitmap Temp_Bmp = BitmapFactory.decodeFile(new File(MainActivity.mContext.getCacheDir() + "/" + item.getName() + ".jpg").getAbsolutePath());
//            if (Temp_Bmp != null) {

//                holder.iconIv.setImageBitmap();
//            } else {
//                holder.iconIv.setImageResource(R.drawable.temp);
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
        holder.shell_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView temptv = v.findViewById(R.id.shell_layout).findViewById(R.id.textView_name);
//            Log.w("dd",temptv.getText().toString());
                Intent intent = new Intent(MainActivity.mContext, send_page.class);
                intent.putExtra("title", temptv.getText());
                MainActivity.mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv;
//        private ImageView iconIv;
        private ViewGroup shell_layout;

        ItemViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.textView_name);
//            nameTv.setTypeface(Typeface.createFromAsset(MainActivity.mContext.getAssets(), "font/NEXEN TIRE_Bold.ttf"));
//            iconIv = itemView.findViewById(R.id.imageView_icon);
            shell_layout = itemView.findViewById(R.id.shell_layout);
        }
    }
}
