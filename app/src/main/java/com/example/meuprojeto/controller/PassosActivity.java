package com.example.meuprojeto.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Passo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PassosActivity extends AppCompatActivity {
    SliderAdapter adapter;
    ViewPager2 page2;
    ArrayList<Passo> listaPassos = new ArrayList<>();
    String idAtividade;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passos);
        idAtividade = getIntent().getStringExtra("idAtividade");
        listaPassos = getIntent().getParcelableArrayListExtra("lista");
        Log.e("passos size", listaPassos.size()+"");
        page2 = findViewById(R.id.viewPager);

        adapter = new SliderAdapter(listaPassos);
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
                .document(idAtividade).update("status", "1").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("update", "updateRealizado");
            }
        });
        Toast.makeText(PassosActivity.this,"Atividade Concluída!", Toast.LENGTH_LONG).show();
        Intent dashboard = new Intent(PassosActivity.this, MinhaRotinaActivity.class);
        dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dashboard);
    }
    /*
    public void onClickOuvir(View v){
        int i = adapter.getItemCount();
        Log.e("i count", i+"");
        String descricao = listaPassos.get(i-1).getDescricaoPasso();
        int speech = textToSpeech.speak(descricao, TextToSpeech.QUEUE_FLUSH, null);
    }*/

}
