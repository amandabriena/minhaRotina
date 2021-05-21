package com.example.meuprojeto.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class AtividadeActivity extends AppCompatActivity {
    EditText nome_atv, horario, musica;
    TextView texto;
    ImageView imagem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);
        String id = getIntent().getStringExtra("idAtividade");
        texto = (TextView) findViewById(R.id.textoAtv);
        nome_atv = (EditText) findViewById(R.id.nome_atividade);
        horario = (EditText) findViewById(R.id.horario);
        musica = (EditText) findViewById(R.id.musica);
        imagem = (ImageView) findViewById(R.id.imgAtv);

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
                    } else {
                        Log.d("Teste", "Atividade não encontrada");
                    }
                } else {
                    Log.d("Teste", "Falha: ", task.getException());
                }
            }
        });
    }
}
