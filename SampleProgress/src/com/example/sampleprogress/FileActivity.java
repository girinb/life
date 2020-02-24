package com.example.sampleprogress;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FileActivity extends Activity implements OnClickListener {

	private Button mBtnFileDown;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file);
		
		// 버튼 객체 설정 
		mBtnFileDown = (Button)findViewById(R.id.button1);

		// 클릭이벤트 설정 
		mBtnFileDown.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			startDownload();
			break;
	
		default:
			break;
		}
	}
	private void startDownload() {
	    String url = "http://cfs11.blog.daum.net/image/5/blog/2008/08/22/18/15/48ae83c8edc9d&filename=DSC04470.JPG";
	    new DownloadFileAsync(this).execute(url, "1", "1");
	}
}
