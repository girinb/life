package com.bf.one.greenpic;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> {

    // 아이템 리스트
//    private String[] mDataset;
    ArrayList<String> mDataset = new ArrayList();

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<String> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //SDCard.getExternalSDCardPath() + "/DCIM/100EOS7D/"+
        return new ItemViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.nameTv.setText(mDataset.get(getItemViewType(position))  );
//        Uri uri = Uri.parse("file:///" + SDCard.getExternalSDCardPath() + "/DCIM/100EOS7D/" + holder.nameTv.getText());

        holder.button_free.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });

        holder.button_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_freeview:
                        if (holder.key) {
                            holder.imageView_free.setVisibility(View.VISIBLE);

                            Uri uri;
                            if (MainActivity.syatem_mode == 0)
                                uri = Uri.parse("file://" + SDCard.getExternalSDCardPath() + "/DCIM/100EOS7D/" + holder.nameTv.getText());
                            else
                                uri = Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_folder/" + holder.nameTv.getText());
                            holder.imageView_free.setImageURI(uri);
                            holder.imageView_free.getLayoutParams().width = MainActivity.screen_width;
                            holder.imageView_free.getLayoutParams().height = (int) (MainActivity.screen_width * 0.75);
                            holder.key = false;
                            holder.button_free.setText("접기");
                        } else {
                            holder.imageView_free.setVisibility(View.INVISIBLE);
                            holder.imageView_free.getLayoutParams().width = 0;
                            holder.imageView_free.getLayoutParams().height = 0;
                            holder.key = true;
                            holder.button_free.setText("미리보기");
                        }
                        break;
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv;
        private Button button_free;
        private ImageView imageView_free;
        private boolean key = true;

        ItemViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.textView_name);
            button_free = itemView.findViewById(R.id.button_freeview);
            imageView_free = itemView.findViewById(R.id.imageView_free);


        }
    }

    public Object getItem(int position)
    {
        return mDataset.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}