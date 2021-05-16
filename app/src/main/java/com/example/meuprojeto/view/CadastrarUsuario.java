package com.example.meuprojeto.view;

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
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.controller.UsuarioController;
import com.example.meuprojeto.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class CadastrarUsuario extends AppCompatActivity {
    Usuario objUsuario;
    UsuarioController controleUsuario;
    //Declarando variáveis
    Button btProximo;
    EditText nome, data, email, senha;
    ImageButton btUploadImg;
    ImageView imgIcon;
    private Uri filePath;

    /*
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        controleUsuario = new UsuarioController();
        btProximo = (Button) findViewById(R.id.btProximo);

        nome = (EditText) findViewById(R.id.nome);
        data = (EditText) findViewById(R.id.data);
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);

        btUploadImg = (ImageButton) findViewById(R.id.btUploadImgUser);
        imgIcon = (ImageView) findViewById(R.id.imgUser);

        btUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarImagem();
            }
        });
        //inicializarFirebase();
        //Capturando clique do botão cadastrar:
        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando a ação do botão padastrar novo usuário:
                criarUsuario();

                /*
                objUsuario= new Usuario();

                objUsuario.setNome(nome.getText().toString());
                objUsuario.setData(data.getText().toString());
                objUsuario.setEmail(email.getText().toString());
                objUsuario.setSenha(senha.getText().toString());
                controleUsuario.incluirUsuario(objUsuario);
                databaseReference.child("Usuario").child(objUsuario.getId()).setValue(objUsuario);*/

            }
        });

    }
    /*
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(CadastrarUsuario.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }*/

    //Função para incluir dados no objeto usuário
    private void criarUsuario(){
        String nome_user = nome.getText().toString();
        String data_nasc = data.getText().toString();
        String email_user = email.getText().toString();
        String senha_user = senha.getText().toString();
        if(nome_user == null || nome_user.isEmpty() || data_nasc == null || data_nasc.isEmpty() || email_user == null || email_user.isEmpty()
        || senha_user == null || senha_user.isEmpty()){
            Toast.makeText(this,"Por favor, preencha todos os campos!", Toast.LENGTH_SHORT);
        }else{
            //AUTENTICAÇÃO DE USUÁRIOCOM O FIREBASE:
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email_user,senha_user)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.i("Sucesso", task.getResult().getUser().getUid());

                        uploadImagem();

                        String uid = FirebaseAuth.getInstance().getUid();
                        String urlIMG = filePath.toString();
                        objUsuario= new Usuario();

                        objUsuario.setId(uid);
                        objUsuario.setImagemURL(urlIMG);
                        objUsuario.setNome(nome.getText().toString());
                        objUsuario.setData(data.getText().toString());
                        objUsuario.setEmail(email.getText().toString());
                        objUsuario.setSenha(senha.getText().toString());

                        controleUsuario.incluirUsuario(objUsuario);

                        FirebaseFirestore.getInstance().collection("usuarios")
                                .add(objUsuario)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.i("Usuario cadastrado", documentReference.getId());

                                        limparDados();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Erro ao cadastrar", e.getMessage());
                            }
                        });
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
    private void selecionarImagem(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/user/*");
        startActivityForResult(intent, 1);
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
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData() !=null){
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
    //Função para limpar dados dos campos
    public void limparDados(){
        nome.setText("");
        data.setText("");
        email.setText("");
        senha.setText("");
        nome.requestFocus();
    }
}
