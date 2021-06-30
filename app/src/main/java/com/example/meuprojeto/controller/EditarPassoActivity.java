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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Passo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class EditarPassoActivity extends AppCompatActivity {
    Passo passo;
    private Uri filePath;
    private byte[] dataIMG;
    TextView ordemPasso;
    ImageButton btFechar;
    Button btExcluir, btAtualizar;
    EditText descricao;
    ImageButton btUploadImg;
    ImageView imgPasso;
    String idAtividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_passo);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) ((dm.widthPixels) * 0.9);
        int height = (int) ((dm.heightPixels) * 0.8);
        getWindow().setLayout(width,height);

        passo = getIntent().getParcelableExtra("passo");
        Log.e("passo atual ", "passo"+passo.getId());
        idAtividade = getIntent().getStringExtra("idAtividade");

        descricao = (EditText) findViewById(R.id.descricao);
        ordemPasso = (TextView) findViewById(R.id.ordemPasso);
        btFechar = (ImageButton) findViewById(R.id.btFechar);
        btExcluir = (Button) findViewById(R.id.btExcluirPasso);
        btAtualizar = (Button) findViewById(R.id.btAtualizar);
        btUploadImg = (ImageButton) findViewById(R.id.btUploadImgPasso);
        imgPasso = (ImageView) findViewById(R.id.imgPasso);

        ordemPasso.setText("Passo "+passo.getNumOrdem());
        descricao.setHint(passo.getDescricaoPasso());
        Picasso.get().load(passo.getImagemURL()).into(imgPasso);

        btUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarImagem();
            }
        });
        //Atualizar informações do passo
        btAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarPasso();
                Toast.makeText(EditarPassoActivity.this,"Passo atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        //Excluir passo:
        btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("passo excluido ", "excluido"+passo.getId());
                FirebaseFirestore.getInstance().collection("usuarios")
                        .document(FirebaseAuth.getInstance().getUid())
                        .collection("/atividades").document(idAtividade)
                        .collection("passos").document(passo.getId()).delete();
                Log.e("passo excluido ", "excluido");
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        //fechar popup de atualização:
        btFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void atualizarPasso(){
        String descricaoPasso = descricao.getText().toString();
        if(descricaoPasso != null){
            FirebaseFirestore.getInstance().collection("usuarios")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("/atividades").document(idAtividade)
                    .collection("passos").document(passo.getId()).update("descricaoPasso",descricaoPasso);
        }
        if(dataIMG != null){
            String fileName = UUID.randomUUID().toString();
            final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/passos" + fileName);
            UploadTask uploadTask2 = ref.putBytes(dataIMG);
            uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.i("FILEURI", "URI: " + uri.toString());

                            passo.setImagemURL(uri.toString());
                            //objPasso.setAudio(idAtividade+"passo"+ordemPasso+".3gp");
                            FirebaseFirestore.getInstance().collection("usuarios")
                                    .document(FirebaseAuth.getInstance().getUid()).collection("atividades")
                                    .document(idAtividade).collection("passos")
                                    .document(passo.getId())
                                    .update("imagemURL", uri.toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.e("numPasso ", "ok");

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("Erro alterar imagem", e.getMessage());
                                        }
                                    });

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
