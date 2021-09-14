package com.example.meuprojeto.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.meuprojeto.R;

public class FeedbackActivity extends AppCompatActivity {
    ImageButton happy0,happy1, happy2, happy3, happy4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        happy0 = (ImageButton) findViewById(R.id.happy0);
        happy1 = (ImageButton) findViewById(R.id.happy1);
        happy2 = (ImageButton) findViewById(R.id.happy2);
        happy3 = (ImageButton) findViewById(R.id.happy3);
        happy4 = (ImageButton) findViewById(R.id.happy4);

        happy0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboard = new Intent(FeedbackActivity.this, MinhaRotinaActivity.class);
                dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dashboard);
            }
        });
        happy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboard = new Intent(FeedbackActivity.this, MinhaRotinaActivity.class);
                dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dashboard);
            }
        });
        happy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboard = new Intent(FeedbackActivity.this, MinhaRotinaActivity.class);
                dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dashboard);
            }
        });
        happy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboard = new Intent(FeedbackActivity.this, MinhaRotinaActivity.class);
                dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dashboard);
            }
        });
        happy4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboard = new Intent(FeedbackActivity.this, MinhaRotinaActivity.class);
                dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dashboard);
            }
        });

    }
}
