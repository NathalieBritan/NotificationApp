package com.nathaliebritan.notificationapp.activity;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nathaliebritan.notificationapp.R;
import com.nathaliebritan.notificationapp.service.mNotificationService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String YOUTUBE_API_KEY = "AIzaSyCO5Pv7-2h4x5oh6FoKnWFIbJRnBQD30J4";
    private static final String KEY_SERVICE = "SERVICE";

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStart = (Button) findViewById(R.id.btn_start);
        Button btnStop = (Button) findViewById(R.id.btn_stop);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        mSharedPreferences = getPreferences(MODE_PRIVATE);

        if (CheckForService()) {
            startService(new Intent(getApplicationContext(), mNotificationService.class));
        }

    }

        @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_start:
                startService(new Intent(getApplicationContext(), mNotificationService.class));
                TurnServiceON();
                break;

            case R.id.btn_stop:
                stopService(new Intent(getApplicationContext(), NotificationManager.class));
                TurnServiceOff();
                break;
        }
    }

    private void TurnServiceON() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(KEY_SERVICE, 1);
        editor.commit();
    }

    private void TurnServiceOff() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(KEY_SERVICE, 0);
        editor.commit();
    }

    private boolean CheckForService() {
        if (mSharedPreferences.getInt(KEY_SERVICE, 0) == 1) {
            return true;
        }
        return false;
    }
}
