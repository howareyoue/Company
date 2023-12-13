package com.example.company;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    private FloatingActionButton floatingActionButton;
    private EditText searchEditText;
    private ArrayAdapter<Restaurant> adapter;
    private List<Restaurant> restaurantList;
    private List<Restaurant> filteredList;

    private static final int POST_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taste);

        list = findViewById(R.id.list);
        floatingActionButton = findViewById(R.id.Post_floating);
        searchEditText = findViewById(R.id.searchEditText);

        restaurantList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new ArrayAdapter<Restaurant>(this, R.layout.restaurant_list_item, filteredList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.restaurant_list_item, parent, false);
                }

                Restaurant restaurant = getItem(position);

                TextView restaurantNameTextView = convertView.findViewById(R.id.restaurantNameTextView);
                TextView CompanyTextView = convertView.findViewById(R.id.CompanyTextView);
                TextView restaurantLocationTextView = convertView.findViewById(R.id.restaurantLocationTextView);

                if (restaurant != null) {
                    restaurantNameTextView.setText("식당 이름: " + restaurant.getName());
                    CompanyTextView.setText("회사 이름: " + restaurant.getCompany());
                    restaurantLocationTextView.setText("식당 위치: " + restaurant.getAddress());
                }
                return convertView;
            }
        };
        list.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("restaurants");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Restaurant selectedRestaurant = filteredList.get(position);
                Intent detailIntent = new Intent(TasteActivity.this, PostDetailActivity.class);
                detailIntent.putExtra("restaurantName", selectedRestaurant.getName());
                detailIntent.putExtra("restaurantAddress", selectedRestaurant.getAddress());
                detailIntent.putExtra("review", selectedRestaurant.getReview());
                startActivity(detailIntent);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TasteActivity.this, PostActivity.class);
                startActivityForResult(intent, POST_ACTIVITY_REQUEST_CODE);
            }
        });

        // 데이터베이스에서 음식점 정보 불러오기
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                restaurantList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Restaurant restaurant = snapshot.getValue(Restaurant.class);
                    if (restaurant != null) {
                        restaurantList.add(restaurant);
                    }
                }
                // 검색어가 변경되면 검색 수행
                performSearch(searchEditText.getText().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 오류 처리 코드 추가
            }
        });

        // EditText에 텍스트가 변경될 때마다 검색 수행
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                performSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }

    private void performSearch(String query) {
        filteredList.clear();

        if (!TextUtils.isEmpty(query)) {
            for (Restaurant restaurant : restaurantList) {
                if (restaurant.getCompany().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(restaurant);
                }
            }
        } else {
            filteredList.addAll(restaurantList);
        }

        adapter.notifyDataSetChanged();
    }
}
