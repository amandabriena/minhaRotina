package com.example.meuprojeto.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class DashboardActivity extends AppCompatActivity {
    //Declarando variáveis
    Button btGerAtividades, btCadastrarAtv, btEditar, btRotinaDiaria;
    ImageView imgIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        verificarAutenticacao();

        imgIcon = (ImageView) findViewById(R.id.imgIcon);
        btGerAtividades = (Button) findViewById(R.id.btGerAtividades);
        btCadastrarAtv = (Button) findViewById(R.id.btAddAtividadeFora);
        btRotinaDiaria = (Button) findViewById(R.id.btRotinaDiaria);
        btEditar = (Button) findViewById(R.id.btEditar);

        //Pegando imagem de icone do usuário
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("/usuarios").document(FirebaseAuth.getInstance().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Teste", "DocumentSnapshot data: " + document.getData());
                        Usuario user = document.toObject(com.example.meuprojeto.model.Usuario.class);
                        Picasso.get().load(user.getImagemURL()).into(imgIcon);
                    } else {
                        Log.d("Teste", "Atividade não encontrada");
                    }
                } else {
                    Log.d("Teste", "Falha: ", task.getException());
                }
            }
        });

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
        if(FirebaseAuth.getInstance().getUid().isEmpty()){
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
