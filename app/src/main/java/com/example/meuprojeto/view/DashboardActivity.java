package com.example.meuprojeto.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.meuprojeto.R;

public class DashboardActivity extends AppCompatActivity {
    Button btGerAtividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btGerAtividades = (Button) findViewById(R.id.btGerAtividades);
        btGerAtividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando a ação do botão para abrir a tela de atividades
                Intent gerAtividades = new Intent(DashboardActivity.this, GerAtividadesActivity.class);
                startActivity(gerAtividades);
            }
        });
    }
}
