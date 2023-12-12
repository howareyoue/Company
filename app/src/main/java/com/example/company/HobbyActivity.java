package com.example.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class HobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby);

        ListView listView = findViewById(R.id.hobbyListView);
        String[] hobbies = new String[] {"독서", "볼링", "당구", "밴드","실내 클라이밍", "헬스","자격증 공부", "영화","맛집탐방"}; // Example hobbies

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hobbies);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Start CalendarActivity
                Intent intent = new Intent(HobbyActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

    }
}
