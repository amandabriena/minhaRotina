package com.example.meuprojeto.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class PassosActivity extends AppCompatActivity {
    SliderAdapter adapter;
    ViewPager2 page2;
    ArrayList<Passo> listaPassos = new ArrayList<>();
    String idAtividade;
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

}
