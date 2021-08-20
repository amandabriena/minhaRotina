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

import java.util.ArrayList;
import java.util.List;

public class GerAtividadesActivity extends AppCompatActivity {
    Button btAdicionarAtividade;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterGerenciadorAtividades recyclerViewAdapter;
    private List<Atividade> listaAtividadesGer = new ArrayList<>();
    private boolean verificador = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ger_atividades);

        btAdicionarAtividade = (Button) findViewById(R.id.btAdicionarAtividade);
        btAdicionarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando a ação do botão para cadastrar uma nova atividade
                Intent addAtividade = new Intent(GerAtividadesActivity.this, CadastrarAtividadesInicialActivity.class);
                startActivity(addAtividade);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewGer);
        recyclerViewAdapter = new RecyclerViewAdapterGerenciadorAtividades(listaAtividadesGer);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        new CarregarListaAsynctask().execute();

        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListenerDeletar(new ClickListener<Atividade>() {
            @Override
            public void onItemClick(Atividade atividade) {
                Intent intent = new Intent(GerAtividadesActivity.this, PopupDeletarAtividadeActivity.class);
                intent.putExtra("atividade", atividade);
                intent.putExtra("idAtividade", atividade.getId());
                startActivityForResult(intent, 1);
                //startActivity(intent);
            }
        });
        recyclerViewAdapter.setOnItemClickListenerEditar(new ClickListener<Atividade>() {
            @Override
            public void onItemClick(Atividade atividade) {
                Intent intent = new Intent(GerAtividadesActivity.this, EditarAtividadeActivity.class);
                intent.putExtra("atividade", atividade);
                intent.putExtra("idAtividade", atividade.getId());
                intent.putExtra("atividadeBase", "false");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode == RESULT_OK){
            String  idAtividade = data.getStringExtra("idAtividade");
            Log.e("Atividade", "id:"+idAtividade);
            int index = buscarAtividade(idAtividade);
            Log.e("Atividade", "posicao atividade removido:"+index);
            listaAtividadesGer.remove(index);
            recyclerViewAdapter.notifyItemRemoved(index);
        }
    }

    public class CarregarListaAsynctask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // carregar do banco
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
                                Log.e("nome", "nome av: "+atv.getNomeAtividade());
                                if(verificador) {
                                    listaAtividadesGer.add(atv);
                                }
                            }
                            verificador = false;
                        }
                    });
            return null;
        }
        @Override
        protected void onPostExecute(Void resultado) {
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }
    public int buscarAtividade(String id) {
        for(int i = 0 ; i< listaAtividadesGer.size(); i++){
            Log.e("Atividade", "index atual:"+i);
            if (listaAtividadesGer.get(i).getId().equals(id)) {
                Log.e("Atividade", "entrou if");
                Log.e("Atividade", "index encontrado:"+i);
                return i;
            }
        }
        return -1;
    }
}
