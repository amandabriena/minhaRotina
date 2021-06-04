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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.datasource.UsuarioController;
import com.example.meuprojeto.model.Usuario;
import com.example.meuprojeto.util.MaskEditUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class CadastrarUsuario extends AppCompatActivity {
    Usuario objUsuario;
    UsuarioController controleUsuario;
    //Declarando variáveis
    Button btCadastrar, btUploadImg;
    EditText nome, data, email, senha;
    ImageView imgIcon;
    private Uri filePath;
    private byte[] dataIMG;

    /*
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        controleUsuario = new UsuarioController();
        btCadastrar = (Button) findViewById(R.id.btCadastrar);

        nome = (EditText) findViewById(R.id.nome);
        data = (EditText) findViewById(R.id.data);
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);

        btUploadImg = (Button) findViewById(R.id.btUploadImgUser);
        imgIcon = (ImageView) findViewById(R.id.imgUser);

        //Adicionando máscara de data:
        data.addTextChangedListener(MaskEditUtil.mask(data, MaskEditUtil.FORMAT_DATE));


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
                criarUsuario();

            }
        });

    }

    //Função para incluir dados no objeto usuário
    private void criarUsuario(){
        String nome_user = nome.getText().toString();
        String data_nasc = data.getText().toString();
        String email_user = email.getText().toString();
        String senha_user = senha.getText().toString();
        if( nome_user.isEmpty() || data_nasc.isEmpty()  || email_user.isEmpty() || senha_user.isEmpty()){
            Toast.makeText(this,"Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
        }else{
            //AUTENTICAÇÃO DE USUÁRIOCOM O FIREBASE:
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email_user,senha_user)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.i("Sucesso", task.getResult().getUser().getUid());
                        salvarUsuarioFirebase();
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Erro", e.getMessage());
                        }
                    });
        }

    }
    private void salvarUsuarioFirebase(){
        String fileName = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/"+fileName);
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
                        objUsuario= new Usuario();

                        objUsuario.setId(uid);
                        objUsuario.setImagemURL(urlIMG);
                        objUsuario.setNome(nome.getText().toString());
                        objUsuario.setData(data.getText().toString());
                        objUsuario.setEmail(email.getText().toString());
                        objUsuario.setSenha(senha.getText().toString());

                        //controleUsuario.incluirUsuario(objUsuario);

                        FirebaseFirestore.getInstance().collection("usuarios")
                                .document(uid)
                                .set(objUsuario)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(CadastrarUsuario.this, DashboardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Erro ao cadastrar", e.getMessage());
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
    //Função para limpar dados dos campos
    public void limparDados(){
        nome.setText("");
        data.setText("");
        email.setText("");
        senha.setText("");
        nome.requestFocus();
    }
}
