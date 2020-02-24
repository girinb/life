package com.bf.one.test_loadimg;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class container extends RecyclerView.Adapter<container.ItemViewHolder> {
    private ArrayList<slot> mItems;

    container(ArrayList<slot> items) {
        mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shell, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        slot item = mItems.get(position);
        holder.nameTv.setText(item.getName());
        holder.iconIv.setImageResource(R.drawable.temp);
        try {
//            Uri uri = Uri.parse("file:///" + Environment.getExternalStorageDirectory() + "/temp_folder/" + item.getName() + ".png");
//            File imgfile = new File( Environment.getExternalStorageDirectory() + "/temp_folder/" + item.getName() + ".png");
            Bitmap Temp_Bmp = BitmapFactory.decodeFile(new File( Environment.getExternalStorageDirectory() + "/temp_folder/" + item.getName() + ".png").getAbsolutePath());
            if(Temp_Bmp != null) {
                holder.iconIv.setImageBitmap(Temp_Bmp);
            }
            else{
                holder.iconIv.setImageResource(R.drawable.temp);
            }
//            holder.iconIv.getLayoutParams().height = 400;
//            holder.iconIv.getLayoutParams().width = 400;
//            holder.iconIv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//              holder.iconIv.
//            holder.iconIv.setImageURI(uri);
//            holder.iconIv.setImageResource(R.drawable.temp);


//            if(uri.getPath() == null) {
//                holder.iconIv.setImageResource(R.drawable.temp);
//            }else {


//            }
//                            holder.iconIv.setImageResource(R.drawable.temp);
        } catch (Throwable e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv;
        private ImageView iconIv;

        ItemViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.textView_name);
            iconIv = itemView.findViewById(R.id.imageView_icon);
        }
    }
}
