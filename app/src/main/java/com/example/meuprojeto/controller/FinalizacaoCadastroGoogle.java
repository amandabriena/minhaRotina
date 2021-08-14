package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Usuario;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FinalizacaoCadastroGoogle extends AppCompatActivity {
    Usuario objUsuario;
    Button btCadastrar, btUploadImg;
    EditText senha;
    ImageView imgIcon;
    private Uri filePath;
    private byte[] dataIMG;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizacao_cadastro_google);

        btCadastrar = (Button) findViewById(R.id.btCadastrar);

        senha = (EditText) findViewById(R.id.senha);

        btUploadImg = (Button) findViewById(R.id.btUploadImgUser);
        imgIcon = (ImageView) findViewById(R.id.imgUser);
        progress = new ProgressDialog(this);

        btUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarImagem();
            }
        });
        //inicializarFirebase();
        //Capturando clique do botão cadastrar:
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando a ação do botão padastrar novo usuário:
                String senha_user = senha.getText().toString();
                if(senha_user.length() < 6){
                    Toast.makeText(FinalizacaoCadastroGoogle.this,"Sua senha deve conter ao menos 6 caracteres!", Toast.LENGTH_SHORT).show();
                }else if(dataIMG == null){
                    Toast.makeText(FinalizacaoCadastroGoogle.this,"Por gentileza adicione uma foto!", Toast.LENGTH_SHORT).show();
                }else if(senha_user.isEmpty()){
                    Toast.makeText(FinalizacaoCadastroGoogle.this,"Por favor, preencha todos a senha!", Toast.LENGTH_SHORT).show();
                }else{
                    progress.setMessage("Criando usuário..");
                    progress.show();
                    salvarUsuarioFirebase();
                }


            }
        });
    }
    private void selecionarImagem(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/user/*");
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData() !=null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                //Comprimindo o tamanho da imagem:
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                dataIMG = baos.toByteArray();

                imgIcon.setImageDrawable(new BitmapDrawable(bitmap));
                btUploadImg.setAlpha(0);
            }catch (IOException e){
                Toast.makeText(this,"Erro ao selecionar imagem! "+e, Toast.LENGTH_SHORT);
            }
        }
    }
    private void salvarUsuarioFirebase(){

        String fileName = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/usuarios"+fileName);
        UploadTask uploadTask2 = ref.putBytes(dataIMG);
        uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("Teste", uri.toString());
                        String uid = FirebaseAuth.getInstance().getUid();
                        String urlIMG = uri.toString();
                        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                        objUsuario= new Usuario();
                        objUsuario.setId(uid);
                        objUsuario.setEmail(account.getEmail());
                        objUsuario.setNome(account.getDisplayName());
                        objUsuario.setSenha(senha.getText().toString());
                        objUsuario.setImagemURL(urlIMG);
                        FirebaseFirestore.getInstance().collection("usuarios")
                                .document(uid)
                                .set(objUsuario)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progress.dismiss();
                                        Log.e("Cadastro","cadastrado com sucesso");
                                        Intent dashboard = new Intent(FinalizacaoCadastroGoogle.this, DashboardActivity.class);
                                        dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(dashboard);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progress.dismiss();
                                        Log.e("Erro ao cadastrar", e.getMessage());
                                    }
                                });
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
}
