package com.example.company;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
public class TasteActivity extends AppCompatActivity {

    private ListView list;
    private EditText searchEditText;
    private FloatingActionButton floatingActionButton;
    private ArrayAdapter<Restaurant> adapter;
    private List<Restaurant> restaurantList;

    private static final int POST_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == POST_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            String restaurantName = data.getStringExtra("restaurantName");
            String restaurantAddress = data.getStringExtra("restaurantAddress");
            String review = data.getStringExtra("review");

            // 현재 사용자의 회사명 가져오기
            String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String company = getCompanyForCurrentUser(currentUserEmail);

            Restaurant newRestaurant = new Restaurant(restaurantName, restaurantAddress, review, company);

            restaurantList.add(newRestaurant);
            adapter.notifyDataSetChanged();
        }
    }

    private String companyname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taste);

        list = findViewById(R.id.list);
        searchEditText = findViewById(R.id.searchEditText);
        floatingActionButton = findViewById(R.id.Post_floating);

        restaurantList = new ArrayList<>();
        adapter = new ArrayAdapter<Restaurant>(this, R.layout.restaurant_list_item, restaurantList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.restaurant_list_item, parent, false);
                }

                Restaurant restaurant = getItem(position);

                TextView restaurantNameTextView = convertView.findViewById(R.id.restaurantNameTextView);
                TextView CompanyTextView = convertView.findViewById(R.id.CompanyTextView);
                TextView restaurantLocationTextView = convertView.findViewById(R.id.restaurantLocationTextView);

                // TextView에 데이터 설정
                if (restaurant != null) {
                    restaurantNameTextView.setText("식당 이름: " + restaurant.getName());
                    CompanyTextView.setText("작성자의 회사명: " + restaurant.getCompany());
                    restaurantLocationTextView.setText("식당 위치: " + restaurant.getAddress());
                }
                return convertView;
            }
        };
        // ...
        list.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("restaurants");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restaurantList.clear();
                String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                companyname = getCompanyForCurrentUser(currentUserEmail);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Restaurant restaurant = snapshot.getValue(Restaurant.class);
                    if (restaurant != null) {
                        // 여기에서 회사명 이외의 다른 정보 설정
                        restaurant.setCompany(companyname);

                        // 리스트에 추가
                        restaurantList.add(restaurant);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // 검색 기능 설정
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // 선택된 레스토랑 가져오기
                Restaurant selectedRestaurant = restaurantList.get(position);

                // 선택된 레스토랑의 데이터를 전달하기 위한 Intent 생성
                Intent detailIntent = new Intent(TasteActivity.this, PostDetailActivity.class);
                detailIntent.putExtra("restaurantName", selectedRestaurant.getName());
                detailIntent.putExtra("restaurantAddress", selectedRestaurant.getAddress());
                detailIntent.putExtra("review", selectedRestaurant.getReview());
                startActivity(detailIntent);
            }
        });

        //플로팅 버튼
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //게시글 작성 화면으로 이동
                Intent intent = new Intent(TasteActivity.this, PostActivity.class);
                startActivityForResult(intent, POST_ACTIVITY_REQUEST_CODE);
            }
        });
    }
    private void updateRestaurantList(String userEmail, DataSnapshot dataSnapshot) {
        String companyname = getCompanyForCurrentUser(userEmail);
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Restaurant restaurant = snapshot.getValue(Restaurant.class);
            if (restaurant != null) {
                // 여기에서 회사명 이외의 다른 정보 설정
                restaurant.setCompany(companyname);

                // 리스트에 추가
                restaurantList.add(restaurant);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void updateRestaurantList(String userEmail, String restaurantName, String restaurantAddress, String review, String company) {
        String companyname = getCompanyForCurrentUser(company);
        Restaurant newRestaurant = new Restaurant(restaurantName, restaurantAddress, review, companyname);

        // Firebase 데이터베이스에 저장
        DatabaseReference restaurantsRef = FirebaseDatabase.getInstance().getReference("restaurants");
        String key = restaurantsRef.push().getKey();
        restaurantsRef.child(key).setValue(newRestaurant);

        restaurantList.add(newRestaurant);
        adapter.notifyDataSetChanged();
    }

    private String getCompanyForCurrentUser(String userEmail) {
        final String[] companyName = {""};

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);
                    if (userAccount != null) {
                        companyName[0] = userAccount.getCompanyname();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 오류 처리
            }
        });
        return companyName[0];
    }
}