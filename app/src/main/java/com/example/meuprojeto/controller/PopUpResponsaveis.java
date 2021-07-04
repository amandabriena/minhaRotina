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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
                String senhaResponsavel = senha.getText().toString();
                if(senhaResponsavel.length() < 6){
                    Toast.makeText(PopUpResponsaveis.this,"SENHA INCORRETA!", Toast.LENGTH_LONG).show();
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getEmail(),senhaResponsavel)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.i("Sucesso ao logar", task.getResult().getUser().getUid());
                                    Intent dashboard = new Intent(PopUpResponsaveis.this, DashboardActivity.class);
                                    dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    progress.dismiss();
                                    startActivity(dashboard);
                                } else {
                                    // If sign in fails, display a message to the user.
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
            }
        });
    }
}
