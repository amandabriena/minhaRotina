package com.example.meuprojeto.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.AtividadeAgendada;

import java.util.List;

public class RecyclerViewAdapterAtividadesAgendadas extends RecyclerView.Adapter<RecyclerViewAdapterAtividadesAgendadas.MyViewHolder> {
    private List<AtividadeAgendada> listaAtividadesAg;
    private ClickListener<AtividadeAgendada> clickListenerDelete, clickListenerEdita;
    public RecyclerViewAdapterAtividadesAgendadas(List<AtividadeAgendada> listaAtividades) {
        this.listaAtividadesAg = listaAtividades;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_agendadas, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AtividadeAgendada atividade = listaAtividadesAg.get(position);

        holder.nome_atv_ger.setText(atividade.getNomeAtividade());
        holder.horario_atv_ger.setText(atividade.getHorario());
        holder.btDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerDelete.onItemClick(atividade);
            }
        });
        holder.btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerEdita.onItemClick(atividade);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaAtividadesAg.size();
    }


    public void setOnItemClickListenerDeletar(ClickListener<AtividadeAgendada> movieClickListener) {
        this.clickListenerDelete = movieClickListener;
    }
    public void setOnItemClickListenerEditar(ClickListener<AtividadeAgendada> movieClickListener) {
        this.clickListenerEdita = movieClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nome_atv_ger;
        private TextView horario_atv_ger;
        private Button btEditar, btDeletar;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            nome_atv_ger = itemView.findViewById(R.id.nome_atv_ger);
            horario_atv_ger = itemView.findViewById(R.id.horario_atv_ger);
            btEditar = itemView.findViewById(R.id.btEditarAtividade);
            btDeletar = itemView.findViewById(R.id.btDeletarAtividade);
            cardView = itemView.findViewById(R.id.cardViewAgendadas);

        }
    }

}
