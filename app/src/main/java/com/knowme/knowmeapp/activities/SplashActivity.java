package com.knowme.knowmeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.knowme.knowmeapp.R;

public class SplashActivity extends AppCompatActivity {
    private final int SLEEP_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        addFullScreenParameters();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startCountDown();
    }

    private void startCountDown() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(SLEEP_TIME);
                    startNextActivity();

                } catch (Exception e) {

                }
            }
        }).start();

    }

    private void startNextActivity() {

        Context context = this;
        Class componentToStart = LoginActivity.class;
        Intent loginIntent = new Intent(context, componentToStart);
        startActivity(loginIntent);
    }


    private void addFullScreenParameters() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
