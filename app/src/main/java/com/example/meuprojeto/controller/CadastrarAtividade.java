package com.example.meuprojeto.controller;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meuprojeto.R;
import com.example.meuprojeto.datasource.AtividadeController;
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.model.DiaSemana;
import com.example.meuprojeto.util.MaskEditUtil;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static android.app.TimePickerDialog.*;

public class CadastrarAtividade extends AppCompatActivity {
    Atividade objAtividade;
    private List<String> listaDiasSemana = new ArrayList<>();

    //Declarando variáveis
    EditText nome_atv, horario, musica;
    Button btCadastrarAtividade, btAddPassos;
    ImageButton btUploadImg;
    ImageView imgIcon;
    private Uri filePath;
    private byte[] dataIMG;

    //Conexão com o db
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_atividade);

        //Atrelando variáveis as views
        nome_atv = (EditText) findViewById(R.id.nome_atividade);
        horario = (EditText) findViewById(R.id.horario);
        musica = (EditText) findViewById(R.id.musica);
        btCadastrarAtividade = (Button) findViewById(R.id.btCadastrarAtividade);
        btAddPassos = (Button) findViewById(R.id.btAdicionarPasso);
        btUploadImg = (ImageButton) findViewById(R.id.btUploadImg);
        imgIcon = (ImageView) findViewById(R.id.imgIcon);

        objAtividade = new Atividade();


        //Adicionando máscara de horário:
        //horario.addTextChangedListener(MaskEditUtil.mask(horario, MaskEditUtil.FORMAT_HOUR));
        horario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(horario);
            }
        });

        //Capturando clique do botão cadastrar:
        btCadastrarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarAtividade();
                Intent intent = new Intent(CadastrarAtividade.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btAddPassos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarAtividade();
                Intent cadPasso = new Intent(CadastrarAtividade.this, CadastrarPassoAtividadeActivity.class);
                //Passando informações da atividade para cadastrar os passos
                cadPasso.putExtra("idAtividade", objAtividade.getId());
                cadPasso.putExtra("nomeAtividade", nome_atv.getText().toString());
                cadPasso.putExtra("numPasso", "1");
                startActivity(cadPasso);
            }
        });
        btUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarImagem();
            }
        });
    }

    private void showTimeDialog(final EditText horario){
        final Calendar c = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                horario.setText(simpleDateFormat.format(c.getTime()));
            }
        };
        new TimePickerDialog(CadastrarAtividade.this, timeSetListener,
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),true).show();
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

                imgIcon.setImageDrawable(new BitmapDrawable(bitmap));
                btUploadImg.setAlpha(0);
            }catch (IOException e){
                Toast.makeText(this,"Erro ao selecionar imagem! "+e, Toast.LENGTH_SHORT);
            }
        }
    }

    public void diasMarcados() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox_dom);
        if( checkBox.isChecked()){
            listaDiasSemana.add("Dom");
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_seg);
        if( checkBox.isChecked()){
            listaDiasSemana.add("Seg");
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_ter);
        if( checkBox.isChecked()){
            listaDiasSemana.add("Ter");
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_qua);
        if( checkBox.isChecked()){
            listaDiasSemana.add("Qua");
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_qui);
        if( checkBox.isChecked()){
            listaDiasSemana.add("Qui");
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_sex);
        if( checkBox.isChecked()){
            listaDiasSemana.add("Sex");
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_sab);
        if( checkBox.isChecked()){
            listaDiasSemana.add("Sab");
        }
    }

    private void criarAtividade(){
        String nome = nome_atv.getText().toString();
        String hora = horario.getText().toString();
        String musica_atv = musica.getText().toString();
        diasMarcados();

        if(nome.isEmpty() || hora.isEmpty() || musica_atv.isEmpty()){
            Toast.makeText(this,"Preencha todos os campos para criar a atividade!", Toast.LENGTH_SHORT).show();
        }else {
            //criando ID randomico e demais informações preenchidas para upload da imagem no firebase:
            String fileName = UUID.randomUUID().toString();
            final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/atividades/" + fileName);
            UploadTask uploadTask2 = ref.putBytes(dataIMG);
            uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.i("FILEURI", "URI: " + uri.toString());

                            //Pegando o usuário que criou a atividade:
                            String usuario_atv = FirebaseAuth.getInstance().getUid();


                            objAtividade.setImagemURL(uri.toString());
                            objAtividade.setNomeAtividade(nome_atv.getText().toString());
                            objAtividade.setHorario(horario.getText().toString());
                            objAtividade.setMusica(musica.getText().toString());
                            objAtividade.setIdUsuario(usuario_atv);
                            FirebaseFirestore.getInstance().collection("atividades")
                                    .document(objAtividade.getId())
                                    .set(objAtividade)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.e("Atividade cadastrada", objAtividade.getId());
                                            //int i = 1;
                                            for(int i = 0; i <listaDiasSemana.size(); i++){
                                                String dia = listaDiasSemana.get(i);
                                                String valor = String.valueOf(i);
                                                DiaSemana diaS = new DiaSemana(valor, dia);
                                                FirebaseFirestore.getInstance().collection("atividades").document(objAtividade.getId())
                                                        .collection("diasSemana")
                                                        .document(valor)
                                                        .set(diaS)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.e("DIA X", " DIA: X");

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
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

}
