package com.bf.one.greenpic.util;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

/**
 * Created by TedPark on 16. 2. 21..
 */
public class NormalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(NormalActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(NormalActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT)
                        .show();
            }


        };


        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleTitle("타이틀임")
//        .setRationaleMessage(R.string.rationale_message)
                .setRationaleMessage("이런 이런 권한이 필요합니다 불리ㅏ불라불라 정말 피요하ㅣ비ㅏ\n줄바궈짐??\n??>??\n????\n???\n\n?????")
                .setDeniedTitle("Permission denied")
                .setDeniedMessage(
                        "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("bla bla")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.READ_PHONE_STATE)
                .check();
//                    Intent intent=new Intent(this, MainActivity.class);
//                startActivity(intent);
//                finish();
    }

}
