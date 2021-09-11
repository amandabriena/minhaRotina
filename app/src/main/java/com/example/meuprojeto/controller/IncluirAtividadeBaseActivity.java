package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
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

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.model.Passo;
import com.example.meuprojeto.util.AlarmeAtividades;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class IncluirAtividadeBaseActivity extends AppCompatActivity {
    private Atividade atividade;
    private ArrayList<String> listaDiasSemana = new ArrayList<>();
    EditText nome_atv, horario, musica;
    ImageButton btUploadImg;
    ImageView imagemAtividade;
    Button btCancelar, btIncluir;
    private Uri filePath;
    private byte[] dataIMG;
    private ProgressDialog progress;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incluir_atividade_base);

        atividade = getIntent().getParcelableExtra("atividade");

        nome_atv = (EditText) findViewById(R.id.nome_atividade);
        horario = (EditText) findViewById(R.id.horario);
        musica = (EditText) findViewById(R.id.musica);
        btUploadImg = (ImageButton) findViewById(R.id.btUploadImg);
        imagemAtividade = (ImageView) findViewById(R.id.imgIcon);
        btCancelar = (Button) findViewById(R.id.btCancelar);
        btIncluir = (Button) findViewById(R.id.btIncluir);
        criarNotificacao();
        progress = new ProgressDialog(this);

        //Setando as informações da atividade:
        Picasso.get().load(atividade.getImagemURL()).into(imagemAtividade);
        nome_atv.setText(atividade.getNomeAtividade());
        musica.setText(atividade.getMusica());
        setarDias();
        //mascara de horário:
        horario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(horario);
            }
        });

        //ação de upload de imagem:
        btUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarImagem();
            }
        });
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btIncluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nome_atv.getText().toString();
                String hora = horario.getText().toString();
                String musica_atv = musica.getText().toString();
                diasMarcados();
                if(nome.isEmpty() || hora.isEmpty() || musica_atv.isEmpty()){
                    Toast.makeText(IncluirAtividadeBaseActivity.this,"Preencha todos os campos para incluir a atividade!", Toast.LENGTH_SHORT).show();
                }else {
                    progress.setMessage("Adicionando atividade..");
                    progress.show();
                    incluirAtividade();
                }
            }
        });
    }
    private void incluirAtividade(){
        diasMarcados();
        //Criando um novo id para a atividade:
        //atividade.setId((new Random().nextInt(999999))+"");
        //incluindo demais informações da atividade:
        final String usuario_atv = FirebaseAuth.getInstance().getUid();
        atividade.setNomeAtividade(nome_atv.getText().toString());
        atividade.setHorario(horario.getText().toString());
        atividade.setMusica(musica.getText().toString());
        atividade.setStatus("0");
        atividade.setIdUsuario(usuario_atv);
        atividade.setDias_semana(listaDiasSemana);
        if(dataIMG != null){
            //caso o usuário tenha adicionado uma nova imagem a atividade:
            String fileName = UUID.randomUUID().toString();
            final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/atividades" + fileName);
            UploadTask uploadTask2 = ref.putBytes(dataIMG);
            uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.i("FILEURI", "URI: " + uri.toString());

                            //Pegando o usuário que criou a atividade:
                            atividade.setImagemURL(uri.toString());
                            FirebaseFirestore.getInstance().collection("usuarios").document(usuario_atv).collection("atividades")
                                    .document(atividade.getId())
                                    .set(atividade)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.e("Atividade cadastrada", atividade.getId());
                                            Intent intent = new Intent(IncluirAtividadeBaseActivity.this, GerAtividadesActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            progress.dismiss();
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
                    //Toast.makeText(Profilepic.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
                    Log.e("Teste", e.getMessage(), e);
                }
            });
        }else{
            //Caso o usuário não tenha atualizado a imagem da atividade:
            FirebaseFirestore.getInstance().collection("usuarios").document(usuario_atv).collection("atividades")
                    .document(atividade.getId())
                    .set(atividade)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("Atividade cadastrada", atividade.getId());
                            Intent intent = new Intent(IncluirAtividadeBaseActivity.this, GerAtividadesActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            progress.dismiss();
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
    }
    private void showTimeDialog(final EditText horario){
        calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                horario.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new TimePickerDialog(IncluirAtividadeBaseActivity.this, timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
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

                imagemAtividade.setImageDrawable(new BitmapDrawable(bitmap));
                btUploadImg.setAlpha(0);
            }catch (IOException e){
                Toast.makeText(this,"Erro ao selecionar imagem! "+e, Toast.LENGTH_SHORT);
            }
        }
    }
    private void criarNotificacao(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "notificacaoAtividade";
            String description = "Canal paa notificar atividade";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("atividadeAlerta", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
    //Setando alarme da atividade:
    private void setarAlarmeAtividade(int diaSemana){
        //alterando o dia da semana para incluir alarme:
        calendar.set(Calendar.DAY_OF_WEEK, diaSemana);
        Log.e("Alarme", "Horario: "+calendar.getTime());
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(IncluirAtividadeBaseActivity.this, AlarmeAtividades.class);
        //O request code do peding intent será o id da atividade com ultimo digito numero referente ao dia da semana
        Log.e("Alarme", "id atv: "+atividade.getId());
        int code =  Integer.parseInt(atividade.getId()+diaSemana);
        Log.e("Alarme", "Request code: "+code);
        pendingIntent = PendingIntent.getBroadcast(IncluirAtividadeBaseActivity.this, code, i, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
        //alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 30*1000, pendingIntent);
        Toast.makeText(IncluirAtividadeBaseActivity.this, "Alarme configurado para: "+calendar.getTime(), Toast.LENGTH_LONG).show();
    }
    public void setarDias(){
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox_dom);
        if( atividade.getDias_semana().contains("Dom")){
            checkBox.setChecked(true);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_seg);
        if( atividade.getDias_semana().contains("Seg")){
            checkBox.setChecked(true);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_ter);
        if( atividade.getDias_semana().contains("Ter")){
            checkBox.setChecked(true);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_qua);
        if( atividade.getDias_semana().contains("Qua")){
            checkBox.setChecked(true);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_qui);
        if( atividade.getDias_semana().contains("Qui")){
            checkBox.setChecked(true);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_sex);
        if( atividade.getDias_semana().contains("Sex")){
            checkBox.setChecked(true);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_sab);
        if( atividade.getDias_semana().contains("Sab")){
            checkBox.setChecked(true);
        }
    }

    public void diasMarcados() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox_dom);
        if( checkBox.isChecked()){
            listaDiasSemana.add("1");
            Log.e("Alarme", "DOM");
            setarAlarmeAtividade(1);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_seg);
        if( checkBox.isChecked()){
            listaDiasSemana.add("2");
            Log.e("Alarme", "SEG");
            setarAlarmeAtividade(2);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_ter);
        if( checkBox.isChecked()){
            listaDiasSemana.add("3");
            Log.e("Alarme", "TER");
            setarAlarmeAtividade(3);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_qua);
        if( checkBox.isChecked()){
            listaDiasSemana.add("4");
            Log.e("Alarme", "QUA");
            setarAlarmeAtividade(4);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_qui);
        if( checkBox.isChecked()){
            listaDiasSemana.add("5");
            Log.e("Alarme", "QUI");

            setarAlarmeAtividade(5);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_sex);
        if( checkBox.isChecked()){
            listaDiasSemana.add("6");
            Log.e("Alarme", "SEX");
            setarAlarmeAtividade(6);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_sab);
        if( checkBox.isChecked()){
            listaDiasSemana.add("7");
            Log.e("Alarme", "SAB");
            setarAlarmeAtividade(7);
        }
    }
}
