package com.example.gt_2m_7_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        TextView textView = findViewById(R.id.answer_second);
        Button button = findViewById(R.id.clear_all_btn);
        Intent intent = getIntent();
        String answer = intent.getStringExtra("answer");
        textView.setText(answer);



        button.setOnClickListener(view -> {
            finishAffinity();
        });
    }


}
