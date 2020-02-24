package com.bf.one.test_loadimg;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private File file;
    private ArrayList<slot> myList = new ArrayList<>();
    private RecyclerView RV_itemlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RV_itemlist = findViewById(R.id.Rvcontain);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                File dir = new File(Environment.getExternalStoragePublicDirectory(
//
//                        Environment.DIRECTORY_DOWNLOADS), "testDir");
//
//                if (!dir.mkdirs()) {
//                    Log.e("FILE", "Directory not created");
//                }else{
//                    Snackbar.make(view, "폴더 생섬됨", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//
//                }
//                PackageManager m = getPackageManager();
//                String path = getPackageName();
//
//                try {
//
//                    PackageInfo p = m.getPackageInfo(path, 0);
//
//                    path = p.applicationInfo.dataDir;
//                    File dir = new File(path, "/testDir");
//                    if (!dir.mkdirs()) {
//                    Log.e("FILE", "Directory not created");
//                }else{
//                    Snackbar.make(view, "폴더 생섬됨", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//
//                }
//                } catch (PackageManager.NameNotFoundException e) {
//
//                }

//                cache.Write(str);
//                String text = "";
//                try {
//                    String obj = "ss";
//
////                    File cacheFile = new File(getCacheDir(), "Cache.txt");
//                    //이건 캐시
//                    File cacheFile = new File(getFilesDir(), "Cache.txt");
//                    //이곤 파일
//
//                    FileWriter fileWriter = new FileWriter(cacheFile);
//                    fileWriter.write(obj);
//                    fileWriter.flush();
//                    fileWriter.close();
//                    //파일 저장
//
//                    FileInputStream inputStream = new FileInputStream(cacheFile);
//                    Scanner s = new Scanner(inputStream);
//
//                    while (s.hasNext()) {
//                        text += s.nextLine();
//                    }
//                    inputStream.close();
//                    //파일 오픈
//                    Log.w("ccc",getFilesDir().toString());
//                }  catch (IOException e){
//                    Log.d("애러?","에러남");
//                }
                String rootSD = Environment.getExternalStorageDirectory().toString();
////                String rootSD = getCacheDir().toString();
                file = new File(rootSD + "/temp_folder");
//                file.mkdir();
//                ArrayList<String> filler = new ArrayList<>();
//                filler.add(".txt");
//                filler.add(".xls");
//                filler.add(".png");
                File list[] = file.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".xls");
                    }
                });
                for(int i = 0; i<list.length; i++)
                {
                    myList.add(new slot(list[i].getName().substring(0,list[i].getName().lastIndexOf('.'))));
                }

                container itemAdapter = new container(myList);
                RV_itemlist.setAdapter(itemAdapter);
                RV_itemlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                //Environment.getExternalStorageDirectory()

//                Snackbar.make(view, getFilesDir().toString(), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

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
}
