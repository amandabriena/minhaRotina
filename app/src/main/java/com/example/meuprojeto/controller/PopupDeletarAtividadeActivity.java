package com.example.meuprojeto.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.util.Alarme;
import com.example.meuprojeto.util.AlarmeAtividades;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PopupDeletarAtividadeActivity extends AppCompatActivity {
    Button btCancelar, btDeletar;
    private String idAtividade;
    private Atividade atividade;
    private ProgressDialog progress;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_deletar_atividade);
        //Setando as informações de dimensão do popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) ((dm.widthPixels) * 0.6);
        int height = (int) ((dm.heightPixels) * 0.25);
        getWindow().setLayout(width,height);
        btCancelar = (Button) findViewById(R.id.btCancelar);
        btDeletar = (Button) findViewById(R.id.btConfirmar);
        view = (View) findViewById(R.id.popupDeletar);

        progress = new ProgressDialog(this);

        idAtividade = getIntent().getStringExtra("idAtividade");
        atividade = getIntent().getParcelableExtra("atividade");

        //Cancelando alarmes relacionados a atividade:
        for (String dia: atividade.getDias_semana()) {
            String idRequest = atividade.getId()+dia;
            Log.e("Alarme", "Request:"+idRequest);
            cancelarAlarme(Integer.parseInt(idRequest));
        }

        btDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setMessage("Excluindo atividade..");
                progress.show();
                //Deletando atividade:
                FirebaseFirestore.getInstance().collection("usuarios")
                        .document(FirebaseAuth.getInstance().getUid())
                        .collection("/atividades").document(idAtividade).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent=new Intent();
                        Log.e("Atividade", "Popup id:"+idAtividade);
                        intent.putExtra("idAtividade", idAtividade);
                        setResult(RESULT_OK,intent);
                        progress.dismiss();
                        finish();
                    }
                });

                /*Intent ger = new Intent(PopupDeletarAtividadeActivity.this, GerAtividadesActivity.class);
                ger.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ger);*/
            }
        });
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void cancelarAlarme(int requestCode){
        Log.e("Alarme", "Cancelando: "+requestCode);
        Intent intent = new Intent(this, AlarmeAtividades.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                requestCode, intent, 0);
        ((AlarmManager)getSystemService(ALARM_SERVICE)).cancel(pendingIntent);
    }
}
