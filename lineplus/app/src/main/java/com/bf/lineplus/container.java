package com.bf.lineplus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;

public class container extends RecyclerView.Adapter<container.ItemViewHolder> {
    private ArrayList<note_item> mItems;
    String[] colors = {"#cf8d2e","#00a4e4","#f47721","#a51890","#008eaa","#007cc0","#9f9fa3","#b4a996"};
    container(ArrayList<note_item> items) {
        mItems = items;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        final ItemViewHolder tempholder = new ItemViewHolder(view);
        tempholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final note_item item = mItems.get(position);
        String[] t_image=  item.getImagelist().split(",");
        holder.setIsRecyclable(false);
        holder.tv_title.setText(item.getTitle());
        holder.tv_contents.setText(item.getContents());
        if (item.getImagelist().equals("")) {
            holder.iv_thumbnail.setVisibility(View.GONE);
        }
        else {
            try {
                FileInputStream fis = new FileInputStream(Singleton.Scontext.getFilesDir()+"/t_"+t_image[0]);
                BufferedInputStream bis = new BufferedInputStream(fis);
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                holder.iv_thumbnail.setImageBitmap(bitmap);
                bis.close();
                fis.close();

            } catch (Exception e) {
                tools.log("비트맵 부분인가? "+e.toString());
            }
        }
        holder.shell_layout.setBackgroundColor(Color.parseColor(colors[position%8]));

        holder.shell_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Singleton.Scontext, Read_note.class);
                intent.putExtra("id", item.getId());
                ((Activity) Singleton.Scontext).startActivityForResult(intent, Singleton.potion_plag_readnote);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private ViewGroup shell_layout;
        private TextView tv_contents;
        private ImageView iv_thumbnail;

        ItemViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_contents = itemView.findViewById(R.id.tv_contents);
            iv_thumbnail = itemView.findViewById(R.id.iv_thumbnail);
            shell_layout = itemView.findViewById(R.id.cv_item_layout);

        }
    }
}
