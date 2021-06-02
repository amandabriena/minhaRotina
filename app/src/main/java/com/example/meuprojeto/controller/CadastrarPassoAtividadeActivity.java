package com.example.meuprojeto.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.datasource.AtividadeController;
import com.example.meuprojeto.model.Atividade;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CadastrarPassoAtividadeActivity extends AppCompatActivity {
    Atividade objAtividade;
    AtividadeController controleAtividade;

    //Declarando variáveis
    EditText descricao, som;
    Button btFinalizarPassos, btAddPassos;
    ImageButton btUploadImg;
    ImageView imgPasso;
    int numPasso;
    private Uri filePath;
    private byte[] dataIMG;
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

        btFinalizarPassos = (Button) findViewById(R.id.btFinalizar);
        btUploadImg = (ImageButton) findViewById(R.id.btUploadImgPasso);
        imgPasso = (ImageView) findViewById(R.id.imgPasso);

        //Pegando informações da atividade que está sendo cadastrada:
        final String id = getIntent().getStringExtra("idAtividade");
        final String nome_atv = getIntent().getStringExtra("nome_atv");
        if(!getIntent().getStringExtra("numPasso").isEmpty()){
            numPasso = Integer.parseInt(getIntent().getStringExtra("numPasso"));
            numPasso++;
        }else{
            numPasso = 1;
        }



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
                Intent add = new Intent(CadastrarPassoAtividadeActivity.this, DashboardActivity.class);
                //Passando número de ordem do passo
                add.putExtra("numPasso", numPasso);
                add.putExtra("idAtividade", id);
                add.putExtra("nomeAtv", nome_atv);
                startActivity(add);
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
}
