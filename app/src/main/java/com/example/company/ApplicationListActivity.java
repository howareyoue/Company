package com.example.company;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApplicationListActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // postId 정보를 Intent로부터 받아옴
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            postId = extras.getString("postId");
        }

        if (postId != null) {
            displayApplications();
        } else {
            Log.e("ApplicationListActivity", "postId is null");
            // postId가 null인 경우에 대한 처리
            // 여기에 필요한 처리를 추가하세요.
            // 예를 들어, Toast 메시지로 사용자에게 알림을 보여줄 수 있습니다.
        }
    }

    private void displayApplications() {
        // 해당 게시물에 대한 신청자 목록을 표시하는 ListView
        ListView applicationsListView = findViewById(R.id.applicationsListView);

        // 해당 게시물의 "applications" 노드에 저장된 데이터를 읽어옴
        mDatabase.child("applications").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 데이터 스냅샷으로부터 신청자의 UID를 가져옴
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                ArrayList<String> applicantList = new ArrayList<>();

                // arrayAdapter를 생성
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ApplicationListActivity.this, android.R.layout.simple_list_item_1);

                for (DataSnapshot child : children) {
                    String applicantUid = child.getKey();
                    applicantList.add(applicantUid);

                    // 주어진 UID로부터 사용자 정보 가져오기
                    getUserInfo(applicantUid, arrayAdapter);
                }

                // 가져온 UID 목록을 어댑터를 사용하여 ListView에 표시
                applicationsListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 읽기가 취소된 경우에 대한 처리
                Log.e("ApplicationListActivity", "Failed to read applications", databaseError.toException());
            }
        });
    }


    private void getUserInfo(String uid, ArrayAdapter<String> arrayAdapter) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserAccount").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 데이터 스냅샷으로부터 사용자 정보를 가져오기
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    String userNumber = dataSnapshot.child("number").getValue(String.class);

                    // 가져온 정보를 출력하거나 사용할 수 있음
                    Log.d("ApplicationListActivity", "User Name: " + userName);
                    Log.d("ApplicationListActivity", "User Number: " + userNumber);

                    // 예를 들어, ListView에 각각의 사용자 정보를 추가하는 방법
                    if (userName != null && userNumber != null) {
                        arrayAdapter.add("사용자 이름: " + userName + ", 전화번호: " + userNumber);
                        arrayAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("ApplicationListActivity", "User name or number is null for UID: " + uid);
                    }
                } else {
                    Log.e("ApplicationListActivity", "User data does not exist for UID: " + uid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 읽기가 취소된 경우에 대한 처리
                Log.e("ApplicationListActivity", "Failed to read user data", databaseError.toException());
            }
        });
    }
}
