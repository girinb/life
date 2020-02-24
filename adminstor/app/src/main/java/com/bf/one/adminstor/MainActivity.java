package com.bf.one.adminstor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.widget.ProgressBar;

import com.bf.one.adminstor.ftp_util.ConnectFTP;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static Context mContext;
    public static final String WIFI_STATE = "WIFE";
    public static final String MOBILE_STATE = "MOBILE";
    public static final String NONE_STATE = "NONE";
    public static final String FTP_WORKS = "/works/";
    public static final String FTP_NOTICE = "/공지/";

    //공용 변수
    Message msg;
    ProgressBar mProgress;

    //컨테이너 변수
    container itemAdapter;

    //FTP클래스
    private ConnectFTP ConnectFTP;


    //각종 리스트
    private ArrayList<slot> myList = new ArrayList<>();

    //리스트뷰
    private RecyclerView recyclerView_doclist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //선언 초기화
        mContext = this;
        myList.clear();


        //링크
        recyclerView_doclist = findViewById(R.id.recyclerView_doclist);
        mProgress = findViewById(R.id.progressBar_loading);


        //연결부
        //워킹 리스트
        itemAdapter = new container(myList);
        recyclerView_doclist.setAdapter(itemAdapter);
        recyclerView_doclist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                ftpallsync
            }
        });

        //앱 초기화 구동부
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (getWhatKindOfNetwork(mContext) == NONE_STATE) {
                    Snackbar.make(findViewById(R.id.recyclerView_doclist), "인터넷이 연결되어 있지 않습니다.. \n인터넷을 연결해주세요.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

                // 작업 폴더 만들기
                File dir = new File(MainActivity.mContext.getCacheDir(), FTP_WORKS);

                if (!dir.mkdirs()) {
                    Log.e("FILE", "폴더 생성 완료1");
                } else {
                    Log.e("FILE", "폴더 생성 실패1");
                }

                //파일 3일이 지나면 삭제
                Calendar cal = Calendar.getInstance();
                long todayMil = cal.getTimeInMillis(); // 현재 시간(밀리 세컨드)
                long oneDayMil = 24 * 60 * 60 * 1000; // 일 단위
                Calendar fileCal = Calendar.getInstance();
                Date fileDate = null;
                File path = new File(MainActivity.mContext.getCacheDir() + FTP_WORKS);
                File[] list = path.listFiles(); // 파일 리스트 가져오기
                for (int j = 0; j < list.length; j++) {
                    // 파일의 마지막 수정시간 가져오기
                    fileDate = new Date(list[j].lastModified());
                    // 현재시간과 파일 수정시간 시간차 계산(단위 : 밀리 세컨드)
                    fileCal.setTime(fileDate);
                    long diffMil = todayMil - fileCal.getTimeInMillis();
                    //날짜로 계산
                    int diffDay = (int) (diffMil / oneDayMil);
                    // 3일이 지난 파일 삭제
                    if (diffDay > 1 && list[j].exists()) {
                        list[j].delete();
                    }
                }
                //FTP동기화부
                //1 서버 초기화
                ConnectFTP = new ConnectFTP();
                ConnectFTP.ftpinit();
                boolean status = false;
                status = ConnectFTP.ftpConnect();

                if (status == true) {
                    Log.w("서버", "Connection Success");
                    String[] files = ConnectFTP.ftpGetFileList(FTP_WORKS);
                    //3. xls 무조건 동기화
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].substring(files[i].lastIndexOf('.')).equals(".xls")) {
                            String temp_str = files[i].replace("(File) ", "");
                            myList.add(new slot(temp_str.substring(0, temp_str.lastIndexOf("."))));
                            ConnectFTP.ftpallsync(files[i].replace("(File) ", ""), FTP_WORKS);
                        }
                    }
                    msg = Message.obtain();
                    msg.what = 0;
                    handler.sendMessage(msg);
                } else {
                    Log.w("서버", "Connection failed");
                    Snackbar.make(findViewById(R.id.recyclerView_doclist), "서버가 점검 중입니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    msg = Message.obtain();
                    msg.what = 4;
                    handler.sendMessage(msg);
                }
                ConnectFTP.ftpDisconnect();
            }
        }).start();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
//                    case R.id.imageView_bt1:
//                        msg = Message.obtain();
//                        msg.what = 2;
//                        handler.sendMessage(msg);
//                        break;
//                    case R.id.imageView_bt2:
//                        msg = Message.obtain();
//                        msg.what = 1;
//                        handler.sendMessage(msg);
//                        break;
//                    case R.id.imageView_bt3:
//                        msg = Message.obtain();
//                        msg.what = 5;
//                        handler.sendMessage(msg);
//                        break;
//                    case R.id.people_bt:
//                        Intent intent = new Intent(MainActivity.this, people.class);
//                        startActivity(intent);
//                        break;
                }
            }
        };
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:// 로딩 후 기본 구동
                    mProgress.clearAnimation();
                    mProgress.setVisibility(View.GONE);
                    itemAdapter.notifyDataSetChanged();
//                    LL_bts.setVisibility(View.VISIBLE);
                    break;
                case 1: //검색창 구동
//                    RV_itemlist.setVisibility(View.VISIBLE);
//                    LL_notice.setVisibility(View.INVISIBLE);
//                    info.setVisibility(View.INVISIBLE);
                    break;
                case 2: //공지창 구동
//                    RV_itemlist.setVisibility(View.INVISIBLE);
//                    LL_notice.setVisibility(View.VISIBLE);
//                    info.setVisibility(View.VISIBLE);
//                    itemAdapter_notice.notifyDataSetChanged();
                    break;
                case 3:
                    finish();
                    break;
                case 4:
//                    mProgress.clearAnimation();
//                    mProgress.setVisibility(View.GONE);
//                    server_notice.setVisibility(View.VISIBLE);
                    break;
                case 5:
//                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    long backKeyClickTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyClickTime + 2000) {
            backKeyClickTime = System.currentTimeMillis();
            Snackbar.make(findViewById(R.id.fab), "뒤로 가기 버튼을 한 번 더 누르면 종료됩니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyClickTime + 2000) {
            finish();
        }
    }

    public static String getWhatKindOfNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return WIFI_STATE;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return MOBILE_STATE;
            }
        }
        return NONE_STATE;
    }
}
