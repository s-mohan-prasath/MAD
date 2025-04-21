package com.legendmp.mad;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatApp extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Message> messageList;
    private EditText inputMessage;
    private Button sendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_app);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Combine both bottom paddings if keyboard is open
            int bottomPadding = Math.max(systemBars.bottom, imeInsets.bottom);

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerViewMessages);
        inputMessage = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.buttonSend);

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        sendButton.setOnClickListener(v->{
            String message = inputMessage.getText().toString().trim();
            if(!message.isEmpty()){
                messageList.add(new Message(message,true));
                adapter.notifyItemInserted(messageList.size()-1);
                inputMessage.setText("");
                recyclerView.smoothScrollToPosition(messageList.size()-1);
            }else{
                Toast.makeText(ChatApp.this, "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });

    }
}