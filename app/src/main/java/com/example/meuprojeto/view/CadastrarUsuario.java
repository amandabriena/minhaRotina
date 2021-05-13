package com.example.meuprojeto.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.controller.UsuarioController;
import com.example.meuprojeto.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CadastrarUsuario extends AppCompatActivity {
    Usuario objUsuario;
    UsuarioController controleUsuario;
    //Declarando variáveis
    Button btProximo;
    EditText nome, data, email, senha;
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
        senha = (EditText) findViewById(R.id.senha);

        inicializarFirebase();
        //Capturando clique do botão cadastrar:
        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando a ação do botão padastrar nova atividade
                objUsuario= new Usuario();
                objUsuario.setId(UUID.randomUUID().toString());
                objUsuario.setNome(nome.getText().toString());
                objUsuario.setData(data.getText().toString());
                objUsuario.setEmail(email.getText().toString());
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
    private void criarUsuario(){
        String nome_user = nome.getText().toString();
        String data_nasc = data.getText().toString();
        String email_user = email.getText().toString();
        String senha_user = senha.getText().toString();
        if(nome_user == null || nome_user.isEmpty() || data_nasc == null || data_nasc.isEmpty() || email_user == null || email_user.isEmpty()
        || senha_user == null || senha_user.isEmpty()){
            Toast.makeText(this,"Por favor, preencha todos os campos!", Toast.LENGTH_SHORT);
        }else{
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email_user,senha_user)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.i("Sucesso", task.getResult().getUser().getUid());
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Erro", e.getMessage());
                        }
                    });
        }

    }
    //Função para limpar dados dos campos
    public void limparDados(){
        nome.setText("");
        data.setText("");
        email.setText("");
        senha.setText("");

        nome.requestFocus();
    }
}
