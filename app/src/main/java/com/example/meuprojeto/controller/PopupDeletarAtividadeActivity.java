package com.example.meuprojeto.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.meuprojeto.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class PopupDeletarAtividadeActivity extends AppCompatActivity {
    Button btCancelar, btDeletar;
    String idAtividade;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_deletar_atividade);
        //Setando as informações de dimensão do popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) ((dm.widthPixels) * 0.6);
        int height = (int) ((dm.heightPixels) * 0.25);
        getWindow().setLayout(width,height);
        btDeletar = (Button) findViewById(R.id.btAcessarResponsavel);
        view = (View) findViewById(R.id.popup);

        idAtividade = getIntent().getStringExtra("idAtividade");

        btDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("/atividades").document(idAtividade).delete();
                Intent ger = new Intent(PopupDeletarAtividadeActivity.this, GerAtividadesActivity.class);
                ger.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ger);
            }
        });
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
