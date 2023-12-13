package com.example.company;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button hob;
    private Button car;
    private Button taste;
    private Button talk;

    private Button logoutButton;

    private FirebaseAuth mFirebaseAuth; // Firebase 인증 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mFirebaseAuth = FirebaseAuth.getInstance(); // Firebase 인증 객체 초기화


        hob = findViewById(R.id.btn_hobby);
        hob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HobbyActivity.class);
                startActivity(intent);
            }
        });

        car = findViewById(R.id.btn_car);
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CarActivity.class);
                startActivity(intent);
            }
        });

        taste = findViewById(R.id.btn_taste);
        taste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TasteActivity.class);
                startActivity(intent);
            }
        });
//
        talk = findViewById(R.id.btn_talk);
        talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, talkActivity.class);
                startActivity(intent);
            }
        });
        logoutButton = findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Firebase 로그아웃
                mFirebaseAuth.signOut();

                // 로그인 페이지로 이동
                Intent intent = new Intent(MainActivity.this, loginActivity.class);
                startActivity(intent);
                finish(); // 현재 액티비티를 종료하여 뒤로가기 시 로그인 페이지가 나오도록 함
            }
        });
    }
}


