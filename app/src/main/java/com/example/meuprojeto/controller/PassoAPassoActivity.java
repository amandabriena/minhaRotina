package com.example.meuprojeto.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.model.Passo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PassoAPassoActivity extends AppCompatActivity {
    TextView atividadeAtual;
    String idAtividade, atividade;
    private List<Passo> listaPassos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passo_a_passo);

        atividadeAtual = (TextView) findViewById(R.id.atividade_atual);
        idAtividade = getIntent().getStringExtra("idAtividade");
        atividade = getIntent().getStringExtra("nomeAtividade");

        atividadeAtual.setText(atividade);

        //Iniciando fragmento
        FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction f = manager.beginTransaction();
        final PassoFragment fragmento = new PassoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("idAtividade",idAtividade);
        bundle.putString("passoAtual","1");
        bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) listaPassos);
        fragmento.setArguments(bundle);
        f.add(R.id.frag, fragmento);
        f.commit();

    }
}
