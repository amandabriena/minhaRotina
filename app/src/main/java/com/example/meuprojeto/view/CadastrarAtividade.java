package com.example.meuprojeto.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meuprojeto.R;
import com.example.meuprojeto.controller.AtividadeController;
import com.example.meuprojeto.model.Atividade;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.UUID;

public class CadastrarAtividade extends AppCompatActivity {
    Atividade objAtividade;
    AtividadeController controleAtividade;

    //Declarando variáveis
    EditText nome_atv, horario, musica;
    Button btCadastrarAtividade, btUploadImg;
    ImageView imgIcon;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    //Conexão com o db
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //FirebaseStorage storage;
    //StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_atividade);
        nome_atv = (EditText) findViewById(R.id.nome_atividade);
        horario = (EditText) findViewById(R.id.horario);
        musica = (EditText) findViewById(R.id.musica);
        //Atrelando variáveis as views
        btCadastrarAtividade = (Button) findViewById(R.id.btCadastrarAtividade);
        btUploadImg = (Button) findViewById(R.id.btUploadImg);
        imgIcon = (ImageView) findViewById(R.id.imgIcon);


        inicializarFirebase();
        //Capturando clique do botão cadastrar:
        btCadastrarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando a ação do botão para cadastrar nova atividade
                objAtividade = new Atividade(nome_atv.getText().toString(), horario.getText().toString(), musica.getText().toString());
                databaseReference.child("Atividade").child(objAtividade.getId()).setValue(objAtividade);
                limparDadosAtv();
            }
        });
        btUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarImagem();
                uploadImage();
            }
        });
    }
    private void selecionarImagem(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    private void uploadImage(){
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK && data!=null && data.getData() !=null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imgIcon.setImageDrawable(new BitmapDrawable(bitmap));
            }catch (IOException e){

            }
        }
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(CadastrarAtividade.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    /*
    private void inicializarStorage(){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }*/
    private void incluirDadosAtividade(){
        //criando ID randomico e demais informações preenchidas:
        objAtividade.setId(UUID.randomUUID().toString());
        objAtividade.setNome_atividade(nome_atv.getText().toString());
        objAtividade.setHorario(horario.getText().toString());
        objAtividade.setMusica(musica.getText().toString());
        databaseReference.child("Atividade").child(objAtividade.getId()).setValue(objAtividade);
        limparDadosAtv();
    }

    //Função para limpar dados dos campos
    public void limparDadosAtv(){
        nome_atv.setText("");
        horario.setText("");
        musica.setText("");
    }
}
