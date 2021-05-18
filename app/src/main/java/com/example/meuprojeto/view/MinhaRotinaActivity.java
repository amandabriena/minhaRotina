package com.example.meuprojeto.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.controller.RecyclerViewAdapter;
import com.example.meuprojeto.model.Atividade;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MinhaRotinaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Atividade> listaAtividades = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_rotina);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(listaAtividades);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        new CarregarListaAsynctask().execute();
        //PASSANDO PARA OUTRA PÁGINA AO CLICAR NA ATIVIDADE
    /*
    recyclerViewAdapter.setOnItemClickListener(new ClickListener<Passaro>() {
        @Override
        public void onItemClick(Atividade atividade) {
            Intent intent = new Intent(MainActivity.this, PaginaBuscaActivity.class);
            intent.putExtra(Constantes.NOME_PASSARO_CHAVE, atividade.getNome());
            startActivity(intent);
            Toast.makeText(MainActivity.this, passaro.getNome(), Toast.LENGTH_LONG).show();
        }
    });*/
        recyclerView.setAdapter(recyclerViewAdapter);
    }
    public class CarregarListaAsynctask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // carregar do banco
            FirebaseFirestore.getInstance().collection("/atividades")
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

                                listaAtividades.add(atv);
                            }
                        }
                    });
            //Polícia-inglesa-do-norte (Sturnella militaris):
            //listaPassaros.add(new Passaro("Polícia-inglesa-do-norte", R.drawable.policia_inglesa_do_norte, "Sturnella militaris"));
            //listaAtividades.add(new Atividade("Escovar Dentes", R.drawable.escovar_dentes,"8:00","fada do Dente" ));
            //listaAtividades.add(new Atividade("Passarinho", R.drawable.policia_inglesa_do_norte,"8:10","Fada do Dente" ));
            return null;
        }
        @Override
        protected void onPostExecute(Void resultado) {
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }



}
