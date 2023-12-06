package com.example.company;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button hob;
    private Button car;
    private Button taste;
    private Button talk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hob = findViewById(R.id.btn_hobby);
        hob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

//        car = findViewById(R.id.btn_car);
//        car.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,adadad.class);
//                startActivity(intent);
//            }
//        });
//
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


    }
}