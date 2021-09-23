package com.example.meuprojeto.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.example.meuprojeto.util.Utilitarios.verificarDiaSemana;


public class MinhaRotinaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapterAtividades recyclerViewAdapter;
    private List<Atividade> listaAtividades = new ArrayList<>();
    Button btEspacoPais;
    ImageButton btCompartilhar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_rotina);

        btEspacoPais = (Button) findViewById(R.id.btEspacoPais);
        btCompartilhar = (ImageButton) findViewById(R.id.btCompartilhar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapterAtividades(listaAtividades);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        new CarregarListaAsynctask().execute();
        //PASSANDO PARA OUTRA PÁGINA AO CLICAR NA ATIVIDADE

        recyclerViewAdapter.setOnItemClickListener(new ClickListener<Atividade>() {
            @Override
            public void onItemClick(Atividade atividade) {
                Intent intent = new Intent(MinhaRotinaActivity.this, AtividadeActivity.class);
                intent.putExtra("atividade", atividade);
                intent.putExtra("idAtividade", atividade.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);


        btEspacoPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Direcionando para tela de gerenciamento de pais ou responsáveis
                Intent pais = new Intent(MinhaRotinaActivity.this, PopUpResponsaveis.class);
                startActivity(pais);
            }
        });
        btCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenShot(recyclerView);
            }
        });

    }
    public void screenShot(RecyclerView view) {

        /*String fileName = Environment.getExternalStorageDirectory() + "/screenshot.jpg";
        View root = getWindow().getDecorView();
        root.setDrawingCacheEnabled(true);
        root.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(root.getDrawingCache());
        root.setDrawingCacheEnabled(false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        Uri uriImagem = Uri.parse(path);*/
        int size = view.getAdapter().getItemCount();
        RecyclerView.ViewHolder holder = view.getAdapter().createViewHolder(view, 0);
        view.getAdapter().onBindViewHolder(holder, 0);
        holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
        Bitmap bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), holder.itemView.getMeasuredHeight() * size,
                Bitmap.Config.ARGB_8888);
        Canvas bigCanvas = new Canvas(bigBitmap);
        bigCanvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        int iHeight = 0;
        holder.itemView.setDrawingCacheEnabled(true);
        holder.itemView.buildDrawingCache();
        bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
        holder.itemView.setDrawingCacheEnabled(false);
        holder.itemView.destroyDrawingCache();
        iHeight += holder.itemView.getMeasuredHeight();
        for (int i = 1; i < size; i++) {
            view.getAdapter().onBindViewHolder(holder, i);
            holder.itemView.setDrawingCacheEnabled(true);
            holder.itemView.buildDrawingCache();
            bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
            iHeight += holder.itemView.getMeasuredHeight();
            holder.itemView.setDrawingCacheEnabled(false);
            holder.itemView.destroyDrawingCache();
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bigBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bigBitmap, "Title", null);
        Uri uriImagem = Uri.parse(path);
        Log.e("URI IMAGEM", uriImagem.toString());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Minha Rotina de Hoje");
        intent.putExtra(Intent.EXTRA_STREAM, uriImagem);
        //intent.setType("image/*");
        intent.setType("*/*");
        startActivity(Intent.createChooser(intent, "Share Image"));
    }
    public class CarregarListaAsynctask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // carregar do banco
            Log.e("Rotina", "carregando atividades..");
            final String dia = verificarDiaSemana();
            FirebaseFirestore.getInstance().collection("usuarios")
                    .document(FirebaseAuth.getInstance().getUid()).collection("atividades")
                    .orderBy("horario", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error != null){
                                Log.e("Erro", error.getMessage());
                                return;
                            }
                            List<DocumentSnapshot> docs = value.getDocuments();
                            for(DocumentSnapshot doc : docs){
                                Atividade atv = doc.toObject(Atividade.class);
                                if(atv.getDias_semana().contains(dia)){
                                    Log.e("Rotina", atv.getNomeAtividade());
                                    listaAtividades.add(atv);
                                }
                            }
                        }
                    });
            return null;
        }
        @Override
        protected void onPostExecute(Void resultado) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

}
