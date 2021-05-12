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
    //Declarando variáveis
    Button btGerAtividades, btCadastrarAtv, btEditar, btRotinaDiaria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btGerAtividades = (Button) findViewById(R.id.btGerAtividades);
        btCadastrarAtv = (Button) findViewById(R.id.btAddAtividadeFora);
        btRotinaDiaria = (Button) findViewById(R.id.btRotinaDiaria);
        btEditar = (Button) findViewById(R.id.btEditar);

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
        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando a ação do botão para abrir a edição do usuário
                Intent cadUsuario = new Intent(DashboardActivity.this, CadastrarUsuario.class);
                startActivity(cadUsuario);
            }
        });
        btRotinaDiaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando para visão da rotina diária
                Intent rotinaHoje = new Intent(DashboardActivity.this, minhaRotinaActivity.class);
                startActivity(rotinaHoje);
            }
        });

    }

}
