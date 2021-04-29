package com.example.meuprojeto.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.meuprojeto.R;
import com.example.meuprojeto.controller.AtividadeController;
import com.example.meuprojeto.model.Atividade;

public class CadastrarAtividade extends AppCompatActivity {
    Atividade objAtividade;
    AtividadeController controleAtividade;

    //Declarando variáveis
    EditText nome_atv, horario, musica;
    Button btCadastrarAtividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_atividade);

        //Atrelando variáveis as views
        btCadastrarAtividade = (Button) findViewById(R.id.btCadastrarAtividade);

        nome_atv = (EditText) findViewById(R.id.nome_atividade);
        horario = (EditText) findViewById(R.id.horario);
        musica = (EditText) findViewById(R.id.musica);

        //Capturando clique do botão cadastrar:

    }
}
