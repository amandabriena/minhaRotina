package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class EditarAtividadeActivity extends AppCompatActivity {
    private Atividade atividade;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterPassosHorizontal recyclerViewAdapter;
    private ArrayList<Passo> listaPassos = new ArrayList<>();
    private ArrayList<Passo> listaPassosAntiga = new ArrayList<>();
    private ArrayList<String> listaDiasSemana = new ArrayList<>();
    private boolean verificador = true;
    EditText nome_atv, horario, musica;
    ImageButton btUploadImg;
    ImageView imagemAtividade;
    Button btCancelar, btAtualizar, btAdicionarPasso;
    private Uri filePath;
    private byte[] dataIMG;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_atividade);

        atividade = getIntent().getParcelableExtra("atividade");
        calendar = Calendar.getInstance();
        Log.e("id Atividade:", atividade.getId());
        new CarregarPassosAsynctask().execute();


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewHorizontal);
        recyclerViewAdapter = new RecyclerViewAdapterPassosHorizontal(listaPassos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewAdapter.setOnItemClickListener(new ClickListener<Passo>() {
            @Override
            public void onItemClick(Passo passo) {
                Intent intent = new Intent(EditarAtividadeActivity.this, EditarPassoActivity.class);
                intent.putExtra("passo", passo);
                intent.putExtra("idAtividade", atividade.getId());
                startActivityForResult(intent, 1);// Activity é iniciada com requestCode 1
                //startActivity(intent);
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);



        nome_atv = (EditText) findViewById(R.id.nome_atividade);
        horario = (EditText) findViewById(R.id.horario);
        //musica = (EditText) findViewById(R.id.musica);
        btUploadImg = (ImageButton) findViewById(R.id.btUploadImg);
        imagemAtividade = (ImageView) findViewById(R.id.imgIcon);
        btCancelar = (Button) findViewById(R.id.btCancelar);
        btAtualizar = (Button) findViewById(R.id.btAtualizarAtv);
        btAdicionarPasso = (Button) findViewById(R.id.btAdicionarPasso);


        //Setando as informações da atividade:
        Picasso.get().load(atividade.getImagemURL()).into(imagemAtividade);
        nome_atv.setHint(atividade.getNomeAtividade());
        horario.setHint(atividade.getHorario());
        //musica.setHint(atividade.getMusica());
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
        btAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarAtividade();
                finish();
            }
        });
        btAdicionarPasso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadPasso = new Intent(EditarAtividadeActivity.this, CadastrarPassoActivity.class);
                //Passando informações da atividade para cadastrar os passos
                String numPasso = (listaPassos.size()+1)+"";
                cadPasso.putExtra("atividade", atividade);
                //cadPasso.putExtra("nomeAtividade", nome_atv.getText().toString());
                cadPasso.putExtra("numPasso", numPasso);
                //startActivity(cadPasso);
                startActivityForResult(cadPasso, 2);
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

    public void cancelarAlarme(int requestCode){
        Log.e("Alarme", "Cancelando: "+requestCode);
        Intent intent = new Intent(this, AlarmeAtividades.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                requestCode, intent, 0);
        ((AlarmManager)getSystemService(ALARM_SERVICE)).cancel(pendingIntent);
    }

    public void atualizarAtividade(){
        Log.e("Atividade", "Atualizando atividade");
        String nome = nome_atv.getText().toString();
        String hora = horario.getText().toString();
        //String musicaAtv = musica.getText().toString();
        //cancelando alarmes anteriormente configurados:
        for (String dia: atividade.getDias_semana()) {
            String idRequest = atividade.getId()+dia;
            Log.e("Alarme", "Request:"+idRequest);
            cancelarAlarme(Integer.parseInt(idRequest));
        }
        diasMarcados();
        FirebaseFirestore.getInstance().collection("usuarios")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("/atividades").document(atividade.getId())
                .update("dias_semana",listaDiasSemana);
        if(!nome.isEmpty()){
            Log.e("Atividade", "nome atualizado");
            FirebaseFirestore.getInstance().collection("usuarios")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("/atividades").document(atividade.getId())
                    .update("nomeAtividade",nome);
        }
        if(!hora.isEmpty()){
            Log.e("Atividade", "hora atualizado");
            FirebaseFirestore.getInstance().collection("usuarios")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("/atividades").document(atividade.getId())
                    .update("horario",hora);
        }
        if(dataIMG != null){
            Log.e("Atividade", "img atualizado");
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
                            String usuario_atv = FirebaseAuth.getInstance().getUid();
                            atividade.setImagemURL(uri.toString());
                            FirebaseFirestore.getInstance().collection("usuarios").document(usuario_atv).collection("atividades")
                                    .document(atividade.getId())
                                    .update("imagemURL", uri.toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.e("Imagem atualizada", atividade.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("Erro ao atualizar", e.getMessage());
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
        //Atualizando passos passos:
        for (Passo p : listaPassosAntiga) {
            Log.e("Atividade", "passo atualizado");
            if(listaPassos.contains(p)){
                /*
                FirebaseFirestore.getInstance().collection("usuarios")
                        .document(FirebaseAuth.getInstance().getUid())
                        .collection("/atividades").document(atividade.getId())
                        .collection("passos").document(p.getId()).update("numOrdem",(listaPassos.indexOf(p)+1));*/
                FirebaseFirestore.getInstance().collection("usuarios")
                        .document(FirebaseAuth.getInstance().getUid())
                        .collection("/atividades").document(atividade.getId())
                        .collection("passos").document(p.getId()).update("numOrdem",(listaPassos.indexOf(p)+1), "imagemURL", p.getImagemURL(), "descricaoPasso", p.getDescricaoPasso());
                Log.e("Passo:", "Index passo:"+listaPassos.indexOf(p)+1+"");
            }else{
                FirebaseFirestore.getInstance().collection("usuarios")
                        .document(FirebaseAuth.getInstance().getUid())
                        .collection("/atividades").document(atividade.getId())
                        .collection("passos").document(p.getId()).delete();
                Log.e("Passo:", "passo excluido:"+p.getDescricaoPasso());
            }
        }
        for (Passo objPasso: listaPassos) {
            if(!listaPassosAntiga.contains(objPasso)){
                FirebaseFirestore.getInstance().collection("usuarios")
                        .document(FirebaseAuth.getInstance().getUid()).collection("atividades")
                        .document(atividade.getId()).collection("passos")
                        .document(objPasso.getId())
                        .set(objPasso);
            }
        }
    }
    public class CarregarPassosAsynctask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // carregar do banco
            Log.e("Passo", "Verificador antes do if"+verificador);
                FirebaseFirestore.getInstance().collection("usuarios")
                        .document(FirebaseAuth.getInstance().getUid()).collection("atividades")
                        .document(atividade.getId()).collection("passos")
                        .orderBy("numOrdem", Query.Direction.ASCENDING)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error != null){
                                    Log.e("Erro", error.getMessage());
                                    return;
                                }
                                List<DocumentSnapshot> docs = value.getDocuments();
                                for(DocumentSnapshot doc : docs){
                                    Passo passo = doc.toObject(Passo.class);
                                    if(verificador){
                                        Log.e("Passo:", "Passo add na lista:"+passo.getDescricaoPasso());
                                        listaPassos.add(passo);
                                        listaPassosAntiga.add(passo);
                                    }
                                }
                                verificador = false;
                                Log.e("Passo", "Verificador dps do for "+verificador);
                            }
                        });


            return null;
            }

        @Override
        protected void onPostExecute(Void resultado) {
            Log.e("Passo", "atualização adapter");
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
    private void showTimeDialog(final EditText horario){

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                horario.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new TimePickerDialog(EditarAtividadeActivity.this, timeSetListener,
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
            //Activity result para atualizar informaçoes do passo que foi deletado:
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
            listaPassos.add(p);
            recyclerView.getAdapter().notifyItemInserted(p.getNumOrdem()-1);
            //recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
    public void setarDias(){
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox_dom);
        if( atividade.getDias_semana().contains("1")){
            checkBox.setChecked(true);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_seg);
        if( atividade.getDias_semana().contains("2")){
            checkBox.setChecked(true);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_ter);
        if( atividade.getDias_semana().contains("3")){
            checkBox.setChecked(true);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_qua);
        if( atividade.getDias_semana().contains("4")){
            checkBox.setChecked(true);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_qui);
        if( atividade.getDias_semana().contains("5")){
            checkBox.setChecked(true);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_sex);
        if( atividade.getDias_semana().contains("6")){
            checkBox.setChecked(true);
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox_sab);
        if( atividade.getDias_semana().contains("7")){
            checkBox.setChecked(true);
        }
    }
    //Setando alarme da atividade:
    private void setarAlarmeAtividade(int diaSemana){
        //alterando o dia da semana para incluir alarme:
        calendar.set(Calendar.DAY_OF_WEEK, diaSemana);
        Log.e("Alarme", "Horario: "+calendar.getTime());
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(EditarAtividadeActivity.this, AlarmeAtividades.class);
        //O request code do peding intent será o id da atividade com ultimo digito numero referente ao dia da semana
        Log.e("Alarme", "id atv: "+atividade.getId());
        int code =  Integer.parseInt(atividade.getId()+diaSemana);
        Log.e("Alarme", "Request code: "+code);
        pendingIntent = PendingIntent.getBroadcast(EditarAtividadeActivity.this, code, i, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
        //alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 30*1000, pendingIntent);
        Toast.makeText(EditarAtividadeActivity.this, "Alarme configurado para: "+calendar.getTime(), Toast.LENGTH_LONG).show();
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
