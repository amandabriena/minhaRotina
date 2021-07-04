package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CadastrarPassoAtividadeActivity extends AppCompatActivity {
    Passo objPasso;
    //Declarando variáveis
    TextView atv_atual, ordemPasso;
    EditText descricao, som;
    Button btFinalizarPassos, btAddPassos;
    ImageButton btUploadImg, btStart, btPlay, btTrash;
    ImageView imgPasso;
    int numPasso;
    private Atividade atividade;
    private Uri filePath;
    private byte[] dataIMG;
    private static int MICROPHONE_PERMISSION_CODE = 200;
    MediaRecorder mediaRecorder;
    private ProgressDialog progress;

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
        btStart = (ImageButton) findViewById(R.id.btStart);
        btPlay = (ImageButton) findViewById(R.id.btPlay);
        btTrash = (ImageButton) findViewById(R.id.btTrash);
        objPasso = new Passo();

        progress = new ProgressDialog(this);

        //Pegando informações da atividade que está sendo cadastrada:
        atividade = getIntent().getParcelableExtra("atividade");
        String num = getIntent().getStringExtra("numPasso");
        //final String modo = getIntent().getStringExtra("modoEdicao");
        numPasso = Integer.parseInt(num);
        objPasso.setNumOrdem(numPasso);
        ordemPasso.setText("Passo "+num);
        atv_atual.setText(atividade.getNomeAtividade());

        //informações para gravação do aúdio:
        if(isMicrophonePresent()){
            permissaoMicrofone();
        }
        btTrash.setEnabled(false);
        btPlay.setEnabled(false);
        btPlay.setVisibility(View.GONE);
        btStart.bringToFront();
        btStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        record();
                    }else if(event.getAction() == MotionEvent.ACTION_UP){
                        stopAudio();
                        btStart.setEnabled(false);
                        btTrash.setEnabled(true);
                        btTrash.bringToFront();
                        btPlay.setEnabled(true);
                        btPlay.setVisibility(View.VISIBLE);
                    }

                return false;
            }
        });

        btTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaRecorder = null;
                btTrash.setEnabled(false);
                btPlay.setEnabled(false);
                btPlay.setVisibility(View.GONE);
                btStart.setEnabled(true);
                btStart.bringToFront();
                som.setText("");
            }
        });
        btUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarImagem();
            }
        });
            /*
        btAddPassos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando para cadastrar mais passos
                progress.setMessage("Adicionando passo..");
                progress.show();
                adicionarPasso();
                Intent add = new Intent(CadastrarPassoAtividadeActivity.this, CadastrarPassoAtividadeActivity.class);
                 //Passando número de ordem do passo
                 numPasso++;
                 add.putExtra("atividade",atividade);
                    add.putExtra("modoEdicao", "false");
                    add.putExtra("numPasso", numPasso+"");
                    startActivity(add);

            }
        });*/
        btFinalizarPassos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando para tela de  de dashboard ao finalizar cadastros
                    /*
                    Intent intent = new Intent(CadastrarPassoAtividadeActivity.this, EditarAtividadeActivity.class);
                    intent.putExtra("modoEdicao", "true");
                    intent.putExtra("atividade",atividade);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/

                    progress.setMessage("Adicionando passo..");
                    progress.show();
                    adicionarPasso();

            }
        });
    }
    private boolean isMicrophonePresent(){
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }else{
            return false;
        }
    }
    private void permissaoMicrofone(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.RECORD_AUDIO},MICROPHONE_PERMISSION_CODE);
        }
    }
    private String getRecordFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        //String name = UUID.randomUUID().toString();
        File file = new File(musicDirectory, "teste_recording"+".mp3");
        return file.getPath();
    }

    public void onClickBt(View v){
        if(v.getId() == R.id.btPlay){
            play();
        }
    }

    private void record() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(getRecordFilePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        som.setText("Gravando..");
    }
    private void stopAudio() {
        mediaRecorder.stop();
        mediaRecorder.release();
        som.setText("Gravação finalizada!");
    }
    private void play() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getRecordFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        som.setText("Ouvindo o audio..");
    }

    private void uploadAudio(String idAtv, String ordemPasso){
        //final StorageReference ref = FirebaseStorage.getInstance().getReference();
        //A denomicação do aúdio do passo será composta pelo id da atividade + "passo" + número de ordem do passo
        //StorageReference arquivo = ref.child("audio").child(idAtv+"-passo"+ordemPasso+".3gp");
        Uri uri = Uri.fromFile(new File(getRecordFilePath()));
        Log.e("FILE audio1", "URI: " + uri.toString());

        /*
        arquivo.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });*/
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/audio" + idAtv+"-passo"+ordemPasso+".3gp");
        UploadTask uploadTask2 = ref.putFile(uri);
        uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e("FILE audio", "URI: " + uri.toString());
                        objPasso.setAudio(uri.toString());
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
        if(dataIMG == null){
            Toast.makeText(this,"Por gentileza adicione uma imagem para a atividade!", Toast.LENGTH_SHORT).show();
        }else if(descricao_passo.isEmpty() || som_passo.isEmpty()){
            Toast.makeText(this,"Preencha todos os campos para criar o Passo!", Toast.LENGTH_SHORT).show();
        }
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
                        //objPasso.setAudio(som.getText().toString());

                        objPasso.setImagemURL(uri.toString());
                        //uploadAudio(atividade.getId(), objPasso.getNumOrdem());
                        //objPasso.setAudio(idAtividade+"passo"+ordemPasso+".3gp");
                        FirebaseFirestore.getInstance().collection("usuarios")
                                .document(FirebaseAuth.getInstance().getUid()).collection("atividades")
                                .document(atividade.getId()).collection("passos")
                                .document(objPasso.getId())
                                .set(objPasso)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e("Passo", "Passo e ID:"+objPasso.getDescricaoPasso()+" ordem:"+objPasso.getNumOrdem()+"ID:"+objPasso.getId());
                                        Log.e("Passo", "ordem Passo:"+numPasso);
                                        Intent intent = new Intent(CadastrarPassoAtividadeActivity.this, EditarAtividadeActivity.class);
                                        intent.putExtra("atividade",atividade);
                                        intent.putExtra("passo", objPasso);
                                        setResult(RESULT_OK,intent);
                                        progress.dismiss();
                                        startActivity(intent);

                                        //finish();

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
                //Toast.makeText(Profilepic.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
                Log.e("Teste", e.getMessage(), e);
            }
        });
    }
}
