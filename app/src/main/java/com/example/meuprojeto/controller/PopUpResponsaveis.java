package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class PopUpResponsaveis extends AppCompatActivity {
    EditText senha;
    Button btAcessar;
    View view;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_responsaveis);

        //Setando as informações de dimensão do popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) ((dm.widthPixels) * 0.6);
        int height = (int) ((dm.heightPixels) * 0.25);
        getWindow().setLayout(width,height);

        senha = (EditText) findViewById(R.id.senhaResponsavel);
        btAcessar = (Button) findViewById(R.id.btAcessarResponsavel);
        view = (View) findViewById(R.id.popup);

        progress = new ProgressDialog(this);

        btAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if para verificar se a senha está correta else caso não esteja
                progress.setMessage("Acessando..");
                progress.show();
                final String senhaResponsavel = senha.getText().toString();
                if(senhaResponsavel.length() < 6){
                    Toast.makeText(PopUpResponsaveis.this,"SENHA INCORRETA!", Toast.LENGTH_LONG).show();
                }
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                FirebaseFirestore.getInstance().collection("usuarios")
                        .document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        Usuario u = doc.toObject(Usuario.class);
                        //Caso o usuário tenha logado com email e senha:
                        if(u.getSenha() == null){
                            Log.e("Login", "usuario auth");
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getEmail(),senhaResponsavel)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.e("Login", task.getResult().getUser().getUid());
                                                Intent dashboard = new Intent(PopUpResponsaveis.this, DashboardActivity.class);
                                                dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                progress.dismiss();
                                                startActivity(dashboard);
                                            } else {
                                                Log.e("Login", "erro ao logar");
                                                // Se houver erro no login:
                                                view.setBackgroundResource(R.drawable.button_round_delete);
                                                senha.setBackgroundResource(R.drawable.popup_error);
                                                progress.dismiss();
                                                Toast.makeText(PopUpResponsaveis.this,"SENHA INCORRETA!", Toast.LENGTH_LONG).show();
                                            }


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("Erro ao logar", e.getMessage());
                                        }
                                    });
                            //caso seja um usuário do google:
                        }else{
                            Log.e("Login", "usuario google");
                            //senha correta:
                            if(u.getSenha().equals(senhaResponsavel)){
                                Log.e("Login", "senha correta");
                                Intent dashboard = new Intent(PopUpResponsaveis.this, DashboardActivity.class);
                                dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                progress.dismiss();
                                startActivity(dashboard);
                            }else{
                                Log.e("Login", "senha incorreta");
                                view.setBackgroundResource(R.drawable.button_round_delete);
                                senha.setBackgroundResource(R.drawable.popup_error);
                                progress.dismiss();
                                Toast.makeText(PopUpResponsaveis.this,"SENHA INCORRETA!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Login","falha");
                    }
                });

            }
        });
    }
}
