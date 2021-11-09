package com.example.adjust.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.example.adjust.AdjustApplication;
import com.example.adjust.R;
import com.example.adjust.model.AdjustResponse;
import com.example.adjust.presenter.MainActivityListener;
import com.example.adjust.presenter.MainActivityPresenter;

import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends AppCompatActivity implements MainActivityListener.View {

    private MainActivityPresenter mainActivityPresenter;
    private AdjustApplication adjustApplication;
    private Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myButton = (Button) findViewById(R.id.button);

        adjustApplication = AdjustApplication.getApplication(this);
        mainActivityPresenter =  new MainActivityPresenter(this,this,adjustApplication);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myButton.setEnabled(false);
                mainActivityPresenter.getData(mainActivityPresenter.getCurrentTime());
            }
        });
    }
    @Override
    public void onRepeatedSeconds(String errorMessage) {
        myButton.setEnabled(true);
        AdjustApplication.printLogMessage(4, errorMessage);
    }

    @Override
    public void onSuccess(Object objectType) {
        myButton.setEnabled(true);
        AdjustResponse adjustResponse = (AdjustResponse) objectType;
        if(adjustResponse != null) {
            AdjustApplication.printLogMessage(5, "Seconds: "+adjustResponse.getSeconds()+" and id: "+adjustResponse.getId());
        }
    }

    @Override
    public void onError(String errorMessage) {
        myButton.setEnabled(true);
        AdjustApplication.printLogMessage(5, errorMessage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivityPresenter.stop();
    }
}