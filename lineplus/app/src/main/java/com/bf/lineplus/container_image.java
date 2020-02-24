package com.bf.lineplus;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class container_image extends RecyclerView.Adapter<container_image.ItemViewHolder> {
    private ArrayList<String> mItems;

    container_image(ArrayList<String> items) {
        mItems = items;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);

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
        final String item = mItems.get(position);
        tools.log("파일먕?"+item.length()+"ss");
        if(item.length() > 0) {
            try {
                FileInputStream fis = new FileInputStream(Singleton.Scontext.getFilesDir() + "/" + item);
                BufferedInputStream bis = new BufferedInputStream(fis);

                Bitmap bitmap = BitmapFactory.decodeStream(bis);
//            Uri photoUri = FileProvider.getUriForFile( Singleton.Scontext, Singleton.Scontext.getPackageName(), );
                holder.imageView.setImageBitmap(bitmap);
                bis.close();
                fis.close();
            } catch (Exception e) {
           tools.log("이미지 변환부분"+e.toString());
//                holder.imageView.setVisibility(View.GONE);
            }
        }
        else {
            holder.imageView.setVisibility(View.GONE);
        }
//
//        holder.tv_title.setText(item.getTitle());
//        holder.tv_contents.setText(item.getContents());
//        if(item.getImagelist().equals("")){
//        holder.iv_thumbnail.setVisibility(View.GONE);
//        }


//        holder.shell_layout.setOnClickListener(new View.OnClickListener(){
//@Override
//public void onClick(View v){
//        Intent intent=new Intent(MainActivity.Scontext,Read_note.class);
//        intent.putExtra("id",item.getId());
//        ((Activity)MainActivity.Scontext).startActivityForResult(intent,Singleton.potion_plag_readnote);
//
//        }
//        });
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        //    private TextView tv_title;
//    private ViewGroup shell_layout;
//    private TextView tv_contents;
//    private ImageView iv_thumbnail;
        private ImageView imageView;


        ItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image_item);
//        tv_title = itemView.findViewById(R.id.tv_title);
//        tv_contents = itemView.findViewById(R.id.tv_contents);
//        iv_thumbnail = itemView.findViewById(R.id.iv_thumbnail);
//        shell_layout = itemView.findViewById(R.id.cv_item_layout);

        }
    }
//    private int exifOrientationToDegrees(int exifOrientation) {
//        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
//            return 90;
//        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
//            return 180;
//        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
//            return 270;
//        }
//        return 0;
//    }
//
//    private Bitmap rotate(Bitmap bitmap, float degree) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(degree);
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//    }

}

