package com.example.company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CarActivity extends AppCompatActivity {

    private ListView listViewCars;
    private DatabaseReference databaseReference;
    private List<RegistrationInfo> registrationList;
    private Button buttonCar;
    private EditText searchEditText;
    private Button searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        listViewCars = findViewById(R.id.listViewCars);
        databaseReference = FirebaseDatabase.getInstance().getReference("registrations");
        registrationList = new ArrayList<>();

        searchEditText = findViewById(R.id.editText_search);
        searchButton = findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchEditText.getText().toString();
                performSearch(searchQuery);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                registrationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RegistrationInfo registrationInfo = snapshot.getValue(RegistrationInfo.class);
                    if (registrationInfo != null) {
                        registrationList.add(registrationInfo);
                    }
                }
                updateUI(registrationList);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        buttonCar = findViewById(R.id.btn_car_reg);
        buttonCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarActivity.this, CarRegActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateUI(final List<RegistrationInfo> registrationList) {
        CarListAdapter adapter = new CarListAdapter(this, R.layout.car_list_item, registrationList);
        listViewCars.setAdapter(adapter);

        listViewCars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RegistrationInfo clickedInfo = registrationList.get(position);

                Intent intent = new Intent(CarActivity.this, CarDetailActivity.class);
                intent.putExtra("driverName", clickedInfo.getDriverName());
                intent.putExtra("departureTime", clickedInfo.getDepartureTime());
                intent.putExtra("departureLocation", clickedInfo.getDepartureLocation());
                intent.putExtra("destination", clickedInfo.getDestination());
                startActivity(intent);
            }
        });
    }
    private void performSearch(String query) {
        List<RegistrationInfo> searchResults = new ArrayList<>();

        for (RegistrationInfo info : registrationList) {
            if (info.getDestination().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(info);
            }
        }

        updateUI(searchResults);
    }
}
