package com.bf.one.sell_project;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bf.one.sell_project.util.NormalActivity;
import com.bf.one.sell_project.util.PermissionListener;
import com.bf.one.sell_project.util.TedPermission;

import java.util.List;

public class logo extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);

        ImageView imgv = findViewById(R.id.imageView_logo);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(logo.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
//                Toast.makeText(logo.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT)
//                        .show();
            }


        };


        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleTitle(R.string.rationale_title)
//        .setRationaleMessage(R.string.rationale_message)
                .setRationaleMessage("이런 이런 권한이 필요합니다 불리ㅏ불라불라 정말 피요하ㅣ비ㅏ\n줄바궈짐??\n??>??\n????\n???\n\n?????")
                .setDeniedTitle("Permission denied")
                .setDeniedMessage(
                        "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("설정 하러 가기")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(logo.this, NormalActivity.class);
//                if (intent != null) {
//                    startActivity(intent);
//                }
                Intent intent=new Intent(logo.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
