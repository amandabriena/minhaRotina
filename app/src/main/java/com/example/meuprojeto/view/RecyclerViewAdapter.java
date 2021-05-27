package com.example.meuprojeto.view;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<Atividade> listaAtividades;
    private ClickListener<Atividade> clickListener;

    public RecyclerViewAdapter(List<Atividade> listaAtividades) {
        this.listaAtividades = listaAtividades;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Atividade atividade = listaAtividades.get(position);

        holder.nome_atv.setText(atividade.getNomeAtividade());
        holder.horario_atv.setText(atividade.getHorario());
        Picasso.get().load(atividade.getImagemURL()).into(holder.imagem_atv);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(atividade);
            }
        });


    }
    @Override
    public int getItemCount() {
        return listaAtividades.size();
    }

    public void setOnItemClickListener(ClickListener<Atividade> movieClickListener) {
        this.clickListener = movieClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nome_atv;
        private ImageView imagem_atv;
        private TextView horario_atv;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            nome_atv = itemView.findViewById(R.id.nome_atv);
            horario_atv = itemView.findViewById(R.id.horario_atv);
            imagem_atv = itemView.findViewById(R.id.imagem_atv);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }

}
interface ClickListener<T> {
    void onItemClick(T data);
}