package com.example.meuprojeto.controller;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.model.Passo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterHorizontal extends RecyclerView.Adapter<RecyclerViewAdapterHorizontal.MyViewHolder>{
    private ArrayList<Passo> listaPassos;
    private ClickListener<Passo> clickListener;

    public RecyclerViewAdapterHorizontal(ArrayList<Passo> listaPassos) {
        this.listaPassos = listaPassos;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_horizontal, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Passo passo = listaPassos.get(position);
        Log.e("passo view", passo.getDescricaoPasso());
        holder.ordemPasso.setText("PASSO "+(position+1));
        Log.e("passo img", passo.getImagemURL());
        Picasso.get().load(passo.getImagemURL()).into(holder.imagemPasso);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(passo);
            }
        });


    }
    @Override
    public int getItemCount() {
        Log.e("size", listaPassos.size()+""); return listaPassos.size();
    }

    public void setOnItemClickListener(ClickListener<Passo> movieClickListener) {
        this.clickListener = movieClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView ordemPasso;
        private ImageView imagemPasso;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ordemPasso = itemView.findViewById(R.id.ordemPasso);
            imagemPasso = itemView.findViewById(R.id.imagemPasso);
            cardView = itemView.findViewById(R.id.cardPasso);

        }
    }

}

