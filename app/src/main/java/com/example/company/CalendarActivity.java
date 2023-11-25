package com.example.company;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ArrayAdapter<String> adapter;
    private List<String> events;

    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        databaseReference = FirebaseDatabase.getInstance().getReference("events");
        events = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, events);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        CalendarView calendarView = findViewById(R.id.calendarView);
        EditText eventEditText = findViewById(R.id.eventEditText);

        // 날짜가 선택되었을 때의 이벤트 처리
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                final String selectedDate = formatDate(year, month, dayOfMonth);

                // Firebase에서 해당 날짜의 일정을 불러옴
                databaseReference.child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        events.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String event = dataSnapshot.getValue(String.class);
                            events.add(event);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // 에러 처리
                        Toast.makeText(CalendarActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 일정 추가 버튼 클릭 이벤트 처리
        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event = eventEditText.getText().toString().trim();
                if (!event.isEmpty()) {
                    // 현재 선택된 날짜 가져오기
                    long selectedDateMillis = calendarView.getDate();
                    Date selectedDate = new Date(selectedDateMillis);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDate = sdf.format(selectedDate);

                    // Firebase에 일정 추가
                    databaseReference.child(formattedDate).push().setValue(event);

                    // 일정 목록 갱신
                    events.add(event);
                    adapter.notifyDataSetChanged();

                    // 입력 필드 초기화
                    eventEditText.setText("");
                }
            }
        });
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        return String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
    }
}