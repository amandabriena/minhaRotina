package com.example.meuprojeto.controller;

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
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.model.AtividadeAgendada;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CadastrarAtividadeFora extends AppCompatActivity {
    AtividadeAgendada objAtividadeFora;
    EditText descricao, data, dataPreparacao, horario;
    Button btCadastrarAtividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_atividade_fora);

        descricao = (EditText) findViewById(R.id.descricaoAtvFora);
        horario = (EditText) findViewById(R.id.horarioPreparacao);
        data = (EditText) findViewById(R.id.dataAtvFora);
        dataPreparacao = (EditText) findViewById(R.id.dataPreparacao);
        btCadastrarAtividade = (Button) findViewById(R.id.btCadastrarAtvFora);
        objAtividadeFora = new AtividadeAgendada();

        btCadastrarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarAtividade();
                Intent intent = new Intent(CadastrarAtividadeFora.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void criarAtividade(){
        String descricaoAtv = descricao.getText().toString();
        String hora = horario.getText().toString();
        String dataAtv = data.getText().toString();
        String dataPrep = dataPreparacao.getText().toString();

        if(descricaoAtv.isEmpty() || hora.isEmpty() || dataAtv.isEmpty() || dataPrep.isEmpty()){
            Toast.makeText(this,"Preencha todos os campos para criar a atividade!", Toast.LENGTH_SHORT).show();
        }else {
            //Pegando o usuário que criou a atividade:
            String usuario_atv = FirebaseAuth.getInstance().getUid();


            objAtividadeFora.setNomeAtividade(descricao.getText().toString());
            objAtividadeFora.setHorario(horario.getText().toString());
            objAtividadeFora.setData(data.getText().toString());
            objAtividadeFora.setDataPrevia(dataPreparacao.getText().toString());
            objAtividadeFora.setIdUsuario(usuario_atv);

            FirebaseFirestore.getInstance().collection("atividadesAgendadas")
                    .document(objAtividadeFora.getId())
                    .set(objAtividadeFora)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("Atividade cadastrada", objAtividadeFora.getId());

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Erro ao cadastrar", e.getMessage());
                        }
                    });
        }
    }
}
