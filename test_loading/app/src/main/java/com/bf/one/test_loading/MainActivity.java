package com.bf.one.test_loading;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {
    int pStatus = 0;
    private Handler handler = new Handler();
    TextView tv;
    ProgressBar mProgress;
    ImageView tempimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Resources res = getResources();
//        Drawable drawable = res.getDrawable(R.drawable.circular);
        mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
//        mProgress.setProgress(0);   // Main Progress
//        mProgress.setSecondaryProgress(100); // Secondary Progress
//        mProgress.setMax(100); // Maximum Progress
//        mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.circular));

//        ObjectAnimator animation = ObjectAnimator.ofInt(mProgress, "progress", 0, 100);
////        animation.setDuration(50000);
////        animation.setInterpolator(new DecelerateInterpolator());
////        animation.start();
//        tempimg = findViewById(R.id.imageve);
//        Animation animation2 = null;
//        animation2 = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mProgress.startAnimation( AnimationUtils.loadAnimation(this, R.anim.rotate));


//        new Thread(new Runnable() {
//            @Override
////            public void run() {
////                try {
////                    Thread.sleep(3000);
////                } catch (Exception e){
////                }
////                tempimg.getAnimation().cancel();
////
////            }
////        });
////        AnimatorSet aniSet = new AnimatorSet();
////        aniSet.playTogether(rotate); // 개수의 제한이 없슴
////        aniSet.setDuration(3000);           // 애니메이터 셋의 실행 시간
////        // 처음엔 빨리 점점 느리게
////        aniSet.setInterpolator(new AccelerateDecelerateInterpolator());
////        aniSet.start();
        tv = (TextView) findViewById(R.id.tv);

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
                            mProgress.setProgress(pStatus);
                            tv.setText(pStatus + "%");
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(100); //thread will take approx 1.5 seconds to finish
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                tempimg.clearAnimation();
                tempimg.setVisibility(View.GONE);
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
}
