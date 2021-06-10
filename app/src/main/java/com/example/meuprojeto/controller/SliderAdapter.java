package com.example.meuprojeto.controller;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Passo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.MyViewHolder> {
    List<Passo> listaPassos;

    public SliderAdapter(List<Passo> listaPassos) {
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
        Log.e("SizePassosSlider", "SizeSlider: "+listaPassos.size());
        Passo p = listaPassos.get(position);
        holder.ordem.setText(p.getNumOrdem());
        holder.descricao.setText(p.getDescricaoPasso());
        Picasso.get().load(p.getImagemURL()).into(holder.imagem);
    }

    @Override
    public int getItemCount() {
        return listaPassos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView ordem;
        private ImageView imagem;
        private TextView descricao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ordem = itemView.findViewById(R.id.ordemPassoItem);
            imagem = itemView.findViewById(R.id.imagemPassoItem);
            descricao = itemView.findViewById(R.id.descricaoPassoItem);
        }
    }
}
