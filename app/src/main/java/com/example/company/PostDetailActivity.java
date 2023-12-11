package com.example.company;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        TextView textViewRestaurantName = findViewById(R.id.detailNameTextView);
        TextView textViewRestaurantAddress = findViewById(R.id.detailAddressTextView);
        TextView textViewReview = findViewById(R.id.detailReviewTextView);

        // PostActivity에서 전달한 데이터를 가져옴
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String restaurantName = extras.getString("restaurantName");
            String restaurantAddress = extras.getString("restaurantAddress");
            String review = extras.getString("review");

            // 받아온 데이터를 화면에 설정
            textViewRestaurantName.setText("식당 이름 : " + restaurantName);
            textViewRestaurantAddress.setText(" 위치 : " + restaurantAddress);
            textViewReview.setText(" 후기 : " + review);
        }
    }
}