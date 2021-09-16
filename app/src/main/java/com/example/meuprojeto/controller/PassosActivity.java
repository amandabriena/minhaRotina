package com.example.meuprojeto.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.model.Passo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;

public class PassosActivity extends AppCompatActivity {
    SliderPassosAdapter adapter;
    ViewPager2 page2;
    ArrayList<Passo> listaPassos = new ArrayList<>();
    Atividade atividade;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passos);
        atividade = getIntent().getParcelableExtra("atividade");
        //idAtividade = getIntent().getStringExtra("idAtividade");
        listaPassos = getIntent().getParcelableArrayListExtra("lista");
        Log.e("passos size", listaPassos.size()+"");
        page2 = findViewById(R.id.viewPager);

        adapter = new SliderPassosAdapter(listaPassos);
        page2.setAdapter(adapter);

        adapter.setOnItemClickListener(new ClickListener<Passo>() {
            @Override
            public void onItemClick(Passo data) {
                Log.e("i count", data.getNumOrdem()+"");
                String descricao = data.getDescricaoPasso();
                int speech = textToSpeech.speak(descricao, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int lang = textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });

    }

    public void onClickConcluido(View v){
        Log.e("btClick", "Clicado!");
        FirebaseFirestore.getInstance().collection("usuarios")
                .document(FirebaseAuth.getInstance().getUid()).collection("atividades")
                .document(atividade.getId()).update("status", "1", "qtVezesConcluida", FieldValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("update", "updateRealizado");
            }
        });
        Toast.makeText(PassosActivity.this,"Atividade Concluída!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(PassosActivity.this, FeedbackActivity.class);
        intent.putExtra("atividade", atividade);
        startActivity(intent);
    }

}
