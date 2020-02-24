package tistory.itmir.exampleasynctask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startMyAsyncTask(View view) {
        MyAsyncTask mProcessTask = new MyAsyncTask();
        mProcessTask.execute(9);
    }
}
