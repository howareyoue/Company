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
            String company = data.getStringExtra("companyName");

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

                // Use the callback to handle the company name
                getCompanyForCurrentUser(new CompanyCallback() {
                    @Override
                    public void onCompanyReceived(String company) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Restaurant restaurant = snapshot.getValue(Restaurant.class);
                            if (restaurant != null) {
                                // Set the company name for each restaurant
                                restaurant.setCompany(company);
                                restaurantList.add(restaurant);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle the error here
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle cancellation here
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
                getCompanyForCurrentUser(new CompanyCallback() {
                    @Override
                    public void onCompanyReceived(String company) {
                        // Now you have the company name, you can proceed to create a new restaurant
                        // and add it to Firebase
                        Intent intent = new Intent(TasteActivity.this, PostActivity.class);
                        intent.putExtra("companyName", company); // Pass the company name to PostActivity
                        startActivityForResult(intent, POST_ACTIVITY_REQUEST_CODE);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle the error here
                    }
                });
            }
        });

    }
    private void updateRestaurantList(String userEmail, DataSnapshot dataSnapshot) {
        getCompanyForCurrentUser(new CompanyCallback() {
            @Override
            public void onCompanyReceived(String company) {
                restaurantList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Restaurant restaurant = snapshot.getValue(Restaurant.class);
                    if (restaurant != null) {
                        // Set the company name for each restaurant
                        restaurant.setCompany(company);

                        // Add to the list
                        restaurantList.add(restaurant);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error here
            }
        });
    }


    private void updateRestaurantList(String userEmail, String restaurantName, String restaurantAddress, String review) {
        getCompanyForCurrentUser(new CompanyCallback() {
            @Override
            public void onCompanyReceived(String company) {
                // Create the new restaurant inside the callback
                Restaurant newRestaurant = new Restaurant(restaurantName, restaurantAddress, review, company);

                // Save to Firebase
                DatabaseReference restaurantsRef = FirebaseDatabase.getInstance().getReference("restaurants");
                String key = restaurantsRef.push().getKey();
                restaurantsRef.child(key).setValue(newRestaurant);

                // Add to local list and notify the adapter
                restaurantList.add(newRestaurant);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                // Handle any errors here
            }
        });
    }


    private void getCompanyForCurrentUser(final CompanyCallback callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String uid = auth.getCurrentUser().getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);
                        if (userAccount != null) {
                            callback.onCompanyReceived(userAccount.getCompanyname());
                        } else {
                            callback.onError("User account data is null");
                        }
                    } else {
                        callback.onError("User data snapshot doesn't exist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    callback.onError(databaseError.getMessage());
                }
            });
        } else {
            callback.onError("User not logged in");
        }
    }


    interface CompanyCallback {
        void onCompanyReceived(String company);
        void onError(String errorMessage);
    }

}