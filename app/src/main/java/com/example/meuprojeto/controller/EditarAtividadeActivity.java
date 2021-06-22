package com.example.meuprojeto.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditarAtividadeActivity extends AppCompatActivity {
    String id;
    Atividade atividade;
    private ArrayList<String> listaDiasSemana = new ArrayList<>();
    EditText nome_atv, horario, musica;
    ImageButton btUploadImg;
    ImageView imagemAtividade;
    private Uri filePath;
    private byte[] dataIMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_atividade);

        nome_atv = (EditText) findViewById(R.id.nome_atividade);
        horario = (EditText) findViewById(R.id.horario);
        musica = (EditText) findViewById(R.id.musica);
        btUploadImg = (ImageButton) findViewById(R.id.btUploadImg);
        imagemAtividade = (ImageView) findViewById(R.id.imgIcon);

        id = getIntent().getStringExtra("idAtividade");
        atividade = getIntent().getParcelableExtra("atividade");

        //Setando as informações da atividade:
        Picasso.get().load(atividade.getImagemURL()).into(imagemAtividade);
        nome_atv.setHint(atividade.getNomeAtividade());
        horario.setHint(atividade.getHorario());
        musica.setHint(atividade.getMusica());
        setarDias();

        horario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(horario);
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
        new TimePickerDialog(EditarAtividadeActivity.this, timeSetListener,
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

                imagemAtividade.setImageDrawable(new BitmapDrawable(bitmap));
                btUploadImg.setAlpha(0);
            }catch (IOException e){
                Toast.makeText(this,"Erro ao selecionar imagem! "+e, Toast.LENGTH_SHORT);
            }
        }
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
}
