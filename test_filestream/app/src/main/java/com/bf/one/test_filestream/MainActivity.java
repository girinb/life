package com.bf.one.test_filestream;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                final StringBuffer sb = new StringBuffer();
                new Thread() {

                    public void run() {

//                        String naverHtml = getNaverHtml();
//
//                        Bundle bun = new Bundle();
//                        bun.putString("HTML_DATA", naverHtml);
//
//                        Message msg = handler.obtainMessage();
//                        msg.setData(bun);
//                        handler.sendMessage(msg);

                        try {
                            URL url = new URL("https://drive.google.com/uc?export=view&id=0B5UTn602gYamNnF5NVRsejljSEU");
                            //https://drive.google.com/uc?export=view&id=
                            //위 주소에 공유 ID를 넣으면 만들수 있다.
                            // 성공한 주소 : https://drive.google.com/uc?export=view&id=0B5UTn602gYamVXFVQXdmYVdZU1E
                            //0B5UTn602gYamNnF5NVRsejljSEU
                            //"https://drive.google.com/drive/u/0/folders/0lB5UTn602gYamaG9KeGZlaWZFRlk"
                            //https://googledrive.com/host/0B5UTn602gYamaG9KeGZlaWZFRlk/index.html
                            //https://docs.google.com/document/d/e/2PACX-1vQcrs-g7XgizrAyUvwRIueF20PGrBED5BSlHa9SpGSe85WEuwGbsDkDNh-d-0S--WnKtOGpNx6phd1t/pub

                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(url.openStream()));

                            String str = null;
                            while ((str = reader.readLine()) != null) {
                                sb.append(str);
                            }
                            Log.w("ddd", sb.toString());
//
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                    mTextMessage.setText(sb.toString());
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }.start();

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
