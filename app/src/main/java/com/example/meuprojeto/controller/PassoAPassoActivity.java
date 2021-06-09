package com.example.meuprojeto.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    String idAtividade;
    private List<Passo> listaPassos = new ArrayList<>();
    Button btProximoF, btAnteriorF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passo_a_passo);

        btAnteriorF = (Button) findViewById(R.id.btAnteriorF);
        btProximoF = (Button) findViewById(R.id.btProximoF);

        idAtividade = getIntent().getStringExtra("idAtividade");
        listaPassos = getIntent().getParcelableArrayListExtra("lista");
        //listaPassos = getIntent().getStringArrayListExtra("lista");
        Log.e("PassoAtividade", idAtividade);

        //Inicializando o fragmento
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction f = manager.beginTransaction();
        PassoFragment fragmento = new PassoFragment();

        Bundle bundle = new Bundle();
        bundle.putString("ordemPasso", "1");
        bundle.putString("idAtividade", idAtividade);
        //bundle.putStringArrayList("lista", (ArrayList<String>) listaPassos);
        bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) listaPassos);
        fragmento.setArguments(bundle);
        f.add(R.id.frag, fragmento);
        f.commit();
        
        //getSupportFragmentManager().beginTransaction().add(R.id.frag, new PassoFragment()).commit();
        //passar o id da atividade para o fragment


    }
}
