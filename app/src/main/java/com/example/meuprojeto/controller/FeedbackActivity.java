package com.example.meuprojeto.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class FeedbackActivity extends AppCompatActivity {
    TextView texto;
    ImageButton happy0,happy1, happy2, happy3, happy4;
    Atividade atividade;
    int feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        atividade = getIntent().getParcelableExtra("atividade");
        texto = (TextView) findViewById(R.id.legendaFeedback);
        happy0 = (ImageButton) findViewById(R.id.happy0);
        happy1 = (ImageButton) findViewById(R.id.happy1);
        happy2 = (ImageButton) findViewById(R.id.happy2);
        happy3 = (ImageButton) findViewById(R.id.happy3);
        happy4 = (ImageButton) findViewById(R.id.happy4);

        String textoCompleto = "Olá! O que você achou da tarefa '"+atividade.getNomeAtividade()+"' ?";
        texto.setText(textoCompleto);


        happy0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback = 1;
                avaliarAtividade();
            }
        });
        happy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback = 2;
                avaliarAtividade();
            }
        });
        happy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback = 3;
                avaliarAtividade();
            }
        });
        happy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback = 4;
                avaliarAtividade();
            }
        });
        happy4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback = 5;
                avaliarAtividade();
            }
        });

    }

    public void avaliarAtividade(){
        FirebaseFirestore.getInstance().collection("usuarios")
                .document(atividade.getIdUsuario()).collection("atividades")
                .document(atividade.getId())
                .update("somatorioFeedback", FieldValue.increment(feedback)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("Atividade", "feedback realizado");
            }
        });
        Intent dashboard = new Intent(FeedbackActivity.this, MinhaRotinaActivity.class);
        dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dashboard);
    }
}
