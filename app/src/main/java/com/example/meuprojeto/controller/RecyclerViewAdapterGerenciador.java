package com.example.meuprojeto.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Atividade;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapterGerenciador extends RecyclerView.Adapter<RecyclerViewAdapterGerenciador.MyViewHolder> {
    private List<Atividade> listaAtividadesGer;
    private ClickListener<Atividade> clickListener;

    public RecyclerViewAdapterGerenciador(List<Atividade> listaAtividadesGer) {
        this.listaAtividadesGer = listaAtividadesGer;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_ger, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Atividade atividade = listaAtividadesGer.get(position);

        holder.nome_atv_ger.setText(atividade.getNomeAtividade());
        holder.horario_atv_ger.setText(atividade.getHorario());
        Picasso.get().load(atividade.getImagemURL()).into(holder.imagem_atv_ger);


        holder.editarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(atividade);
            }
        });
        holder.deletarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(atividade);
            }
        });

    }
    @Override
    public int getItemCount() {
        return listaAtividadesGer.size();
    }

    public void setOnItemClickListener(ClickListener<Atividade> movieClickListener) {
        this.clickListener = movieClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nome_atv_ger;
        private ImageView imagem_atv_ger;
        private TextView horario_atv_ger;
        private Button editarAtividade, deletarAtividade;
        private CardView cardViewGer;

        public MyViewHolder(View itemView) {
            super(itemView);
            nome_atv_ger = itemView.findViewById(R.id.nome_atv_ger);
            horario_atv_ger = itemView.findViewById(R.id.horario_atv_ger);
            imagem_atv_ger = itemView.findViewById(R.id.imagem_atv_ger);
            editarAtividade = itemView.findViewById(R.id.btEditarAtividade);
            deletarAtividade = itemView.findViewById(R.id.btDeletarAtividade);
            cardViewGer = itemView.findViewById(R.id.cardViewGer);

        }
    }

}
interface ClickListenerGer<T> {
    void onItemClick(T data);
}
