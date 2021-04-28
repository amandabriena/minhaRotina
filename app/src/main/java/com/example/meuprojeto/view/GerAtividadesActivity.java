package com.example.meuprojeto.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.meuprojeto.R;
import com.example.meuprojeto.datasource.DataSource;

import java.util.ArrayList;
import java.util.List;

public class GerAtividadesActivity extends AppCompatActivity {
    DataSource ds;
    ArrayAdapter<String> atividadeAdapter;
    List<String> atividades;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_atividades);

        ds = new DataSource();

        atividades = ds.listarAtividades();

        listView = (ListView) findViewById(R.id.lvAtividades);

        atividadeAdapter = new ArrayAdapter<String>(this, R.layout.detalhe_atividade, atividades);

        listView.setAdapter(atividadeAdapter);
    }
}
