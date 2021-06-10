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
    private List<Passo> listaPassos = new ArrayList<>();
    Passo passoAtual;
    Button btProximoF, btAnteriorF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passo_a_passo);
        int numPasso = 0;
        btAnteriorF = (Button) findViewById(R.id.btAnteriorF);
        btProximoF = (Button) findViewById(R.id.btProximoF);
        //listaPassos = getIntent().getParcelableArrayListExtra("lista");
        //Inicializando o fragmento
        FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction f = manager.beginTransaction();
        final PassoFragment fragmento = new PassoFragment();

        Bundle bundle = new Bundle();
        passoAtual = listaPassos.get(numPasso);
        bundle.putParcelable("passoAtual",passoAtual);
        fragmento.setArguments(bundle);
        f.add(R.id.frag, fragmento);
        f.commit();
        if(numPasso<=1){
            btAnteriorF.setAlpha(0);
            btProximoF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //numPasso++;
                    Bundle bundle2 = new Bundle();
                    //bundle2.putParcelable("passoAtual",listaPassos.get(numPasso));
                    FragmentManager managerF = getSupportFragmentManager();
                    final FragmentTransaction f2 = managerF.beginTransaction();
                    final PassoFragment fragmento2 = new PassoFragment();
                    fragmento2.setArguments(bundle2);
                    f2.replace(R.id.frag, fragmento2);
                    f2.commit();
                }
            });
        }else if(listaPassos.size() == numPasso){
            btProximoF.setText("CONCLUÍDO");
            btAnteriorF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Direcionando a ação do botão para abrir a tela de atividades
                    //numPasso--;
                    //iniciarFragmento(numPasso);
                }
            });
            btProximoF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Direcionando a ação do botão para abrir a tela de atividades
                    Intent dashboard = new Intent(PassoAPassoActivity.this, MinhaRotinaActivity.class);
                    Toast.makeText(PassoAPassoActivity.this,"Atividade Concluída", Toast.LENGTH_LONG).show();
                    dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dashboard);
                }
            });
        }else{
            btAnteriorF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //numPasso--;
                    //iniciarFragmento(numPasso);
                }
            });
            btProximoF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //numPasso++;
                    //iniciarFragmento(numPasso);
                }
            });
        }

    }

    public void iniciarFragmento(int numPasso, boolean next_prev){

        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("passoAtual",listaPassos.get(numPasso));
        FragmentManager managerF = getSupportFragmentManager();
        final FragmentTransaction f2 = managerF.beginTransaction();
        final PassoFragment fragmento2 = new PassoFragment();
        fragmento2.setArguments(bundle2);
        f2.replace(R.id.frag, fragmento2);
        f2.commit();
    }
}
