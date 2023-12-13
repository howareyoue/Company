package com.example.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

public class PostActivity extends AppCompatActivity {

    private EditText editTextRestaurantName;
    private EditText editTextRestaurantAddress;
    private EditText editTextReview;
    private EditText editTextCompanyname;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        editTextRestaurantName = findViewById(R.id.editTextRestaurantName);
        editTextRestaurantAddress = findViewById(R.id.editTextRestaurantAddress);
        editTextReview = findViewById(R.id.editTextReview);
        editTextCompanyname = findViewById(R.id.editTextCompanyname);
        databaseReference = FirebaseDatabase.getInstance().getReference("restaurants");

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String restaurantName = editTextRestaurantName.getText().toString();
                String restaurantAddress = editTextRestaurantAddress.getText().toString();
                String review = editTextReview.getText().toString();
                String companyname = editTextCompanyname.getText().toString();

                String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                saveRestaurant(restaurantName, restaurantAddress, review, currentUserEmail);

                finish();
            }
        });
    }
    private void saveRestaurant(String name, String address, String review, String companyname) {
        Restaurant restaurant = new Restaurant(name, address, review, companyname);

        String key = databaseReference.push().getKey(); //고유한 키 생성
        databaseReference.child(key).setValue(restaurant);

        // PostDetailActivity로 데이터 전달
        Intent intent = new Intent(PostActivity.this, PostDetailActivity.class);
        intent.putExtra("restaurantName", name);
        intent.putExtra("restaurantAddress", address);
        intent.putExtra("review", review);
        intent.putExtra("companyname", companyname);
        startActivity(intent);
    }
    private String getCompanyForCurrentUser(String userEmail, CompanyCallback callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);
                    if (userAccount != null) {
                        callback.onCompanyReceived(userAccount.getCompanyname());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 오류 처리
            }
        });
        return uid;
    }
    interface CompanyCallback {
        void onCompanyReceived(String company);

        void onError(String errorMessage);
    }
}