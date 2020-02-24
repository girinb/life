package com.bf.one.adminstor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bf.one.adminstor.util.PermissionListener;
import com.bf.one.adminstor.util.TedPermission;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class logo extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.logo);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Timer timer = new Timer();
                int delay = 1000;
                timer.schedule(new TimerTask() {
                    public void run() {
                        Intent intent = new Intent(logo.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, delay);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(logo.this, "권한을 설정해주셔야 앱이 구동합니다.", Toast.LENGTH_SHORT)
                        .show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            finish();
                        } catch (Exception e) {

                        }

                    }
                });
            }


        };

        TedPermission.with(logo.this)
                .setPermissionListener(permissionlistener)
                .setRationaleTitle(R.string.rationale_title)
//        .setRationaleMessage(R.string.rationale_message)
                .setRationaleMessage("이런 이런 권한이 필요합니다 불리ㅏ불라불라 정말 피요하ㅣ비ㅏ\n줄바궈짐??\n??>??\n????\n???\n\n?????")
                .setDeniedTitle("Permission denied")
                .setDeniedMessage(
                        "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("설정 하러 가기")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS)
                .check();
    }

}
