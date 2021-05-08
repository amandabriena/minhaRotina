package com.example.meuprojeto.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.meuprojeto.R;
import com.example.meuprojeto.controller.AtividadeController;
import com.example.meuprojeto.controller.RecyclerViewFragment;
import com.example.meuprojeto.datasource.DataSource;

import java.util.ArrayList;
import java.util.List;

public class GerAtividadesActivity extends AppCompatActivity {
    ArrayAdapter<String> atividadeAdapter;
    List<String> atividades;
    ListView listView;
    AtividadeController atvController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_atividades);
        /*
        if(savedInstanceState == null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment fragment = new RecyclerViewFragment();

            transaction.replace(R.id.lvAtividades, fragment);
            transaction.commit();
        }*/

        atvController = new AtividadeController();

        atividades = atvController.listarDados();

        listView = (ListView) findViewById(R.id.lvAtividades);

        atividadeAdapter = new ArrayAdapter<String>(this, R.layout.detalhe_atividade, atividades);

        listView.setAdapter(atividadeAdapter);
    }
}
