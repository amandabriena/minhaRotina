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

public class RecyclerViewAdapterFeedbackAtividades extends RecyclerView.Adapter<RecyclerViewAdapterFeedbackAtividades.MyViewHolder>{
    private List<Atividade> listaAtividadesGer;

    public RecyclerViewAdapterFeedbackAtividades(List<Atividade> listaAtividadesGer) {
        this.listaAtividadesGer = listaAtividadesGer;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_feedback, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Atividade atividade = listaAtividadesGer.get(position);

        holder.nome_atv.setText(atividade.getNomeAtividade());
        int porcentagemFeedback = (atividade.getSomatorioFeedBack()/ atividade.getTotalRealizada())*100;
        holder.porcentagem_feedback.setText(porcentagemFeedback+"% de aprovação");
        int porcentagemConclusao = (atividade.getQtVezesConcluida()/ atividade.getTotalRealizada())*100;
        holder.porcentagem_conclusao.setText("Percentual conclusão: "+porcentagemConclusao+"%");
        Picasso.get().load(atividade.getImagemURL()).into(holder.imagem_atv);


    }

    @Override
    public int getItemCount() {
        return listaAtividadesGer.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nome_atv;
        private ImageView imagem_atv;
        private TextView porcentagem_feedback, porcentagem_conclusao;
        private CardView cardViewGer;

        public MyViewHolder(View itemView) {
            super(itemView);
            nome_atv = itemView.findViewById(R.id.nome_atv);
            porcentagem_feedback = itemView.findViewById(R.id.porcentagemFeedback);
            porcentagem_conclusao = itemView.findViewById(R.id.porcentagemConclusao);
            imagem_atv = itemView.findViewById(R.id.imagem_atv);

            cardViewGer = itemView.findViewById(R.id.cardViewGer);

        }
    }

}
