package com.bf.one.adminstor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.util.ArrayList;

public class send_page extends Activity {

    static ArrayList<user_slot> search_data_bases = new ArrayList<>();
    ArrayList<user_slot> myList = new ArrayList<>();
    final String[] cell_name = new String[]{"이름", "전화번호"};
    ArrayList<Integer> cell_bus = new ArrayList<>();
    boolean selected_key = false;

    container_users itemAdapter;

    RecyclerView recyclerView_callnumber_list;


    Message msg;

    EditText editText_substring;
    String filename;
    Intent intent_title;
    SmsManager smsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_page_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        recyclerView_callnumber_list = findViewById(R.id.RecyclerView_callnumberlist);


        intent_title = getIntent();
        filename = intent_title.getExtras().getString("title");
        TextView textView_title = findViewById(R.id.textView_title);

        textView_title.setText(filename + " 신청자 리스트");

        final TextView textView_search_board = findViewById(R.id.search_board);

        //워킹 리스트

        itemAdapter = new container_users(myList);
        recyclerView_callnumber_list.setAdapter(itemAdapter);
        recyclerView_callnumber_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadexcel_database(filename + ".xls");
                myList.addAll(search_data_bases);
                msg = Message.obtain();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        }).start();


        textView_search_board.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = textView_search_board.getText().toString();
                search(text);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.bt_all:
                        for (int i = 0; i < myList.size(); i++) {
                            for (int j = 0; j < search_data_bases.size(); j++) {
                                if (myList.get(i).getCallnumber().equals(search_data_bases.get(j).getCallnumber()) && myList.get(i).getName().equals(search_data_bases.get(j).getName())) {
                                    search_data_bases.get(j).setSelected(true);
                                }
                            }
                        }

                        msg = Message.obtain();
                        msg.what = 0;
                        handler.sendMessage(msg);


                        break;
                    case R.id.bt_not_all:  // 체크 됨 전체 해제함
                        for (int i = 0; i < myList.size(); i++) {
                            for (int j = 0; j < search_data_bases.size(); j++) {
                                if (myList.get(i).getCallnumber().equals(search_data_bases.get(j).getCallnumber()) && myList.get(i).getName().equals(search_data_bases.get(j).getName())) {
                                    search_data_bases.get(j).setSelected(false);
                                }
                            }
                        }

                        msg = Message.obtain();
                        msg.what = 0;
                        handler.sendMessage(msg);

                        break;
                    case R.id.button_sms_send:
                        editText_substring = findViewById(R.id.editText_substring);
                        if (editText_substring.equals("")) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    send_page.this);
// 제목셋팅
                            alertDialogBuilder.setTitle("문자 보내기");

                            alertDialogBuilder
                                    .setMessage("문자를 보내시겠습니까?")
                                    .setCancelable(true)
                                    .setPositiveButton("보내기",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {

                                                    smsManager = SmsManager.getDefault();
                                                    for (int i = 0; i < search_data_bases.size(); i++) {
                                                        if (search_data_bases.get(i).getSelected()) {
                                                            smsManager.sendTextMessage(search_data_bases.get(i).getCallnumber(), null, editText_substring.getText().toString(), null, null);
                                                        }
                                                    }
                                                }
                                            })
                                    .setNegativeButton("취소",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    // 다이얼로그를 취소한다
                                                    dialog.cancel();
                                                }
                                            });

                            // 다이얼로그 생성
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // 다이얼로그 보여주기
                            alertDialog.show();
                        } else
                            Snackbar.make(findViewById(R.id.textView_title), "내용을 입력해주세요", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        break;

                    case R.id.bt_eraser:
                        EditText temp = findViewById(R.id.search_board);
                        temp.setText("");
                        break;

                    default:
                        break;
                }
            }
        };
        View bt_selected_control = findViewById(R.id.bt_all);
        bt_selected_control.setOnClickListener(onClickListener);
        View bt_selected_control2 = findViewById(R.id.bt_not_all);
        bt_selected_control2.setOnClickListener(onClickListener);
        Button bt_sms_send = findViewById(R.id.button_sms_send);
        bt_sms_send.setOnClickListener(onClickListener);
        Button bt_eraser = findViewById(R.id.bt_eraser);
        bt_eraser.setOnClickListener(onClickListener);

    }


    long backKeyClickTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyClickTime + 2000) {
            backKeyClickTime = System.currentTimeMillis();
            Snackbar.make(findViewById(R.id.textView_title), "뒤로 가기 버튼을 한번더 누르면 아파트리스트로 돌아갑니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyClickTime + 2000) {
            finish();
        }
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:// 로딩 후 기본 구동
                    itemAdapter.notifyDataSetChanged();
//                    refresh_user_count();
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

                    break;
                case 5:

                    break;
                default:
                    break;
            }
        }
    };

    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        myList.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            myList.addAll(search_data_bases);
        }
        // 문자 입력을 할때..
        else {
            // 리스트의 모든 데이터를 검색한다.
            for (int i = 0; i < search_data_bases.size(); i++) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (search_data_bases.get(i).getName().toLowerCase().contains(charText) || search_data_bases.get(i).getCallnumber().toLowerCase().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    myList.add(search_data_bases.get(i));
//                    Log.w("불값보기",search_data_bases.get(i).getName()+"//"+search_data_bases.get(i).getSelected());
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        itemAdapter.notifyDataSetChanged();
    }

//    void  refresh_user_count() {
//        int count = 0;
//        for (int i = 0; i < search_data_bases.size(); i++) {
//            if (search_data_bases.get(i).getSelected()) {
//                count++;
//            }
//        }
//        TextView user_couint = findViewById(R.id.tv_users);
//        user_couint.setText("현재 선택된 인원수 :" + count);
//
//    }

    //데이터 베이스 로드
    private void loadexcel_database(String filename) {
//        HSSFWorkbook workbook = null;
//        HSSFSheet sheet = null;
//        HSSFRow row = null;
//        HSSFCell cell = null;

        Workbook workbook = null;
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        try {
            workbook = new HSSFWorkbook(new FileInputStream(MainActivity.mContext.getCacheDir() + MainActivity.FTP_WORKS + filename));

            if (workbook != null) {
                sheet = workbook.getSheetAt(0);

                //sheet는 시트 탭인것 같다.
                if (sheet != null) {
                    //기록물철의 경우 실제 데이터가 시작되는 Row지정
                    int nRowStartIndex = 0;
                    //이건 데이터를 z처음 읽어오는 행의 순번
                    //0을 넣으면 카테고리까지 읽어들임
                    //기록물철의 경우 실제 데이터가 끝 Row지정
//                    int nRowEndIndex = sheet.getDefaultRowHeight();
                    int nRowEndIndex = sheet.getLastRowNum();
                    //이건 행의 끝을 체크하는 부분으로 이걸로 줄이 몇줄인지 알수 있음
//                    Toast.makeText(getApplicationContext(), "nRowEndIndex:"+nRowEndIndex, Toast.LENGTH_SHORT).show();
                    //기록물철의 경우 실제 데이터가 시작되는 Column지정
                    int nColumnStartIndex = 0;
                    //이게 각 열에서 첫 값을 불러오는 시작점 값

                    //기록물철의 경우 실제 데이터가 끝나는 Column지정
                    int nColumnEndIndex = sheet.getRow(0).getLastCellNum();
                    //여기서 라우 인덱스가 중요함 행의 길이를 알수 있음
                    //그런데 왜 1줄 더 읽어 들이는걸까???
                    //이건 알아봐야 겠는데????????????????
                    //해결 크거나 같을때 라고 해서 +1 더 들어감
//                    Log.w("로그", "nRowEndIndex" + nRowEndIndex + "//" + "nColumnEndIndex" + nColumnEndIndex);

                    String szValue = "";

                    row = sheet.getRow(0);
                    for (int i = 0; i < cell_name.length; i++) {
                        for (int cell_cout = 0; cell_cout < sheet.getRow(0).getLastCellNum(); cell_cout++) {
                            cell = row.getCell(cell_cout);
                            if (cell == null) {
                                continue;
                            }
                            if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                                szValue = String.valueOf(Math.round(cell.getNumericCellValue()));
                            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                szValue = cell.getStringCellValue();
                            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
                                szValue = cell.getCellFormula();
                            }

                            if (szValue.equals(cell_name[i])) {
                                cell_bus.add(cell_cout);
                            }
                        }
                    }
//                    Log.w("데이터추출값", cell_bus.toString());
                    //데이터 테이블 생성
                    search_data_bases.clear();
                    nRowStartIndex = 1;
                    nColumnStartIndex = 0;
                    nRowEndIndex = sheet.getLastRowNum();
                    nColumnEndIndex = sheet.getRow(0).getLastCellNum();

                    for (int i = nRowStartIndex; i <= nRowEndIndex; i++) {
                        row = sheet.getRow(i);
                        user_slot temp_search_data_base = new user_slot();
                        for (int cell_count = 0; cell_count < cell_bus.size(); cell_count++) {
                            cell = row.getCell(cell_bus.get(cell_count));
                            if (cell == null) {
                                continue;
                            }
                            if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                                szValue = String.valueOf(Math.round(cell.getNumericCellValue()));
                            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                szValue = cell.getStringCellValue();
                            } else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
                                szValue = cell.getCellFormula();
                            }

                            //"이름","전화번호",

                            switch (cell_count) {
                                case 0:
                                    temp_search_data_base.setName(szValue);
                                    break;
                                case 1:
                                    temp_search_data_base.setCallnumber(szValue);
                                    break;
                                default:
                                    break;
                            }
                        }
                        if (temp_search_data_base.getName() != null && temp_search_data_base.getCallnumber() != null) {
                            search_data_bases.add(temp_search_data_base);
                            Log.w("제길", temp_search_data_base.getName());
                        } else Log.w("공백", "공백이다냥?");
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Sheet is null!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "WorkBook is null!!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
