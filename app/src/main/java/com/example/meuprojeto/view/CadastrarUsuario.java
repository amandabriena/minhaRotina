package com.example.meuprojeto.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.meuprojeto.R;
import com.example.meuprojeto.controller.UsuarioController;
import com.example.meuprojeto.model.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CadastrarUsuario extends AppCompatActivity {
    Usuario objUsuario;
    UsuarioController controleUsuario;
    //Declarando variáveis
    Button btProximo;
    EditText nome, data, email, senha, genero;
    //Conexão com o db
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        controleUsuario = new UsuarioController();
        btProximo = (Button) findViewById(R.id.btProximo);

        nome = (EditText) findViewById(R.id.nome);
        data = (EditText) findViewById(R.id.data);
        email = (EditText) findViewById(R.id.email);
        genero = (EditText) findViewById(R.id.genero);
        senha = (EditText) findViewById(R.id.senha);

        inicializarFirebase();
        //Capturando clique do botão cadastrar:
        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando a ação do botão para cadastrar nova atividade
                objUsuario= new Usuario();
                objUsuario.setId(UUID.randomUUID().toString());
                objUsuario.setNome(nome.getText().toString());
                objUsuario.setData(data.getText().toString());
                objUsuario.setEmail(email.getText().toString());
                objUsuario.setGenero(genero.getText().toString());
                objUsuario.setSenha(senha.getText().toString());
                controleUsuario.incluirUsuario(objUsuario);

                databaseReference.child("Usuario").child(objUsuario.getId()).setValue(objUsuario);
                limparDados();

            }
        });

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(CadastrarUsuario.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    //Função para incluir dados no objeto usuário
    private void incluirDadosUsuario(){
        objUsuario.setNome(nome.getText().toString());
        objUsuario.setData(data.getText().toString());
        objUsuario.setEmail(email.getText().toString());
        objUsuario.setSenha(senha.getText().toString());
        objUsuario.setGenero(genero.getText().toString());

    }
    //Função para limpar dados dos campos
    public void limparDados(){
        nome.setText("");
        data.setText("");
        email.setText("");
        senha.setText("");
        genero.setText("");

        nome.requestFocus();
    }
}
