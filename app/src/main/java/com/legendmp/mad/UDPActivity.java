package com.legendmp.mad;

import android.os.Bundle;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UDPActivity extends AppCompatActivity {
    private static final int PORT = 5000;
    private static final String PASSWORD = "12345";
    private Handler uiHandler = new Handler();
    private TextView receivedData;
    private EditText passwordInput;
    private Button receiveButton, sendButton;

    private List<String> quizQuestions = Arrays.asList(
            "Q1: What is 2 + 2?", "Q2: What is the capital of France?",
            "Q3: Who wrote '1984'?", "Q4: What is the square root of 16?",
            "Q5: What is the speed of light?", "Q6: Who painted the Mona Lisa?",
            "Q7: What is the powerhouse of the cell?", "Q8: Who discovered gravity?",
            "Q9: What is 5! (5 factorial)?", "Q10: What is the chemical symbol for water?"
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_udpactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        receivedData = findViewById(R.id.receivedData);
        passwordInput = findViewById(R.id.passwordInput);
        receiveButton = findViewById(R.id.udpReceive);
        sendButton = findViewById(R.id.udpSend);

        receiveButton.setOnClickListener(v -> startReceiver());
        sendButton.setOnClickListener(v -> startBroadcast());

    }
    private void startBroadcast() {
        new Thread(() -> {
            try {
                DatagramSocket socket = new DatagramSocket();
                InetAddress broadcastIP = InetAddress.getByName("255.255.255.255");

                for (String question : quizQuestions) {
                    String data = PASSWORD + ":" + question;
                    byte[] buffer = data.getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastIP, PORT);
                    socket.send(packet);
                    Thread.sleep(1000); // Small delay for better transmission
                }
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void startReceiver() {
        new Thread(() -> {
            try {
                DatagramSocket socket = new DatagramSocket(PORT);
                byte[] buffer = new byte[1024];

                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String received = new String(packet.getData(), 0, packet.getLength());
                    if (received.startsWith(PASSWORD + ":")) {
                        String question = received.substring(PASSWORD.length() + 1);
                        uiHandler.post(() -> receivedData.append(question + "\n"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}