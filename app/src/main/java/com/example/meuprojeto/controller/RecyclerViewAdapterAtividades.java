package com.example.meuprojeto.controller;


import android.graphics.Color;
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
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class RecyclerViewAdapterAtividades extends RecyclerView.Adapter<RecyclerViewAdapterAtividades.MyViewHolder> {
    private List<Atividade> listaAtividades;
    private ClickListener<Atividade> clickListener;


    public RecyclerViewAdapterAtividades(List<Atividade> listaAtividades) {
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
        if(atividade.getHorario() != null && atividade.getStatus() !=null){
            //Verificando horário atual:
            Date d = new Date();
            Calendar c = new GregorianCalendar();
            c.setTime(d);
            c.set(Calendar.HOUR_OF_DAY, d.getHours());
            c.set(Calendar.MINUTE, d.getMinutes());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            String horaAtual = simpleDateFormat.format(c.getTime());
            try {
                //Verificando se já passou do horário da atividade:
                Date present = simpleDateFormat.parse(horaAtual);
                Date closed = simpleDateFormat.parse(atividade.getHorario());
                if (present.after(closed) || atividade.getStatus().equals("1")) {
                    Log.e("hora:", "horário posterior");
                    holder.cardView.setCardBackgroundColor(Color.LTGRAY);
                }else{
                    Log.e("hora:", "horário anterior");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


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