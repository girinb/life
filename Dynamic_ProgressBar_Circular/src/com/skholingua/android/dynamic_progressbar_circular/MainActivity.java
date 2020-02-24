package com.skholingua.android.dynamic_progressbar_circular;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView tv;
	ProgressBar pBar;
	int pStatus = 0;
	private Handler handler = new Handler();
	RelativeLayout rl;
	RelativeLayout.LayoutParams param1, param2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rl = new RelativeLayout(this);

		param1 = new RelativeLayout.LayoutParams(
				(int) RelativeLayout.LayoutParams.WRAP_CONTENT,
				(int) RelativeLayout.LayoutParams.WRAP_CONTENT);

		param2 = new RelativeLayout.LayoutParams(
				(int) RelativeLayout.LayoutParams.WRAP_CONTENT,
				(int) RelativeLayout.LayoutParams.WRAP_CONTENT);

		param1.leftMargin = 100;
		param1.topMargin = 100;

		param2.addRule(RelativeLayout.CENTER_IN_PARENT);
		tv = new TextView(this);
		pBar = new ProgressBar(this,
                null, 
                android.R.attr.progressBarStyleLarge);

		tv.setLayoutParams(param1);
		pBar.setLayoutParams(param2);

		rl.addView(tv);
		rl.addView(pBar);

		setContentView(rl);

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (pStatus < 100) {
					pStatus += 1;

					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							pBar.setProgress(pStatus);
							tv.setText(pStatus + "/" + pBar.getMax());
						}
					});
					try {
						// Sleep for 200 milliseconds.
						// Just to display the progress slowly
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
