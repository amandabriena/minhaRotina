package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.UUID;

public class CadastrarAtividadeCompleto extends AppCompatActivity {
    private Atividade atividade;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterPassosHorizontal recyclerViewAdapter;
    private ArrayList<Passo> listaPassos = new ArrayList<>();
    private ArrayList<String> listaDiasSemana = new ArrayList<>();
    EditText nome_atv, horario, musica;
    ImageButton btUploadImg;
    ImageView imagemAtividade;
    Button btCancelar, btCadastrar, btAdicionarPasso;
    private Uri filePath;
    private byte[] dataIMG;
    private ProgressDialog progress;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_atividade_completo);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewHorizontal);
        recyclerViewAdapter = new RecyclerViewAdapterPassosHorizontal(listaPassos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter.setOnItemClickListener(new ClickListener<Passo>() {
            @Override
            public void onItemClick(Passo passo) {
                Intent intent = new Intent(CadastrarAtividadeCompleto.this, EditarPassoActivity.class);
                intent.putExtra("passo", passo);
                intent.putExtra("idAtividade", atividade.getId());
                startActivityForResult(intent, 1);// Activity é iniciada com requestCode 1
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
        criarNotificacao();
        //Atrelando variáveis as views
        nome_atv = (EditText) findViewById(R.id.nome_atividade);
        horario = (EditText) findViewById(R.id.horario);
        musica = (EditText) findViewById(R.id.musica);
        btCadastrar = (Button) findViewById(R.id.btCadastrar);
        btCancelar = (Button) findViewById(R.id.btCancelar);
        btUploadImg = (ImageButton) findViewById(R.id.btUploadImg);
        imagemAtividade = (ImageView) findViewById(R.id.imgIcon);
        btAdicionarPasso = (Button) findViewById(R.id.btAdicionarPasso);

        progress = new ProgressDialog(this);

        atividade = new Atividade();

        //Adicionando máscara de horário:
        horario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(horario);
            }
        });
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btAdicionarPasso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadPasso = new Intent(CadastrarAtividadeCompleto.this, CadastrarPassoActivity.class);
                //Passando informações da atividade para cadastrar os passos
                String numPasso = (listaPassos.size()+1)+"";
                cadPasso.putExtra("nomeAtividade", nome_atv.getText().toString());
                cadPasso.putExtra("numPasso", numPasso);
                startActivityForResult(cadPasso, 2);
            }
        });
        btUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarImagem();
            }
        });
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nome_atv.getText().toString();
                String hora = horario.getText().toString();
                String musica_atv = musica.getText().toString();
                diasMarcados();
                if(dataIMG == null){
                    Toast.makeText(CadastrarAtividadeCompleto.this,"Por gentileza adicione uma imagem para a atividade!", Toast.LENGTH_SHORT).show();
                }else if(nome.isEmpty() || hora.isEmpty() || musica_atv.isEmpty()){
                    Toast.makeText(CadastrarAtividadeCompleto.this,"Preencha todos os campos para criar a atividade!", Toast.LENGTH_SHORT).show();
                }else {
                    progress.setMessage("Adicionando atividade..");
                    progress.show();
                    criarAtividade();

                }
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    //Função para reorganizar os passos:
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT
            | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(listaPassos, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            //Alterando posição dos passos na lista e na visualização:
            listaPassos.get(fromPosition).setNumOrdem(fromPosition+1);
            recyclerView.getAdapter().notifyItemChanged(fromPosition);
            listaPassos.get(toPosition).setNumOrdem(toPosition+1);
            recyclerView.getAdapter().notifyItemChanged(toPosition);
            Log.e("Passo From:", "From:"+fromPosition+"");
            Log.e("Passo To:", "To:"+toPosition+"");
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
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
        Intent i = new Intent(CadastrarAtividadeCompleto.this, AlarmeAtividades.class);
        //O request code do peding intent será o id da atividade com ultimo digito numero referente ao dia da semana
        Log.e("Alarme", "id atv: "+atividade.getId());
        int code =  Integer.parseInt(atividade.getId()+diaSemana);
        Log.e("Alarme", "Request code: "+code);
        pendingIntent = PendingIntent.getBroadcast(CadastrarAtividadeCompleto.this, code, i, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
        //alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 30*1000, pendingIntent);
        Toast.makeText(CadastrarAtividadeCompleto.this, "Alarme configurado para: "+calendar.getTime(), Toast.LENGTH_LONG).show();
    }
    private void criarAtividade(){
        diasMarcados();
        //criando ID randomico e demais informações preenchidas para upload da imagem no firebase:
        String fileName = UUID.randomUUID().toString();
        //setarAlarmeAtividade();
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
                        String usuario_atv = FirebaseAuth.getInstance().getUid();


                        atividade.setImagemURL(uri.toString());
                        atividade.setNomeAtividade(nome_atv.getText().toString());
                        atividade.setHorario(horario.getText().toString());
                        atividade.setMusica(musica.getText().toString());
                        atividade.setStatus("0");
                        atividade.setIdUsuario(usuario_atv);
                        atividade.setDias_semana(listaDiasSemana);
                        
                        FirebaseFirestore.getInstance().collection("usuarios").document(usuario_atv).collection("atividades")
                                .document(atividade.getId())
                                .set(atividade)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e("Atividade cadastrada", atividade.getId());
                                        Intent intent = new Intent(CadastrarAtividadeCompleto.this, GerAtividadesActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        for (final Passo objPasso: listaPassos) {
                                            FirebaseFirestore.getInstance().collection("usuarios")
                                                    .document(FirebaseAuth.getInstance().getUid()).collection("atividades")
                                                    .document(atividade.getId()).collection("passos")
                                                    .document(objPasso.getId())
                                                    .set(objPasso)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.e("Passo", "Passo e ID:"+objPasso.getDescricaoPasso()+" ordem:"+objPasso.getNumOrdem()+"ID:"+objPasso.getId());
                                                            Log.e("Passo", "ordem Passo:"+objPasso.getNumOrdem());

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.i("Erro", e.getMessage());
                                                        }
                                                    });
                                        }
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
    }
    private void showTimeDialog(final EditText horario){
        //final Calendar c = Calendar.getInstance();
        calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                horario.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new TimePickerDialog(CadastrarAtividadeCompleto.this, timeSetListener,
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
        }else if (requestCode==1 && resultCode == RESULT_OK){
            String tipo = data.getStringExtra("tipo");
            Passo passoAtual = data.getParcelableExtra("passo");
            int ordemPassoAtual = data.getIntExtra("posicaoPasso", 0);
            Log.e("Passo:", "Tipo:"+tipo);
            if(tipo.equals("excluir")){
                boolean removido = listaPassos.remove(passoAtual);
                Log.e("Passo", "Removido?"+removido);
                Log.e("Passo", "passo removido:"+passoAtual.getDescricaoPasso());
                recyclerViewAdapter.notifyItemRemoved(ordemPassoAtual-1);
                Log.e("Passo:", "posicao passo removido:"+ordemPassoAtual);
            }else{
                listaPassos.set(ordemPassoAtual-1, passoAtual);
                Log.e("Passo", "passo alterado:"+passoAtual.getDescricaoPasso());
                Log.e("Passo", "Passo Alterado");
                Log.e("Passo:", "posicao passo alterado:"+ordemPassoAtual);
                recyclerViewAdapter.notifyItemChanged(ordemPassoAtual-1,passoAtual);
            }
            //Activity result para atualizar informaçoes do passo que foi adicionado:
        }else if(requestCode==2 && resultCode == RESULT_OK){
            Passo p = data.getParcelableExtra("passo");
            Log.e("Passo", "Passo adicionado:"+p.getDescricaoPasso());
            Log.e("Passo", "Passo adicionado img:"+p.getUri());
            listaPassos.add(p);
            recyclerView.getAdapter().notifyItemInserted(p.getNumOrdem()-1);
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
