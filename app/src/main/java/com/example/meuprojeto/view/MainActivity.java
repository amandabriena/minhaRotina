package com.example.meuprojeto.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.controller.UsuarioController;
import com.example.meuprojeto.model.Usuario;


public class MainActivity extends AppCompatActivity {

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
                Intent gerAtividades = new Intent(MainActivity.this, GerAtividadesActivity.class);
                startActivity(gerAtividades);
            }
        });
        btCadastrarAtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando a ação do botão para abrir a tela de atividades
                Intent addAtividade = new Intent(MainActivity.this, CadastrarAtividade.class);
                startActivity(addAtividade);
            }
        });
        //Atrelando variáveis as views
        /*btnCadatrar = (Button) findViewById(R.id.btnCadastrar);

        nome = (EditText) findViewById(R.id.nome);
        data = (EditText) findViewById(R.id.data);
        genero = (EditText) findViewById(R.id.genero);
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);


        //Capturando clique do botão cadastrar:
        btnCadatrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Incluindo no objeto usuários os dados do formulário
                incluirDadosUsuario();

                //Salvando os dados na controller
                controleUsuario.incluirUsuario(objUsuario);

                Toast.makeText(getApplicationContext(), controleUsuario.toString(), Toast.LENGTH_LONG).show();
                limparDados();
            }
        });*/

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
