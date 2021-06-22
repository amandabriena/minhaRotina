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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static java.util.Calendar.getInstance;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 2000;
    private List<Atividade> listaAtividades = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Bundle bundle = new Bundle();
        final String dia = verificarDiaSemana();
        Log.e("DIA", dia);

        //executar tarefas enquanto carrega a tela splash
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            FirebaseFirestore.getInstance().collection("usuarios")
                    .document(FirebaseAuth.getInstance().getUid()).collection("atividades")
                    .orderBy("horario", Query.Direction.ASCENDING)
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
                                if(atv.getDias_semana().contains(dia)){
                                    Log.e("Rotina", atv.getNomeAtividade());
                                    listaAtividades.add(atv);
                                }
                            }

                        }
                    });
            Intent intent = new Intent(SplashActivity.this, MinhaRotinaActivity.class);
            intent.putParcelableArrayListExtra("listaAtividades", (ArrayList<? extends Parcelable>) listaAtividades);
            intent.putExtra("idUsuario", user.getUid());
            finish();
            startActivity(intent);
        }

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
            }
        }, SPLASH_TIME_OUT);
        /*
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
                    intent.putParcelableArrayListExtra("listaAtividades", (ArrayList<? extends Parcelable>) listaAtividades);
                    intent.putExtra("idUsuario", user.getUid());
                    finish();
                    startActivity(intent);
                }

            }
        }, SPLASH_TIME_OUT);*/
    }
    private String verificarDiaSemana(){
        Date d = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(d); String nome = "";
        int diaS = c.get(c.DAY_OF_WEEK);
        String dia = "";
        switch(diaS){
            case Calendar.SUNDAY:
                dia = "Dom";
            break;
            case Calendar.MONDAY:
                dia = "Seg";
                break;
            case Calendar.TUESDAY:
                dia = "Ter";
                break;
            case Calendar.WEDNESDAY:
                dia = "Qua";
                break;
            case Calendar.THURSDAY:
                dia = "Qui";
                break;
            case Calendar.FRIDAY:
                dia = "Sex";
                break;
            case Calendar.SATURDAY:
                dia = "Sab";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + Calendar.DAY_OF_WEEK);
        }
        return dia;
    }
}
