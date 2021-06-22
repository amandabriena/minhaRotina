package com.example.meuprojeto.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MinhaRotinaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Atividade> listaAtividades = new ArrayList<>();
    private String idUsuario;
    Button btEspacoPais;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_rotina);

        btEspacoPais = (Button) findViewById(R.id.btEspacoPais);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(listaAtividades);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        new CarregarListaAsynctask().execute();
        //PASSANDO PARA OUTRA PÁGINA AO CLICAR NA ATIVIDADE

        recyclerViewAdapter.setOnItemClickListener(new ClickListener<Atividade>() {
            @Override
            public void onItemClick(Atividade atividade) {
                Intent intent = new Intent(MinhaRotinaActivity.this, AtividadeActivity.class);
                intent.putExtra("atividade", atividade);
                intent.putExtra("idAtividade", atividade.getId());
                startActivity(intent);
                //Toast.makeText(MinhaRotinaActivity.this, atividade.getId(), Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);


        btEspacoPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando para tela de gerenciamento de pais ou responsáveis
                Intent pais = new Intent(MinhaRotinaActivity.this, PopUpResponsaveis.class);
                startActivity(pais);
            }
        });
    }
    public class CarregarListaAsynctask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // carregar do banco
            final String dia = verificarDiaSemana();
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
            return null;
        }
        @Override
        protected void onPostExecute(Void resultado) {
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }
    private String verificarDiaSemana(){
        Date d = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        String nome = "";
        int diaS = c.get(c.DAY_OF_WEEK);
        c.set(Calendar.HOUR_OF_DAY, d.getHours());
        c.set(Calendar.MINUTE, d.getMinutes());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Log.e("hora:", simpleDateFormat.format(c.getTime()));
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
