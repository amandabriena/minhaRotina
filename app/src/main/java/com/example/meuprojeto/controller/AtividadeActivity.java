package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AtividadeActivity extends AppCompatActivity {
    EditText nome_atv, horario, musica;
    TextView texto;
    ImageView imagem;
    Button btIniciar;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);
        id = getIntent().getStringExtra("idAtividade");
        texto = (TextView) findViewById(R.id.textoAtv);
        nome_atv = (EditText) findViewById(R.id.nome_atividade);
        horario = (EditText) findViewById(R.id.horario);
        musica = (EditText) findViewById(R.id.musica);
        imagem = (ImageView) findViewById(R.id.imgAtv);
        btIniciar = (Button) findViewById(R.id.btIniciar);

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("/atividades").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Teste", "DocumentSnapshot data: " + document.getData());
                        Atividade atv = document.toObject(com.example.meuprojeto.model.Atividade.class);
                        String textoCompleto = "Olá! São "+atv.getHorario()+", hora de '"+atv.getNomeAtividade()+"'";
                        texto.setText(textoCompleto);
                        Picasso.get().load(atv.getImagemURL()).into(imagem);
                        new CarregarPassosAsynctask().execute();
                    } else {
                        Log.d("Teste", "Atividade não encontrada");
                    }
                } else {
                    Log.d("Teste", "Falha: ", task.getException());
                }
            }
        });
        btIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando a ação do botão para abrir a tela de atividades
                Intent intent = new Intent(AtividadeActivity.this, PassosAtividadeActivity.class);
                intent.putExtra("idAtividade", id);
                intent.putExtra("numPasso", "1");
                startActivity(intent);
            }
        });
    }
    public class CarregarPassosAsynctask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // carregar do banco
            FirebaseFirestore.getInstance().collection("/atividades").document(id).collection("passos").
                orderBy("numOrdem", Query.Direction.ASCENDING)
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
                                Log.e("Teste", passo.getDescricaoPasso());
                            }
                        }
                    });
            return null;
        }
        @Override
        protected void onPostExecute(Void resultado) {

        }
    }
}
