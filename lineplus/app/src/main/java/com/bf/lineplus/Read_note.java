package com.bf.lineplus;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.io.File;
import java.util.ArrayList;

public class Read_note extends AppCompatActivity {
    int id;
    RecyclerView rv_read_imagelist;
    container_image itemAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_note);
        /*
        인텐트를 통해서 id 값을 받아와서 커서에 검색된 DB값을 넣음
         */
        Intent dataIntent = getIntent();
        id = dataIntent.getExtras().getInt("id");
        Cursor t_cCursor = Singleton.getInstance().mDbOpenHelper.getColumn(id);
        rv_read_imagelist = findViewById(R.id.rv_read_immagelist);
        rv_read_imagelist.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        ArrayList<String> iam = new ArrayList<>();

        String[] str = t_cCursor.getString(t_cCursor.getColumnIndex("IMAGES")).split(",");
        for (int i = 0; str.length > i; i++) {
            iam.add(str[i]);
        }


        itemAdapter = new container_image(iam);
        rv_read_imagelist.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
        /*
        텍스트 박스에 내용을 넣어줌
         */
        TextView tv_read_title = findViewById(R.id.tv_read_title);
        TextView tv_read_contents = findViewById(R.id.tv_read_contents);
        tv_read_title.setText(t_cCursor.getString(t_cCursor.getColumnIndex("TITLE")));
        tv_read_contents.setText(t_cCursor.getString(t_cCursor.getColumnIndex("CONTENTS")));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_readnote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings_delete:
                //삭제
                Cursor t_cCursor = Singleton.getInstance().mDbOpenHelper.getColumn(id);
                String[] str = t_cCursor.getString(t_cCursor.getColumnIndex("IMAGES")).split(",");
                for (int i = 0; str.length > i; i++) {
                    File file = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        file = new File(getDataDir() + "/" + str[i]);
                        tools.log("dirlemfdjrk여기들감?");
                    }
                    if (file.delete()) {
                        tools.log("삭제완료" + file.toString());
                    } else {
                        tools.log("삭제실패" + file.toString());
                    }


                }
                Singleton.getInstance().mDbOpenHelper.deleteColumn(id);
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.action_settings_modify:
                //수정을 위해 노트 생서부로 넘어감
                Intent intent2 = new Intent(Singleton.Scontext, Create_Note.class);
                intent2.putExtra("create_mode", "" + id);
                startActivityForResult(intent2, Singleton.potion_plag_editnote);
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            /*
            에디트 했으면 종료
             */
            case Singleton.potion_plag_editnote:

                finish();
                break;

        }


    }
}
