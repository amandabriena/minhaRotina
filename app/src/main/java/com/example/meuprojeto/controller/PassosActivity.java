package com.example.meuprojeto.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Passo;
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
    List<Passo> listaPassos = new ArrayList<>();
    String idAtividade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passos);
        idAtividade = getIntent().getStringExtra("idAtividade");
        listaPassos = getIntent().getParcelableArrayListExtra("lista");
        page2 = findViewById(R.id.viewPager);

        adapter = new SliderAdapter(listaPassos);
        page2.setAdapter(adapter);

    }

}
