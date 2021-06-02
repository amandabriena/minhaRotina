package com.example.meuprojeto.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

public class GerAtividadesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapterGerenciador recyclerViewAdapter;
    private List<Atividade> listaAtividadesGer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ger_atividades);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewGer);
        recyclerViewAdapter = new RecyclerViewAdapterGerenciador(listaAtividadesGer);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        new CarregarListaAsynctask().execute();
        //PASSANDO PARA OUTRA PÁGINA AO CLICAR NA ATIVIDADE

        recyclerViewAdapter.setOnItemClickListener(new ClickListener<Atividade>() {
            @Override
            public void onItemClick(Atividade atividade) {
                Intent intent = new Intent(GerAtividadesActivity.this, AtividadeActivity.class);
                intent.putExtra("idAtividade", atividade.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
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
                                Log.e("nome", "nome av: "+atv.getNomeAtividade());
                                listaAtividadesGer.add(atv);
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
