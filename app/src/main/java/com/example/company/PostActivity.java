package com.example.company;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity {

    private EditText editTextRestaurantName;
    private EditText editTextRestaurantAddress;
    private EditText editTextReview;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        editTextRestaurantName = findViewById(R.id.editTextRestaurantName);
        editTextRestaurantAddress = findViewById(R.id.editTextRestaurantAddress);
        editTextReview = findViewById(R.id.editTextReview);

        databaseReference = FirebaseDatabase.getInstance().getReference("restaurants");

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String restaurantName = editTextRestaurantName.getText().toString();
                String restaurantAddress = editTextRestaurantAddress.getText().toString();
                String review = editTextReview.getText().toString();

                saveRestaurant(restaurantName, restaurantAddress, review);

                finish();
            }
        });
    }

    private void saveRestaurant(String name, String address, String review) {
        Restaurant restaurant = new Restaurant(name, address, review);

        String key = databaseReference.push().getKey(); //고유한 키 생성
        databaseReference.child(key).setValue(restaurant);
    }
}