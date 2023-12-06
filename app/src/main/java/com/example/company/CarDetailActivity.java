package com.example.company;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CarDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        Intent intent = getIntent();
        String driverName = intent.getStringExtra("driverName");
        String departureTime = intent.getStringExtra("departureTime");
        String departureLocation = intent.getStringExtra("departureLocation");
        String destination = intent.getStringExtra("destination");

        TextView textDriverName = findViewById(R.id.textDriverNameDetail);
        TextView textDepartureTime = findViewById(R.id.textDepartureTimeDetail);
        TextView textRouteInfo = findViewById(R.id.textRouteInfoDetail);

        textDriverName.setText("운전자: " + driverName);
        textDepartureTime.setText("출발 시간: " + departureTime);
        textRouteInfo.setText("출발지 > 도착지: " + departureLocation + " > " + destination);
    }
}

