package com.example.company;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CarRegActivity extends AppCompatActivity {

    private EditText editDriverName, editDepartureTime, editDepartureLocation, editDestination;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_car_registration);

        editDriverName = findViewById(R.id.editDriverName);
        editDepartureTime = findViewById(R.id.editDepartureTime);
        editDepartureLocation = findViewById(R.id.editDepartureLocation);
        editDestination = findViewById(R.id.editDestination);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(CarRegActivity.this, CarActivity.class);
                intent.putExtra("driverName", editDriverName.getText().toString());
                intent.putExtra("departureTime", editDepartureTime.getText().toString());
                intent.putExtra("departureLocation", editDepartureLocation.getText().toString());
                intent.putExtra("destination", editDestination.getText().toString());


                saveRegistrationInfoToFirebase(
                        editDriverName.getText().toString(),
                        editDepartureTime.getText().toString(),
                        editDepartureLocation.getText().toString(),
                        editDestination.getText().toString()

                );
                startActivity(intent);
            }
        });
    }
    private void saveRegistrationInfoToFirebase(String driverName, String departureTime, String departureLocation, String destination) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("registrations");

        RegistrationInfo registrationInfo = new RegistrationInfo(driverName, departureTime, departureLocation, destination);

        databaseReference.push().setValue(registrationInfo);
    }
}