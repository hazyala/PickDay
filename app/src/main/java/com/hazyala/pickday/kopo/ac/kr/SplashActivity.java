package com.hazyala.pickday.kopo.ac.kr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    // 스플래시 유지 시간 (2초)
    private static final int SPLASH_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 일정 시간 후 LoginActivity로 이동
        new Handler().postDelayed(() -> {

            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);

            // 스플래시 종료
            finish();

        }, SPLASH_TIME);
    }
}