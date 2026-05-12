package com.hazyala.pickday.kopo.ac.kr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 버튼 연결
        btnStart = findViewById(R.id.btnStart);

        // 버튼 클릭 이벤트
        btnStart.setOnClickListener(v -> {

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);

        });
    }
}