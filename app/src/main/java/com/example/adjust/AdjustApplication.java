package com.example.adjust;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.adjust.constants.Constant;
import com.example.adjust.restfuls.WebService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.concurrent.ConcurrentHashMap;

public class AdjustApplication extends Application {

    public static AdjustApplication getApplication(Context context)
    {
        return (AdjustApplication) context.getApplicationContext();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        if(webService == null)
        {
            webService = new WebService();
        }
    }

    public static int LOGLEVEL = 6;
    public static boolean ERROR = LOGLEVEL > 5;
    public static boolean WARN = LOGLEVEL > 4;
    public static boolean INFO = LOGLEVEL > 3;
    public static boolean DEBUG = LOGLEVEL > 2;
    public static boolean VERBOSE = LOGLEVEL > 1;

    private WebService webService;
    public WebService getWebService()
    {
        return webService;
    }

    public static void printLogMessage(int logType, String logMessage)
    {
        if (logType == 5)
        {
            if (ERROR) {
                android.util.Log.e(Constant.TAG, logMessage);
            }
        }
        else
        {
            if (Constant.DEBUG)
            {
                switch (logType)
                {
                    case 1:
                        if (VERBOSE) {
                            android.util.Log.v(Constant.TAG, logMessage);
                        }
                        break;
                    case 2:
                        if (DEBUG) {
                            android.util.Log.d(Constant.TAG, logMessage);
                        }
                        break;
                    case 3:
                        if (INFO) {
                            android.util.Log.i(Constant.TAG, logMessage);
                        }
                        break;
                    case 4:
                        if (WARN) {
                            android.util.Log.w(Constant.TAG, logMessage);
                        }
                        break;
                }
            }
        }
    }
}
