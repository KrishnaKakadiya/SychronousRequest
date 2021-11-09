package com.example.adjust.presenter;

import java.util.concurrent.ConcurrentHashMap;

public interface MainActivityListener {
    public interface View{
        public void onRepeatedSeconds(String errorMessage);
        public void onSuccess(Object objectType);
        public void onError(String errorMessage);
    }
    public interface Presenter{
        public void stop();
        public void getData(String second);
        public String getCurrentTime();
    }
}
