package com.example.meuprojeto.controller;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Passo;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class SliderPassosAdapter extends RecyclerView.Adapter<SliderPassosAdapter.MyViewHolder> {
    private ArrayList<Passo> listaPassos;
    private MediaPlayer mediaPlayer;
    private ClickListener<Passo> clickListener;


    public SliderPassosAdapter(ArrayList<Passo> listaPassos) {
        this.listaPassos = listaPassos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.passo_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //
        Log.e("SizePassosSlider", "SizeSlider: "+listaPassos.size());
        final Passo p = listaPassos.get(position);
        Log.e("SizePassosSlider", "num Ordem "+p.getNumOrdem()+"");
        if(p.getNumOrdem()==listaPassos.size()){
            holder.btConcluido.setEnabled(true);
            holder.btConcluido.setVisibility(View.VISIBLE);
        }else{
            holder.btConcluido.setEnabled(false);
            holder.btConcluido.setVisibility(View.GONE);
        }
        holder.ordem.setText("Passo "+p.getNumOrdem()+":");
        holder.descricao.setText(p.getDescricaoPasso());
        Picasso.get().load(p.getImagemURL()).into(holder.imagem);
        holder.btOuvir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(p);
            }
        });

    }
    public void setOnItemClickListener(ClickListener<Passo> movieClickListener) {
        this.clickListener = movieClickListener;
    }
    @Override
    public int getItemCount() {
        return listaPassos.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView ordem;
        private ImageView imagem;
        private TextView descricao;
        private Button btConcluido, btOuvir;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ordem = itemView.findViewById(R.id.ordemPassoItem);
            imagem = itemView.findViewById(R.id.imagemPassoItem);
            descricao = itemView.findViewById(R.id.descricaoPassoItem);
            btConcluido = itemView.findViewById(R.id.btConcluido);
            btOuvir = itemView.findViewById(R.id.btOuvirItem);

        }
    }
    void sendUrlToMediaPlayer(String url) {
        mediaPlayer = new MediaPlayer();
        Log.e("SizePassosSlider Url", url+"");
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        try {
            // enviar a StreamUrl para o player
            Log.e("SizePassosSlider Url", url+"");
            mediaPlayer.setDataSource(url);
            // esperar que ele fique pronto e após ficar pronto tocar o áudio
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });

            mediaPlayer.prepareAsync();
        } catch (IOException err) {
            Log.e("Audio Error", err.toString());
        }*/
    }
}
