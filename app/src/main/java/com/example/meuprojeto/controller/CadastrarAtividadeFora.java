package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
    AtividadeAgendada objAtividadeFora;
    EditText descricao, data, dataPreparacao, horario;
    Button btCadastrarAtividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_atividade_fora);

        descricao = (EditText) findViewById(R.id.descricaoAtvFora);
        //horario = (EditText) findViewById(R.id.horarioPreparacao);
        data = (EditText) findViewById(R.id.dataAtvFora);
        dataPreparacao = (EditText) findViewById(R.id.dataPreparacao);
        btCadastrarAtividade = (Button) findViewById(R.id.btCadastrarAtvFora);
        objAtividadeFora = new AtividadeAgendada();

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
        final Calendar c = getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                c.set(YEAR, year);
                c.set(MONTH, month);
                c.set(DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        dataPreparacao.setText(simpleDateFormat.format(c.getTime()));
                    }
                };
                new TimePickerDialog(CadastrarAtividadeFora.this, timeSetListener,
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),true).show();
            }
        };
        new  DatePickerDialog(CadastrarAtividadeFora.this, dateSetListener, c.get(YEAR),
                c.get(MONTH), c.get(DAY_OF_MONTH)).show();
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


            objAtividadeFora.setNomeAtividade(descricao.getText().toString());
            objAtividadeFora.setData(data.getText().toString());
            objAtividadeFora.setDataPrevia(dataPreparacao.getText().toString());
            objAtividadeFora.setIdUsuario(usuario_atv);

            FirebaseFirestore.getInstance().collection("atividadesAgendadas")
                    .document(objAtividadeFora.getId())
                    .set(objAtividadeFora)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("Atividade cadastrada", objAtividadeFora.getId());

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
