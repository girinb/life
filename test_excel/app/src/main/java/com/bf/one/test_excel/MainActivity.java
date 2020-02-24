package com.bf.one.test_excel;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {
    Row row;
    Cell cell;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        exte();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
//                Snackbar.make(view, , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                try {

                    FileInputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/통합 문서2.xlsx");     //파일경로
//                    Workbook workbook = WorkbookFactory.create(inputStream);

                    OPCPackage pkg = OPCPackage.open(inputStream);
                    Workbook workbook = new XSSFWorkbook(pkg);
                    //sheet수 취득


                    int sheetCn = workbook.getNumberOfSheets();
                    sheetCn = 1;  //일단 1번 sheet만
                    for (int cn = 0; cn < sheetCn; cn++) {
                        Log.w("엑셀로그", "취득하는 sheet 이름 : " + workbook.getSheetName(cn));
                        Log.w("엑셀로그", workbook.getSheetName(cn) + " sheet 데이터 취득 시작");
                        //0번째 sheet 정보 취득
                        Sheet sheet = workbook.getSheetAt(cn);

                        //취득된 sheet에서 rows수 취득
                        int rows = sheet.getPhysicalNumberOfRows();
                        Log.w("엑셀로그", workbook.getSheetName(cn) + " sheet의 row수 : " + rows);

                        //취득된 row에서 취득대상 cell수 취득
                        int cells = sheet.getRow(cn).getPhysicalNumberOfCells(); //
                        Log.w("엑셀로그", workbook.getSheetName(cn) + " sheet의 row에 취득대상 cell수 : " + cells);

                        for (int r = 0; r < rows; r++) {
                            row = sheet.getRow(r); // row 가져오기
                            if (row != null) {
                                for (int c = 0; c < cells; c++) {
                                    cell = row.getCell(c);
                                    if (cell != null) {
                                        String value = null;
                                        switch (cell.getCellType()) {
                                            case Cell.CELL_TYPE_FORMULA:
                                                value = cell.getCellFormula();
                                                break;
                                            case Cell.CELL_TYPE_NUMERIC:
                                                value = "" + cell.getNumericCellValue();
                                                break;
                                            case Cell.CELL_TYPE_STRING:
                                                value = "" + cell.getStringCellValue();
                                                break;
                                            case Cell.CELL_TYPE_BLANK:
                                                value = "[null 아닌 공백]";
                                                break;
                                            case Cell.CELL_TYPE_ERROR:
                                                value = "" + cell.getErrorCellValue();
                                                break;
                                            default:
                                        }
                                        Log.w("결과값", value + ":\t");
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
//                    Snackbar.make(view, e.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Log.w("엑셀로그", e + "");
                }
            }
        });

    }

    void exte() {
        try {

            FileInputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/통합 문서2.xlsx");     //파일경로
//                    Workbook workbook = WorkbookFactory.create(inputStream);

//                    OPCPackage pkg = OPCPackage.open
            Workbook workbook = new XSSFWorkbook(inputStream);
            //sheet수 취득


            int sheetCn = workbook.getNumberOfSheets();
            sheetCn = 1;  //일단 1번 sheet만
            for (int cn = 0; cn < sheetCn; cn++) {
                Log.w("엑셀로그", "취득하는 sheet 이름 : " + workbook.getSheetName(cn));
                Log.w("엑셀로그", workbook.getSheetName(cn) + " sheet 데이터 취득 시작");
                //0번째 sheet 정보 취득
                Sheet sheet = workbook.getSheetAt(cn);

                //취득된 sheet에서 rows수 취득
                int rows = sheet.getPhysicalNumberOfRows();
                Log.w("엑셀로그", workbook.getSheetName(cn) + " sheet의 row수 : " + rows);

                //취득된 row에서 취득대상 cell수 취득
                int cells = sheet.getRow(cn).getPhysicalNumberOfCells(); //
                Log.w("엑셀로그", workbook.getSheetName(cn) + " sheet의 row에 취득대상 cell수 : " + cells);

                for (int r = 0; r < rows; r++) {
                    row = sheet.getRow(r); // row 가져오기
                    if (row != null) {
                        for (int c = 0; c < cells; c++) {
                            cell = row.getCell(c);
                            if (cell != null) {
                                String value = null;
                                switch (cell.getCellType()) {
                                    case Cell.CELL_TYPE_FORMULA:
                                        value = cell.getCellFormula();
                                        break;
                                    case Cell.CELL_TYPE_NUMERIC:
                                        value = "" + cell.getNumericCellValue();
                                        break;
                                    case Cell.CELL_TYPE_STRING:
                                        value = "" + cell.getStringCellValue();
                                        break;
                                    case Cell.CELL_TYPE_BLANK:
                                        value = "[null 아닌 공백]";
                                        break;
                                    case Cell.CELL_TYPE_ERROR:
                                        value = "" + cell.getErrorCellValue();
                                        break;
                                    default:
                                }
                                Log.w("결과값", value + ":\t");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
//                    Snackbar.make(view, e.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Log.w("엑셀로그", e + "");
        }


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
