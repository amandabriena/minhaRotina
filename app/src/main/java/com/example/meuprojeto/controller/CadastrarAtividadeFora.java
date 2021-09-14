package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.model.AtividadeAgendada;
import com.example.meuprojeto.util.AlarmeAtividadeFora;
import com.example.meuprojeto.util.AlarmeAtividades;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class CadastrarAtividadeFora extends AppCompatActivity {
    AtividadeAgendada atividadeFora;
    EditText descricao, data, dataPreparacao, horario;
    Button btCadastrarAtividade;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_atividade_fora);

        descricao = (EditText) findViewById(R.id.descricaoAtvFora);
        //horario = (EditText) findViewById(R.id.horarioPreparacao);
        data = (EditText) findViewById(R.id.dataAtvFora);
        dataPreparacao = (EditText) findViewById(R.id.dataPreparacao);
        btCadastrarAtividade = (Button) findViewById(R.id.btCadastrarAtvFora);
        atividadeFora = new AtividadeAgendada();

        criarNotificacao();

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(data);
            }
        });
        dataPreparacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dataPreparacao);
            }
        });

        btCadastrarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarAtividade();
                Intent intent = new Intent(CadastrarAtividadeFora.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    private void showDateDialog(final EditText data){
        final Calendar c = getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                c.set(YEAR, year);
                c.set(MONTH, month);
                c.set(DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                data.setText(simpleDateFormat.format(c.getTime()));
            }
        };
        new  DatePickerDialog(CadastrarAtividadeFora.this, dateSetListener, c.get(YEAR),
                c.get(MONTH), c.get(DAY_OF_MONTH)).show();
    }

    private void showDateTimeDialog(final EditText dataPreparacao){
        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(YEAR, year);
                calendar.set(MONTH, month);
                calendar.set(DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        dataPreparacao.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(CadastrarAtividadeFora.this, timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
            }
        };
        new  DatePickerDialog(CadastrarAtividadeFora.this, dateSetListener, calendar.get(YEAR),
                calendar.get(MONTH), calendar.get(DAY_OF_MONTH)).show();
    }
    private void criarNotificacao(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "notificacaoAtividade";
            String description = "Canal paa notificar atividade";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("atividadeForaAlerta", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    //Setando alarme da atividade:
    private void setarAlarmeAtividade(){
        //alterando o dia da semana para incluir alarme:
        //calendar.set(Calendar.DAY_OF_WEEK, diaSemana);
        Log.e("Alarme", "Horario: "+calendar.getTime());
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(CadastrarAtividadeFora.this, AlarmeAtividadeFora.class);
        //O request code do peding intent será o id da atividade com ultimo digito numero referente ao dia da semana
        Log.e("Alarme", "id atv: "+atividadeFora.getId());
        int code =  Integer.parseInt(atividadeFora.getId());
        Log.e("Alarme", "Request code: "+code);
        pendingIntent = PendingIntent.getBroadcast(CadastrarAtividadeFora.this, code, i, 0);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(CadastrarAtividadeFora.this, "Alarme configurado para: "+calendar.getTime(), Toast.LENGTH_LONG).show();
    }
    private void criarAtividade(){
        String descricaoAtv = descricao.getText().toString();
        //String hora = horario.getText().toString();
        String dataAtv = data.getText().toString();
        String dataPrep = dataPreparacao.getText().toString();

        if(descricaoAtv.isEmpty() || dataAtv.isEmpty() || dataPrep.isEmpty()){
            Toast.makeText(this,"Preencha todos os campos para criar a atividade!", Toast.LENGTH_SHORT).show();
        }else {
            //Pegando o usuário que criou a atividade:
            String usuario_atv = FirebaseAuth.getInstance().getUid();


            atividadeFora.setNomeAtividade(descricao.getText().toString());
            atividadeFora.setData(data.getText().toString());
            atividadeFora.setDataPrevia(dataPreparacao.getText().toString());
            atividadeFora.setIdUsuario(usuario_atv);
            setarAlarmeAtividade();
            FirebaseFirestore.getInstance().collection("usuarios").document(usuario_atv).collection("atividadesAgendadas")
                    .document(atividadeFora.getId())
                    .set(atividadeFora)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("Atividade cadastrada:", atividadeFora.getId());

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Erro ao cadastrar", e.getMessage());
                        }
                    });
        }
    }
}
