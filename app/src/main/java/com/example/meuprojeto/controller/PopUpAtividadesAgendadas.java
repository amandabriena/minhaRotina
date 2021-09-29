package com.example.meuprojeto.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.meuprojeto.R;

public class PopUpAtividadesAgendadas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_atividades_agendadas);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) ((dm.widthPixels) * 0.9);
        int height = (int) ((dm.heightPixels) * 0.8);
        getWindow().setLayout(width,height);

    }
}
