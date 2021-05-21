package com.example.meuprojeto.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.meuprojeto.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Atividade extends AppCompatActivity {
    EditText nome_atv, horario, musica;
    ImageView imagem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);
        String id = getIntent().getStringExtra("idAtividade");

        nome_atv = (EditText) findViewById(R.id.nome_atividade);
        horario = (EditText) findViewById(R.id.horario);
        musica = (EditText) findViewById(R.id.musica);
        imagem = (ImageView) findViewById(R.id.imgIcon);

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("/atividades").document(id);
        
    }
}
