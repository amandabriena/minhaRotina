package com.example.meuprojeto.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.meuprojeto.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //executar tarefas enquanto carrega a tela splash

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent i = new Intent(SplashActivity.this, DashboardActivity.class);
                finish();
                startActivity(i);
            }
        }, SPLASH_TIME_OUT);
    }
}
