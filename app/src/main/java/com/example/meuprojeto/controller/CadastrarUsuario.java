package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Usuario;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import static java.util.Calendar.*;

public class CadastrarUsuario extends AppCompatActivity {
    Usuario objUsuario;
    //Declarando variáveis
    Button btCadastrar, btUploadImg;
    EditText nome, data, email, senha;
    ImageView imgIcon;
    private Uri filePath;
    private byte[] dataIMG;
    private ProgressDialog progress;

    /*
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);


        btCadastrar = (Button) findViewById(R.id.btCadastrar);

        nome = (EditText) findViewById(R.id.nome);
        data = (EditText) findViewById(R.id.data);
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);

        btUploadImg = (Button) findViewById(R.id.btUploadImgUser);
        imgIcon = (ImageView) findViewById(R.id.imgUser);

        //Adicionando máscara de data:
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(data);
            }
        });

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
                String nome_user = nome.getText().toString();
                String data_nasc = data.getText().toString();
                String email_user = email.getText().toString();
                String senha_user = senha.getText().toString();
                if(senha_user.length() < 6){
                    Toast.makeText(CadastrarUsuario.this,"Sua senha deve conter ao menos 6 caracteres!", Toast.LENGTH_SHORT).show();
                }else if(dataIMG == null){
                    Toast.makeText(CadastrarUsuario.this,"Por gentileza adicione uma foto!", Toast.LENGTH_SHORT).show();
                }else if( nome_user.isEmpty() || data_nasc.isEmpty()  || email_user.isEmpty() || senha_user.isEmpty()){
                    Toast.makeText(CadastrarUsuario.this,"Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }else{
                    progress.setMessage("Criando usuário..");
                    progress.show();
                    criarUsuario();

                }


            }
        });

    }
    private void showDateDialog(final EditText data){
        final Calendar c = getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                c.set(YEAR, year);
                c.set(MONTH, month);
                c.set(DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                data.setText(simpleDateFormat.format(c.getTime()));
                Log.e("Data", data.getText().toString());
            }
        };
        new  DatePickerDialog(CadastrarUsuario.this, dateSetListener, c.get(YEAR),
                c.get(MONTH), c.get(DAY_OF_MONTH)).show();
    }


    //Função para incluir dados no objeto usuário no auth firebase
    private void criarUsuario(){
        String email_user = email.getText().toString();
        String senha_user = senha.getText().toString();
            //AUTENTICAÇÃO DE USUÁRIO COM O FIREBASE:

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
                            Toast.makeText(CadastrarUsuario.this,"Email já cadastrado", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            Log.e("Erro ao cadastrar", e.getMessage());

                        }
                    });


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
                        objUsuario= new Usuario();

                        objUsuario.setId(uid);
                        objUsuario.setImagemURL(urlIMG);
                        objUsuario.setNome(nome.getText().toString());
                        objUsuario.setData(data.getText().toString());
                        objUsuario.setEmail(email.getText().toString());
                        //objUsuario.setSenha(senha.getText().toString());

                        //controleUsuario.incluirUsuario(objUsuario);

                        FirebaseFirestore.getInstance().collection("usuarios")
                                .document(uid)
                                .set(objUsuario)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progress.dismiss();
                                        Log.i("Sucesso","ok");
                                        Intent intent = new Intent(CadastrarUsuario.this, DashboardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progress.dismiss();
                                Log.e("Erro ao cadastrar", e.getMessage());
                                Toast.makeText(CadastrarUsuario.this,"usuário já cadastrado"+e, Toast.LENGTH_SHORT);
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
