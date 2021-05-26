package com.example.meuprojeto.view;

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
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MinhaRotinaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Atividade> listaAtividades = new ArrayList<>();
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
                Intent pais = new Intent(MinhaRotinaActivity.this, DashboardActivity.class);
                pais.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(pais);
            }
        });
    }
    public class CarregarListaAsynctask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // carregar do banco
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
                                Log.e("Teste", atv.getNomeAtividade());
                                listaAtividades.add(atv);
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



}
