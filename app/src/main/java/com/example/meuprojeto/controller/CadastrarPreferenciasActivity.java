package com.example.meuprojeto.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.meuprojeto.R;

public class CadastrarPreferenciasActivity extends AppCompatActivity {
    ImageButton btNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_preferencias);

        btNext = (ImageButton) findViewById(R.id.btNextCadastro);

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando para espaço infantil
                Intent intent = new Intent(CadastrarPreferenciasActivity.this, GerAtividadesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
