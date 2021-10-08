package com.example.meuprojeto.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.AtividadeAgendada;

import java.util.ArrayList;
import java.util.List;

public class PopUpAtividadesAgendadas extends AppCompatActivity {
    private List<AtividadeAgendada> listaAtividades = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewAdapterAtividadesAgendadas recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Definindo Dimensões do popup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_atividades_agendadas);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) ((dm.widthPixels) * 0.9);
        int height = (int) ((dm.heightPixels) * 0.8);
        getWindow().setLayout(width, height);


        listaAtividades = getIntent().getParcelableArrayListExtra("atividades");
        Log.e("Alarme", "size lista:"+listaAtividades.size());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapterAtividadesAgendadas(listaAtividades);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListenerDeletar(new ClickListener<AtividadeAgendada>() {
            @Override
            public void onItemClick(AtividadeAgendada atividade) {
                Intent intent = new Intent(PopUpAtividadesAgendadas.this, PopupDeletarAtividadeActivity.class);
                intent.putExtra("atividade", atividade);
                intent.putExtra("idAtividade", atividade.getId());
                startActivityForResult(intent, 1);
                //startActivity(intent);
            }
        });
        recyclerViewAdapter.setOnItemClickListenerEditar(new ClickListener<AtividadeAgendada>() {
            @Override
            public void onItemClick(AtividadeAgendada atividade) {
                Intent intent = new Intent(PopUpAtividadesAgendadas.this, EditarAtividadeActivity.class);
                intent.putExtra("atividade", atividade);
                intent.putExtra("idAtividade", atividade.getId());
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
            listaAtividades.remove(index);
            recyclerViewAdapter.notifyItemRemoved(index);
        }
    }
    public int buscarAtividade(String id) {
        for(int i = 0 ; i< listaAtividades.size(); i++){
            Log.e("Atividade", "index atual:"+i);
            if (listaAtividades.get(i).getId().equals(id)) {
                Log.e("Atividade", "entrou if");
                Log.e("Atividade", "index encontrado:"+i);
                return i;
            }
        }
        return -1;
    }
}

