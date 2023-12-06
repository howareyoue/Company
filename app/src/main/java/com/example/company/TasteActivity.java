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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

            Restaurant newRestaurant = new Restaurant(restaurantName, restaurantAddress, review);

            restaurantList.add(newRestaurant);
            adapter.notifyDataSetChanged();
        }
    }

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

                UserAccount userAccount = new UserAccount();

                // TextView에 데이터 설정
                if (restaurant != null) {
                    restaurantNameTextView.setText(restaurant.getName());
                    CompanyTextView.setText(userAccount.getCompanyname());
                    restaurantLocationTextView.setText(restaurant.getAddress());
                }
                return convertView;
            }
        };
        list.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("restaurants");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restaurantList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Restaurant restaurant = snapshot.getValue(Restaurant.class);
                    restaurantList.add(restaurant);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //검색 기능 설정
        if (searchEditText != null) {
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
        }

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
}
//