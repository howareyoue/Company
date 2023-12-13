package com.example.company;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class CarDetailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String postId; // 게시물 ID를 저장하는 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");


        String driverName = intent.getStringExtra("driverName");
        String departureTime = intent.getStringExtra("departureTime");
        String departureLocation = intent.getStringExtra("departureLocation");
        String destination = intent.getStringExtra("destination");


        TextView textDriverName = findViewById(R.id.textDriverNameDetail);
        TextView textDepartureTime = findViewById(R.id.textDepartureTimeDetail);
        TextView textDepartureLocation = findViewById(R.id.textDepartureLocationDetail);
        TextView textDestination = findViewById(R.id.textDestinationDetail);




        textDriverName.setText("운전자: "+driverName);
        textDepartureTime.setText(departureTime+" 출발");
        textDepartureLocation.setText("출발지 : " + departureLocation );
        textDestination.setText("도착지 : " + destination);

        Button applyButton = findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                applyForPost(); // 게시물에 신청하는 메소드 호출
            }
        });
    }

    private void applyForPost() {
        // FirebaseAuth 객체 초기화 여부 확인
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // 현재 로그인한 사용자의 UID를 얻어옴
            String userId = currentUser.getUid();

            // postId가 null이 아닌지 확인
            if (postId != null) {
                // "applications" 노드에 게시물 ID 아래에 신청 정보를 저장
                mDatabase.child("applications").child(postId).child(userId).setValue(true);
            } else {
                // postId가 null인 경우에 대한 처리
                // 여기에 필요한 처리를 추가하세요.
                // 예를 들어, Toast 메시지로 사용자에게 알림을 보여줄 수 있습니다.
                Toast.makeText(this, "게시물 ID가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }

}