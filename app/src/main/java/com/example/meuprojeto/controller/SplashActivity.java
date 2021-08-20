package com.example.meuprojeto.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.util.Alarme;
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
    //private String user;
    private static final int SPLASH_TIME_OUT = 2000;
    private List<Atividade> listaAtividades = new ArrayList<>();
    private int code_alarm = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent = new Intent(this, Alarme.class);

        //Verificando se a tarefa de resetar status da atividade já foi definida anteriormente:
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,code_alarm,intent,PendingIntent.FLAG_NO_CREATE);

        if(pendingIntent == null){
            Log.e("Alarme", "Alarme não definido");
            atualizarStatusAtividade();
        }

        final String dia = verificarDiaSemana();
        Log.e("DIA", dia);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //executar tarefas enquanto carrega a tela splash
        if(user !=null){
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
                                    Log.e("Rotina", atv.getId());
                                    Log.e("Rotina", atv.getStatus());
                                    listaAtividades.add(atv);
                                }
                            }

                        }
                    });
                    //Resetando status das atividades à meia noite:
                    //atualizarStatusAtividade();
        }



        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
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
        }, SPLASH_TIME_OUT);
    }
    private void atualizarStatusAtividade(){
        //Setando o horário para resetar atividades:
        Date d = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 00);
        long time = cal.getTimeInMillis();

        Log.e("Alarme", cal.getTimeInMillis()+"");
        Log.e("Alarme", "Time: "+cal.getTime());

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alarme.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, code_alarm, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private String verificarDiaSemana(){
        Date d = new Date();
        Calendar c = new GregorianCalendar();
        Log.e("Rotina", "Verifiação dias: "+c.getTime());
        c.setTime(d); String nome = "";
        int diaS = c.get(c.DAY_OF_WEEK);
        String dia = "";
        switch(diaS){
            case Calendar.SUNDAY:
                dia = "1";
            break;
            case Calendar.MONDAY:
                dia = "2";
                break;
            case Calendar.TUESDAY:
                dia = "3";
                break;
            case Calendar.WEDNESDAY:
                dia = "4";
                break;
            case Calendar.THURSDAY:
                dia = "5";
                break;
            case Calendar.FRIDAY:
                dia = "6";
                break;
            case Calendar.SATURDAY:
                dia = "7";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + Calendar.DAY_OF_WEEK);
        }
        return dia;
    }
}
