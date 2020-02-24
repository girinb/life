package com.bf.lineplus;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    //전역변수


    //지역 변수 선언부
    RecyclerView rv_mainlist;
    ArrayList<note_item> itemlist;
    container itemAdapter;


    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Singleton.Scontext = this;
        //변수 초기화
        rv_mainlist = findViewById(R.id.id_rv_mainlist);
        itemlist = new ArrayList<>();
        FloatingActionButton fab_createnote = findViewById(R.id.fab_createnote);
		/*
		DB에서 데이터를 불러옴
		ID
		타이틀
		컨텐츠
		이미지들
		 */
        //디비 생성 오픈
        Singleton.getInstance().mDbOpenHelper = new DbOpenHelper(this);
        Singleton.getInstance().mDbOpenHelper.open();

//        Singleton.getInstance().mDbOpenHelper.insertColumn("제발","오제발","20200224094122.jpg");
//        Singleton.getInstance().mDbOpenHelper.insertColumn("제발","오제발","20200224095310.jpg");

        //디비를 커서로 변화시켜서 릴레이티브뷰에 넣게 쉽게 변환
        datatocursor();

        itemAdapter = new container(itemlist);
        rv_mainlist.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv_mainlist.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();

        fab_createnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Create_Note.class);
                intent.putExtra("create_mode","create");
                startActivityForResult(intent, Singleton.potion_plag_createnote);
//				Snackbar.make(view, itemlist.get(0).getTitle(), Snackbar.LENGTH_LONG)
//						.setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void datatocursor() {
        mCursor = null;
        mCursor = Singleton.getInstance().mDbOpenHelper.getAllColumns();
        itemlist.clear();
        while (mCursor.moveToNext()) {
            itemlist.add(new note_item(mCursor.getInt(mCursor.getColumnIndex("_id")),
                    mCursor.getString(mCursor.getColumnIndex("TITLE")),
                    mCursor.getString(mCursor.getColumnIndex("CONTENTS")),
                    mCursor.getString(mCursor.getColumnIndex("IMAGES"))));
        }
        mCursor.close();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case Singleton.potion_plag_createnote:
            case Singleton.potion_plag_readnote:
                if (resultCode == RESULT_OK) {
                    datatocursor();
                    itemAdapter.notifyDataSetChanged();
                    tools.log("됨??");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Singleton.getInstance().mDbOpenHelper.close();
        super.onDestroy();
    }
}
