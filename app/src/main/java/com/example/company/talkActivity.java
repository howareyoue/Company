package com.example.company;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class talkActivity extends AppCompatActivity {
    private EditText messageInput;
    private Button sendButton;
    private ListView messagesView;
    private ArrayAdapter<Message> adapter;
    private ArrayList<Message> messages;
    private DatabaseReference databaseReference;
    private String currentUserId;
    private String currentUserNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        // FirebaseUser 객체에서 UID를 가져옵니다.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUserId = user.getUid();

            // 사용자의 닉네임을 데이터베이스에서 가져옵니다.
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserAccount").child(currentUserId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);
                    if (userAccount != null) {
                        currentUserNickname = userAccount.getName(); // 또는 적절한 필드 사용
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("talkActivity", "Database error: " + databaseError.getMessage());
                }
            });
        }


        messageInput = findViewById(R.id.editText); // EditText의 ID를 맞춤
        sendButton = findViewById(R.id.buttonSend); // Button의 ID를 맞춤
        messagesView = findViewById(R.id.listView); // ListView의 ID를 맞춤

        messages = new ArrayList<>();
        adapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_2, android.R.id.text1, messages) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                // ArrayAdapter의 getView를 커스터마이징합니다.
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
                }
                TextView text1 = convertView.findViewById(android.R.id.text1);
                TextView text2 = convertView.findViewById(android.R.id.text2);

                Message message = getItem(position);
                if (message != null) {
                    text1.setText(message.getNickname());
                    text2.setText(message.getText());
                }
                return convertView;
            }
        };
        messagesView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("UserMessages");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageInput.getText().toString().trim();
                if (!messageText.isEmpty() && currentUserId != null && currentUserNickname != null) {
                    Message message = new Message(messageText, currentUserNickname, currentUserId);
                    databaseReference.push().setValue(message.toMap()); // Modified here
                    messageInput.setText("");
                }
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null) {
                        long diff = System.currentTimeMillis() - message.getTimestamp();
                        long hours = diff / (1000 * 60 * 60);
                        if (hours < 24) {
                            messages.add(message);
                        } else {
                            // Remove the message that is older than 24 hours
                            snapshot.getRef().removeValue();
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("talkActivity", "Database error: " + databaseError.getMessage());
            }
        });

    }
}