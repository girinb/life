package com.bf.lineplus;

public class tools {

    protected final static String TAG = "디버그중";

    public static void log(String type, String logText) {
        if (BuildConfig.DEBUG) {
            switch (type) {
                case "D":
                    android.util.Log.d(TAG, logText);
                    break;
                case "I":
                    android.util.Log.i(TAG, logText);
                    break;
                case "W":
                    android.util.Log.w(TAG, logText);
                    break;
                case "E":
                    android.util.Log.e(TAG, logText);
                    break;
            }
        }
    }

    public static void log(String logText) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(TAG, logText);
        }
    }

}
