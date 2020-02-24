package com.bf.one.ftp_download;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ConnectFTP ConnectFTP;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        ConnectFTP = new ConnectFTP();
        ConnectFTP.ftpinit();



        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "연결값?", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean status = false;
                        status = ConnectFTP.ftpConnect();
                        if (status == true) {
                            Log.w("서버", "Connection Success");
                        } else {
                            Log.w("서버", "Connection failed");
                        }

                    }
                }).start();

            }
        });

        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "연결값?", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Log.w("시간",System.currentTimeMillis()+"");
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        String[] files = ConnectFTP.ftpGetFileList("");
                        for (int i = 0; i < files.length; i++) {
                            Log.w("asd", files[i].substring(files[i].lastIndexOf('.')));
                            ConnectFTP.ftpsync(files[i].replace("(File) ", ""));

//                            Log.w("tlqkf",ftp_address+"/"+files[i].replace("(File) ", ""));
//                          ConnectFTP.ftpDownloadFile("ftp://girinb.gonetis.com/111/aas.xls", Environment.getExternalStorageDirectory()+"/temp_folder/1"+files[i]);
//
                            String rootSD = getCacheDir().toString();
                            File file = new File(rootSD);
                            File list[] = file.listFiles();
                            for (int j = 0; j < list.length; j++) {
//                                Log.w("파일있냐?", list[j].getName());
                            }
                        }
                    }
                }).start();
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
