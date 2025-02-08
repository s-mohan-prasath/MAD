package com.legendmp.mad;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class Exercise2_3 extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private final char[] operators = {'%','^','x','-','+','/'};
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
        TextView calcOutput = findViewById(R.id.calc_output_text);

        // Sample data
        String[] items = {"AC","( )","%","/","7","8","9","x","4","5","6","-","1", "2", "3","+","0",".","DEL","="};

        CalcGridAdapter adapter = new CalcGridAdapter(this,items);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((parent, view, position, id) -> {

            // BUTTON CLICK - VALUE INSERT IN THE EDIT TEXT
            String selectedValue = (String) parent.getItemAtPosition(position);
            String oldText = calcInput.getText().toString();
            int cursorPos = calcInput.getText().length();

            switch (selectedValue) {
                case "=":
                    if(cursorPos!=0){
                        ExpressionEvaluator evaluate = new ExpressionEvaluator();
                        calcOutput.setText(evaluate.evalExp(oldText));
                    }
//                    calcInput.setText("");
                    cursorPos = -1;
                    break;
                case "AC":
                    calcInput.setText("");
                    cursorPos = -1;
                    break;
                case "DEL":
                    if (cursorPos!=0) {
                        calcInput.setText(oldText.substring(0, cursorPos - 1));
                        cursorPos -= 2;
                    } else {
                        cursorPos = -1;
                    }
                    break;
                case "( )":
                    if (cursorPos==0 || oldText.charAt(cursorPos-1) == '(' || isOperator(oldText.charAt(cursorPos-1))) {
                        calcInput.setText(oldText + "(");
                    } else {
                        if(haveMoreOpenParenthesis(oldText)){
                            calcInput.setText(oldText + ")");
                        }else{
                            calcInput.setText(oldText+"(");
                        }
                    }
                    break;
                default:
                    char op = selectedValue.charAt(0);
                    if(isOperator(op)){
                        if(cursorPos==0){
                            if(op=='-'){
                                calcInput.setText(oldText+"-");
                            }else{
                                cursorPos--;
                            }
                        }
                        else{
                            if(cursorPos==1 && oldText.charAt(0)=='-'){
                                cursorPos--;
                            }
                            else if(isOperator(oldText.charAt(cursorPos-1))){
                                oldText = oldText.substring(0,cursorPos-1) + selectedValue;
                                calcInput.setText(oldText);
                                cursorPos--;
                            }else{
                                calcInput.setText(oldText+selectedValue);
                            }
                        }
                    }
                    else{
                        calcInput.setText(oldText+selectedValue);
                    }
                    break;
            }
            calcInput.setSelection(cursorPos+1);

            // OPACITY AND SOUND PLAYING
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
        });
    }
    private boolean haveMoreOpenParenthesis(String exp) {
        int left = 0,right = 0;
        int len = exp.length();
        for(int i  = 0;i<len;i++){
            if(exp.charAt(i)=='(')left++;
            else if(exp.charAt(i)==')')right++;
        }
        return left>right;
    }
    private boolean isOperator(char op) {
        for (char c : operators) {
            if (c == op) return true;
        }
        return false;
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
