package com.example.meuprojeto.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.meuprojeto.R;

public class MainActivity extends AppCompatActivity {

    Usuario objUsuario;

    //Declarando variáveis
    Button btnCadatrar;
    EditText nome, data, email, senha, genero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        objUsuario = new Usuario();

        //Atrelando variáveis as views
        btnCadatrar = (Button) findViewById(R.id.btnCadastrar);

        nome = (EditText) findViewById(R.id.nome);
        data = (EditText) findViewById(R.id.data);
        genero = (EditText) findViewById(R.id.genero);
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);


        //Capturando clique do botão cadastrar:
        btnCadatrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incluirUsuario();

                String dadosUsuario;
                dadosUsuario = "Nome: "+objUsuario.getNome()+"\n";
                dadosUsuario = "Email: "+objUsuario.getEmail()+"\n";
                dadosUsuario = "Data: "+objUsuario.getData()+"\n";
                dadosUsuario = "Genero: "+objUsuario.getGenero()+"\n";
                dadosUsuario = "Senha: "+objUsuario.getSenha()+"\n";

                Toast.makeText(getApplicationContext(), dadosUsuario, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void incluirUsuario(){
        objUsuario.setNome(nome.getText().toString());
        objUsuario.setData(data.getText().toString());
        objUsuario.setEmail(email.getText().toString());
        objUsuario.setSenha(senha.getText().toString());
        objUsuario.setGenero(genero.getText().toString());

    }
}
