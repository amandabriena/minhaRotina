package com.example.meuprojeto.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class CadastrarAtividade extends AppCompatActivity {
    Atividade objAtividade;
    AtividadeController controleAtividade;

    //Declarando variáveis
    EditText nome_atv, horario, musica;
    Button btCadastrarAtividade;
    ImageButton btUploadImg;
    ImageView imgIcon;
    private Uri filePath;

    //Conexão com o db
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_atividade);
        nome_atv = (EditText) findViewById(R.id.nome_atividade);
        horario = (EditText) findViewById(R.id.horario);
        musica = (EditText) findViewById(R.id.musica);
        //Atrelando variáveis as views
        btCadastrarAtividade = (Button) findViewById(R.id.btCadastrarAtividade);
        btUploadImg = (ImageButton) findViewById(R.id.btUploadImg);
        imgIcon = (ImageView) findViewById(R.id.imgIcon);


        inicializarFirebase();
        //Capturando clique do botão cadastrar:
        btCadastrarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarAtividade();
            }
        });
        btUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarImagem();
            }
        });
    }
    private void selecionarImagem(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    private void uploadImagem(){
        String fileName = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/"+fileName);
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("Teste", uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Teste",e.getMessage(),e);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK && data!=null && data.getData() !=null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imgIcon.setImageDrawable(new BitmapDrawable(bitmap));
                btUploadImg.setAlpha(0);
            }catch (IOException e){
                Toast.makeText(this,"Erro ao selecionar imagem! "+e, Toast.LENGTH_SHORT);
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
    private void criarAtividade(){
        //criando ID randomico e demais informações preenchidas:
        String nome = nome_atv.getText().toString();
        String hora = horario.getText().toString();
        String musica_atv = musica.getText().toString();
        if(nome == null || nome.isEmpty() || hora == null || hora.isEmpty() || musica_atv == null || musica_atv.isEmpty()){
            Toast.makeText(this,"Preencha todos os campos para criar a atividade!", Toast.LENGTH_SHORT);
        }else{
            objAtividade = new Atividade(nome, hora, musica_atv);
            databaseReference.child("Atividade").child(objAtividade.getId()).setValue(objAtividade);
            uploadImagem();
            limparDadosAtv();
        }
    }

    //Função para limpar dados dos campos
    public void limparDadosAtv(){
        nome_atv.setText("");
        horario.setText("");
        musica.setText("");
    }
}
