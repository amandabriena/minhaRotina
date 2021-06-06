package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.model.Passo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class PassosAtividadeActivity extends AppCompatActivity {
    String idAtividade;
    int numPasso;
    TextView passoAtual, descricaoPasso;
    Button btAnterior, btProximo, btConcluido;
    ImageView imgPasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passos_atividade);

        passoAtual = (TextView) findViewById(R.id.numPassoAtual);
        descricaoPasso = (TextView) findViewById(R.id.descricaoPasso);
        btAnterior = (Button) findViewById(R.id.btAnterior);
        btProximo = (Button) findViewById(R.id.btProximo);
        imgPasso = (ImageView) findViewById(R.id.imgPassoAtual);

        //Coletando as informações do passo atual
        idAtividade = getIntent().getStringExtra("idAtividade");
        String num = getIntent().getStringExtra("numPasso");

        numPasso = Integer.parseInt(num);
        passoAtual.setText("Passo "+num+":");

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("/atividades").document(idAtividade)
                .collection("passos").document(num);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Teste", "DocumentSnapshot data: " + document.getData());
                        Passo passo = document.toObject(com.example.meuprojeto.model.Passo.class);

                        descricaoPasso.setText(passo.getDescricaoPasso());
                        Picasso.get().load(passo.getImagemURL()).into(imgPasso);
                    } else {
                        Log.d("Teste", "Passo não encontrado");
                    }
                } else {
                    Log.d("Teste", "Falha: ", task.getException());
                }
            }
        });
        //Tirando a visibilidade dos botões caso seja o primeiro passo ou o último
        if(numPasso<=1){
            btAnterior.setAlpha(0);
            btProximo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Direcionando a ação do botão para abrir a tela de atividades
                    Intent intent = new Intent(PassosAtividadeActivity.this, PassosAtividadeActivity.class);
                    numPasso++;
                    intent.putExtra("idAtividade", idAtividade);
                    intent.putExtra("numPasso", numPasso+"");
                    startActivity(intent);
                }
            });
        }else if(numPasso>3){
            btProximo.setText("CONCLUÍDO");
            btAnterior.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Direcionando a ação do botão para abrir a tela de atividades
                    Intent intent = new Intent(PassosAtividadeActivity.this, PassosAtividadeActivity.class);
                    numPasso--;
                    intent.putExtra("idAtividade", idAtividade);
                    intent.putExtra("numPasso", numPasso+"");
                    startActivity(intent);
                }
            });
            btProximo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Direcionando a ação do botão para abrir a tela de atividades
                    Intent dashboard = new Intent(PassosAtividadeActivity.this, MinhaRotinaActivity.class);
                    Toast.makeText(PassosAtividadeActivity.this,"Atividade Concluída", Toast.LENGTH_LONG).show();
                    dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dashboard);
                }
            });
        }else{
            btAnterior.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Direcionando a ação do botão para abrir a tela de atividades
                    Intent intent = new Intent(PassosAtividadeActivity.this, PassosAtividadeActivity.class);
                    numPasso--;
                    intent.putExtra("idAtividade", idAtividade);
                    intent.putExtra("numPasso", numPasso+"");
                    startActivity(intent);
                }
            });
            btProximo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Direcionando a ação do botão para abrir a tela de atividades
                    Intent intent = new Intent(PassosAtividadeActivity.this, PassosAtividadeActivity.class);
                    numPasso++;
                    intent.putExtra("idAtividade", idAtividade);
                    intent.putExtra("numPasso", numPasso+"");
                    startActivity(intent);
                }
            });
        }




    }
}
