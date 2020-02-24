package com.example.charlie.excelexample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Item> mItems = new ArrayList<>();

    private RecyclerView itemRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        itemRv = findViewById(R.id.mainRv);
        initData();
        setRv();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveExcel();
            }
        });
        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadexcel();
            }
        });
    }

    private void initData() {
        mItems.add(new Item("키위남", 25));
        mItems.add(new Item("강감찬", 30));
        mItems.add(new Item("이순신", 35));
        mItems.add(new Item("홍길동", 40));
        mItems.add(new Item("이성계", 45));
        mItems.add(new Item("아무개", 50));
        mItems.add(new Item("키위남", 25));
        mItems.add(new Item("강감찬", 30));
        mItems.add(new Item("이순신", 35));
        mItems.add(new Item("홍길동", 40));
        mItems.add(new Item("이성계", 45));
        mItems.add(new Item("아무개", 50));
        mItems.add(new Item("키위남", 25));
        mItems.add(new Item("강감찬", 30));
        mItems.add(new Item("이순신", 35));
        mItems.add(new Item("홍길동", 40));
        mItems.add(new Item("이성계", 45));
        mItems.add(new Item("아무개", 50));
        mItems.add(new Item("키위남", 25));
        mItems.add(new Item("강감찬", 30));
        mItems.add(new Item("이순신", 35));
        mItems.add(new Item("홍길동", 40));
        mItems.add(new Item("이성계", 45));
        mItems.add(new Item("아무개", 50));
        mItems.add(new Item("키위남", 25));
        mItems.add(new Item("강감찬", 30));
        mItems.add(new Item("이순신", 35));
        mItems.add(new Item("홍길동", 40));
        mItems.add(new Item("이성계", 45));
        mItems.add(new Item("아무개", 50));
        mItems.add(new Item("키위남", 25));
        mItems.add(new Item("강감찬", 30));
        mItems.add(new Item("이순신", 35));
        mItems.add(new Item("홍길동", 40));
        mItems.add(new Item("이성계", 45));
        mItems.add(new Item("아무개", 50));
    }

    private void setRv() {
        ItemAdapter itemAdapter = new ItemAdapter(mItems);
        itemRv.setAdapter(itemAdapter);
        itemRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }


    private void saveExcel() {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(); // 새로운 시트 생성

        Row row = sheet.createRow(0); // 새로운 행 생성
        Cell cell;

        cell = row.createCell(0); // 1번 셀 생성
        cell.setCellValue("이름"); // 1번 셀 값 입력

        cell = row.createCell(1); // 2번 셀 생성
        cell.setCellValue("나이"); // 2번 셀 값 입

        for (int i = 0; i < mItems.size(); i++) { // 데이터 엑셀에 입력
            row = sheet.createRow(i + 1);
            cell = row.createCell(0);
            cell.setCellValue(mItems.get(i).getName());
            cell = row.createCell(1);
            cell.setCellValue(mItems.get(i).getAge());
        }

        File xlsFile = new File(getExternalFilesDir(null), "/test.xls");
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
        Workbook workbook = null;
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        try {
             workbook = new XSSFWorkbook(new FileInputStream(new File(getExternalFilesDir(null) + "/test.xls")));

            if (workbook != null) {
                Toast.makeText(getApplicationContext(), "파일있음", Toast.LENGTH_SHORT).show();
                sheet = workbook.getSheetAt(0);

                if (sheet != null) {
                    //기록물철의 경우 실제 데이터가 시작되는 Row지정
                    int nRowStartIndex = 1;
                    //기록물철의 경우 실제 데이터가 끝 Row지정
                    int nRowEndIndex = sheet.getLastRowNum();

                    //기록물철의 경우 실제 데이터가 시작되는 Column  지정
                    int nColumnStartIndex = 0;
                    //기록물철의 경우 실제 데이터가 끝나는 Column지정
                    int nColumnEndIndex = sheet.getRow(2).getLastCellNum();

                    String szValue = "";

                    for (int i = nRowStartIndex; i <= nRowEndIndex; i++) {
                        row = sheet.getRow(i);

                        for (int nColumn = nColumnStartIndex; nColumn <= nColumnEndIndex; nColumn++) {
                            cell = row.getCell((short) nColumn);

                            if (cell == null) {
                                continue;
                            }
                            if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                                szValue = String.valueOf(cell.getNumericCellValue());
                            } else {
                                szValue = cell.getStringCellValue();
                            }

//                            System.out.print( szValue);
//                            System.out.print( "\t" );
                            Toast.makeText(getApplicationContext(), szValue.toString(), Toast.LENGTH_SHORT).show();
                        }
//                        System.out.println();
                    }
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

//        Toast.makeText(getApplicationContext(), "로드로드다냥", Toast.LENGTH_SHORT).show();

    }
}
