package com.example.meuprojeto.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Passo;

import java.util.ArrayList;
import java.util.List;

public class OldPassoAPassoActivity extends AppCompatActivity {
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
        final OldPassoFragment fragmento = new OldPassoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("idAtividade",idAtividade);
        bundle.putString("passoAtual","1");
        bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) listaPassos);
        fragmento.setArguments(bundle);
        f.add(R.id.frag, fragmento);
        f.commit();

    }
}
