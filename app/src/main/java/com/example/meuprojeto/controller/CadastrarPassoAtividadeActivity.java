package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.datasource.AtividadeController;
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.model.Passo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class CadastrarPassoAtividadeActivity extends AppCompatActivity {
    Passo objPasso;

    //Declarando variáveis
    TextView atv_atual, ordemPasso;
    EditText descricao, som;
    Button btFinalizarPassos, btAddPassos;
    ImageButton btUploadImg;
    ImageView imgPasso;
    int numPasso;
    private Uri filePath;
    private byte[] dataIMG;
    String idAtividade;
    //Conexão com o db
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_passo_atividade);
        //Atrelando variáveis as views
        descricao = (EditText) findViewById(R.id.descricao);
        som = (EditText) findViewById(R.id.som);

        atv_atual = (TextView) findViewById(R.id.atividade_atual);
        ordemPasso = (TextView) findViewById(R.id.ordemPasso);

        btFinalizarPassos = (Button) findViewById(R.id.btFinalizar);
        btAddPassos = (Button) findViewById(R.id.btAddPassos);
        btUploadImg = (ImageButton) findViewById(R.id.btUploadImgPasso);
        imgPasso = (ImageView) findViewById(R.id.imgPasso);

        //Pegando informações da atividade que está sendo cadastrada:
        idAtividade = getIntent().getStringExtra("idAtividade");
        final String atividadeAtual = getIntent().getStringExtra("nomeAtividade");
        String num = getIntent().getStringExtra("numPasso");
        numPasso = Integer.parseInt(num);
        ordemPasso.setText("Passo "+numPasso+":");
        atv_atual.setText(atividadeAtual);


        btUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarImagem();
            }
        });
        btAddPassos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando para tela de gerenciamento de pais ou responsáveis
                adicionarPasso();
                Intent add = new Intent(CadastrarPassoAtividadeActivity.this, CadastrarPassoAtividadeActivity.class);
                //Passando número de ordem do passo
                numPasso++;
                add.putExtra("numPasso", numPasso+"");
                add.putExtra("idAtividade", idAtividade);
                add.putExtra("nomeAtividade", atividadeAtual);
                startActivity(add);
            }
        });
        btFinalizarPassos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando para tela de finaliza
                adicionarPasso();

                Intent intent = new Intent(CadastrarPassoAtividadeActivity.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    private void selecionarImagem(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK && data!=null && data.getData() !=null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                //Comprimindo o tamanho da imagem:
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                dataIMG = baos.toByteArray();

                imgPasso.setImageDrawable(new BitmapDrawable(bitmap));
                btUploadImg.setAlpha(0);
            }catch (IOException e){
                Toast.makeText(this,"Erro ao selecionar imagem! "+e, Toast.LENGTH_SHORT);
            }
        }
    }
    private void adicionarPasso(){
        String descricao_passo = descricao.getText().toString();
        String som_passo = som.getText().toString();

        if(descricao_passo.isEmpty() || som_passo.isEmpty()){
            Toast.makeText(this,"Preencha todos os campos para criar o Passo!", Toast.LENGTH_SHORT).show();
        }
            final String numOrdem = numPasso+"";

            objPasso = new Passo(numOrdem, descricao_passo, som_passo);

            FirebaseFirestore.getInstance().collection("atividades").document(idAtividade).collection("passos")
                    .document(objPasso.getNumOrdem())
                    .set(objPasso)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("Passo", objPasso.getDescricaoPasso());
                            Log.e("Passo ID ", numOrdem);

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
