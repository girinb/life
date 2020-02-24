package tistory.itmir.exampleasynctask;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by whdghks913 on 2016-09-11.
 */

/**
 * AsyncTask<Integer, Integer, Boolean>은 아래와 같은 형식이다
 * AsyncTask<전달받은값의종류, Update값의종류, 결과값의종류>
 * ex) AsyncTask<Void, Integer, Void>
 */
public class MyAsyncTask extends AsyncTask<Integer, Integer, Integer> {

    /**
     * execute() 메소드가 호출되면 doInBackground()가 돌기 전 이 메소드가 호출됨.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * 백그라운드 작업
     *
     * @param integers
     * @return
     */
    @Override
    protected Integer doInBackground(Integer... integers) {
        // 1부터 integers[0]까지의 곱을 구한다.
        int result = 1;
        int num = integers[0];

        publishProgress(20);

        for (int i = 1; i <= num; i++) {
            result = result * i;
        }

        return result;
    }

    /**
     * 백그라운드 진행 상황을 업데이트하기 위한 표시
     *
     * @param params
     */
    @Override
    protected void onProgressUpdate(Integer... params) {
        Log.d("MyAsyncTask", params[0] + " % ");
    }

    /**
     * 백그라운드 작업이 완료된 후 호출되는 메소드
     *
     * @param result
     */
    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        Log.d("Result", "result : " + result);
    }
}
