package com.example.meuprojeto.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meuprojeto.R;
import com.example.meuprojeto.controller.AtividadeController;
import com.example.meuprojeto.model.Atividade;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CadastrarAtividade extends AppCompatActivity {
    Atividade objAtividade;
    AtividadeController controleAtividade;

    //Declarando variáveis
    EditText nome_atv, horario, musica;
    Button btCadastrarAtividade;

    //Conexão com o db
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_atividade);

        //Atrelando variáveis as views
        btCadastrarAtividade = (Button) findViewById(R.id.btCadastrarAtividade);

        nome_atv = (EditText) findViewById(R.id.nome_atividade);
        horario = (EditText) findViewById(R.id.horario);
        musica = (EditText) findViewById(R.id.musica);

        inicializarFirebase();
        //Capturando clique do botão cadastrar:
        btCadastrarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando a ação do botão para cadastrar nova atividade
                objAtividade = new Atividade();

                objAtividade.setId(UUID.randomUUID().toString());
                objAtividade.setNome_atividade(nome_atv.getText().toString());
                objAtividade.setHorario(horario.getText().toString());
                objAtividade.setMusica(musica.getText().toString());
                databaseReference.child("Atividade").child(objAtividade.getId()).setValue(objAtividade);
                limparDadosAtv();


            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(CadastrarAtividade.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    private void incluirDadosAtividade(){
        //criando ID randomico e demais informações preenchidas:
        objAtividade.setId(UUID.randomUUID().toString());
        objAtividade.setNome_atividade(nome_atv.getText().toString());
        objAtividade.setHorario(horario.getText().toString());
        objAtividade.setMusica(musica.getText().toString());
        databaseReference.child("Atividade").child(objAtividade.getId()).setValue(objAtividade);
        limparDadosAtv();
    }

    //Função para limpar dados dos campos
    public void limparDadosAtv(){
        nome_atv.setText("");
        horario.setText("");
        musica.setText("");
    }
}
