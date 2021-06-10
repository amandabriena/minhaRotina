package com.example.meuprojeto.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.model.Passo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 2000;
    private List<Atividade> listaAtividades = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Bundle bundle = new Bundle();

        //executar tarefas enquanto carrega a tela splash
        FirebaseFirestore.getInstance().collection("/atividades").orderBy("horario", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e("Erro", error.getMessage());
                            return;
                        }
                        List<DocumentSnapshot> docs = value.getDocuments();
                        for(DocumentSnapshot doc : docs){

                            Atividade atv = doc.toObject(Atividade.class);
                            Log.e("ROTINA", atv.getNomeAtividade());
                            listaAtividades.add(atv);
                        }
                    }
                });

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null){
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(SplashActivity.this, MinhaRotinaActivity.class);
                    //intent.putParcelableArrayListExtra("listaAtividades", (ArrayList<? extends Parcelable>) listaAtividades);
                    finish();
                    startActivity(intent);
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
