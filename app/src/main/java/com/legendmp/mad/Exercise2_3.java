package com.legendmp.mad;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class Exercise2_3 extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise2_3);

        // Handling window insets properly in Java
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializing GridView
        GridView gridView = findViewById(R.id.calc_grid_view);
        EditText calcInput = findViewById(R.id.calc_input);

        // Sample data
        String[] items = {"AC","( )","%","/","7","8","9","x","4","5","6","-","1", "2", "3","+","0",".","DEL","="};

        CalcGridAdapter adapter = new CalcGridAdapter(this,items);
        gridView.setAdapter(adapter);
        calcInput.setOnTouchListener((v,event)->{
            if(event.getAction()== MotionEvent.ACTION_DOWN){
                v.requestFocus();
                hideKeyboard(v);
            }
            return false;
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = (String) parent.getItemAtPosition(position);
                String oldText = calcInput.getText().toString();
                int cursorPos = calcInput.getText().length();

                if(selectedValue.equals("AC")){
                    calcInput.setText("");
                    cursorPos = -1;
                }else if(selectedValue.equals("DEL")){
                    int len = oldText.length();
                    if(!oldText.isEmpty()){
                        calcInput.setText(oldText.substring(0,len-1));
                        cursorPos-=2;
                    }else{
                        cursorPos=-1;
                    }
                }else if(selectedValue.equals("( )")){
                    if (!oldText.contains("(")) {
                        calcInput.setText(oldText + "(");
                    } else if (!oldText.contains(")")) {
                        calcInput.setText(oldText + ")");
                    }else{
                        calcInput.setText(oldText + "(");
                    }
                }else{
                    cursorPos = calcInput.getSelectionStart();
                    calcInput.setText(calcInput.getText().toString()+selectedValue);
                }
                calcInput.setSelection(cursorPos+1);

//      OPACITY AND SOUND PLAYING
                view.setAlpha(0.7f);
                view.setBackgroundColor(getResources().getColor(R.color.black));
                playClickSound();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() ->
                        {
                            view.setAlpha(1f);
                            view.setBackgroundColor(getResources().getColor(R.color.blue));
                        });
                    }
                }, 500);
            }
        });
    }
    private String calculate(String mathExp){
        return mathExp;
    }
    private boolean validateParenthesis(String exp){
        Stack<Character> stack = new Stack<>();
        int len = exp.length();
        for(int i = 0;i<len;i++){
            if(exp.charAt(i)=='(')stack.push('(');
            else if(exp.charAt(i)==')'){
                if(!stack.isEmpty())stack.pop();
                else return false;
            }
        }
        return true;
    }
    // Hide keyboard function
    private void hideKeyboard(View view) {
        view.clearFocus(); // Remove focus to prevent soft keyboard from appearing
    }
    private void playClickSound(){
        if(mediaPlayer==null)mediaPlayer = MediaPlayer.create(this,R.raw.click_sound);
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(this,R.raw.click_sound);
        }
        mediaPlayer.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
