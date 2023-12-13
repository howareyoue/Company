package com.example.company;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CarRegActivity extends AppCompatActivity {

    private EditText editDriverName, editDepartureTime, editDepartureLocation, editDestination;
    private Button btnRegister;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_registration);

        firebaseAuth = FirebaseAuth.getInstance();

        editDriverName = findViewById(R.id.editDriverName);
        editDepartureTime = findViewById(R.id.editDepartureTime);
        editDepartureLocation = findViewById(R.id.editDepartureLocation);
        editDestination = findViewById(R.id.editDestination);
        btnRegister = findViewById(R.id.btnRegister);

        // DatePickerDialog 초기화
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = dateFormat.format(selectedDate.getTime());

                showTimePickerDialog(formattedDate);
            }
        };

        // DatePicker 호출을 위한 이벤트 추가
        editDepartureTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    // Firebase Realtime Database에서 사용자 정보 조회
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserAccount").child(currentUser.getUid());

                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String userName = dataSnapshot.child("name").getValue(String.class);

                                if (userName != null && !userName.isEmpty()) {
                                    // 사용자의 이름이 존재하면 EditText에 설정
                                    editDriverName.setText(userName);

                                    // 현재 로그인한 사용자의 이름을 DB에 저장
                                    saveRegistrationInfoToFirebase(
                                            userName,
                                            editDepartureTime.getText().toString(),
                                            editDepartureLocation.getText().toString(),
                                            editDestination.getText().toString()
                                    );
                                } else {
                                    Toast.makeText(CarRegActivity.this, "사용자 이름을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // 에러 처리
                        }
                    });
                }

                Intent intent = new Intent(CarRegActivity.this, CarActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog(final String selectedDate) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // 선택한 날짜 및 시간을 형식에 맞게 설정
                        String formattedDateTime = String.format(Locale.getDefault(), "%s %02d:%02d", selectedDate, hourOfDay, minute);
                        editDepartureTime.setText(formattedDateTime);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    private void saveRegistrationInfoToFirebase(String driverName, String dateTime, String departureLocation, String destination) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("registrations");


        String postId = databaseReference.push().getKey();


        RegistrationInfo registrationInfo = new RegistrationInfo(driverName, dateTime, departureLocation, destination);

        registrationInfo.setPostId(postId);

        databaseReference.child(postId).setValue(registrationInfo);

        Intent intent = new Intent(CarRegActivity.this, CarActivity.class);
        startActivity(intent);
    }
}