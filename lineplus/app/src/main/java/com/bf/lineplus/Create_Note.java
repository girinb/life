package com.bf.lineplus;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Create_Note extends AppCompatActivity {
    TextInputLayout til_title;
    TextInputLayout til_contents;
    RecyclerView rv_imagelist;
    private String imageFilePath;
    private Uri photoUri;
    ImageView expanded_image;
    StringBuffer sb;
    ArrayList<String> iam = new ArrayList<>();
    container_image itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note);

        //각종 변수 선언부
        til_title = findViewById(R.id.til_title);
        til_contents = findViewById(R.id.til_contents);
        ImageButton ib_save = findViewById(R.id.ib_save);
        ImageButton ib_add = findViewById(R.id.ib_img_add);
        ImageButton ib_camera = findViewById(R.id.ib_img_camera);
        ImageButton ib_uri = findViewById(R.id.ib_img_uri);

        rv_imagelist = findViewById(R.id.rv_imagelist);
        rv_imagelist.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        expanded_image = findViewById(R.id.expanded_image);
        til_title.setCounterMaxLength(100);
        sb = new StringBuffer();

        itemAdapter = new container_image(iam);
        rv_imagelist.setAdapter(itemAdapter);

        Intent intent = getIntent();
/*
        인텐트를 받아서 그 값에 따라서 2가지 모드로 구동
 */
        if (intent.getExtras().getString("create_mode").equals("create")) {
            ib_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    til_title = findViewById(R.id.til_title);
                    til_contents = findViewById(R.id.til_contents);
/*
                기본 구조는 단순
                DB에 본문을 저장
 */

//                    long now = System.currentTimeMillis();
//                    Date mDate = new Date(now);
//                    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddhhmmss");
//                    String getTime = simpleDate.format(mDate);
                    if (til_title.getEditText().getText().toString().length() != 0) {
                        Singleton.getInstance().mDbOpenHelper.insertColumn(til_title.getEditText().getText().toString(), til_contents.getEditText().getText().toString(), sb.toString());
                        Intent intent = getIntent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Snackbar.make(v, "제목을 입력해주세요", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            final int cid = Integer.parseInt(intent.getExtras().getString("create_mode"));
            Cursor t_cCursor = Singleton.getInstance().mDbOpenHelper.getColumn(cid);


            til_title = findViewById(R.id.til_title);
            til_contents = findViewById(R.id.til_contents);
            til_title.getEditText().setText(t_cCursor.getString(t_cCursor.getColumnIndex("TITLE")));
            til_contents.getEditText().setText(t_cCursor.getString(t_cCursor.getColumnIndex("CONTENTS")));

            String[] str = t_cCursor.getString(t_cCursor.getColumnIndex("IMAGES")).split(",");
            for (int i = 0; str.length > i; i++) {
                tools.log(i+"");
                iam.add(str[i]);
                sb.append(str[i]+",");
            }


            itemAdapter.notifyDataSetChanged();

            ib_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    til_title = findViewById(R.id.til_title);
                    til_contents = findViewById(R.id.til_contents);
/*
                기본 구조는 단순
                DB에 본문을 저장
 */
//                    long now = System.currentTimeMillis();
//                    Date mDate = new Date(now);
//                    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddhhmmss");
//                    String getTime = simpleDate.format(mDate);
                    Singleton.getInstance().mDbOpenHelper.updateColumn(cid, til_title.getEditText().getText().toString(), til_contents.getEditText().getText().toString(), sb.toString());

                    finish();
                }
            });


        }
        View.OnClickListener buttoncontrol = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ib_img_add:
                        getalbum();
                        break;
                    case R.id.ib_img_camera:
                        getcamera();
                        break;
                    case R.id.ib_img_uri:
                        break;

                }
            }
        };


        ib_add.setOnClickListener(buttoncontrol);
        ib_camera.setOnClickListener(buttoncontrol);
        ib_uri.setOnClickListener(buttoncontrol);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Singleton.PICK_FROM_ALBUM) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    long now = System.currentTimeMillis();
                    Date mDate = new Date(now);
                    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddhhmmss");
                    String getTime = simpleDate.format(mDate);
                    saveBitmapToJpeg(img, getTime);
                    // 파일을 쓸 수 있는 스트림을 준비합니다.
                    sb.append(getTime + ".jpg,");

                    tools.log(sb.toString());
                    iam.add(getTime + ".jpg");
                    itemAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "사진을 가져오지 못했습니다.", Snackbar.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "사진 올리기를 취소하셨습니다.", Snackbar.LENGTH_LONG).show();
            }
        } else if (requestCode == Singleton.PICK_FROM_CAMERA) {
            if (resultCode == RESULT_OK) {
                try {

//                    imageView.setImageURI(photoUri);
                } catch (Exception e) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "사진을 가져오지 못했습니다.", Snackbar.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "사진 올리기를 취소하셨습니다.", Snackbar.LENGTH_LONG).show();
            }
        }

    }

    void getalbum() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Singleton.PICK_FROM_ALBUM);
    }

    void getcamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                long now = System.currentTimeMillis();
                Date mDate = new Date(now);
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddhhmmss");
                String getTime = simpleDate.format(mDate);

                photoFile = createImageFile(getTime);
                tools.log("파일주소"+photoFile.getName());

                sb.append(photoFile.getName()+",");

                tools.log(sb.toString());
                iam.add(photoFile.getName());
                itemAdapter.notifyDataSetChanged();


            } catch (IOException ex) {
                // Error occurred while creating the File
                tools.log("카메라" + ex.toString());
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, Singleton.PICK_FROM_CAMERA);
            }
        }
    }

    private void saveBitmapToJpeg(Bitmap bitmap, String name) {

        //내부저장소 캐시 경로를 받아옵니다.
        File storage = getFilesDir();

        //저장할 파일 이름
        String fileName = name + ".jpg";

        //storage 에 파일 인스턴스를 생성합니다.
        File tempFile = new File(storage, fileName);

        try {

            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();
            tools.log("vkdlfa파일만들어짐");
            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream out = new FileOutputStream(tempFile);

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            tools.log("파일저징");
            // 스트림 사용후 닫아줍니다.
            out.close();


        } catch (FileNotFoundException e) {
            Log.e("MyTag", "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag", "IOException : " + e.getMessage());
        }

        fileName = "t_" + name + ".jpg";
        tempFile = new File(storage, fileName);
        try {

            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();
            tools.log("vkdlfa파일만들어짐");
            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream out = new FileOutputStream(tempFile);

            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            int inSampleSize = 1;
            if (height > 150 * bitmap.getHeight() / bitmap.getWidth() || width > 150) {

                int halfHeight = height / 2;
                int halfWidth = width / 2;

                while ((halfHeight / inSampleSize) >= 150 * bitmap.getHeight() / bitmap.getWidth()
                        && (halfWidth / inSampleSize) >= 150) {
                    inSampleSize *= 2;
                }
            }


            bitmap = Bitmap.createScaledBitmap(bitmap, width / inSampleSize, height / inSampleSize, true);

            // compress 함수를 사용해 스트림에 비트맵을 저장
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            tools.log("파일저징" + width / inSampleSize + "//" + height / inSampleSize);
            // 스트림 사용후 닫아줍니다.
            out.close();


        } catch (FileNotFoundException e) {
            Log.e("MyTag", "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag", "IOException : " + e.getMessage());
        }

    }

    private void convert_camera(String name) {
        try {
            FileInputStream in = new FileInputStream(getFilesDir() + name + ".jpg");
            Bitmap originalBm = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            originalBm = BitmapFactory.decodeFile(getFilesDir() + name + ".jpg", options);

//            imageView.setImageBitmap(originalBm);

        } catch (FileNotFoundException e) {

        }
    }

    private File createImageFile(String imageFileName) throws IOException {
        File image = null;
        image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                getFilesDir()          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        tools.log(imageFilePath);
        return image;
    }
}
