package com.example.sampleprogress;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class MainActivity extends Activity implements OnClickListener {

	/** 다이얼로그 생성에서 프로그레시브바를 구분하기 위한 인자 */
	/*private static final int PROGRESSBAR_DLG_LARGE = 1;
	private static final int PROGRESSBAR_DLG_MID = 2;
	private static final int PROGRESSBAR_DLG_SMALL = 3;
	private static final int PROGRESSBAR_DLG_STATIC = 4;
	private static final int PROGRESSBAR_DLG_SPINNER = 5;*/

	private Button mBtnProgressDlg;
	private Button mBtnSpinner;
	private Button mBtnLarge;
	private Button mBtnMid;
	private Button mBtnSmall;
	private Button mBtnStick;
	
	private AsyncTask<Integer, String, Integer> mProgressDlg;
	private ProgressBar mProgressLarge;
	private ProgressBar mProgressMid;
	private ProgressBar mProgressSmall;
	private ProgressBar mProgressStick;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 버튼 객체 설정 
		mBtnProgressDlg = (Button) findViewById(R.id.btnProgressDialog);
		mBtnLarge = (Button)findViewById(R.id.btnProgressLarge);
		mBtnMid = (Button)findViewById(R.id.btnProgressMid);
		mBtnSmall = (Button)findViewById(R.id.btnProgressSmall);
		mBtnStick = (Button)findViewById(R.id.btnProgressStick);
		
		// 프로그레시브바 설정 
		mProgressLarge = (ProgressBar) findViewById(R.id.progressBar1);
		mProgressMid = (ProgressBar) findViewById(R.id.progressBar2);
		mProgressSmall = (ProgressBar) findViewById(R.id.progressBar3);
		mProgressStick = (ProgressBar) findViewById(R.id.progressBar4);

		// 클릭이벤트 설정 
		mBtnProgressDlg.setOnClickListener(this);
		mBtnLarge.setOnClickListener(this); // 진행바 큰것
		mBtnMid.setOnClickListener(this); // 진행바 중간
		mBtnSmall.setOnClickListener(this);// 진행바 작은것
		mBtnStick.setOnClickListener(this); // 막대형 진행바
		
		// 진행바를 숨긴다
		mProgressLarge.setVisibility(ProgressBar.GONE);
		mProgressMid.setVisibility(ProgressBar.GONE); // 진행바 중간
		mProgressSmall.setVisibility(ProgressBar.GONE);// 진행바 작은것
		mProgressStick.setVisibility(ProgressBar.GONE); // 막대형 진행바
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnProgressDialog:
			mProgressDlg = new ProgressDlgSample(MainActivity.this).execute(100);
			break;
			
		case R.id.btnProgressLarge:
			mProgressLarge.setVisibility(ProgressBar.VISIBLE);
			mProgressLarge.setIndeterminate(true);
			mProgressLarge.setMax(100);
	
			break;
			
		case R.id.btnProgressMid:
			mProgressMid.setVisibility(ProgressBar.VISIBLE);
			mProgressMid.setIndeterminate(true);
			mProgressMid.setMax(100);
			break;
			
		case R.id.btnProgressSmall:
			mProgressSmall.setVisibility(ProgressBar.VISIBLE);
			mProgressSmall.setIndeterminate(true);
			mProgressSmall.setMax(100);
			break;
			
		case R.id.btnProgressStick:
			mProgressStick.setVisibility(ProgressBar.VISIBLE);
			mProgressStick.setIndeterminate(true);
			mProgressStick.setMax(100);
			break;
	
		default:
			break;
		}
	}
}
