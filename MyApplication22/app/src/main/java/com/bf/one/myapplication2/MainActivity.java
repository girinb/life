package com.bf.one.myapplication2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<slot> item_list = new ArrayList<>();
    private ArrayList<ArrayList<String>> excel_exfort = new ArrayList<>();
    private RecyclerView RV_itemlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        RV_itemlist = findViewById(R.id.RV_xml_list);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "1번 버튼", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                saveExcel();
            }
        });
        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "2번 버튼", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                loadexcel();
            }
        });

        FloatingActionButton fab3 = findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "3번 버튼", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                setRv();
            }
        });
        FloatingActionButton fab4 = findViewById(R.id.fab4);
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText phone_number  = (EditText)findViewById(R.id.editText);
                EditText sub = (EditText)findViewById(R.id.editText2);
                Snackbar.make(view, "4번 버튼", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendTextMessage(phone_number.getText().toString(), null, sub.getText().toString(), null, null);
//                smsManager.sendTextMessage("01028807593", null, "문자 전송 테스트", null, null);

            }
        });
        FloatingActionButton fab5 = findViewById(R.id.fab5);
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText phone_number  = (EditText)findViewById(R.id.editText);
                EditText sub = (EditText)findViewById(R.id.editText2);
                Snackbar.make(view, "5번 버튼", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                SmsManager smsManager = SmsManager.getDefault();

                for(int i2 = 0; i2 < excel_exfort.size(); i2++){
//                        Toast.makeText(getApplicationContext(), excel_exfort.get(i2).get(0).toString()+"//"+excel_exfort.get(i2).get(1).toString(), Toast.LENGTH_SHORT).show();
                    smsManager.sendTextMessage(excel_exfort.get(i2).get(1).toString(), null, excel_exfort.get(i2).get(0).toString(), null, null);
                }

//                smsManager.sendTextMessage(phone_number.getText().toString(), null, sub.getText().toString(), null, null);
//                smsManager.sendTextMessage("01028807593", null, "문자 전송 테스트", null, null);

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

    private void setRv() {
        item_list.add(new slot("김정기", "01040408567"));
        item_list.add(new slot("김인학", "01028807593"));
        item_list.add(new slot("죄형민", "01054746966"));
        item_list.add(new slot("김갱민", "01051434578"));
//        item_list.add(new slot("5번아무개", 50));
        container itemAdapter = new container(item_list);
        RV_itemlist.setAdapter(itemAdapter);
        RV_itemlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void saveExcel() {
        Workbook workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet(); // 새로운 시트 생성

        Row row = sheet.createRow(0); // 새로운 행 생성
        Cell cell;

        cell = row.createCell(0); // 1번 셀 생성
        cell.setCellValue("이름"); // 1번 셀 값 입력

        cell = row.createCell(1); // 2번 셀 생성
        cell.setCellValue("나이"); // 2번 셀 값 입

        cell = row.createCell(2); // 3번 셀 생성
        cell.setCellValue("비고"); // 3번 셀 값 입

        for (int i = 0; i < item_list.size(); i++) { // 데이터 엑셀에 입력
            row = sheet.createRow(i + 1);
            cell = row.createCell(0);
            cell.setCellValue(item_list.get(i).getName());
            cell = row.createCell(1);
            cell.setCellValue(item_list.get(i).getAge());
        }

        File xlsFile = new File(getExternalFilesDir(null), "test.xls");
        try {
            FileOutputStream os = new FileOutputStream(xlsFile);
            workbook.write(os); // 외부 저장소에 엑셀 파일 생성
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), xlsFile.getAbsolutePath() + "에 저장되었습니다", Toast.LENGTH_SHORT).show();

//        Uri path = Uri.fromFile(xlsFile);
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("application/excel");
//        shareIntent.putExtra(Intent.EXTRA_STREAM,path);
//        startActivity(Intent.createChooser(shareIntent,"엑셀 내보내기"));
    }

    private void loadexcel() {
        HSSFWorkbook workbook = null;
        HSSFSheet sheet = null;
        HSSFRow row = null;
        HSSFCell cell = null;
        excel_exfort.clear();
//        excel_exfort.clear();
        try {
            workbook = new HSSFWorkbook(new FileInputStream(new File(getExternalFilesDir(null) + "/test.xls")));

            if (workbook != null) {
                Toast.makeText(getApplicationContext(), "파일있음", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), workbook.toString(), Toast.LENGTH_SHORT).show();

                sheet = workbook.getSheetAt(0);
                //sheet는 시트 탭인것 같다.

                if (sheet != null) {
                    //기록물철의 경우 실제 데이터가 시작되는 Row지정
                    int nRowStartIndex = 1;
                    //이건 데이터를 처음 읽어오는 행의 순번
                    //0을 넣으면 카테고리까지 읽어들임

                    //기록물철의 경우 실제 데이터가 끝 Row지정
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

                    Toast.makeText(getApplicationContext(), "nColumnEndIndex:" + nColumnEndIndex, Toast.LENGTH_SHORT).show();

                    String szValue = "";

                    for (int i = nRowStartIndex; i <= nRowEndIndex; i++) {
                        row = sheet.getRow(i);
                        ArrayList<String> temp_str = new ArrayList<>();
                        for (int nColumn = nColumnStartIndex; nColumn < nColumnEndIndex; nColumn++) {
                                cell = row.getCell((short) nColumn);

                                if (cell == null) {
//                                    Toast.makeText(getApplicationContext(), "빈칸", Toast.LENGTH_SHORT).show();
                                    continue;
                                }
                                if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                                    szValue = String.valueOf(cell.getNumericCellValue());
//                                    Toast.makeText(getApplicationContext(), szValue.toString(), Toast.LENGTH_SHORT).show();
                                } else {
                                    szValue = cell.getStringCellValue();
                                    temp_str.add(szValue);
//                                    Toast.makeText(getApplicationContext(), szValue.toString(), Toast.LENGTH_SHORT).show();
                                }

//                            System.out.print( szValue);
//                            System.out.print( "\t" );
                        }
                        excel_exfort.add(temp_str);

//                        System.out.println();
                    }
//                    for(int i2 = 0; i2 < excel_exfort.size(); i2++){
//                        Toast.makeText(getApplicationContext(), excel_exfort.get(i2).toString(), Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Sheet is null!!", Toast.LENGTH_SHORT).show();

//                    System.out.println( "Sheet is null!!" );
                }
            } else {
                Toast.makeText(getApplicationContext(), "WorkBook is null!!", Toast.LENGTH_SHORT).show();
//                System.out.println( "WorkBook is null!!" );
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }


    }
}
