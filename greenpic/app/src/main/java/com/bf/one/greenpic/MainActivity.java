package com.bf.one.greenpic;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bf.one.greenpic.ftp_util.ConnectFTP;
import com.bf.one.greenpic.util.PermissionListener;
import com.bf.one.greenpic.util.TedPermission;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    public static Context mContext;
    public static int syatem_mode = 0;
    String dir = "/DCIM/100EOS7D";
    String[] photo = {"01054746966", "01056907994", "01028807593", "01076436967"};
    String host = "183.104.107.160";
    //    String host = "girinb.gonetis.com";
    //    String username[] = {"keyhwon","taeng"};
    //    String password[] = {"dlarlghks","xodnd"};
    int port = 21;
    static boolean key = false;

    //0 기본 1 테스트

    //FTP클래스
    private ConnectFTP ConnectFTP;
    ArrayList<String> mList = new ArrayList();
    MyAdapter myAdapter;


    SmsManager smsManager;
    RadioGroup photographers;

    Switch Switch1;
    Switch Switch2;
    Switch Switch3;

    ProgressBar progressBar;
    TextView textView_prog;
    int prog = 0;
    int fail = 0;
    public static int screen_width;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView_mainlist = findViewById(R.id.mainlist);
        myAdapter = new MyAdapter(mList);
        recyclerView_mainlist.setAdapter(myAdapter);
        recyclerView_mainlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Switch1 = findViewById(R.id.switch_photo1);
        Switch2 = findViewById(R.id.switch_photo2);
        Switch3 = findViewById(R.id.switch_photo3);


        photographers = (RadioGroup) findViewById(R.id.radios);
        Button button = findViewById(R.id.button_refresh);
        Button button2 = findViewById(R.id.button_send);

        textView_prog = findViewById(R.id.textView_prog);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(0);
        smsManager = SmsManager.getDefault();

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        screen_width = dm.widthPixels;

        File dir1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/grrenpic_log");
        //디렉토리 폴더가 없으면 생성함
        if (!dir1.exists()) {
            dir1.mkdir();
        }

        testStart();

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Timer timer = new Timer();
//                int delay = 1000;
//                timer.schedule(new TimerTask() {
//                    public void run() {
//                        Intent intent = new Intent(logo.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }, delay);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "권한을 설정해주셔야 앱이 구동합니다.", Toast.LENGTH_SHORT)
                        .show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            finish();
                        } catch (Exception ignored) {

                        }

                    }
                });
            }


        };

        TedPermission.with(MainActivity.this)
                .setPermissionListener(permissionlistener)
                .setRationaleTitle("권한 설정")
                .setRationaleMessage("권한이 필요하니 무조건 쓴다고 누르세요.")
                .setDeniedTitle("Permission denied")
                .setDeniedMessage(
                        "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("설정 하러 가기")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS)
                .check();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.button_refresh:
//                        createNotification("메시지지지");
//기본 스토리지 패턴
                        try {
                            if (syatem_mode == 0) {
                                if (SDCard.getExternalSDCardPath() != null) {
//                        if (Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_folder/" != null) {
                                    mList.clear();
                                    call_message(0);

                                    File file = new File(SDCard.getExternalSDCardPath() + dir);
                                    File file_list[] = file.listFiles();
//                            Toast.makeText(getApplicationContext(), SDCard.getExternalSDCardPath() + dir, Toast.LENGTH_LONG).show();
//                                if (file_list != null) {
                                    for (File aFile_list : file_list) {
//                                if (aFile_list.getName().contains(".jpg"))
                                        mList.add(aFile_list.getName());
                                    }
//                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_folder/");
//                            File file_list[] = file.listFiles();
//                            for (int i = 0; i < file_list.length; i++) {
//                                mList.add(file_list[i].getName());
//                            }
                                    progressBar.setMax(mList.size());

                                    call_message(0);
                                } else {
                                    call_message(0);
                                    mList.clear();
                                    Snackbar.make(view, "카드 꼽아라", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
//                            }
                            } else if (syatem_mode == 1) {

//내 PC\기린비의 V30+\내부 저장소\temp_folder

//                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp_folder/");
//                        File file_list[] =file.listFiles();
//                        for (int i = 0 ; i < file_list.length;i++)
//                        {
//                            mList.add(file_list[i].getName());
//                        }
//                            msg = Message.obtain();
//                            msg.what = 0;
//                            handler.sendMessage(msg);

                                // TODO 확장 스토리지 패턴


//                            if (SDCard.getExternalSDCardPath() != null) {
                                if (Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_folder/" != null) {
                                    mList.clear();

//                                File file = new File(SDCard.getExternalSDCardPath() + dir);
//                                File file_list[] = file.listFiles();
////                            Toast.makeText(getApplicationContext(), SDCard.getExternalSDCardPath() + dir, Toast.LENGTH_LONG).show();
//                                for (File aFile_list : file_list) {
////                                if (aFile_list.getName().contains(".jpg"))
//                                    mList.add(aFile_list.getName());
//                                }
                                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_folder/");
                                    File file_list[] = file.listFiles();
                                    for (int i = 0; i < file_list.length; i++) {
                                        if (file_list[i].getName().contains(".jpg") || file_list[i].getName().contains(".JPG"))
                                            mList.add(file_list[i].getName());
//                                    file_list[i].delete();
                                    }
                                    progressBar.setMax(mList.size());

                                    call_message(0);
                                } else {
                                    mList.clear();
                                    Snackbar.make(view, "카드 꼽아라", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
//내 PC\기린비의 V30+\내부 저장소\temp_folder

//                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp_folder/");
//                        File file_list[] =file.listFiles();
//                        for (int i = 0 ; i < file_list.length;i++)
//                        {
//                            mList.add(file_list[i].getName());
//                        }
//                            msg = Message.obtain();
//                            msg.what = 0;
//                            handler.sendMessage(msg);

                            }
                        } catch (Exception e) {
                            write_log(e.toString());
                        }
                        break;
                    case R.id.button_send:

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                MainActivity.this);
// 제목셋팅
                        alertDialogBuilder.setTitle("이미지 전송하기");

                        alertDialogBuilder
                                .setMessage("전송을 하시겠습니까?")
                                .setCancelable(true)
                                .setPositiveButton("전송",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog, int id) {
                                                call_message(0);

                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //FTP동기화부
                                                        //1 서버 초기화


                                                        ConnectFTP = new ConnectFTP();
                                                        ConnectFTP.ftpinit();
                                                        boolean status = false;
//
//                                                        SharedPreferences pref = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);

//                                                        status = ConnectFTP.ftpConnect(host, pref.getString("id", "kknd"), pref.getString("password", "kknd"), port);
                                                        status = ConnectFTP.ftpConnect(host, "greenpic2", "1945", port);
                                                        String stemp = new String();
                                                        if (photographers.getCheckedRadioButtonId() == R.id.radioButton) {
                                                            long now = System.currentTimeMillis();
                                                            Date date = new Date(now);
                                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
//                                                            stemp += "A side " + editText_substring.getText() + " " + sdf.format(date);
                                                            stemp += "임기환" + sdf.format(date);
                                                        } else if (photographers.getCheckedRadioButtonId() == R.id.radioButton2) {
                                                            long now = System.currentTimeMillis();
                                                            Date date = new Date(now);
                                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
//                                                            stemp += "B side " + editText_substring.getText() + " " + sdf.format(date);
                                                            stemp += "허태웅" + sdf.format(date);
                                                        }

                                                        if (Switch1.isChecked())
                                                            smsManager.sendTextMessage(photo[0], null, stemp + "전송시작", null, null);
                                                        if (Switch2.isChecked())
                                                            smsManager.sendTextMessage(photo[1], null, stemp + "전송시작", null, null);
                                                        if (Switch3.isChecked())
                                                            smsManager.sendTextMessage(photo[2], null, stemp + "전송시작", null, null);
                                                        smsManager.sendTextMessage(photo[3], null, stemp + "전송시작", null, null);
                                                        ConnectFTP.ftpCreateDirectory(stemp);
                                                        if (mList.size() > 0) {
                                                            try {
                                                                if (syatem_mode == 0) {
                                                                    progressBar.setProgress(0);
                                                                    for (int i = 0; i < mList.size(); i++) {
                                                                        if (ConnectFTP.ftpUploadFile(SDCard.getExternalSDCardPath() + dir + "/" + mList.get(i), mList.get(i), stemp + "/")) {
                                                                            progressBar.setProgress(i + 1);
                                                                            File tempfile = new File(SDCard.getExternalSDCardPath() + dir + "/" + mList.get(i));
//                                                                            tempfile.delete();
                                                                            call_message(1);
                                                                        } else {
//                                                                            Toast.makeText(getApplicationContext(), mList.get(i) + "업로드 실패" + fail + "실패 누적", Toast.LENGTH_LONG).show();
//                                                                            write_log(mList.get(i));
                                                                            call_message(3);
                                                                        }

                                                                    }
                                                                } else if (syatem_mode == 1) {
                                                                    progressBar.setProgress(0);
                                                                    for (int i = 0; i < mList.size(); i++) {
                                                                        if (ConnectFTP.ftpUploadFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_folder/" + mList.get(i), mList.get(i), stemp + "/")) {
                                                                            progressBar.setProgress(i + 1);
                                                                            call_message(1);
                                                                        } else {
//                                                                            Toast.makeText(getApplicationContext(), mList.get(i) + "업로드 실패" + fail + "실패 누적", Toast.LENGTH_LONG).show();
//                                                                            write_log(mList.get(i));
                                                                            call_message(3);
                                                                        }

                                                                    }
                                                                }

//                                                                for (int i = 0; i < mList.size(); i++) {
//                                                                    File file = new File(SDCard.getExternalSDCardPath() + dir + "/" + mList.get(i));
//                                                                    boolean deleted = file.delete();
//                                                                    smsManager = SmsManager.getDefault();
//                                                                    smsManager.sendTextMessage("01028807593", null, deleted+"", null, null);
//                                                                }
////                                                                    File file = new File();
//                                                                    File photoLcl = new File(SDCard.getExternalSDCardPath() + dir + "/" + mList.get(i));
//                                                                    Uri imageUriLcl = FileProvider.getUriForFile(MainActivity.this,
//                                                                            getApplicationContext().getPackageName() +
//                                                                                    ".provider", photoLcl);
//                                                                    ContentResolver contentResolver = getContentResolver();
//                                                                    contentResolver.delete(imageUriLcl, null, null);
//
////                                                                    file.delete();
//                                                                    progressBar.setProgress(i + 1);
//                                                                    msg = Message.obtain();
//                                                                    msg.what = 0;
//                                                                    handler.sendMessage(msg);
//                                                                }
                                                                if (Switch1.isChecked())
                                                                    smsManager.sendTextMessage(photo[0], null, stemp + "총" + mList.size() + "개중" + fail + "실패" + "카트 사진 전송 완료", null, null);
                                                                if (Switch2.isChecked())
                                                                    smsManager.sendTextMessage(photo[1], null, stemp + "총" + mList.size() + "개중" + fail + "실패" + "카트 사진 전송 완료", null, null);
                                                                if (Switch3.isChecked())
                                                                    smsManager.sendTextMessage(photo[2], null, stemp + "총" + mList.size() + "개중" + fail + "실패" + "카트 사진 전송 완료", null, null);
                                                                smsManager.sendTextMessage(photo[3], null, stemp + "카트 사진 전송 완료", null, null);
                                                                call_message(0);
                                                                call_message(2);

                                                                ConnectFTP.ftpDisconnect();

                                                            } catch (
                                                                    Exception e) {
                                                                smsManager = SmsManager.getDefault();
                                                                if (Switch1.isChecked())
                                                                    smsManager.sendTextMessage(photo[0], null, stemp + "카트 사진 전송 실패", null, null);
                                                                if (Switch2.isChecked())
                                                                    smsManager.sendTextMessage(photo[1], null, stemp + "카트 사진 전송 실패", null, null);
                                                                if (Switch3.isChecked())
                                                                    smsManager.sendTextMessage(photo[2], null, stemp + "카트 사진 전송 실패", null, null);
                                                                smsManager.sendTextMessage(photo[3], null, stemp + "카트 사진 전송 실패", null, null);
                                                                smsManager.sendTextMessage("01028807593", null, e.toString(), null, null);
                                                                write_log(e.toString());
                                                                createNotification(34, "전솔 실패");

                                                            }
                                                        }


                                                    }

                                                }).start();
                                                call_message(0);
                                            }
                                        })
                                .
                                        setNegativeButton("취소",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                        // 다이얼로그를 취소한다
                                                        dialog.cancel();
                                                    }
                                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();


                        // 다이얼로그 생성


                        // 다이얼로그 보여주기
                        break;
//                    case R.id.imageView_bt3:
//                        msg = Message.obtain();
//                        msg.what = 5;
//                        handler.sendMessage(msg);
//                        break;

                }
            }
        };

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton) {

                } else if (checkedId == R.id.radioButton2) {

                }


            }
        };
        photographers.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        button.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);

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
            final ConstraintLayout linear = (ConstraintLayout) View.inflate(this, R.layout.login, null);
            new AlertDialog.Builder(this)
                    .setTitle("로그인 정보를 입력하시오.")
                    .setIcon(R.mipmap.logo)
                    .setView(linear)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            EditText login_id = (EditText) linear.findViewById(R.id.editText_login_id);
                            EditText login_password = (EditText) linear.findViewById(R.id.editText2_login_password);
                            SharedPreferences pref = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);

// SharedPreferences 의 데이터를 저장/편집 하기위해 Editor 변수를 선언한다.
                            SharedPreferences.Editor editor = pref.edit();

// key값에 value값을 저장한다.
// String, boolean, int, float, long 값 모두 저장가능하다.
                            editor.putString("id", login_id.getText().toString());
                            editor.putString("password", login_password.getText().toString());
// 메모리에 있는 데이터를 저장장치에 저장한다.
                            editor.commit();

//                            pref.getString("id","kknd");
//                            pref.getString("password","kknd");
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }

                    })
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:// 로딩 후 기본 구동
                    myAdapter.notifyDataSetChanged();
                    textView_prog.setText("0/" + mList.size() + "/0");
                    prog = 0;
                    fail = 0;


/*//                    if (progressBar.getMax() > 0)
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //FTP동기화부
//                                //1 서버 초기화
//                                ConnectFTP = new ConnectFTP();
//                                ConnectFTP.ftpinit();
//                                boolean status = false;
////
////                                                        SharedPreferences pref = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
//
////                                                        status = ConnectFTP.ftpConnect(host, pref.getString("id", "kknd"), pref.getString("password", "kknd"), port);
//                                status = ConnectFTP.ftpConnect(host, "test_id", "1945", port);
//                                String stemp = new String();
//                                if (photographers.getCheckedRadioButtonId() == R.id.radioButton) {
//                                    long now = System.currentTimeMillis();
//                                    Date date = new Date(now);
//                                    SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
////                                                            stemp += "A side " + editText_substring.getText() + " " + sdf.format(date);
//                                    stemp += "A side " + sdf.format(date);
//                                } else if (photographers.getCheckedRadioButtonId() == R.id.radioButton2) {
//                                    long now = System.currentTimeMillis();
//                                    Date date = new Date(now);
//                                    SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
////                                                            stemp += "B side " + editText_substring.getText() + " " + sdf.format(date);
//                                    stemp += "B side " + sdf.format(date);
//                                }
//
//                                ConnectFTP.ftpCreateDirectory(stemp);
//                                if (mList.size() > 0) {
//                                    try {
//                                        if (syatem_mode == 0) {
//                                            progressBar.setProgress(0);
//                                            for (int i = 0; i < mList.size(); i++) {
//                                                if (ConnectFTP.ftpUploadFile(SDCard.getExternalSDCardPath() + dir + "/" + mList.get(i), mList.get(i), stemp + "/")) {
//                                                    progressBar.setProgress(i + 1);
//                                                    File tempfile = new File(SDCard.getExternalSDCardPath() + dir + "/" + mList.get(i));
//                                                    tempfile.delete();
//                                                    call_message(1);
//                                                } else {
////                                                                            Toast.makeText(getApplicationContext(), mList.get(i) + "업로드 실패" + fail + "실패 누적", Toast.LENGTH_LONG).show();
//                                                    write_log(mList.get(i));
//                                                    call_message(3);
//                                                }
//
//                                            }
//                                        } else if (syatem_mode == 1) {
//                                            progressBar.setProgress(0);
//                                            for (int i = 0; i < mList.size(); i++) {
//                                                if (ConnectFTP.ftpUploadFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_folder/" + mList.get(i), mList.get(i), stemp + "/")) {
//                                                    progressBar.setProgress(i + 1);
//                                                    call_message(1);
//                                                } else {
////                                                                            Toast.makeText(getApplicationContext(), mList.get(i) + "업로드 실패" + fail + "실패 누적", Toast.LENGTH_LONG).show();
//                                                    write_log(mList.get(i));
//                                                    call_message(3);
//                                                }
//
//                                            }
//                                        }
//
////                                                                for (int i = 0; i < mList.size(); i++) {
////                                                                    File file = new File(SDCard.getExternalSDCardPath() + dir + "/" + mList.get(i));
////                                                                    boolean deleted = file.delete();
////                                                                    smsManager = SmsManager.getDefault();
////                                                                    smsManager.sendTextMessage("01028807593", null, deleted+"", null, null);
////                                                                }
//////                                                                    File file = new File();
////                                                                    File photoLcl = new File(SDCard.getExternalSDCardPath() + dir + "/" + mList.get(i));
////                                                                    Uri imageUriLcl = FileProvider.getUriForFile(MainActivity.this,
////                                                                            getApplicationContext().getPackageName() +
////                                                                                    ".provider", photoLcl);
////                                                                    ContentResolver contentResolver = getContentResolver();
////                                                                    contentResolver.delete(imageUriLcl, null, null);
////
//////                                                                    file.delete();
////                                                                    progressBar.setProgress(i + 1);
////                                                                    msg = Message.obtain();
////                                                                    msg.what = 0;
////                                                                    handler.sendMessage(msg);
////                                                                }
//                                        if (Switch1.isChecked())
//                                            smsManager.sendTextMessage(photo[0], null, "카트 사진 전송 완료", null, null);
//                                        if (Switch2.isChecked())
//                                            smsManager.sendTextMessage(photo[1], null, "카트 사진 전송 완료", null, null);
//                                        if (Switch3.isChecked())
//                                            smsManager.sendTextMessage(photo[2], null, "카트 사진 전송 완료", null, null);
//                                        call_message(0);
//
//                                        call_message(2);
//
//                                        ConnectFTP.ftpDisconnect();
//
//                                    } catch (
//                                            Exception e) {
//                                        smsManager = SmsManager.getDefault();
//                                        smsManager.sendTextMessage("01028807593", null, e.toString(), null, null);
//                                        write_log(e.toString());
//
//                                    }
//                                }
//
//
//                            }
//
//                        }).start();
*/
                    break;
                case 1:// 쓰레드
                    prog++;
                    textView_prog.setText(prog + "/" + mList.size() + "/" + fail);
                    createNotification(2, "진행상황 : " + prog + "/" + mList.size() + " 실패 : " + fail);
                    break;
                case 2:// 전송 완료
                    if (fail <= 0) {
                        createNotification(3, "전송 완료");

                        AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
                                MainActivity.this);
// 제목셋팅
                        alertDialogBuilder2.setTitle("이미지 전송 완료");

                        alertDialogBuilder2
                                .setMessage("전송을 완료했습니다")
                                .setCancelable(true)
                                .

                                        setNegativeButton("확인",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                        // 다이얼로그를 취소한다
                                                        mList.clear();
                                                        prog = 0;
                                                        progressBar.setProgress(0);
                                                        myAdapter.notifyDataSetChanged();
                                                        dialog.cancel();
                                                    }
                                                });
                        AlertDialog alertDialog2 = alertDialogBuilder2.create();
                        alertDialog2.show();
                    }
                    break;
                case 3:// 전송
                    fail++;
                    textView_prog.setText(prog + "/" + mList.size() + "/" + fail);
                    break;
                default:
                    break;
            }
        }
    };
    /* 7초마다 폴더를 확인하고
    확인한 폴더가 존재하면 바로 전송
     */
    private TimerTask second;

    public void testStart() {
        second = new TimerTask() {

            @Override
            public void run() {
                Update();
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 7000);
    }

    protected void Update() {
        Runnable updater = new Runnable() {
            public void run() {
                if (progressBar.getMax() <= 0)
                    if (syatem_mode == 0) {
                        if (SDCard.getExternalSDCardPath() != null) {

//                        if (Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_folder/" != null) {
                            mList.clear();

                            File file = new File(SDCard.getExternalSDCardPath() + dir);
                            File file_list[] = file.listFiles();
//                            if (file_list != null) {

                            if (file_list.length <= 0)
                                key = false;
//                            Toast.makeText(getApplicationContext(), SDCard.getExternalSDCardPath() + dir, Toast.LENGTH_LONG).show();
                            for (File aFile_list : file_list) {
//                                if (aFile_list.getName().contains(".jpg"))
                                mList.add(aFile_list.getName());
                            }
//                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_folder/");
//                            File file_list[] = file.listFiles();
//                            for (int i = 0; i < file_list.length; i++) {
//                                mList.add(file_list[i].getName());
//                            }
                            progressBar.setMax(mList.size());

                            call_message(0);

                        } else {
                            mList.clear();
                            Snackbar.make(textView_prog, "카드 꼽아라", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
//                        }
                    } else if (syatem_mode == 1) {

//내 PC\기린비의 V30+\내부 저장소\temp_folder

//                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp_folder/");
//                        File file_list[] =file.listFiles();
//                        for (int i = 0 ; i < file_list.length;i++)
//                        {
//                            mList.add(file_list[i].getName());
//                        }
//                            msg = Message.obtain();
//                            msg.what = 0;
//                            handler.sendMessage(msg);

                        // TODO 확장 스토리지 패턴


//                            if (SDCard.getExternalSDCardPath() != null) {
                        if (Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_folder/" != null) {
                            mList.clear();

//                                File file = new File(SDCard.getExternalSDCardPath() + dir);
//                                File file_list[] = file.listFiles();
////                            Toast.makeText(getApplicationContext(), SDCard.getExternalSDCardPath() + dir, Toast.LENGTH_LONG).show();
//                                for (File aFile_list : file_list) {
////                                if (aFile_list.getName().contains(".jpg"))
//                                    mList.add(aFile_list.getName());
//                                }
                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_folder/");
                            File file_list[] = file.listFiles();
                            for (int i = 0; i < file_list.length; i++) {
                                if (file_list[i].getName().contains(".jpg") || file_list[i].getName().contains(".JPG"))
                                    mList.add(file_list[i].getName());
//                                    file_list[i].delete();
                            }
                            progressBar.setMax(mList.size());

                            call_message(0);
                        } else {
                            mList.clear();
                            Snackbar.make(textView_prog, "카드 꼽아라", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
//내 PC\기린비의 V30+\내부 저장소\temp_folder

//                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp_folder/");
//                        File file_list[] =file.listFiles();
//                        for (int i = 0 ; i < file_list.length;i++)
//                        {
//                            mList.add(file_list[i].getName());
//                        }
//                            msg = Message.obtain();
//                            msg.what = 0;
//                            handler.sendMessage(msg);

                    }
            }
        };
        handler.post(updater);
    }


    void call_message(int code) {
        Message msg = Message.obtain();
        msg.what = code;
        handler.sendMessage(msg);
    }

    void write_log(String contents) {
        try {

//            if (Environment.getExternalStorageDirectory().getAbsolutePath() + "/grrenpic_log/" != null) {


            String stemp = "";
            //파일 output stream 생성
            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/grrenpic_log" + "/" + "greenpic.txt", true);
            //파일쓰기
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            stemp += now + "--" + contents;
            writer.write("/n" + stemp);
            writer.close();
            fos.close();
            stemp = "";

        } catch (Exception e) {
            if (Switch1.isChecked())
                smsManager.sendTextMessage(photo[0], null, "카트 사진 전송 실패222", null, null);
            if (Switch2.isChecked())
                smsManager.sendTextMessage(photo[1], null, "카트 사진 전송 실패2222", null, null);
            if (Switch3.isChecked())
                smsManager.sendTextMessage(photo[2], null, "카트 사진 전송 실패222", null, null);
            smsManager.sendTextMessage(photo[3], null, "카트 사진 전송 실패2222", null, null);
            e.printStackTrace();

        }
    }

    private void createNotification(int _type, String _message) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("전송 현황");
        builder.setContentText(_message);

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(_type, builder.build());
    }

}

