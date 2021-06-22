package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.model.Passo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AtividadeActivity extends AppCompatActivity {
    EditText nome_atv, horario, musica;
    TextView texto;
    ImageView imagem;
    Button btIniciar;
    String id;
    Atividade atividade;
    private ArrayList<Passo> listaPassos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);
        id = getIntent().getStringExtra("idAtividade");
        atividade = getIntent().getParcelableExtra("atividade");
        new CarregarPassosAsynctask().execute();
        texto = (TextView) findViewById(R.id.textoAtv);
        nome_atv = (EditText) findViewById(R.id.nome_atividade);
        horario = (EditText) findViewById(R.id.horario);
        musica = (EditText) findViewById(R.id.musica);
        imagem = (ImageView) findViewById(R.id.imgAtv);
        btIniciar = (Button) findViewById(R.id.btIniciar);

        Log.e("passos size", listaPassos.size()+"");
        String textoCompleto = "Olá! São "+atividade.getHorario()+", hora de '"+atividade.getNomeAtividade()+"'";
        texto.setText(textoCompleto);
        Picasso.get().load(atividade.getImagemURL()).into(imagem);
            btIniciar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listaPassos.size()<=0){
                    //Direcionando a ação do botão para abrir a tela de dashboard pois a atividade não contem passos
                        new AtualizarStatusAsynctask().execute();
                        Intent intent = new Intent(AtividadeActivity.this, MinhaRotinaActivity.class);
                        startActivity(intent);
                    }else {
                        btIniciar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Direcionando a ação do botão para abrir a tela de atividades
                                Intent intent = new Intent(AtividadeActivity.this, PassosActivity.class);
                                intent.putExtra("idAtividade", id);
                                intent.putParcelableArrayListExtra("lista", (ArrayList<? extends Parcelable>) listaPassos);
                                startActivity(intent);
                            }
                        });
                    }
                }
            });

    }
    public class CarregarPassosAsynctask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // carregar do banco
            FirebaseFirestore.getInstance().collection("usuarios")
                    .document(FirebaseAuth.getInstance().getUid()).collection("atividades")
                    .document(id).collection("passos")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error != null){
                                Log.e("Erro", error.getMessage());
                                return;
                            }
                            List<DocumentSnapshot> docs = value.getDocuments();
                            for(DocumentSnapshot doc : docs){
                                Passo passo = doc.toObject(Passo.class);
                                Log.e("Passo:", passo.getDescricaoPasso());
                                listaPassos.add(passo);
                            }
                        }
                    });
            return null;
        }
        @Override
        protected void onPostExecute(Void resultado) {

        }
    }
    public class AtualizarStatusAsynctask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // carregar do banco
            FirebaseFirestore.getInstance().collection("usuarios")
                    .document(FirebaseAuth.getInstance().getUid()).collection("atividades")
                    .document(id).update("status", "1").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("update", "updateRealizado");
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void resultado) {

        }
    }
}
