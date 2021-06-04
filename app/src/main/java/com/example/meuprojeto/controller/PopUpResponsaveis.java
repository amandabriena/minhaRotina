package com.example.meuprojeto.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.meuprojeto.R;

public class PopUpResponsaveis extends AppCompatActivity {
    EditText senha;
    Button btAcessar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_responsaveis);

        //Setando as informações de dimensão do popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) ((dm.widthPixels) * 0.6);
        int height = (int) ((dm.heightPixels) * 0.25);
        getWindow().setLayout(width,height);

        senha = (EditText) findViewById(R.id.senhaResponsavel);
        btAcessar = (Button) findViewById(R.id.btAcessarResponsavel);

        btAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if para verificar se a senha está correta else caso não esteja
                Intent intent = new Intent(PopUpResponsaveis.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
