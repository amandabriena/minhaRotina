package com.example.meuprojeto.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.meuprojeto.R;
import com.example.meuprojeto.controller.UsuarioController;
import com.example.meuprojeto.model.Usuario;


public class DashboardActivity extends AppCompatActivity {

    Usuario objUsuario;
    UsuarioController controleUsuario;

    //Declarando variáveis
    Button btnCadatrar;
    EditText nome, data, email, senha, genero;
    Button btGerAtividades;
    Button btCadastrarAtv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        objUsuario = new Usuario();
        controleUsuario = new UsuarioController();
        btGerAtividades = (Button) findViewById(R.id.btGerAtividades);
        btCadastrarAtv = (Button) findViewById(R.id.btAddAtividadeFora);
        btGerAtividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando a ação do botão para abrir a tela de atividades
                Intent gerAtividades = new Intent(DashboardActivity.this, GerAtividadesActivity.class);
                startActivity(gerAtividades);
            }
        });
        btCadastrarAtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando a ação do botão para abrir a tela de atividades
                Intent addAtividade = new Intent(DashboardActivity.this, CadastrarAtividade.class);
                startActivity(addAtividade);
            }
        });


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
