package com.example.meuprojeto.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email, senha;
    Button btEntrar, btCadastrese;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.emailLogin);
        senha = (EditText) findViewById(R.id.senhaLogin);
        btEntrar = (Button) findViewById(R.id.btEntrar);
        btCadastrese = (Button) findViewById(R.id.btCadastrese);

        btCadastrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando a ação para tela de cadastro:
                Intent cadUsuario = new Intent(LoginActivity.this, CadastrarUsuario.class);
                startActivity(cadUsuario);
            }
        });

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validando dados de email e senha para login:
                String email_login = email.getText().toString();
                String senha_login = senha.getText().toString();
                if(email_login == null || email_login.isEmpty()
                        || senha_login == null || senha_login.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Por favor, preencha todos os campos de email e senha!", Toast.LENGTH_SHORT);
                }
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email_login,senha_login)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.i("Sucesso ao logar", task.getResult().getUser().getUid());
                                Intent dashboard = new Intent(LoginActivity.this, DashboardActivity.class);
                                dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(dashboard);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Erro ao logar", e.getMessage());
                    }
                });
            }
        });
    }
}
