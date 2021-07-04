package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.model.Passo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class CadastrarPassoActivity extends AppCompatActivity {
    Passo objPasso;
    //Declarando variáveis
    TextView atv_atual, ordemPasso;
    EditText descricao;
    Button btCadastrarPasso, btCancelar;
    ImageButton btUploadImg;
    ImageView imgPasso;
    int numPasso;
    private Atividade atividade;
    private Uri filePath;
    private byte[] dataIMG;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_passo);
        //Atrelando variáveis as views
        descricao = (EditText) findViewById(R.id.descricao);
        atv_atual = (TextView) findViewById(R.id.atividade_atual);
        ordemPasso = (TextView) findViewById(R.id.ordemPasso);
        btCadastrarPasso = (Button) findViewById(R.id.btCadastrarPasso);
        btCancelar = (Button) findViewById(R.id.btCancelar);
        btUploadImg = (ImageButton) findViewById(R.id.btUploadImgPasso);
        imgPasso = (ImageView) findViewById(R.id.imgPasso);

        objPasso = new Passo();

        progress = new ProgressDialog(this);

        String num = getIntent().getStringExtra("numPasso");
        //String nomeAtividade = getIntent().getStringExtra("nomeAtividade");
        numPasso = Integer.parseInt(num);
        objPasso.setNumOrdem(numPasso);
        ordemPasso.setText("Passo "+num);
        //atv_atual.setText(nomeAtividade);

        btUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarImagem();
            }
        });
        btCadastrarPasso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setMessage("Adicionando passo..");
                progress.show();
                cadastrarPasso();
            }
        });
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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


    public void cadastrarPasso(){
        String descricao_passo = descricao.getText().toString();
        if(dataIMG == null){
            Toast.makeText(this,"Por gentileza adicione uma imagem para a atividade!", Toast.LENGTH_SHORT).show();
        }else if(descricao_passo.isEmpty()){
            Toast.makeText(this,"Preencha todos os campos para criar o Passo!", Toast.LENGTH_SHORT).show();
        }else{
            String fileName = UUID.randomUUID().toString();
            //Uri uriAudio = Uri.fromFile(new File(getRecordFilePath()));
            final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/passos" + fileName);
            //UploadTask uploadTask1 = ref.putFile(uriAudio);
            UploadTask uploadTask = ref.putBytes(dataIMG);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.i("FILEURI", "URI: " + uri.toString());

                            objPasso.setDescricaoPasso(descricao.getText().toString());

                            objPasso.setImagemURL(uri.toString());
                            Intent intent=new Intent();
                            intent.putExtra("passo", objPasso);
                            Log.e("Passo", "data img:"+objPasso.getUri());
                            Log.e("Passo", "ordem Passo:"+numPasso);
                            Log.e("Passo", "Passo cadastrado:"+objPasso.getDescricaoPasso());
                            intent.putExtra("passo", objPasso);
                            setResult(RESULT_OK,intent);
                            progress.dismiss();
                            finish();


                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(Profilepic.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
                    Log.e("Teste", e.getMessage(), e);
                }
            });

        }
    }
}
