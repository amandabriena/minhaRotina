package com.example.meuprojeto.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.meuprojeto.R;
import com.google.firebase.auth.FirebaseAuth;


public class DashboardActivity extends AppCompatActivity {
    //Declarando variáveis
    Button btGerAtividades, btCadastrarAtv, btEditar, btRotinaDiaria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        verificarAutenticacao();

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
                Intent rotinaHoje = new Intent(DashboardActivity.this, MinhaRotinaActivity.class);
                startActivity(rotinaHoje);
            }
        });

    }

    private void verificarAutenticacao() {
        if(FirebaseAuth.getInstance().getUid() == null){
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            verificarAutenticacao();
        }
        return super.onOptionsItemSelected(item);
    }
}
