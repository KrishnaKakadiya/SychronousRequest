package com.example.adjust.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.example.adjust.AdjustApplication;
import com.example.adjust.R;
import com.example.adjust.constants.Constant;
import com.example.adjust.model.AdjustResponse;
import com.example.adjust.restfuls.WebService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MainActivityPresenter implements MainActivityListener.Presenter {
    private Context mContext;
    private WebService mWebService;
    private MainActivityListener.View mMainActivityListener;
    private ConcurrentHashMap<String, Boolean> seconds;
    private Gson gson;
    MyTask mMyTask;

    public MainActivityPresenter(Context context, MainActivityListener.View mainActivityListener, AdjustApplication adjustApplication) {
        mContext = context;
        mWebService = adjustApplication.getWebService();
        mMainActivityListener = mainActivityListener;
        gson = new GsonBuilder().create();
        seconds = new ConcurrentHashMap<String, Boolean>();
    }

    @Override
    public void stop() {
        if (mMyTask != null && mMyTask.getStatus() == AsyncTask.Status.RUNNING) {
            mMyTask.cancel(true);
        }
    }

    public void getData(String second) {
        if (!(isRepeatedSeconds(second) || Objects.equals(seconds.get(second), false))) {
            seconds.put(second, true);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("seconds", second);
            } catch (JSONException e) {
                seconds.put(second, false);
                mMainActivityListener.onError(mContext.getString(R.string.json_exception));
            }
            // yourMethod();
            if (mMyTask == null || mMyTask.getStatus() != AsyncTask.Status.RUNNING) {
                mMyTask = new MyTask();
                mMyTask.execute(jsonObject.toString());
            }
        } else {
            mMainActivityListener.onRepeatedSeconds(mContext.getString(R.string.repeated_seconds));
        }
    }

    private boolean isRepeatedSeconds(String second) {
        return seconds.containsKey(second);
    }

    public String getCurrentTime() {
        return new SimpleDateFormat("ss", Locale.getDefault()).format(new Date());
    }

    public class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            String postData = params[0];

            InputStream inputStream = mWebService.getSecuredInputStreams(Constant.ADJUST_URL, postData);
            if (inputStream != null) {
                return mWebService.getResponse(inputStream);
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                AdjustApplication.printLogMessage(3, result);
                AdjustResponse mAdjustResponse = gson.fromJson(result, AdjustResponse.class);
                if (mAdjustResponse != null) {
                    mMainActivityListener.onSuccess(mAdjustResponse);
                } else {
                    mMainActivityListener.onError(mContext.getString(R.string.error_message));
                }
            }else {
                mMainActivityListener.onError(mContext.getString(R.string.error_message));
            }
        }
    }
}